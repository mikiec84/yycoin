<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <p:link title="商品转换配置" link="true" guid="true" cal="true" dialog="true" />
    <script src="../js/common.js"></script>
    <script src="../js/public.js"></script>
     <script src="../js/pop.js"></script>
    <script src="../js/plugin/highlight/jquery.highlight.js"></script>
    <script src="../js/jquery.blockUI.js"></script>
    <script type="text/javascript">

        var gurl = '../sail/productExchangeConfig.do?method=';
//        var ukey = 'ProductExchange';

        var addUrl = '../sail/addProductExchange.jsp';

        var allDef = window.top.topFrame.allDef;
        var guidMap;
        var thisObj;
        function load()
        {
            preload();

            guidMap = {
                title: '商品转换配置',
                url: gurl + 'list'   ,
                colModel : [
                    {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} >', width : 40, align: 'center'},
                    {display: '商品名', name : 'srcProductName', width : '30%'},
                    {display: '商品数量', name : 'srcAmount', width : '10%'},
                    {display: '发货商品名', name : 'destProductName', width : '30%'},
                    {display: '发货商品数量', name : 'destAmount', width : 'auto'}
                ],
                extAtt: {
                },
                buttons : [
                    {id: 'add', bclass: 'add', onpress : addBean},
                    {id: 'update', bclass: 'update', onpress : updateBean},
                    {id: 'del', bclass: 'del',  onpress : delBean}
//                    {id: 'search', bclass: 'search', onpress : doSearch}
                ],
                <p:conf/>
            };

            $("#mainTable").flexigrid(guidMap, thisObj);

        }

        function $callBack()
        {
            loadForm();
        }

        function addBean(opr, grid)
        {
            $l(addUrl);
        }

        function delBean(opr, grid)
        {
            if (getRadio('checkb') && getRadioValue('checkb'))
            {
                if(window.confirm('确定删除?'))
                    $ajax(gurl + 'delete' + '&id=' + getRadio('checkb').value, callBackFun);
            }
            else
                $error('不能操作');
        }

        function updateBean()
        {
            if (getRadio('checkb') && getRadioValue('checkb'))
            {
                $l(gurl + 'find' + '&update=1&id=' + getRadioValue('checkb'));
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
    <p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<div id="loadingDiv" style="display:none">
    <p>&nbsp;</p>
    <p>进在处理中......</p>
    <p><img src="../images/oa/process.gif" /></p>
    <p>&nbsp;</p>
</div>
<p:query height="40px"/>

<p:query />
</body>