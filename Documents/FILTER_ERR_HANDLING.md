# 필터 에러 핸들링

## 필터 에러
* 필터 내부에서 문제가 발생한 경우 `controller advice`로는 해당 예외처리가 불가능하다.
* 일반적으로 custom 예외를 적절히 발생시키고, 이를 내부에서 처리하거나, 
* 외부(클라이언트)에게 전달이 필요한 경우 `controller advice`로 클라이언트에게 전달하는 것이 일반적이다.
* 그러나 이러한 작업이 필터에서 예외가 발생하면 할 수 없다는 것이다.

## 필터 예외처리
* 필터 내부에서 발생한 예외에 대해 처리하려면 `HttpServletResponse`를 이용해서 직접 클라이언트에게 전달해주어야 한다.
* response에 `status`, `content type`, 마지막으로 전달할 `body`(데이터)를 setting 해주는 방식으로 처리해야한다.

## 예제
* 아래는 jwt를 사용할때 jwt 내부에서 발생한 여러가지 예외를 `jwt custom exception`으로 묶고,
* custom exception 내부에 response message(body)를 두어서 각 예외마다 필요한 항목을 클라이언트에게 넘기도록 하였다.
```kotlin
} catch (e : JwtCustomException) {
    val httpResponse = response as HttpServletResponse
    httpResponse.status = e.jwtExceptionMessage.status
    httpResponse.contentType = MediaType.APPLICATION_JSON_VALUE
    val errorResponse = e.message
    val objectMapper = ObjectMapper()
    httpResponse.writer.write(objectMapper.writeValueAsString(errorResponse))
}
```

## 결론
* 이렇듯 시큐리티나 여타 config를 걸게될때 필터를 사용하는 경우가 잦다.
* 특히 필자는 filter를 사용할때 그러한데, 이럴때에는 controller advice를 사용할 수 없지만
* 위와 같은 방식으로 처리하여 controller advice와 같은 효과를 낼 수 있다.