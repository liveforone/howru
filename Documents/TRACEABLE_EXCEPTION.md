# 추적가능한 예외처리

## 추적가능한 예외처리란?
* 필자는 이 글을 작성하기 전에는 예외를 다음과 같이 구현하였다.
* 예외의 상황에 대한 설명문과 적절한 http status를 enum으로 만들어 controller advice로 전달한다.
* 그러나 이러한 방식은 문제점이 무엇일까? controller advice로 전달하고..어쩌구.. 이것이 문제가 되는게 아니라,
* 예외의 상황에 대한 설명만을 전달한다는 것이 문제이다.
* 예외의 상황 뿐만이 아니라, 지금 해당 예외가 게시글 커스텀 예외라면 어떤 게시글에서 발생하고 있는지 예외를 추적가능하게 구현하는것이 좋다.

## 식별자가 필요하다.
* 추적 가능한 예외를 만들려면 적절한 식별자를 선별해야한다.
* 필자는 api 설계/구현시 보안이 중요한경우 uuid를 사용하여 외부로 리턴하고, 아니라면 id를 리턴하는 방식을 취하고 있다.
* 예외 또한 마찬가지로 설계하였다. 결국에 전달하는 message는 String이기 때문에, 그것이 uuid이건 String이건 long 이건 상관없이 String형태로 변환하여 예외에 넘기기만 하면된다.
* 필자의 경우 회원은 email과 uuid를 식별자로, 게시글과 같은 것들은 id를, 복합키를 사용하는 엔티티는 복합키 모두를 식별자로 하여 추적가능하게 설계/구현하였다.

## 예시
* 회원엔티티가 제일 복잡하게 예외를 처리하고있기 때문에 회원 엔티티를 예시로 하겠다.
* 아래 코드는 custom exception으로 전달할 메세지와 http status를 담고 있는 enum클래스이다.
* enum 클래스는 건들게 아무것도 없다. 그냥 메세지와 http status를 그대로 작성하고, 뒤나 앞에 올 식별자에 대한 부연 설명(회원식별자 : )을 달아주면 된다.
```kotlin
enum class MemberExceptionMessage(val status: Int, val message: String) {
    SUSPEND_MEMBER(401, "정지된 계정입니다. 회원식별자 : "),
    WRONG_PASSWORD(400, "비밀번호를 틀렸습니다. 회원식별자 : "),
    MEMBER_IS_NULL(404, "회원이 존재하지 않습니다. 회원식별자 : "),
    DUPLICATE_EMAIL(400, "중복되는 이메일이 존재합니다. 회원식별자 : "),
    NOT_ADMIN(401, "운영자가 아니여서 광고 접근이 불가능합니다. 회원식별자 : ")
}
```
* 아래 코드는 enum클래스를 받는 회원 커스텀 예외 클래스이다.
* 
```kotlin
class MemberException(val memberExceptionMessage: MemberExceptionMessage, val memberIdentifier: String?) : RuntimeException(memberExceptionMessage.message)
```
* 아래는 회원 커스텀 예외 클래스를 받아 핸들링하는 controller advice이다.
```kotlin
@ExceptionHandler(MemberException::class)
fun memberExceptionHandle(memberException: MemberException): ResponseEntity<String> {
    return ResponseEntity
        .status(memberException.memberExceptionMessage.status)
        .body(memberException.message + memberException.memberIdentifier)
}
```
* 아래는 예외를 사용하는 코드이다.
* String 형태의 어떤 값이던, 회원의 식별자로 쓸 수 있는 것이면 된다.
* 첫번째 예시는 email을 식별자로 받고 있다.
* 두번째 예시는 회원의 uuid(외부식별자)를 받고 있다.
```kotlin
check(reportState.isNotSuspend()) { throw MemberException(MemberExceptionMessage.SUSPEND_MEMBER, email) }
require (isMatchPassword(oldPassword, pw)) { throw MemberException(MemberExceptionMessage.WRONG_PASSWORD, uuid.toString()) }
```

## 한계
* 필자가 구현한 형태의 추적가능한 설계의 한계는 바로 식별자를 원하는 위치에 넣지 못한다는 것이다.
* 식별자를 메세지의 앞, 혹은 뒤에만 삽입 가능하다. 원하는 위치에 삽입하려면 수정이 필요하다.
* 일반적인 상황에서는 앞 혹은 뒤에 식별자를 삽입하는게 대부분이므로, 대다수의 개발자들은 위처럼 추적가능한 예외를 구현하면된다.