package cn.zjc.app.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * token验证失败异常
 */
public class AuthTokenException extends AuthenticationException {

    public AuthTokenException(String message) {
        super(message);
    }
}
