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
			<STYLE>.successIcon{font-size: 60px;}</STYLE>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-8">
				<DIV class="card w-80 mx-auto">
					<DIV class="row justify-content-center text-center">
						<DIV class="col-lg-6">
							<DIV class="text-gradient text-primary mb-0 h1 successIcon">
								<xsl:choose>
									<xsl:when test="@fail">
										<I class="fal fa-exclamation-circle"></I>
									</xsl:when>
									<xsl:otherwise>
										<I class="fal fa-check-circle"></I>
									</xsl:otherwise>
								</xsl:choose>
							</DIV>
							<H1 class="text-primary text-gradient">
								<xsl:value-of select="@message"/>
							</H1>
							<P class="lead text-primary">
								<xsl:value-of select="@reason"/>
							</P>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="row justify-content-center text-center">
					<xsl:if test="not(@fail)">
						<DIV class="h4 mt-5">
							<DIV>您已成為 VIP</DIV>
							<DIV>馬上搜尋心儀對象</DIV>
						</DIV>
					</xsl:if>
					<A class="h1 text-info text-gradient" href="">
						<xsl:attribute name="href">
							<xsl:value-of select="@redirect"/>
						</xsl:attribute>
						<I class="fal fa-house-return"></I>
					</A>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/activate.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>