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
			<LINK crossorigin="anonymous" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.css" integrity="sha512-Aix44jXZerxlqPbbSLJ03lEsUch9H/CmnNfWxShD6vJBbboR+rPdDXmKN+/QjISWT80D4wMjtM4Kx7+xkLVywQ==" referrerpolicy="no-referrer" rel="stylesheet"/>
			<LINK href="/STYLE/profile.css" rel="stylesheet"/>
			<LINK href="/STYLE/rateStar.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:choose>
				<xsl:when test="@me">
					<xsl:call-template name="navbar"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="navbarWithoutBottomNav"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-6 py-md-7">
				<DIV class="col-md-12 col-lg-10 mx-auto mb-2">
					<DIV class="text-primary text-center text-lg text-bolder d-flex align-items-start">
						<I class="fad fa-arrow-circle-left fontSize35 cursor-pointer prevPageBtn"></I>
					</DIV>
				</DIV>
				<DIV class="row justify-content-center">
					<xsl:apply-templates select="lover"/>
				</DIV>
				<xsl:if test="@male and not(@me)">
					<DIV class="modal fade" id="giftModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
											<I class="fal fa-times"></I>
										</BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<I class="fad fa-badge-dollar text-success mb-1 fontSize50"></I>
										<H5 class="modal-title">透過ME點邀約</H5>
									</DIV>
									<DIV class="form-group mx-auto col-11 text-center">
										<DIV class="my-2 text-primary text-sm">
											<DIV>安心防詐騙</DIV>
											<DIV>可於48小時內向客服檢舉退回</DIV>
										</DIV>
										<DIV class="my-2 text-primary text-xs">
											<DIV>
												<I class="fad fa-taxi fontSize22 me-1"></I>
												<SPAN>率先支付約會出席費，成功率提高80%</SPAN>
											</DIV>
											<DIV>
												<I class="fad fa-gift fontSize22 me-1"></I>
												<SPAN>透過ME點做為禮物發送，好感度增加90%</SPAN>
											</DIV>
										</DIV>
										<INPUT class="form-control" id="fare" inputmode="numeric" min="1" name="howMany" required="" type="number"/>
										<xsl:if test="@male">
											<DIV>
												<I class="fad fa-gift fontSize22 me-1"></I>
												<h9 class="text-secondary">確定邀約即表示您已閱讀並同意
												</h9>
												<div>
													<A class="text-primary" href="/termsInvite.asp">服務條款</A>
												</div>
											</DIV>
										</xsl:if>
									</DIV>
									<DIV class="text-center">
										<BUTTON class="btn btn-outline-primary confirmFareBtn mx-1" type="button">
											<xsl:value-of select="@i18n-confirm"/>
										</BUTTON>
										<BUTTON class="btn btn-outline-dark mx-1" data-bs-dismiss="modal" type="button">
											<xsl:value-of select="@i18n-cancel"/>
										</BUTTON>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:if test="@male and @lockedPix">
					<DIV class="modal fade" id="upgradeVIP">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
											<I class="fal fa-times"></I>
										</BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<I class="fad fa-crown fontSize25 text-yellow"></I>
										<H5 class="modal-title my-3">需升級貴賓才能取得甜心授權</H5>
									</DIV>
									<DIV class="text-center">
										<A class="btn btn-outline-primary btn-round mx-1" href="/upgrade.asp">馬上升級</A>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:if test="not(@me)">
					<DIV class="modal fade" id="blockModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
											<I class="fal fa-times"></I>
										</BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<I class="fas fa-exclamation-circle text-info mb-2 fontSize50"></I>
										<P class="text-dark">1. 對方不會知道您封鎖他</P>
										<P class="text-dark">2. 對方瀏覽您的網頁，會出現您的帳戶已關閉</P>
									</DIV>
									<DIV class="text-center">
										<BUTTON type="button" class="btn btn-outline-primary px-3 py-2 block">確認</BUTTON>
										<BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:if test="@me">
					<DIV class="modal fade" id="cropModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
											<I class="fal fa-times"></I>
										</BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<DIV class="d-flex align-items-center justify-content-center">
											<I class="fad fa-shield-check fontSize25"></I>
											<SPAN class="text-primary text-bold">通過安心認證，增加邀約成功率</SPAN>
										</DIV>
										<DIV class="text-dark text-sm text-bold mt-1">上傳一張您與證件的自拍吧!</DIV>
										<DIV class="text-dark text-sm text-bold">證件與本人需可清楚辨識</DIV>
										<DIV class="position-relative">
											<DIV class="example">範例</DIV>
											<DIV>
												<IMG src="https://static.youngme.vip/IMAGE/reliefExample.png" width="200"/>
											</DIV>
										</DIV>
										<DIV class="text-dark text-xs my-1">由真人審核，需等待3-5天</DIV>
										<LABEL class="cursor-pointer mx-0 my-1">
											<INPUT accept="image/*" class="sr-only" name="image" type="file"/>
											<I class="fad fa-camera fontSize35 text-primary"></I>
										</LABEL>
										<DIV class="text-primary text-bold text-xs mb-1">
											<DIV>照片僅供系統認證使用，</DIV>
											<DIV>不會外流，請安心上傳。</DIV>
										</DIV>
										<DIV class="result"></DIV>
										<DIV class="progress-wrapper my-3 w-90 mx-auto">
											<DIV class="progress-info">
												<DIV class="progress-percentage">
													<span class="text-sm font-weight-bold">0%</span>
												</DIV>
											</DIV>
											<DIV class="progress">
												<DIV class="progress-bar bg-primary" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></DIV>
											</DIV>
										</DIV>
									</DIV>
									<DIV class="text-center">
										<BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
										<BUTTON class="btn btn-outline-primary px-3 py-2" id="cropBtn" type="button" style="display: none;">上傳</BUTTON>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:if test="@blocking or @blockedBy">
					<DIV class="modal fade show" id="blockedModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="mt-3 text-center">
										<I class="fal fa-times text-warning mb-1 fontSize50"></I>
										<H5 class="modal-title">
											<xsl:if test="@blocking">此用戶已被您封鎖</xsl:if>
											<xsl:if test="@blockedBy">此用戶已不存在</xsl:if>
										</H5>
									</DIV>
									<DIV class="text-center">
										<A class="btn btn-link text-dark" href="/">回首頁</A>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/profile.js"/>
			<SCRIPT crossorigin="anonymous" src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.js" integrity="sha512-E4KfIuQAc9ZX6zW1IUJROqxrBqJXPuEcDKP6XesMdu2OV4LW7pj8+gkkyx2y646xEV7yxocPbaTtk2LQIJewXw==" referrerpolicy="no-referrer"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
	<xsl:template match="lover">
		<INPUT name="whom" type="hidden" value="{@identifier}"/>
		<DIV class="col-md-6 col-lg-5 mb-2 px-0 px-md-2">
			<DIV class="carousel slide" data-bs-ride="carousel" id="carousel">
				<DIV class="carousel-indicators">
					<BUTTON aria-current="true" type="button" data-bs-target="#carousel" data-bs-slide-to="0" class="active"></BUTTON>
					<xsl:for-each select="picture">
						<BUTTON data-bs-slide-to="{position()}" data-bs-target="#carousel" type="button"></BUTTON>
					</xsl:for-each>
				</DIV>
				<DIV class="carousel-inner">
					<DIV class="carousel-item active profileImage">
						<IMG class="d-block w-100 border-radius-md" src="{profileImage}" alt="大頭照"/>
					</DIV>
					<xsl:for-each select="picture">
						<DIV class="carousel-item">
							<IMG class="d-block w-100" src="{.}" alt="照片{position()}">
								<xsl:if test="/document/@unlockedPix">
									<xsl:attribute name="src">
										<xsl:value-of select="/document/@unlockedPix"/>
										<xsl:value-of select="."/>
									</xsl:attribute>
								</xsl:if>
								<xsl:if test="/document/@lockedPix">
									<xsl:attribute name="src">
										<xsl:value-of select="/document/@lockedPix"/>
										<xsl:value-of select="."/>
										<xsl:text>.png</xsl:text>
									</xsl:attribute>
								</xsl:if>
							</IMG>
							<xsl:if test="/document/@lockedPix">
								<DIV class="position-absolute bottom-50 right-0 left-0 mx-auto w-50">
									<DIV class="text-center">
										<BUTTON class="btn btn-dark btn-round m-0 picturesAuth" type="button">
											<xsl:if test="not(/document/@waitForAuth)">要求查看</xsl:if>
											<xsl:if test="/document/@waitForAuth">
												<xsl:attribute name="disabled">true</xsl:attribute>
												<xsl:text>等待回覆</xsl:text>
											</xsl:if>
										</BUTTON>
									</DIV>
								</DIV>
							</xsl:if>
						</DIV>
					</xsl:for-each>
				</DIV>
				<BUTTON class="carousel-control-prev" data-bs-slide="prev" data-bs-target="#carousel" type="button">
					<SPAN class="carouselControlBtn">
						<I class="fal fa-chevron-left"></I>
					</SPAN>
					<SPAN class="visually-hidden">Previous</SPAN>
				</BUTTON>
				<BUTTON class="carousel-control-next" data-bs-slide="next" data-bs-target="#carousel" type="button">
					<SPAN class="carouselControlBtn">
						<I class="fal fa-chevron-right"></I>
					</SPAN>
					<SPAN class="visually-hidden">Next</SPAN>
				</BUTTON>
				<xsl:if test="/document/@me">
					<A class="btn btn-link m-0 p-0" href="/album.asp">
						<I class="fad fa-camera fontSize35"></I>
					</A>
				</xsl:if>
			</DIV>
			<xsl:if test="/document/@me">
				<div class="text-sm text-danger text-bold text-center">
					<I class="far fa-lightbulb-on me-1"></I>
					<SPAN>真人照片邀約成功率提高90%</SPAN>
				</div>
			</xsl:if>
			<DIV class="d-flex">
				<xsl:if test="not(/document/@me) and not(/document/@blocking) and not(/document/@blockedBy)">
					<DIV class="d-flex mt-md-2 justify-content-center justify-content-md-start" id="icon">
						<DIV>
							<BUTTON onclick="fbq('track', 'Collect');" type="button">
								<xsl:choose>
									<xsl:when test="/document/follow">
										<xsl:attribute name="class">btn btn-link fav liked mb-0 p-0</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">btn btn-link fav mb-0 p-0</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<I class="fad fa-heart-circle fontSize30"></I>
								<DIV>收藏</DIV>
							</BUTTON>
						</DIV>
						<DIV>
							<A class="btn btn-link ms-4 mb-0 p-0" href="/chatroom/{@identifier}/" onclick="fbq('track', 'StartChat');">
								<I class="fas fa-comments fa-beat fontSize30"></I>
								<DIV>聊天</DIV>
							</A>
						</DIV>
						<xsl:if test="/document/@male">
							<DIV>
								<BUTTON class="btn btn-link ms-4 gift mb-0 p-0" data-bs-target="#giftModal" data-bs-toggle="modal" onclick="fbq('track', 'SendAGift');" type="button">
									<I class="fas fa-badge-dollar fontSize30"></I>
									<DIV>邀約</DIV>
								</BUTTON>
							</DIV>
						</xsl:if>
						<!--						<DIV>
							<BUTTON class="btn btn-link ms-4 mb-0 p-0">
								<I class="fas fa-lightbulb-exclamation fontSize30"></I>
								<DIV>檢舉</DIV>
							</BUTTON>
						</DIV>-->
						<DIV>
							<BUTTON class="btn btn-link ms-3 me-1 mb-0 p-0" data-bs-target="#blockModal" data-bs-toggle="modal">
								<I class="fas fa-user-slash fontSize30"></I>
								<DIV>封鎖</DIV>
							</BUTTON>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:if test="/document/@me">
					<DIV class="ms-auto mt-1">
						<xsl:if test="not(@relief)">
							<BUTTON class="btn btn-link mx-2 my-0 p-0 text-dark reliefBtn" data-bs-target="#cropModal" data-bs-toggle="modal">
								<I class="fad fa-shield-check fontSize30"></I>
								<DIV>安心認證</DIV>
							</BUTTON>
						</xsl:if>
						<A class="btn btn-link mx-2 my-0 p-0 text-dark" href="/me.asp">
							<I class="fad fa-pen fontSize30"></I>
							<DIV>編輯檔案</DIV>
						</A>
					</DIV>
				</xsl:if>
			</DIV>
			<xsl:if test="rate">
				<DIV class="moreThan768 mt-3">
					<DIV class="rateBox">
						<DIV class="card">
							<xsl:for-each select="rate">
								<DIV class="d-flex flex-row px-3 py-2 align-items-center">
									<xsl:if test="position() mod 2 = 0">
										<xsl:attribute name="style">background: #F3F3F3AA;</xsl:attribute>
									</xsl:if>
									<DIV class="col-3 col-sm-2 blurImg">
										<INPUT class="hiddenImg" type="hidden" value="{@profileImage}"/>
									</DIV>
									<DIV class="ms-2">
										<xsl:choose>
											<xsl:when test="(/document/@female) or (/document/@vip) or (/document/@vvip) or (/document/@me)">
												<DIV class="text-xs">
													<xsl:value-of select="@nickname"/>
												</DIV>
												<DIV class="star text-lg" data-star="{@rate}">
													<xsl:if test="/document/@identifier = @identifier">
														<xsl:attribute name="class">star text-lg selfStar</xsl:attribute>
													</xsl:if>
												</DIV>
												<DIV class="text-sm">
													<xsl:if test="/document/@identifier = @identifier">
														<xsl:attribute name="class">text-sm selfComment</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="@comment"/>
												</DIV>
											</xsl:when>
											<xsl:otherwise>
												<A href="/upgrade.asp">升級 VIP 查看</A>
											</xsl:otherwise>
										</xsl:choose>
									</DIV>
									<xsl:if test="/document/@identifier = @identifier">
										<I class="fad fa-edit fontSize22 col-1 position-absolute top-1 right-1 cursor-pointer" data-bs-target="#rateModal" data-bs-toggle="modal"></I>
									</xsl:if>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
					<DIV class="modal fade" id="rateModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
											<I class="fal fa-times"></I>
										</BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<I class="far fa-comment-alt-smile text-success mb-1 fontSize50"></I>
										<H5 class="modal-title">編輯評價</H5>
									</DIV>
									<DIV class="form-group mx-auto col-10">
										<DIV class="rating d-flex flex-row-reverse justify-content-center">
											<INPUT class="d-none" id="rating-5" name="rating" type="radio" value="5">
												<xsl:if test="@rate = 5">
													<xsl:attribute name="checked">true</xsl:attribute>
												</xsl:if>
											</INPUT>
											<LABEL for="rating-5"></LABEL>
											<INPUT class="d-none" id="rating-4" name="rating" type="radio" value="4">
												<xsl:if test="@rate = 4">
													<xsl:attribute name="checked">true</xsl:attribute>
												</xsl:if>
											</INPUT>
											<LABEL for="rating-4"></LABEL>
											<INPUT class="d-none" id="rating-3" name="rating" type="radio" value="3">
												<xsl:if test="@rate = 3">
													<xsl:attribute name="checked">true</xsl:attribute>
												</xsl:if>
											</INPUT>
											<LABEL for="rating-3"></LABEL>
											<INPUT class="d-none" id="rating-2" name="rating" type="radio" value="2">
												<xsl:if test="@rate = 2">
													<xsl:attribute name="checked">true</xsl:attribute>
												</xsl:if>
											</INPUT>
											<LABEL for="rating-2"></LABEL>
											<INPUT class="d-none" id="rating-1" name="rating" type="radio" value="1">
												<xsl:if test="@rate = 1">
													<xsl:attribute name="checked">true</xsl:attribute>
												</xsl:if>
											</INPUT>
											<LABEL for="rating-1"></LABEL>
										</DIV>
										<TEXTAREA class="form-control" name="comment" placeholder="留下評價..." type="text">
											<xsl:value-of select="@comment"/>
										</TEXTAREA>
									</DIV>
									<DIV class="text-center">
										<BUTTON class="btn btn-outline-primary commentBtn mx-1 px-3 py-2" type="button">確認</BUTTON>
										<BUTTON class="btn btn-outline-dark mx-1 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
					<xsl:if test="not(@lastPage)">
						<BUTTON class="btn btn-link p-1 mb-0 moreRate" data-page="0">更多評價...</BUTTON>
					</xsl:if>
				</DIV>
			</xsl:if>
		</DIV>
		<DIV class="col-md-6 col-lg-5 p-0">
			<SECTION class="mb-2 p-3 card">
				<DIV class="d-flex align-items-baseline my-1">
					<DIV class="me-2">
						<xsl:if test="gender/@gender = 'male'">
							<I class="fad fa-chess-king-alt fontSize35 text-info"></I>
						</xsl:if>
						<xsl:if test="gender/@gender = 'female'">
							<I class="fad fa-chess-queen-alt fontSize35 text-primary"></I>
						</xsl:if>
					</DIV>
					<H3 class="text-dark me-2 m-0">
						<xsl:value-of select="nickname"/>
					</H3>
					<DIV class="text-dark text-bold mx-1">
						<xsl:value-of select="age"/>
					</DIV>
					<xsl:if test="@vvip">
						<DIV class="mx-1">
							<I class="fad fa-crown fontSize25 text-yellow"></I>
						</DIV>
					</xsl:if>
					<xsl:if test="not(@relief) or @relief = 'false'">
						<DIV class="mx-1 reliefBadge">
							<I class="fad fa-shield-check fontSize25"></I>
						</DIV>
					</xsl:if>
					<xsl:if test="@relief = 'true'">
						<DIV class="mx-1">
							<I class="fas fa-shield-check fontSize25 text-success"></I>
						</DIV>
					</xsl:if>
					<xsl:if test="@socialMedia">
						<DIV class="mx-1">
							<xsl:if test="@socialMedia = 'line'">
								<I class="fab fa-line fontSize25 text-dark"></I>
							</xsl:if>
							<xsl:if test="@socialMedia = 'weChat'">
								<I class="fab fa-weixin fontSize25 text-dark"></I>
							</xsl:if>
						</DIV>
					</xsl:if>
				</DIV>
				<DIV class="text-xs my-1">
					<I class="fal fa-clock"></I>
					<SAPN class="ms-1">
						<xsl:value-of select="active"/>
					</SAPN>
				</DIV>
			</SECTION>

			<SECTION class="mb-2 p-3 card">
				<DIV>
					<I class="fad fa-map-marker-alt fontSize22 me-2"></I>
					<xsl:for-each select="location">
						<A class="me-1 btn btn-pink m-0 px-2 py-1" href="/search.json?location={@id}">
							<SPAN>#</SPAN>
							<SPAN>
								<xsl:value-of select="."/>
							</SPAN>
						</A>
					</xsl:for-each>
				</DIV>
				<DIV class="mt-2">
					<I class="fad fa-book-heart fontSize22 me-2"></I>
					<xsl:if test="gender/@gender = 'female'">
						<div class="row">
							<xsl:for-each select="service">
								<div class="col-6 mb-2">
									<A class="me-1 btn btn-outline-pink m-0 p-1" href="/search.json?companionship={@id}">
										<SPAN>
											<xsl:value-of select="."/>
										</SPAN>
									</A>
									<i class="fad fa-badge-dollar fontSize22 width30whenMobile" aria-hidden="true"></i>
									<span><xsl:value-of select="@servicePoint"/>/<xsl:value-of select="@serviceHour"/>H</span>
								</div>
							</xsl:for-each>
						</div>
					</xsl:if>
					<xsl:if test="gender/@gender = 'male'">
						<xsl:for-each select="service">
							<A class="me-1 btn btn-outline-pink m-0 p-1" href="/search.json?companionship={@id}">
								<SPAN>#</SPAN>
								<SPAN>
									<xsl:value-of select="."/>
								</SPAN>
							</A>
						</xsl:for-each>							
					</xsl:if>
				</DIV>
			</SECTION>
			<SECTION class="mb-2 p-3 card">
				<DIV class="mb-2">
					<I class="fad fa-ruler-vertical fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<SPAN>
							<xsl:value-of select="height"/>
						</SPAN>
						<SPAN>公分</SPAN>
					</SPAN>
				</DIV>
				<DIV class="mb-2">
					<I class="fad fa-weight fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<SPAN>
							<xsl:value-of select="weight"/>
						</SPAN>
						<SPAN>公斤</SPAN>
					</SPAN>
				</DIV>
				<DIV class="mb-2">
					<I class="fad fa-street-view fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<xsl:value-of select="bodyType"/>
					</SPAN>
				</DIV>
				<DIV class="mb-2">
					<I class="fad fa-briefcase fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<xsl:value-of select="occupation"/>
					</SPAN>
				</DIV>
				<DIV class="mb-2">
					<I class="fad fa-graduation-cap fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<xsl:value-of select="education"/>
					</SPAN>
				</DIV>
				<DIV class="mb-2">
					<I class="fad fa-heart fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<xsl:value-of select="marriage"/>
					</SPAN>
				</DIV>
				<DIV class="mb-2">
					<I class="far fa-smoking fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<xsl:value-of select="smoking"/>
					</SPAN>
				</DIV>
				<DIV class="mb-2">
					<I class="fad fa-wine-glass-alt fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<xsl:value-of select="drinking"/>
					</SPAN>
				</DIV>
<!--				<DIV class="mb-2">-->
<!--					<I class="fad fa-grin-hearts fontSize22 col-1"></I>-->
<!--					<SPAN class="ms-4">-->
<!--						<xsl:value-of select="relationship"/>-->
<!--					</SPAN>-->
<!--				</DIV>-->
<!--				<xsl:if test="gender/@gender = 'female'">-->
<!--					<DIV class="mb-2">-->
<!--						<I class="fad fa-usd-circle fontSize22 col-1"></I>-->
<!--						<SPAN class="ms-4">-->
<!--							<xsl:value-of select="allowance"/>-->
<!--						</SPAN>-->
<!--					</DIV>-->
<!--				</xsl:if>-->
<!--				<xsl:if test="gender/@gender = 'male'">-->
<!--					<DIV class="mb-2">-->
<!--						<I class="fad fa-usd-circle fontSize22 col-1"></I>-->
<!--						<SPAN class="ms-4">-->
<!--							<xsl:value-of select="annualIncome"/>-->
<!--						</SPAN>-->
<!--					</DIV>-->
<!--				</xsl:if>-->
			</SECTION>
			<SECTION class="p-3 card">
				<xsl:if test="/document/@me">
					<DIV class="text-sm text-danger text-bold mb-3">
						<I class="far fa-lightbulb-on me-1"></I>
						<SPAN>修改預設內容，邀約成功率提高60%！</SPAN>
					</DIV>
				</xsl:if>

				<DIV class="d-flex">
					<I class="fad fa-file-user fontSize22 col-1"></I>
					<DIV class="aboutMe ms-4">
						<xsl:value-of disable-output-escaping="yes" select="aboutMe"/>
					</DIV>
				</DIV>
				<DIV class="d-flex">
					<I class="fad fa-fire-alt fontSize22 col-1"></I>
					<DIV class="idealConditions ms-4">
						<xsl:value-of disable-output-escaping="yes" select="idealConditions"/>
					</DIV>
				</DIV>
			</SECTION>
			<DIV class="lessThan768 mt-2"></DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>