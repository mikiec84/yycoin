<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
    <p:link title="修改商品转换配置" />
    <script language="JavaScript" src="../js/JCheck.js"></script>
    <script language="JavaScript" src="../js/common.js"></script>
    <script language="JavaScript" src="../js/public.js"></script>
    <script language="JavaScript" src="../js/math.js"></script>
    <script language="javascript">
        function addBean()
        {
            submit('确定修改商品转换配置?', null, null);
        }

        function load()
        {
        }

    </script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../sail/productExchangeConfig.do" method="post">
    <input type="hidden" name="method" value="update">
    <input type="hidden" name="srcProductId" value="${bean.srcProductId}">
    <input type="hidden" name="destProductId" value="${bean.destProductId}">
    <input type="hidden" name="id" value="${bean.id}">

    <p:navigation
            height="22">
        <td width="550" class="navigation"><span style="cursor: pointer;"
                                                 onclick="javascript:history.go(-1)">商品转换配置管理</span> &gt;&gt; 修改商品转换配置</td>
        <td width="85"></td>

    </p:navigation> <br>

    <p:body width="98%">

        <p:title>
            <td class="caption"><strong>商品转换基本信息：</strong></td>
        </p:title>

        <p:line flag="0" />

        <p:subBody width="100%">
            <p:class value="com.china.center.oa.sail.bean.ProductExchangeConfigBean" opr="1"/>

            <p:table cells="1">
                <p:pro field="srcProductId" value="${bean.srcProductName}" innerString="size=60">
                </p:pro>

                <p:pro field="srcAmount" value="${bean.srcAmount}" innerString="size=60 oncheck='isMathNumber'" />

                <p:pro field="destProductId" value="${bean.destProductName}" innerString="size=60">
                </p:pro>

                <p:pro field="destAmount" value="${bean.destAmount}" innerString="size=60 oncheck='isMathNumber'"/>

            </p:table>
        </p:subBody>

        <p:line flag="1" />

        <p:button leftWidth="100%" rightWidth="0%">
            <div align="right"><input type="button" class="button_class" id="ok_b"
                                      style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
                                      onclick="addBean()"></div>
        </p:button>

        <p:message2/>
    </p:body></form>
</body>
</html>

