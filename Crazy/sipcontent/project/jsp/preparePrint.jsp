<%@page language="java" session="false" contentType="text/xml"%><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const,crazy.BaiduFile,java.util.*,crazy.CrazyAppHelper,pcs.*,crazy.CrazyConst"%><%
  SipRequest sipReq = (SipRequest)request;
  String cuiPrint = sipReq.sipCapInterestUrl("/cui/print");
  ResourceBundle rb = sipReq.getResourceBundle();
  String ok = rb.getString(Const.ResStrKey.BUTTON_PRINT);
  String pair = sipReq.getUniqueIdNVPair();
  String preview=rb.getString(CrazyConst.FILE_PRINT_PREVIEW);
  
  String token=CrazyAppHelper.getAccessToken(sipReq);
  String thumbnail=(String)sipReq.getAttribute("thumbnail");
  thumbnail=XmlUtils.escapeURI(thumbnail);
  String path=(String)sipReq.getAttribute("path");
  String templatepath=SipsUtils.escapeTemplatedURIComponent(path);
  String labelpath=path;
  int i=labelpath.lastIndexOf("/");
  if(i>=0&&(i+1)<labelpath.length())
	  labelpath=labelpath.substring(i+1);
  if((i=labelpath.indexOf("/"))>=0)
	  labelpath=XmlUtils.escapeURI(labelpath);
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="Hello2" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  
  <screen id="start" showBusy="true">
  	<static location="heading" labelText="<%=preview%>"/>
    <link logical="ok" labelText="<%=ok%>" href="<%=cuiPrint%>/screen/prepare_print/path/<%=templatepath%>/access_token/<%=token%>/_back/1?<%=pair%>" saveSelection="true"/>
    <list view="horiz1UpImgTxt" wrap="false">
      <item src="<%=thumbnail%>" labelText="<%=labelpath%>"/>
    </list>
  </screen>
</cloudUiDescription>