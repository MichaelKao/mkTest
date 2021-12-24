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
			<STYLE>.form-control{border:none;}</STYLE>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="page-header section-height-100">
				<DIV class="container pt-7 pb-4">
					<DIV class="text-center mb-3">
						<DIV>
							<IMG class="logo" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/bigLOGO.svg" width="125	"/>
						</DIV>
						<DIV class="text-dark my-2">STEP 3 初始密碼</DIV>
						<SVG viewBox="0 0 228 20" height="20" width="228" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns="http://www.w3.org/2000/svg">
							<g id="iPhone_12_12_Pro_1" data-name="iPhone 12, 12 Pro – 1" clip-path="url(#clip-iPhone_12_12_Pro_1)">
								<rect id="Rectangle_1" data-name="Rectangle 1" width="228" height="18" rx="9" transform="translate(0 0)" fill="#154354"></rect>
								<g id="Ellipse_1" data-name="Ellipse 1" transform="translate(0 0)" fill="#fff" stroke="#62636e" stroke-width="3">
									<circle cx="9" cy="9" r="9" stroke="none"></circle>
									<circle cx="9" cy="9" r="7.5" fill="none"></circle>
								</g>
								<g id="Ellipse_2" data-name="Ellipse 2" transform="translate(70 0)" fill="#fff" stroke="#62636e" stroke-width="3">
									<circle cx="9" cy="9" r="9" stroke="none"></circle>
									<circle cx="9" cy="9" r="7.5" fill="none"></circle>
								</g>
								<g id="Ellipse_3" data-name="Ellipse 3" transform="translate(140 0)" fill="#468ca6" stroke="#62636e" stroke-width="3">
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
			</DIV>
			<xsl:call-template name="footer"/>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/activated.js"/>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<DIV class="row">
			<DIV class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-auto">
				<DIV class="card card-plain">
					<DIV class="p-1 p-sm-2 text-left">
						<P class="mb-0">設定您的密碼</P>
					</DIV>
					<DIV class="card-body p-1 p-sm-2">
						<FORM action="/activated.asp" method="post">
							<INPUT class="d-none" name="identifier" type="hidden" value="{identifier}"/>
							<DIV class="row align-items-center mb-3">
								<DIV class="col-1 d-flex justify-content-start">
									<I class="fas fa-key text-lg"></I>
								</DIV>
								<DIV class="col-11 position-relative">
									<INPUT class="form-control" id="shadow" name="shadow" placeholder="{shadow/@i18n}" required="" type="password"/>
									<SPAN class="text-secondary togglePwd text-sm position-absolute cursor-pointer" style="right: 20px; top: 10px;">
										<I class="fad fa-eye"></I>
									</SPAN>
								</DIV>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-dark w-100 mt-4 mb-0" type="submit">
									<xsl:value-of select="@i18n-submit"/>
								</BUTTON>
							</DIV>
						</FORM>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>