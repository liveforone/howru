# service가 service를 의존할 때, facade로 결합도 낮추기

## 목차

- [service가 service를 의존할 때, facade로 결합도 낮추기](#service가-service를-의존할-때-facade로-결합도-낮추기)
    - [목차](#목차)
    - [service가 service를 의존하는 상황](#service가-service를-의존하는-상황)
    - [방법1 - controller에서 여러 service를 호출한다.](#방법1---controller에서-여러-service를-호출한다)
    - [방법2 - facade 패턴을 사용하고 서비스를 상/하위로 나눈다.](#방법2---facade-패턴을-사용하고-서비스를-상하위로-나눈다)
    - [facade의 단점?](#facade의-단점)
    - [facade에 들어가면 좋은 로직](#facade에-들어가면-좋은-로직)
    - [전체적인 구조 및 상세 설명](#전체적인-구조-및-상세-설명)
    - [구현](#구현)
    - [트랜잭션 전파에 관한 고민](#트랜잭션-전파에-관한-고민)
    - [결론](#결론)

## service가 service를 의존하는 상황

- 개발을 하다보면 비즈니스 로직이 복잡해지는 도메인에서 service가 service를 의존하는 상황이 발생한다.
- 이와 비슷하게 repository가 repository를 의존하는 일도 있는데, 이 경우는 주로 연관관계 테이블을 조회하여 저장하는 과정이 대부분이라 큰 문제가 없다.
- 그러나 service가 service를 의존하면 많은 문제가 발생한다.
- 대표적으로 순환참조 문제이다. 순환참조 문제는 필자의 경우 service가 service를 의존하는 경우에 많이 발생했다.
- 무조건 순환참조 문제가 발생하진 않는다. 다만 머릿속에 염두하지 않고 프로그래밍 할 경우 순환참조 문제가 빈번히 발생했다.
- 이 말인 즉슨 협업시에 이러한 불 안전성을 가진 로직은 반드시 순환참조를 발생시킨다.
- 또 이 뿐만이 아니라 높은 결합도를 가지게 되고, 한 service가 많은 책임을 가지게 된다.
- 이러한 이유로 service가 service를 의존할때, 결합도를 낮추는 방법을 고민하게 되었다.

## 방법1 - controller에서 여러 service를 호출한다.

- mvc 구조에서는 controller -> service -> repository 순으로 참조해가며 나아간다.
- 이러한 구조의 특징을 살려 최상위 호출로직인 controller에서 다양한 도메인의 service를 생성자 주입한 후,
- 호출하는 방법이 있다. 그러나 이 방법은 비즈니스 로직이 controller에 있게 되고,
- 또 트랜잭션을 사용하는 경우 서로 다른 트랜잭션이 동작하며 이에 따라 원자성을 깨뜨리게 된다.

## 방법2 - facade 패턴을 사용하고 서비스를 상/하위로 나눈다.

- facade는 어려운 말처럼 들릴 수 있어도, 초등학생도 생각해 낼 수 있는 개념이다.
- 바로 하나의 클래스가 여러 클래스를 참조하며, 저수준의 객체들을 하나의 고수준 객체에서 사용하는 방식이라 보면 된다.
- 앞선 방법1 처럼 하나의 controller에서 여러 service를 호출하는 방법과 유사하다.
- 그러나 다른 점은 facade를 service로 선언하면 트랜잭션을 개발자 맘대로 조정할 수 있게 된다는 점이다.
- 이에 따라 facade service를 만들어서 처리하는 방법을 이용하는 것이 가장 베스트인 것으로 판단되었다.

## facade의 단점?

- 다만 facade도 피해갈 수 없는 것은 facade가 많은 책임을 지고 있다는 것이며, facade는 강합 결합도를 가지고 있다는 것이다.
- 이러한 단점이 있기에 모든 로직을 facade에 넣는 것이 아닌 아래 필자가 고안한 방법론에 따라 facade를 사용하면 좋을 것 같다.

## facade에 들어가면 좋은 로직

- 일단 service가 service를 의존해야한다.
- 그러나 이러한 의존이 단순히 조회 쿼리를 호출하는 함수를 의존하는 정도라면, 굳이 facade 클래스를 만들어서 처리할 필요가 없다.
- 내부에 단순 조회한 데이터를 사용하는 것이 아니라, 비즈니스 로직이 있는 경우에는 facade를 만들어 사용하면 좋다.
- 또 많은 service를 하나의 함수에서 참조하는 하는 경우, 사용하면 아주 좋다.

## 전체적인 구조 및 상세 설명

- 전체적인 호출구조는 아래와 같다.

```
controller -> facade service(IntegratedXxService) & service(XxService)
facade service(IntegratedXxService) -> service(Xxservice)
service -> repository
```

- 컨트롤러는 퍼사드 서비스와 일반 서비스를 모두 호출한다.
- 퍼사드 서비스는 일반 서비스들을 호출한다. 그러나 repository는 호출하지 않는다.
- repository는 오로지 일반 서비스들만 호출한다.
- 쉽게 말해 service 계층을 상위/하위로 나누어서 컨트롤러에서 사용하도록 하는 것이다.
- 그리고 상위/하위 서비스는 각각 접근할 수 있는 범위를 제한하여,
- 많은 책임을 가지고 있는 서비스의 경우 repository 단까지 접근하지 못하도록 막는 것이다.

## 구현

- 구현 예시는 아래와 같다.

```kotlin
@Service
@Transactional(readOnly = true)
class IntegratedPostService
    @Autowired
    constructor(
        private val memberQueryService: MemberQueryService,
        private val subscribeQueryService: SubscribeQueryService,
        private val postQueryService: PostQueryService
    ) {
        fun getPostOfOtherMember(
            memberId: UUID,
            myId: UUID,
            lastId: Long?
        ): PostPage {
            val writer = memberQueryService.getMemberById(memberId)
            return if (writer.isUnlock() || subscribeQueryService.isFollowee(memberId, myId)) {
                postQueryService.getPostsByMember(memberId, lastId)
            } else {
                logger().warn(IntegratedPostServiceLog.NOT_FOLLOWER + memberId)
                throw SubscribeException(SubscribeExceptionMessage.NOT_FOLLOWER, myId)
            }
        }
    }
```

- 위 클래스는 원래 도메인인 post service 이외에도 member와 subscribe 도메인을 필요로 한다.
- 심지어 하나의 함수에서 이 두 도메인을 사용한다.
- 위의 함수는 뜻은 간단한데, 다른 회원의 게시물을 조회할때, 비공개 회원이 아니거나, 내가 팔로우 하고 있는 회원이라면 게시글 조회를 가능하도록 하는 로직이다.
- 이 클래스는 오로지 service 만을 사용하며, repository 계층에는 접근하지 않는다.
- 책임이 많고 결합도가 높기 때문에, 많은 권한을 부여하지 않는 것이 핵심이다.

## 트랜잭션 전파에 관한 고민

- 위 예제는 readOnly 이기 때문에 별 문제가 없지만,
- command 작업의 경우 트랜잭션을 사용하므로 트랜잭션 전파에 관한 고민을 하는 독자가 있을 수 있다.
- 그러나 이는 jpa의 트랙잭션에 대해 잘 몰라서 그렇다.
- jpa의 트랜잭션은 기본적으로 REQUIRED로, 이는 이미 시작된 트랜잭션이 있다면 그 트랜잭션에 참여하는 전파방식이다.
- 따라서 a, b, c 함수를 하나의 퍼사드 클래스 함수가 호출한다면, 이때 a, b, c중 하나의 메서드에서 예외가 터질경우 모두 롤백된다.
- 따라서 트랜잭션을 사용하는 경우에도 전혀 문제없이 퍼사드 패턴을 사용할 수 있다.

## 결론

- 도메인마다 도메인의 서비스만을 사용하면 가장 좋겠지만, 다른 도메인과 소통하고,
- 또 주문로직처럼 아예 여러 도메인 로직이 섞여서 작동하는 복잡한 비즈니스 로직을 다룰때 이러한 패턴을 사용하면 아주 좋다.
- 하나로 모아 응집력있고, 결합도는 높더라도 접근에 제한을 두는 전략을 사용하여 안전하게 높은 결합도와 책임을 다룰 수 있도록 한다.
