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
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container pt-9 pb-5 pt-md-10 pb-mb-5">
				<DIV class="card col-12 col-md-7 mx-auto">
					<DIV class="bg-gradient-primary border-radius-lg py-3 w-85 mt-n5 mx-auto text-center">
						<DIV class="text-light mb-0 h1 resultIcon">
							<xsl:choose>
								<xsl:when test="@fail">
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
					<DIV class="mx-auto my-5">
						<xsl:if test="not(@fail)">
							<DIV class="d-flex mb-2">
								<DIV>交易時間：</DIV>
								<DIV class="text-primary text-gradient">
									<xsl:value-of select="@date"/>
								</DIV>
							</DIV>
							<DIV class="d-flex mb-2">
								<DIV>支付金額：</DIV>
								<DIV class="text-primary text-gradient totalAmount">
									<xsl:value-of select="@amount"/>
								</DIV>
							</DIV>
							<DIV class="d-flex mb-2">
								<DIV>付款項目：</DIV>
								<DIV class="text-primary text-gradient">
									<xsl:value-of select="@result"/>
								</DIV>
							</DIV>
						</xsl:if>
						<xsl:if test="@fail">
							<DIV class="d-flex mb-2">
								<DIV>失敗原因：</DIV>
								<DIV class="text-primary text-gradient">
									<xsl:value-of select="@reason"/>
								</DIV>
							</DIV>
						</xsl:if>
					</DIV>
					<DIV class="mx-auto text-center">
						<A class="btn btn-outline-dark">
							<xsl:attribute name="href">
								<xsl:value-of select="@redirect"/>
							</xsl:attribute>
							<SPAN>確認</SPAN>
						</A>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="footer"/>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
			<SCRIPT src="/SCRIPT/orderResult.js"/>

		</BODY>
	</xsl:template>
</xsl:stylesheet>