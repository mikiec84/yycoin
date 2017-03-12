<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <p:link title="合成申请管理" link="true" guid="true" cal="true" dialog="true" />
    <script src="../js/common.js"></script>
    <script src="../js/public.js"></script>
    <script src="../js/pop.js"></script>
    <script src="../js/plugin/highlight/jquery.highlight.js"></script>
    <script type="text/javascript">

        var gurl = '../product/product.do?method=';
        var addUrl = '../product/addProduct.jsp';
        var ukey = 'Compose';

        var allDef = getAllDef();
        var guidMap;
        var thisObj;
        function load()
        {
            preload();

            guidMap = {
                title: '合成申请列表',
                url: gurl + 'querySelfCompose' ,
                colModel : [
                    {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status}>', width : 40, align: 'center'},
                    {display: '标识', name : 'id', width : '15%'},
                    {display: '产品', name : 'productName', content: '{productName}({productCode})', width : '15%'},
                    {display: '数量', name : 'amount', width : '5%'},
                    {display: '价格', name : 'price', width : '8%', toFixed: 2},
                    {display: '核对', name : 'checkStatus', cc: 'pubCheckStatus', width : '8%'},
                    {display: '类型', name : 'type', cc : 'composeType', width : '8%'},
                    {display: '状态', name : 'status', cc : 'composeStatus', width : '15%'},
                    {display: '合成人', name : 'stafferName', width : '8%'},
                    {display: '时间', name : 'logTime', content: '{logTime}' ,cname: 'logTime',  sortable : true, width : 'auto'}
                ],
                extAtt: {
                    id : {begin : '<a href=' + gurl + 'find' + ukey + '&id={id}>', end : '</a>'}
                },
                buttons : [
                    {id: 'add', bclass: 'add', onpress : addBean, auth: '0000'},
                    {id: 'update', bclass: 'update', onpress : updateBean, auth: '0000'},
                    {id: 'del', bclass: 'del',  onpress : delBean, auth: '0000'}
//             {id: 'search', bclass: 'search', onpress : doSearch}
                ],
                <p:conf/>
            };

            $("#mainTable").flexigrid(guidMap, thisObj);
        }

        function $callBack()
        {
            loadForm();
            highlights($("#mainTable").get(0), ['结束'], 'blue');
            highlights($("#mainTable").get(0), ['驳回', '未关联报销'], 'red');
        }

        function addBean(opr, grid)
        {
            $l(gurl + 'preForCompose&menu=1');
        }

        function delBean(opr, grid)
        {
            if (getRadio('checkb') &&
                    (getRadio('checkb').lstatus == 0 || getRadio('checkb').lstatus == 1 || getRadio('checkb').lstatus == 6))
            {
                if(window.confirm('确定删除?'))
                    $ajax(gurl + 'delete' + ukey + '&id=' + getRadioValue('checkb'), callBackFun);
            }
            else
                $error('不能操作');
        }

        function updateBean()
        {
            if (getRadio('checkb') && getRadioValue('checkb'))
            {
                $l(gurl + 'find' + ukey + '&update=1&id=' + getRadioValue('checkb'));
            }
            else
                $error('不能操作');
        }

        //
        //function doSearch()
        //{
        //    $modalQuery('../admin/query.do?method=popCommonQuery2&key=tcp.querySelfTravelApply');
        //}

    </script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm" method="post">
    <p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<p:query height="300px"/>
</body>