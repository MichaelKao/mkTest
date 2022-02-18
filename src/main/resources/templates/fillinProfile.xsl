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
			<LINK href="/STYLE/album.css" rel="stylesheet"/>
			<LINK href="/STYLE/editProfile.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container container py-6 py-md-7 px-3">
				<DIV class="modal fade" id="qrcodeModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex mb-3">
									<H5 class="modal-title">教學</H5>
									<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<BUTTON class="btn btn-light btn-round collapsed me-2" data-bs-target="#line" data-bs-toggle="collapse" type="button">LINE</BUTTON>
								<BUTTON class="btn btn-light btn-round" data-bs-target="#wechat" data-bs-toggle="collapse" type="button">微信WeChat</BUTTON>
								<DIV class="accordion" id="accordionRental">
									<DIV class="accordion-item mb-3">
										<DIV class="accordion-collapse collapse" data-bs-parent="#accordionRental" id="line">
											<IMG alt="qrcode" src="/IMAGE/line.png" style="max-width: 100%;"/>
										</DIV>
									</DIV>
									<DIV class="accordion-item mb-3">
										<DIV class="accordion-collapse collapse" data-bs-parent="#accordionRental" id="wechat">
											<IMG alt="qrcode" src="/IMAGE/weChat.png" style="max-width: 100%;"/>
										</DIV>
									</DIV>
								</DIV>
								<DIV class="text-center">
									<BUTTON class="btn btn-outline-dark btn-round" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="modal fade" id="cropModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title" id="modalLabel">裁切照片</H5>
								<BUTTON aria-label="Close" class="btn-close text-dark" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<P class="text-primary text-bold">註：僅能上傳 LINE 或微信的 QR code</P>
								<DIV class="imgContainer">
									<IMG alt="cropper" src="https://via.placeholder.com/200" id="image"/>
								</DIV>
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
								<BUTTON class="btn btn-primary" id="crop" type="button">完成</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="text-center mb-3">
					<DIV>
						<IMG class="logo" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/bigLOGO.svg" width="125	"/>
					</DIV>
					<DIV class="text-dark my-2">最後1步  填寫資料</DIV>
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
							<g id="Ellipse_3" data-name="Ellipse 3" transform="translate(140 0)" fill="#fff" stroke="#62636e" stroke-width="3">
								<circle cx="9" cy="9" r="9" stroke="none"></circle>
								<circle cx="9" cy="9" r="7.5" fill="none"></circle>
							</g>
							<g id="Ellipse_4" data-name="Ellipse 4" transform="translate(210 0)" fill="#468ca6" stroke="#62636e" stroke-width="3">
								<circle cx="9" cy="9" r="9" stroke="none"></circle>
								<circle cx="9" cy="9" r="7.5" fill="none"></circle>
							</g>
						</g>
					</SVG>
					<DIV class="text-center">
						<DIV class="text-sm text-dark">完整填寫資料</DIV>
						<DIV class="text-sm text-dark">開始您的約會</DIV>
					</DIV>
				</DIV>
				<DIV class="card mx-auto pt-3">
					<xsl:apply-templates select="lover"/>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT crossorigin="anonymous" src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.js" integrity="sha512-E4KfIuQAc9ZX6zW1IUJROqxrBqJXPuEcDKP6XesMdu2OV4LW7pj8+gkkyx2y646xEV7yxocPbaTtk2LQIJewXw==" referrerpolicy="no-referrer"/>
			<SCRIPT src="/SCRIPT/editProfile.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
			<xsl:if test="/document/seo/isProductionHost">
				<IMG id="1000303301_cpa_testing" src="https://ads.trafficjunky.net/tj_ads_pt?a=1000303301&amp;member_id=1003790101&amp;cb=
					[RANDOM_NUMBER]&amp;cti=[TRANSACTION_UNIQ_ID]&amp;ctv=[VALUE_OF_THE_TRANSACTION]&amp;ctd=[TRANSACTION_DESCRIPTION]"
				     width="1" height="1" border="0" />
				<SCRIPT>
					_lt('send', 'cv', {
					type: 'Conversion'
					},['de2dc0b2-f135-44ba-b7fe-31959fd7f2dd']);
				</SCRIPT>
			</xsl:if>
			<!-- Event snippet for 註冊 conversion page -->
			<SCRIPT>gtag('event', 'conversion', {'send_to': '<xsl:value-of select="/document/seo/googleAds/@id"/>/<xsl:value-of select="/document/seo/googleAds/@label"/>'});</SCRIPT>
		</BODY>
	</xsl:template>
	<xsl:template match="lover">
		<FORM action="/me.asp" class="signUpStep" method="post">
			<DIV class="card-body row pt-1 px-3">
				<DIV class="col-12 col-lg-6">
					<DIV class="form-group">
						<LABEL for="nickname">
							<I class="fad fa-mask text-lg"></I>
						</LABEL>
						<INPUT class="form-control" id="nickname" name="nickname" placeholder="暱稱" type="text" value="{nickname}"/>
					</DIV>
					<DIV class="form-group">
						<LABEL>
							<I class="fad fa-birthday-cake text-lg"></I>
						</LABEL>
						<INPUT class="form-control" readonly="" required="" type="text" value="{birthday}"/>
					</DIV>
					<DIV class="d-flex">
						<DIV class="col-6 mb-3 pe-1">
							<LABEL for="height">
								<I class="fad fa-ruler-vertical text-lg"></I>
							</LABEL>
							<INPUT class="form-control" id="height" inputmode="numeric" name="height" placeholder="身高" type="text" value="{height}"/>
						</DIV>
						<DIV class="col-6 mb-3 ps-1">
							<LABEL for="weight">
								<I class="fad fa-weight text-lg"></I>
							</LABEL>
							<INPUT class="form-control" id="weight" inputmode="numeric" name="weight" placeholder="體重" type="text" value="{weight}"/>
						</DIV>
					</DIV>
					<DIV class="form-group">
						<LABEL for="bodyType">
							<I class="fad fa-street-view text-lg"></I>
						</LABEL>
						<SELECT class="form-control" id="bodyType" name="bodyType">
							<xsl:for-each select="bodyType">
								<OPTION value="{@bodyTypeEnum}">
									<xsl:if test="@bodyTypeSelected">
										<xsl:attribute name="selected"/>
									</xsl:if>
									<xsl:value-of select="."/>
								</OPTION>
							</xsl:for-each>
						</SELECT>
					</DIV>
					<DIV class="form-group">
						<LABEL for="occupation">
							<I class="fad fa-briefcase text-lg"></I>
						</LABEL>
						<INPUT class="form-control" id="occupation" name="occupation" placeholder="職業" type="text" value="{occupation}"/>
					</DIV>
					<DIV class="form-group">
						<LABEL for="education">
							<I class="fad fa-graduation-cap text-lg"></I>
						</LABEL>
						<SELECT class="form-control" id="education" name="education">
							<xsl:for-each select="education">
								<OPTION value="{@educationEnum}">
									<xsl:if test="@educationSelected">
										<xsl:attribute name="selected"/>
									</xsl:if>
									<xsl:value-of select="."/>
								</OPTION>
							</xsl:for-each>
						</SELECT>
					</DIV>
					<DIV class="form-group">
						<LABEL for="marriage">
							<I class="fad fa-heart text-lg"></I>
						</LABEL>
						<SELECT class="form-control" id="marriage" name="marriage">
							<xsl:for-each select="marriage">
								<OPTION value="{@marriageEnum}">
									<xsl:if test="@marriageSelected">
										<xsl:attribute name="selected"/>
									</xsl:if>
									<xsl:value-of select="."/>
								</OPTION>
							</xsl:for-each>
						</SELECT>
					</DIV>
					<DIV class="form-group">
						<LABEL for="smoking">
							<I class="far fa-smoking text-lg"></I>
						</LABEL>
						<SELECT class="form-control" id="smoking" name="smoking">
							<xsl:for-each select="smoking">
								<OPTION value="{@smokingEnum}">
									<xsl:if test="@smokingSelected">
										<xsl:attribute name="selected"/>
									</xsl:if>
									<xsl:value-of select="."/>
								</OPTION>
							</xsl:for-each>
						</SELECT>
					</DIV>
					<DIV class="form-group">
						<LABEL for="drinking">
							<I class="fad fa-wine-glass-alt text-lg"></I>
						</LABEL>
						<SELECT class="form-control" id="drinking" name="drinking">
							<xsl:for-each select="drinking">
								<OPTION value="{@drinkingEnum}">
									<xsl:if test="@drinkingSelected">
										<xsl:attribute name="selected"/>
									</xsl:if>
									<xsl:value-of select="."/>
								</OPTION>
							</xsl:for-each>
						</SELECT>
					</DIV>
					<DIV class="form-group">
						<LABEL for="relationship">
							<I class="fad fa-grin-hearts text-lg"></I>
						</LABEL>
						<SELECT class="form-control" id="relationship" name="relationship">
							<xsl:for-each select="relationship">
								<OPTION value="{@relationshipEnum}">
									<xsl:if test="@relationshipSelected">
										<xsl:attribute name="selected"/>
									</xsl:if>
									<xsl:value-of select="."/>
								</OPTION>
							</xsl:for-each>
						</SELECT>
					</DIV>
					<xsl:if test="gender/@gender = 'male'">
						<DIV class="form-group">
							<LABEL for="annualIncome">
								<I class="fad fa-usd-circle text-lg"></I>
							</LABEL>
							<SELECT class="form-control" id="annualIncome" name="annualIncome">
								<xsl:for-each select="annualIncome">
									<OPTION value="{@annualIncomeID}">
										<xsl:if test="@annualIncomeSelected">
											<xsl:attribute name="selected"/>
										</xsl:if>
										<xsl:value-of select="."/>
									</OPTION>
								</xsl:for-each>
							</SELECT>
						</DIV>
					</xsl:if>
					<xsl:if test="gender/@gender = 'female'">
						<DIV class="form-group">
							<LABEL for="allowance">
								<I class="fad fa-usd-circle text-lg"></I>
							</LABEL>
							<SELECT class="form-control" id="allowance" name="allowance">
								<xsl:for-each select="allowance">
									<OPTION value="{@allowanceID}">
										<xsl:if test="@allowanceSelected">
											<xsl:attribute name="selected"/>
										</xsl:if>
										<xsl:value-of select="."/>
									</OPTION>
								</xsl:for-each>
							</SELECT>
						</DIV>
					</xsl:if>
				</DIV>
				<DIV class="col-12 col-lg-6">
					<DIV class="form-group">
						<LABEL for="aboutMe">
							<I class="fad fa-file-user text-lg"></I>
							<SPAN class="ms-1">關於我</SPAN>
						</LABEL>
						<SPAN class="ms-1 text-xs text-danger">(修改預設內容，邀約成功率提高60%！)</SPAN>
						<TEXTAREA class="form-control" id="aboutMe" name="aboutMe" rows="6">
							<xsl:value-of select="aboutMe"/>
						</TEXTAREA>
					</DIV>
					<DIV class="form-group">
						<LABEL for="idealConditions">
							<I class="fad fa-fire-alt text-lg"></I>
							<SPAN class="ms-1">理想對象</SPAN>
						</LABEL>
						<SPAN class="ms-1 text-xs text-danger">(修改預設內容，邀約成功率提高60%！)</SPAN>
						<TEXTAREA class="form-control" id="idealConditions" name="idealConditions" rows="6">
							<xsl:value-of select="idealConditions"/>
						</TEXTAREA>
					</DIV>
					<DIV class="form-group">
						<LABEL for="greeting">
							<I class="fad fa-comment-edit text-lg"></I>
							<xsl:if test="gender/@gender = 'female'">
								<SPAN class="ms-1">與男仕打招呼</SPAN>
							</xsl:if>
							<xsl:if test="gender/@gender = 'male'">
								<SPAN class="ms-1">用一句話介紹自己來打動甜心</SPAN>
							</xsl:if>
						</LABEL>
						<TEXTAREA class="form-control" id="greeting" name="greeting" rows="4">
							<xsl:value-of select="greeting"/>
						</TEXTAREA>
					</DIV>
					<DIV class="form-group">
						<LABEL>
							<I class="fab fa-line text-lg me-1"></I>
							<I class="fab fa-weixin text-lg"></I>
							<SPAN class="text-xs m-1 mb-1">上傳 QR Code，安心加入好友，保護ID資料不外洩</SPAN>
						</LABEL>
						<DIV class="uploadQrcode ">
							<LABEL>
								<INPUT accept="image/*" class="sr-only" name="image" type="file"/>
								<A class="btn btn-primary px-4 py-1 me-1 mb-0">點擊上傳</A>
							</LABEL>
							<BUTTON class="btn btn-info px-4 py-1 ms-1 mb-0" data-bs-target="#qrcodeModal" data-bs-toggle="modal" type="button">使用教學</BUTTON>
						</DIV>
						<DIV class="mt-1">
							<INPUT class="form-control" name="inviteMeAsLineFriend" readonly="" required="" style="display: none;" type="text" value="">
								<xsl:if test="inviteMeAsLineFriend">
									<xsl:attribute name="style">display: inline;</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="inviteMeAsLineFriend"/>
									</xsl:attribute>
								</xsl:if>
							</INPUT>
						</DIV>
					</DIV>
					<DIV class="form-group">
						<LABEL>
							<I class="fad fa-book-heart text-lg"></I>
							<SPAN class="ms-1">約會模式</SPAN>
						</LABEL>
						<DIV class="d-flex flex-wrap bg-gray-100 border-radius-lg p-2">
							<xsl:for-each select="service">
								<DIV class="form-check p-0">
									<INPUT class="form-check-input service" id="service{@serviceID}" type="checkbox" value="{@serviceID}">
										<xsl:if test="@serviceSelected">
											<xsl:attribute name="checked"/>
										</xsl:if>
									</INPUT>
									<LABEL class="custom-control-label tag" for="service{@serviceID}">
										<xsl:value-of select="."/>
									</LABEL>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
					<DIV class="form-group">
						<LABEL>
							<I class="fad fa-map-marker-alt text-lg"></I>
							<SPAN class="ms-1">約會地區</SPAN>
						</LABEL>
						<DIV class="d-flex flex-wrap bg-gray-100 border-radius-lg p-2">
							<xsl:for-each select="location">
								<DIV class="form-check p-0">
									<INPUT class="form-check-input location" id="location{@locationID}" type="checkbox" value="{@locationID}">
										<xsl:if test="@locationSelected">
											<xsl:attribute name="checked"/>
										</xsl:if>
									</INPUT>
									<LABEL class="custom-control-label tag" for="location{@locationID}">
										<xsl:value-of select="."/>
									</LABEL>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="mx-auto mt-2 col-6 col-lg-4">
					<BUTTON class="btn btn-round btn-dark mb-0 w-100" type="submit">完成註冊!</BUTTON>
				</DIV>
			</DIV>
		</FORM>
	</xsl:template>
</xsl:stylesheet>