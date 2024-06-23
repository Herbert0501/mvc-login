package top.kangyaocoding.training.config;

import com.feiniaojin.gracefulresponse.GracefulResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        GracefulResponse.raiseException("500", "服务器异常！");
    }
}
