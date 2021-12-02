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
			<LINK href="/STYLE/loading.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<INPUT id="me" type="hidden" value="{@identifier}"/>
			<DIV class="container pt-7 pt-md-8 pb-2 px-2">
				<DIV class="card col-11 col-md-8 col-lg-6 mx-auto mt-3">
					<DIV class="card-body px-3">
						<xsl:if test="not(@vvip)">
							<SPAN class="text-primary text-sm font-weight-bold my-2">養蜜 Young me</SPAN>
							<DIV class="h4 d-block text-darker my-2">
								<SPAN>馬上升級</SPAN>
								<span class="fontSize22 text-primary mx-1">meKING</span>
								<SPAN class="text-bolder fontSize22">PRO</SPAN>
							</DIV>
							<DIV id="ECPayPayment"/>
							<FORM action="" class="text-center mt-2" method="post" name="payment">
								<BUTTON class="btn bg-dark btn-round text-white mb-0" type="submit">付款</BUTTON>
							</FORM>
						</xsl:if>
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
						<xsl:when test="@development and @development='true'">/SCRIPT/upgradeLongTerm_.js</xsl:when>
						<xsl:otherwise>/SCRIPT/upgradeLongTerm.js</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</SCRIPT>
		</BODY>
	</xsl:template>
</xsl:stylesheet>