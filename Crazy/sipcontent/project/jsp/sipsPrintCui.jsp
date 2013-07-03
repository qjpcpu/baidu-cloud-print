<%@ page language="java" session="false" contentType="text/xml" %><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const"%><%
  SipRequest sipReq = (SipRequest)request;
  ResourceBundle rb = sipReq.getResourceBundle();
  String info = rb.getString(Const.ResStrKey.MSG_INFO_DOWNLOADING);
  String cancel = rb.getString(Const.ResStrKey.BUTTON_CACNEL);
  String jobId = (String)sipReq.getAttribute("jobId");
  String uniqueIdNVPair = sipReq.getUniqueIdNVPair();
  String pageBack = sipReq.getTemplParameter("_back");
  if (pageBack == null) {
      pageBack = "1";
  }
  String pageOnPrint = sipReq.getTemplParameter("_backP");
  if (pageOnPrint == null) {
      pageOnPrint = pageBack;
  }
  String pageOnCancel = sipReq.getTemplParameter("_backC");  
  if (pageOnCancel == null) {
      pageOnCancel = pageBack;
  }
  String pageOnTimeout = sipReq.getTemplParameter("_backT");  
  if (pageOnTimeout == null) {
      pageOnTimeout = pageBack;
  }
  String hrefCancel = sipReq.sipCapInterestUrl("/cui/cancel") + "/_backC/" + pageOnCancel + "?jobid=" + jobId + "&amp;" + uniqueIdNVPair;
  String hrefTimeOut = sipReq.sipCapInterestUrl("/cui/timeout") + "/_backT/" + pageOnTimeout +  "?jobid=" + jobId + "&amp;" + uniqueIdNVPair;
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="<%=jobId%>" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <screen id="start" showBusy="true">
    <static location="info" labelText="<%=info%>"/>
    <link logical="cancel" href="<%=hrefCancel%>" labelText="<%=cancel%>"/>
    <autoLink jobID="<%=jobId%>" href="control:prev(<%=pageOnPrint%>)"/>
    <autoLink timeout="115000" href="<%=hrefTimeOut%>"/>
  </screen>
</cloudUiDescription>