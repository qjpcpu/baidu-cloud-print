<%@page language="java" session="false" contentType="text/xml"%><%@page import="com.hp.sbs.container.sip.SipRequest,java.util.ResourceBundle,com.hp.sbs.commons.sbsconfig.Const,crazy.BaiduFile,java.util.*,crazy.CrazyAppHelper,pcs.*,crazy.CrazyConst"%><%
  SipRequest sipReq = (SipRequest)request;
  String cuiImage=sipReq.sipCapInterestUrl("/cui/image");
  String cuiMain=sipReq.sipCapInterestUrl("/cui/main");
  String cuiPrint = sipReq.sipCapInterestUrl("/cui/print");
  ResourceBundle rb = sipReq.getResourceBundle();
  String ok = rb.getString(Const.ResStrKey.BUTTON_OK);
  String nextPage=rb.getString(CrazyConst.ROWS_LIST_NEXT_PAGE);
  String mainMenu=rb.getString(CrazyConst.ROWS_LIST_MAIN_MENU);
  String pair = sipReq.getUniqueIdNVPair();
  String token=CrazyAppHelper.getAccessToken(sipReq);
  String session_id=(String)sipReq.getAttribute("session_id");
  session_id=SipsUtils.escapeTemplatedURIComponent(session_id);
  List<BaiduFile> files=(List<BaiduFile>)sipReq.getAttribute(CrazyConst.FILES_IN_CURRENT_DIR);
  String sPageStart=(String)sipReq.getAttribute(CrazyConst.STREAM_FILE_START_INDEX);
  int pageStart=Integer.parseInt(sPageStart);
  int pageSize=CrazyConst.STREAM_FILE_PAGE_SIZE;
%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="Hello1" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <parameter name="path" type="single"/>
  <screen id="start" showBusy="true">
    <link logical="ok" labelText="<%=ok%>" href="<%=cuiPrint%>/screen/picture_document/path/{path}/access_token/<%=token%>/session_id/<%=session_id%>?<%=pair%>" saveSelection="true"/>
    <%-- the saveSelection must be true, otherwise the altOk can't be executed --%>
    <link logical="altOk" labelText="<%=mainMenu%>" href="<%=cuiMain%>/screen/mainList/access_token/<%=token%>/session_id/<%=session_id%>?<%=pair%>" saveSelection="false"/>
    <list view="horizMultiRowImgTxt" kind="single" param="path">
      <%
      for(int i=0;i<files.size();i++){
    	  BaiduFile f=files.get(i);
    	  String real_path=f.getFilePath();
    	  String escaped_path=SipsUtils.escapeTemplatedURIComponent(real_path); %>
<%--     	  <item value="<%=escaped_path%>" src="<%=cuiImage%>/sipcontent/project/thumbnail/picture.png" labelText="<%=f.getFileName()%>"/> --%>
    	  <item value="<%=escaped_path%>" src="https://pcs.baidu.com/rest/2.0/pcs/thumbnail?method=generate&amp;access_token=<%=token%>&amp;path=<%=real_path%>&amp;quality=100&amp;width=300&amp;height=300" labelText="<%=f.getFileName()%>"/>
    <% 
      }
      %>
    </list>
    <%
    if(files.size()==pageSize){
    %>
    	<link logical="context" labelText="<%=nextPage%>" href="<%=cuiMain%>/screen/picture_document/direction/next/start/<%=sPageStart%>/access_token/<%=token%>/session_id/<%=session_id%>?<%=pair%>" />
    <%
    }
    %>
  </screen>
</cloudUiDescription>