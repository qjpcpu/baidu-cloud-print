<%@page language="java" session="false" contentType="text/xml"%><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const,crazy.BaiduFile,java.util.*,crazy.CrazyAppHelper,pcs.*,crazy.CrazyConst"%><%
  SipRequest sipReq = (SipRequest)request;
  String cuiImage=sipReq.sipCapInterestUrl("/cui/image");
  String cuiMain = sipReq.sipCapInterestUrl("/cui/main");
  ResourceBundle rb = sipReq.getResourceBundle();
  String ok = rb.getString(Const.ResStrKey.BUTTON_OK);
  String pair = sipReq.getUniqueIdNVPair();
  String token=CrazyAppHelper.getAccessToken(sipReq);
  String session_id=(String)sipReq.getAttribute("session_id");
  session_id=SipsUtils.escapeTemplatedURIComponent(session_id);
  String current_directory=(String)sipReq.getAttribute(CrazyConst.CURRENT_DIR);
  current_directory=XmlUtils.escapeURI(current_directory);
  List<BaiduFile> files=(List<BaiduFile>)sipReq.getAttribute(CrazyConst.FILES_IN_CURRENT_DIR);
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="Hello1" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <parameter name="path" type="single"/>
  <screen id="start" showBusy="true">
  	<static location="heading" labelText="<%=current_directory%>"/>
    <link logical="ok" labelText="<%=ok%>" href="<%=cuiMain%>/screen/all_document/path/{path}/access_token/<%=token%>/session_id/<%=session_id%>?<%=pair%>" saveSelection="true"/>
    <list view="vert1ColImgTxt" kind="single" param="path">
      <%
      for(int i=0;i<files.size();i++){
    	  BaiduFile f=files.get(i);
    	  String thumbnail=f.isDirectory()?"folder.png":"doc.png";
    	  String escaped_path=SipsUtils.escapeTemplatedURIComponent(f.getFilePath()); %>
    	  <item value="<%=escaped_path%>" src="<%=cuiImage%>/sipcontent/project/thumbnail/<%=thumbnail%>" labelText="<%=f.getFileName()%>" labelAlign="left" labelPos="right"/>
    <% 
      }
      %>
    </list>
  </screen>
</cloudUiDescription>