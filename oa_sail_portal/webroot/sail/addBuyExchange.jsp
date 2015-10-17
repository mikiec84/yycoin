<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="填写入库-商品调换单" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/math.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/compatible.js"></script>
<script language="JavaScript" src="../js/json.js"></script>
<script language="JavaScript" src="../sail_js/addBuy.js"></script>
<script language="javascript">
<%@include file="../sail_js/exchange_stock_in.jsp"%>
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
        showTr('distribution4', true);
        showTr('distribution5', true);
    }
    else if (obj.value == '0')
    {
        showTr('distribution1', false);
        showTr('distribution2', false);
        showTr('distribution3', false);
        showTr('distribution4', false);
        showTr('distribution5', false);
    }
}

/**
 * 选择产品
 */
function selectProduct(obj)
{
	var ret = checkCurrentUser();

	if (!ret)
	{
		window.parent.location.reload();

		return false
	}
	
    oo = obj;

//    var mtype = duesTypeMap[$$('dutyId')];
    //alert('========'+ g_url_query);
    window.common.modal(RPT_PRODUCT + '&ctype=1');
//    if (g_url_query == 0)
//    window.common.modal('../depot/storage.do?method=rptQueryStorageRelationInDepot&costMode=1&queryType=1&showAbs=1&load=1&depotId='+ $$('location') + '&code=' + obj.productcode + '&mtype=' + mtype);
//    else
//    window.common.modal('../product/product.do?method=rptQueryProduct&load=1&selectMode=1&abstractType=0&status=0' + '&mtype=' + mtype);
}

</script>
</head>
<body class="body_class" onload="load()">
<form name="outForm" method=post action="../sail/out.do">
	<input type=hidden name="method" value="addBuyExchange" />
	<input type=hidden name="customerId" />
    <input type="hidden" name="reserve9" value=""/>
    <input type=hidden name="type" value='1' />
    <input type=hidden name="outType" value='8' />
    <input type=hidden name="saves" value="" />
    <input type=hidden name="id" value="" />
    <input type=hidden name="oprType" value="0">
<p:navigation
	height="22">
	<td width="550" class="navigation">库单管理 &gt;&gt; 填写入库单-商品调换</td>
				<td width="85"></td>
</p:navigation> <br>

<table width="95%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td valign="top" colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="784" height="6"></td>
			</tr>
			<tr>
				<td align="center" valign="top">
				<div align="left">
				<table width="90%" border="0" cellspacing="2">
					<tr>
						<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="10">
							<tr>
								<td width="35">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption">
                                    <strong>填写入库单信息:</strong>
								</td>
							</tr>
						</table>
						</td>
					</tr>


				</table>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>


	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td colspan='2' align='center'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td align="right">品名：</td>
						<td colspan="3">
                            <input type="text" name="productName"
                                   onclick="selectProduct(this)"
                                   productid=""
                                   productcode=""
                                   price=""
                                   stafferid=""
                                   depotpartid=""
                                   readonly="readonly"
                                   style="width: 100%; cursor: hand">
                            <font color="#FF0000">*</font></td>
					</tr>

                    <tr class="content1" id="dir_tr">
                        <td align="right">数量：</td>
                        <td colspan="3">
                            <input type="number">
                            <font color="#FF0000">*</font>
                        </td>
                    </tr>

                    <tr class="content1" id = "staffer_tr">
                        <td align="right" id="outd">换货人：</td>
                        <td colspan="3">
                            <input type="text" name="stafferName1" maxlength="14" value=""  size="20"
                                   onclick="selectStaffer()" style="cursor: pointer;"
                                   readonly="readonly">
                            <font color="#FF0000">*</font>
                        </td>
                    </tr>

                    <tr class="content2" id="dir_tr">
                        <td align="right">发货仓库：</td>
                        <td colspan="3">
                            <select name="destinationId" class="select_class" values="${destinationId}" oncheck="notNone;">
                                <c:forEach items='${dirLocationList}' var="item">
                                    <option value="${item.id}">${item.name}</option>
                                </c:forEach>
                            </select>
                            <font color="#FF0000">*</font></td>
                    </tr>


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
						<tr class="content1" id="distribution4">
							<td width="15%" align="right">送货地址：</td>
							<td width="35%">
								<select name="provinceId" quick=true onchange="change_city(this)" class="select_class" >
									<option>-</option>
									<c:forEach items="${provinceList}" var="province">
										<option value="${province.id}">${province.name}</option>
									</c:forEach>
									</select>&nbsp;&nbsp;
								<select name="cityId" quick=true class="select_class" >
									<option>-</option>
								</select>&nbsp;&nbsp;
							</td>
                            <td width="15%" align="right">收货人：</td>
                            <td width="35%">
                                <input type="text" name='receiver' id ='receiver' maxlength="10" required="required" /><font color="#FF0000">*</font>
                            </td>
						</tr>
						<tr class="content2" id="distribution5">
							<td width="15%" align="right">地址：</td>

							<td width="35%">
								<input type="text" name="address" id="address" maxlength="60" required="required" /><font color="#FF0000">*</font>
							</td>

							<td width="15%" align="right">电话：</td>
							<td width="35%">
                                <input type="text" name="mobile" id ="mobile" maxlength="13" required="required"/><font color="#FF0000">*</font>
							</td>
						</tr>
					</div>


					<tr class="content2">
						<td align="right">入库单备注：</td>
						<td colspan="3">
                            <textarea rows="3" cols="55" oncheck="notNone;" name="description"></textarea>
							<font color="#FF0000">*</font>
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
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
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
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;保 存&nbsp;&nbsp;" onClick="save()" />&nbsp;&nbsp;<input
			type="button" class="button_class" id="sub_b"
			value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onClick="sub()" /></div>
		</td>
		<td width="0%"></td>
	</tr>

</table>
</form>
</body>
</html>

