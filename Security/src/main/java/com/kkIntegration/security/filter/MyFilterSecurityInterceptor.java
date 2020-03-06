package com.kkIntegration.security.filter;

import com.kkIntegration.common.properties.KkIntegrationSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * description: 重写 FilterSecurityInterceptor
 * author: ckk
 * create: 2019-12-16 16:13
 */

public class MyFilterSecurityInterceptor extends FilterSecurityInterceptor {
    // ~ Static fields/initializers
    // =====================================================================================

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";



    // ~ Instance fields
    // ================================================================================================

    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    private boolean observeOncePerRequest = true;

    // 整合安全配置
    private KkIntegrationSecurityProperties kkIntegrationSecurityProperties;

    public MyFilterSecurityInterceptor(KkIntegrationSecurityProperties properties){
        this.kkIntegrationSecurityProperties = properties;
    }


    // ~ Methods
    // ========================================================================================================

    /**
     * Not used (we rely on IoC container lifecycle services instead)
     *
     * @param arg0 ignored
     *
     */
    public void init(FilterConfig arg0) {
    }

    /**
     * Not used (we rely on IoC container lifecycle services instead)
     */
    public void destroy() {
    }

    /**
     * Method that is actually called by the filter chain. Simply delegates to the
     * {@link #invoke(FilterInvocation)} method.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     *
     * @throws IOException if the filter chain fails
     * @throws ServletException if the filter chain fails
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
        this.securityMetadataSource = newSource;
    }

    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        if ((fi.getRequest() != null)
                && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
                && observeOncePerRequest) {
            // filter already applied to this request and user wants us to observe
            // once-per-request handling, so don't re-do security checking
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }
        else {
            // first time this request being called, so perform security checking
            if (fi.getRequest() != null && observeOncePerRequest) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }


            boolean isMatch = false;
            // 公开
            boolean backEnd = false;

            // 获取权限
            Collection<? extends GrantedAuthority> authorities =  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            while(iterator.hasNext()){  // 先判断
                GrantedAuthority g = iterator.next();

                // 开启后台, 并且有登录后台权限
                if(!backEnd && kkIntegrationSecurityProperties.isOpenBackEnd() && g.getAuthority().equals(kkIntegrationSecurityProperties.getBankEndId())){
                    backEnd = true;
                }

                // 判断是否有后台公开前端调用 接口
                if(!backEnd && kkIntegrationSecurityProperties.isOpenBackEnd() ){
                    backEnd = isHaveAuthority(kkIntegrationSecurityProperties.getFrontToEndId(), fi.getRequestUrl().split("\\?")[0]);
                    if(backEnd){
                        isMatch = true;
                        break;
                    }
                }


                // 先判断游客权限
                // 默认 ROLE_ANONYMOUS 游客
                isMatch = isHaveAuthority("ROLE_ANONYMOUS", fi.getRequestUrl().split("\\?")[0]);
                if(isMatch){
                    break;
                }
                // 再判断设置的权限
                isMatch = isHaveAuthority(g.getAuthority(), fi.getRequestUrl().split("\\?")[0]);
                if(isMatch){
                    break;
                }


            }

            // 不匹配权限路径 ，报错
            if(!isMatch){
                throw new AccessDeniedException("没有访问的权限");
            }else if(kkIntegrationSecurityProperties.isOpenBackEnd() && !backEnd){
                throw new AccessDeniedException("没有权限登录后台");
            }else{
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            }

        }
    }


    // 角色是否有权限
    private boolean isHaveAuthority(String role, String requestUrl){
        if(kkIntegrationSecurityProperties.getRolePathMap() == null || kkIntegrationSecurityProperties.getRolePathMap().size()==0){
            return false;
        }
        // 权限 map , 是否有权限
        ArrayList<String> adminPath = kkIntegrationSecurityProperties.getRolePathMap().get(role);
        if(adminPath != null && adminPath.size()>0){
            for(String patternPath : adminPath){
                if(isMatchRolePath(patternPath, requestUrl)){
                    return true;
                }
            }
        }
        return false;
    }

    // url 路径匹配
    private boolean isMatchRolePath(String patternPath, String requestUrl){
        // 保证不为空
        if(patternPath == null || patternPath.isEmpty() ||
                requestUrl == null || requestUrl.isEmpty() ){
            return false;
        }

        // 将 两个进行  /  切分
        String[] patternPaths = patternPath.split("/");
        String[] requestUrls = requestUrl.split("/");

        for(int i= 0 , len = patternPaths.length ; i< len ; i++ ){
            // 空就跳过
            if(patternPaths[i].isEmpty()){
                continue;
            }
            // 判断 ** 就是通配符
            if(patternPaths[i].equals("**")){
                return true;
            }
            // 如果 是 * ，进行下一轮判断，如果没有下一个，就返回匹配
            if (patternPaths[i].equals("*")) {
                // 如果匹配了， 判断是否有下一个路径匹配
                if(len == i+1 && requestUrls.length == i+1){
                    // 匹配了， 而且是两个都是最后一个
                    return true;
                }
            }
            // 不匹配 直接返回
            if(!Pattern.matches(patternPaths[i], requestUrls[i])){
                break;
            }else{
                // 如果匹配了， 判断是否有下一个路径匹配
                if(len == i+1 && requestUrls.length == i+1){
                    // 匹配了， 而且是两个都是最后一个
                    return true;
                }

            }
        }

        return false;
    }

    /**
     * Indicates whether once-per-request handling will be observed. By default this is
     * <code>true</code>, meaning the <code>FilterSecurityInterceptor</code> will only
     * execute once-per-request. Sometimes users may wish it to execute more than once per
     * request, such as when JSP forwards are being used and filter security is desired on
     * each included fragment of the HTTP request.
     *
     * @return <code>true</code> (the default) if once-per-request is honoured, otherwise
     * <code>false</code> if <code>FilterSecurityInterceptor</code> will enforce
     * authorizations for each and every fragment of the HTTP request.
     */
    public boolean isObserveOncePerRequest() {
        return observeOncePerRequest;
    }

    public void setObserveOncePerRequest(boolean observeOncePerRequest) {
        this.observeOncePerRequest = observeOncePerRequest;
    }





}
