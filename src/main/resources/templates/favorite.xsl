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
			<DIV class="container mx-auto col-md-10 col-lg-8 px-1 px-sm-2 py-6 py-md-7 pb-2">
				<DIV class="text-center">
					<H4 class="text-primary">
						<xsl:value-of select="@title"/>
					</H4>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-0">
					<xsl:for-each select="lover">
						<A class="position-relative m-1" href="/profile/{@identifier}/">
							<IMG class="border-radius-md" src="{@profileImage}" width="150"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center">
								<DIV>
									<SPAN>
										<xsl:value-of select="@nickname"/>
									</SPAN>
									<SPAN class="ms-2">
										<xsl:value-of select="@age"/>
									</SPAN>
								</DIV>
								<xsl:if test="relationship">
									<DIV>
										<SPAN>尋找</SPAN>
										<SPAN>
											<xsl:value-of select="relationship"/>
										</SPAN>
									</DIV>
								</xsl:if>
								<DIV>
									<xsl:for-each select="location">
										<SPAN class="me-1">
											<xsl:if test="position() = last()">
												<xsl:attribute name="class"></xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</SPAN>
									</xsl:for-each>
								</DIV>
							</DIV>
							<xsl:if test="@following">
								<DIV class="position-absolute left-0" style="width: 32px; top: 5px;">
									<I class="fas fa-heart-circle text-pink fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
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