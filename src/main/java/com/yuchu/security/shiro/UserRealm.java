package com.yuchu.security.shiro;

import com.yuchu.security.entity.SysPermission;
import com.yuchu.security.entity.SysRole;
import com.yuchu.security.entity.SysUserInfo;
import com.yuchu.security.repository.SysUserInfoRepository;
import com.yuchu.security.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 自定义Realm
 * @author cnyuchu@gmail.com
 * @date 2018/10/19 14:10
 */
@Component
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserInfoRepository userInfoRepository;
    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
    /**
     * 授权
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("权限配置-->doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        SysUserInfo userInfo  = (SysUserInfo)principals.getPrimaryPrincipal();
        String username = JWTUtil.getUsername(principals.toString());
        SysUserInfo userInfo  = userInfoRepository.findByUsername(username);
        for(SysRole role:userInfo.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            for(SysPermission permission:role.getPermissions()){
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;
    }

    /**
     * 身份验证
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("登录验证->doGetAuthenticationInfo()");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null || !JWTUtil.verify(token, username)) {
            throw new AuthenticationException("token认证失败！");
        }
        SysUserInfo userInfo = userInfoRepository.findByUsername(username);
        if (userInfo == null) {
            throw new AuthenticationException("该用户不存在！");
        }
//        int ban = userInfoRepository.checkUserBanStatus(username);
//        if (ban == 1) {
//            throw new AuthenticationException("该用户已禁用！");
//        }
        return new SimpleAuthenticationInfo(token, token, "UserRealm");
    }
}
