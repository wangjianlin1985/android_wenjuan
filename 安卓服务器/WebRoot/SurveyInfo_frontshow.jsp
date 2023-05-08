<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.chengxusheji.domain.SurveyInfo" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    SurveyInfo surveyInfo = (SurveyInfo)request.getAttribute("surveyInfo");

%>
<HTML><HEAD><TITLE>查看问卷信息</TITLE>
<STYLE type=text/css>
body{margin:0px; font-size:12px; background-image:url(<%=basePath%>images/bg.jpg); background-position:bottom; background-repeat:repeat-x; background-color:#A2D5F0;}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
</HEAD>
<BODY><br/><br/>
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3'  class="tablewidth">
  <tr>
    <td width=30%>记录编号:</td>
    <td width=70%><%=surveyInfo.getPaperId() %></td>
  </tr>

  <tr>
    <td width=30%>问卷名称:</td>
    <td width=70%><%=surveyInfo.getQuestionPaperName() %></td>
  </tr>

  <tr>
    <td width=30%>发起人:</td>
    <td width=70%><%=surveyInfo.getFaqiren() %></td>
  </tr>

  <tr>
    <td width=30%>问卷描述:</td>
    <td width=70%><%=surveyInfo.getDescription() %></td>
  </tr>

  <tr>
    <td width=30%>发起日期:</td>
        <% java.text.DateFormat startDateSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><%=startDateSDF.format(surveyInfo.getStartDate()) %></td>
  </tr>

  <tr>
    <td width=30%>结束日期:</td>
        <% java.text.DateFormat endDateSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");  %>
    <td width=70%><%=endDateSDF.format(surveyInfo.getEndDate()) %></td>
  </tr>

  <tr>
    <td width=30%>主题图片:</td>
    <td width=70%><img src="<%=basePath %><%=surveyInfo.getZhutitupian() %>" width="200px" border="0px"/></td>
  </tr>
  <tr>
    <td width=30%>审核标志:</td>
    <td width=70%><%=surveyInfo.getPublishFlag() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="返回" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
