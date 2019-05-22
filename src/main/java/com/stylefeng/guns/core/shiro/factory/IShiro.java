package com.stylefeng.guns.core.shiro.factory;

import com.stylefeng.guns.common.persistence.model.SecretUser;
import com.stylefeng.guns.core.shiro.ShiroUser;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

import java.util.List;

/**
 * 定义shirorealm所需数据的接口
 *
 * @author fengshuonan
 * @date 2016年12月5日 上午10:23:34
 */
public interface IShiro {

    /**
     * 根据账号获取登录用户
     *
     * @param account 账号
     */
    SecretUser user(String account);

    /**
     * 根据系统用户获取Shiro的用户
     *
     * @param secretUser 系统用户
     */
    ShiroUser shiroUser(SecretUser secretUser);

    /**
     * 获取权限列表通过角色id
     *
     * @param roleId 角色id
     */
    List<String> findPermissionsByRoleId(Integer roleId);

    /**
     * 根据角色id获取角色名称
     *
     * @param roleId 角色id
     */
    String findRoleNameByRoleId(Integer roleId);

    /**
     * 获取shiro的认证信息
     */
    SimpleAuthenticationInfo info(ShiroUser shiroUser, SecretUser secretUser, String realmName);

}
