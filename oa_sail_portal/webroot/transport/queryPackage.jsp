<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="发货管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<%--<script src="../js/jquery/jquery.js"></script>--%>
<script src="../js/plugin/dialog/jquery.dialog.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>

<script type="text/javascript">

var gurl = '../sail/ship.do?method=';
var addUrl = '../sail/ship.do?method=';
var ukey = 'Package';

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;

var str = '';

function load()
{
     preload();
     
     guidMap = {
         title: '出库单列表',
         url: gurl + 'query' + ukey,
         colModel : [
             {display: '<input type=checkbox id=flexi_Check onclick=checkAll(this)>选择', name : 'check', content : '<input type=checkbox name=checkb value={id} lstatus={status}>', width : 40, sortable : false, align: 'center'},             
             {display: '出库单', name : 'id', width : '15%', sortable : true},
             {display: '商品种类', name : 'productCount', width : '5%'},
             {display: '发货数量', name : 'amount',  width : '5%'},
             {display: '收货人', name : 'receiver',  width : '5%' , sortable : true},
             {display: '收货电话', name : 'mobile',  width : '8%' , sortable : true},
             {display: '发货方式', name : 'shipping', cc : 'outShipment', width : '6%' , sortable : true},
             {display: '发货公司', name : 'transportName1', content : '{transportName1}/{transportName2}',  width : '5%' },
             {display: '支付方式', name : 'pay', width : '10%' },
             {display: '承担人', name : 'stafferName', width : '5%' , sortable : true},
             {display: '仓库地点', name : 'locationName', width : '10%'},
             {display: '状态', name : 'status', cc: 'shipStatus',  width : '5%' , sortable : true},
             {display: '发票单发', name : 'insFollowOut',cc: 'invoiceShipStatus', width : '6%', sortable : true},
             {display: '赠品单发', name : 'zsFollowOut', cc: 'zsShipStatus', width : '6%', sortable : true},
             {display: '单据时间', name : 'billsTime', width : '10%', sortable : true},
             {display: '时间', name : 'logTime',  width : 'auto', sortable : true }
             ],
         extAtt: {
             id : {begin : '<a href=' + gurl + 'findPackage&packageId={id}>', end : '</a>'}
         },
         buttons : [
             {id: 'add', bclass: 'add', caption : '拣配', onpress : addBean},
             {id: 'del', bclass: 'del', caption : '撤销', onpress : undoBean},
             {id: 'autoPickup', bclass: 'add', caption : '自动拣配数量', onpress : autoPickup},
             {id: 'mergePackages', bclass: 'add', caption : '合并', onpress : mergePackages},
             {id: 'search', bclass: 'search', onpress : doSearch}
             ],
        <p:conf/>
     };
     
     $("#mainTable").flexigrid(guidMap, thisObj);

    $('#dlg').dialog(
            {
                modal:true,
                closed:true,
                buttons: {
                    '继续捡配?': function() {
                        var packageIds = $O("packageIds").value;
                        $ajax('../sail/ship.do?method=addPickup&confirm=1&packageIds=' + packageIds, success);
                    },
                    "取 消": function() {
                        $('#dlg').dialog({closed:true});
                    }
                }});

    $ESC('dlg');
}

function autoPickup()
{
    $l(gurl + 'preForAutoPickup');
}

function mergePackages()
{
    var clis = getCheckBox('checkb');

    if (clis.length >=2)
    {
        var str = '';
        for (var i = 0; i < clis.length; i++)
        {
            str += clis[i].value + '~';
        }

        if (window.confirm('确定合并选中的发货单?'))
        {
            $l(gurl + 'preForMergePackages&&packageIds='+str);
        }
    } else{
        alert("请至少选择2个待合并的CK单！");
    }
}
 
function $callBack()
{
    loadForm();
    //highlights($("#mainTable").get(0), ['驳回'], 'red');
}

function addBean(opr, grid)
{
    var clis = getCheckBox('checkb');
    
    if (clis.length > 0)
    {
//        var str = '';
        for (var i = 0; i < clis.length; i++)
        {
            str += clis[i].value + '~';
        }
        
        if (window.confirm('确定拣配选中的发货单?'))
        {
//            $ajax('../sail/ship.do?method=addPickup&packageIds=' + str, callBackFun, error);
            $O("packageIds").value = str;
            $ajax('../sail/ship.do?method=addPickup&packageIds=' + str, callback);
        }
    }
}

function callback(data)
{
//    console.log(data);
//    console.log(str);
    if (data.ret == 0){
        success();
    } else{
        $O('dia_inner').innerHTML = data.msg;
        $('#dlg').dialog({closed:false});
    }
    str = "";
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function success(){
    alert("捡配成功！");
//    document.location.reload(true);
    //remove current row from table
//    console.log($O('mainTable').innerHTML)
    var packageIds = $O("packageIds").value;
//    console.log(packageIds);
    var packageList = packageIds.split("~");
    for (var i = 0; i < packageList.length; i++) {
        if (!isEmpty(packageList[i])){
            var selector = 'td[title='+packageList[i]+']';
//            console.log(selector);
//            console.log($(selector).parent());
            $(selector).parent().remove();
        }
    }
//    $('#dlg').dialog({closed:false});
//    $('td[title="CK201505171017648495"]').parent().remove();
//    console.log("ok!")
}

function undoBean(opr, grid)
{
    var clis = getCheckBox('checkb');
    
    if (clis.length > 0)
    {
        var str = '';
        for (var i = 0; i < clis.length; i++)
        {
            str += clis[i].value + '~';
        }
        
        if (window.confirm('确定撤销?'))
        {
            $ajax(gurl + 'delete' + ukey + '&packageIds=' + str, callBackFun);
        }
    }
    else
        $error('不能操作');
}

function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=query' + ukey);
}

</script>
</head>
<body onload="load()" class="body_class">

<form name="mainForm" method="post">
    <input type="hidden" name="packageIds" id="packageIds" value="">
<p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<div id="dlg" title="同一收货人或电话尚有CK单未合并" style="width:320px;">
    <div style="padding:20px;height:300px;" id="dia_inner" title="">
    </div>
</div>

<p:query/>

<%--<form id="pickupForm" method="post" action="../sail/ship.do?method=autoPickup">--%>
    <%--<div id="dlg" title="需自动拣配的批次数量" style="width:320px;">--%>
        <%--<div style="padding:20px;height:300px;" id="dia_inner" title="">--%>
            <%--<input type="text" name="pickupCount" placeholder="需自动拣配的批次数量">--%>
            <%--<input type="text" name="productName" placeholder="产品名称">--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</form>--%>
<%--<div id="dlg" title="同一收货人或电话尚有CK单未合并" style="width:320px;">--%>
<%--</div>--%>
<%--<div id="dlg" title="同一收货人或电话尚有CK单未合并" style="width:320px;">--%>
    <%--<div style="padding:20px;height:200px;" id="dia_inner" title="">--%>
    <%--</div>--%>
<%--</div>--%>

</body>