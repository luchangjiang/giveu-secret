package com.stylefeng.guns.common.constant.factory;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.common.constant.cache.Cache;
import com.stylefeng.guns.common.constant.cache.CacheKey;
import com.stylefeng.guns.common.constant.state.ManagerStatus;
import com.stylefeng.guns.common.constant.state.MenuStatus;
import com.stylefeng.guns.common.constant.state.Sex;
import com.stylefeng.guns.common.persistence.dao.*;
import com.stylefeng.guns.common.persistence.model.*;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.core.util.ToolUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 常量的生产工厂
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory {

    private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private DeptMapper deptMapper = SpringContextHolder.getBean(DeptMapper.class);
    private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);
    private UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private NoticeMapper noticeMapper = SpringContextHolder.getBean(NoticeMapper.class);

    public static ConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    /**
     * 根据用户id获取用户名称
     *
     * @author stylefeng
     * @Date 2017/5/9 23:41
     */
    public String getUserNameById(Integer userId) {
        SecretUser secretUser = userMapper.selectById(userId);
        if (secretUser != null) {
            return secretUser.getUserName();
        } else {
            return "--";
        }
    }

    /**
     * 根据用户id获取用户账号
     *
     * @author stylefeng
     * @date 2017年5月16日21:55:371
     */
    public String getUserAccountById(Integer userId) {
        SecretUser secretUser = userMapper.selectById(userId);
        if (secretUser != null) {
            return secretUser.getAccount();
        } else {
            return "--";
        }
    }

    /**
     * 通过角色ids获取角色名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.ROLES_NAME + "'+#roleIds")
    public String getRoleName(String roleIds) {
        Integer[] roles = Convert.toIntArray(roleIds);
        StringBuilder sb = new StringBuilder();
        for (int role : roles) {
            SecretRole secretRoleObj = roleMapper.selectById(role);
            if (ToolUtil.isNotEmpty(secretRoleObj) && ToolUtil.isNotEmpty(secretRoleObj.getRoleName())) {
                sb.append(secretRoleObj.getRoleName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 通过角色id获取角色名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    public String getSingleRoleName(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        SecretRole secretRoleObj = roleMapper.selectById(roleId);
        if (ToolUtil.isNotEmpty(secretRoleObj) && ToolUtil.isNotEmpty(secretRoleObj.getRoleName())) {
            return secretRoleObj.getRoleName();
        }
        return "";
    }

    /**
     * 通过角色id获取角色英文名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_TIP + "'+#roleId")
    public String getSingleRoleTip(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        SecretRole secretRoleObj = roleMapper.selectById(roleId);
        if (ToolUtil.isNotEmpty(secretRoleObj) && ToolUtil.isNotEmpty(secretRoleObj.getRoleName())) {
            return secretRoleObj.getTips();
        }
        return "";
    }

    /**
     * 获取部门名称
     */
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
    public String getDeptName(Integer deptId) {
        SecretDept secretDept = deptMapper.selectById(deptId);
        if (ToolUtil.isNotEmpty(secretDept) && ToolUtil.isNotEmpty(secretDept.getFullName())) {
            return secretDept.getFullName();
        }
        return "";
    }

    /**
     * 获取菜单的名称们(多个)
     */
    public String getMenuNames(String menuIds) {
        Integer[] menus = Convert.toIntArray(menuIds);
        StringBuilder sb = new StringBuilder();
        for (int menu : menus) {
            SecretMenu secretMenuObj = menuMapper.selectById(menu);
            if (ToolUtil.isNotEmpty(secretMenuObj) && ToolUtil.isNotEmpty(secretMenuObj.getMenuName())) {
                sb.append(secretMenuObj.getMenuName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 获取菜单名称
     */
    public String getMenuName(Integer menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            return "";
        } else {
            SecretMenu secretMenu = menuMapper.selectById(menuId);
            if (secretMenu == null) {
                return "";
            } else {
                return secretMenu.getMenuName();
            }
        }
    }

    /**
     * 获取菜单名称通过编号
     */
    public String getMenuNameByCode(String code) {
        if (ToolUtil.isEmpty(code)) {
            return "";
        } else {
            SecretMenu param = new SecretMenu();
            param.setMenuCode(code);
            SecretMenu secretMenu = menuMapper.selectOne(param);
            if (secretMenu == null) {
                return "";
            } else {
                return secretMenu.getMenuName();
            }
        }
    }

    /**
     * 获取字典名称
     */
    public String getDictName(Integer dictId){
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            SecretDict secretDict = dictMapper.selectById(dictId);
            if (secretDict == null) {
                return "";
            } else {
                return secretDict.getDictName();
            }
        }
    }

    /**
     * 获取通知标题
     */
    public String getNoticeTitle(Integer dictId){
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            SecretNotice secretNotice = noticeMapper.selectById(dictId);
            if (secretNotice == null) {
                return "";
            } else {
                return secretNotice.getTitle();
            }
        }
    }

    /**
     * 获取性别名称
     */
    public String getSexName(Integer sex) {
        return Sex.valueOf(sex);
    }

    /**
     * 获取用户登录状态
     */
    public String getStatusName(Integer status) {
        return ManagerStatus.valueOf(status);
    }

    /**
     * 获取菜单状态
     */
    public String getMenuStatusName(Integer status) {
        return MenuStatus.valueOf(status);
    }

    /**
     * 查询字典
     */
    public List<SecretDict> findInDict(Integer id) {
        if (ToolUtil.isEmpty(id)) {
            return null;
        } else {
            EntityWrapper<SecretDict> wrapper = new EntityWrapper<>();
            List<SecretDict> secretDicts = dictMapper.selectList(wrapper.eq("pid", id));
            if (secretDicts == null || secretDicts.size() == 0) {
                return null;
            } else {
                return secretDicts;
            }
        }
    }

    /**
     * 获取被缓存的对象(用户删除业务)
     */
    public String getCacheObject(String para) {
        return LogObjectHolder.me().get().toString();
    }

}
