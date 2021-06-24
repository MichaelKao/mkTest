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
					<FORM class="p-3" id="contact-form" method="post">
						<DIV class="card-body pt-1">
							<A class="text-primary text-lg" href="/profile/">
								<I class="fad fa-chevron-double-left"></I>
							</A>
							<DIV class="row mt-3">
								<DIV class="col-md-12 mb-3">
									<LABEL for="niChen">暱稱</LABEL>
									<INPUT class="form-control" id="niChen" type="text" value="魚仔"/>
								</DIV>
								<DIV class="form-group">
									<LABEL for="datepickerUntil">生日</LABEL>
									<INPUT class="form-control datetimepicker" id="datepickerUntil" name="jieShu" readonly="" required="" type="text" value="2000-06-30"/>
								</DIV>
								<DIV class="d-flex">
									<DIV class="col-6 mb-3 pe-1">
										<LABEL for="height">身高</LABEL>
										<INPUT class="form-control" id="height" type="text" value="169"/>
									</DIV>
									<DIV class="col-6 mb-3 ps-1">
										<LABEL for="weight">體重</LABEL>
										<INPUT class="form-control" id="weight" type="text" value="52"/>
									</DIV>
								</DIV>
								<DIV class="form-group">
									<LABEL for="bodyshape">體型</LABEL>
									<SELECT class="form-control" id="bodyshape">
										<OPTION>標準型</OPTION>
										<OPTION>苗條型</OPTION>
										<OPTION>運動型</OPTION>
										<OPTION>微肉型</OPTION>
									</SELECT>
								</DIV>
								<DIV class="form-group">
									<LABEL for="school">學歷</LABEL>
									<SELECT class="form-control" id="school">
										<OPTION>國中或以下</OPTION>
										<OPTION>高中</OPTION>
										<OPTION>大專以上</OPTION>
									</SELECT>
								</DIV>
								<DIV class="form-group">
									<LABEL for="marriage">婚姻狀態</LABEL>
									<SELECT class="form-control" id="marriage">
										<OPTION>單身</OPTION>
										<OPTION>已婚</OPTION>
										<OPTION>非單身</OPTION>
									</SELECT>
								</DIV>
								<DIV class="form-group">
									<LABEL for="smoking">抽菸習慣</LABEL>
									<SELECT class="form-control" id="smoking">
										<OPTION>時常</OPTION>
										<OPTION>偶爾</OPTION>
										<OPTION>只抽社交菸</OPTION>
									</SELECT>
								</DIV>
								<DIV class="form-group">
									<LABEL for="drinking">飲酒習慣</LABEL>
									<SELECT class="form-control" id="drinking">
										<OPTION>時常</OPTION>
										<OPTION>偶爾</OPTION>
										<OPTION>只喝社交酒</OPTION>
									</SELECT>
								</DIV>
								<DIV class="col-md-12 pe-2 mb-3">
									<DIV class="form-group mb-0">
										<LABEL for="aboutMe">關於我</LABEL>
										<TEXTAREA name="message" class="form-control" id="aboutMe" rows="6">想成為你的小淘氣支出我零用的daddy如果你覺得我們有緣請與我連結💌💕</TEXTAREA>
									</DIV>
								</DIV>
								<DIV class="col-md-12 pe-2 mb-3">
									<DIV class="form-group mb-0">
										<LABEL for="type">理想對象</LABEL>
										<TEXTAREA name="message" class="form-control" id="type" rows="6">溫柔體貼💕</TEXTAREA>
									</DIV>
								</DIV>
								<DIV class="col-md-12 pe-2 mb-3">
									<DIV class="form-group mb-0">
										<LABEL for="sayHi">打招呼(預設是關於我)</LABEL>
										<TEXTAREA name="message" class="form-control" id="sayHi" rows="4">想成為你的小淘氣支出我零用的daddy如果你覺得我們有緣請與我連結💌💕</TEXTAREA>
									</DIV>
								</DIV>
							</DIV>
							<DIV class="row">
								<DIV class="col-md-6 text-right ms-auto">
									<BUTTON type="submit" class="btn btn-round bg-gradient-primary mb-0">Send Message</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</FORM>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT crossorigin="anonymous" integrity="sha512-qTXRIMyZIFb8iQcfjXWCO8+M5Tbc38Qi5WzdPOYZHIlZpzBHG3L3by84BBBOiRGiEb7KKtAOAs5qYdUiZiQNNQ==" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"/>
			<SCRIPT src="/SCRIPT/bootstrap-datetimepicker.js" type="text/javascript"></SCRIPT>
			<SCRIPT src="/SCRIPT/editProfile.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>