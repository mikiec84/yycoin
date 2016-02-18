<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<link href="../css/self.css" type=text/css rel=stylesheet>
<script language="javascript">
function pagePrint()
{
	document.getElementById('ptr').style.display = 'none';
	window.print();
	document.getElementById('ptr').style.display = 'block';

	var pickupId = $O('pickupId').value;
	var index_pos = $O('index_pos').value;

	var packageId = $O('packageId').value;
	var subindex_pos = $O('subindex_pos').value;

	var compose = $O('compose').value;
    var batchPrint = $O('batchPrint').value;

    if ($$('allPackages') == index_pos && batchPrint == '0')
    {
        var pickupId = $O('pickupId').value;
        var index_pos = $O('index_pos').value;
        var compose = $O('compose').value;

        // 链到交接清单打印界面
        $l("../sail/ship.do?method=printHandover&pickupId="+pickupId+"&index_pos=0" + "&compose=" + compose);
    } else{
        // 链到客户出库单打印界面
        $l("../sail/ship.do?method=findOutForReceipt&pickupId="
                +pickupId+"&index_pos="+index_pos +"&packageId=" + packageId + "&subindex_pos=" + subindex_pos
                + "&compose=" + compose+ "&batchPrint=" + batchPrint);
    }
}

function callBackPrintFun()
{
	//
}

</script>
</head>
<body>
<input type="hidden" name="pickupId" value="${pickupId}">
<input type="hidden" name="index_pos" value="${index_pos}">
<input type="hidden" name="packageId" value="${packageId}">
<input type="hidden" name="subindex_pos" value="${subindex_pos}">
<input type="hidden" name="compose" value="${compose}">
<input type="hidden" name="batchPrint" value="${batchPrint}">
<input type="hidden" name="allPackages" value="${allPackages}">
<input type="hidden" name="printMode" value="${printMode}">
<input type="hidden" name="printSmode" value="${printSmode}">
<input type="hidden" name="stafferName" value="${stafferName}">

<table width="90%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>

			</tr>
			<tr>
				<td align="center">
				<table width="100%" border="0" cellspacing="2">
					<tr>
						<td style="height: 27px" align="center">
                            <font size=5>
                                <b>
						${title}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${bean.emergency == 1}">紧急订单</c:if>
                                </b>
                            </font>
                        </td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>


	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td colspan='2' align='center'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<table width="100%" cellspacing='0' cellpadding="0">
					<tr class="content2">
						<td colspan="3" width="50%"><table><tr><td>发货时间：${bean.repTime}</td></tr></table></td>
						<td width="50%"><table><tr><td>传真：025-51885907/025-51885911</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="3"><table><tr><td>收货时间：</td></tr></table></td>
						<td><table><tr><td>联系人：${stafferName} </td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="3"><table><tr><td></td></tr></table></td>
						<td><table><tr><td>电话：${phone}</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="4"><table><tr><td>今收到订单编号为( ${bean.refId})的以下产品:</td></tr></table></td>
					</tr>
				</table>
				</td>
			</tr>
			
			<tr>
				<td>
				<table width="100%" cellspacing='0' cellpadding="0"  class="border">
					<tr class="content2">
						<td width="20%"><table class="border1"><tr><td align="center">产品分类名称</td></tr></table></td>
                        <td width="20%"><table class="border1"><tr><td align="center">银行产品编号</td></tr></table></td>
                        <td width="8%"><table class="border1"><tr><td align="center">订货数量</td></tr></table></td>
                        <td width="8%"><table class="border1"><tr><td align="center">收货数量</td></tr></table></td>
						<td width="10%"><table class="border1"><tr><td align="center">订单日期</td></tr></table></td>
                        <td width="26%"><table class="border1"><tr><td align="center">备注</td></tr></table></td>
					</tr>
					
					<c:forEach items="${bean.itemList}" var="item" varStatus="vs">
					<tr class="content2">
						<td><table class="border1"><tr><td>${item.productName}</td></tr></table></td>
                        <td><table class="border1"><tr><td>${item.productCode}</td></tr></table></td>
                        <td><table class="border1"><tr><td align="center">${item.amount}</td></tr></table></td>
						<td><table class="border1"><tr><td></td></tr></table></td>
						<td><table class="border1"><tr><td>${item.poDate}</td></tr></table></td>
						<td><table class="border1"><tr><td>${item.description}${item.printText}</td></tr></table></td>
					</tr>
					</c:forEach>
					
					<c:forEach varStatus="vs" begin="1" end="${(2 - my:length(vo.itemList)) > 0 ? (2 - my:length(vo.itemList)) : 0}">
					<tr class="content2">
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
						<td><table class="border1"><tr><td></td></tr></table></td>
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
					</tr>
					</c:forEach>
					<tr class="content2">
						<td colspan="2"><table class="border1"><tr><td align="center">合计(人民币):</td></tr></table></td>
						<td><table class="border1"><tr><td align="center">${total}</td></tr></table></td>
						<td><table class="border1"><tr><td align="center">-</td></tr></table></td>
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="6">
                            <table class="border1"><tr><td align="center">注："此收货回执单仅做为收货确认所用，其价格不作为对帐与结算的依据”</td></tr></table>
                        </td>
					</tr>
				</table>
				</td>
			</tr>
			
			<tr>
				<td height="15"></td>
			</tr>
			
			<tr>
				<td>
				<table width="100%" cellspacing='0' cellpadding="0">
					<tr class="content2">
						<td colspan="3" width="50%"><table><tr><td>所属分行：${bean.customerName}</td></tr></table></td>
						<td width="50%"><table><tr><td></td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="3"><table><tr><td>收货人：${bean.receiver}</td></tr></table></td>
						<td><table><tr><td>收货人身份证号：</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="3"><table><tr><td>电话：${bean.mobile}</td></tr></table></td>
						<td><table><tr><td></td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="3"><table><tr><td>邮编：</td></tr></table></td>
						<td><table><tr><td>地址：</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="3"><table><tr><td>收货人签字：</td></tr></table></td>
						<td><table><tr><td>收货人盖章：</td></tr></table></td>
					</tr>
                    <tr class="content2">
                        <td colspan="4"><table><tr><td>盖章签字后寄回地址：南京市应天大街388号1865文化创意产业园C2栋永银文化银行事业部商务部收（烦请将回执传真后原件邮寄回我司，谢谢）</td></tr></table></td>
                    </tr>
					<tr class="content2">
						<td colspan="3"><table><tr><td></td></tr></table></td>
						<td><table><tr><td></td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="4"><table><tr>
						<td>
						</td>
						</tr></table></td>
					</tr>
				</table>
				</td>
			</tr>
			
		</table>

		</td>
	</tr>
	
	<tr id="ptr">
		<td width="92%">
		<div align="right"><input type="button" name="pr"
			class="button_class" onclick="pagePrint()"
			value="&nbsp;&nbsp;打 印&nbsp;&nbsp;"></div>
		</td>
	</tr>
</table>
</body>
</html>
