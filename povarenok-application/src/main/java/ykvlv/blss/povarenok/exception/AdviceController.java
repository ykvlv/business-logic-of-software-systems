package ykvlv.blss.povarenok.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ykvlv.blss.povarenok.data.dto.response.ErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class AdviceController {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(BEWrapper.class)
    ResponseEntity<ErrorResponse> BEWrapperHandler(BEWrapper e) {
        return ResponseEntity
                .status(e.getBusinessException().getHttpStatus())
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(AccessDeniedException e) {
        return new ResponseEntity<>(
                new ErrorResponse("Доступ запрещен"),
                HttpStatus.FORBIDDEN
        );
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
    public ResponseEntity<ErrorResponse> httpMessageNotReadableHandler(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException cause) {
            String pathReference = cause.getPathReference(); // Получаем ссылку на путь
            String attributeName = StringUtils.substringBetween(pathReference, "\"", "\""); // Извлекаем имя

            return new ResponseEntity<>(
                    new ErrorResponse(String.format("%s: Значение '%s' отсутствует в списке допустимых",
                            attributeName,
                            cause.getValue())),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                new ErrorResponse("Ваш запрос содержит ошибку, назвать которую мы не можем. " +
                        "Напишите нам на почту и мы постараемся вам помочь"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
        return new ResponseEntity<>(
                new ErrorResponse(String.format(
                        "Размер файла превышает допустимый размер (%s)",
                        maxFileSize)),
                HttpStatus.BAD_REQUEST
        );
    }

}
