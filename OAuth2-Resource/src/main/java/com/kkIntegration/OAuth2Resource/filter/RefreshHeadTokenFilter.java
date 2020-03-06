package com.kkIntegration.OAuth2Resource.filter;

import com.kkIntegration.common.entity.MyOAuth2AccessToken;
import com.kkIntegration.common.fliter.HttpServletRequestImpl;
import com.kkIntegration.common.properties.KkIntegrationOAuth2Properties;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description: 刷新头部 token
 * author: ckk
 * create: 2020-01-14 18:41
 */
//@Component
//@WebFilter(urlPatterns = { "/" }, filterName = "refreshHeadTokenFilter")
public class RefreshHeadTokenFilter implements Filter {

    @Autowired
    KkIntegrationOAuth2Properties kkIntegrationOAuth2Properties;

/*    public RefreshHeadTokenFilter(KkIntegrationOAuth2Properties kkIntegrationOAuth2Properties){
        this.kkIntegrationOAuth2Properties = kkIntegrationOAuth2Properties;
    }*/

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequestImpl httpRequest = null;
        if(authentication != null){
            if(! (authentication.getDetails() instanceof WebAuthenticationDetails)){
                OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
                if(oAuth2AuthenticationDetails != null){
                    MyOAuth2AccessToken entity = (MyOAuth2AccessToken)oAuth2AuthenticationDetails.getDecodedDetails();
                    if(entity.isAutoRefresh()){
                        // 如果是自动刷新  org.springframework.security.web.firewall.StrictHttpFirewall
                        // return entity.getValue();
                        System.out.println(entity.getValue());

                        httpRequest = new HttpServletRequestImpl(request);
                        httpRequest.putHeader("Authorization", kkIntegrationOAuth2Properties.getBearerType()+" "+entity.getValue());
                        httpRequest.putHeader("isRefresh", "true");

                    }
                }
            }
        }
        filterChain.doFilter(httpRequest != null ? httpRequest: request,response);
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequestImpl httpRequest = null;
        if(authentication != null){
            if(! (authentication.getDetails() instanceof WebAuthenticationDetails)){
                OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
                if(oAuth2AuthenticationDetails != null){
                    MyOAuth2AccessToken entity = (MyOAuth2AccessToken)oAuth2AuthenticationDetails.getDecodedDetails();
                    if(entity.isAutoRefresh()){
                        // 如果是自动刷新  org.springframework.security.web.firewall.StrictHttpFirewall
                        // return entity.getValue();
                        System.out.println(entity.getValue());

                        httpRequest = new HttpServletRequestImpl((HttpServletRequest) servletRequest);
                        httpRequest.putHeader("Authorization", kkIntegrationOAuth2Properties.getBearerType()+" "+entity.getValue());
                        httpRequest.putHeader("isRefresh", "true");

                    }
                }
            }
        }
        filterChain.doFilter(httpRequest != null ? httpRequest: servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
