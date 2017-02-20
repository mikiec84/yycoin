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
	var oDOM = null;
	if (typeof DOMParser != "undefined"){
		var oParser = new DOMParser();
		var oDOM = oParser.parseFromString(result, "text/xml");
	}else if (typeof ActiveXObject != "undefined") {
		//IE8
		oDOM = new ActiveXObject("Microsoft.XMLDOM");
		oDOM.async = false;
		oDOM.loadXML(result);
		if (oDOM.parseError != 0) {
			throw new Error("XML parsing error: " + oDOM.parseError.reason);
		}
	}else {
		alert("No XML parser available.");
	}

	var result = oDOM.getElementsByTagName("Result")[0].childNodes[0].nodeValue;
//	alert(result);
	if (result === '1') {
		var msg = oDOM.getElementsByTagName("ErrMsg")[0].childNodes[0].nodeValue;
		alert(msg);
	}
}


function Invoice(){
    //TODO
    var packageId = $O('packageId').value;

//	var inv = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><invinterface><invhead><fpzl>2</fpzl><djhm>1002009ZKI</djhm><gfmc>南京某有限公司</gfmc><gfsh>320100000011111</gfsh><gfyh>购方开户行及账号 11112222</gfyh><gfdz>购方地址电话 025-11111111</gfdz><fpsl>17</fpsl><fpbz>备注</fpbz><kprm>开票人</kprm><fhrm>复核人</fhrm><skrm>收款人</skrm><hsbz>1</hsbz><xfdz>销方地址及电话 22222222</xfdz><xfyh>销方开户行及账号 222211</xfyh><hysy>0</hysy></invhead><invdetails><details><spmc>A商品</spmc><ggxh>规格</ggxh><jldw>吨</jldw><spsl>10</spsl><spdj>11.7</spdj><spje>117</spje><spse>17</spse><zkje></zkje><flbm>304020101</flbm><kcje></kcje></details></invdetails></invinterface>";
//	var xml =  a.JsaeroKP(inv);
//	alert(xml);
    $ajax('../sail/ship.do?method=generateInvoiceinsXml&packageId='+packageId, callbackGenerateInvoice);
}

//开票
function callbackGenerateInvoice(data)
{
//	console.log(data);
//	console.log(data.obj);
//	var result = JSON.parse(data.obj);
//	console.log(result);
	if (data.retMsg.toLowerCase() === "ok") {
		for (var key in data.obj) {
			var response =  a.JsaeroKP(data.obj[key]);
			alert(response);
			var oDOM = null;
			if (typeof DOMParser != "undefined"){
				var oParser = new DOMParser();
				var oDOM = oParser.parseFromString(response, "text/xml");
			}else if (typeof ActiveXObject != "undefined") {
				//IE8
				oDOM = new ActiveXObject("Microsoft.XMLDOM");
				oDOM.async = false;
				oDOM.loadXML(response);
				if (oDOM.parseError != 0) {
					throw new Error("XML parsing error: " + oDOM.parseError.reason);
				}
			}else {
				alert("No XML parser available.");
			}

			var result = oDOM.getElementsByTagName("Result")[0].childNodes[0].nodeValue;
//			alert(result);
			if (result === '0'){
				var fphm = oDOM.getElementsByTagName("fphm")[0].childNodes[0].nodeValue;
				alert(fphm);
				var fpdm = oDOM.getElementsByTagName("fpdm")[0].childNodes[0].nodeValue;
				alert(fpdm);
				//update fphm
				var packageId = $O('packageId').value;
				$ajax('../finance/invoiceins.do?method=generateInvoiceins&insId='+key+'&fphm='+fphm+"&packageId="+packageId+"&fpdm="+fpdm, callbackUpdateInsNum);
			}else{
				var msg = oDOM.getElementsByTagName("ErrMsg")[0].childNodes[0].nodeValue;
				alert(msg);
			}
		}
	}
//    var inv = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><invinterface><invhead><fpzl>2</fpzl><djhm>1002009ZKI</djhm><gfmc>南京某有限公司</gfmc><gfsh>320100000011111</gfsh><gfyh>购方开户行及账号 11112222</gfyh><gfdz>购方地址电话 025-11111111</gfdz><fpsl>17</fpsl><fpbz>备注</fpbz><kprm>开票人</kprm><fhrm>复核人</fhrm><skrm>收款人</skrm><hsbz>1</hsbz><xfdz>销方地址及电话 22222222</xfdz><xfyh>销方开户行及账号 222211</xfyh><hysy>0</hysy></invhead><invdetails><details><spmc>A商品</spmc><ggxh>规格</ggxh><jldw>吨</jldw><spsl>10</spsl><spdj>11.7</spdj><spje>117</spje><spse>17</spse><zkje></zkje><flbm>304020101</flbm><kcje></kcje></details></invdetails></invinterface>";
//    var xml =  a.JsaeroKP(inv);
//    alert(xml);
}

function parseXml(response){
	var oDOM = null;
	if (typeof DOMParser != "undefined"){
		var oParser = new DOMParser();
		var oDOM = oParser.parseFromString(response, "text/xml");
	}else if (typeof ActiveXObject != "undefined") {
		//IE8
		oDOM = new ActiveXObject("Microsoft.XMLDOM");
		oDOM.async = false;
		oDOM.loadXML(response);
		if (oDOM.parseError != 0) {
			throw new Error("XML parsing error: " + oDOM.parseError.reason);
		}
	}else {
		alert("No XML parser available.");
	}
	return oDOM;
}

//打印发票
function callbackUpdateInsNum(data){
	var obj = data.obj;
//	console.log(obj);
//	alert("obj.insId:"+obj.insId);
//	alert(obj.id);
//	alert(obj.invoiceNum);
	//TODO print
	var xml =  a.JsaeroDY(obj.insId,obj.id,obj.invoiceNum,"0");
	alert(xml);
	var oDOM = parseXml(xml);
	var result = oDOM.getElementsByTagName("Result")[0].childNodes[0].nodeValue;
	if (result === '1') {
		var msg = oDOM.getElementsByTagName("ErrMsg")[0].childNodes[0].nodeValue;
		alert(msg);
	}

    // display invoice number
	alert(data.extraObj);
    var insDiv = $O(data.extraObj);
	insDiv.value=obj.invoiceNum;
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
                    <td align="center">${item.itemAmount}</td>
					<td align="center">${item.price}</td>
					<td align="center">${item.moneys}</td>
					<td align="center">${item.sl}</td>
                    <td align="center"><input type="text" readonly id="${item.id}"></td>
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

