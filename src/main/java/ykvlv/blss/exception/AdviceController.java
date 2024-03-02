package ykvlv.blss.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ykvlv.blss.data.dto.response.ErrorResponse;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class AdviceController {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

    @ExceptionHandler(BEWrapper.class)
    ResponseEntity<ErrorResponse> BEWrapperHandler(BEWrapper e) {
        return ResponseEntity
                .status(e.getBusinessException().getHttpStatus())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponse> runtimeExceptionHandler(RuntimeException e) {
        log.error("ТВОЮ МАТЬ БЛ*ТЬ. ЧЕ У ВАС ЗДЕСЬ ПРОИСХОДИТ!?", e);

        return new ResponseEntity<>(
                new ErrorResponse("Внутренняя ошибка сервера, извините за неудобства"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        var result = e.getBindingResult();
        var errors = result.getFieldErrors().stream()
                .map((ee) -> ee.getField() + ": " + ee.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return new ResponseEntity<>(
                new ErrorResponse(errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableHandler() {
        return new ResponseEntity<>(
                new ErrorResponse("Ваш запрос содержит ошибку, назвать которую мы не можем. " +
                        "Напишите нам на почту и мы постараемся вам помочь"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> fileSizeLimitExceededExceptionHandler(FileSizeLimitExceededException e) {
        return new ResponseEntity<>(
                new ErrorResponse(String.format(
                        "Размер файла превышает допустимый размер (%s) на %fMB",
                        maxFileSize, (e.getActualSize() - e.getPermittedSize()) / 1024.0 / 1024.0)),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> sizeLimitExceededExceptionHandler(SizeLimitExceededException e) {
        return new ResponseEntity<>(
                new ErrorResponse(String.format(
                        "Размер запроса превышает допустимый размер (%s) на %fMB",
                        maxRequestSize, (e.getActualSize() - e.getPermittedSize()) / 1024.0 / 1024.0)),
                HttpStatus.BAD_REQUEST
        );
    }

}
