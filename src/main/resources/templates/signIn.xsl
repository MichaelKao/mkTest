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
			<DIV class="container">
				<FORM action="/signIn.asp" method="post">
					<INPUT class="d-none" id="username" name="username" type="hidden"/>
					<DIV class="form-group mb-3">
						<DIV class="input-group">
							<SELECT class="custom-select" id="country" required="">
								<xsl:apply-templates select="countries/*"/>
							</SELECT>
							<DIV class="input-group-append">
								<INPUT class="form-control" id="cellularPhoneNumber" required="" type="text"/>
							</DIV>
						</DIV>
						<DIV class="input-group mb-3">
							<INPUT class="form-control" name="password" required="" type="password"/>
						</DIV>
					</DIV>
					<DIV class="form-group mb-3">
						<BUTTON class="btn btn-primary" id="signInButton" type="submit">登入</BUTTON>
					</DIV>
				</FORM>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/signIn.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>