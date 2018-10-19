package com.yuchu.security.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author cnyuchu@gmail.com
 * @date 2018/10/19 13:39
 */
@Entity
@Table(name = "sys_user_info")
@Data
public class SysUserInfo implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique =true)
    private String username;

    private String name;

    private String password;

    private String salt;

    private byte  state;

    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    private List<SysRole> roleList;// 一个用户具有多个角色

}
