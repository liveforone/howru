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
   |ㅡ howru
        |ㅡ 도메인1
        |ㅡ 도메인2
        |ㅡ global
              |ㅡ config
              |ㅡ exception
              |ㅡ util
        |ㅡ converter
```

## 도메인 디렉토리 구조
* 도메인의 상세 디렉토리는 아래와 같다.
* controller에서 사용하는 상수(url, param, header)등은 `도메인ControllerConstant`파일에 모두 집어 넣는다.
* command작업에서 클라이언트에게 전달할 정보(주로 성공 메세지 + http status)는 `도메인Response`에 담아 전달한다.
```
ㅡ 도메인
    |ㅡ cache
    |ㅡ controller
          |ㅡ constant
                |ㅡ 도메인ControllerConstant.kt
          |ㅡ response
                |ㅡ 도메인Response.kt
    |ㅡ domain
          |ㅡ constant
          |ㅡ vo
          |ㅡ 도메인.kt
          |ㅡ 엔티티에 사용되는 enum.kt
    |ㅡ dto
    |ㅡ exception
          |ㅡ 도메인ControllerAdvice.kt
          |ㅡ 도메인Exception.kt
          |ㅡ 도메인도메인ExceptionMessage.kt
    |ㅡ log
    |ㅡ repository
    |ㅡ service
          |ㅡ command
          |ㅡ query
```
