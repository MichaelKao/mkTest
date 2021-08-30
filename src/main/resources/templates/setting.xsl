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
			<DIV class="container py-8 px-3">
				<DIV class="modal fade" id="deleteModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
									<I class="fal fa-times"></I>
								</BUTTON>
								<DIV class="mt-3 mb-5 text-center">
									<I class="fas fa-exclamation-circle text-warning mb-1" style="font-size: 50px;"></I>
									<P class="text-warning text-bold">刪除帳號將無法再恢復，確定要刪除？</P>
								</DIV>
								<DIV class="text-center">
									<BUTTON class="btn btn-primary mx-2 confirmBtn" type="button">確認</BUTTON>
									<BUTTON class="btn btn-secondary mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="modal fade" id="pwdModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<DIV class="row align-items-center my-3">
									<DIV class="col-1">
										<I class="fas fa-key text-lg"></I>
									</DIV>
									<DIV class="col-11">
										<INPUT class="form-control" name="password" placeholder="輸入新密碼" required="" type="password"/>
									</DIV>
								</DIV>
								<DIV class="d-flex">
									<DIV class="ms-auto">
										<BUTTON class="btn btn-primary mx-2 resetPwdBtn px-3 py-2" type="button">確認</BUTTON>
										<BUTTON class="btn btn-secondary mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
									</DIV>
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
						<DIV class="card-body p-3">
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
							<HR/>
							<DIV>
								<INPUT name="country" type="hidden" val="{@country}"/>
								<INPUT name="login" type="hidden" val="{@login}"/>
								<DIV class="d-flex justify-content-center align-items-center">
									<DIV class="text-bold">
										<I class="far fa-key text-lg text-danger me-1"></I>
										<SPAN>重設密碼</SPAN>
									</DIV>
									<DIV class="ms-auto">
										<BUTTON class="btn btn-link m-0 px-3 text-lg" data-bs-target="#pwdModal" data-bs-toggle="modal" type="button">重設密碼</BUTTON>
									</DIV>
								</DIV>
								<DIV class="d-flex justify-content-center align-items-center">
									<DIV class="text-bold">
										<I class="far fa-user-slash text-lg text-danger me-1"></I>
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
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/setting.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>