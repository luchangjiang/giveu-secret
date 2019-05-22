package com.stylefeng.guns.modular.system.factory;

import com.stylefeng.guns.common.persistence.model.SecretUser;
import com.stylefeng.guns.modular.system.transfer.UserDto;
import org.springframework.beans.BeanUtils;

/**
 * 用户创建工厂
 *
 * @author fengshuonan
 * @date 2017-05-05 22:43
 */
public class UserFactory {

    public static SecretUser createUser(UserDto userDto){
        if(userDto == null){
            return null;
        }else{
            SecretUser secretUser = new SecretUser();
            BeanUtils.copyProperties(userDto, secretUser);
            return secretUser;
        }
    }
}
