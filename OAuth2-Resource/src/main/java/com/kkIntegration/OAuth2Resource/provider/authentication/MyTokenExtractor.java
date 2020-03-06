package com.kkIntegration.OAuth2Resource.provider.authentication;

import com.kkIntegration.common.utils.NewTokenCurrentThreadUtils;
import com.kkIntegration.common.utils.SecurityOauth2Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 15:17
 */
public class MyTokenExtractor implements TokenExtractor {

    private final static Log logger = LogFactory.getLog(BearerTokenExtractor.class);

    private String bearerType = OAuth2AccessToken.BEARER_TYPE;


    public MyTokenExtractor(String bearerType){
        this.bearerType = bearerType;
    }

    @Override
    public Authentication extract(HttpServletRequest request) {
        // 先从当前线程获取是否存在新的token
        String tokenValue = SecurityOauth2Utils.isRefresh();
        // 不存在从头获取
        if(tokenValue == null){
            tokenValue = extractToken(request);
        }

        if (tokenValue != null) {
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
            return authentication;
        }
        return null;
    }

    protected String extractToken(HttpServletRequest request) {
        // first check the header...
        String token = extractHeaderToken(request);
        // bearer type allows a request parameter as well
        if (token == null) {
            logger.debug("Token not found in headers. Trying request parameters.");
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (token == null) {
                logger.debug("Token not found in request parameters.  Not an OAuth2 request.");
            }
            else {
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
            }
        }

        return token;
    }


    /**
     * Extract the OAuth bearer token from a header.
     *
     * @param request The request.
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    protected String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(bearerType.toLowerCase()))) {
                String authHeaderValue = value.substring(bearerType.length()).trim();
                // Add this here for the auth details later. Would be better to change the signature of this method.
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
                        value.substring(0, bearerType.length()).trim());
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }

        return null;
    }

}