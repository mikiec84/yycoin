<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
    <p:link title="修改发货方式" />
    <script language="JavaScript" src="../js/common.js"></script>
    <script language="JavaScript" src="../js/math.js"></script>
    <script language="JavaScript" src="../js/public.js"></script>
    <script language="JavaScript" src="../js/cnchina.js"></script>
    <script language="JavaScript" src="../js/JCheck.js"></script>
    <script language="JavaScript" src="../js/compatible.js"></script>
    <script language="JavaScript" src="../js/json.js"></script>
    <script language="javascript">
        var provinceMap = {};
        <c:forEach items="${cityList}" var="item">
        var cities = provinceMap['${item.parentId}'];
        //        console.log(cities);
        if (typeof cities === "undefined"){
            provinceMap['${item.parentId}'] = []
            provinceMap['${item.parentId}'].push({'id':'${item.id}','name':'${item.name}'})
        } else{
            cities.push({'id':'${item.id}','name':'${item.name}'})
        }
        </c:forEach>

        function change_city(obj)
        {
            removeAllItem($O('cityId'));
            setOption($O('cityId'), "", "--");
            if ($$('provinceId') == "")
            {
                return;
            }
            var cityList = provinceMap[$$('provinceId')];
//        console.log(cityList);
            for (var i = 0; i < cityList.length; i++)
            {
                setOption($O('cityId'), cityList[i].id, cityList[i].name);
            }
        }
        function radio_click(obj)
        {
            if (obj.value == '2')
            {
                $O('transport1').disabled = false;
                removeAllItem($O('transport1'));
                setOption($O('transport1'), "", "--");
                <c:forEach items="${expressList}" var="item">
                if ("${item.type}" == 0 || "${item.type}" == 99)
                {
                    setOption($O('transport1'), "${item.id}", "${item.name}");
                }
                </c:forEach>
                removeAllItem($O('transport2'));
                setOption($O('transport2'), "", "--");
                $O('transport2').disabled = true;
                var expressPay = $O('expressPay');
                var transportPay = $O('transportPay');
                removeAllItem(expressPay);
                setOption(expressPay, '1', '业务员支付');
                setOption(expressPay, '2', '公司支付');
                setOption(expressPay, '3', '客户支付');
                removeAllItem(transportPay);
                setOption(transportPay, '', '--');
            }
            else if (obj.value == '3')
            {
                $O('transport2').disabled = false;
                removeAllItem($O('transport2'));
                setOption($O('transport2'), "", "--");
                <c:forEach items="${expressList}" var="item">
                if ("${item.type}" == 1 || "${item.type}" == 99)
                {
                    setOption($O('transport2'), "${item.id}", "${item.name}");
                }
                </c:forEach>
                removeAllItem($O('transport1'));
                setOption($O('transport1'), "", "--");
                $O('transport1').disabled = true;
                var expressPay = $O('expressPay');
                var transportPay = $O('transportPay');
                removeAllItem(expressPay);
                setOption(expressPay, '', '--');
                removeAllItem(transportPay);
                setOption(transportPay, '1', '业务员支付');
                setOption(transportPay, '2', '公司支付');
                setOption(transportPay, '3', '客户支付');
            }
            else if (obj.value == '4')
            {
                $O('transport1').disabled = false;
                $O('transport2').disabled = false;
                removeAllItem($O('transport1'));
                setOption($O('transport1'), "", "--");
                removeAllItem($O('transport2'));
                setOption($O('transport2'), "", "--");
                <c:forEach items="${expressList}" var="item">
                if ("${item.type}" == 0 || "${item.type}" == 99)
                {
                    setOption($O('transport1'), "${item.id}", "${item.name}");
                }
                </c:forEach>
                <c:forEach items="${expressList}" var="item">
                if ("${item.type}" == 1 || "${item.type}" == 99)
                {
                    setOption($O('transport2'), "${item.id}", "${item.name}");
                }
                </c:forEach>
                var expressPay = $O('expressPay');
                var transportPay = $O('transportPay');
                removeAllItem(expressPay);
                setOption(expressPay, '1', '业务员支付');
                setOption(expressPay, '2', '公司支付');
                setOption(expressPay, '3', '客户支付');
                removeAllItem(transportPay);
                setOption(transportPay, '1', '业务员支付');
                setOption(transportPay, '2', '公司支付');
                setOption(transportPay, '3', '客户支付');
            }
            else
            {
                $O('transport1').disabled = true;
                $O('transport2').disabled = true;
                removeAllItem($O('transport1'));
                setOption($O('transport1'), "", "--");
                removeAllItem($O('transport2'));
                setOption($O('transport2'), "", "--");
                var expressPay = $O('expressPay');
                var transportPay = $O('transportPay');
                removeAllItem(expressPay);
                setOption(expressPay, '', '--');
                removeAllItem(transportPay);
                setOption(transportPay, '', '--');
            }
        }

        function radio2_click(obj)
        {
            //console.log(obj.value);
            if (obj.value == '1')
            {
                showTr('distribution1', true);
                showTr('distribution2', true);
                showTr('distribution3', true);
            }
            else if (obj.value == '0')
            {
                showTr('distribution1', false);
                showTr('distribution2', false);
                showTr('distribution3', false);
            }
        }

        function sub()
        {
            if (window.confirm('确定修改发货单?'))
            {
                shippingForm.submit();
            }
        }
    </script>
</head>
<body class="body_class">
<form name="shippingForm" method=post action="../sail/ship.do">
    <input type=hidden name="method" value="updateShipping" />
    <input type=hidden name="id" value="${id}" />

    <p:navigation
            height="22">
        <td width="550" class="navigation">商品拣配 &gt;&gt; 修改发货方式</td>
        <td width="85"></td>
    </p:navigation> <br>

    <table width="95%" border="0" cellpadding="0" cellspacing="0"
           align="center">
        <tr>
            <td colspan='2' align='center'>
                <table width="100%" border="0" cellpadding="0" cellspacing="0"
                       class="border">
                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing='1'>
                                <div>
                                    <tr class="content1" id="distribution1">
                                        <td align="right">发货方式：</td>
                                        <td colspan="3">
                                            <label><input type="radio" name="shipping" value="0" onClick="radio_click(this)">自提</label>
                                            <label><input type="radio" name="shipping" value="1" onClick="radio_click(this)">公司</label>
                                            <label><input type="radio" name="shipping" value="2" onClick="radio_click(this)">第三方快递</label>
                                            <label><input type="radio" name="shipping" value="3" onClick="radio_click(this)">第三方货运</label>
                                            <label><input type="radio" name="shipping" value="4" onClick="radio_click(this)">第三方快递+货运</label>
                                        </td>
                                    </tr>
                                    <tr class="content1" id="distribution2">
                                        <td align="right">运输方式：</td>
                                        <td colspan="3">
                                            <select name="transport1" id="transport1" quick=true class="select_class" style="width:20%" >
                                            </select>&nbsp;&nbsp;
                                            <select name="transport2" id="transport2" quick=true class="select_class" style="width:20%" >
                                            </select>
                                        </td>
                                    </tr>
                                    <tr class="content1" id="distribution3">
                                        <td align="right">运费支付方式：</td>
                                        <td colspan="3">
                                            <select name="expressPay" quick=true class="select_class" style="width:20%">
                                                <p:option type="deliverPay"></p:option>
                                            </select>&nbsp;&nbsp;
                                            <select name="transportPay" quick=true class="select_class" style="width:20%">
                                                <p:option type="deliverPay"></p:option>
                                            </select>
                                        </td>
                                    </tr>

                                </div>
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
            <td background="../images/dot_line.gif" colspan='2'></td>
        </tr>

        <tr>
            <td height="10" colspan='2'></td>
        </tr>

        <tr id = "button_tr">
            <td width="100%">
                <div align="right">
                    <input
                        type="button" class="button_class" id="sub_b"
                        value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onClick="sub()" /></div>
            </td>
            <td width="0%"></td>
        </tr>

    </table>
</form>
</body>
</html>

