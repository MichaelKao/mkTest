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
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-8">
				<DIV class="card col-11 col-md-8 col-lg-6 mx-auto">
					<DIV class="card-body pt-2">
						<SPAN class="text-gradient text-primary text-sm font-weight-bold my-2">訂製情人</SPAN>
						<DIV class="h3 d-block text-darker my-2">解鎖 VIP</DIV>
						<P class="card-description mb-4">
							<DIV>1. 查看有哪些女生曾到訪觀看自介</DIV>
							<DIV>2. 每日可跟女生要求 10 次加入 Line 好友</DIV>
							<DIV>3. 可打賞車馬費給女生</DIV>
						</P>
						<BUTTON class="btn btn-sm btn-outline-info m-0" type="button">升級</BUTTON>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>