<%@page language="java" session="false" contentType="text/xml"%><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const,crazy.CrazyConst,pcs.*"%><%
  SipRequest sipReq = (SipRequest)request;
  String cuiMain = sipReq.sipCapInterestUrl("/cui/main");
  ResourceBundle rb = sipReq.getResourceBundle();
  String ok = rb.getString(Const.ResStrKey.BUTTON_OK);
  String info = rb.getString(Const.ResStrKey.MSG_INFO_DOWNLOADING);
  String pair = sipReq.getUniqueIdNVPair();
  String session_id = (String)sipReq.getAttribute("session_id");
  session_id=SipsUtils.escapeTemplatedURIComponent(session_id);
  String user_code = (String)sipReq.getAttribute("user_code");
  String verification_url = (String)sipReq.getAttribute("verification_url");
  String label_user_code=rb.getString(CrazyConst.USER_CODE_LABEL);
  String label_ver_url=rb.getString(CrazyConst.VER_URL_LABEL);
  String content=label_user_code+user_code+"\r\n "+label_ver_url+verification_url;
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="HelloRegister" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <screen id="start">
    <link logical="ok" labelText="<%=ok%>" href="<%=cuiMain%>/screen/accessTokenscreen/session_id/<%=session_id%>?<%=pair%>"/>
    <static location="info" labelText="<%=content%>" labelAlign="center" labelPos="left"/>
  </screen>
</cloudUiDescription>