package com.yuchu.security.web;

import com.yuchu.security.common.ResultMap;
import com.yuchu.security.entity.SysUserInfo;
import com.yuchu.security.repository.SysUserInfoRepository;
import com.yuchu.security.util.JWTUtil;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private ResultMap resultMap;
    @Autowired
    private SysUserInfoRepository userInfoRepository;

    @PostMapping("/login")
    public ResultMap login(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        SysUserInfo userInfo = userInfoRepository.findByUsername(username);
        // 将用户名作为盐值

        /*
         * MD5加密：
         * 使用SimpleHash类对原始密码进行加密。
         * 第一个参数代表使用MD5方式加密
         * 第二个参数为原始密码
         * 第三个参数为盐值，即用户名
         * 第四个参数为加密次数
         * 最后用toHex()方法将加密后的密码转成String
         * */

        if (userInfo == null) {
            return resultMap.fail().code(401).message("用户名错误");

        } else {
            ByteSource salt = ByteSource.Util.bytes(userInfo.getSalt());
            String newPs = new SimpleHash("MD5", password, salt, 1024).toHex();
            if (!newPs.equals(userInfo.getPassword())) {
                return resultMap.fail().code(401).message("密码错误");
            } else {
                return resultMap.success().code(200).message(JWTUtil.createToken(username));
            }
        }
    }

    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return resultMap.success().code(401).message(message);
    }
}
