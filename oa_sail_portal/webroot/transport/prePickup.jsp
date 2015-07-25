<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="出库单列表" />
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

function sub()
{
	var packagesIds = "${packagesIds}";
	console.log(packagesIds);
	$l('../sail/ship.do?method=addPickup&confirm=1&packageIds='+packageIds);
}

function cancelPickup()
{
	$l('../transport/queryPackage.jsp');
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../sail/ship.do">
	<input type="hidden" name="method" value="addPickup">

	<p:navigation
	height="22">
	<td width="550" class="navigation">出库单管理 &gt;&gt; 未合并出库单列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>出库单列表</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class" ><strong>客户名或手机号</strong></td>
				<td align="center" class="td_class" ><strong>出库单</strong></td>
			</tr>

			<c:forEach var="entry" items="${map}">
				<tr class="content2">
					<td width="10%"><table class="border1"><tr><td align="center">${entry.key}</td></tr></table></td>
					<td width="30%"><table class="border1"><tr><td align="center"></td></tr></table></td>				</tr>
				<c:forEach var="item" items="${entry.value}" varStatus="vs">
					<tr class="content2">
						<td width="10%"><table class="border1"><tr><td align="center">${vs.index + 1}</td></tr></table></td>
						<td width="30%"><table class="border1"><tr><td align="center">${item}</td></tr></table></td>
					</tr>
				</c:forEach>
			</c:forEach>
            
		</table>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="98%" rightWidth="2%">
		
		<div align="right">
			<input type="button"
			class="button_class" onclick="sub()"
			value="&nbsp;&nbsp;确认捡配&nbsp;&nbsp;">&nbsp;&nbsp;
            <input type="button"
                   class="button_class" onclick="cancelPickup()"
                   value="&nbsp;&nbsp;取消拣配&nbsp;&nbsp;">&nbsp;&nbsp;

		</div>
	</p:button>

	<p:message2 />

</p:body></form>

</body>
</html>

