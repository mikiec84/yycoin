<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
    <%--<p:link title="打印宁波银行回执单" link="false" guid="true" cal="true" dialog="true" />--%>
    <script language="JavaScript" src="../js/common.js"></script>
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
        <td height="10" colspan='2'>宁波银行股份有限公司：</td>
    </tr>

    <tr>
        <td height="10" colspan='2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;根据贵行订单，现我公司发货明细如下：</td>
    </tr>

    <tr>
        <td colspan='2' align='center'>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table width="100%" cellspacing='0' cellpadding="0"  class="border">
                            <tr class="content2">
                                <td width="4%"><table class="border1"><tr><td align="center">序号</td></tr></table></td>
                                <td width="8%"><table class="border1"><tr><td align="center">产品编码</td></tr></table></td>
                                <td width="30%"><table class="border1"><tr><td align="center">产品名称</td></tr></table></td>
                                <td width="10%"><table class="border1"><tr><td align="center">材质成色</td></tr></table></td>
                                <td width="8%"><table class="border1"><tr><td align="center">单件重量</td></tr></table></td>
                                <td width="8%"><table class="border1"><tr><td align="center">数量(实物)</td></tr></table></td>
                                <td width="8%"><table class="border1"><tr><td align="center">数量(包装)</td></tr></table></td>
                                <td width="8%"><table class="border1"><tr><td align="center">数量(证书)</td></tr></table></td>
                                <td width="16%"><table class="border1"><tr><td align="center">备注</td></tr></table></td>
                            </tr>

                            <c:forEach items="${bean.itemList}" var="item" varStatus="vs">
                                <tr class="content2">
                                    <td><table class="border1"><tr><td align="center">${vs.index+1}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.productCode}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.productName}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.materiaType}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.productWeight}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.productAmount}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.packageAmount}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.certificateAmount}</td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center">${item.description}${item.printText}</td></tr></table></td>
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
                                    <td><table class="border1"><tr><td align="center"></td></tr></table></td>
                                    <td><table class="border1"><tr><td align="center"></td></tr></table></td>
                                </tr>
                            </c:forEach>
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
                                <td colspan="4"><table><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：请贵行收验并加盖预留印鉴后，向我司快递/传真/邮件本确认单。从签收之日起15个工作日内未发出，则视贵行已收齐我司以上发货产品无误。</td></tr></table></td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td height="15"></td>
                </tr>

                <tr>
                    <td>
                        <table width="100%" cellspacing='0' cellpadding="0"  class="border">
                            <tr class="content2">
                                <td width="8%" rowspan="3">
                                    <table class="border1">
                                        <tr class="content2">
                                            <td colspan="3" width="50%"><table><tr><td>贵行如有问题请在此处注明：</td></tr></table></td>
                                            <td width="50%"><table><tr><td></td></tr></table></td>
                                        </tr>
                                        <tr class="content2">
                                            <td colspan="3"><table><tr><td></td></tr></table></td>
                                            <td><table><tr><td></td></tr></table></td>
                                        </tr>
                                        <tr class="content2">
                                            <td colspan="3"><table><tr><td></td></tr></table></td>
                                            <td><table><tr><td></td></tr></table></td>
                                        </tr>
                                    </table>
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
                                <td colspan="3" width="50%"><table><tr><td>发货方：永银文化</td></tr></table></td>
                                <td width="50%"><table><tr><td>收货方：${bean.customerName}</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>经办人员：${stafferName}</td></tr></table></td>
                                <td><table><tr><td>收货人员：${bean.receiver}</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>联系方式：${phone}</td></tr></table></td>
                                <td><table><tr><td>联系方式：${bean.mobile}</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>传真号码：025-51885923</td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>送货时间：${shipTime}</td></tr></table></td>
                                <td><table><tr><td>监交人员：</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td>盖章：</td></tr></table></td>
                                <td><table><tr><td>收货时间：</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td></td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="3"><table><tr><td></td></tr></table></td>
                                <td><table><tr><td>盖章/签字：</td></tr></table></td>
                            </tr>
                            <tr class="content2">
                                <td colspan="4">
                                    <table>
                                        <tr>
                                            <td>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

            </table>

        </td>
    </tr>

    <tr id="ptr">
        <td width="92%">
            <div align="right">
                <input type="button" name="pr" class="button_class" onclick="pagePrint()"
                                      value="&nbsp;&nbsp;打 印&nbsp;&nbsp;">
            </div>
        </td>
    </tr>
</table>
</body>
</html>
