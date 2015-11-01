<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加商品转换配置" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/math.js"></script>
<script language="javascript">
    var index = -1;
function addBean()
{
	submit('确定增加商品转换配置?', null, null);
}

function selectProduct(idx)
{
    index = idx;
    window.common.modal('../product/product.do?method=rptQueryProduct&load=1&selectMode=1');
}

function getProduct(oos)
{
    var obj = oos[0];

    if (index == 1){
        $O('srcProductName').value = obj.pname;
        $O('srcProductId').value = obj.value;
    } else if (index ==2){
        $O('destProductName').value = obj.pname;
        $O('destProductId').value = obj.value;
    }
}

function clears(idx)
{
    if (idx == 1){
        $O('srcProductId').value = '';
        $O('srcProductName').value = '';
    } else if (idx ==2){
        $O('destProductId').value = '';
        $O('destProductName').value = '';
    }
}

</script>

</head>
<body class="body_class">
<form name="formEntry" action="../sail/productExchangeConfig.do" method="post">
<input type="hidden" name="method" value="add">
<input type="hidden" name="srcProductId" value="">
<input type="hidden" name="destProductId" value="">

<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">商品转换配置管理</span> &gt;&gt; 增加商品转换</td>
	<td width="85"></td>
	
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>商品转换配置基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.sail.bean.ProductExchangeConfigBean" />

		<p:table cells="1">
		    
			<p:pro field="srcProductId" innerString="size=60">
			     <input type="button" value="&nbsp;选择产品&nbsp;" name="qout1" id="qout1"
                    class="button_class" onclick="selectProduct(1)">&nbsp;
                 <input type="button" value="&nbsp;清 空&nbsp;" name="qout" id="qout"
                        class="button_class" onclick="clears(1)">&nbsp;&nbsp;
			</p:pro>
			
			<%--<p:pro field="srcProductName" innerString="size=60"/>--%>

            <p:pro field="srcAmount" value="0" innerString="size=60 oncheck='isInt'"/>

            <p:pro field="destProductId" innerString="size=60">
                <input type="button" value="&nbsp;选择发货商品&nbsp;" name="qout1" id="qout1"
                       class="button_class" onclick="selectProduct(2)">&nbsp;
                <input type="button" value="&nbsp;清 空&nbsp;" name="qout" id="qout"
                       class="button_class" onclick="clears(2)">&nbsp;&nbsp;
            </p:pro>

            <%--<p:pro field="destProductName" innerString="size=60"/>--%>
			
			<p:pro field="destAmount" value="0" innerString="size=60 oncheck='isInt'"/>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" id="ok_b"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>

	<p:message2/>
</p:body></form>
</body>
</html>

