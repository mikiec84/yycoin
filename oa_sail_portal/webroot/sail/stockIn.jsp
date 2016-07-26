<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="填写销售单(now)" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/math.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/compatible.js"></script>
<script language="JavaScript" src="../js/jquery/jquery.js"></script>
<script language="JavaScript" src="../js/json.js"></script>
<script language="JavaScript" src="../sail_js/accessoryInStorage.js"></script>
<script language="javascript">
    var productList = [];
    <c:forEach items="${bean.baseList}" var="item">
    productList.push("${item.productId}")
    </c:forEach>

    function load()
    {
        loadForm();
    }

    function confirmBack(){
        backForm.submit();
        <%--$l('../sail/out.do?method=processInvoke&outId=${bean.fullId}&flag=1&depotpartId=' + $$('depotpartId'));--%>
    }

    function rejects()
    {
        if (window.confirm('确定驳回此调出的库单?'))
        {
            document.location.href = '../sail/out.do?method=processInvoke&outId=${bean.fullId}&flag=3';
        }
    }

function changeLocation(obj){
    var value = obj.value;
    console.log(value);
//    console.log(obj.val())
//    var depotPartSelect = $(this).closest('tr').find('select[name="depotPart"]');
//    console.log(depotPartSelect);
//    var depotPartSelect = $(this).next('select');
//    console.log(depotPartSelect);

    var os = obj.parentNode.parentNode;
    console.log(os);
    console.log(os.cells);
    console.log(os.cells[0]);
    console.log(os.cells[0].childNodes);
    var depotPartSelect = os.cells[3].childNodes[1];
    console.log(depotPartSelect);
//    var selectedLocation = $("#budget option:selected");
//    console.log("selectedLocation**"+selectedLocation);
//    var locationId = selectedLocation.val();
//    console.log("***budget id***"+locationId);
    $ajax('../sail/out.do?method=queryDepotPart&locationId='+value,
            function(data){
            console.log(data);
            var dataList = data.obj;
            console.log(dataList);
            //TODO render select
            removeAllItem(depotPartSelect);
            setOption(depotPartSelect, "-", "");
            for (var j = 0; j < dataList.length; j++)
            {
                setOption(depotPartSelect, dataList[j].id, dataList[j].name);
            }

    });
}

</script>
</head>
<body class="body_class" onload="load()">
<form name="backForm" id="backForm" method="post" action="../sail/out.do?method=processInvoke" enctype="multipart/form-data">
<input type=hidden name="productList" />
<input type=hidden id="outId" name="outId" value="${bean.fullId}"/>
<input type=hidden name="method" value="addOut" />
<input type=hidden name="nameList" />
<input type=hidden name="idsList" />
<input
        type=hidden name="unitList" />
<input type=hidden name="amontList" />
<input type=hidden name="priceList" />
<input type=hidden
       name="totalList" />
<input type=hidden name="totalss" />
<input type=hidden name="customerId" value="${bean.customerId}"/>
<input type=hidden name="type"
       value='0' />
<input type=hidden name="saves" value="" />
<input type=hidden name="desList" value="" />
<input type=hidden name="otherList" value="" />
<input type=hidden name="showIdList" value="" />
<input type=hidden name="showNameList" value="" />
<input type=hidden name="customercreditlevel" value="" />
<p:navigation
	height="22">
	<td width="550" class="navigation">入库 &gt;&gt; ${stockInType}</td>
				<td width="85"></td>
</p:navigation> <br>

<table width="95%" border="0" cellpadding="0" cellspacing="0"	align="center">

	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
        <table width="100%" border="0" cellpadding="0" cellspacing="0"
               class="border">
            <tr>
                <td>
                    <table width="100%" border="0" cellspacing='1'>
                        <tr class="content2">
                            <td width="60%" align="center">商品名：</td>
                            <td width="40%" align="center">数量：</td>
                        </tr>

                        <c:forEach items="${bean.baseList}" var="item" varStatus="vs">
                            <tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
                                <td align="center">${item.productName}</td>
                                <td align="center">${item.amount}</td>
                            </tr>
                        </c:forEach>


                    </table>
                </td>
            </tr>
        </table>
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
				<table width="100%" border="0" cellspacing='1' id="tables">
					<tr align="center" class="content0">
						<td width="35%" align="center">商品名</td>
						<td width="15%" align="center">数量</td>
                        <td width="35%" align="center">入库仓库</td>
                        <td width="13%" align="center">仓区</td>
                        <td width="15%" align="center">
                            <input type="button" accesskey="A" value="增加" class="button_class" onclick="addTr()">
                        </td>
					</tr>

                    <!--用来作为新增行时copy只用 -->
					<tr class="content1" id="trCopy" style="display: none;">
                        <td>
                            <select name="productName" class="select_class product" style="width: 100%">
                                <option value="">--</option>
                                <c:forEach items='${bean.baseList}' var="item">
                                    <option value="${item.productId}">${item.productName}</option>
                                </c:forEach>
                            </select>
                        </td>

                        <td align="center">
                            <input type="text" style="width: 100%" maxlength="6" name="amount" required="required">
                        </td>

                        <td>
                            <select name="location" class="select_class location" style="width: 100%" onchange="changeLocation(this)">
                                <option value="">--</option>
                                <c:forEach items='${locationList}' var="item">
                                    <option value="${item.id}">${item.name}</option>
                                </c:forEach>
                            </select>
                        </td>

                        <td>
                            <select name="depotPart" class="select_class" style="width: 100%">
                                <option value="">--</option>
                                <c:forEach items='${depotartList}' var="depotPart">
                                    <option value="${depotPart.id}" selected>${depotPart.name}</option>
                                </c:forEach>
                            </select>
                        </td>


						<td align="center"></td>
					</tr>
                    <!-- end of trCopy -->

                    <c:forEach items="${bean.baseList}" var="item" varStatus="vs">
                        <tr class="content1">
                            <td>
                                <select name="productName" class="select_class product" style="width: 100%">
                                    <option value="">--</option>
                                    <c:forEach items='${baseBeans}' var="base">
                                        <c:if test="${base.productName == item.productName}">
                                            <option value="${base.productId}" selected>${base.productName}</option>
                                        </c:if>
                                        <c:if test="${base.productName != item.productName}">
                                            <option value="${base.productId}">${base.productName}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </td>

                            <td align="center">
                                <input type="text" style="width: 100%" maxlength="6" name="amount" value="${item.amount}" required="required">
                            </td>

                            <td>
                                <select name="location" class="select_class location" style="width: 100%" onchange="changeLocation(this)">
                                    <option value="">--</option>
                                    <c:forEach items='${locationList}' var="location">
                                        <c:if test="${location.id == destinationId}">
                                            <option value="${location.id}" selected>${location.name}</option>
                                        </c:if>
                                        <c:if test="${location.id != destinationId}">
                                            <option value="${location.id}">${location.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </td>

                            <td>
                                <select name="depotPart" class="select_class" style="width: 100%">
                                    <option value="">--</option>
                                    <c:forEach items='${depotartList}' var="depotPart">
                                        <option value="${depotPart.id}">${depotPart.name}</option>
                                    </c:forEach>
                                </select>
                            </td>

                            <td align="center">
                                <input type="button" value="删除" name="eachDel" class="button_class" onclick="removeTr(this)">
                            </td>


                            <td align="center"></td>
                        </tr>
                    </c:forEach>

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
		<td width="100%">
		<div align="right">
			<input type="button" class="button_class" value="确认退库" onClick="confirmBack()" />&nbsp;&nbsp;
            <input type="button" class="button_class" value="返回" onclick="javascript:history.go(-1)" >
        </div>
		</td>
		<td width="0%"></td>
	</tr>

</table>
</form>
</body>
</html>

