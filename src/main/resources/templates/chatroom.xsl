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
			<LINK href="/STYLE/chatroom.css" rel="stylesheet"/>
			<LINK href="/STYLE/rateStar.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="bootstrapToast"/>
			<INPUT type="hidden" name="selfIdentifier" value="{@selfIdentifier}"/>
			<INPUT type="hidden" name="friendIdentifier" value="{@friendIdentifier}"/>
			<INPUT type="hidden" name="gender" value="{@gender}"/>
			<DIV class="modal fade" id="rateModal">
				<DIV class="modal-dialog modal-dialog-centered">
					<DIV class="modal-content">
						<DIV class="modal-body">
							<DIV class="d-flex">
								<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
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
								<BUTTON class="btn btn-secondary mx-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
								<BUTTON class="btn btn-primary commentBtn mx-1" type="button">確認</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<DIV class="modal fade" id="fareModal">
				<DIV class="modal-dialog modal-dialog-centered">
					<DIV class="modal-content">
						<DIV class="modal-body">
							<DIV class="d-flex">
								<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="mt-3 text-center">
								<I class="fad fa-taxi text-success mb-1" style="font-size: 50px;"></I>
								<H5 class="modal-title">
									<xsl:if test="@gender = 'true'">車馬費</xsl:if>
									<xsl:if test="@gender = 'false'">要求車馬費</xsl:if>
								</H5>
							</DIV>
							<DIV class="form-group text-center">
								<xsl:if test="@gender = 'true'">
									<LABEL class="text-xs" for="fare">使用平台支付不必擔心私下給甜心爽約，可檢舉查證屬實退回</LABEL>
								</xsl:if>
								<INPUT class="form-control" id="fare" min="1" name="howMany" required="" type="number"/>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-secondary mx-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
								<BUTTON class="btn btn-primary confirmFare mx-1" type="button">確認</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:if test="@gender = 'true'">
				<DIV class="modal fade" id="weChatModel">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">微信 WeCaht QRcode</H5>
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
			<DIV class="modal fade" id="blockModal">
				<DIV class="modal-dialog modal-dialog-centered">
					<DIV class="modal-content">
						<DIV class="modal-body">
							<DIV class="d-flex">
								<BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="mt-3">
								<I class="fas fa-exclamation-circle text-info mb-1" style="font-size: 50px;"></I>
								<P class="text-bold">1. 對方不會知道您封鎖他</P>
								<P class="text-bold">2. 對方瀏覽您的網頁，會出現您的帳戶已關閉</P>
							</DIV>
							<DIV>
								<BUTTON class="btn btn-primary block" type="button">確認</BUTTON>
								<BUTTON class="btn btn-secondary mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<DIV id="chatroom">
				<SECTION class="fixed-top col-12 col-md-10 mx-auto shadow">
					<DIV class="chatroomHeader d-flex align-items-center ps-2 shadow">
						<DIV class="me-3">
							<BUTTON class="btn btn-link text-primary h2 m-0 p-0 locationBack">
								<I class="fal fa-arrow-left"></I>
							</BUTTON>
						</DIV>
						<DIV>
							<A href="/profile/{@friendIdentifier}/">
								<IMG alt="profileImage" class="rounded-circle" src="{@friendProfileImage}" width="45"/>
								<SPAN class="text-lg ms-2 text-secondary">
									<xsl:value-of select="@friendNickname"/>
								</SPAN>
							</A>
						</DIV>
						<DIV class="ms-auto me-3">
							<BUTTON class="btn btn-link m-0 p-3 fare" data-bs-toggle="modal" type="button">
								<xsl:if test="@gender = 'true'">
									<xsl:attribute name="data-bs-target">#fareModal</xsl:attribute>
								</xsl:if>
								<xsl:if test="@gender = 'false'">
									<xsl:attribute name="data-bs-target">#reqFareModal</xsl:attribute>
								</xsl:if>
								<I class="fad fa-taxi font30"></I>
							</BUTTON>
							<A class="btn btn-link m-0 p-3" data-bs-toggle="dropdown">
								<I class="fal fa-ellipsis-v mb-0 font30"></I>
							</A>
							<DIV class="dropdown-menu shadow">
								<BUTTON class="dropdown-item">檢舉對方</BUTTON>
								<BUTTON class="dropdown-item" data-bs-target="#blockModal" data-bs-toggle="modal">封鎖對方</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</SECTION>
				<MAIN class="chatroom">
					<DIV class="chatroomWrapper col-12 col-md-10 mx-auto">
						<SECTION class="chatHistory d-flex flex-column justify-content-end">
							<DIV class="mb-4" id="messagesArea"></DIV>
						</SECTION>
					</DIV>
				</MAIN>
				<SECTION id="chatInputWrapper" class="fixed-bottom col-12 col-md-10 mx-auto shadow">
					<DIV class="d-flex align-items-center justify-content-center p-2 position-relative footerWrap">
						<xsl:if test="@gender = 'false'">
							<xsl:if test="@decideBtn">
								<DIV class="d-flex align-items-center justify-content-center femaleBtn floatBtn">
									<BUTTON class="btn btn-sm btn-outline-primary px-3 py-2 m-0 me-1 border-radius-xl accept" type="button">接受</BUTTON>
									<BUTTON class="btn btn-sm btn-outline-dark px-3 py-2 m-0 me-1 border-radius-xl refuse" type="button">拒絕</BUTTON>
								</DIV>
							</xsl:if>
							<xsl:if test="@rateBtn">
								<DIV class="d-flex align-items-center justify-content-center femaleBtn floatBtn">
									<BUTTON class="btn btn-sm btn-warning px-3 py-2 m-0 border-radius-xl rate" data-bs-target="#rateModal" data-bs-toggle="modal" type="button">評價</BUTTON>
								</DIV>
							</xsl:if>
						</xsl:if>
						<xsl:if test="@gender = 'true'">
							<DIV class="d-flex align-items-center justify-content-center maleBtn floatBtn">
								<xsl:choose>
									<xsl:when test="@reqSocialMediaBtn">
										<BUTTON class="btn btn-sm btn-info px-3 py-2 m-0 border-radius-xl" type="button">
											<xsl:attribute name="id">giveMeLine</xsl:attribute>
											<SPAN>要求通訊軟體</SPAN>
											<I class="far fa-user-plus ms-1"></I>
										</BUTTON>
									</xsl:when>
									<xsl:when test="@waitingForRes">
										<BUTTON class="btn btn-sm btn-info text-light px-3 py-2 m-0 border-radius-xl" disabled="" type="button">
											<SPAN>已要求通訊軟體, 等待甜心回應</SPAN>
										</BUTTON>
									</xsl:when>
									<xsl:when test="@addLineBtn">
										<BUTTON class="btn btn-sm btn-success px-3 py-2 m-0 border-radius-xl openSocialMedia" type="button">
											<SPAN>加入好友</SPAN>
											<xsl:if test="@remindDeduct">
												<DIV class="text-xxs">需 100 愛心</DIV>
											</xsl:if>
										</BUTTON>
									</xsl:when>
								</xsl:choose>
								<xsl:if test="@rateBtn">
									<BUTTON class="btn btn-sm btn-warning px-3 py-2 m-0 ms-1 border-radius-xl rate" data-bs-target="#rateModal" data-bs-toggle="modal" type="button">評價</BUTTON>
								</xsl:if>
							</DIV>
						</xsl:if>
						<xsl:if test="not(@notAbleToSendMsgs)">
							<TEXTAREA class="form-control bg-light" id="chatInput" maxlength="240" placeholder="說點什麼吧...!" rows="1"></TEXTAREA>
							<BUTTON class="btn btn-icon-only btn-link m-0 p-0 sendMsgBtn" type="button">
								<I class="fal fa-paper-plane" style="font-size: 30px;"></I>
							</BUTTON>
						</xsl:if>
						<xsl:if test="@notAbleToSendMsgs">
							<SPAN>12小時內僅能發送3句話給甜心</SPAN>
						</xsl:if>
					</DIV>
				</SECTION>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/chatroom.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocketChat.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>