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
			<LINK href="/STYLE/signUp.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container pt-7">
				<xsl:apply-templates select="form"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/birthday.js"/>
			<SCRIPT src="/SCRIPT/signUp.js"/>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<DIV class="row mt-2">
			<DIV class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-auto px-0">
				<DIV class="card card-plain">
					<DIV class="card-header p-1 p-sm-2 text-left">
						<H5 class="text-center font-weight-bolder">開啟養蜜旅程</H5>
					</DIV>
					<DIV class="card-body p-1 p-sm-2">
						<FORM action="/signUp.asp" method="post">
							<DIV class="row align-items-center mb-2">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-globe-americas text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<SELECT class="form-control form-control-lg" name="country" required="">
										<xsl:apply-templates select="countries/*"/>
									</SELECT>
								</DIV>
							</DIV>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-phone-square-alt text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<INPUT class="form-control form-control-lg" name="login" placeholder="手機號碼" required="" type="text" value=""/>
								</DIV>
							</DIV>
							<HR class="horizontal dark my-3"/>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-birthday-cake text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<DIV class="text-xs text-warning mb-1">
										<I class="fas fa-exclamation-circle me-1"></I>
										<SPAN>生日註冊後不得更改</SPAN>
									</DIV>
									<DIV class="d-flex justify-content-around align-items-center">
										<INPUT class="d-none" name="birthday" type="hidden" value=""/>
										<DIV class="d-flex align-items-center w-100 me-2">
											<SELECT class="form-control" id="years" required=""></SELECT>
											<SPAN class="ms-1">年</SPAN>
										</DIV>
										<DIV class="d-flex align-items-center w-100 me-2">
											<SELECT class="form-control" id="months" required=""></SELECT>
											<SPAN class="ms-1">月</SPAN>
										</DIV>
										<DIV class="d-flex align-items-center w-100">
											<SELECT class="form-control" id="days" required=""></SELECT>
											<SPAN class="ms-1">日</SPAN>
										</DIV>
									</DIV>
								</DIV>
							</DIV>
							<HR class="horizontal dark my-3"/>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-venus-mars text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<DIV class="text-xs text-warning mb-2">
										<I class="fas fa-exclamation-circle me-1"></I>
										<SPAN>性別註冊後不得更改</SPAN>
									</DIV>
									<DIV class="text-center">
										<UL class="p-0 m-0">
											<LI>
												<INPUT class="form-check-input" id="female" name="gender" type="radio" value="false"/>
												<LABEL class="female" for="female">
													<IMG src="/IMAGE/meQueen.png" width="75"/>
												</LABEL>
											</LI>
											<LI>
												<INPUT id="male" name="gender" type="radio" value="true"/>
												<LABEL class="male" for="male">
													<IMG src="/IMAGE/meKing.png" width="75"/>
												</LABEL>
											</LI>
										</UL>
									</DIV>
								</DIV>
							</DIV>
							<HR class="horizontal dark my-3"/>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-users-crown text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<INPUT class="form-control form-control-lg" name="referralCode" placeholder="邀請碼" type="text" value=""/>
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