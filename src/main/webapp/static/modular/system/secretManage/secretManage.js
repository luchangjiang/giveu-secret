/**
 * 密口令管理初始化
 */
var SecretManage = {
    id: "SecretManageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SecretManage.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'uuid', field: 'uuid', visible: false, align: 'center', valign: 'middle'},
        {title: '密口令', field: 'secretCommandEx', align: 'center', valign: 'middle', sortable: true},
        {title: '秘钥', field: 'secretPassword', align: 'center', valign: 'middle', sortable: true},
        {title: '安全码版本', field: 'version', align: 'center', valign: 'middle', sortable: true},
        {title: '加密次数', field: 'encryptTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '解密次数', field: 'decryptTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
SecretManage.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SecretManage.seItem = selected[0];
        return true;
    }
};

/**
 * 替换密口令
 */
SecretManage.instead = function () {

    if (this.check()) {
        var status = $('#' + this.id).bootstrapTable('getSelections')[0].status;
        if (status == 0) {
            Feng.error("该秘钥已经禁用!");
            return;
        }
        var ajax = new $ax(Feng.ctxPath + "/secretManage/instead", function (data) {
            if (data.code === 200) {
                Feng.success("禁用成功!");
                SecretManage.table.refresh();
            }
            else {
                Feng.error(data.message);
            }
        }, function (data) {
            console.log(data);
            Feng.error("禁用失败!" + data.responseJSON.message + "!");
        });
        ajax.set("uuid", this.seItem.uuid);
        ajax.start();
    }
};

/**
 * 替换全部密口令
 */
SecretManage.insteadAll = function () {
    var ajax = new $ax(Feng.ctxPath + "/secretManage/insteadAll", function (data) {
        if (data.code === 200) {
            Feng.success("操作成功!");
            SecretManage.table.refresh();
        }
        else {
            Feng.error(data.message);
        }
    }, function (data) {
        console.log(data);
        Feng.error("操作失败!" + data.responseJSON.message + "!");
    });
    ajax.start();

};

/**
 * 查询密口令列表
 */
SecretManage.search = function () {
    var queryData = {};
    queryData['status'] = $("#secretStatus").val();
    SecretManage.table.refresh({query: queryData});

    // var queryData = {};
    // queryData['condition'] = $("#condition").val();
    // SecretManage.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SecretManage.initColumn();
    var table = new BSTable(SecretManage.id, "/secretManage/list", defaultColunms);
    table.setPaginationType("client");
    SecretManage.table = table.init();
});
