<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
    <p:link title="采购单" />
    <script language="JavaScript" src="../js/common.js"></script>
    <script language="JavaScript" src="../js/JCheck.js"></script>
    <script language="JavaScript" src="../js/public.js"></script>
    <script language="JavaScript" src="../js/key.js"></script>
    <script language="JavaScript" src="../js/title_div.js"></script>
    <script language="JavaScript" src="../js/jquery/jquery.js"></script>
    <script language="JavaScript" src="../stock_js/addStockArrival.js"></script>

    <script language="javascript">
        function load()
        {
            loadForm();
        }

        var jmap = {};
        <c:forEach items="${map}" var="item">
        jmap['${item.key}'] = "${item.value}";
        </c:forEach>

        function showDiv(id)
        {
            if (jmap[id] != null && jmap[id] != '')
                tooltip.showTable(jmap[id]);
        }

        function addBean(opr)
        {
            submit('确定提交采购到货信息?', null, lverify);
        }

        function lverify()
        {
//            var checkArr = document.getElementsByName('check_init');
//
//            var isSelect = false;
//
//            var imap = {};
//
//            for (var i = 0; i < checkArr.length; i++)
//            {
//                var obj = checkArr[i];
//
//                var index = obj.value;
//
//                if (obj.checked)
//                {
//                    isSelect = true;
//                    if ($O('productName_' + i).value == '' || $O('productId_' + i).value == '' )
//                    {
//                        alert('产品不能为空');
//                        return false;
//                    }
//
//                    if ($$('amount_' + i)  == null)
//                    {
//                        alert('请选择产品是否满足数量要求');
//                        return false;
//                    }
//
//                    if (imap[$O('productId_' + i).value] == $O('productId_' + i).value)
//                    {
//                        alert('选择的产品不能重复');
//                        return false;
//                    }
//
//                    imap[$O('productId_' + i).value] = $O('productId_' + i).value;
//                }
//            }
//
//            if (!isSelect)
//            {
//                alert('请选择采购产品');
//                return false;
//            }

            return true;
        }

    </script>

</head>
<body class="body_class" onload="load()" onkeydown="tooltip.bingEsc(event)">
<form action="../stock/stock.do" name="formEntry">
    <input type="hidden" name="method" value="addStockArrival">
    <input type="hidden" name="stockId" value="${bean.id}">
    <p:navigation height="22">
        <td width="550" class="navigation">采购单明细</td>
        <td width="85"></td>
    </p:navigation> <br>

    <p:body width="98%">

        <p:title>
            <td class="caption"><strong>采购单信息：</strong></td>
        </p:title>

        <p:line flag="0" />

        <p:subBody width="100%">
            <p:table cells="2">
                <p:class value="com.china.center.oa.stock.bean.StockBean" opr="2"/>

                <p:cell title="单号">
                    ${bean.id}
                </p:cell>

                <p:cell title="采购人">
                    ${bean.userName}
                </p:cell>

                <p:cell title="库存人">
                    ${bean.owerName}
                </p:cell>

                <p:cell title="事业部">
                    ${bean.industryName}
                </p:cell>

                <p:cell title="状态">
                    ${my:get('stockStatus', bean.status)}
                </p:cell>

                <p:pro field="stockType">
                    <option value="">--</option>
                    <p:option type="stockSailType"></p:option>
                </p:pro>

                <p:pro field="stype">
                    <option value="">--</option>
                    <p:option type="stockStype"></p:option>
                </p:pro>

                <p:pro field="areaId" innerString="style='width: 300px'">
                    <option value="">--</option>
                    <p:option type="123"></p:option>
                </p:pro>

                <p:cell title="录入时间">
                    ${bean.logTime}
                </p:cell>

                <p:cell title="需到货时间">
                    ${bean.needTime}
                </p:cell>

                <p:cell title="物流">
                    ${bean.flow}
                </p:cell>

                <p:cell title="总计金额">
                    ${my:formatNum(bean.total)}
                </p:cell>

                <p:cells celspan="1" title="异常状态">
                    ${my:get('stockExceptStatus', bean.exceptStatus)}
                </p:cells>

                <p:cells celspan="1" title="询价类型">
                    外网/卢工/马甸询价
                </p:cells>

                <p:pro field="willDate" cell="1"/>

                <p:pro field="mode" cell="1">
                    <p:option type="stockMode"></p:option>
                </p:pro>

                <p:pro field="mtype" cell="1">
                    <p:option type="stockManagerType"></p:option>
                </p:pro>

                <p:pro field="ptype" cell="1">
                    <p:option type="natureType"></p:option>
                </p:pro>

                <p:cells celspan="2" title="备注">
                    ${bean.target}
                </p:cells>

                <p:cells celspan="2" title="备注">
                    ${bean.description}
                </p:cells>

                <p:cells celspan="2" title="发货说明">
                    ${bean.consign}
                </p:cells>

                <p:cell title="核对状态" end="true">
                    ${my:get('pubCheckStatus', bean.checkStatus)}
                </p:cell>

                <p:cells celspan="2" title="核对">
                    ${bean.checks}
                </p:cells>

                <p:cells celspan="2" title="关联凭证">
                    <c:forEach items="${financeBeanList}" var="item">
                        <a href="../finance/finance.do?method=findFinance&id=${item.id}">${item.id}</a>
                        &nbsp;
                    </c:forEach>
                </p:cells>

                <p:cells celspan="2" title="关联跟单">
                    <c:forEach items="${stockWorkBeanList}" var="item">
                        <a href="../stock/work.do?method=findStockWork&id=${item.id}">${item.id}</a>
                        &nbsp;
                    </c:forEach>
                </p:cells>

            </p:table>
        </p:subBody>

        <p:tr />

        <p:subBody width="100%">
            <table width="100%" border="0" cellspacing='1' id="tables1">
                <tr align="center" class="content0">
                    <td width="10%" align="center">采购产品</td>
                    <td width="5%" align="center">采购数量</td>
                    <td width="5%" align="center">当前数量</td>
                    <td width="5%" align="center">是否询价</td>
                    <td width="5%" align="center">是否拿货</td>
                    <td width="10%" align="center">参考/实际价格</td>
                    <td width="10%" align="center">付款时间</td>
                    <td width="15%" align="center">供应商</td>
                    <td width="5%" align="center">付款</td>
                    <td width="5%" align="center">是否入库</td>
                    <td width="10%" align="center">合计金额</td>
                    <td width="10%" align="center">描述</td>

                </tr>

                <c:forEach items="${bean.itemVO}" var="item" varStatus="vs">
                    <tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
                        <td align="center" style="cursor: pointer;"
                            onMouseOver="showDiv('${item.id}')" onmousemove="tooltip.move()" onmouseout="tooltip.hide()"
                                ><a href="../product/product.do?method=findProduct&id=${item.productId}&detail=1">${item.productName}</a></td>

                        <td align="center">${item.amount}</td>

                        <td align="center">${item.productNum}</td>

                        <td align="center">${item.status == 0 ? "否" : "是"}</td>

                        <td align="center">${my:get('stockItemFech', item.fechProduct)}</td>

                        <td align="center">${my:formatNum(item.prePrice)}/${my:formatNum(item.price)}</td>


                        <td align="center">${item.nearlyPayDate}</td>

                        <td align="center">${item.providerName}</td>

                        <td align="center">${item.pay == 0 ? "未付款" : "已付款"}</td>

                        <c:if test="${item.hasRef == 0}">
                            <td align="center">
                                <font color=red>否</font>
                            </td>
                        </c:if>

                        <c:if test="${item.hasRef == 1}">
                            <td align="center">
                                <a href=../sail/out.do?method=findOut&fow=99&outId=${item.refOutId}>是</a>
                            </td>
                        </c:if>

                        <td align="center">${my:formatNum(item.total)}</td>

                        <td align="center">${item.description}</td>

                    </tr>
                </c:forEach>
            </table>
        </p:subBody>

        <p:tr />

        <p:subBody width="100%">
            <table width="100%" border="0" cellspacing='1' id="tables">
                <tr align="center" class="content0">
                    <td width="30%" align="center">采购产品</td>
                    <td width="20%" align="center">数量</td>
                    <td width="30%" align="center">出货日期</td>
                    <td width="20%" align="center">预计到货日期</td>
                    <td width="5%" align="left">
                        <input type="button" accesskey="A" value="增加" class="button_class" onclick="addTr()">
                    </td>
                </tr>

                <tr id="trCopy" style="display: none;">
                    <td align="center">
                        <select name="productId">
                            <option value="">-</option>
                            <c:forEach items="${bean.itemVO}" var="item2" varStatus="vs">
                                <option value="${item2.productId}">${item2.productName}</option>
                            </c:forEach>
                        </select>
                    </td>

                    <td align="center"><input type="number" name="amount"></td>

                    <td align="center"><input type="date" name="deliveryDate"></td>

                    <td align="center"><input type="date" name="arrivalDate"></td>

                    <td align="left"><input type="button" value="清空"  class="button_class" onclick="clears()"></td>
                </tr>

                <c:forEach items="${bean.itemVO}" var="item" varStatus="vs">
                    <tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
                        <td align="center">
                            <input type="text" name="productName" value="${item.productName}" readonly>
                            <input type="hidden" name="productId" value="${item.productId}">
                        </td>

                        <td align="center"><input type="number" name="amount" value="${item.amount}"></td>

                        <td align="center"><input type="date" name="deliveryDate"></td>

                        <td align="center"><input type="date" name="arrivalDate"></td>

                        <td align="left"><input type="button" value="删除"  class="button_class" onclick="removeTr(this)"></td>
                    </tr>
                </c:forEach>
            </table>
        </p:subBody>

        <p:tr />


        <p:line flag="1" />

        <p:button leftWidth="100%" rightWidth="0%">
            <div align="right">
                <input type="button" class="button_class"
                                      onclick="javascript:history.go(-1)"
                                      value="&nbsp;&nbsp;返 回&nbsp;&nbsp;">
                <input type="button" class="button_class"
                       name="b_submit" style="cursor: pointer"
                       value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onclick="addBean(1)">&nbsp;&nbsp;
            </div>
        </p:button>

        <p:message2></p:message2>
    </p:body></form>
</body>
</html>

