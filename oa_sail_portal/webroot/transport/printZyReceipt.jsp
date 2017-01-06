<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
    <%--<p:link title="打印中原银行回执单" link="false" guid="true" cal="true" dialog="true" />--%>
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
                        <table width="100%" cellspacing='0' cellpadding="0"  class="border">
                            <tr class="content2">
                                <td width="10%"><table class="border1"><tr><td align="center">企业代码</td></tr></table></td>
                                <td width="10%"><table class="border1"><tr><td align="center">产品代码</td></tr></table></td>
                                <td width="10%"><table class="border1"><tr><td align="center">规格代码</td></tr></table></td>
                                <td width="30%"><table class="border1"><tr><td align="center">产品名称</td></tr></table></td>
                                <td width="10%"><table class="border1"><tr><td align="center">数量(套)</td></tr></table></td>
                                <td width="30%"><table class="border1"><tr><td align="center">订单类型</td></tr></table></td>
                            </tr>

                            <c:forEach items="${bean.itemList}" var="item" varStatus="vs">
                                <tr class="content2">
                                    <td><table class="border1"><tr><td align="center">102</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.productCode}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">01</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.productName}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.amount}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.itemType}</td></tr></table></td>
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
                                <td><table class="border1"><tr><td align="center">合计:</td></tr></table></td>
                                <td><table class="border1"><tr><td align="center"></td></tr></table></td>
                                <td><table class="border1"><tr><td align="center"></td></tr></table></td>
                                <td><table class="border1"><tr><td align="center"></td></tr></table></td>
                                <td><table class="border1"><tr><td align="center">${total}</td></tr></table></td>
                                <td><table class="border1"><tr><td align="center"></td></tr></table></td>
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
                                <td colspan="3" width="50%"><table><tr><td>调出单位：永银文化创意产业发展有限公司</td></tr></table></td>
                                <td width="50%"><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3" width="50%"><table><tr><td>调入分行：${branchName}</td></tr></table></td>
                                <td width="50%"><table><tr><td>调入支行：${bean.customerName}</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>经办人员：</td></tr></table></td>
                                <td><table><tr><td>经办人员：</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>监交人：</td></tr></table></td>
                                <td><table><tr><td>收件人：${bean.receiver}</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>交接时间：&nbsp;&nbsp;${year}&nbsp;&nbsp;年&nbsp;&nbsp;${month}&nbsp;&nbsp;月&nbsp;&nbsp;${day}&nbsp;&nbsp;日</td></tr></table></td>
                                <td><table><tr><td>收件人电话：${bean.mobile}</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="4"><table><tr><td>备注：交接确认单一式两联，第一联作为调出单位记账凭证附件，第二联作为调入单位记账凭证附件。</td></tr></table></td>
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
