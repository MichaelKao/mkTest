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
			<DIV class="container pt-7 pb-4">
				<DIV class="text-center mb-3">
					<DIV>
						<IMG class="logo" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/bigLOGO.svg" width="125"/>
					</DIV>
					<DIV class="text-dark my-2">STEP 1 註冊帳號</DIV>
					<SVG viewBox="0 0 228 20" height="20" width="228" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg">
						<g id="iPhone_12_12_Pro_1" data-name="iPhone 12, 12 Pro – 1" clip-path="url(#clip-iPhone_12_12_Pro_1)">
							<rect id="Rectangle_1" data-name="Rectangle 1" width="228" height="18" rx="9" transform="translate(0 0)" fill="#154354"></rect>
							<g id="Ellipse_1" data-name="Ellipse 1" transform="translate(0 0)" fill="#468ca6" stroke="#62636e" stroke-width="3">
								<circle cx="9" cy="9" r="9" stroke="none"></circle>
								<circle cx="9" cy="9" r="7.5" fill="none"></circle>
							</g>
							<g id="Ellipse_2" data-name="Ellipse 2" transform="translate(70 0)" fill="#fff" stroke="#62636e" stroke-width="3">
								<circle cx="9" cy="9" r="9" stroke="none"></circle>
								<circle cx="9" cy="9" r="7.5" fill="none"></circle>
							</g>
							<g id="Ellipse_3" data-name="Ellipse 3" transform="translate(140 0)" fill="#fff" stroke="#62636e" stroke-width="3">
								<circle cx="9" cy="9" r="9" stroke="none"></circle>
								<circle cx="9" cy="9" r="7.5" fill="none"></circle>
							</g>
							<g id="Ellipse_4" data-name="Ellipse 4" transform="translate(210 0)" fill="#fff" stroke="#62636e" stroke-width="3">
								<circle cx="9" cy="9" r="9" stroke="none"></circle>
								<circle cx="9" cy="9" r="7.5" fill="none"></circle>
							</g>
						</g>
					</SVG>
				</DIV>
				<xsl:apply-templates select="form"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/birthday.js"/>
			<SCRIPT src="/SCRIPT/signUp.js"/>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<DIV class="row">
			<DIV class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-auto px-0">
				<DIV class="card card-plain">
					<DIV class="card-body p-1 p-sm-2">
						<FORM action="/signUp.asp" method="post">
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-globe-americas text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<SELECT class="form-control" name="country" required="">
										<xsl:apply-templates select="countries/*"/>
									</SELECT>
								</DIV>
							</DIV>
							<DIV class="row align-items-center mb-4">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-phone-square-alt text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<INPUT class="form-control loginInput" inputmode="numeric" placeholder="手機號碼, 例: 0912345678" required="" type="text" value=""/>
									<INPUT class="d-none" name="login" required="" type="hidden" value=""/>
								</DIV>
							</DIV>
							<DIV class="row align-items-center mb-4">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-birthday-cake text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<DIV class="text-xs mb-1">
										<I class="fas fa-exclamation-circle me-1"></I>
										<SPAN>您的生日《註冊後不得更改》</SPAN>
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
							<DIV class="row align-items-center mb-4">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-venus-mars text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<DIV class="text-xs mb-2">
										<I class="fas fa-exclamation-circle me-1"></I>
										<SPAN>您的性別《註冊後不得更改》</SPAN>
									</DIV>
									<DIV class="text-center">
										<UL class="p-0 m-0">
											<LI>
												<INPUT class="form-check-input" id="female" name="gender" type="radio" value="false"/>
												<LABEL class="female" for="female">
													<IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/meQueen.png" width="75"/>
												</LABEL>
												<DIV class="gender">女</DIV>
											</LI>
											<LI>
												<INPUT id="male" name="gender" type="radio" value="true"/>
												<LABEL class="male" for="male">
													<IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/meKing.png" width="75"/>
												</LABEL>
												<DIV class="gender">男</DIV>
											</LI>
										</UL>
									</DIV>
								</DIV>
							</DIV>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fad fa-users-crown text-lg"></I>
								</DIV>
								<DIV class="col-11">
									<INPUT class="form-control" name="referralCode" placeholder="邀請碼(選填)" type="text" value="">
										<xsl:if test="referralCode">
											<xsl:attribute name="value">
												<xsl:value-of select="referralCode"/>
											</xsl:attribute>
										</xsl:if>
									</INPUT>
								</DIV>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-dark w-100 mb-0" type="submit">
									<xsl:value-of select="@i18n-submit"/>
								</BUTTON>
							</DIV>
						</FORM>
					</DIV>
					<DIV class="card-footer text-center pt-0 px-lg-2 px-1">
						<DIV class="text-xs">
							<SPAN>註冊即表示您已閱讀並同意</SPAN>
							<DIV>
								<A href="/privacy.asp" class="text-primary font-weight-bold">隱私權政策</A>
								<SPAN>與</SPAN>
								<A href="/terms.asp" class="text-primary font-weight-bold">服務條款</A>
							</DIV>
							<SPAN>並表示已滿18歲。</SPAN>
						</DIV>
						<DIV class="mt-2 text-sm">
							<SPAN class="me-1">已是會員?</SPAN>
							<A href="/signIn.asp" class="text-primary font-weight-bold">按此登入</A>
						</DIV>
						<DIV class="text-sm">
							<SPAN class="me-1">已有註冊手機?</SPAN>
							<A href="/reactivate.asp" class="text-primary font-weight-bold">按此重新激活</A>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>