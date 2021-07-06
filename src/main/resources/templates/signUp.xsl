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
			<DIV class="container pt-8">
				<xsl:apply-templates select="form"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/birthday.js"/>
			<SCRIPT src="/SCRIPT/signUp.js"/>
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
						<FORM action="/signUp.asp" method="post">
							<DIV class="form-group">
								<SELECT class="form-control form-control-lg" name="country" required="">
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
							<HR class="horizontal dark my-3"/>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1">
									<i class="fas fa-birthday-cake"></i>
								</DIV>
								<DIV class="col-11">
									<DIV class="d-flex justify-content-around align-items-center">
										<INPUT class="d-none" name="birthday" type="hidden" value=""/>
										<DIV class="d-flex align-items-center">
											<SELECT class="form-control" id="years" required=""></SELECT>
											<SPAN class="ms-1">年</SPAN>
										</DIV>
										<DIV class="d-flex align-items-center">
											<SELECT class="form-control" id="months" required=""></SELECT>
											<SPAN class="ms-1">月</SPAN>
										</DIV>
										<DIV class="d-flex align-items-center">
											<SELECT class="form-control" id="days" required=""></SELECT>
											<SPAN class="ms-1">日</SPAN>
										</DIV>
									</DIV>
									<DIV class="text-center text-sm mt-1">生日註冊後不得更改</DIV>
								</DIV>
							</DIV>
							<HR class="horizontal dark my-3"/>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1">
									<i class="fas fa-venus-mars"></i>
								</DIV>
								<DIV class="col-11">
									<DIV class="d-flex justify-content-around align-items-center">
										<DIV class="form-check">
											<INPUT class="form-check-input" id="female" name="gender" required="" type="radio" value="false"/>
											<LABEL class="custom-control-label h6" for="female">
												<xsl:value-of select="gender/@female"/>
											</LABEL>
										</DIV>
										<DIV class="form-check">
											<INPUT class="form-check-input" id="male" name="gender" required="" type="radio" value="true"/>
											<LABEL class="custom-control-label h6" for="male">
												<xsl:value-of select="gender/@male"/>
											</LABEL>
										</DIV>
									</DIV>
									<DIV class="text-center text-sm mt-1">性別註冊後不得更改</DIV>
								</DIV>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-lg bg-gradient-primary btn-lg w-100 mb-0" type="submit">
									<xsl:value-of select="@i18n-submit"/>
								</BUTTON>
							</DIV>
						</FORM>
					</DIV>
					<DIV class="card-footer text-center pt-0 px-lg-2 px-1">
						<DIV class="text-sm mx-auto">
							<SPAN>註冊即表示您已閱讀並同意</SPAN>
							<A href="/privacy.asp" class="text-primary text-gradient font-weight-bold">隱私權政策</A>
							<SPAN>與</SPAN>
							<A href="/terms.asp" class="text-primary text-gradient font-weight-bold">服務條款</A>
							<SPAN>，並表示已滿18歲。</SPAN>
						</DIV>
						<DIV class="text-sm mx-auto">
							<SPAN class="me-1">已是會員?</SPAN>
							<A href="/signIn.asp" class="text-primary text-gradient font-weight-bold">按此登入</A>
						</DIV>
						<DIV class="text-sm mx-auto">
							<SPAN class="me-1">已有註冊手機?</SPAN>
							<A href="/reactivate.asp" class="text-primary text-gradient font-weight-bold">按此重新激活</A>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>