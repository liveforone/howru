# 정지 기간이 있는 계정 정지 방법

## 다양한 계정 정지 방법
* 계정을 정지하는 방법은 다양하다. 
* **"어떤 수만큼의 신고가 들어왔을때 완전 정지하여 회복이 불가능"** 하게 만들거나,
* **"정지 기간이 존재하고, 기간에 맞게만 정지하도록 하는 방법"**, 
* 마지막으로 필자가 소개 **"할 정지 기간이 있고 + 신고 수가 한계치에 도달하였을때 영구 정지처리하는 방법"** 이다.

## 날짜를 기반으로 동작한다.
* 이러한 방식은 날짜를 기반으로 동작한다.
* 정지 기간이 한달 이라면, 정지된 그날의 날짜를 저장하고 재 로그인 하는 날의 날짜를 비교하여
* 저장된 정지 기간만큼 날짜가 지났는지 판별해 정지를 풀어주거, 유지하면 된다.
* 필자는 아래와 같이 정지 날짜 컬럼을 두었다.
```kotlin
@Column(nullable = false, columnDefinition = DATE_TYPE) var modifiedStateDate: Int = getDateDigit(LocalDate.now())
```
* 또한 기간이 지났다면 정지를 풀어주는 코드는 아래와 같다.
```kotlin
fun releaseSuspend() {
        val now = LocalDate.now()
        val modifiedDate = convertDateToLocalDate(modifiedStateDate)
        when (memberState) {
            MemberState.SUSPEND_MONTH -> {
                val oneMonthReleaseDate = modifiedDate.plusMonths(MemberConstant.ONE_MONTH)
                if (now.isAfter(oneMonthReleaseDate) || now.isEqual(oneMonthReleaseDate)) {
                    memberState = MemberState.NORMAL
                }
            }
            MemberState.SUSPEND_SIX_MONTH -> {
                val sixMonthReleaseDate = modifiedDate.plusMonths(MemberConstant.SIX_MONTH)
                if (now.isAfter(sixMonthReleaseDate) || now.isEqual(sixMonthReleaseDate)) {
                    memberState = MemberState.NORMAL
                }
            }
            else -> Unit
        }
}
```

## 회원 상태가 필요하다.
* 회원이 정지되지 않았는지, 정지됬다면 몇개월 정지인지 ENUM을 활용하면 아주 편리하게 저장이 가능하다.
* 이 경우 정지가 풀렸다면 회원의 상태를 정상으로 돌려놓는 작업 또한 필요할 것이다.
* 또한 로그인 시에 회원의 상태를 분석해 로그인이 가능하도록 할 것인지 말것인지 판별도 가능하다.
* 필자는 아래와 같이 기본, 1개월 정지, 6개월 정지, 영구 정지와 같은 상태를 두었다.
```kotlin
enum class MemberState { NORMAL, SUSPEND_MONTH, SUSPEND_SIX_MONTH, SUSPEND_FOREVER }
```
* 정지되었는지 판별하는 코드는 아래와 같다.
* 필자는 로그인시 `require()` 혹은 `check()` 함수를 사용할 것이라서, 조건에 true한 함수를 만들었다.
```kotlin
fun isNotSuspend() = memberState == MemberState.NORMAL
```

## 로그인시 판별하기
* 로그인시에는 먼저 회원을 조회하여 정지 기간이 지났다면 정지를 풀고, 아니라면 그대로 두는 함수인 releaseSuspend() 를 호출한다.
* 후에 회원의 상태가 변경이 일어났을텐데, 최종적인 회원을 가지고 정지된 회원인지 판별하여 로그인 성공/실패를 리턴하도록 하였다.
```kotlin
override fun loadUserByUsername(email: String): UserDetails {
    val member = memberCommandService.releaseSuspend(email)
    require(member.isNotSuspend()) { throw MemberException(MemberExceptionMessage.SUSPEND_MEMBER) }
    return createUserDetails(member)
}
```

## 결론
* 이런식의 로직을 통해서 계정에 대한 정지 상태를 컨트롤 할 수 있도록 하였다.
* 회원의 정지는 주로 기간제에 따른 정지를 많이 쓰는 편인데, 위와 같은 방식으로 구현하면 될 것 같다.