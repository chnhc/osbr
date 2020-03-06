package com.kkIntegration.OAuth2Common.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * description:
 * author: ckk
 * create: 2020-03-02 20:46
 */
public class MyAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;



    /**
     * Constructor used for an authentication request. The
     * {@link org.springframework.security.core.Authentication#isAuthenticated()} will
     * return <code>false</code>.
     *
     * @param aPrincipal The pre-authenticated principal
     * @param aCredentials The pre-authenticated credentials
     */
    public MyAuthenticationToken(Object aPrincipal, Object aCredentials) {
        super(null);
        this.principal = aPrincipal;
        this.credentials = aCredentials;
    }

    /**
     * Constructor used for an authentication response. The
     * {@link org.springframework.security.core.Authentication#isAuthenticated()} will
     * return <code>true</code>.
     *
     * @param aPrincipal The authenticated principal
     * @param anAuthorities The granted authorities
     */
    public MyAuthenticationToken(Object aPrincipal, Object aCredentials,
                                               Collection<? extends GrantedAuthority> anAuthorities) {
        super(anAuthorities);
        this.principal = aPrincipal;
        this.credentials = aCredentials;
        setAuthenticated(true);
    }

    /**
     * Get the credentials
     */
    public Object getCredentials() {
        return this.credentials;
    }

    /**
     * Get the principal
     */
    public Object getPrincipal() {
        return this.principal;
    }

}
