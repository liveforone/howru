# 동일한 url에 쿼리 스트링이 다를 때 여러 메서드로 분리하기

## 문제 발생
* 동일한 url에 쿼리 스트링이 다를 때 여러 메서드로 분리하여 작성하면 에러가 발생한다.
* url에 필수 쿼리 스트링이 다르다는 것은 서로 별개의 uri임을 의미한다.
* 그런데도 스프링은 이를 인지하지 못한다.
* 예시를 들면 /posts?member-id={memberId}와 /posts api 메서드를 만들때 `@GetMapping("/posts/")`라는 동일한 값을 사용한다.
* 그리고 `@RequestParam()`을 사용해서 쿼리스트링을 표현하여도 스프링은 이를 동일한 uri라고 인식하여
* 동일한 uri는 하나의 컨트롤러 메서드만 만들 수 있다고 하며 에러를 발생시킨다,

## 명시적 쿼리스트링을 지정하여 해결한다.
* 이를 해결하려면 `@RequestMapping`어노테이션이나, 이를 오버라이드한 `@GetMapping`등과 같은 `@XxMapping` 어노테이션의 `params` 매개변수를 이용해야한다.
* 이 옵션을 사용하면 스프링은 단번에 쿼리스트링이 다른, 완전히 별개의 uri임으로 인식하고 에러를 발생시키지 않는다.

## 사용 예시
* 사용 예시는 아래와 같다.
* 평소에는 상수를 이용해 url과 파라미터등을 표현하지만 예시에서는 알아보기 쉽게 raw string을 사용하였다.
```kotlin
@GetMapping("/posts", params = ["member-id"])
fun postOfOtherMember(
            @RequestParam("member-id") memberId: UUID,
            @RequestParam("last-id", required = false) lastId: Long?,
            principal: Principal
): ResponseEntity<PostPage> {
            val posts = integratedPostService.getPostOfOtherMember(memberId, UUID.fromString(principal.name), lastId)
            return ResponseEntity.ok(posts)
}
```
* last-id 쿼리스트링은 선언에서 알 수 있듯 required 옵션이 false이다. 즉 필수가 아니다.
* 그러나 member-id 쿼리스트링은 필수 쿼리스트링으로, 이는 params 매개변수를 통해서 명시적으로 지정해주었다.
* 이렇게 선언해주면 /posts를 사용하는 또 다른 메서드가 있을때에도 문제가 발생하지 않는다.

## 결론 및 요약
* /posts?member-id={memberId}와 /posts는 완전히 별개의 uri이다.
* 그런데 스프링에서 두개의 메서드를 만들고 동일한 url인 /posts를 사용하면 에러가 발생한다.
* 이를 해결하려면 필수 쿼리스트링이 완전히 다른 uri임을 명시적으로 스프링에게 알려줘야한다.
* 그 방법은 Mapping 어노테이션의 params 옵션을 사용하는 방법이다.
* 주의 할점은 필수 쿼리스트링만 params 옵션에 넣는다.
