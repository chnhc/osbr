package com.kkIntegration.common.utils;

import com.kkIntegration.common.entity.MyOAuth2AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;

/**
 * description:
 * author: ckk
 * create: 2019-12-17 17:18
 */
public class SecurityOauth2Utils {

    /**
     * 判断 当前的 token 是否已经刷新
     * @return 刷新的 token
     */
    public static String isRefresh(){
        NewTokenCurrentThreadUtils.NewToken newToken = NewTokenCurrentThreadUtils.getNewToken();
        if(newToken != null){
            if(newToken.isRefresh){
                return newToken.getNewToken();
            }
        }
        return null;
    }

    public static String isRefreshAndClear(){
        NewTokenCurrentThreadUtils.NewToken newToken = NewTokenCurrentThreadUtils.getNewToken(true);
        if(newToken != null){
            if(newToken.isRefresh){
                return newToken.getNewToken();
            }
        }
        return null;
    }

    // 自动续签旧方法
    public static String isRefresh1(){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;
        }
        if(authentication.getDetails() instanceof WebAuthenticationDetails){
            return null;
        }
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        if(oAuth2AuthenticationDetails == null){
            return null;
        }
        MyOAuth2AccessToken entity = (MyOAuth2AccessToken)oAuth2AuthenticationDetails.getDecodedDetails();
        if(entity == null){
            return null;
        }
        // 如果是自动刷新，那么需要返回value
        if(entity.isAutoRefresh()){
            return entity.getValue();
        }
        return null;
    }



    /**
     * 获取用户的所有权限
     */
    public static Collection<? extends GrantedAuthority> getAllRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;
        }
        return authentication.getAuthorities();
    }

}
