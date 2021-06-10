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
			<DIV class="p-3 position-absolute" style="top:0;right:0">
				<DIV class="toast hide" data-delay="3000" role="alert">
					<DIV class="toast-header">
						<STRONG class="mr-auto"/>
						<BUTTON type="button" class="ml-2 mb-1 close" data-dismiss="toast">
							<SPAN>&#215;</SPAN>
						</BUTTON>
					</DIV>
					<DIV class="toast-body"/>
				</DIV>
			</DIV>
			<DIV class="container">
				<FORM action="/activated.asp" method="post">
					<INPUT class="d-none" name="identifier" type="hidden" value="{identifier}"/>
					<DIV class="form-group mb-3">
						<DIV class="input-group">
							<DIV class="input-group-prepend">
								<LABEL class="input-group-text" for="shadow">密碼</LABEL>
							</DIV>
							<INPUT class="form-control" id="shadow" name="shadow" required="" type="password"/>
							<DIV class="input-group-append">
								<BUTTON class="btn btn-primary" type="submit">開始</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</FORM>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/activation.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>