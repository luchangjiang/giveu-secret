/**
 * 
 */
package com.stylefeng.guns.modular.system.factory;

import com.stylefeng.guns.common.persistence.model.Robot;
import com.stylefeng.guns.modular.system.transfer.RobotDto;
import org.springframework.beans.BeanUtils;

/**
 * @author 529017
 *
 */
public class RobotFactory {

    public static Robot createRobot(RobotDto robotDto){
        if(robotDto == null){
            return null;
        }else{
        	Robot robot = new Robot();
            BeanUtils.copyProperties(robotDto,robot);
            return robot;
        }
    }
}
