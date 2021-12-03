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
			<LINK href="https://fonts.googleapis.com/css2?family=Crimson+Text:wght@600" rel="stylesheet"/>
			<LINK href="https://npmcdn.com/flickity@2/dist/flickity.css" rel="stylesheet"/>
			<LINK href="/STYLE/index.css" rel="stylesheet"/>
		</HEAD>
		<BODY class="pb-6">
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<xsl:if test="not(@signIn)">
				<HEADER class="container px-0 mx-auto d-flex align-items-lg-center">
					<input name="signIn" type="hidden" value="false"/>
					<DIV class="row pt-5">
						<DIV class="col-lg-8 p-0">
							<IMG class="top1Bg" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/index_TOP1.jpg"/>
						</DIV>
						<DIV class="col-lg-4 pt-6 pb-3 px-0 pe-1 d-flex flex-column justify-content-center align-items-center bg-dark position-relative">
							<DIV class="d-flex justify-content-center logoDiv">
								<IMG class="logo" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/LOGO.svg"/>
							</DIV>
							<DIV class="d-flex flex-column align-items-center justify-content-center mt-2">
								<DIV class="text-white text-bold text-lg">
									<SPAN class="me-3">奢華約會</SPAN>
									<SPAN>即刻見面</SPAN>
								</DIV>
								<DIV class="divider"></DIV>
								<DIV class="text-white text-bold text-lg">
									<SPAN class="robotoItalic">Luxury &amp; Efficiency</SPAN>
								</DIV>
							</DIV>
							<DIV class="d-flex justify-content-center">
								<DIV class="mt-3">
									<A class="btn btn-primary btn-round btn-sm px-5 py-2 mx-2 mb-0 imageShadow" href="/signIn.asp">登入</A>
									<A class="btn btn-primary btn-round btn-sm px-5 py-2 mx-2 mb-0 imageShadow" href="/signUp.asp" onclick="fbq('track','StartTrial');">註冊</A>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</HEADER>
				<SECTION class="container px-0 pt-7 position-relative">
					<DIV class="orangeBg">
						<DIV class="videoSlogan">孤單是現代文明病, 找到陪伴就來養蜜</DIV>
					</DIV>
					<DIV class="embedVideo">
						<IFRAME allowfullscreen="" frameborder="0" src="https://www.youtube.com/embed/LOIa7Zn2Z6A"></IFRAME>
					</DIV>
					<DIV class="robotoItalic text-primary text-lg videoSloganEng">
						<DIV class="text-center ms-xl-10">LONELINESS IS MODERN CIVILZED ILLNESS</DIV>
						<DIV class="text-right">WELCOME TO YOUNGME! LET'S CHAT &amp; DATE</DIV>
					</DIV>
				</SECTION>
				<SECTION class="mt-3 py-4 py-md-6 container meKING">
					<DIV class="text-right pe-lg-5 pe-xl-8 gentlemen">
						<H2>OUR GENTLEMEN</H2>
						<DIV>
							<A class="meBtn btn btn-dark shadow px-3 py-2" href="/signUp.asp">meKING</A>
						</DIV>
						<DIV class="text-dark text-bold intro">
							<DIV>時間比金錢還貴的您</DIV>
							<DIV>是否止步於交友軟體耗時配對?</DIV>
							<DIV>來到養蜜, 簡單三步驟開始約會</DIV>
						</DIV>
					</DIV>
					<DIV class="row mt-2 mt-md-5 justify-content-around">
						<DIV class="col-4 stepBox shadow">
							<DIV>1. 註冊</DIV>
							<DIV>介紹自己</DIV>
						</DIV>
						<DIV class="col-4 stepBox shadow">
							<DIV>2. 聊聊</DIV>
							<DIV>提出邀約</DIV>
						</DIV>
						<DIV class="col-4 stepBox shadow">
							<DIV>3. 約會</DIV>
							<DIV>心動時間</DIV>
						</DIV>
					</DIV>
				</SECTION>
				<section class="mt-2 py-4 py-md-6 container meQUEEN">
					<div class="ladies px-2 ms-sm-1 py-1 ms-md-2 ms-lg-6 ms-xl-8 p-xl-5">
						<h2>OUR LADIES</h2>
						<div>
							<a class="meBtn btn btn-primary shadow px-3 py-2" href="/signUp.asp">meQUEEN</a>
						</div>
						<div class="text-bold intro">
							<div>時間比金錢還貴的您</div>
							<div>是否止步於交友軟體耗時配對?</div>
							<div>來到養蜜, 簡單三步驟開始約會</div>
						</div>
					</div>
					<div class="row mt-2 mt-md-5 justify-content-around">
						<div class="col-4 stepBox shadow">
							<div>1. 註冊</div>
							<div>精選照片</div>
						</div>
						<div class="col-4 stepBox shadow">
							<div>2. 聊聊</div>
							<div>確認邀約</div>
						</div>
						<div class="col-4 stepBox shadow">
							<div>3. 約會</div>
							<div>紳士請客</div>
						</div>
					</div>
				</section>
			</xsl:if>
			<xsl:if test="not(@lineNotify) and @signIn">
				<DIV class="modal fade" id="lineNotifyModal" tabindex="-1">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<DIV class="mt-3 text-center">
									<I class="far fa-bell-on text-success mb-1" style="font-size: 50px;"></I>
									<H4>接收Line Notify即時通知?</H4>
									<P>這是LINE官方推出的新功能，若是有通知則可以推送到您的LINE上，第一時間收到通知消息，隱私又安全。</P>
								</DIV>
								<DIV class="text-center">
									<A class="btn btn-primary mx-2" href="/notify-bot.line.me/authorize.asp">接收</A>
									<BUTTON class="btn btn-dark mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</xsl:if>
			<xsl:if test="@signIn">
				<INPUT name="signIn" type="hidden" value="true"/>
				<INPUT name="guidance" type="hidden" value="{@guidance}"/>
				<A class="guidance d-flex align-items-center justify-content-center bg-danger fontSize25 opacity-9 shadow" data-bs-target="#guide" data-bs-toggle="modal">
					<I class="fad fa-question"></I>
				</A>
				<DIV class="modal fade" id="guide" tabindex="-1">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body p-0">
								<DIV class="d-flex">
									<BUTTON class="btn btn-link text-white ms-auto fontSize30 m-0 me-2 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<DIV class="carousel mx-auto">
									<xsl:if test="@male">
										<DIV class="carousel-cell imageShadow">
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<H3 class="text-white text-bold">WELCOME!</H3>
												<DIV class="guideTitle text-bold text-primary">歡迎來到養蜜</DIV>
												<DIV class="text-white mt-1">提升效率快速約會</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV class="text-white text-bold text-lg">1. 不用配對，直接聊天</DIV>
												<DIV class="text-white text-sm mt-1">互動前先新增一張好看的照片吧</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV>
													<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/kingTop1.jpg" width="150px"/>
												</DIV>
												<DIV class="text-white text-sm mt-1">放上個人照片</DIV>
												<DIV class="text-white text-sm mt-1">meQUEEN認識意願提高100%</DIV>
												<A class="btn btn-light mt-2 px-2 py-1 m-0" href="/profile/">
													<I class="fad fa-user-edit fontSize22 width30"></I>
													<SPAN class="ms-1">個人檔案</SPAN>
												</A>
											</DIV>
											<OL class="flickity-page-dots">
												<LI class="dot is-selected"></LI>
												<LI class="dot"></LI>
												<LI class="dot"></LI>
											</OL>
										</DIV>
										<DIV class="carousel-cell imageShadow">
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<H3 class="text-white text-bold">TIPS</H3>
												<DIV class="guideTitle text-bold text-primary">安心認證</DIV>
												<DIV class="text-white mt-1">提升效率快速約會</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV class="text-white text-bold text-lg">2. 約會安心保證</DIV>
												<DIV class="text-white text-sm mt-1">官方真人審核</DIV>
												<DIV class="text-white text-sm">增加meQUEEN信任度</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV>
													<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/kingTop2.jpg" width="150px"/>
												</DIV>
												<DIV class="text-white text-sm mt-1">獨立專區讓meQUEEN看到您</DIV>
											</DIV>
											<OL class="flickity-page-dots">
												<LI class="dot"></LI>
												<LI class="dot is-selected"></LI>
												<LI class="dot"></LI>
											</OL>
										</DIV>
										<DIV class="carousel-cell imageShadow">
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<H3 class="text-white text-bold">TIPS</H3>
												<DIV class="guideTitle text-bold text-primary">討論區</DIV>
												<DIV class="text-white mt-1">提升效率快速約會</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV class="text-white text-bold text-lg">3. 使用心得分享</DIV>
												<DIV class="text-white text-sm mt-1">站內用戶使用心得</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV>
													<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/kingTop3.jpg" width="150px"/>
												</DIV>
												<DIV class="text-white text-sm mt-1">如何成為情場高手看這裡</DIV>
											</DIV>
											<OL class="flickity-page-dots">
												<LI class="dot"></LI>
												<LI class="dot"></LI>
												<LI class="dot is-selected"></LI>
											</OL>
										</DIV>
									</xsl:if>
									<xsl:if test="@female">
										<DIV class="carousel-cell imageShadow">
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<H3 class="text-white text-bold">WELCOME!</H3>
												<DIV class="guideTitle text-bold text-primary">歡迎來到養蜜</DIV>
												<DIV class="text-white mt-1">提升效率快速約會</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV class="text-white text-bold text-lg">1. 不用配對，直接聊天</DIV>
												<DIV class="text-white text-sm mt-1">互動前先新增一張好看的照片吧</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV>
													<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/queenTop1.jpg" width="150px"/>
												</DIV>
												<DIV class="text-white text-sm mt-1">放上個人照片</DIV>
												<DIV class="text-white text-sm mt-1">meKING認識意願提高100%</DIV>
												<A class="btn btn-light mt-2 px-2 py-1 m-0" href="/profile/">
													<I class="fad fa-user-edit fontSize22 width30"></I>
													<SPAN class="ms-1">個人檔案</SPAN>
												</A>
											</DIV>
											<OL class="flickity-page-dots">
												<LI class="dot is-selected"></LI>
												<LI class="dot"></LI>
												<LI class="dot"></LI>
											</OL>
										</DIV>
										<DIV class="carousel-cell imageShadow">
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<H3 class="text-white text-bold">TIPS</H3>
												<DIV class="guideTitle text-bold text-primary">安心認證</DIV>
												<DIV class="text-white mt-1">提升效率快速約會</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV class="text-white text-bold text-lg">2. 約會安心保證</DIV>
												<DIV class="text-white text-sm mt-1">官方真人審核</DIV>
												<DIV class="text-white text-sm">增加個人帳戶信任度</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV>
													<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/queenTop2.jpg" width="150px"/>
												</DIV>
												<DIV class="text-white text-sm mt-1">獨立專區讓meKING看到您</DIV>
											</DIV>
											<OL class="flickity-page-dots">
												<LI class="dot"></LI>
												<LI class="dot is-selected"></LI>
												<LI class="dot"></LI>
											</OL>
										</DIV>
										<DIV class="carousel-cell imageShadow">
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<H3 class="text-white text-bold">TIPS</H3>
												<DIV class="guideTitle text-bold text-primary">討論區</DIV>
												<DIV class="text-white mt-1">提升效率快速約會</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV class="text-white text-bold text-lg">3. 使用心得分享</DIV>
												<DIV class="text-white text-sm mt-1">站內用戶使用心得</DIV>
											</DIV>
											<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
												<DIV>
													<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/queenTop3.jpg" width="150px"/>
												</DIV>
												<DIV class="text-white text-sm mt-1">如何與優質男士展開約會</DIV>
											</DIV>
											<OL class="flickity-page-dots">
												<LI class="dot"></LI>
												<LI class="dot"></LI>
												<LI class="dot is-selected"></LI>
											</OL>
										</DIV>
									</xsl:if>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="container px-0 px-md-3 pt-7 pt-md-8">
					<INPUT name="gender" type="hidden">
						<xsl:if test="@male">
							<xsl:attribute name="value">male</xsl:attribute>
						</xsl:if>
						<xsl:if test="@female">
							<xsl:attribute name="value">female</xsl:attribute>
						</xsl:if>
					</INPUT>
					<DIV class="text-center mb-3">
						<A class="tutorial text-bold" href="">
							<xsl:if test="@male">
								<xsl:attribute name="href">https://medium.com/@me.KING</xsl:attribute>
							</xsl:if>
							<xsl:if test="@female">
								<xsl:attribute name="href">https://medium.com/@meQUEEN</xsl:attribute>
							</xsl:if>
							<SPAN>養蜜超詳細教學點擊觀看</SPAN>
							<I class="fad fa-comment-exclamation fontSize30 bottom-1 ms-1"></I>
						</A>
					</DIV>
					<xsl:apply-templates select="area"/>
				</DIV>
			</xsl:if>
			<xsl:call-template name="footer"/>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="https://npmcdn.com/flickity@2/dist/flickity.pkgd.js"/>
			<SCRIPT src="/SCRIPT/index.js"/>
			<xsl:choose>
				<xsl:when test="@signIn">
					<SCRIPT><![CDATA[fbq('track', 'memberspageview');]]></SCRIPT>
					<SCRIPT src="/SCRIPT/websocket.js"/>
				</xsl:when>
				<xsl:otherwise>
					<SCRIPT><![CDATA[fbq('track', 'toppageview');]]></SCRIPT>
				</xsl:otherwise>
			</xsl:choose>
		</BODY>
	</xsl:template>

	<xsl:template match="area">
		<!--貴賓會員區塊-->
		<xsl:if test="vip">
			<ARTICLE class="border-radius-xl shadow-lg p-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex mb-2 mx-3 align-items-center justify-content-between">
					<DIV class="m-2 text-bold text-dark">貴賓會員</DIV>
					<DIV class="d-flex">
						<DIV class="vipPageBtn">
							<xsl:if test="vip/@hasPrev">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="vip" data-page="{vip/@hasPrev}">
									<I class="fad fa-angle-double-left me-1"></I>
									<SPAN>上一頁</SPAN>
								</BUTTON>
							</xsl:if>
							<xsl:if test="vip/@hasNext">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="vip" data-page="{vip/@hasNext}">
									<SPAN>下一頁</SPAN>
									<I class="fad fa-angle-double-right ms-1"></I>
								</BUTTON>
							</xsl:if>
						</DIV>
						<DIV>
							<BUTTON class="btn btn-link fontSize22 mx-1 m-0 p-0 refreshBtn" data-type="vip">
								<I class="fal fa-sync-alt"></I>
							</BUTTON>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 vip">
					<xsl:for-each select="vip/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md width148whileMobile" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center">
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
									<I class="fas fa-heart-circle text-pink fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<!--安心認證區塊-->
		<xsl:if test="relief">
			<ARTICLE class="border-radius-xl shadow-lg p-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex mb-2 mx-3 align-items-center justify-content-between">
					<DIV class="m-2 text-bold text-dark">安心認證</DIV>
					<DIV class="d-flex">
						<DIV class="reliefPageBtn">
							<xsl:if test="relief/@hasPrev">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="relief" data-page="{relief/@hasPrev}">
									<I class="fad fa-angle-double-left me-1"></I>
									<SPAN>上一頁</SPAN>
								</BUTTON>
							</xsl:if>
							<xsl:if test="relief/@hasNext">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="relief" data-page="{relief/@hasNext}">
									<SPAN>下一頁</SPAN>
									<I class="fad fa-angle-double-right ms-1"></I>
								</BUTTON>
							</xsl:if>
						</DIV>
						<DIV>
							<BUTTON class="btn btn-link fontSize22 mx-1 m-0 p-0 refreshBtn" data-type="relief">
								<I class="fal fa-sync-alt"></I>
							</BUTTON>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 relief">
					<xsl:for-each select="relief/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md width148whileMobile" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center">
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
									<I class="fas fa-heart-circle text-pink fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<!--最近活躍區塊-->
		<xsl:if test="active">
			<ARTICLE class="border-radius-xl shadow-lg p-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex mb-2 mx-3 align-items-center justify-content-between">
					<DIV class="m-2 text-bold text-dark">最近活躍</DIV>
					<DIV class="d-flex">
						<DIV class="activePageBtn">
							<xsl:if test="active/@hasPrev">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="active" data-page="{active/@hasPrev}">
									<I class="fad fa-angle-double-left me-1"></I>
									<SPAN>上一頁</SPAN>
								</BUTTON>
							</xsl:if>
							<xsl:if test="active/@hasNext">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="active" data-page="{active/@hasNext}">
									<SPAN>下一頁</SPAN>
									<I class="fad fa-angle-double-right ms-1"></I>
								</BUTTON>
							</xsl:if>
						</DIV>
						<DIV>
							<BUTTON class="btn btn-link fontSize22 mx-1 m-0 p-0 refreshBtn" data-type="active">
								<I class="fal fa-sync-alt"></I>
							</BUTTON>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 active">
					<xsl:for-each select="active/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md width148whileMobile" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center">
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
									<I class="fas fa-heart-circle text-pink fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<!--最新註冊區塊-->
		<xsl:if test="register">
			<ARTICLE class="border-radius-xl shadow-lg p-2 mx-auto mb-3 d-none d-md-block">
				<DIV class="d-flex mb-2 mx-3 align-items-center justify-content-between">
					<DIV class="m-2 text-bold text-dark">最新註冊</DIV>
					<DIV class="d-flex">
						<DIV class="registerPageBtn">
							<xsl:if test="register/@hasPrev">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="register" data-page="{register/@hasPrev}">
									<I class="fad fa-angle-double-left me-1"></I>
									<SPAN>上一頁</SPAN>
								</BUTTON>
							</xsl:if>
							<xsl:if test="register/@hasNext">
								<BUTTON class="btn btn-primary btn-round pageBtn mx-1 m-0 px-2 py-1" data-type="register" data-page="{register/@hasNext}">
									<SPAN>下一頁</SPAN>
									<I class="fad fa-angle-double-right ms-1"></I>
								</BUTTON>
							</xsl:if>
						</DIV>
						<DIV>
							<BUTTON class="btn btn-link fontSize22 mx-1 m-0 p-0 refreshBtn" data-type="register">
								<I class="fal fa-sync-alt"></I>
							</BUTTON>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="d-flex flex-wrap justify-content-center mx-2 register">
					<xsl:for-each select="register/section">
						<A class="position-relative m-1" href="/profile/{identifier}/">
							<IMG class="border-radius-md width148whileMobile" src="{profileImage}" width="152"/>
							<DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
								<xsl:if test="@vvip">
									<I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
								</xsl:if>
								<xsl:if test="@relief = 'true'">
									<I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
								</xsl:if>
							</DIV>
							<DIV class="position-absolute imageShadow bottom-0 left-0 right-0 mx-3 mb-1 py-0 text-bolder text-dark bg-white opacity-7 border-radius-md p-1 text-xs text-center">
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
									<I class="fas fa-heart-circle text-pink fontSize22"></I>
								</DIV>
							</xsl:if>
						</A>
					</xsl:for-each>
				</DIV>
			</ARTICLE>
		</xsl:if>
		<SECTION class="d-block d-md-none text-center">
			<DIV class="mobileModeWrap">
				<DIV class="d-flex flex-wrap justify-content-center mobileMode"></DIV>
				<DIV class="d-flex mt-3 justify-content-center">
					<DIV id="mobilePageBtn"></DIV>
					<DIV>
						<BUTTON class="btn btn-link fontSize22 mx-1 m-0 p-0 refreshBtn" id="mobileRefreshBtn">
							<I class="fal fa-sync-alt"></I>
						</BUTTON>
					</DIV>
				</DIV>
			</DIV>
		</SECTION>
		<SECTION class="fixed-bottom d-block d-md-none bg-white shadow m-2 bottom12rem text-lg">
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
					<A class="nav-link cursor-pointer activeA mobileModeA" data-type="active">活躍</A>
				</LI>
				<LI class="nav-item col-4 text-center">
					<xsl:if test="vip">
						<xsl:attribute name="class">nav-item col-3 text-center</xsl:attribute>
					</xsl:if>
					<A class="nav-link cursor-pointer registerA mobileModeA" data-type="register">新會員</A>
				</LI>
			</UL>
		</SECTION>
	</xsl:template>
</xsl:stylesheet>