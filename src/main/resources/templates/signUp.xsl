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
			<META content="IE=edge" http-equiv="X-UA-Compatible"/>
			<META content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport"/>
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
			<SCRIPT src="/SCRIPT/signUp.js"/>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<DIV class="row">
			<DIV class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-auto">
				<DIV class="card card-plain">
					<DIV class="card-header pb-0 text-left">
						<H4 class="font-weight-bolder">
							<xsl:value-of select="/document/@title"/>
						</H4>
						<P class="mb-0">輸入手機號碼註冊</P>
					</DIV>
					<DIV class="card-body">
						<FORM action="/signUp.asp" method="post">
							<DIV class="form-group">
								<SELECT class="form-control" name="country" required="">
									<xsl:apply-templates select="countries/*"/>
								</SELECT>
							</DIV>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1">
									<I class="fas fa-mobile-alt text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<INPUT class="form-control form-control-lg" name="login" placeholder="手機號碼" required="" type="text" value=""/>
								</DIV>
							</DIV>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1">
									<i class="fas fa-venus-mars"></i>
								</DIV>
								<DIV class="col-10 d-flex justify-content-around align-items-center">
									<DIV class="form-check">
										<INPUT class="form-check-input" id="female" name="gender" required="" type="radio" value="false"/>
										<LABEL class="custom-control-label" for="female">
											<xsl:value-of select="gender/@female"/>
										</LABEL>
									</DIV>
									<DIV class="form-check">
										<INPUT class="form-check-input" id="male" name="gender" required="" type="radio" value="true"/>
										<LABEL class="custom-control-label" for="male">
											<xsl:value-of select="gender/@male"/>
										</LABEL>
									</DIV>
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
							<SPAN class="me-1">已是會員?</SPAN>
							<A href="/signIn.asp" class="text-primary text-gradient font-weight-bold">按此登入</A>
						</P>
						<P class="text-sm mx-auto">
							<SPAN class="me-1">已有註冊手機?</SPAN>
							<A href="/reactivate.asp" class="text-primary text-gradient font-weight-bold">按此重新激活</A>
						</P>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>