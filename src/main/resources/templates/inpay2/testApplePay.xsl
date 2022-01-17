<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output
		encoding="UTF-8"
		media-type="text/html"
		method="html"
		indent="no"
		omit-xml-declaration="yes"
	/>

	<xsl:include href="../default.xsl"/>

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
			<LINK href="/STYLE/loading.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<INPUT id="me" type="hidden" value="{@identifier}"/>
			<INPUT id="plan" type="hidden" value="{plan/@id}"/>
			<DIV class="container px-0 pt-6 pt-md-7 pb-5">
				<DIV class="card col-11 col-md-8 col-lg-6 mx-auto mt-3">
					<INPUT name="points" type="hidden" value="{plan/@points}"/>
					<DIV class="card-body">
						<SPAN class="text-primary text-sm font-weight-bold my-2">養蜜 Young me</SPAN>
						<DIV class="h4 d-block text-darker my-2 totalAmount">
							<SPAN class="me-1">NT$</SPAN>
							<SPAN>
								<xsl:value-of select="plan/@amount"/>
							</SPAN>
							<SPAN>｜</SPAN>
							<SPAN>
								<xsl:value-of select="plan/@points"/>
							</SPAN>
							<SPAN class="ms-1">ME 點</SPAN>
						</DIV>
						<DIV id="ECPayPayment"/>
						<FORM action="" class="text-center mt-2" method="post" name="payment">
							<BUTTON class="btn bg-dark btn-round text-white mb-0" type="submit">付款</BUTTON>
						</FORM>
					</DIV>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<DIV class="loadingWrap" style="display: block;">
				<DIV class="loading">
					<DIV class="round"></DIV>
					<DIV class="round ms-1"></DIV>
					<DIV class="round ms-1"></DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="https://cdn.jsdelivr.net/npm/node-forge@0.7.0/dist/forge.min.js"/>
			<SCRIPT>
				<xsl:attribute name="src">
					<xsl:choose>
						<xsl:when test="@development and @development='true'">https://ecpg-stage.ecpay.com.tw/Scripts/sdk-1.0.0.js?t=20210121100116</xsl:when>
						<xsl:otherwise>https://ecpg.ecpay.com.tw/Scripts/sdk-1.0.0.js?t=20210121100116</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</SCRIPT>
			<SCRIPT>
				<xsl:attribute name="src">
					<xsl:choose>
						<xsl:when test="@development and @development='true'">/SCRIPT/rechargeByInpay2_.js</xsl:when>
						<xsl:otherwise>/SCRIPT/rechargeByInpay2.js</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</SCRIPT>
		</BODY>
	</xsl:template>
</xsl:stylesheet>