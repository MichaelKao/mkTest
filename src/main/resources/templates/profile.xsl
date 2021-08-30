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
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-7 py-md-8">
				<DIV class="row justify-content-center">
					<xsl:apply-templates select="lover"/>
				</DIV>
				<xsl:if test="@male and not(@me)">
					<DIV class="modal fade" id="giftModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<I class="fad fa-taxi text-success mb-1" style="font-size: 50px;"></I>
										<H5 class="modal-title">車馬費</H5>
									</DIV>
									<DIV class="form-group text-center">
										<LABEL class="text-xs" for="fare">使用平台支付不必擔心私下給甜心爽約，可檢舉查證屬實退回</LABEL>
										<INPUT class="form-control" id="fare" min="1" name="howMany" required="" type="number"/>
									</DIV>
									<DIV class="text-center">
										<BUTTON class="btn btn-secondary mx-1" data-bs-dismiss="modal" type="button">
											<xsl:value-of select="@i18n-cancel"/>
										</BUTTON>
										<BUTTON class="btn btn-primary confirmBtn mx-1" type="button">
											<xsl:value-of select="@i18n-confirm"/>
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
										<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
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
				<xsl:if test="@male and not(@me)">
					<DIV class="modal fade" id="weChatModel">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-header">
									<H5 class="modal-title">微信 WeChat QRcode</H5>
									<BUTTON aria-label="Close" class="btn-close bg-dark" data-bs-dismiss="modal" type="button"></BUTTON>
								</DIV>
								<DIV class="modal-body">
									<DIV class="form-group">
										<P>使用微信 APP 掃描加入好友</P>
										<P>若是用手機，需以截圖或下載 QRCode 的方式使用微信 APP 加入好友</P>
										<P class="text-primary">點擊 QRcode 可直接下載</P>
										<A class="weChatQRcode" href="" download="weChatQRcode.png">
											<IMG alt="weChatQRCode" class="weChatQRcode" src=""/>
										</A>
									</DIV>
								</DIV>
								<DIV class="modal-footer">
									<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">
										<xsl:value-of select="@i18n-cancel"/>
									</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:if test="not(@me)">
					<DIV class="modal fade" id="modal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-header">
									<xsl:choose>
										<xsl:when test="@male">
											<H5 class="modal-title">和女生要求通訊軟體</H5>
										</xsl:when>
										<xsl:otherwise>
											<H5 class="modal-title">和男生打招呼</H5>
										</xsl:otherwise>
									</xsl:choose>
									<BUTTON aria-label="Close" class="btn-close bg-dark" data-bs-dismiss="modal" type="button"></BUTTON>
								</DIV>
								<DIV class="modal-body">
									<DIV class="form-group col-8">
										<xsl:choose>
											<xsl:when test="@male">
												<LABEL class="h6" for="hello">請用一句話打動甜心</LABEL>
											</xsl:when>
											<xsl:otherwise>
												<LABEL class="h6" for="hello">招呼語</LABEL>
											</xsl:otherwise>
										</xsl:choose>
										<TEXTAREA class="form-control" id="hello" name="what" type="text">
											<xsl:value-of select="lover/greeting"/>
										</TEXTAREA>
									</DIV>
								</DIV>
								<DIV class="modal-footer">
									<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">
										<xsl:value-of select="@i18n-cancel"/>
									</BUTTON>
									<BUTTON class="btn btn-primary confirmBtn" type="submit">
										<xsl:value-of select="@i18n-confirm"/>
									</BUTTON>
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
										<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
									</DIV>
									<DIV class="mt-3">
										<I class="fas fa-exclamation-circle text-info mb-1" style="font-size: 50px;"></I>
										<P class="text-bold">1. 對方不會知道您封鎖他</P>
										<P class="text-bold">2. 對方瀏覽您的網頁，會出現您的帳戶已關閉</P>
									</DIV>
									<DIV>
										<BUTTON class="btn btn-primary block" type="button">確認</BUTTON>
										<BUTTON class="btn btn-secondary mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
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
								<DIV class="modal-header">
									<DIV class="modal-title h5">
										<IMG alt="relief" src="/accept.svg" width="30"/>
										<SPAN class="ms-1">手持證件安心認證</SPAN>
									</DIV>
									<BUTTON aria-label="Close" class="btn-close bg-dark" data-bs-dismiss="modal" type="button"></BUTTON>
								</DIV>
								<DIV class="modal-body">
									<P class="text-primary text-bold">上傳本人自拍並且手持證件，通過安心認證增加真實性！</P>
									<P class="text-primary text-bold">此照片不會對外顯示，請安心上傳。</P>
									<LABEL>
										<INPUT accept="image/*" class="sr-only" name="image" type="file"/>
										<A class="btn btn-outline-info">上傳手持證件</A>
									</LABEL>
									<DIV class="result"></DIV>
									<DIV class="progress-wrapper">
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
								<DIV class="modal-footer">
									<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
									<BUTTON class="btn btn-primary" id="cropBtn" type="button" style="display: none;">上傳</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="modal fade" id="referralCodeModal" tabindex="-1">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<I class="fal fa-user-friends text-success mb-1" style="font-size: 50px;"></I>
										<H4>好友邀請碼</H4>
										<DIV class="d-flex justify-content-center">
											<DIV class="h1 text-lighter" id="referralCode">
												<xsl:value-of select="@referralCode"/>
											</DIV>
											<BUTTON class="btn btn-link m-0 p-0 text-lg ms-2 text-dark" id="referralCodeCopy">
												<I class="far fa-copy"></I>
											</BUTTON>
										</DIV>
									</DIV>
									<DIV class="text-center">
										<BUTTON class="btn btn-secondary mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
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
										<I class="fal fa-times text-warning mb-1" style="font-size: 50px;"></I>
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
		<DIV class="col-md-6 col-lg-5">
			<DIV class="carousel slide" data-bs-ride="carousel" id="carousel">
				<DIV class="carousel-indicators">
					<BUTTON aria-current="true" type="button" data-bs-target="#carousel" data-bs-slide-to="0" class="active"></BUTTON>
					<xsl:for-each select="picture">
						<BUTTON data-bs-slide-to="{position()}" data-bs-target="#carousel" type="button"></BUTTON>
					</xsl:for-each>
				</DIV>
				<DIV class="carousel-inner">
					<DIV class="carousel-item active">
						<IMG class="d-block w-100" src="{profileImage}" alt="大頭照"/>
					</DIV>
					<xsl:for-each select="picture">
						<DIV class="carousel-item">
							<IMG class="d-block w-100 border-radius-xl" src="{.}" alt="照片{position()}">
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
											<xsl:if test="not(/document/@waitForAuth)">取得授權</xsl:if>
											<xsl:if test="/document/@waitForAuth">
												<xsl:attribute name="disabled">true</xsl:attribute>
												<xsl:text>等待授權</xsl:text>
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
					<A class="text-pink" href="/album.asp">
						<I class="fad fa-camera fontSize35"></I>
					</A>
				</xsl:if>
			</DIV>
			<DIV class="d-flex">
				<xsl:if test="not(/document/@me) and not(/document/@blocking) and not(/document/@blockedBy)">
					<DIV class="d-flex justify-content-center justify-content-md-start" id="icon">
						<DIV>
							<BUTTON type="button">
								<xsl:choose>
									<xsl:when test="/document/follow">
										<xsl:attribute name="class">btn btn-icon-only btn-link fav liked</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="class">btn btn-icon-only btn-link fav</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
								<I class="fad fa-heart fontSize35"></I>
							</BUTTON>
						</DIV>
						<DIV>
							<A class="btn btn-icon-only btn-link ms-4" href="/chatroom/{@identifier}/">
								<I class="fad fa-comment-dots fontSize35"></I>
							</A>
						</DIV>
						<xsl:if test="/document/@male">
							<DIV>
								<BUTTON class="btn btn-icon-only btn-link ms-4 gift" type="button">
									<I class="fad fa-taxi fontSize35"></I>
								</BUTTON>
							</DIV>
						</xsl:if>
						<DIV>
							<A class="btn btn-icon-only btn-link ms-4" data-bs-toggle="dropdown">
								<I class="fad fa-exclamation-circle fontSize35"></I>
							</A>
							<DIV class="dropdown-menu shadow">
								<BUTTON class="dropdown-item">檢舉對方</BUTTON>
								<BUTTON class="dropdown-item" data-bs-target="#blockModal" data-bs-toggle="modal">封鎖對方</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<xsl:if test="/document/@me">
					<DIV class="ms-md-auto ms-md-5">
						<A class="btn btn-link m-2 p-0 text-pink" href="/me.asp">
							<I class="fad fa-pen fontSize35"></I>
						</A>
					</DIV>
				</xsl:if>
			</DIV>
			<xsl:if test="rate">
				<DIV class="moreThan768 mt-3">
					<DIV class="rateBox">
						<xsl:for-each select="rate">
							<DIV class="card flex-row px-3 py-2 mb-2 align-items-center">
								<DIV class="col-3 col-sm-2">
									<IMG class="border-radius-sm" src="{@profileImage}" width="50"/>
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
											<SPAN>升級 VIP 查看</SPAN>
										</xsl:otherwise>
									</xsl:choose>
								</DIV>
								<xsl:if test="/document/@identifier = @identifier">
									<I class="fad fa-edit fontSize22 col-1 position-absolute top-1 right-1 cursor-pointer" data-bs-target="#rateModal" data-bs-toggle="modal"></I>
								</xsl:if>
							</DIV>
						</xsl:for-each>
					</DIV>
					<DIV class="modal fade" id="rateModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-body">
									<DIV class="d-flex">
										<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
									</DIV>
									<DIV class="mt-3 text-center">
										<I class="far fa-comment-alt-smile text-success mb-1" style="font-size: 50px;"></I>
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
										<BUTTON class="btn btn-secondary mx-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
										<BUTTON class="btn btn-primary commentBtn mx-1" type="button">確認</BUTTON>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
					<BUTTON class="btn btn-link p-1 mb-0 moreRate" data-page="0">更多評價...</BUTTON>
				</DIV>
			</xsl:if>
		</DIV>
		<DIV class="col-md-6 col-lg-5">
			<SECTION class="my-3">
				<DIV class="d-flex align-items-baseline mb-1">
					<DIV class="me-2">
						<xsl:if test="gender/@gender = 'male'">
							<I class="fad fa-chess-king-alt fontSize35 text-info"></I>
						</xsl:if>
						<xsl:if test="gender/@gender = 'female'">
							<I class="fad fa-chess-queen-alt fontSize35 text-pink"></I>
						</xsl:if>
					</DIV>
					<H3 class="text-primary me-2 m-0">
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
					<xsl:if test="(/document/@me) and (not(@relief) or @relief = 'false')">
						<DIV class="mx-1">
							<xsl:if test="@relief = 'false'">
								<I class="fas fa-shield-check fontSize25 text-secondary"></I>
							</xsl:if>
							<xsl:if test="not(@relief)">
								<DIV class="cursor-pointer" data-bs-target="#cropModal" data-bs-toggle="modal">
									<I class="fas fa-shield-check fontSize25 text-secondary"></I>
								</DIV>
							</xsl:if>
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
				<xsl:if test="/document/@me">
					<DIV class="d-flex align-items-center">
						<BUTTON class="btn text-xs bg-gradient-primary px-2 py-1 m-0" data-bs-target="#referralCodeModal" data-bs-toggle="modal">邀請碼</BUTTON>
					</DIV>
				</xsl:if>
				<DIV class="text-xs my-1">
					<I class="fal fa-clock"></I>
					<SAPN class="ms-1">
						<xsl:value-of select="active"/>
					</SAPN>
				</DIV>
			</SECTION>
			<SECTION>
				<DIV class="mt-2">
					<I class="fad fa-map-marker-alt fontSize22 me-2"></I>
					<xsl:for-each select="location">
						<A class="me-1 btn btn-dark m-0 px-2 py-1" href="/search.json?location={@id}">
							<xsl:value-of select="."/>
						</A>
					</xsl:for-each>
				</DIV>
				<DIV class="mt-2">
					<I class="fad fa-book-heart fontSize22 me-2"></I>
					<xsl:for-each select="service">
						<A class="me-1 btn btn-outline-dark m-0 p-1" href="/search.json?serviceTag={@id}">
							<SPAN>#</SPAN>
							<SPAN>
								<xsl:value-of select="."/>
							</SPAN>
						</A>
					</xsl:for-each>
				</DIV>
			</SECTION>
			<DIV class="mt-3">
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
				<DIV class="mb-2">
					<I class="fad fa-grin-hearts fontSize22 col-1"></I>
					<SPAN class="ms-4">
						<xsl:value-of select="relationship"/>
					</SPAN>
				</DIV>
				<xsl:if test="gender/@gender = 'female'">
					<DIV class="mb-2">
						<I class="fad fa-usd-circle fontSize22 col-1"></I>
						<SPAN class="ms-4">
							<xsl:value-of select="allowance"/>
						</SPAN>
					</DIV>
				</xsl:if>
				<xsl:if test="gender/@gender = 'male'">
					<DIV class="mb-2">
						<I class="fad fa-usd-circle fontSize22 col-1"></I>
						<SPAN class="ms-4">
							<xsl:value-of select="annualIncome"/>
						</SPAN>
					</DIV>
				</xsl:if>
			</DIV>
			<HR class="text-pink"/>
			<DIV class="mb-2 d-flex">
				<I class="fad fa-file-user fontSize22 col-1"></I>
				<DIV class="aboutMe ms-4">
					<xsl:value-of disable-output-escaping="yes" select="aboutMe"/>
				</DIV>
			</DIV>
			<DIV class="mb-2 d-flex">
				<I class="fad fa-fire-alt fontSize22 col-1"></I>
				<DIV class="idealConditions ms-4">
					<xsl:value-of disable-output-escaping="yes" select="idealConditions"/>
				</DIV>
			</DIV>
			<DIV class="lessThan768"></DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>