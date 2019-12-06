package com.poizzz.vservice.security;

import com.poizzz.vservice.service.UserService;
import com.poizzz.vservice.dto.UserDto;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTShiroRealm extends AuthorizingRealm {
    private final Logger log = LoggerFactory.getLogger(JWTShiroRealm.class);

    protected UserService userService;

    public JWTShiroRealm(UserService userService) {
        this.userService = userService;
        //这里使用我们自定义的Matcher
        this.setCredentialsMatcher(new JWTCredentialsMatcher());
    }

    /**
     * 限定这个Realm只支持我们自定义的JWT Token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 更controller登录一样，也是获取用户的salt值，给到shiro，由shiro来调用matcher来做认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authcToken;
        String token = jwtToken.getToken();

        UserDto user = userService.getJwtTokenInfo(JwtUtils.getUsername(token));
        if (user == null)
            throw new AuthenticationException("token过期，请重新登录");

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(),
                                                                                   user.getSalt(),
                                                                                   "jwtRealm");

        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }
}
