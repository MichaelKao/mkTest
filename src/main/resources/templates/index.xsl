<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fmt="http://java.sun.com/jsp/jstl/fmt">
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
		<LINK href="/STYLE/index.css" rel="stylesheet"/>
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
			<HEADER class="header-2">
				<DIV class="page-header section-height-85 relative" style="background-image: url('https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/COUPLE/COUPLE+(1).jpg')">
					<DIV class="container text-center">
						<DIV class="row">
							<DIV class="col-10 col-md-6 text-center mx-auto">
								<H2 class="text-white pt-3">訂製你的專屬情人</H2>
								<DIV class="text-white mt-3">
									<DIV>成熟穩重的男人</DIV>
									<DIV>遇上充滿魅力的甜心寶貝</DIV>
									<DIV>從此不再寂寞。</DIV>
								</DIV>
							</DIV>
						</DIV>
						<xsl:if test="not(@signIn)">
							<DIV class="my-4">
								<A class="btn btn-dark mx-2" href="/signIn.asp">登入</A>
								<A class="btn btn-dark mx-2" href="/signUp.asp">註冊</A>
							</DIV>
						</xsl:if>
					</DIV>
					<DIV class="position-absolute w-100 z-index-1 bottom-0">
						<SVG class="waves" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 24 150 40" preserveAspectRatio="none" shape-rendering="auto">
							<DEFS>
								<PATH id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z" />
							</DEFS>
							<G class="moving-waves">
								<use xlink:href="#gentle-wave" x="48" y="-1" fill="rgba(255,255,255,0.40" />
								<use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.35)" />
								<use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.25)" />
								<use xlink:href="#gentle-wave" x="48" y="8" fill="rgba(255,255,255,0.20)" />
								<use xlink:href="#gentle-wave" x="48" y="13" fill="rgba(255,255,255,0.15)" />
								<use xlink:href="#gentle-wave" x="48" y="16" fill="rgba(255,255,255,0.95" />
							</G>
						</SVG>
					</DIV>
				</DIV>
			</HEADER>
			<xsl:if test="@signIn">
				<DIV class="d-flex flex-wrap justify-content-center my-5">
					<xsl:for-each select="lover">
						<A class="position-relative m-2" href="/profile/{identifier}/">
							<IMG class="border-radius-md" src="http://www.youngme.vip/profileImage/{identifier}" width="120"/>
							<DIV class="position-absolute bottom-0 right-0 d-flex text-light text-bold">
								<SPAN class="bg-dark opacity-6 border-radius-md px-1">
									<xsl:value-of select="nickname"/>
									<SPAN>,&#160;</SPAN>
									<xsl:value-of select="age"/>
								</SPAN>
							</DIV>
						</A>
					</xsl:for-each>
				</DIV>
			</xsl:if>
			<xsl:call-template name="bodyScriptTags"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>