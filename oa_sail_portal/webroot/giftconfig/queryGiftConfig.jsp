<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <p:link title="赠品配置" link="true" guid="true" cal="true" dialog="true" />
    <script src="../js/common.js"></script>
    <script src="../js/public.js"></script>
    <script src="../js/pop.js"></script>
    <script src="../js/plugin/highlight/jquery.highlight.js"></script>
    <script type="text/javascript">
        var gurl = '../sail/giftconfig.do?method=';
        var addUrl = '../giftconfig/addGiftConfig.jsp';
        var ukey = 'GiftConfig';

        var allDef = window.top.topFrame.allDef;
        var guidMap;
        var thisObj;
        function load()
        {
            preload();

            guidMap = {
                title: '赠品配置列表',
                url: gurl + 'query' + ukey,
                colModel : [
                    {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id}>', width : 40, align: 'center'},
                    {display: '活动描述', name : 'activity', width : '20%'},
                    {display: '适用银行', name : 'bank', width : '10%'},
                    {display: '开始日期', name : 'beginDate', width : '8%'},
                    {display: '结束日期', name : 'endDate', width : '8%'},
                    {display: '销售商品品名', name : 'productName', width : '15%'},
                    {display: '销售商品数量', name : 'sailAmount', width : '6%'},
                    {display: '赠送商品品名', name : 'giftProductName', width : '15%'},
                    {display: '赠送商品数量', name : 'amount', width : '6%'},
                    {display: '备注', name : 'description', width : 'auto'}
                ],
                extAtt: {
                    //name : {begin : '<a href=' + gurl + 'find' + ukey + '&id={id}>', end : '</a>'}
                },
                buttons : [
                    {id: 'add', bclass: 'add', onpress : addBean, auth: '0111'},
                    {id: 'update', bclass: 'update', onpress : updateBean, auth: '0111'},
                    {id: 'del', bclass: 'del',  onpress : delBean, auth: '0111'},
                    {id: 'search', bclass: 'search', onpress : doSearch}
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
            $l(gurl + 'preForAdd' + ukey);
            //$l(addUrl);
        }

        function delBean(opr, grid)
        {
            if (getRadio('checkb') && getRadioValue('checkb'))
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
<p:query/>
</body>