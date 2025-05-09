package howru.howru.advertisement.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AdvertisementControllerAdvice {
    @ExceptionHandler(AdvertisementException::class)
    fun handleAdvertisementException(advertisementException: AdvertisementException): ResponseEntity<String> =
        ResponseEntity
            .status(advertisementException.advertisementExceptionMessage.status)
            .body(advertisementException.message + advertisementException.advertisementId)
}
