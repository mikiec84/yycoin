<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="打印发票" />
<link href="../js/plugin/dialog/css/dialog.css" type="text/css" rel="stylesheet"/>
<script src="../js/title_div.js"></script>
<script src="../js/public.js"></script>
<script src="../js/JCheck.js"></script>
<script src="../js/common.js"></script>
<script src="../js/tableSort.js"></script>
<script src="../js/jquery/jquery.js"></script>
<script src="../js/plugin/dialog/jquery.dialog.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script src="../js/adapter.js"></script>
<script language="javascript">
var a=new ActiveXObject("JSTAXS.Tax");
var is_open=0;
var xmlDoc = "";
/*开启金税盘*/
function OpenCard(){
	var result = a.JsaeroOpen();
	alert(result);
}


function Invoice(){
    //TODO
    var packageId = $O('packageId').value;

//	var inv = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><invinterface><invhead><fpzl>2</fpzl><djhm>1002009ZKI</djhm><gfmc>南京某有限公司</gfmc><gfsh>320100000011111</gfsh><gfyh>购方开户行及账号 11112222</gfyh><gfdz>购方地址电话 025-11111111</gfdz><fpsl>17</fpsl><fpbz>备注</fpbz><kprm>开票人</kprm><fhrm>复核人</fhrm><skrm>收款人</skrm><hsbz>1</hsbz><xfdz>销方地址及电话 22222222</xfdz><xfyh>销方开户行及账号 222211</xfyh><hysy>0</hysy></invhead><invdetails><details><spmc>A商品</spmc><ggxh>规格</ggxh><jldw>吨</jldw><spsl>10</spsl><spdj>11.7</spdj><spje>117</spje><spse>17</spse><zkje></zkje><flbm>304020101</flbm><kcje></kcje></details></invdetails></invinterface>";
//	var xml =  a.JsaeroKP(inv);
//	alert(xml);
    $ajax('../sail/ship.do?method=generateInvoiceinsXml&packageId='+packageId, callBackFunPrint);
}

function process()
{
	var packageId = $O('packageId').value;
	$ajax('../sail/ship.do?method=generateInvoiceins&packageId='+packageId, callBackFunPrint);
}

function callBackFunPrint(data)
{
	console.log(data);
	var result = JSON.parse(data);
	if (result.retMsg.toLowerCase() === "ok") {
		for (var key in result.obj) {
			console.log(key + ': ' + result.obj[key]);
		}
	}
    var inv = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><invinterface><invhead><fpzl>2</fpzl><djhm>1002009ZKI</djhm><gfmc>南京某有限公司</gfmc><gfsh>320100000011111</gfsh><gfyh>购方开户行及账号 11112222</gfyh><gfdz>购方地址电话 025-11111111</gfdz><fpsl>17</fpsl><fpbz>备注</fpbz><kprm>开票人</kprm><fhrm>复核人</fhrm><skrm>收款人</skrm><hsbz>1</hsbz><xfdz>销方地址及电话 22222222</xfdz><xfyh>销方开户行及账号 222211</xfyh><hysy>0</hysy></invhead><invdetails><details><spmc>A商品</spmc><ggxh>规格</ggxh><jldw>吨</jldw><spsl>10</spsl><spdj>11.7</spdj><spje>117</spje><spse>17</spse><zkje></zkje><flbm>304020101</flbm><kcje></kcje></details></invdetails></invinterface>";
    var xml =  a.JsaeroKP(inv);
    alert(xml);
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../sail/ship.do">
	<input type="hidden" name="method" value="printInvoiceins">
	<input type="hidden" value="1" name="firstLoad">
	
	<p:navigation
	height="22">
	<td width="550" class="navigation">打印发票 &gt;&gt; </td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr class="content1">
				<td width="15%" align="center">CK单号：</td>
				<td align="left">
				<input name="packageId" size="20" value="${packageId}"  />
				</td>
			</tr>
			
			<tr class="content1">
				<td colspan="4" align="right"><input type="submit"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input
					type="reset" class="button_class"
					value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
			</tr>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>待打印发票列表</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>开票申请</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>开票抬头</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>开票品名</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>数量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>单价</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>金额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>税率</strong></td>
                <td align="center" class="td_class" onclick="tableSort(this)"><strong>发票号码</strong></td>
			</tr>
			
			<c:forEach items="${invoiceList}" var="item" varStatus="vs">
                <tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
                    <td align="center" onclick="hrefAndSelect(this)">
						<a href="../finance/invoiceins.do?method=findInvoiceins&id=${item.id}">
                    ${item.id}</a></td>
                    <td align="center">${item.headContent}</td>
                    <td align="center">${item.spmc}</td>
                    <td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
                    <td align="center"><div id="${item.id}"></div></td>
                </tr>
            </c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1"/>
	
	<br>

	<p:button>
		<div align="right">
			<input type="button" class="button_class"
				value="&nbsp;&nbsp;开启金税盘&nbsp;&nbsp;" onclick="OpenCard()">&nbsp;&nbsp;
            <input type="button" class="button_class"
                   value="&nbsp;&nbsp;打印发票&nbsp;&nbsp;" onclick="Invoice()">&nbsp;&nbsp;

			<%--<button onclick="OpenCard()">开启金税盘</button>--%>
			<%--<button onclick="Invoice()">打印发票</button>--%>
			</div>	
	</p:button>

	<p:message2 />

</p:body></form>

</body>
</html>

