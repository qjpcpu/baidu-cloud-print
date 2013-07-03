<%@ page language="java" session="false" contentType="text/xml" %><%@page import="com.hp.sbs.container.sip.SipRequest"%><%@page import="java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const"%><%
  SipRequest sipReq = (SipRequest)request;
  ResourceBundle rb = sipReq.getResourceBundle();
  String info = rb.getString(Const.ResStrKey.MSG_INFO_TIMEOUT);
  String back = rb.getString(Const.ResStrKey.BUTTON_BACK);
  String pageOnTimeout = sipReq.getTemplParameter("_backT");
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="Timeout<%=pageOnTimeout%>" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <screen id="start">
    <static location="info" labelText="<%=info%>"/>
    <link logical="ok" href="control:prev(<%=pageOnTimeout%>)" labelText="<%=back%>"/>
  </screen>
</cloudUiDescription>