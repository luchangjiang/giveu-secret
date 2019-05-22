/**
 * 机器人管理初始化
 */
var Robot = {
    id: "RobotTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Robot.initColumn = function () {
    var columns = [
                   {field: 'selectItem', radio: true},
                   {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
                   {title: '账号', field: 'account', align: 'center', valign: 'middle', sortable: true},
                   {title: '姓名', field: 'name', align: 'center', valign: 'middle', sortable: true},
                   {title: '性别', field: 'sexName', align: 'center', valign: 'middle', sortable: true},
                   {title: '角色', field: 'roleName', align: 'center', valign: 'middle', sortable: true},
                   {title: '部门', field: 'deptName', align: 'center', valign: 'middle', sortable: true},
                   {title: '电话', field: 'phone', align: 'center', valign: 'middle', sortable: true},
                   {title: '创建时间', field: 'createtime', align: 'center', valign: 'middle', sortable: true},
                   {title: '状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true}];
               return columns;
};

/**
 * 检查是否选中
 */
Robot.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Robot.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加机器人
 */
Robot.openAddRobot = function () {
    var index = layer.open({
        type: 2,
        title: '添加机器人',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/robot/robot_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看机器人详情
 */
Robot.openRobotDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '机器人详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/robot/robot_update/' + Robot.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除机器人
 */
Robot.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/robot/delete", function (data) {
            Feng.success("删除成功!");
            Robot.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("robotId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询机器人列表
 */
Robot.search = function () {
    var queryData = {};
    queryData['name'] = $("#condition").val();
    Robot.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Robot.initColumn();
    var table = new BSTable(Robot.id, "/robot/list", defaultColunms);
    table.setPaginationType("client");
    Robot.table = table.init();
});
