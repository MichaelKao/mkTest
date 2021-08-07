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
								<DIV class="modal-header">
									<H5 class="modal-title">車馬費</H5>
									<BUTTON aria-label="Close" class="btn-close bg-dark" data-bs-dismiss="modal" type="button"></BUTTON>
								</DIV>
								<DIV class="modal-body">
									<DIV class="form-group">
										<LABEL class="text-xs" for="gift">使用平台支付不必擔心私下給甜心爽約，可檢舉查證屬實退回</LABEL>
										<INPUT class="form-control" id="gift" name="howMany" required="" type="number"/>
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
		<DIV class="col-md-5 mb-4">
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
							<IMG class="d-block w-100" src="{.}" alt="照片{position()}"/>
						</DIV>
					</xsl:for-each>
				</DIV>
				<BUTTON class="carousel-control-prev" data-bs-slide="prev" data-bs-target="#carousel" type="button">
					<SPAN aria-hidden="true" class="carousel-control-prev-icon"></SPAN>
					<SPAN class="visually-hidden">Previous</SPAN>
				</BUTTON>
				<BUTTON class="carousel-control-next" data-bs-slide="next" data-bs-target="#carousel" type="button">
					<SPAN aria-hidden="true" class="carousel-control-next-icon"></SPAN>
					<SPAN class="visually-hidden">Next</SPAN>
				</BUTTON>
			</DIV>
			<DIV class="d-flex">
				<xsl:choose>
					<xsl:when test="not(/document/@me)">
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
									<I class="fad fa-heart font40"></I>
								</BUTTON>
							</DIV>
							<DIV>
								<A class="btn btn-icon-only btn-link ms-4">
									<xsl:choose>
										<xsl:when test="/document/@male">
											<xsl:choose>
												<xsl:when test="/document/@matched">
													<xsl:attribute name="id">openLine</xsl:attribute>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="id">giveMeLine</xsl:attribute>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="id">greeting</xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
									<I class="fad fa-comment-plus font40"></I>
								</A>
							</DIV>
							<xsl:if test="/document/@male">
								<DIV>
									<BUTTON class="btn btn-icon-only btn-link mx-4 gift" type="button">
										<I class="fad fa-taxi font40"></I>
									</BUTTON>
								</DIV>
							</xsl:if>
						</DIV>
					</xsl:when>
					<xsl:otherwise>
						<DIV class="ms-md-auto ms-md-5">
							<A class="btn btn-link ms-0 m-2 p-0" href="/album.asp">
								<I class="fad fa-camera font40"></I>
							</A>
							<A class="btn btn-link m-2 p-0" href="/me.asp">
								<I class="fad fa-pen font40"></I>
							</A>
							<BUTTON class="btn btn-success p-2 m-0" data-bs-target="#referralCodeModal" data-bs-toggle="modal">我的邀請碼</BUTTON>
						</DIV>
					</xsl:otherwise>
				</xsl:choose>
			</DIV>
			<xsl:if test="rate">
				<DIV class="moreThan768 mt-3">
					<DIV class="text-primary h5">評價</DIV>
					<xsl:for-each select="rate">
						<DIV class="d-flex align-items-center">
							<DIV class="col-3">
								<IMG class="border-radius-md" src="{@profileImage}" width="50"/>
							</DIV>
							<DIV>
								<xsl:choose>
									<xsl:when test="(/document/@female) or (/document/@vip) or (/document/@me)">
										<DIV class="star text-lg" data-star="{@rate}"></DIV>
										<DIV class="text-xs">
											<xsl:value-of select="@nickname"/>
										</DIV>
										<DIV class="text-sm">
											<xsl:value-of select="@comment"/>
										</DIV>
									</xsl:when>
									<xsl:otherwise>
										<SPAN>升級 VIP 查看</SPAN>
									</xsl:otherwise>
								</xsl:choose>
							</DIV>
						</DIV>
						<HR class="horizontal dark my-2"/>
					</xsl:for-each>
				</DIV>
			</xsl:if>
		</DIV>
		<DIV class="col-md-4">
			<DIV class="d-flex align-items-center">
				<H3 class="text-primary me-2">
					<xsl:value-of select="nickname"/>
				</H3>
				<DIV class="text-dark text-bold mx-1">
					<xsl:value-of select="age"/>
				</DIV>
				<DIV class="text-dark text-bold mx-1">
					<xsl:value-of select="gender"/>
				</DIV>
				<xsl:if test="@vip">
					<DIV class="mx-1">
						<IMG class="border-radius-md" src="/vip.svg" width="32"/>
					</DIV>
				</xsl:if>
				<xsl:if test="@relief = 'true'">
					<DIV class="mx-1">
						<IMG class="border-radius-md" src="/accept.svg" width="32"/>
					</DIV>
				</xsl:if>
				<xsl:if test="@socialMedia">
					<DIV class="mx-1">
						<xsl:if test="@socialMedia = 'line'">
							<IMG class="border-radius-md" src="/line.svg" width="32"/>
						</xsl:if>
						<xsl:if test="@socialMedia = 'weChat'">
							<IMG class="border-radius-md" src="/wechat.svg" width="32"/>
						</xsl:if>
					</DIV>
				</xsl:if>
			</DIV>
			<xsl:if test="(/document/@me) and (not(@relief) or @relief = 'false')">
				<DIV class="ms-auto">
					<BUTTON class="btn btn-outline-info p-1 relief" data-bs-target="#cropModal" data-bs-toggle="modal" type="button">
						<xsl:if test="@relief = 'false'">
							<xsl:attribute name="disabled">true</xsl:attribute>
							<SPAN>安心認證審核中</SPAN>
						</xsl:if>
						<xsl:if test="not(@relief)">
							<SPAN>進行安心認證</SPAN>
						</xsl:if>
					</BUTTON>
				</DIV>
			</xsl:if>
			<DIV class="d-flex align-items-center">
				<DIV class="text-dark text-bold me-1">
					<xsl:value-of select="@i18n-lastActive"/>
				</DIV>
				<DIV class="text-primary text-bold mx-1">
					<xsl:value-of select="active"/>
				</DIV>

			</DIV>
			<DIV class="mt-2">
				<xsl:for-each select="location">
					<A class="me-1 badge bg-dark" href="/search.json?location={@id}">
						<xsl:value-of select="."/>
					</A>
				</xsl:for-each>
			</DIV>
			<DIV class="mt-2">
				<xsl:for-each select="service">
					<A class="me-1 badge bg-primary" href="/search.json?serviceTag={@id}">
						<SPAN>#</SPAN>
						<SPAN>
							<xsl:value-of select="."/>
						</SPAN>
					</A>
				</xsl:for-each>
			</DIV>
			<HR class="horizontal dark my-4"/>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">身高：</SPAN>
				<SPAN>
					<xsl:value-of select="height"/>
				</SPAN>
				<SPAN>公分</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">體重：</SPAN>
				<SPAN>
					<xsl:value-of select="weight"/>
				</SPAN>
				<SPAN>公斤</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">體型：</SPAN>
				<SPAN>
					<xsl:value-of select="bodyType"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">學歷：</SPAN>
				<SPAN>
					<xsl:value-of select="education"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">婚姻狀態：</SPAN>
				<SPAN>
					<xsl:value-of select="marriage"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">職業：</SPAN>
				<SPAN>
					<xsl:value-of select="occupation"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">抽菸習慣：</SPAN>
				<SPAN>
					<xsl:value-of select="smoking"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">飲酒習慣：</SPAN>
				<SPAN>
					<xsl:value-of select="drinking"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">相處關係：</SPAN>
				<SPAN>
					<xsl:value-of select="relationship"/>
				</SPAN>
			</DIV>
			<xsl:if test="gender/@gender = 'female'">
				<DIV class="mb-2">
					<SPAN class="font-weight-bold text-lg">期望零用金：</SPAN>
					<SPAN>
						<xsl:value-of select="allowance"/>
					</SPAN>
				</DIV>
			</xsl:if>
			<xsl:if test="gender/@gender = 'male'">
				<DIV class="mb-2">
					<SPAN class="font-weight-bold text-lg">年收入：</SPAN>
					<SPAN>
						<xsl:value-of select="annualIncome"/>
					</SPAN>
				</DIV>
			</xsl:if>
			<HR class="horizontal dark my-3"/>
			<DIV class="mb-2">
				<DIV class="font-weight-bold text-lg">關於我：</DIV>
				<DIV class="aboutMe">
					<xsl:value-of disable-output-escaping="yes" select="aboutMe"/>
				</DIV>
			</DIV>
			<DIV class="mb-2">
				<DIV class="font-weight-bold text-lg">理想中的約會對象：</DIV>
				<DIV class="idealConditions">
					<xsl:value-of disable-output-escaping="yes" select="idealConditions"/>
				</DIV>
			</DIV>
			<DIV class="lessThan768"></DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>