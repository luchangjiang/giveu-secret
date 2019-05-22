/**
 * 初始化机器人详情对话框
 */
var RobotInfoDlg = {
    robotInfoData : {}
};

/**
 * 清除数据
 */
RobotInfoDlg.clearData = function() {
    this.robotInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RobotInfoDlg.set = function(key, val) {
    this.robotInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
RobotInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
RobotInfoDlg.close = function() {
    parent.layer.close(window.parent.Robot.layerIndex);
}

/**
 * 收集数据
 */
RobotInfoDlg.collectData = function() {
    this.set('id').set('account').set('sex').set('password').set('name').set('birthday').set('rePassword').set('deptid').set('phone');
}

/**
 * 提交添加
 */
RobotInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/robot/add", function(data){
        Feng.success("添加成功!");
        window.parent.Robot.table.refresh();
        RobotInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.robotInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
RobotInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/robot/update", function(data){
        Feng.success("修改成功!");
        window.parent.Robot.table.refresh();
        RobotInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.robotInfoData);
    ajax.start();
}

$(function() {

});
