<%@page language="java" session="false" contentType="text/xml"%><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const,pcs.*"%><%
  SipRequest sipReq = (SipRequest)request;
  String cuiImage = sipReq.sipCapInterestUrl("/cui/image");
  String cuiMain = sipReq.sipCapInterestUrl("/cui/main");
  ResourceBundle rb = sipReq.getResourceBundle();
  String ok = rb.getString(Const.ResStrKey.BUTTON_OK);
  String info = rb.getString(Const.ResStrKey.MSG_INFO_DOWNLOADING);
  String pair = sipReq.getUniqueIdNVPair();
  String session_id=SipsUtils.escapeTemplatedURIComponent((String)sipReq.getAttribute("session_id"));
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="Hello" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <screen id="start">
    <link logical="ok" labelText="<%=ok%>" href="<%=cuiMain%>/screen/mainList/session_id/<%=session_id%>?<%=pair%>"/>
    <list view="horizFullScreenImgOnly">
      <item src="<%=cuiImage%>/sipcontent/project/thumbnail/HelloWorld.png"/>
    </list>
  </screen>
</cloudUiDescription>