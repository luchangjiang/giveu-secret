package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.common.persistence.model.Robot;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 机器人Dao
 *
 * @author fengshuonan
 * @Date 2018-08-08 16:39:53
 */
public interface RobotDao {

    /**
     * 修改用户状态
     *
     * @param user
     * @date 2017年2月12日 下午8:42:31
     */
    int setStatus(@Param("userId") Integer userId, @Param("status") int status);
    
    /**
     * 根据条件查询用户列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Map<String, Object>> selectUsers(@Param("name") String name);

    /**
     * 设置用户的角色
     *
     * @return
     * @date 2017年2月13日 下午7:31:30
     */
    int setRoles(@Param("userId") Integer userId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     *
     * @param account
     * @return
     * @date 2017年2月17日 下午11:07:46
     */
    Robot getByAccount(@Param("account") String account);
}
