package com.kkIntegration.common.properties.tokenStore;


import com.kkIntegration.common.properties.TokenStoreType;

/**
 * tokenStore类型
 */

public class TokenStoreProperties {

    /**
     * 定义token存储类型
     */
    private TokenStoreType tokenStoreType ;

    /**
     * 定义signingKey的值
     */
    private String tokenSigningKey = "OAUTHBOOT@IUY09&098#UIOKNJJ-YUIT.CLUB";

    /**
     * 是否需要jwt 替换加密方式
     */
    private boolean isNeedJWT = false;

    /**
     * 是否需要jwt扩展
     */
    private boolean isNeedJWTExt = false;

    public TokenStoreType getTokenStoreType() {
        return tokenStoreType;
    }

    public void setTokenStoreType(TokenStoreType tokenStoreType) {
        this.tokenStoreType = tokenStoreType;
    }

    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    public void setTokenSigningKey(String tokenSigningKey) {
        this.tokenSigningKey = tokenSigningKey;
    }

    public boolean isNeedJWT() {
        return isNeedJWT;
    }

    public void setNeedJWT(boolean needJWT) {
        isNeedJWT = needJWT;
    }

    public boolean isNeedJWTExt() {
        return isNeedJWTExt;
    }

    public void setNeedJWTExt(boolean needJWTExt) {
        isNeedJWTExt = needJWTExt;
    }
}
