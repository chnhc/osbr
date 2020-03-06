package com.kkIntegration.OAuth2Server.exceptions.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import com.kkIntegration.OAuth2Server.exceptions.BootOAuth2Exception;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 *
 */

public class BootOauthExceptionSerializer extends StdSerializer<BootOAuth2Exception> {
    public BootOauthExceptionSerializer() {
        super(BootOAuth2Exception.class);
    }

    @Override
    public void serialize(BootOAuth2Exception value,
                          JsonGenerator gen, SerializerProvider provider) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        gen.writeStartObject();
        gen.writeStringField("error", String.valueOf(value.getHttpErrorCode()));
        gen.writeStringField("message", value.getMessage());
        //        gen.writeStringField("message", "用户名或密码错误");
        gen.writeStringField("path", request.getServletPath());
        gen.writeStringField("timestamp", String.valueOf(new Date().getTime()));
        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                gen.writeStringField(key, add);
            }
        }
        gen.writeEndObject();
    }
}
