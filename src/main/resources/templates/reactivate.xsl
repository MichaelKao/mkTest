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
		</HEAD>
		<BODY class="mt-3 mb-3">
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container">
				<xsl:apply-templates select="form"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/reactivate.js"/>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<FORM action="/reactivate.json" method="post">
			<INPUT class="d-none" id="username" name="username" type="hidden"/>
			<DIV class="form-group mb-3">
				<DIV class="input-group">
					<SELECT class="custom-select" id="country" required="">
						<xsl:apply-templates select="country/*"/>
					</SELECT>
					<DIV class="input-group-append">
						<INPUT class="form-control" id="cellularPhoneNumber" required="" type="text"/>
					</DIV>
				</DIV>
			</DIV>
			<DIV class="form-group mb-3">
				<BUTTON class="btn btn-primary" id="signInButton" type="submit">
					<xsl:value-of select="@i18n-submit"/>
				</BUTTON>
			</DIV>
		</FORM>
	</xsl:template>
</xsl:stylesheet>