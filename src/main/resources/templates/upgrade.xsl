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
				<DIV class="text-center mx-sm-5 mx-lg-12 mb-5">
					<H4 class="text-primary">
						<xsl:value-of select="@title"/>
					</H4>
				</DIV>
				<DIV class="d-flex flex-column flex-md-row align-items-center justify-content-center ms-2">
					<DIV class="col-11 col-md-4 card mb-3 mx-2">
						<DIV class="card-body p-md-4 p-lg-5">
							<SPAN class="text-gradient text-primary text-sm font-weight-bold my-2">長期VIP</SPAN>
							<DIV class="text-lg text-bold text-dark my-2">
								<SPAN>月付1288元 / 一年期</SPAN>
							</DIV>
							<DIV class="card-description d-flex align-items-baseline">
								<SPAN>NT$</SPAN>
								<SPAN>1288</SPAN>
								<A class="ms-auto btn btn-sm btn-outline-info px-3 px-sm-4 m-0" href="/upgrade/1.asp">選擇</A>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="col-11 col-md-4 card mb-3 mx-2">
						<DIV class="card-body p-md-4 p-lg-5">
							<SPAN class="text-gradient text-primary text-sm font-weight-bold my-2">短期VIP</SPAN>
							<DIV class="text-lg text-bold text-dark my-2">
								<SPAN>一個月$1688 / 一次性</SPAN>
							</DIV>
							<DIV class="card-description d-flex align-items-baseline">
								<SPAN>NT$</SPAN>
								<SPAN>1288</SPAN>
								<A class="ms-auto btn btn-sm btn-outline-info px-3 px-sm-4 m-0" href="/upgrade/2.asp">選擇</A>
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