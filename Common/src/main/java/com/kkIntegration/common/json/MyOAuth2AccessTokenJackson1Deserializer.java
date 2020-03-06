package com.kkIntegration.common.json;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 13:36
 */
public class MyOAuth2AccessTokenJackson1Deserializer  extends StdDeserializer<OAuth2AccessToken> {

    public MyOAuth2AccessTokenJackson1Deserializer() {
        super(OAuth2AccessToken.class);
    }

    @Override
    public OAuth2AccessToken deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {

        String tokenValue = null;
        String tokenType = null;
        String refreshToken = null;
        Long expiresIn = null;
        Set<String> scope = null;
        Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();

        boolean isData = false;
        // TODO What should occur if a parameter exists twice
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String name = jp.getCurrentName();

            if("data".equals(name)){
                isData = true;
                continue;
            }
            if(!isData){
                continue;
            }

            jp.nextToken();
            if (OAuth2AccessToken.ACCESS_TOKEN.equals(name)) {
                tokenValue = jp.getText();
            }
            else if (OAuth2AccessToken.TOKEN_TYPE.equals(name)) {
                tokenType = jp.getText();
            }
            else if (OAuth2AccessToken.REFRESH_TOKEN.equals(name)) {
                refreshToken = jp.getText();
            }
            else if (OAuth2AccessToken.EXPIRES_IN.equals(name)) {
                try {
                    expiresIn = jp.getLongValue();
                } catch (JsonParseException e) {
                    expiresIn = Long.valueOf(jp.getText());
                }
            }
            else if (OAuth2AccessToken.SCOPE.equals(name)) {
                String text = jp.getText();
                scope = OAuth2Utils.parseParameterList(text);
            } else {
                additionalInformation.put(name, jp.readValueAs(Object.class));
            }
        }

        // TODO What should occur if a required parameter (tokenValue or tokenType) is missing?

        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(tokenValue);
        accessToken.setTokenType(tokenType);
        if (expiresIn != null) {
            accessToken.setExpiration(new Date(System.currentTimeMillis() + (expiresIn * 1000)));
        }
        if (refreshToken != null) {
            accessToken.setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));
        }
        accessToken.setScope(scope);
        accessToken.setAdditionalInformation(additionalInformation);

        return accessToken;
    }
}