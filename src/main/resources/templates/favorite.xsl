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
				<DIV class="mx-sm-5 mx-lg-12 mb-3">
					<H4 class="text-primary">
						<xsl:value-of select="@title"/>
					</H4>
					<HR class="horizontal dark"/>
				</DIV>
				<DIV class="col-lg-8 mx-auto d-flex flex-column flex-md-row flex-wrap justify-content-center align-items-center">
					<xsl:for-each select="lover">
						<DIV class="col-11 col-sm-8 col-md-5 card card-frame mb-3 mx-2">
							<DIV class="card-body d-flex align-items-center justify-content-evenly p-3 p-md-4">
								<DIV>
									<A href="/profile/{@identifier}/">
										<IMG alt="profile_photo" class="border-radius-lg" src="{@profileImage}" width="70"/>
									</A>
								</DIV>
								<DIV class="ms-3">
									<DIV class="d-flex justify-content-around">
										<xsl:if test="@vip">
											<DIV>
												<IMG class="border-radius-md" src="/vip.svg" width="30"/>
											</DIV>
										</xsl:if>
										<xsl:if test="@relief = 'true'">
											<DIV>
												<IMG class="border-radius-md" src="/accept.svg" width="30"/>
											</DIV>
										</xsl:if>
									</DIV>
									<A class="h6" href="/profile/{@identifier}/">
										<SPAN class="me-2">
											<xsl:value-of select="@nickname"/>
										</SPAN>
										<SPAN>
											<xsl:value-of select="@age"/>
										</SPAN>
									</A>
								</DIV>
							</DIV>
						</DIV>
					</xsl:for-each>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>