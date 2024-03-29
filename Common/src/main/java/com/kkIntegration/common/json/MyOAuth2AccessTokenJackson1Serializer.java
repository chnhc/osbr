package com.kkIntegration.common.json;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.io.IOException;
import java.util.Map;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 13:35
 */
public class MyOAuth2AccessTokenJackson1Serializer extends SerializerBase<OAuth2AccessToken> {

    public MyOAuth2AccessTokenJackson1Serializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public void serialize(OAuth2AccessToken token, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonGenerationException {
        jgen.writeStartObject();

        // status ---- start
        jgen.writeNumberField("status", 20000);
        // status ---- end

        // message ---- start
        jgen.writeStringField("message", "获取token成功");
        // message ---- end

        // data ----- start
        jgen.writeObjectFieldStart("data");
        jgen.writeStringField(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
        jgen.writeStringField(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
        // 不需要返回 refresh Token
        /*OAuth2RefreshToken refreshToken = token.getRefreshToken();
        if (refreshToken != null) {
            jgen.writeStringField(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
        }*/
        // 不需要返回 过期时间
        /*Date expiration = token.getExpiration();
        if (expiration != null) {
            long now = System.currentTimeMillis();
            jgen.writeNumberField(OAuth2AccessToken.EXPIRES_IN, (expiration.getTime() - now) / 1000);
        }*/
        // 不需要返回 scope
        /*Set<String> scope = token.getScope();
        if (scope != null && !scope.isEmpty()) {
            StringBuffer scopes = new StringBuffer();
            for (String s : scope) {
                Assert.hasLength(s, "Scopes cannot be null or empty. Got " + scope + "");
                scopes.append(s);
                scopes.append(" ");
            }
            jgen.writeStringField(OAuth2AccessToken.SCOPE, scopes.substring(0, scopes.length() - 1));
        }*/
        Map<String, Object> additionalInformation = token.getAdditionalInformation();
        for (String key : additionalInformation.keySet()) {
            jgen.writeObjectField(key, additionalInformation.get(key));
        }
        jgen.writeEndObject();
        // data ----- end
        jgen.writeEndObject();
    }
}