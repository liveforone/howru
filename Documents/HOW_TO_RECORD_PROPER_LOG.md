# 적절한 로그를 기록하는 법

## 개요
* 우리는 프로젝트를 하며 로그를 남긴다. 그런데 이렇게 남기는 로그가 올바른 로그를 남기는 방법일까?
* 혹은 이런 로그가 적절한 것 일까? 필자는 이런 고민이 들었다. 
* 실제로 로그를 남기는 것이 시스템에 부하를 주기도 한다. 물론 쿼리처럼 큰 부하는 아닐지 몰라도 이것이 쌓인다면 시스템에 영향을 주는 것은 분명하다.
* 로그를 남길때 문자열을 그대로 쓰는 것은 좋지 못하다. 당연하겠지만 상수를 적극 활용하자.

## 적절한 로그?
* 적절한 로그는 무엇일까?
* 필자는 꼭 필요한 로그라고 생각한다.
* 로그에는 다양한 level이 있다. 이것에 대해 설명하진 않겠다.
* 그러나 이 level별로 정말 남겨야하는 내용들만 남기는 것이 적절한 로그라고 생각한다.
* warn level의 경우 대부분 꼭 남겨야하는 것들일 거다. 그러나 info로그는 조금 남발되고 있다고 필자는 생각한다.
* 따라서 로그를 남길때 이것이 꼭 기록되야 하는가? 에 대한 의문을 품으며 적절한 로그를 선별해 남기길 바란다.

## 꼭 필요한 로그
* 생성, 수정, 삭제는 필자가 느낄때 꼭 필요한 로그에 속한다고 생각한다. 
* 데이터가 변경되는 경우는 기록해야한다고 생각한다.
* 그리고 에러와 예외에 대해서도 로그를 남겨야 한다.
* 필자는 controller advice를 두고 여기서 예외를 받아 client로 넘겨주는 방식을 강하게 선호한다.
* 이런 추상화는 너무나 깔끔한 시스템을 만들 수 있기 때문이다. 그런데 이때 간과하는 것이 로그이다. 
* 예외는 분명 잘못된 input이나 logic, 에러 등에 의해서 발생하는 것인데 이를 그냥 client로 넘겨주기만 한다? 이는 적절치 않다고 생각된다.
* 이러한 예외에 대해서도 warn 혹은 error 레벨의 로그를 남겨야 한다고 본다. 필자는 대부분의 경우 warn level의 로그를 남긴다.


## 로그의 식별자
* 로그를 남길때 "로그인 하엿습니다."와 같은 수준의 로그는 남기는 의미가 전혀 없다.
* 로그에는 식별자가 필요하다. 위와 같은 상황에서는 어떤 회원이 로그인 했는지 같이 더해주어야한다.
* 필자는 특히나 회원의 id(pk)를 외부에 노출하는 것을 좋아하진 않는다. 이것에 대한 문서도 작성했었다. [내부/외부 식별자 구분](https://github.com/liveforone/howru/blob/master/Documents/INTERNAL_EXTERNAL_PK.md)
* 필자의 경우 회원과 관련된 로그일때에는 회원의 외부식별자를 뒤에 붙여서 누가 로그의 주체인지 꼭 남긴다.
* 회원이 아니더라도 게시글이라면 어떤 게시글인지 식별할 수 있도록 해야한다.
* 이처럼 로그에 식별자는 상당히 중요하다. 그리고 이 식별자를 아무렇게 붙이는 것이 아니라 로그의 내용을 이해할 수 있는 식별자를 사용해야한다는 점을 반드시 잊지 말아야한다.
* 일례로 게시글 좋아요와 관련된 로그인데 회원의 id를 식별자로 쓰는 것은 적절치 못하다. 이 경우에는 게시글 id가 훨씬 적절할 것이다.
* 항상 로그의 주체와 목적을 잘 파악해서 작성해야한다.

## 코틀린에서 적절한 로그 상수 객체 타입
* 이 부분은 코틀린을 사용하는 경우에 해당한다.
* 코틀린을 사용할떄 로그는 어떤 객체 타입을 써야할까?
* 로그 상수로 보통 enum을 쓰는 경우가 많을 것이다.
* 그런데 코틀린에서 이 방법은 별로 좋지 않다.
* 코틀린은 자바와 달리 object라는 타입을 지원한다.
* 로그는 보통 단순한 상수이다. 복잡한 상수가 아니다!
* enum은 각 상수가 해당 enum 타입의 인스턴스이기 때문에 추가적인 오버헤드가 발생할 수 있다.
* 따라서 enum보다 경량화된 object를 쓰는 것이 로그 상수를 사용하는데에 좋다.

## 로그를 중복해 남기지 말아라 - 로그와 성능
* 똑같은 로그를 중복해서 찍는 것은 성능에 악영향을 미치고, 쓸모없는 로그를 만든다.
* 일례로 회원가입이 성공할경우 controller 단에서도 성공 로그를 찍고, 내용은 다를 수 있지만 주제는 똑같은 로그를 service단에서도 찍는다면 이것은 잘못된 것이다.
* 로그는 계층에 따라 성격을 나누어서 기록하는 것이 필자가 고안한 아이디어다.
* controller 단에는 성공에 대한 로그를 찍는다. 일례로 게시글 수정시 "게시글 수정 성공"과 같은 로그이다.
* service 단에는 실패에 대한 로그를 찍는다. service 단에서는 validate를 비롯한 다양한 값들에 대한 평가와 처리가 이루어진다.
* 또한 예외를 많이 터뜨리기도 한다. 따라서 service 단에는 실패에 대한 로그를 남김으로써 로그가 중복되지 않도록 하고 이를 통해 유의미한 로그를 만들고 성능 향상을 가져올 수 있다.
* 개요 단에서 말했듯이 로그 또한 성능에 영향을 미친다. 쿼리와 같은 성능은 아니더라도 상당히 많이 사용되는 로그의 특성상 쌓이고 쌓이면 이 또한 성능에 악영향을 준다.
* 따라서 꼭 필요한, 그리고 적절한 로그를 남김으로써 문제가 발생할때 쉽게 문제를 찾아 해결할수 있도록 도와주는 이정표를 만들 수 있도록 해야한다.