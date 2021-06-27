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
				<DIV class="mb-4 text-center">
					<H4 class="text-primary">我的收藏</H4>
					<HR class="horizontal dark"/>
				</DIV>
				<DIV class="d-flex flex-column flex-md-row flex-wrap justify-content-center align-items-center">
					<DIV class="col-10 col-sm-8 col-md-5 card card-frame mb-3 mx-2">
						<DIV class="card-body d-flex align-items-center justify-content-around p-3 p-md-4">
							<DIV>
								<A href="/profile/">
									<IMG alt="profile_photo" class="border-radius-lg" src="https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/WOMAN/WOMAN+(8).jpg" width="70"/>
								</A>
							</DIV>
							<DIV class="memberInfo ms-3">
								<A class="h6" href="/profile/">Michael Roven</A>
								<DIV>28, 高雄</DIV>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="col-10 col-sm-8 col-md-5 card card-frame mb-3 mx-2">
						<DIV class="card-body d-flex align-items-center justify-content-around">
							<DIV>
								<A href="/profile/">
									<IMG alt="profile_photo" class="border-radius-lg" src="https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/WOMAN/WOMAN+(5).jpg" width="70"/>
								</A>
							</DIV>
							<DIV class="memberInfo ms-3">
								<A class="h6" href="/profile/">Michael Roven</A>
								<DIV>28, 高雄</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>