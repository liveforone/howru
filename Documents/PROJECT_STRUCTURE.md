# 프로젝트 구조 설계
> 프로젝트의 전반적인 구조에 대해 기술한다.

## intro
* 이 프로젝트는 기본적으로 도메인형 구조로 디자인 되었다.
* 도메인 형 구조란 각 도메인 별로 디렉토리를 만들고 그 안에 해당 도메인의 db layer, business layer, web(controller) layer 등을 넣은 구조를 말한다.
* 아래 디렉토리 표현에서 파일의 이름이 등장하는 것은, 해당 구조를 강제화 하려함이다.
* 개발자가 임의로 이름 짓는 것이 아닌, 강제하기 위함이다.

## 루트 디렉토리를 기준으로(src ->)
* 루트를 기준으로 전반적인 디렉토리는 아래와 같다.
```
ㅡ howru
   ㄴ howru
   |     ㄴ 도메인1
   |     ㄴ 도메인2
   |     ㄴ global
   |     |     ㄴ config
   |     |     ㄴ exception
   |     |     ㄴ util
   |     ㄴ  converter
```

## 도메인 디렉토리 구조
* 도메인의 상세 디렉토리는 아래와 같다.
* controller에서 사용하는 상수(url, param, header)등은 `도메인ControllerConstant`파일에 모두 집어 넣는다.
* command작업에서 클라이언트에게 전달할 정보(주로 성공 메세지 + http status)는 `도메인Response`에 담아 전달한다.
```
ㅡ 도메인
    ㄴ cache
    ㄴ controller
    |      ㄴ constant
    |      |      ㄴ 도메인ControllerConstant.kt
    |      ㄴ response
    |             ㄴ 도메인Response.kt
    ㄴ domain
    |      ㄴ constant
    |      ㄴ vo
    |      ㄴ 도메인.kt
    |      ㄴ 엔티티에 사용되는 enum.kt
    ㄴ dto
    ㄴ exception
    |      ㄴ 도메인ControllerAdvice.kt
    |      ㄴ 도메인Exception.kt
    |      ㄴ 도메인도메인ExceptionMessage.kt
    ㄴ log
    ㄴ repository
    ㄴ service
    |      ㄴ command
    |      ㄴ query
```
