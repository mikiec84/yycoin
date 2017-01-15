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
function process()
{
	$l('../sail/ship.do?method=generateInvoiceins');
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
				<td align="center" class="td_class">选择</td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>开票申请</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>开票抬头</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>开票品名</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>数量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>单价</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>金额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>税率</strong></td>
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
                </tr>
            </c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1"/>
	
	<br>

	<p:button>
		<div align="right">
			<input type="button" class="button_class"
				value="&nbsp;&nbsp;打印发票&nbsp;&nbsp;" onclick="process()">&nbsp;&nbsp;
			</div>	
	</p:button>

	<p:message2 />

</p:body></form>

</body>
</html>

