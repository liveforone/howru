package howru.howru.globalUtil

import howru.howru.exception.exception.BindingException
import org.springframework.validation.BindingResult
import java.util.*

fun validateBinding(bindingResult: BindingResult) {
    if (bindingResult.hasErrors()) {
        val errorMessage = Objects
            .requireNonNull(bindingResult.fieldError)
            ?.defaultMessage
        throw errorMessage?.let { BindingException(it) }!!
    }
}