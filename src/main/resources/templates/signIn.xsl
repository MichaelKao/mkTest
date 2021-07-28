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
			<DIV class="page-header section-height-100">
				<DIV class="container">
					<xsl:apply-templates select="form"/>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/signIn.js"/>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<DIV class="row">
			<DIV class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-auto">
				<DIV class="card card-plain">
					<DIV class="card-header p-1 p-sm-2 text-left">
						<H4 class="font-weight-bolder">
							<xsl:value-of select="/document/@title"/>
						</H4>
					</DIV>
					<DIV class="card-body p-1 p-sm-2">
						<FORM action="/signIn.asp" method="post">
							<INPUT class="d-none" id="username" name="username" type="hidden"/>
							<DIV class="form-group">
								<SELECT class="form-control" id="country" required="">
									<xsl:apply-templates select="country/*"/>
								</SELECT>
							</DIV>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1">
									<I class="fas fa-mobile-alt text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<INPUT class="form-control" id="cellularPhoneNumber" placeholder="手機號碼" required="" type="text"/>
								</DIV>
							</DIV>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1">
									<I class="fas fa-lock-alt text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<INPUT class="form-control" name="password" placeholder="密碼" required="" type="password"/>
								</DIV>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-lg bg-gradient-primary btn-lg w-100 mt-4 mb-0" type="submit">
									<xsl:value-of select="@i18n-submit"/>
								</BUTTON>
							</DIV>
						</FORM>
					</DIV>
					<DIV class="card-footer text-center pt-0 px-lg-2 px-1">
						<P class="text-sm mx-auto">
							<SPAN class="me-1">還沒有帳號嗎?</SPAN>
							<A href="/signUp.asp" class="text-primary text-gradient font-weight-bold">免費註冊</A>
						</P>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>