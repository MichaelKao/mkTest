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
			<LINK href="/STYLE/ECPayPayment.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<INPUT id="me" type="hidden" value="{@identifier}"/>
			<INPUT id="plan" type="hidden" value="{plan/@id}"/>
			<DIV class="container pt-7 pb-5">
				<DIV class="card col-11 col-md-8 col-lg-6 mx-auto mt-3">
					<DIV class="card-body pt-2">
						<DIV id="ECPayPayment"/>
						<FORM action="" class="mt-2 text-center" method="post" name="payment">
							<BUTTON class="btn btn-outline-info btn-sm h6 text-info px-3 m-0" type="submit">付款</BUTTON>
						</FORM>
					</DIV>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<DIV class="loadingWrap">
				<DIV class="loading">
					<DIV class="round"></DIV>
					<DIV class="round ms-1"></DIV>
					<DIV class="round ms-1"></DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="https://cdn.jsdelivr.net/npm/node-forge@0.7.0/dist/forge.min.js"/>
			<SCRIPT src="https://ecpg-stage.ecpay.com.tw/Scripts/sdk-1.0.0.js?t=20210121100116"/>
			<SCRIPT src="/SCRIPT/rechargeByInpay2.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>