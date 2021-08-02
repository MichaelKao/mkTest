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
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-7">
				<DIV class="modal fade" id="qrcodeModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">教學</H5>
								<BUTTON aria-label="Close" class="btn-close bg-dark" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<BUTTON class="btn btn-light collapsed me-2" data-bs-target="#line" data-bs-toggle="collapse" type="button">LINE</BUTTON>
								<BUTTON class="btn btn-light" data-bs-target="#wechat" data-bs-toggle="collapse" type="button">微信WeChat</BUTTON>
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

							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">
									<xsl:value-of select="@i18n-cancel"/>
								</BUTTON>
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
				<DIV class="card mt-5 mx-md-7">
					<DIV class="text-center">
						<H4 class="text-primary">
							<xsl:value-of select="@title"/>
						</H4>
						<HR class="horizontal dark"/>
					</DIV>
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
		</BODY>
	</xsl:template>
	<xsl:template match="lover">
		<FORM action="/me.asp" method="post">
			<DIV class="card-body pt-1">
				<DIV class="row mt-3">
					<DIV class="col-md-12 mb-3">
						<LABEL for="nickname">暱稱</LABEL>
						<INPUT class="form-control" id="nickname" name="nickname" required="" type="text" value="{nickname}"/>
					</DIV>
					<DIV class="form-group">
						<LABEL>生日</LABEL>
						<INPUT class="form-control" readonly="" required="" type="text" value="{birthday}"/>
					</DIV>
					<DIV class="d-flex">
						<DIV class="col-6 mb-3 pe-1">
							<LABEL for="height">身高</LABEL>
							<INPUT class="form-control" id="height" name="height" type="text" value="{height}"/>
						</DIV>
						<DIV class="col-6 mb-3 ps-1">
							<LABEL for="weight">體重</LABEL>
							<INPUT class="form-control" id="weight" name="weight" type="text" value="{weight}"/>
						</DIV>
					</DIV>
					<DIV class="col-md-12 mb-3">
						<LABEL for="occupation">職業</LABEL>
						<INPUT class="form-control" id="occupation" name="occupation" type="text" value="{occupation}"/>
					</DIV>
					<DIV class="form-group">
						<LABEL for="bodyType">體型</LABEL>
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
						<LABEL for="education">學歷</LABEL>
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
						<LABEL for="marriage">婚姻狀態</LABEL>
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
						<LABEL for="smoking">抽菸習慣</LABEL>
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
						<LABEL for="drinking">飲酒習慣</LABEL>
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
						<LABEL for="relationship">相處關係</LABEL>
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
							<LABEL for="annualIncome">年收入</LABEL>
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
							<LABEL for="allowance">期望零用金</LABEL>
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
					<DIV class="col-md-12 pe-2 mb-3">
						<DIV class="form-group mb-0">
							<LABEL for="aboutMe">關於我</LABEL>
							<TEXTAREA class="form-control" id="aboutMe" name="aboutMe" required="" rows="6">
								<xsl:value-of select="aboutMe"/>
							</TEXTAREA>
						</DIV>
					</DIV>
					<DIV class="col-md-12 pe-2 mb-3">
						<DIV class="form-group mb-0">
							<LABEL for="idealConditions">理想對象</LABEL>
							<TEXTAREA class="form-control" id="idealConditions" name="idealConditions" required="" rows="6">
								<xsl:value-of select="idealConditions"/>
							</TEXTAREA>
						</DIV>
					</DIV>
					<DIV class="col-md-12 pe-2 mb-3">
						<DIV class="form-group mb-0">
							<LABEL for="greeting">
								<xsl:if test="gender/@gender = 'female'">
									<SPAN>與男仕打招呼</SPAN>
								</xsl:if>
								<xsl:if test="gender/@gender = 'male'">
									<SPAN>用一句話介紹自己來打動甜心</SPAN>
								</xsl:if>
							</LABEL>
							<TEXTAREA class="form-control" id="greeting" name="greeting" required="" rows="4">
								<xsl:value-of select="greeting"/>
							</TEXTAREA>
						</DIV>
					</DIV>
					<xsl:if test="/document/@female">
						<DIV class="col-md-12 mb-3">
							<LABEL>LINE/微信WeChat</LABEL>
							<P class="text-xs m-1 mb-1">與男仕聯繫的通訊軟體 (擇一上傳 QR Code)</P>
							<DIV class="uploadQrcode ">
								<LABEL>
									<INPUT accept="image/*" class="sr-only" name="image" type="file"/>
									<A class="btn btn-primary p-1 me-1 mb-0">點擊上傳 QR code</A>
								</LABEL>
								<BUTTON class="btn btn-info p-1 mb-0 ms-1" data-bs-target="#qrcodeModal" data-bs-toggle="modal" type="button">如何取得 QR code?</BUTTON>
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
					</xsl:if>
					<DIV class="col-md-12 mb-3">
						<LABEL>服務標籤</LABEL>
						<DIV class="d-flex flex-wrap bg-gray-100 border-radius-lg p-2">
							<xsl:for-each select="service">
								<DIV class="form-check ms-2">
									<INPUT class="form-check-input service" id="service{@serviceID}" type="checkbox" value="{@serviceID}">
										<xsl:if test="@serviceSelected">
											<xsl:attribute name="checked"/>
										</xsl:if>
									</INPUT>
									<LABEL class="custom-control-label" for="service{@serviceID}">
										<xsl:value-of select="."/>
									</LABEL>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
					<DIV class="col-md-12 mb-3">
						<LABEL>服務地區</LABEL>
						<DIV class="d-flex flex-wrap bg-gray-100 border-radius-lg p-2">
							<xsl:for-each select="location">
								<DIV class="form-check ms-2">
									<INPUT class="form-check-input location" id="location{@locationID}" type="checkbox" value="{@locationID}">
										<xsl:if test="@locationSelected">
											<xsl:attribute name="checked"/>
										</xsl:if>
									</INPUT>
									<LABEL class="custom-control-label" for="location{@locationID}">
										<xsl:value-of select="."/>
									</LABEL>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="text-center">
					<BUTTON class="btn btn-round bg-gradient-primary mb-0" type="submit">
						<xsl:value-of select="@i18n-submit"/>
					</BUTTON>
				</DIV>
			</DIV>
		</FORM>
	</xsl:template>
</xsl:stylesheet>