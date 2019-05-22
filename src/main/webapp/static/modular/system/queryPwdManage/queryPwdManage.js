/**
 * 查询口令管理管理初始化
 */
var QueryPwdManage = {
    id: "QueryPwdManageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
QueryPwdManage.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '查询口令', field: 'queryCommand', align: 'center', valign: 'middle', sortable: true},
        {title: '描述', field: 'queryDesc', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true},
        {title: '有效截止时间', field: 'endDate', align: 'center', valign: 'middle', sortable: true},
        {title: '设定使用次数', field: 'totalUseTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '使用次数', field: 'useTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '设定加密次数', field: 'totalEncryptTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '加密次数', field: 'encryptTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '设定解密次数', field: 'totalDecryptTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '解密次数', field: 'decryptTimes', align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true},
        {title: '修改时间', field: 'updateTime', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 检查是否选中
 */
QueryPwdManage.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        QueryPwdManage.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加查询口令管理
 */
QueryPwdManage.openAddQueryPwdManage = function () {
    var index = layer.open({
        type: 2,
        title: '添加查询口令管理',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/queryPwdManage/queryPwdManage_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看查询口令管理详情
 */
QueryPwdManage.openQueryPwdManageDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '查询口令管理详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/queryPwdManage/queryPwdManage_update/' + QueryPwdManage.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除查询口令管理
 */
QueryPwdManage.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/queryPwdManage/delete", function (data) {
            if(data.code===200) {
                Feng.success("操作成功!");
                QueryPwdManage.table.refresh();
            }
            else {
                Feng.error(data.message);
            }
        }, function (data) {
            Feng.error("操作失败!" + data.responseJSON.message + "!");
        });
        ajax.set("queryPwdManageId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询查询口令管理列表
 */
QueryPwdManage.search = function () {
    var queryData = {};
    queryData['queryPwd'] = $("#condition").val();
    QueryPwdManage.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = QueryPwdManage.initColumn();
    var table = new BSTable(QueryPwdManage.id, "/queryPwdManage/list", defaultColunms);
    table.setPaginationType("client");
    QueryPwdManage.table = table.init();
});
