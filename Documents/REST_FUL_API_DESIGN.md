# restful한 api 설계

## 규칙
* api설계에는 다음과 같은 규칙을 따른다.
* http method를 활용하여 api를 재활용하는 restful 한 api를 사용한다.
* 생성 이외에는 식별자(통상 id라 칭함)를 path에 삽입하여 쓰는 것이 좋다.
* json에 식별자를 넣지말고 path에 삽입하라.
```
생성(post) : /user
삭제(delete) : /user/{id}
조회(get) : /user/{id}
수정(put) : /user/{id}/task
```
* 그러나 이해를 돕기위해 작업을 명시적으로 부여하는 것은 가능하다.
* 작업을 부여할때, 일례로 `수정`작업을 한다고 치면, 이 작업이 단건만 존재할경우 `edit-title` 처럼 `작업-무엇` 형태로 배치한다.
* 그러나 여러건 존재할 경우 `edit/title`처럼 `작업/무엇` 형태로 배치한다.
```
[단건]
/post/{id}/edit-content

[여러건]
/post/{id}/edit/title
/post/{id}/edit/content
```
* 회원과 관련되 api에는 path에 id를 삽입하지 않는다. 이는 보안을 위함이다.
* jwt 토큰을 통해 회원의 식별자를 내부적으로 파악하는 방식을 사용하고, 외부에 식별자를 노출시키지 않는다.

## http method
* 수정시에는 put 메서드를 사용하여 전체 컬럼을 받아 업데이트 하는 것을 권고한다.
* 그러나 이는 데이터 필드 수가 적을때 이야기이며, 개발자가 판단할때 필드가 많다면 patch를 통해 필요한 부분만 업데이트 하는 것을 권장한다.
* 이유는 필드가 많을 경우 오버헤드가 발생할 수 있기 때문이다.
* jpa의 더티체킹을 이용하는 경우일지라도 client로 부터 전체 필드를 받아 업데이트 하는 것이 아니라면 patch로 처리한다.
* 이 설계 문서는 해당 프로젝트에서만 사용하는 규약이다.