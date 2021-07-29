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
			<STYLE>BODY{background: #F3F3F3 !important;} .resultIcon{font-size: 60px;}</STYLE>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container pt-9 pb-5 pt-md-10 pb-mb-5">
				<DIV class="modal fade" id="deleteModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
								</DIV>
								<DIV class="mt-3 mb-5 text-center">
									<I class="fas fa-exclamation-circle text-warning mb-1" style="font-size: 50px;"></I>
									<P class="text-warning text-bold">刪除帳號將無法再恢復，確定要刪除？</P>
								</DIV>
								<DIV class="text-center">
									<BUTTON class="btn btn-primary mx-2 confirmBtn" data-bs-dismiss="modal" type="button">確認</BUTTON>
									<BUTTON class="btn btn-secondary mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="text-center">
					<H4 class="text-primary">
						<xsl:value-of select="@title"/>
					</H4>
					<HR class="horizontal dark"/>
				</DIV>
				<DIV class="col-lg-8 mx-auto d-flex justify-content-center align-items-center">
					<DIV class="col-12 col-md-8 card card-frame">
						<DIV class="card-title"></DIV>
						<DIV class="card-body">
							<DIV class="d-flex justify-content-center align-items-center">
								<DIV class="text-bold">
									<I class="far fa-bell-on text-lg me-1" style="color: #00B900;"></I>
									<SPAN>LINE Notify 通知</SPAN>
								</DIV>
								<DIV class="ms-auto">
									<xsl:choose>
										<xsl:when test="not(@lineNotify)">
											<A class="btn btn-link m-0 px-3 text-lg" href="/notify-bot.line.me/authorize.asp">綁定</A>
										</xsl:when>
										<xsl:otherwise>
											<A class="btn btn-link m-0 px-3 text-lg" href="/notify-bot.line.me/revoke.asp">取消綁定</A>
										</xsl:otherwise>
									</xsl:choose>
								</DIV>
							</DIV>
							<DIV class="d-flex justify-content-center align-items-center">
								<DIV class="text-bold">
									<I class="far fa-user-slash text-lg me-1" style="color: #EA0606;"></I>
									<SPAN>刪除帳號</SPAN>
								</DIV>
								<DIV class="ms-auto">
									<BUTTON class="btn btn-link m-0 px-3 text-lg" data-bs-target="#deleteModal" data-bs-toggle="modal" type="button">刪除帳號</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/setting.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>