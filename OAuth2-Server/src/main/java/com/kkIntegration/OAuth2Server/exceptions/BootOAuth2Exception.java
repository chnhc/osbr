package com.kkIntegration.OAuth2Server.exceptions;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.kkIntegration.OAuth2Server.exceptions.serializer.BootOauthExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 *
 */
@JsonSerialize(using = BootOauthExceptionSerializer.class)
public class BootOAuth2Exception extends OAuth2Exception {


    public BootOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public BootOAuth2Exception(String msg) {
        super(msg);
    }
}
