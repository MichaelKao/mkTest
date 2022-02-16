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
			<STYLE>BODY{background: #F3F3F3 !important;}</STYLE>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-7 py-md-8 px-3 col-11 col-md-6 col-lg-5">
				<DIV class="text-center">
					<I class="fad fa-users-crown mb-1 fontSize50"></I>
					<H4 class="text-primary">好友邀請碼</H4>
					<DIV class="d-flex justify-content-center">
						<DIV class="h2 text-lighter" id="referralCode">
							<xsl:value-of select="referralCode"/>
						</DIV>
						<BUTTON class="btn btn-link m-0 p-0 text-lg ms-2 text-dark" id="referralCodeCopy">
							<I class="fal fa-copy"></I>
						</BUTTON>
					</DIV>
					<BUTTON class="btn btn-primary btn-round text-lg py-1 share">
						<SPAN>分享給好友</SPAN>
						<I class="fal fa-share ms-1"></I>
					</BUTTON>
				</DIV>
				<DIV class="position-relative">
					<DIV class="descendants"></DIV>
					<DIV class="pagination text-center text-sm text-primary cursor-pointer"></DIV>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/referralCode.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>