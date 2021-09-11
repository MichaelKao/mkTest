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
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<xsl:call-template name="headMetaTags"/>
			<TITLE>
				<xsl:value-of select="@title"/>
			</TITLE>
			<LINK href="/manifest.json" rel="manifest"/>
			<xsl:call-template name="headLinkTags"/>
			<LINK href="/STYLE/index.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<xsl:if test="not(@signIn)">
				<HEADER class="header-2">
					<INPUT name="signIn" type="hidden" value="false"/>
					<DIV class="page-header section-height-85 relative" style="background-image: url('https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/COUPLE/COUPLE+(1).jpg')">
						<DIV class="container text-center">
							<DIV class="row">
								<DIV class="col-10 col-md-6 text-center mx-auto">
									<H3 class="text-white mb-0">養蜜</H3>
									<H3 class="text-white mb-0">YOUNG ME</H3>
									<H6 class="text-white">比閨蜜更親密</H6>
									<DIV class="text-white mt-3 text-sm">
										<DIV>她，用青春滋養你</DIV>
										<DIV>他，用富足寵愛妳</DIV>
										<DIV>一場最美好的養蜜關係。</DIV>
									</DIV>
									<DIV class="text-white mt-3 text-sm">
										<DIV>開啟你的養蜜旅程</DIV>
									</DIV>
								</DIV>
							</DIV>
							<xsl:if test="not(@signIn)">
								<DIV class="mt-2">
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
				<SECTION class="py-4">
					<DIV class="container">
						<DIV class="row">
							<DIV class="col-lg-9 z-index-2 border-radius-xl mt-n8 mx-auto py-3 blur shadow-blur">
								<DIV class="row">
									<DIV class="col-6 col-md-3 position-relative">
										<DIV class="p-1 text-center">
											<H3>
												<I class="fad fa-heart"></I>
											</H3>
											<DIV class="text-sm">
												<DIV>真人審核安心認證</DIV>
												<DIV>檢舉功能防詐騙爽約</DIV>
											</DIV>
										</DIV>
										<HR class="vertical dark"/>
									</DIV>
									<DIV class="col-6 col-md-3 position-relative">
										<DIV class="p-1 text-center">
											<H3>
												<I class="fad fa-map-marker-alt"></I>
											</H3>
											<DIV class="text-sm">用戶廣布全台</DIV>
											<DIV class="text-sm">分類搜尋更效率</DIV>
										</DIV>
										<HR class="vertical dark"/>
									</DIV>
									<DIV class="col-6 col-md-3 position-relative">
										<DIV class="p-1 text-center">
											<H3>
												<I class="fad fa-lock-alt"></I>
											</H3>
											<DIV class="text-sm">資訊絕對隱私</DIV>
											<DIV class="text-sm">金流明細保密</DIV>
										</DIV>
										<HR class="vertical dark"/>
									</DIV>
									<DIV class="col-6 col-md-3">
										<DIV class="p-1 text-center">
											<H3>
												<I class="fad fa-comment-smile"></I>
											</H3>
											<DIV class="text-sm">系統跳轉聊天</DIV>
											<DIV class="text-sm">ID不外洩好安心</DIV>
										</DIV>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</SECTION>
				<SECTION class="py-4 w-95 mx-auto">
					<DIV class="row mx-0 my-4 px-3 px-md-5 px-lg-11">
						<DIV class="col-md-6 d-flex align-items-center justify-content-center">
							<DIV class="position-relative">
								<DIV class="manPic border-radius-2xl height-200 border-radius-bottom-start-0 border-radius-top-end-0">
									<DIV class="manPicFrame position-absolute height-200 bg-gradient-primary border-radius-2xl border-radius-bottom-start-0 border-radius-top-end-0"></DIV>
								</DIV>
							</DIV>
						</DIV>
						<DIV class="col-md-6 d-flex flex-column align-items-md-start align-items-center">
							<SPAN class="text-gradient text-primary text-md font-weight-bold my-2">meKING</SPAN>
							<DIV>你是叱吒職場的王者</DIV>
							<DIV>時間的分秒珍貴無比</DIV>
							<DIV>善用有效直接的方式</DIV>
							<DIV>找到理想的 meQueen</DIV>
						</DIV>
					</DIV>
					<DIV class="row mx-0 px-3 px-md-5 px-xl-11">
						<DIV class="col-md-6 mt-4 order-2 order-md-1 d-flex flex-column align-items-md-end align-items-center">
							<SPAN class="text-gradient text-primary text-md font-weight-bold my-2">meQUEEN</SPAN>
							<DIV>妳是讓人上癮的蜜糖</DIV>
							<DIV>有著獨一無二的美麗</DIV>
							<DIV>meKING 的萬千寵愛</DIV>
							<DIV>讓妳的迷人燦爛綻放</DIV>
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
				<xsl:call-template name="footer"/>
			</xsl:if>
			<xsl:if test="not(@lineNotify) and @signIn">
				<DIV class="modal fade" id="lineNotifyModal" tabindex="-1">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
								</DIV>
								<DIV class="mt-3 text-center">
									<I class="far fa-bell-on text-success mb-1" style="font-size: 50px;"></I>
									<H4>接收Line Notify即時通知?</H4>
									<P>這是LINE官方推出的新功能，若是有通知則可以推送到您的LINE上，第一時間收到通知消息，隱私又安全。</P>
								</DIV>
								<DIV class="text-center">
									<A class="btn btn-primary mx-2" href="/notify-bot.line.me/authorize.asp">接收</A>
									<BUTTON class="btn btn-secondary mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</xsl:if>
			<xsl:if test="@signIn">
				<INPUT name="signIn" type="hidden" value="true"/>
				<DIV class="container px-0 px-md-3 py-7">
					<INPUT name="gender" type="hidden">
						<xsl:if test="@male">
							<xsl:attribute name="value">male</xsl:attribute>
						</xsl:if>
						<xsl:if test="@female">
							<xsl:attribute name="value">female</xsl:attribute>
						</xsl:if>
					</INPUT>
					<xsl:apply-templates select="area"/>
					<xsl:call-template name="footer"/>
				</DIV>
			</xsl:if>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/index.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
	<xsl:template match="area">
		<xsl:if test="vip">
			<ARTICLE class="col-md-8 border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex m-2 align-items-center">
					<DIV class="text-lg m-2 text-bold text-primary">貴賓會員</DIV>
					<xsl:if test="not(vip/@lastPage)">
						<BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="vip" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
					</xsl:if>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 vip">
					<xsl:for-each select="vip/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
								<DIV>
									<SPAN>
										<xsl:value-of select="nickname"/>
									</SPAN>
									<SPAN class="ms-2">
										<xsl:value-of select="age"/>
									</SPAN>
								</DIV>
								<xsl:if test="relationship">
									<DIV>
										<SPAN>尋找</SPAN>
										<SPAN>
											<xsl:value-of select="relationship"/>
										</SPAN>
									</DIV>
								</xsl:if>
								<DIV>
									<xsl:for-each select="location">
										<SPAN class="me-1">
											<xsl:if test="position() = last()">
												<xsl:attribute name="class"></xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</SPAN>
									</xsl:for-each>
								</DIV>
							</DIV>
							<xsl:if test="@following">
								<DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
									<I class="fas fa-heart-circle text-primary fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<xsl:if test="relief">
			<ARTICLE class="col-md-8 border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex m-2 align-items-center">
					<DIV class="text-lg m-2 text-bold text-primary">安心認證</DIV>
					<xsl:if test="not(relief/@lastPage)">
						<BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="relief" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
					</xsl:if>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 relief">
					<xsl:for-each select="relief/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
								<DIV>
									<SPAN>
										<xsl:value-of select="nickname"/>
									</SPAN>
									<SPAN class="ms-2">
										<xsl:value-of select="age"/>
									</SPAN>
								</DIV>
								<xsl:if test="relationship">
									<DIV>
										<SPAN>尋找</SPAN>
										<SPAN>
											<xsl:value-of select="relationship"/>
										</SPAN>
									</DIV>
								</xsl:if>
								<DIV>
									<xsl:for-each select="location">
										<SPAN class="me-1">
											<xsl:if test="position() = last()">
												<xsl:attribute name="class"></xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</SPAN>
									</xsl:for-each>
								</DIV>
							</DIV>
							<xsl:if test="@following">
								<DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
									<I class="fas fa-heart-circle text-primary fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<xsl:if test="active">
			<ARTICLE class="col-md-8 border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex m-2 align-items-center">
					<DIV class="text-lg m-2 text-bold text-primary">最近活躍</DIV>
					<xsl:if test="not(active/@lastPage)">
						<BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="active" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
					</xsl:if>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 active">
					<xsl:for-each select="active/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
								<DIV>
									<SPAN>
										<xsl:value-of select="nickname"/>
									</SPAN>
									<SPAN class="ms-2">
										<xsl:value-of select="age"/>
									</SPAN>
								</DIV>
								<xsl:if test="relationship">
									<DIV>
										<SPAN>尋找</SPAN>
										<SPAN>
											<xsl:value-of select="relationship"/>
										</SPAN>
									</DIV>
								</xsl:if>
								<DIV>
									<xsl:for-each select="location">
										<SPAN class="me-1">
											<xsl:if test="position() = last()">
												<xsl:attribute name="class"></xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</SPAN>
									</xsl:for-each>
								</DIV>
							</DIV>
							<xsl:if test="@following">
								<DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
									<I class="fas fa-heart-circle text-primary fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<xsl:if test="register">
			<ARTICLE class="col-md-8 border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex m-2 align-items-center">
					<DIV class="text-lg m-2 text-bold text-primary">最新註冊</DIV>
					<xsl:if test="not(register/@lastPage)">
						<BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="register" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
					</xsl:if>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 register">
					<xsl:for-each select="register/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
								<DIV>
									<SPAN>
										<xsl:value-of select="nickname"/>
									</SPAN>
									<SPAN class="ms-2">
										<xsl:value-of select="age"/>
									</SPAN>
								</DIV>
								<xsl:if test="relationship">
									<DIV>
										<SPAN>尋找</SPAN>
										<SPAN>
											<xsl:value-of select="relationship"/>
										</SPAN>
									</DIV>
								</xsl:if>
								<DIV>
									<xsl:for-each select="location">
										<SPAN class="me-1">
											<xsl:if test="position() = last()">
												<xsl:attribute name="class"></xsl:attribute>
											</xsl:if>
											<xsl:value-of select="."/>
										</SPAN>
									</xsl:for-each>
								</DIV>
							</DIV>
							<xsl:if test="@following">
								<DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
									<I class="fas fa-heart-circle text-primary fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<ARTICLE class="d-block d-md-none text-center">
			<DIV class="d-flex flex-wrap justify-content-center mobileMode"></DIV>
			<BUTTON class="btn btn-link text-lg seeMoreBtn" id="mobileMode"  data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
		</ARTICLE>
		<SECTION class="fixed-bottom d-block d-md-none bg-light shadow m-2 bottom-2 text-lg">
			<UL class="navbar-nav flex-row mobileMode">
				<xsl:if test="vip">
					<LI class="nav-item col-3 text-center">
						<A class="nav-link cursor-pointer vipA mobileModeA" data-type="vip">VIP</A>
					</LI>
				</xsl:if>
				<LI class="nav-item col-4 text-center">
					<xsl:if test="vip">
						<xsl:attribute name="class">nav-item col-3 text-center</xsl:attribute>
					</xsl:if>
					<A class="nav-link cursor-pointer reliefA mobileModeA" data-type="relief">安心</A>
				</LI>
				<LI class="nav-item col-4 text-center">
					<xsl:if test="vip">
						<xsl:attribute name="class">nav-item col-3 text-center</xsl:attribute>
					</xsl:if>
					<A class="nav-link cursor-pointer mobileModeA" data-type="active">活躍</A>
				</LI>
				<LI class="nav-item col-4 text-center">
					<xsl:if test="vip">
						<xsl:attribute name="class">nav-item col-3 text-center</xsl:attribute>
					</xsl:if>
					<A class="nav-link cursor-pointer mobileModeA" data-type="register">新會員</A>
				</LI>
			</UL>
		</SECTION>
	</xsl:template>
</xsl:stylesheet>