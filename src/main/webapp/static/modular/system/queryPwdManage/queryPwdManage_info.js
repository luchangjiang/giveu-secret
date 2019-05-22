/**
 * 初始化查询口令管理详情对话框
 */
var QueryPwdManageInfoDlg = {
    queryPwdManageInfoData : {}
};

/**
 * 清除数据
 */
QueryPwdManageInfoDlg.clearData = function() {
    this.queryPwdManageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
QueryPwdManageInfoDlg.set = function(key, val) {
    this.queryPwdManageInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
QueryPwdManageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
QueryPwdManageInfoDlg.close = function() {
    parent.layer.close(window.parent.QueryPwdManage.layerIndex);
}

/**
 * 收集数据
 */
QueryPwdManageInfoDlg.collectData = function() {
    this.set('id').set('queryCommand').set('endDate').set('totalUseTimes').set('status').set('queryDesc').set('totalEncryptTimes').set('totalDecryptTimes');
}

/**
 * 提交添加
 */
QueryPwdManageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();
    console.log(this.queryPwdManageInfoData);
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/queryPwdManage/add", function(data){
        if(data.code===200) {
            Feng.success("添加成功!");
            window.parent.QueryPwdManage.table.refresh();
            QueryPwdManageInfoDlg.close();
        }
        else {
            Feng.error(data.message);
        }
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.queryPwdManageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
QueryPwdManageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/queryPwdManage/update", function(data){
        if(data.code===200) {
            Feng.success("修改成功!");
            window.parent.QueryPwdManage.table.refresh();
            QueryPwdManageInfoDlg.close();
        }
        else {
            Feng.error(data.message);
        }
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.queryPwdManageInfoData);
    ajax.start();
}

$(function() {

    //初始化启用状态
    $("#status").val($("#statusValue").val());
});
