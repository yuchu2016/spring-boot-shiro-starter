package com.yuchu.security.web;

import com.yuchu.security.common.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 游客角色可以访问的页面
 * @Date 2018-04-30
 * @Time 14:24
 */
@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private ResultMap resultMap;

    @Autowired
    public GuestController(ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    @GetMapping("/welcome")
    public ResultMap login() {
        return resultMap.success().code(200).message("欢迎访问游客页面！");
    }
}
