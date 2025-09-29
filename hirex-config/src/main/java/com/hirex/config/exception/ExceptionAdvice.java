package com.hirex.config.exception;

import com.hirex.config.exception.exceptions.BusinessException;
import com.hirex.config.lib.commom.Messages;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    private static final String MESSAGES_PARAM = "message";

    private final Messages messages;

    @Autowired
    public ExceptionAdvice(Messages messages) {
        this.messages = messages;
    }

/*    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody
    Map<String, String> authentication(BadCredentialsException e) {
        Map<String, String> msg = new HashMap<>();
        msg.put(MESSAGES_PARAM, messages.getMessage(e.getMessage()));
        return msg;
    }*/

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody
    Map<String, String> authentication(AccessDeniedException e) {
        Map<String, String> msg = new HashMap<>();
        msg.put(MESSAGES_PARAM, e.getMessage());
        return msg;
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    ResponseEntity<String> handleHttpClient(HttpClientErrorException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BusinessException.class)
    public @ResponseBody
    Map<String, Object> bizErrorHandler(BusinessException e) {
        Map<String, Object> msg = new HashMap<>();
        msg.put(MESSAGES_PARAM, messages.getMessage(e.getMessage()));
        if (e.getViolations() != null) {
            List<String> validations = e.getViolations().parallelStream()
                    .map(a -> messages.getMessage(a.getMessage()))
                    .toList();
            msg.put("validations", validations);
        }
        return msg;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public @ResponseBody
    Map<String, Object> bizErrorHandler(EntityNotFoundException e) {
        Map<String, Object> msg = new HashMap<>();
        msg.put(MESSAGES_PARAM, messages.getMessage(e.getMessage()));
        return msg;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody Map<String, Object> unexpectedErrorHandler(Exception e, HttpServletRequest req) {
        log.error("URI: {}", req.getRequestURI());
        String guid = UUID.randomUUID().toString();
        log.error(guid + ": " + messages.getMessage(e.getMessage()), e);
        Map<String, Object> msg = new HashMap<>();
        msg.put("hash", guid);
        return msg;
    }
}
