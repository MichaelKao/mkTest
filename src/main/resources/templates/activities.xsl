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
			<LINK href="/STYLE/loading.css" rel="stylesheet"/>
			<STYLE>body {background-color: #F3F3F3;}</STYLE>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container pt-7 pt-md-8">
				<H4 class="text-primary text-center">
					<xsl:value-of select="@title"/>
				</H4>
				<DIV class="modal fade" id="weChatModel">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<DIV class="my-3 text-center">
									<I class="fad fa-user-plus mb-1" style="font-size: 50px;"></I>
									<H5 class="modal-title">微信 WeCaht QRcode</H5>
								</DIV>
								<DIV class="form-group">
									<DIV class="text-center">使用微信 APP 掃描加入好友</DIV>
									<DIV class="text-center">若是用手機，需以截圖或下載 QRCode 的方式使用微信 APP 加入好友</DIV>
									<DIV class="text-center text-primary">點擊 QRcode 可直接下載</DIV>
									<A class="weChatQRcode d-flex justify-content-center" href="" download="weChatQRcode.png">
										<IMG alt="weChatQRCode" class="weChatQRcode" src=""/>
									</A>
								</DIV>
								<DIV class="text-center">
									<BUTTON class="btn btn-outline-dark px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="modal fade" id="rateModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<DIV class="mt-3 text-center">
									<I class="far fa-comment-alt-smile text-success mb-1" style="font-size: 50px;"></I>
									<H5 class="modal-title">給予對方評價</H5>
								</DIV>
								<DIV class="form-group mx-auto col-10">
									<DIV class="rating d-flex flex-row-reverse justify-content-center">
										<INPUT class="d-none" id="rating-5" name="rating" type="radio" value="5"/>
										<LABEL for="rating-5"></LABEL>
										<INPUT class="d-none" id="rating-4" name="rating" type="radio" value="4"/>
										<LABEL for="rating-4"></LABEL>
										<INPUT class="d-none" id="rating-3" name="rating" type="radio" value="3"/>
										<LABEL for="rating-3"></LABEL>
										<INPUT class="d-none" id="rating-2" name="rating" type="radio" value="2"/>
										<LABEL for="rating-2"></LABEL>
										<INPUT class="d-none" id="rating-1" name="rating" type="radio" value="1"/>
										<LABEL for="rating-1"></LABEL>
									</DIV>
									<TEXTAREA class="form-control" name="comment" placeholder="留下評價..." type="text"></TEXTAREA>
								</DIV>
								<DIV class="text-center">
									<BUTTON class="btn btn-outline-primary commentBtn mx-1 px-3 py-2" type="button">
										<xsl:value-of select="@i18n-confirm"/>
									</BUTTON>
									<BUTTON class="btn btn-outline-dark mx-1 px-3 py-2" data-bs-dismiss="modal" type="button">
										<xsl:value-of select="@i18n-cancel"/>
									</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="d-flex flex-column flex-md-row flex-wrap justify-content-center align-items-center mt-3 activities">
					<INPUT name="nextPage" type="hidden" value="1"/>
					<xsl:for-each select="history">
						<DIV class="col-12 col-lg-8 card card-frame mb-2">
							<DIV class="card-body d-flex align-items-center justify-content-start py-2">
								<DIV>
									<A href="/profile/{@identifier}/">
										<IMG alt="profile_photo" class="border-radius-md" src="{@profileImage}" width="55"/>
									</A>
								</DIV>
								<DIV class="ms-4 w-100 d-flex flex-column flex-md-row">
									<DIV>
										<SPAN class="text-xs font-weight-bold my-2">
											<xsl:value-of select="@time"/>
										</SPAN>
										<DIV class="text-dark text-xs text-bold">
											<xsl:value-of select="@message"/>
										</DIV>

									</DIV>
									<xsl:if test="@pixAuthBtn">
										<DIV class="ms-0 ms-md-auto d-flex align-items-center my-1">
											<INPUT name="whom" type="hidden" value="{@identifier}"/>
											<BUTTON class="btn btn-sm btn-outline-primary px-2 py-1 p-md-2 m-0 me-1 acceptPixAuth" type="button">同意</BUTTON>
										</DIV>
									</xsl:if>
								</DIV>
							</DIV>
						</DIV>
					</xsl:for-each>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/activeLogs.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>