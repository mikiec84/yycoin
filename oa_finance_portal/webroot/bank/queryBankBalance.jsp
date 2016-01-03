<%@ page contentType="text/html;charset=UTF-8" language="java"
         errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
    <p:link title="银行余额列表" />
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
      function query()
        {
           formEntry.submit();
        }

      function load()
      {
          loadForm();
      }

      function exportBankBalance(){
          if (window.confirm("确定导出当前查询结果?")){
              var bank = $('#bank').val();
              var beginDate = $('#beginDate').val();
              var endDate = $('#endDate').val();
              document.location.href = '../finance/bank.do?method=export&bank='+bank+"&beginDate="+
                beginDate+"&endDate="+endDate;
          }
      }
    </script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../finance/bank.do" method="post">
    <input type="hidden" name="method" value="queryBankBalance">
    <input type="hidden" value="1" name="firstLoad">
    <p:navigation height="22">
        <td width="550" class="navigation">资金管理 &gt;&gt; 银行余额列表</td>
        <td width="85"></td>
    </p:navigation> <br>

    <p:body width="100%">

        <p:subBody width="98%">
            <table width="100%" align="center" cellspacing='1' class="table0">
                <tr class="content1">
                    <td width="15%" align="center">开始时间</td>
                    <td align="center" width="35%"><p:plugin name="beginDate" size="20" value="${ppmap.beginDate}"/></td>
                    <td width="15%" align="center">结束时间</td>
                    <td align="center"><p:plugin name="endDate" size="20" value="${ppmap.endDate}"/>
                    </td>
                </tr>

                <tr class="content2">
                    <td width="15%" align="center">银行</td>
                    <td align="left">
                        <input type="text" id="bank" name="bank" maxlength="40" size="30" value="${ppmap.bank}">
                    </td>
                    <td width="15%" align="center"></td>
                    <td align="left">
                    </td>
                </tr>

                <tr class="content1">
                    <td colspan="4" align="right">
                        <input type="button"  class="button_class" onclick="query()" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;
                        <input type="reset" class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;">
                    </td>
                </tr>
            </table>

        </p:subBody>

        <p:title>
            <td class="caption"><strong>银行余额列表</strong></td>
        </p:title>

        <p:line flag="0" />

        <p:subBody width="98%">
            <table width="100%" align="center" cellspacing='1' class="table0">
                <tr align=center class="content0">
                    <td align="center" class="td_class" ><strong>银行</strong></td>
                    <td align="center" class="td_class" ><strong>余额</strong></td>
                    <td align="center" class="td_class" ><strong>日期</strong></td>
                </tr>

                <c:forEach items="${list}" var="item" varStatus="vs">
                    <tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
                        <td align="center" class="td_class" ><strong>${item.bankName}</strong></td>
                        <td align="center" class="td_class" ><strong>${item.balance}</strong></td>
                        <td align="center" class="td_class" ><strong>${item.statDate}</strong></td>
                    </tr>
                </c:forEach>

            </table>

            <p:formTurning form="formEntry" method="queryBankBalance"></p:formTurning>

        </p:subBody>

        <p:line flag="1" />

        <p:button leftWidth="98%" rightWidth="2%">

            <div align="right">
                <input type="button"
                       class="button_class" onclick="exportBankBalance()"
                       value="&nbsp;&nbsp;导出查询结果&nbsp;&nbsp;">&nbsp;&nbsp;
            </div>
        </p:button>

        <p:message2 />

    </p:body></form>

</body>
</html>

