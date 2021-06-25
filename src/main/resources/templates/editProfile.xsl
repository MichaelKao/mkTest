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
				<DIV class="card mx-md-7">
					<xsl:apply-templates select="lover"/>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT crossorigin="anonymous" integrity="sha512-qTXRIMyZIFb8iQcfjXWCO8+M5Tbc38Qi5WzdPOYZHIlZpzBHG3L3by84BBBOiRGiEb7KKtAOAs5qYdUiZiQNNQ==" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"/>
			<SCRIPT src="/SCRIPT/bootstrap-datetimepicker.js" type="text/javascript"></SCRIPT>
			<SCRIPT src="/SCRIPT/editProfile.js"/>
		</BODY>
	</xsl:template>
	<xsl:template match="lover">
		<FORM action="/me.asp" method="post">
			<DIV class="card-body pt-1">
				<A class="text-primary text-lg" href="/profile/">
					<I class="fad fa-chevron-double-left"></I>
				</A>
				<DIV class="row mt-3">
					<DIV class="col-md-12 mb-3">
						<LABEL for="nickname">暱稱</LABEL>
						<INPUT class="form-control" id="nickname" name="nickname" type="text" value="{@nickname}"/>
					</DIV>
					<DIV class="form-group">
						<LABEL for="birth">生日</LABEL>
						<INPUT class="form-control datetimepicker" id="birth" name="birth" readonly="" required="" type="text" value="{@birthday}"/>
					</DIV>
					<DIV class="d-flex">
						<DIV class="col-6 mb-3 pe-1">
							<LABEL for="height">身高</LABEL>
							<INPUT class="form-control" id="height" name="height" type="text" value="{@height}"/>
						</DIV>
						<DIV class="col-6 mb-3 ps-1">
							<LABEL for="weight">體重</LABEL>
							<INPUT class="form-control" id="weight" name="weight" type="text" value="{@weight}"/>
						</DIV>
					</DIV>
					<DIV class="col-md-12 mb-3">
						<LABEL for="occupation">職業</LABEL>
						<INPUT class="form-control" id="occupation" name="occupation" type="text" value="{@occupation}"/>
					</DIV>
					<DIV class="col-md-12 mb-3">
						<LABEL for="lineLink">Line 好友連結</LABEL>
						<INPUT class="form-control" id="lineLink" name="lineID" type="text" value="{@lineLink}"/>
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
					<DIV class="col-md-12 pe-2 mb-3">
						<DIV class="form-group mb-0">
							<LABEL for="introduction">關於我</LABEL>
							<TEXTAREA class="form-control" id="introduction" name="introduction" rows="6">
								<xsl:value-of select="@intro"/>
							</TEXTAREA>
						</DIV>
					</DIV>
					<DIV class="col-md-12 pe-2 mb-3">
						<DIV class="form-group mb-0">
							<LABEL for="idealType">理想對象</LABEL>
							<TEXTAREA class="form-control" id="idealType" name="idealType" rows="6">
								<xsl:value-of select="@idealType"/>
							</TEXTAREA>
						</DIV>
					</DIV>
					<DIV class="col-md-12 pe-2 mb-3">
						<DIV class="form-group mb-0">
							<LABEL for="hello">打招呼</LABEL>
							<TEXTAREA class="form-control" id="hello" name="hello" rows="4">
								<xsl:value-of select="@hello"/>
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