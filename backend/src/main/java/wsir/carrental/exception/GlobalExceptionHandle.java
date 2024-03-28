package wsir.carrental.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import wsir.carrental.util.Result;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public <T> Result<T> handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }
}
