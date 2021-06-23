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
				<DIV class="mx-sm-5 mx-lg-12 mb-5">
					<h4 class="text-primary">我的收藏</h4>
					<HR class="horizontal dark"/>
				</DIV>
				<DIV class="row mx-sm-5 mx-lg-12 align-items-center justify-content-center">
					<A class="col-4 col-sm-3" href="/profile.asp">
						<IMG alt="profile_photo" class="border-radius-lg w-65" src="https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/WOMAN/WOMAN+(8).jpg"/>
					</A>
					<DIV class="col-6 col-sm-7">
						<A class="h4" href="/profile.asp">Michael Roven</A>
						<DIV>28, 高雄</DIV>
					</DIV>
					<HR class="horizontal dark my-3"/>
				</DIV>
				<DIV class="row mx-sm-5 mx-lg-12 align-items-center justify-content-center">
					<A class="col-4 col-sm-3" href="/profile.asp">
						<IMG alt="profile_photo" class="border-radius-lg w-65" src="https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/WOMAN/WOMAN+(5).jpg"/>
					</A>
					<DIV class="col-6 col-sm-7">
						<A class="h4" href="/profile.asp">Michael Roven</A>
						<DIV>28, 高雄</DIV>
					</DIV>
					<HR class="horizontal dark my-3"/>
				</DIV>
				<DIV class="row mx-sm-5 mx-lg-12 align-items-center justify-content-center">
					<A class="col-4 col-sm-3" href="/profile.asp">
						<IMG alt="profile_photo" class="border-radius-lg w-65" src="https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/WOMAN/WOMAN+(6).jpg"/>
					</A>
					<DIV class="col-6 col-sm-7">
						<A class="h4" href="/profile.asp">Michael Roven</A>
						<DIV>28, 高雄</DIV>
					</DIV>
					<HR class="horizontal dark my-3"/>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>