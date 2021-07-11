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
			<LINK href="/STYLE/profile.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-7 py-md-8">
				<DIV class="row justify-content-center">
					<xsl:apply-templates select="lover"/>
				</DIV>
				<xsl:if test="@male">
					<DIV class="modal fade" id="giftModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-header">
									<H5 class="modal-title">請輸入</H5>
									<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
								</DIV>
								<DIV class="modal-body">
									<DIV class="form-group col-6">
										<LABEL class="h6" for="gift">車馬費</LABEL>
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
				<DIV class="modal fade" id="modal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<xsl:choose>
									<xsl:when test="@male">
										<H5 class="modal-title">和女生要求Line</H5>
									</xsl:when>
									<xsl:otherwise>
										<H5 class="modal-title">和男生打招呼</H5>
									</xsl:otherwise>
								</xsl:choose>
								<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<DIV class="form-group col-8">
									<LABEL class="h6" for="hello">招呼語</LABEL>
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
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/profile.js"/>
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
				<xsl:if test="/document/@me">
					<A href="/album.asp">
						<I class="fad fa-camera font40"></I>
					</A>
				</xsl:if>
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
												<xsl:when test="/document/@match">
													<xsl:attribute name="href">
														<xsl:value-of select="/document/@match"/>
													</xsl:attribute>
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
										<I class="fad fa-gift font40"></I>
									</BUTTON>
								</DIV>
							</xsl:if>
						</DIV>
					</xsl:when>
					<xsl:otherwise>
						<DIV class="ms-md-auto">
							<A class="btn btn-icon-only btn-link mx-md-4" href="/me.asp">
								<I class="fad fa-pen font40"></I>
							</A>
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
								<DIV class="star text-lg" data-star="{@rate}"></DIV>
								<DIV class="text-xs">
									<xsl:value-of select="@nickname"/>
								</DIV>
								<DIV class="text-sm">
									<xsl:choose>
										<xsl:when test="(/document/@female) or (/document/@vip) or (/document/@me)">
											<xsl:value-of select="@comment"/>
										</xsl:when>
										<xsl:otherwise>
											<SPAN>升級 VIP 查看</SPAN>
										</xsl:otherwise>
									</xsl:choose>
								</DIV>
							</DIV>
						</DIV>
						<HR class="horizontal dark my-2"/>
					</xsl:for-each>
				</DIV>
			</xsl:if>
		</DIV>
		<DIV class="col-md-4 ms-4">
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
						<IMG class="border-radius-md" src="/vip.svg" width="35"/>
					</DIV>
				</xsl:if>
			</DIV>
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
					<SPAN class="me-1 badge bg-gradient-dark">
						<xsl:value-of select="."/>
					</SPAN>
				</xsl:for-each>
			</DIV>
			<DIV class="mt-2">
				<xsl:for-each select="service">
					<SPAN class="me-1 badge bg-gradient-primary">
						<SPAN>#</SPAN>
						<SPAN>
							<xsl:value-of select="."/>
						</SPAN>
					</SPAN>
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