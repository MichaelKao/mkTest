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
			<LINK href="/STYLE/inbox.css" rel="stylesheet"/>
			<LINK href="/STYLE/loading.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container col-sm-10 col-md-6 col-lg-5 col-xl-4 mx-auto px-1 px-sm-2 py-6 py-md-7 pb-2">
				<H3 class="text-dark ms-3">聊天</H3>
				<DIV class="justify-content-center mx-0" id="chatList">
					<INPUT name="nextPage" type="hidden" value="1"/>
					<INPUT name="gender" type="hidden" value="">
						<xsl:if test="@male">
							<xsl:attribute name="value">true</xsl:attribute>
						</xsl:if>
						<xsl:if test="@female">
							<xsl:attribute name="value">false</xsl:attribute>
						</xsl:if>
					</INPUT>
					<xsl:for-each select="conversation">
						<DIV class="conversationWrap position-relative">
							<A class="inboxLink" href="/chatroom/{@identifier}/"></A>
							<DIV class="d-flex justify-content-between align-items-center p-2">
								<DIV class="position-relative">
									<xsl:if test="@isMatchedOrIsVip = 'true'">
										<xsl:if test="/document/@female">
											<I class="fad fa-crown text-yellow text-shadow position-absolute" style="right: -5px;bottom: 1px;"></I>
										</xsl:if>
										<xsl:if test="/document/@male">
											<I class="fad fa-users text-primary text-shadow position-absolute" style="right: -5px;bottom: 1px;"></I>
										</xsl:if>
									</xsl:if>
									<IMG alt="大頭照" src="{@profileImage}" class="rounded-circle shadow" width="60px"/>
								</DIV>
								<DIV class="me-auto" style="overflow: hidden;">
									<DIV class="d-flex flex-column align-items-start ms-3">
										<A class="font-weight-bold text-dark text-sm mb-1">
											<xsl:value-of select="@nickname"/>
										</A>
										<P class="text-sm mb-0 content">
											<xsl:value-of select="@content"/>
										</P>
									</DIV>
								</DIV>
								<DIV class="col-3 d-flex">
									<DIV class="ms-auto d-flex flex-column">
										<SPAN class="text-xs mb-1">
											<xsl:value-of select="@occurredTime"/><!--多久之前-->
										</SPAN>
										<xsl:if test="@notSeenCount">
											<DIV class="d-flex justify-content-center">
												<SPAN class="text-xs text-light bg-danger border-radius-md px-1">
													<xsl:value-of select="@notSeenCount"/>
												</SPAN>
											</DIV>
										</xsl:if>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</xsl:for-each>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/inbox.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocketInbox.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>