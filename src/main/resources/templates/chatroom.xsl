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
								<BUTTON class="btn btn-outline-dark mx-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
								<BUTTON class="btn btn-outline-primary commentBtn mx-1" type="button">確認</BUTTON>
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
								<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
									<I class="fal fa-times"></I>
								</BUTTON>
							</DIV>
							<DIV class="mt-3 text-center">
								<I class="fad fa-taxi text-success mb-1" style="font-size: 50px;"></I>
								<H5 class="modal-title">
									<xsl:if test="@gender = 'true'">車馬費</xsl:if>
									<xsl:if test="@gender = 'false'">要求車馬費</xsl:if>
								</H5>
							</DIV>
							<DIV class="form-group mx-auto col-10">
								<xsl:if test="@gender = 'true'">
									<LABEL class="text-xs" for="fare">使用平台支付不必擔心私下給甜心爽約，可檢舉查證屬實退回</LABEL>
								</xsl:if>
								<INPUT class="form-control" id="fare" min="1" name="howMany" required="" type="number"/>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-outline-dark mx-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
								<BUTTON class="btn btn-outline-primary confirmFare mx-1" type="button">確認</BUTTON>
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
								<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
									<I class="fal fa-times"></I>
								</BUTTON>
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
								<BUTTON class="btn btn-outline-dark" data-bs-dismiss="modal" type="button">
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
								<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
									<I class="fal fa-times"></I>
								</BUTTON>
							</DIV>
							<DIV class="mt-3">
								<I class="fas fa-exclamation-circle text-info mb-1" style="font-size: 50px;"></I>
								<P class="text-bold">1. 對方不會知道您封鎖他</P>
								<P class="text-bold">2. 對方瀏覽您的網頁，會出現您的帳戶已關閉</P>
							</DIV>
							<DIV>
								<BUTTON type="button" class="btn btn-outline-primary px-3 py-2 block">確認</BUTTON>
								<BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<div class="nav d-none">
				<h1>LOGO</h1>
				<i class="fas fa-chevron-down"></i>
				<i class="fas">
					<img class="fa-user-circle" src="http://placekitten.com/70/70" alt="profile piture"/>
					<span class="user-name">John Doe</span>

				</i>
				<i class="fas fa-bell">
					<i class="fas fa-circle"></i>
				</i>
			</div>

			<div class="d-flex">
				<div class="chats d-none d-lg-block col-lg-4 bg-dark">
					<div class="group-list">
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
						<div class="card my-2 px-2 mx-auto conversationWrap position-relative">
							<a class="inboxLink" href="/chatroom/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/"></a>
							<div class="d-flex justify-content-between align-items-center py-2">
								<div>
									<img src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" class="rounded-circle" width="55px"/>
								</div>
								<div class="me-auto" style="overflow: hidden;">
									<div class="d-flex flex-column align-items-start ms-3">
										<a class=" font-weight-bold text-dark text-lgmb-1">Peter</a>
										<p class="text-sm mb-0 content">給我車馬費</p>
									</div>
								</div>
								<div class="col-2 d-flex">
									<div class="ms-auto d-flex flex-column">
										<span class="text-xs mb-1">6天前</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="chat-container col-12 col-lg-8">
					<div class="chat-room">
						<div class="chatroomNav col-12 col-lg-8 bg-dark shadow d-flex align-items-center">
							<DIV class="me-3">
								<BUTTON class="btn btn-link text-primary h2 m-0 p-0 locationBack">
									<I class="fal fa-arrow-left fontSize35"></I>
								</BUTTON>
							</DIV>
							<DIV>
								<A href="/profile/{@friendIdentifier}/">
									<IMG alt="profileImage" class="rounded-circle" src="{@friendProfileImage}" width="45"/>
									<SPAN class="text-lg ms-2 text-white">
										<xsl:value-of select="@friendNickname"/>
									</SPAN>
								</A>
							</DIV>
							<xsl:if test="not(@blocking) and not(@blockedBy)">
								<DIV class="ms-auto me-3">
									<BUTTON class="btn btn-link m-0 p-2" data-bs-target="#fareModal" data-bs-toggle="modal" type="button">
										<xsl:if test="@gender = 'true'">
											<xsl:attribute name="id">fare</xsl:attribute>
										</xsl:if>
										<xsl:if test="@gender = 'false'">
											<xsl:attribute name="id">reqFare</xsl:attribute>
										</xsl:if>
										<I class="fas fa-badge-dollar fontSize25"></I>
									</BUTTON>
									<A class="btn btn-link m-0 p-2" data-bs-toggle="dropdown">
										<I class="fas fa-comment-alt-times mb-0 fontSize25"></I>
									</A>
									<DIV class="dropdown-menu shadow">
										<BUTTON class="dropdown-item">
											<I class="fad fa-lightbulb-exclamation fontSize22 col-2 text-center"></I>
											<SPAN class="ms-2">檢舉對方</SPAN>
										</BUTTON>
										<BUTTON class="dropdown-item" data-bs-target="#blockModal" data-bs-toggle="modal">
											<I class="fad fa-user-slash fontSize22 col-2"></I>
											<SPAN class="ms-2">封鎖對方</SPAN>
										</BUTTON>
									</DIV>
								</DIV>
							</xsl:if>
						</div>
						<div class="pt-7 mb-4" id="messagesArea">
						</div>
						<div class="chat-bar col-12 col-lg-8 d-flex align-items-center">
							<DIV class="d-flex align-items-center justify-content-center p-2 w-100 position-relative footerWrap">
								<xsl:if test="@gender = 'false'">
									<xsl:if test="@decideBtn and not(@blocking) and not(@blockedBy)">
										<DIV class="d-flex align-items-center justify-content-center col-12 col-lg-8 ms-auto femaleBtn floatBtn">
											<BUTTON class="btn btn-sm btn-outline-primary px-3 py-2 m-0 me-1 border-radius-xl accept" type="button">接受</BUTTON>
											<BUTTON class="btn btn-sm btn-outline-dark px-3 py-2 m-0 border-radius-xl refuse" type="button">拒絕</BUTTON>
										</DIV>
									</xsl:if>
									<xsl:if test="@rateBtn and not(@blocking) and not(@blockedBy)">
										<DIV class="d-flex align-items-center justify-content-center col-12 col-lg-8 ms-auto femaleBtn floatBtn">
											<BUTTON class="btn btn-sm btn-warning px-3 py-2 m-0 border-radius-xl rate" data-bs-target="#rateModal" data-bs-toggle="modal" type="button">評價</BUTTON>
										</DIV>
									</xsl:if>
								</xsl:if>
								<xsl:if test="@gender = 'true'">
									<DIV class="d-flex align-items-center justify-content-center col-12 col-lg-8 ms-auto maleBtn floatBtn">
										<xsl:choose>
											<xsl:when test="@reqSocialMediaBtn and not(@blocking) and not(@blockedBy)">
												<BUTTON class="btn btn-sm btn-info px-3 py-2 m-0 border-radius-xl" type="button">
													<xsl:attribute name="id">giveMeLine</xsl:attribute>
													<SPAN>要求通訊軟體</SPAN>
													<I class="far fa-user-plus ms-1"></I>
												</BUTTON>
											</xsl:when>
											<xsl:when test="@waitingForRes and not(@blocking) and not(@blockedBy)">
												<BUTTON class="btn btn-sm btn-info text-light px-3 py-2 m-0 border-radius-xl" disabled="" type="button">
													<SPAN>已要求通訊軟體, 等待甜心回應</SPAN>
												</BUTTON>
											</xsl:when>
											<xsl:when test="@addLineBtn and not(@blocking) and not(@blockedBy)">
												<BUTTON class="btn btn-sm btn-success px-3 py-2 m-0 border-radius-xl openSocialMedia" type="button">
													<SPAN>加入好友</SPAN>
													<xsl:if test="@remindDeduct">
														<DIV class="text-xxs">需 100 愛心</DIV>
													</xsl:if>
												</BUTTON>
											</xsl:when>
										</xsl:choose>
										<xsl:if test="@rateBtn and not(@blocking) and not(@blockedBy)">
											<BUTTON class="btn btn-sm btn-warning px-3 py-2 m-0 ms-1 border-radius-xl rate" data-bs-target="#rateModal" data-bs-toggle="modal" type="button">評價</BUTTON>
										</xsl:if>
									</DIV>
								</xsl:if>
								<xsl:choose>
									<xsl:when test="not(@notAbleToSendMsgs) and not(@blocking) and not(@blockedBy)">
										<TEXTAREA class="form-control" id="chatInput" maxlength="240" placeholder="說點什麼吧...!" rows="1"></TEXTAREA>
										<BUTTON class="btn btn-icon-only btn-link m-0 p-0 sendMsgBtn" type="button">
											<I class="fal fa-paper-plane" style="font-size: 30px;"></I>
										</BUTTON>
									</xsl:when>
									<xsl:when test="@blockedBy">
										<SPAN>此用戶已不存在</SPAN>
									</xsl:when>
									<xsl:when test="@blocking">
										<SPAN>您已封鎖對方</SPAN>
									</xsl:when>
									<xsl:when test="@notAbleToSendMsgs">
										<SPAN>12小時內僅能發送3句話給甜心</SPAN>
									</xsl:when>
								</xsl:choose>
							</DIV>
						</div>
					</div>
				</div>
			</div>


			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/chatroom.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocketChat.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>