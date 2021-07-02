<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
		<LINK href="/manifest.json" rel="manifest"/>
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
								<A class="btn btn-primary btn-round btn-sm mx-2" href="/signIn.asp">登入</A>
								<A class="btn btn-primary btn-round btn-sm mx-2" href="/signUp.asp">註冊</A>
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
			<SECTION class="pt-3 pb-4" id="count-stats">
				<DIV class="container">
					<DIV class="row">
						<DIV class="col-lg-9 z-index-2 border-radius-xl mt-n8 mx-auto py-3 blur shadow-blur">
							<DIV class="row">
								<DIV class="col-4 position-relative">
									<DIV class="p-1 text-center">
										<H3 class="text-gradient text-primary text-">1</H3>
										<H6 class="mt-3">手機註冊</H6>
										<P class="text-sm">僅需使用手機號碼簡單註冊</P>
									</DIV>
									<HR class="vertical dark"/>
								</DIV>
								<DIV class="col-4 position-relative">
									<DIV class="p-1 text-center">
										<H3 class="text-gradient text-primary">2</H3>
										<H6 class="mt-3">編輯資料</H6>
										<P class="text-sm">編輯你/妳的個人資料</P>
									</DIV>
									<HR class="vertical dark"/>
								</DIV>
								<DIV class="col-4">
									<DIV class="p-1 text-center">
										<H3 class="text-gradient text-primary">3</H3>
										<H6 class="mt-3">尋找對象</H6>
										<P class="text-sm">馬上就能尋找心儀對象</P>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</SECTION>
			<SECTION class="py-4">
				<DIV class="row my-4 px-3 px-md-5 px-lg-11">
					<DIV class="col-md-6 d-flex align-items-center justify-content-center">
						<DIV class="position-relative">
							<DIV class="manPic border-radius-2xl height-200 border-radius-bottom-start-0 border-radius-top-end-0">
								<DIV class="manPicFrame position-absolute height-200 bg-gradient-primary border-radius-2xl border-radius-bottom-start-0 border-radius-top-end-0"></DIV>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="col-md-6 d-flex flex-column align-items-md-start align-items-center">
						<SPAN class="text-gradient text-primary text-sm font-weight-bold my-2">MAN</SPAN>
						<H4>馬上尋找你的理想男士</H4>
						<DIV>這裡的對象不但事業有成，而且慷慨大方、寵愛女孩。</DIV>
						<DIV>無論妳是網美、上班族或者是學生，</DIV>
						<DIV>只要妳期待著被有經濟能力的人來照顧，</DIV>
						<DIV>現在立即註冊，尋找你想要的理想關係。</DIV>
					</DIV>
				</DIV>
				<DIV class="row px-3 px-md-5 px-xl-11">
					<DIV class="col-md-6 mt-4 order-2 order-md-1 d-flex flex-column align-items-md-end align-items-center">
						<SPAN class="text-gradient text-primary text-sm font-weight-bold my-2">WOMAN</SPAN>
						<H4>馬上尋找你的心儀女孩</H4>
						<DIV>這裡的女孩渴望找尋像您這樣的成功人士，</DIV>
						<DIV>裡面有不少的網美、直播主或學生，</DIV>
						<DIV>她們希望能夠用她們的美麗與溫柔，來換取您的寵愛。</DIV>
						<DIV>現在立即註冊，尋找你心儀的幸福對象。</DIV>
					</DIV>
					<DIV class="col-md-6 d-flex align-items-center justify-content-center order-1 order-md-2">
						<DIV class="position-relative">
							<DIV class="womanPic border-radius-2xl height-200 border-radius-bottom-start-0 border-radius-top-end-0">
								<DIV class="womanPicFrame position-absolute height-200 bg-gradient-primary border-radius-2xl border-radius-bottom-start-0 border-radius-top-end-0"></DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</SECTION>
			<xsl:if test="@signIn">
				<SECTION class="mt-4">
					<H3 class="text-primary text-center">尋找你/妳的心儀對象</H3>
					<DIV class="card">
						<DIV class="d-flex flex-wrap justify-content-center my-5">
							<xsl:for-each select="lover">
								<A class="position-relative m-2" href="/profile/{identifier}/">
									<IMG class="border-radius-md" src="{profileImage}" width="130"/>
									<DIV class="position-absolute bottom-0 right-0 d-flex text-light text-bold">
										<SPAN class="bg-dark opacity-6 border-radius-md px-1">
											<SPAN>
												<xsl:value-of select="nickname"/>
											</SPAN>
											<SPAN class="ms-2">
												<xsl:value-of select="age"/>
											</SPAN>
										</SPAN>
									</DIV>
								</A>
							</xsl:for-each>
						</DIV>
					</DIV>
				</SECTION>
			</xsl:if>
			<xsl:call-template name="bodyScriptTags"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>