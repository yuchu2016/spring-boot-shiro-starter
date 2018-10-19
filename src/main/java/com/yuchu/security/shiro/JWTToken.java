package com.yuchu.security.shiro;

import org.apache.shiro.authc.AuthenticationToken;


/**
 * 自定义Token
 * @author cnyuchu@gmail.com
 * @date 2018/10/19 14:10
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
