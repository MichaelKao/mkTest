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
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-7">
				<DIV class="modal fade" id="modal" role="dialog" tabindex="-1">
					<DIV class="modal-dialog" role="document">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">提醒！</H5>
								<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<DIV class="form-group col-8 text-warning">刪除帳號將無法再恢復，確定要刪除？</DIV>
							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">
									<xsl:value-of select="@i18n-cancel"/>
								</BUTTON>
								<BUTTON class="btn btn-primary confirmBtn" type="button">
									<xsl:value-of select="@i18n-confirm"/>
								</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="card mx-md-7">
					<xsl:apply-templates select="lover"/>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/editProfile.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
	<xsl:template match="lover">
		<FORM action="/me.asp" method="post">
			<DIV class="card-body pt-1">
				<DIV class="d-flex">
					<A class="text-primary h2" href="/profile/">
						<I class="fad fa-chevron-double-left"></I>
					</A>
					<DIV class="ms-auto">
						<BUTTON class="btn btn-outline-primary deleteAccount" type="button">刪除帳號</BUTTON>
					</DIV>
				</DIV>
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
					<xsl:if test="/document/@female">
						<DIV class="col-md-12 mb-3">
							<LABEL for="inviteMeAsLineFriend">Line 好友連結</LABEL>
							<INPUT class="form-control" id="inviteMeAsLineFriend" name="inviteMeAsLineFriend" required="" type="text" value="{inviteMeAsLineFriend}"/>
						</DIV>
					</xsl:if>
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
							<LABEL for="greeting">打招呼</LABEL>
							<TEXTAREA class="form-control" id="greeting" name="greeting" required="" rows="4">
								<xsl:value-of select="greeting"/>
							</TEXTAREA>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="row">
					<DIV class="col-md-6 text-right ms-auto">
						<BUTTON class="btn btn-round bg-gradient-primary mb-0" type="submit">
							<xsl:value-of select="@i18n-submit"/>
						</BUTTON>
					</DIV>
				</DIV>
			</DIV>
		</FORM>
	</xsl:template>
</xsl:stylesheet>