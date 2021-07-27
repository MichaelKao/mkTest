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
			<DIV class="container py-8 text-center">
				<DIV class="row">
					<DIV class="col-lg-7 mx-auto d-flex justify-content-center flex-column">
						<H3 class="text-center">群發打招呼</H3>
						<P>每 24 小時可依照您的地區標籤來向 30 位男仕群發打招呼一次。</P>
						<FORM action="" method="post">
							<DIV class="card-body">
								<DIV class="form-group mb-4">
									<LABEL for="greeting">招呼語</LABEL>
									<TEXTAREA class="form-control" id="greeting" name="greeting" rows="4"></TEXTAREA>
								</DIV>
								<DIV class="row">
									<DIV class="col-md-12">
										<button class="btn bg-gradient-dark w-100" type="submit">打招呼</button>
									</DIV>
								</DIV>
							</DIV>
						</FORM>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>