<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String username=(String)session.getAttribute("username");
    if(username==null){
        response.getWriter().println("<script>top.location.href='" + basePath + "login/login_view.action';</script>");
    }
%>
<HTML><HEAD><TITLE>添加问卷信息</TITLE> 
<STYLE type=text/css>
BODY {
    	MARGIN-LEFT: 0px; BACKGROUND-COLOR: #ffffff
}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
<script language="javascript">
/*验证表单*/
function checkForm() {
    var questionPaperName = document.getElementById("surveyInfo.questionPaperName").value;
    if(questionPaperName=="") {
        alert('请输入问卷名称!');
        return false;
    }
    return true; 
}
 </script>
</HEAD>

<BODY background="<%=basePath %>images/adminBg.jpg">
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top >
    <s:form action="SurveyInfo/SurveyInfo_AddSurveyInfo.action" method="post" id="surveyInfoAddForm" onsubmit="return checkForm();"  enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3' class="tablewidth">

  <tr>
    <td width=30%>问卷名称:</td>
    <td width=70%><input id="surveyInfo.questionPaperName" name="surveyInfo.questionPaperName" type="text" size="30" /></td>
  </tr>

  <tr>
    <td width=30%>发起人:</td>
    <td width=70%><input id="surveyInfo.faqiren" name="surveyInfo.faqiren" type="text" size="20" /></td>
  </tr>

  <tr>
    <td width=30%>问卷描述:</td>
    <td width=70%><textarea id="surveyInfo.description" name="surveyInfo.description" rows="5" cols="50"></textarea></td>
  </tr>

  <tr>
    <td width=30%>发起日期:</td>
    <td width=70%><input type="text" readonly id="surveyInfo.startDate"  name="surveyInfo.startDate" onclick="setDay(this);"/></td>
  </tr>

  <tr>
    <td width=30%>结束日期:</td>
    <td width=70%><input type="text" readonly id="surveyInfo.endDate"  name="surveyInfo.endDate" onclick="setDay(this);"/></td>
  </tr>

  <tr>
    <td width=30%>主题图片:</td>
    <td width=70%><input id="zhutitupianFile" name="zhutitupianFile" type="file" size="50" /></td>
  </tr>

  <tr>
    <td width=30%>审核标志:</td>
    <td width=70%><input id="surveyInfo.publishFlag" name="surveyInfo.publishFlag" type="text" size="8" /></td>
  </tr>

  <tr bgcolor='#FFFFFF'>
      <td colspan="4" align="center">
        <input type='submit' name='button' value='保存' >
        &nbsp;&nbsp;
        <input type="reset" value='重写' />
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
