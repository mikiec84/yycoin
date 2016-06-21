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

//    console.log("batchPrint:"+batchPrint);
//    console.log("stafferName:"+$$('stafferName'));
//    console.log("index_pos:"+index_pos);
//    console.log("allPackages:"+$$('allPackages'));
    //连打模式下，并且回执单都已经打印完毕就跳转到交接单打印
//    if ($$('allPackages') == index_pos && batchPrint == '0' && $$('stafferName') == '叶百韬')
    //2015/3/26 最后打印的回执单可能不是叶百韬的单子，这个判断到打印交接单时做
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
                            <td align="left" style="height: 27px"><img src="${qrcode}"/></td>
                            <td align="right"><img src="${qrcode}"/></td>
                        </tr>
                        <tr>
                            <td style="height: 27px" align="center" colspan="2">
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
						<td colspan="2" width="50%"><table><tr><td>出库时间：${year}-${month}-${day}</td></tr></table></td>
						<td colspan="2" width="50%"><table><tr><td>收货客户：${bean.customerName}</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td><table><tr><td>业务联系：${stafferName}</td></tr></table></td>
						<td colspan="3"><table><tr><td>客服电话：4006518859</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td><table><tr><td>业务电话：${phone}</td></tr></table></td>
						<td><table><tr><td>客服传真：025-51885923</td></tr></table></td>
                        <td><table><tr><td>收货人员：${bean.receiver}</td></tr></table></td>
                        <td><table><tr><td>联系电话：${bean.mobile}</td></tr></table></td>
					</tr>
				</table>
				</td>
			</tr>
			
			<tr>
				<td>
				<table width="100%" cellspacing='0' cellpadding="0"  class="border">
					<tr class="content2">
						<td width="20%"><table class="border1"><tr><td align="center">产品名称</td></tr></table></td>
                        <td width="8%"><table class="border1"><tr><td align="center">代码</td></tr></table></td>
						<td width="8%"><table class="border1"><tr><td align="center">数量</td></tr></table></td>
						<td width="8%"><table class="border1"><tr><td align="center">订单性质</td></tr></table></td>
						<td width="20%"><table class="border1"><tr><td align="center">客户单号</td></tr></table></td>
						<td width="10%"><table class="border1"><tr><td align="center">订单日期</td></tr></table></td>
                        <td width="8%"><table class="border1"><tr><td align="center">顾客姓名</td></tr></table></td>
					</tr>
					
					<c:forEach items="${bean.itemList}" var="item" varStatus="vs">
					<tr class="content2">
						<td><table class="border1"><tr><td>${item.productName}</td></tr></table></td>
                        <td><table class="border1"><tr><td align="center">${item.productCode}</td></tr></table></td>
						<td><table class="border1"><tr><td align="center">${item.amount}</td></tr></table></td>
						<td><table class="border1"><tr><td>${item.itemType}</td></tr></table></td>
						<td><table class="border1"><tr><td>${item.refId}</td></tr></table></td>
						<td><table class="border1"><tr><td>${item.poDate}</td></tr></table></td>
                        <td><table class="border1"><tr><td>${item.customerName}</td></tr></table></td>
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
						<td><table class="border1"><tr><td align="center"></td></tr></table></td>
					</tr>
					</c:forEach>
				</table>
				</td>
			</tr>
			
			<tr>
				<td height="20"></td>
			</tr>
			
			<tr>
				<td>
				<table width="100%" cellspacing='0' cellpadding="0">
					<tr class="content2">
						<td colspan="3" width="50%"><table><tr><td>发货单位盖章：</td></tr></table></td>
						<td width="50%"><table><tr><td>收货人签字确认：</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="4"><table><tr><td>备注：</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="4"><table><tr><td>1.此发货清单仅作为收货确认使用。请依据本清单清点货品，请特别留意：如有发票，发票与发货清单一起放置在透明塑封袋中。</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="4"><table><tr><td>2.约定有配套赠品的，本次如未配送，将随后补发，请留意收货。</td></tr></table></td>
					</tr>
					<tr class="content2">
						<td colspan="4"><table><tr><td>3.如需要退换补货，请留存照片等说明情况的依据，<b>在收货后三日内联系我方人员</b>，提供需退换补货的订单号及退货快递单号。需退回的货品，烦请保留所有配件完整，原样（含发票）完整打包后寄回。不原路返回的退款要求，请同时寄回有效盖章的退款函，注明退回账号信息。</td></tr></table></td>
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
