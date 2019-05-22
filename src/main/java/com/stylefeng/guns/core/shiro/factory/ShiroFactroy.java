package com.stylefeng.guns.core.shiro.factory;

import com.stylefeng.guns.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.common.constant.state.ManagerStatus;
import com.stylefeng.guns.common.persistence.model.SecretUser;
import com.stylefeng.guns.core.shiro.ShiroUser;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.modular.system.dao.MenuDao;
import com.stylefeng.guns.modular.system.dao.UserMgrDao;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class ShiroFactroy implements IShiro {

    @Autowired
    private UserMgrDao userMgrDao;

    @Autowired
    private MenuDao menuDao;

    public static IShiro me() {
        return SpringContextHolder.getBean(IShiro.class);
    }

    @Override
    public SecretUser user(String account) {

        SecretUser secretUser = userMgrDao.getByAccount(account);

        // 账号不存在
        if (null == secretUser) {
            throw new CredentialsException();
        }
        // 账号被冻结
        if (secretUser.getUserStatus() != ManagerStatus.OK.getCode()) {
            throw new LockedAccountException();
        }
        return secretUser;
    }

    @Override
    public ShiroUser shiroUser(SecretUser secretUser) {
        ShiroUser shiroUser = new ShiroUser();

        shiroUser.setId(secretUser.getId());            // 账号id
        shiroUser.setAccount(secretUser.getAccount());// 账号
        shiroUser.setDeptId(secretUser.getDeptId());    // 部门id
        shiroUser.setDeptName(ConstantFactory.me().getDeptName(secretUser.getDeptId()));// 部门名称
        shiroUser.setName(secretUser.getUserName());        // 用户名称

        Integer[] roleArray = Convert.toIntArray(secretUser.getRoleId());// 角色集合
        List<Integer> roleList = new ArrayList<Integer>();
        List<String> roleNameList = new ArrayList<String>();
        for (int roleId : roleArray) {
            roleList.add(roleId);
            roleNameList.add(ConstantFactory.me().getSingleRoleName(roleId));
        }
        shiroUser.setRoleList(roleList);
        shiroUser.setRoleNames(roleNameList);

        return shiroUser;
    }

    @Override
    public List<String> findPermissionsByRoleId(Integer roleId) {
        List<String> resUrls = menuDao.getResUrlsByRoleId(roleId);
        return resUrls;
    }

    @Override
    public String findRoleNameByRoleId(Integer roleId) {
        return ConstantFactory.me().getSingleRoleTip(roleId);
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, SecretUser secretUser, String realmName) {
        String credentials = secretUser.getPassword();
        // 密码加盐处理
        String source = secretUser.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
