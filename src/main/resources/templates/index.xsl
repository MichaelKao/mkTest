<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fmt="http://java.sun.com/jsp/jstl/fmt">
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
		<LINK href="/STYLE/index.css" rel="stylesheet"/>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<xsl:call-template name="headMetaTags"/>
			<TITLE>
				<xsl:value-of select="@title"/>
			</TITLE>
			<xsl:call-template name="headLinkTags"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container">
				<UL class="list-group">
					<xsl:choose>
						<xsl:when test="@me">
							<LI class="list-group-item">
								<A href="/signOut.asp">登出</A>
							</LI>
						</xsl:when>
						<xsl:otherwise>
							<LI class="list-group-item">
								<A href="/signIn.asp">登入</A>
							</LI>
							<LI class="list-group-item">
								<A href="/signUp.asp">註冊</A>
							</LI>
							<LI class="list-group-item">
								<A href="/activate.asp">激活</A>
							</LI>
							<LI class="list-group-item">
								<A href="/reactivate.asp">重新激活</A>
							</LI>
						</xsl:otherwise>
					</xsl:choose>
					<LI class="list-group-item">
						<A href="/authentication.json">認證</A>
					</LI>
				</UL>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>