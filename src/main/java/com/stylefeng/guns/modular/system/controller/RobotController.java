package com.stylefeng.guns.modular.system.controller;

import com.stylefeng.guns.common.constant.Const;
import com.stylefeng.guns.common.constant.factory.ConstantFactory;
import com.stylefeng.guns.common.constant.state.ManagerStatus;
import com.stylefeng.guns.common.constant.tips.Tip;
import com.stylefeng.guns.common.controller.BaseController;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.common.persistence.dao.RobotMapper;
import com.stylefeng.guns.common.persistence.model.Robot;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.shiro.ShiroUser;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.dao.RobotDao;
import com.stylefeng.guns.modular.system.factory.RobotFactory;
import com.stylefeng.guns.modular.system.transfer.RobotDto;
import com.stylefeng.guns.modular.system.warpper.RobotWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 机器人控制器
 *
 * @author fengshuonan
 * @Date 2018-08-08 16:39:53
 */
@Controller
@RequestMapping("/robot")
public class RobotController extends BaseController {

    private String PREFIX = "/system/robot/";

    @Resource
    private RobotDao robotDao;
    
    @Resource
    private RobotMapper robotMapper;

    /**
     * 跳转到机器人首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "robot.html";
    }

    /**
     * 跳转到添加机器人
     */
    @RequestMapping("/robot_add")
    public String robotAdd() {
        return PREFIX + "robot_add.html";
    }

    /**
     * 跳转到修改机器人
     */
    @RequestMapping("/robot_update/{robotId}")
    public String robotUpdate(@PathVariable Integer robotId, Model model) {
        if (ToolUtil.isEmpty(robotId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        Robot robot = this.robotMapper.selectById(robotId);
        model.addAttribute(robot);
        model.addAttribute("roleName", ConstantFactory.me().getRoleName(robot.getRoleid()));
        model.addAttribute("deptName", ConstantFactory.me().getDeptName(robot.getDeptid()));
        LogObjectHolder.me().set(robot);
        return PREFIX + "robot_edit.html";
    }

    /**
     * 获取机器人列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name) {
        List<Map<String, Object>> robots = robotDao.selectUsers(name);
        return new RobotWarpper(robots).warp();
    }

    /**
     * 新增机器人
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Tip add(@Valid RobotDto robot, BindingResult result) {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }

        // 判断账号是否重复
        Robot theRobot = robotDao.getByAccount(robot.getAccount());
        if (theRobot != null) {
            throw new BussinessException(BizExceptionEnum.USER_ALREADY_REG);
        }

        // 完善账号信息
        robot.setPassword(ShiroKit.md5(robot.getPassword(), ShiroKit.getRandomSalt(5)));
        robot.setStatus(ManagerStatus.OK.getCode());
        robot.setCreatetime(new Date());

        this.robotMapper.insert(RobotFactory.createRobot(robot));
        return super.SUCCESS_TIP;
    }

    /**
     * 删除机器人
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer robotId) {
        if (ToolUtil.isEmpty(robotId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        //不能删除超级管理员
        if (robotId.equals(Const.ADMIN_ID)) {
            throw new BussinessException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        this.robotDao.setStatus(robotId, ManagerStatus.DELETED.getCode());
        return SUCCESS_TIP;
    }


    /**
     * 修改机器人
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid RobotDto robot, BindingResult result) throws NoPermissionException {
        if (result.hasErrors()) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
            this.robotMapper.updateById(RobotFactory.createRobot(robot));
            return SUCCESS_TIP;
        } else {
            ShiroUser shiroUser = ShiroKit.getUser();
            if (shiroUser.getId().equals(robot.getId())) {
                this.robotMapper.updateById(RobotFactory.createRobot(robot));
                return SUCCESS_TIP;
            } else {
                throw new BussinessException(BizExceptionEnum.NO_PERMITION);
            }
        }
    }

    /**
     * 机器人详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
