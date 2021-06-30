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
			<DIV class="container py-7">
				<H4 class="text-primary text-center">
					<xsl:value-of select="@title"/>
				</H4>
				<DIV class="d-flex flex-column flex-md-row flex-wrap justify-content-center align-items-center">
					<DIV class="col-12 col-md-8 card card-frame mb-3 mx-2">
						<xsl:for-each select="history">
							<DIV class="card-body d-flex align-items-center justify-content-start py-0">
								<DIV>
									<A href="/profile/{@identifier}/">
										<IMG alt="profile_photo" class="border-radius-lg" src="{@profileImage}" width="70"/>
									</A>
								</DIV>
								<DIV class="ms-4 w-100 d-flex flex-column flex-md-row">
									<DIV>
										<SPAN class="text-xs font-weight-bold my-2">
											<xsl:value-of select="@time"/>
										</SPAN>
										<DIV class="text-dark">
											<xsl:value-of select="@message"/>
										</DIV>
									</DIV>
									<xsl:if test="@button">
										<DIV class="ms-0 ms-md-auto buttons">
											<INPUT name="whom" type="hidden" value="{@identifier}"/>
											<BUTTON class="btn btn-sm btn-outline-primary px-2 py-1 p-md-2 m-0 me-1 accept" type="button">接受</BUTTON>
											<BUTTON class="btn btn-sm btn-outline-primary px-2 py-1 p-md-2 m-0 me-1 refuse" type="button">拒絕</BUTTON>
										</DIV>
									</xsl:if>
								</DIV>
							</DIV>
							<HR class="horizontal dark"/>
						</xsl:for-each>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/activeLogs.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>