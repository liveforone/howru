# refresh token을 이용한 jwt 토큰 재발급 매커니즘

## 개요
* 상황과 환경에 따라 재발급 매커니즘은 바뀔 수 있다.
* 통상적으로 사용되는 방식이 아닌 필자는 필자만의 방식으로 처리했다.
* 들어가기 전 글을 이해하려면 `jwt` 토큰에 대해 잘 알고 있어야한다.
* 또한 필자는 프로젝트에서 회원의 외부 식별자로 db의 id를 쓰는 것이 아닌 따로 만들어 저장하고 있는 `uuid`를 사용한다.

## refresh token 저장
* `refresh token`은 `redis`와 같은 캐시에 저장하는 것이 좋다.
* 찾기 좋게 회원 `uuid`를 key로 두고 value에 `refresh token`을 저장하였다.

## refresh 토큰의 라이프 사이클
* 토큰은 로그인시 생성된다.
* 재발급할 경우 `refresh token`이 유효하고, 캐시에 저장된 값과 판별 후 재발급 한다.
* 토큰은 로그아웃시 캐시에서 삭제된다.
* 회원 탈퇴시 토큰 데이터는 삭제된다.

## 로그인 후 동작방식
* 로그인을 하게되면 `access token`과 `refresh token`을 발급하여 client에게 보낸다.
* 서버는 `refresh token`을 15일 `ttl`로 설정하여 저장한다. 만약 `redis`에 이미 값이 있을 경우 업데이트 된다.
* 일반적으로 대부분의 요청(회원가입/로그인 등을 제외한)에는 `access token`을 헤더에 넣어 서버에 보낸다.
* 그리고 필터는 이 토큰이 유효한지 판단한다.
* 이때 토큰이 만료된경우 필터에서는 토큰이 만료되었다는 response를 client로 보낸다.
* 여기서 필요한 것은 토큰의 재발급 요청을 처리할 api이다. 
* 해당 api로 client는 헤더에 refresh token과 회원의 식별자(uuid)를 넣어서 토큰의 재발급을 요청한다.
* 이 api는 시큐리티가 막지 않도록 permit all로 설정함에 유의한다.
* 헤더의 uuid(회원의 식별자)로 캐시에 저장된 `refresh token`을 찾는다.
* 찾은 `refresh token`과 헤더의 `refresh token`을 비교하고, 유효성 검사(만료/일치한지/empty 등)를 진행한다.
* 유효성이 적절하다 판단되면 `access token`과 `refresh token`을 새로 발급(refresh token rotation)하여 client에게 전달하고, 캐시의 refresh 토큰의 값을 변경한다.
* client는 다시 새 access 토큰을 헤더에 삽입하여 기존의 url로 재요청한다.

## 로그아웃 동작방식
* 로그아웃을 하게되면 캐시에 저장된 refresh 토큰을 삭제한다.

## 회원 탈퇴 동작방식
* 탈퇴시에는 로그아웃과 동일하게 캐시에서 삭제한다.
