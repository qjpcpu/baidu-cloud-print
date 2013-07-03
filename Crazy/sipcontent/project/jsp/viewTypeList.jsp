<%@page language="java" session="false" contentType="text/xml"%><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const,crazy.CrazyAppHelper,pcs.*,crazy.CrazyConst"%><%
  SipRequest sipReq = (SipRequest)request;
  String cuiImage=sipReq.sipCapInterestUrl("/cui/image");
  String cuiMain = sipReq.sipCapInterestUrl("/cui/main");
  ResourceBundle rb = sipReq.getResourceBundle();
  String ok = rb.getString(Const.ResStrKey.BUTTON_OK);
  String allFilesLabel=rb.getString(CrazyConst.MAIN_LIST_ALL_FILE);
  String allImagesLabel=rb.getString(CrazyConst.MAIN_LIST_ALL_IMAGE);
  String allDocLabel=rb.getString(CrazyConst.MAIN_LIST_ALL_DOC);
  String pair = sipReq.getUniqueIdNVPair();
  String token=CrazyAppHelper.getAccessToken(sipReq);
  String sessionId=SipsUtils.escapeTemplatedURIComponent((String)sipReq.getAttribute("session_id"));
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="Hello" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <parameter name="screen" type="single"/>
  <screen id="start">
    <link logical="ok" labelText="<%=ok%>" href="<%=cuiMain%>/screen/{screen}/access_token/<%=token%>/session_id/<%=sessionId%>?<%=pair%>" saveSelection="true"/>
    <list view="vert1ColImgTxt" kind="single" param="screen">
      <item value="all_document" src="<%=cuiImage%>/sipcontent/project/thumbnail/all.png" labelText="<%=allFilesLabel%>" labelAlign="left" labelPos="right"/>
      <item value="picture_document" src="<%=cuiImage%>/sipcontent/project/thumbnail/picture.png" labelText="<%=allImagesLabel%>" labelAlign="left" labelPos="right"/>
      <item value="plain_document" src="<%=cuiImage%>/sipcontent/project/thumbnail/doc.png" labelText="<%=allDocLabel%>" labelAlign="left" labelPos="right"/>
    </list>
  </screen>
</cloudUiDescription>