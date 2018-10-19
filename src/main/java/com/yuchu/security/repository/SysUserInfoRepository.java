package com.yuchu.security.repository;

import com.yuchu.security.entity.SysUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cnyuchu@gmail.com
 * @date 2018/10/19 14:12
 */

public interface SysUserInfoRepository extends JpaRepository<SysUserInfo,Integer> {

    SysUserInfo findByUsername(String username);
}
