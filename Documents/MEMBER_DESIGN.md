# 회원 & JWT 설계

## 회원관리 기술
* 세션보다 가볍고 관리하기 편한 Jwt 토큰을 활용해 서버에 부담을 최대한 줄이는 방식을 채택하였습니다.
* 토큰관리는 가장 보편적인 방식인 프론트엔드의 로컬스토리지에서 jwt토큰을 관리하는것을 전제로 합니다.
* access 토큰의 만료시간은 2시간 입니다. refresh 토큰의 만료시간은 30일 입니다.

## 상세 설계
* 외부 식별자 이름은 uuid로 합니다. 다른 도메인에서 사용하게된다면 memberUUID 형태로 사용합니다.
* 이는 회원의 pk를 외부에 노출하지 않기 위함입니다.
* 회원은 member, admin, 두 종류가 있습니다.
* 비밀번호는 모두 bcrypt로 암호화 합니다.
* 회원의 비밀번호는 변경 가능합니다.
* 로그인은 스프링 시큐리티에 위임합니다
* 정지된 계정인지는 로그인시에 판별합니다.
* 회원은 잠금이 가능하며, 잠금 회원의 경우 맞팔로우한 회원만 해당 회원의 게시글이나 프로필에 접근 가능합니다.
* 잠금 상태는 회원가입시 기본적으로 off로 처리됩니다. 자유롭게 on/off 변경 가능합니다.
* 정규화를 하여 회원의 상태와 신고 로직은 ReportState 테이블로 이관하였습니다.
* refresh-token 또한 정규화를 통해 테이블을 생성하였지만, 회원문서에서 설명합니다.
* refresh-token을 이용한 jwt 토큰 재발급 매커니즘은 [refresh token을 이용한 jwt 토큰 재발급 매커니즘](https://github.com/liveforone/howru/blob/master/Documents/JWT_TOKEN_REISSUE.md)을 참조하시면 됩니다.

## 회원 탈퇴 매커니즘
* 회원이 탈퇴되면 바로 계정이 삭제되지 않습니다. 
* 각국의 개인정보 보호 정책에 따라 삭제하면 안되는 경우도 있습니다.
* 따라서 auth를 withdraw로 설정하고, 모든 회원 조회쿼리에 auth가 `not equal withdraw` 조건절을 넣어서 
* withdraw 권한을 가진 회원은 조회되지 않도록 합니다.
* 또한 이렇게 withdraw된 회원이 똑같은 id로 중복가입을 막기위해 duplicate email validation에서는 auth가 withdraw인 회원을 포함하여 검증합니다.
* 후에 회원이 복구 api를 이용하여 접근하면 auth를 member auth로 전환시켜서 다시 활동이 가능하도록 합니다.
* 당연히 이 api는 시큐리티 설정에 외부접근을 permit 시켜주어서 jwt 토큰없이도 접근 가능하도록 합니다. 이때 사용자로부터 email과 pw를 json으로 받고,
* pw를 체크하여 pw가 올바른 경우 복구 시켜주도록 하였습니다.
* 만약 여러가지 회원 권한(어드민을 제외한다. 어드민은 탈퇴하지 않는다.)이 존재하는 경우에는 각 권한별로 api를 만들어서 처리하거나, 
* 복구 api에서 각 권한을 json에 포함시켜 받아서 각 권한별 복구를 진행하면됩니다.
* 아래는 이해를 돕기 쉽게 전체적인 매커니즘을 코드로 표현했습니다.
```
function 탈퇴() {
    회원 auth -> withdraw로 변경
}

function 복구(이메일, 비밀번호) {
    if (회원이 존재할때) {
        if (비밀번호가 맞으면) {
            회원 auth -> member로 변경
        } else {
            예외 발생
        }
    } else {
        예외 발생
    }
}
```

## API 설계
```
[POST] /member/signup
[POST] /member/login
[GET] /member/info
[PATCH] /member/update/password
[PATCH] /member/lock-on
[PATCH] /member/lock-off
[PUT] /auth/reissue
[POST] /member/logout
[POST] /member/recovery
[DELETE] /member/withdraw
```

## Json body 예시
```json
[SignupRequest]
{
  "email": "member1234@gmail.com",
  "pw": "1234",
  "nickName": "testName"
}

[LoginRequest]
{
  "email": "member1234@gmail.com",
  "pw": "1234"
}

[UpdatePassword]
{
  "password": "1111",
  "oldPassword": "1234"
}

[RecoveryRequest]
{
  "email": "member1234@gmail.com",
  "pw": "1234"
}

[WithdrawRequest]
{
  "pw": "112233"
}
```

## DB 설계
### member
```sql
create table member (
    id BINARY(16) not null,
    email varchar(255) UNIQUE not null,
    password varchar(100) not null,
    auth varchar(7) not null,
    nick_name VARCHAR(10) not null UNIQUE,
    member_lock varchar(3) not null,
    primary key (id)
);
CREATE INDEX id_auth_idx ON member (id, auth);
CREATE INDEX email_idx ON member (email);
CREATE INDEX email_auth_idx ON member (email, auth);
```
### refresh_token
```sql
create table refresh_token (
    id BINARY(16) not null,
    refresh_token varchar(255),
    primary key (id)
);
```