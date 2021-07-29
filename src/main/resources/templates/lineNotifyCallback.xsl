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
			<STYLE>BODY{background: #F3F3F3 !important;} .resultIcon{font-size: 60px;}</STYLE>
		</HEAD>
		<BODY>
			<DIV class="container position-sticky z-index-sticky top-0">
				<INPUT name="identifier" type="hidden" value="{@identifier}"/>
				<NAV class="navbar navbar-expand-lg blur blur-rounded top-0 z-index-3 shadow position-absolute my-3 py-2 start-0 end-0 mx-4">
					<DIV class="container-fluid">
						<A class="navbar-brand font-weight-bolder ms-sm-3" href="/">Young Me 甜蜜約會</A>
					</DIV>
				</NAV>
			</DIV>
			<DIV class="container pt-9 pb-5 pt-md-10 pb-mb-5">
				<DIV class="card col-12 col-md-7 mx-auto">
					<DIV class="bg-gradient-primary border-radius-lg py-3 w-85 mt-n5 mx-auto text-center">
						<DIV class="text-light mb-0 h1 resultIcon">
							<xsl:choose>
								<xsl:when test="@result = 'false'">
									<I class="fal fa-exclamation-circle"></I>
								</xsl:when>
								<xsl:otherwise>
									<I class="fal fa-check-circle"></I>
								</xsl:otherwise>
							</xsl:choose>
						</DIV>
						<H1 class="text-light">
							<xsl:value-of select="@message"/>
						</H1>
					</DIV>
					<DIV class="mx-auto my-4 h3">
						<i class="far fa-bell-on me-1" aria-hidden="true"></i>
						<xsl:if test="not(@revoke)">
							<xsl:choose>
								<xsl:when test="@result = 'false'">
									<SPAN>LINE Notify 連動失敗</SPAN>
								</xsl:when>
								<xsl:otherwise>
									<SPAN>LINE Notify 連動成功</SPAN>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<xsl:if test="@revoke">
							<xsl:choose>
								<xsl:when test="@result = 'false'">
									<SPAN>LINE Notify 解除綁定失敗</SPAN>
								</xsl:when>
								<xsl:otherwise>
									<SPAN>LINE Notify 已取消綁定</SPAN>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</DIV>
					<DIV class="mx-auto text-center">
						<A class="btn btn-outline-dark text-lg" href="/">
							<SPAN>回首頁</SPAN>
						</A>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>