<%@page language="java" session="false" contentType="text/xml"%><?xml version="1.0" encoding="UTF-8"?>
<cloudUiDescription name="Unexpected" defaultScreen="start" xmlns="http://www.hp.com/schemas/imaging/cnx/sip/cloudui/2009/10/31">
  <screen id="start">
    <static location="heading" labelText="Unexpected Error."/>
    <static location="info" labelText="SBS is not configured for SipRequest."/>
    <link labelText="Back" logical="ok" href="control:prev(1)"/>
  </screen>
</cloudUiDescription>
