/**
 * 初始化查询页面详情对话框
 */
var QueryPageInfoDlg = {
    queryPageInfoData : {}
};

/**
 * 清除数据
 */
QueryPageInfoDlg.clearData = function() {
    this.queryPageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
QueryPageInfoDlg.set = function(key, val) {
    this.queryPageInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
QueryPageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
QueryPageInfoDlg.close = function() {
    parent.layer.close(window.parent.QueryPage.layerIndex);
}

/**
 * 收集数据
 */
QueryPageInfoDlg.collectData = function() {
    this.set('id');
}

/**
 * 提交添加
 */
QueryPageInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/queryPage/add", function(data){
        Feng.success("添加成功!");
        window.parent.QueryPage.table.refresh();
        QueryPageInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.queryPageInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
QueryPageInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/queryPage/update", function(data){
        Feng.success("修改成功!");
        window.parent.QueryPage.table.refresh();
        QueryPageInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.queryPageInfoData);
    ajax.start();
}

/**
 * 文本加密
 */
QueryPageInfoDlg.encryptText = function() {
    var pwd=$("#condition").val();
    if(isNullOrEmpty(pwd)){
        Feng.error("请输入查询口令");
        return;
    }
    var text = $("#intputText").val();
    if (text === null || text === "" || text.length == 0) {
        Feng.error("输入文本不能为空");
        return false;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/queryPage/encryptText", function (data) {
        if(data.code===200) {
            $("#outputText").val(data.message);
            Feng.success("加密成功");
        }
        else {
            Feng.error(data.message);
        }
    }, function (data) {
        console.log(data);
        Feng.error("加密失败!");
    });
    ajax.set("inputStr", text);
    ajax.set("pwd", pwd);
    ajax.start();
}
/**
 * 文本解密
 */
QueryPageInfoDlg.decryptText = function() {
    var pwd=$("#condition").val();
    if(isNullOrEmpty(pwd)){
        Feng.error("请输入查询口令");
        return;
    }
    var text = $("#intputText").val();
    if (text === null || text === "" || text.length == 0) {
        Feng.error("输入文本不能为空");
        return false;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/queryPage/decryptText", function (data) {
        if(data.code===200) {
            $("#outputText").val(data.message);
            Feng.success("解密成功");
        }
        else {
            Feng.error(data.message);
        }
    }, function (data) {
        console.log(data);
        Feng.error("解密失败!");
    });
    ajax.set("inputStr", text);
    ajax.set("pwd", pwd);
    ajax.start();
}

QueryPageInfoDlg.uploadFile = function(type) {
    var msg = "";
    if (type === "encryptFile") {
        msg = "加密";
    } else if (type === "decryptFile") {
        msg = "解密";
    }
    else {
        Feng.error("未知操作");
        return;
    }
    var fileinfo = $("#fileinfo").val();
    if (isNullOrEmpty(fileinfo)) {
        Feng.error("请先选择文件");
        return false;
    }
    console.log(fileinfo);
    var pwd = $("#condition").val();
    if (isNullOrEmpty(pwd)) {
        Feng.error("请输入查询口令");
        return false;
    }

    $("#inputType").val(type);
    $("#pwd").val(pwd);
    $("#upload-form").submit();

    // var formdata = new FormData($("#upload-form"));
    // console.log(formdata);
    // $.ajax({
    //     // url: "/queryPage/handleFile?inputType=" + type + "&pwd=" + pwd,
    //     url: "/queryPage/handleFile",
    //     type: "post",
    //     data: formdata,
    //     contentType: false, //- 必须false才会自动加上正确的Content-Type
    //     processData: false, //- 必须false才会避开jQuery对 formdata 的默认处理,XMLHttpRequest会对 formdata 进行正确的处理
    //     success: function (data) {
    //         Feng.success(msg + "文件成功");
    //     },
    //     error: function () {
    //         Feng.error(msg + "文件失败!");
    //     }
    // });
    // return false;
}

isNullOrEmpty = function(param) {
    if(param===null||param===""||param.length===0){
        return true;
    }
    return false;
}

$(function() {

});
