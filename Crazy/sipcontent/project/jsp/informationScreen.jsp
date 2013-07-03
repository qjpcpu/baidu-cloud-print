<%@page language="java" session="false" contentType="text/xml"%><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const"%><%
  String heading = (String)request.getAttribute("error");;
  String info = (String)request.getAttribute("error_description");
  SipRequest sipReq = (SipRequest)request;
  ResourceBundle rb = sipReq.getResourceBundle();
  String back = rb.getString(Const.ResStrKey.BUTTON_BACK);
  
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="<%=sipReq.getUniqueCuiName()%>" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <screen id="start">
    <static location="heading" labelText="<%=heading%>"/>
    <static location="info" labelText="<%=info%>"/>
    <link labelText="<%=back%>" logical="ok" href="control:prev(1)"/>
  </screen>
</cloudUiDescription>
