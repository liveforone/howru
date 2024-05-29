# RedisTemplate 만을 사용해서 @Cacheable 어노테이션 대체하기

## object mapper 문제 발생과 이에 따른 aop 사용불가
* `object mapper`의 버전이 번경되면서, 더 정확히는 코틀린 버전을 1.9.22에서 1.9.24로 변경함에 따라
* 기존에 사용가능 했던 `object mapper`의 `DefaultTyping.EVERYTHING`이 deprecated 되었다.
* 자바에서는 문제가 없지만, 코틀린을 사용하는 환경에서 문제가 발생했다.
* 즉 `@Cacheable`과 같은 스프링 캐시에서 제공하는 aop를 사용할 수 없게 되었다.
* 여러 가지 방법을 사용해보았지만, 결국 되지 않았다.

## aop가 없어도 괜찮아!
* aop를 사용해 편리하게 캐싱을 할 수 있어서 `cacheManager`를 세팅해왔었는데,
* 앞으로 이러한 기능을 사용하지 못할 것을 생각하니 기분이 썩좋진 않았다.
* 그러나 `@Cacheable`어노테이션 만으로는 만족하지 못할 정도로 캐싱 로직이 복잡해지는 현 상황에 대응하기 위해 `custom redis repository`를 만들어 사용했으므로,
* 아예 `custom redis repository`를 사용해서 redis 캐싱 로직을 완성하기로 마음을 굳혓다.
* 기존에 `custom redis repository`에는 `save()`, `delete()`, `getByKey()` 메서드만 존재했다.

## redis 캐싱 로직
* redis 캐싱 로직은 사실상 스프링 캐시 aop에서 제공하는 `@Cacheable`어노테이션과 동일한 로직을 가진다.
* 해당 어노테이션을 사용하면 자동으로 캐싱을 진행 한후 함수는 조회된 값을 리턴해버리고 종료된다.
* 즉 캐싱 이외의 로직을 수행하기 어려운 환경이었으나, 해당 어노테이션과 동일한 로직을 수행하는 함수를 만들어 호출하게되면
* 캐싱을 마친 후에도 원하는 비즈니스 로직을 수행할 수 있으므로 복잡한 캐싱 로직에서 대응하기 안성맞춤이다.
* 캐싱 로직은 다음과 같다.
* 첫번째로 redis에 키를 이용해서 데이터를 조회한다.
* 만약 redis에 데이터가 있다면 그 값을 `object mapper`를 사용해서 매핑할 클래스로 매핑한다.
* 만약 redis에 데이터가 없다면 db에서 값을 꺼내는, repository 메서드를 호출한다.
* 그리고 db에서 가져온 데이터를 string으로 변환 후 redis에 삽입한다.
* 최종적으로 조회된 데이터를 리턴한다.

## 캐싱 로직 추상화
* redis에 key와 value는 항상 string으로 저장된다는 조건을 깔고 가겠다.
```kotlin
class RedisTimeOut(
    val time: Long,
    val timeUnit: TimeUnit
)

fun <T> save(
    key: String,
    value: T,
    timeOut: RedisTimeOut? = null
) {
    timeOut?.let {
        redisTemplate.opsForValue().set(
            key,
            objectMapper.writeValueAsString(value),
            timeOut.time,
            timeOut.timeUnit
        )
    } ?: run {
        redisTemplate.opsForValue().set(
            key,
            objectMapper.writeValueAsString(value)
        )
    }
}

fun <T> getByKey(
    key: String,
    clazz: Class<T>
): T? {
    val result = redisTemplate.opsForValue()[key].toString()
    return if (result.isEmpty()) {
        null
    } else {
        return objectMapper.readValue(result, clazz)
    }
}

fun <T> getOrLoad(
        key: String,
        clazz: Class<T>,
        findDataFromDB: () -> T,
        timeOut: RedisTimeOut?
): T {
        val cachedValue: T? = getByKey(key, clazz)
        return cachedValue ?: findDataFromDB().also { data ->
            timeOut?.let { save(key, data, timeOut) } ?: save(key, data)
        }
}
```
* 매개변수로 받는 `clazz`는 string 데이터를 매핑 할 클래스 타입을 의미한다.
* `findDataFromDB`는 repository에서 데이터를 조회하는 함수이다.
* `timeOut`은 직접 만든 ttl 객체로 원하는 시간과 그 시간에 대한 정확한 정보(시, 분, 초, 밀리 초 등)를 가진다.
* `getByKey`함수를 호출하여 데이터를 조회하고, 만약 조회된 데이터가 없다면 db에서 조회하는 함수를 호출한다.
* 그리고 `timeout`객체를 매개변수로 받았다면 이 `timeout`을 사용해서 저장하고, 아니라면 그냥 저장한다.
* 이 함수를 아래와 같이 사용하여 위에서 제시한 redis 캐싱을 사용할 때마다 발생하는 로직을 추상화하여 코드를 단순화 할 수 있다.
```kotlin
val data = redisRepository.getOrLoad(
                PostCacheKey.POST_DETAIL + id,
                PostInfo::class.java,
                findDataFromDB = { postRepository.findPostInfoById(id) },
                RedisTimeOut(CacheTTL.TEN, TimeUnit.MINUTES)
)
```
* 적절한 key를 넣고, 가져올 데이터의 타입을 넣은 후, redis에 데이터가 없을시 데이터를 조회 할 함수를 넣고
* 마지막으로 `timeout`객체를 삽입하여 데이터를 조회함과 동시에 캐시에 데이터를 삽입한다.
