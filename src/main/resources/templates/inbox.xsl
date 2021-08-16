<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output
		encoding="UTF-8"
		media-type="text/html"
		method="html"
		indent="no"
		omit-xml-declaration="yes"
	/>

	<xsl:include href="default.xsl"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&#60;!DOCTYPE HTML&#62;</xsl:text>
		<HTML dir="ltr" lang="zh-Hant">
			<xsl:apply-templates select="document"/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<xsl:call-template name="headMetaTags"/>
			<TITLE>
				<xsl:value-of select="@title"/>
			</TITLE>
			<xsl:call-template name="headLinkTags"/>
			<LINK href="/STYLE/chatroom.css" rel="stylesheet"/>
			<LINK href="/STYLE/rateStar.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-8 text-center">
				<DIV class="mx-sm-5 mx-lg-12 mb-3">
					<H4 class="text-primary">
						<xsl:value-of select="@title"/>
					</H4>
					<HR class="horizontal dark"/>
				</DIV>
				<DIV class="row justify-content-center mx-0">
					<DIV class="col-12 col-md-8 my-2 position-relative">
						<A class="inboxLink" href="/chatroom/{@identifier}/"></A>
						<DIV class="d-flex justify-content-between align-items-center p-2">
							<DIV>
								<IMG src="https://d35hi420xc5ji7.cloudfront.net/profileImage/31835bd9-ab5e-42fc-8616-af3e6f66fae3" alt="大頭貼" class="rounded-circle" width="70px"/>
							</DIV>
							<DIV class="me-auto" style="overflow: hidden;">
								<DIV class="d-flex flex-column align-items-start">
									<A class=" font-weight-bold text-dark mb-1">吳俊賢</A>
									<P class="text-muted mb-0">
										<SPAN>HIHI</SPAN>
									</P>
								</DIV>
							</DIV>
							<DIV class="col-2 pe-1">
								<DIV class="d-flex flex-column align-items-end">
									<SPAN class="text-muted">3天前</SPAN>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>