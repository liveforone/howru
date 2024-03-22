# jjwt 변경사항에 따른 로직 변화

## jjwt 변경
* jjwt가 `0.12.X`으로 업데이트 됨에 따라 다음과 같은 변경이 이루어졌다.
* 바로 몇몇 메서드의 deprecated와 secret key이다.
* 몇몇 메서드의 deprecated는 대체 메서드를 문서에서 찾아 변경하면 되는데, 
* secret key 문제는 고치지 않으면 프로그램이 정상적으로 돌아가지 않는다.

## secret key 생성 제공
* 기존에는 개발자가 secret key를 미리 생성해두고 그 키를 기반으로 사용해야했다.
* 그러나 그런 문자열 기반의 secret key 방식이 보안상 안전하지 않다는 jjwt팀의 판단에 따라
* jjwt에서 키를 생성하는 방식으로 키를 사용해야한다.
* 키는 다음과 같이 생성한다.
```kotlin
private val key = Jwts.SIG.HS256.key().build()
```

## 키 생성 제공 방식의 주의점
* 이러한 방식은 주의할 점이 있다.
* 서버가 죽을 경우, 서버를 다시 살리게 되면 기존에 사용하던 키와 달라져서 
* 만료되지 않은 토큰일지라도 유효하지 않다고 판단된다.
* 즉 서버가 죽었다가 다시 살아나면, 모든 클라이언트들은 재 로그인을 해야 정상적으로 이용이 가능해진다.

## accessToken 생성 코드
* accessToken 생성은 다음과 같이 한다.
```kotlin
private fun generateAccessToken(authentication: Authentication): String {
        return Jwts.builder()
            .subject(authentication.name)  //sub은 자유롭게 삽입하라
            .claim(JwtConstant.CLAIM_NAME, authentication.authorities.iterator().next().authority)
            .expiration(Date(Date().time + JwtConstant.TWO_HOUR_MS))  //유효기간은 자유롭게 설정하라
            .signWith(key)
            .compact()
}
```

## parsing
* `verifyWith` 함수를 사용한다.
```kotlin
private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken).payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
}
```