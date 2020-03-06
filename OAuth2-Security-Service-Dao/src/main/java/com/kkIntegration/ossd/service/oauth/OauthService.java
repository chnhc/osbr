package com.kkIntegration.ossd.service.oauth;


import com.kkIntegration.ossd.entity.oauth.OauthAccessToken;
import com.kkIntegration.ossd.entity.oauth.OauthRefreshToken;

import java.util.List;

public interface OauthService {

    OauthAccessToken selectOauthAccessToken(String token_id);

    OauthAccessToken getTokenByAuthenticationId(String authentication_id);

    void insertAccessToken(OauthAccessToken oauthAccessToken);

    OauthAccessToken selectAccessToken(String token_id);

    OauthAccessToken selectAccessTokenAuthentication(String token_id);

    List<OauthAccessToken> selectAccessTokensFromClientId(String client_id);

    List<OauthAccessToken> selectAccessTokensFromUserName(String user_name);

    List<OauthAccessToken> selectAccessTokensFromUserNameAndClientId(String user_name, String client_id);

    void deleteAccessTokenSql(String token_id);

    void insertRefreshToken(OauthRefreshToken oauthRefreshToken);

    OauthRefreshToken selectRefreshToken(String token_id);

    void deleteRefreshToken(String token_id);

    OauthRefreshToken selectRefreshTokenAuthentication(String token_id);

    void deleteAccessTokenFromRefreshToken(String refresh_token);

    void deleteRefreshTokenByAccessToken(String token_id);

    String readRefreshTokenValueByAccessToken(String token_id);
}
