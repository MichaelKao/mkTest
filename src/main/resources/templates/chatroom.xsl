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
			<LINK href="https://npmcdn.com/flickity@2/dist/flickity.css" rel="stylesheet"/>
			<LINK href="/STYLE/chatroom.css" rel="stylesheet"/>
			<LINK href="/STYLE/rateStar.css" rel="stylesheet"/>
			<LINK href="/STYLE/loading.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="bootstrapToast"/>
			<INPUT name="identifier" type="hidden" value="{@identifier}"/>
			<INPUT name="friendIdentifier" type="hidden" value="{@friendIdentifier}"/>
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
								<BUTTON class="btn btn-outline-primary commentBtn mx-1 px-3 py-2" type="button">確認</BUTTON>
								<BUTTON class="btn btn-outline-dark mx-1 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
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
								<I class="fad fa-badge-dollar text-success mb-1" style="font-size: 50px;"></I>
								<H5 class="modal-title">
									<xsl:if test="@male">透過ME點邀約</xsl:if>
									<xsl:if test="@female">透過ME點確認邀約</xsl:if>
								</H5>
							</DIV>
							<DIV class="form-group mx-auto col-11 text-center">
								<xsl:if test="@male">
									<DIV class="my-2 text-primary text-sm">
										<DIV>安心防詐騙</DIV>
										<DIV>可於48小時內向客服檢舉退回</DIV>
									</DIV>
									<DIV class="my-2 text-primary text-xs">
										<DIV>
											<I class="fad fa-taxi fontSize22 me-1"></I>
											<SPAN>率先支付約會車馬費，成功率提高80%</SPAN>
										</DIV>
										<DIV>
											<I class="fad fa-gift fontSize22 me-1"></I>
											<SPAN>透過ME點做為禮物發送，好感度增加90%</SPAN>
										</DIV>
									</DIV>
								</xsl:if>
								<xsl:if test="@female">
									<DIV class="my-2 text-primary text-sm">
										<DIV>。防止爽約與詐騙情形</DIV>
										<DIV>。一鍵退回方便取消約會</DIV>
										<DIV>。避免約會當日金錢糾紛</DIV>
									</DIV>
									<DIV class="my-2 text-warning text-xs">
										<SPAN>
											<I class="far fa-wallet me-1"></I>
										</SPAN>
										<SPAN>換算台幣 1:1, 提領有一成金流手續費</SPAN>
									</DIV>
								</xsl:if>
								<INPUT class="form-control" id="fare" inputmode="numeric" min="1" name="howMany" placeholder="ME點" required="" type="number"/>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-outline-primary confirmFare mx-1 px-3 py-2" type="button">確認</BUTTON>
								<BUTTON class="btn btn-outline-dark mx-1 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:if test="@male">
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
			<DIV class="fixed-top container chatNavbar">
				<NAV class="navbar navbar-expand-lg top-0 position-absolute p-0 start-0 end-0">
					<DIV class="fixed-top container mx-auto bg-light">
						<A class="navbar-brand font-weight-bolder m-0 p-0" href="/">
							<IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/icon.svg"/>
						</A>
						<DIV class="d-flex align-items-center">
							<xsl:if test="@signIn">
								<A class="d-lg-none pe-1" href="/activities.asp">
									<I class="fad fa-bell fontSize22 text-dark" style="z-index: -1;"></I>
									<SPAN class="text-xs text-light bg-danger border-radius-md ms-n2 announcement" style="display: none;">
										<xsl:if test="@announcement">
											<xsl:attribute name="style">display: inline;</xsl:attribute>
											<xsl:value-of select="@announcement"/>
										</xsl:if>
									</SPAN>
								</A>
								<A class="d-lg-none px-1" href="/inbox.asp">
									<I class="fad fa-comments fontSize22 text-dark" style="z-index: -1;"></I>
									<SPAN class="text-xs text-light bg-danger border-radius-md ms-n2 inbox" style="display: none;">
										<xsl:if test="@inbox">
											<xsl:attribute name="style">display: inline;</xsl:attribute>
											<xsl:value-of select="@inbox"/>
										</xsl:if>
									</SPAN>
								</A>
							</xsl:if>
							<BUTTON class="navbar-toggler shadow-none px-1" type="button" data-bs-toggle="collapse" data-bs-target="#navigation">
								<SPAN class="navbar-toggler-icon mt-2">
									<SPAN class="navbar-toggler-bar bar1"></SPAN>
									<SPAN class="navbar-toggler-bar bar2"></SPAN>
									<SPAN class="navbar-toggler-bar bar3"></SPAN>
								</SPAN>
							</BUTTON>
						</DIV>
						<DIV class="navbar-collapse justify-content-end bg-light collapse" id="navigation">
							<UL class="navbar-nav navbar-nav-hover">
								<xsl:if test="@signIn">
									<LI class="nav-item dropdown dropdown-hover">
										<A class="nav-link cursor-pointer text-primary p-2 p-lg-1 me-2" id="dropdownMenuPages" data-bs-toggle="dropdown">
											<DIV class="d-flex align-items-center">
												<SPAN class="me-1">
													<I class="fad fa-user-cog fontSize22"></I>
												</SPAN>
												<I class="fas fa-sort-down"></I>
											</DIV>
											<DIV class="text-xs text-bold d-none d-lg-block">設定</DIV>
										</A>
										<DIV class="dropdown-menu dropdown-menu-animation p-3 border-radius-lg mt-0 mt-lg-3">
											<DIV class="d-none d-lg-block">
												<A class="dropdown-item border-radius-md row" href="/profile/">
													<I class="fad fa-user-edit fontSize22 col-1"></I>
													<SPAN class="ms-1">個人檔案</SPAN>
												</A>
												<A class="dropdown-item border-radius-md row" href="/referralCode.asp">
													<I class="fad fa-users-crown fontSize22 col-1"></I>
													<SPAN class="ms-1">好友邀請碼</SPAN>
												</A>
												<xsl:if test="@female">
													<A class="dropdown-item border-radius-md row" href="/groupGreeting.asp">
														<I class="fad fa-hand-heart fontSize22 col-1"></I>
														<SPAN class="ms-1">群發打招呼</SPAN>
													</A>
												</xsl:if>
												<A class="dropdown-item border-radius-md row" href="/setting.asp">
													<I class="fad fa-cog fontSize22 col-1"></I>
													<SPAN class="ms-1">進階設定</SPAN>
												</A>
											</DIV>
											<DIV class="d-lg-none">
												<A class="dropdown-item border-radius-md row" href="/profile/">
													<I class="fad fa-user-edit fontSize22 col-1"></I>
													<SPAN class="ms-1">個人檔案</SPAN>
												</A>
												<A class="dropdown-item border-radius-md row" href="/referralCode.asp">
													<I class="fad fa-users-crown fontSize22 col-1"></I>
													<SPAN class="ms-1">好友邀請碼</SPAN>
												</A>
												<xsl:if test="@female">
													<A class="dropdown-item border-radius-md row" href="/groupGreeting.asp">
														<I class="fad fa-hand-heart fontSize22 col-1"></I>
														<SPAN class="ms-1">群發打招呼</SPAN>
													</A>
												</xsl:if>
												<A class="dropdown-item border-radius-md row" href="/setting.asp">
													<I class="fad fa-cog fontSize22 col-1"></I>
													<SPAN class="ms-1">進階設定</SPAN>
												</A>
											</DIV>
										</DIV>
									</LI>
									<xsl:if test="@almighty or @finance">
										<LI class="nav-item d-flex align-items-end">
											<A class="nav-link nav-link-icon text-primary p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/dashboard/members.asp">
												<DIV class="d-flex align-items-center">
													<I class="fad fa-id-card-alt fontSize22 width30whenMobile"></I>
													<SPAN class="d-lg-none">YoungMe 後台</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">後台</DIV>
											</A>
										</LI>
									</xsl:if>
									<xsl:if test="@signIn">
										<LI class="nav-item d-none d-lg-flex align-items-end">
											<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/activities.asp">
												<DIV>
													<I class="fad fa-bell fontSize22"></I>
													<SPAN class="text-xs text-light bg-danger border-radius-md ms-n2 announcement" style="display: none;">
														<xsl:if test="@announcement">
															<xsl:attribute name="style">display: inline;</xsl:attribute>
															<xsl:value-of select="@announcement"/>
														</xsl:if>
													</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">通知</DIV>
											</A>
										</LI>
										<LI class="nav-item d-none d-lg-flex align-items-end">
											<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/inbox.asp">
												<DIV>
													<I class="fad fa-comments fontSize22"></I>
													<SPAN class="text-xs text-light bg-danger border-radius-md ms-n1 inbox" style="display: none;">
														<xsl:if test="@inbox">
															<xsl:attribute name="style">display: inline;</xsl:attribute>
															<xsl:value-of select="@inbox"/>
														</xsl:if>
													</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">訊息</DIV>
											</A>
										</LI>
									</xsl:if>
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/">
											<DIV>
												<I class="fad fa-home-heart fontSize22 me-1"></I>
												<SPAN class="d-lg-none">所有養蜜</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">養蜜</DIV>
										</A>
									</LI>
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/filter.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-search fontSize22 width30whenMobile"></I>
												<SPAN class="d-lg-none">搜尋養蜜</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">搜尋</DIV>
										</A>
									</LI>
									<!--									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/forum/">
											<DIV class="d-flex align-items-center">
												<I class="fas fa-comment-alt-edit fontSize22 width30whenMobile"></I>
												<SPAN class="d-lg-none">討論區</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">討論</DIV>
										</A>
									</LI>-->
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/favorite.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-box-heart fontSize22 me-1"></I>
												<SPAN class="d-lg-none">我的收藏</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">收藏</DIV>
										</A>
									</LI>
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/looksMe.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-shoe-prints fontSize22 me-1"></I>
												<SPAN class="d-lg-none">誰看過我</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">足跡</DIV>
										</A>
									</LI>
									<xsl:if test="@female">
										<LI class="nav-item d-flex align-items-end">
											<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/withdrawal.asp">
												<DIV class="d-flex align-items-center">
													<I class="fad fa-badge-dollar fontSize22 me-1"></I>
													<SPAN class="d-lg-none">提領ME點</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">帳戶</DIV>
											</A>
										</LI>
									</xsl:if>
									<xsl:if test="@male">
										<LI class="nav-item d-flex align-items-end">
											<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/recharge.asp">
												<DIV class="d-flex align-items-center">
													<I class="fad fa-badge-dollar fontSize22 me-1"></I>
													<SPAN class="d-lg-none">購買ME點</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">儲值</DIV>
											</A>
										</LI>
										<LI class="nav-item d-flex align-items-end">
											<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/upgrade.asp">
												<DIV class="d-flex align-items-center">
													<I class="fad fa-crown fontSize22 me-1"></I>
													<SPAN class="d-lg-none">meKING 升級</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">升級</DIV>
											</A>
										</LI>
									</xsl:if>
								</xsl:if>
								<LI class="nav-item d-flex align-items-end">
									<xsl:choose>
										<xsl:when test="@signIn">
											<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/signOut.asp">
												<DIV class="d-flex align-items-center">
													<I class="fad fa-sign-out fontSize22 me-1"></I>
													<SPAN class="d-lg-none">登出</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">登出</DIV>
											</A>
										</xsl:when>
										<xsl:otherwise>
											<A class="nav-link nav-link-icon p-2 p-lg-1 me-3 d-flex flex-column align-items-center" href="/signIn.asp">
												<DIV class="d-flex align-items-center">
													<I class="fad fa-sign-in fontSize22 me-1"></I>
													<SPAN class="d-lg-none">登入</SPAN>
												</DIV>
												<DIV class="d-none d-lg-block text-xs text-bold">登入</DIV>
											</A>
										</xsl:otherwise>
									</xsl:choose>
								</LI>
							</UL>
						</DIV>
					</DIV>
				</NAV>
			</DIV>
			<DIV class="d-flex chatContainer container px-0">
				<DIV class="d-none d-lg-block col-lg-3 chatList" id="chatList">
					<DIV class="list shadow">
						<H3 class="text-dark ms-2">聊天</H3>
						<DIV class="justify-content-center mx-0 pb-6" id="listContent">
							<INPUT name="nextPage" type="hidden" value="1"/>
							<xsl:for-each select="conversation">
								<DIV class="conversationWrap position-relative cursor-pointer" id="{@identifier}">
									<xsl:if test="/document/@friendIdentifier = @identifier">
										<xsl:attribute name="class">conversationWrap position-relative cursor-pointer active</xsl:attribute>
									</xsl:if>
									<DIV class="d-flex justify-content-between align-items-center p-2">
										<DIV class="position-relative">
											<xsl:if test="@isMatchedOrIsVip = 'true'">
												<xsl:if test="/document/@female">
													<I class="fad fa-crown text-yellow text-shadow position-absolute" style="right: -5px;bottom: 1px;"></I>
												</xsl:if>
												<xsl:if test="/document/@male">
													<I class="fad fa-users text-primary text-shadow position-absolute" style="right: -5px;bottom: 1px;"></I>
												</xsl:if>
											</xsl:if>
											<IMG alt="大頭照" src="{@profileImage}" class="rounded-circle shadow" width="60px"/>
										</DIV>
										<DIV class="me-auto" style="overflow: hidden;">
											<DIV class="d-flex flex-column align-items-start ms-3">
												<A class="font-weight-bold text-dark text-sm mb-1 name">
													<xsl:value-of select="@nickname"/>
												</A>
												<P class="text-sm mb-0 content">
													<xsl:value-of select="@content"/>
												</P>
											</DIV>
										</DIV>
										<DIV class="col-3 d-flex">
											<DIV class="ms-auto d-flex flex-column">
												<SPAN class="text-xs mb-1">
													<xsl:value-of select="@occurredTime"/><!--多久之前-->
												</SPAN>
												<xsl:if test="@notSeenCount">
													<DIV class="d-flex justify-content-center">
														<SPAN class="text-xs text-light bg-danger border-radius-md px-1">
															<xsl:value-of select="@notSeenCount"/>
														</SPAN>
													</DIV>
												</xsl:if>
											</DIV>
										</DIV>
									</DIV>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
					<DIV class="hideSideBar d-lg-none position-absolute top-0 bottom-0 bg-dark opacity-7 text-white fontSize35 text-center cursor-pointer">
						<I class="fal fa-angle-left"></I>
					</DIV>
				</DIV>
				<DIV class="showSideBar d-lg-none position-absolute top-0 bottom-0 left-0 bg-dark opacity-7 text-white fontSize35 text-center cursor-pointer">
					<I class="fal fa-angle-right"></I>
				</DIV>
				<DIV class="col-12 col-lg-9">
					<DIV class="list">
						<DIV class="d-flex align-items-center bg-dark py-1 px-2 chatHeader">
							<DIV>
								<A href="/profile/{@friendIdentifier}/" id="friendLink">
									<IMG alt="profileImage" class="rounded-circle" id="friendImg" src="{@friendProfileImage}" width="35"/>
									<SPAN class="ms-2 text-white" id="friendName">
										<xsl:value-of select="@friendNickname"/>
									</SPAN>
								</A>
							</DIV>
							<xsl:if test="not(@blocking) and not(@blockedBy)">
								<DIV class="ms-auto">
									<BUTTON class="btn btn-link m-0 p-2" data-bs-target="#fareModal" data-bs-toggle="modal" type="button">
										<xsl:if test="@male">
											<xsl:attribute name="id">fare</xsl:attribute>
										</xsl:if>
										<xsl:if test="@female">
											<xsl:attribute name="id">reqFare</xsl:attribute>
										</xsl:if>
										<I class="fas fa-badge-dollar fontSize25 text-white"></I>
									</BUTTON>
									<A class="btn btn-link m-0 p-2" data-bs-toggle="dropdown">
										<I class="fas fa-comment-alt-times mb-0 fontSize25 text-white"></I>
									</A>
									<DIV class="dropdown-menu shadow">
										<!--										<BUTTON class="dropdown-item">
											<I class="fad fa-lightbulb-exclamation fontSize22 col-2 text-center"></I>
											<SPAN class="ms-2">檢舉對方</SPAN>
										</BUTTON>-->
										<BUTTON class="dropdown-item" data-bs-target="#blockModal" data-bs-toggle="modal">
											<I class="fad fa-user-slash fontSize22 col-2"></I>
											<SPAN class="ms-2">封鎖對方</SPAN>
										</BUTTON>
									</DIV>
								</DIV>
							</xsl:if>
						</DIV>
						<DIV class="border-radius-lg mx-auto position-absolute left-0 right-0 pt-0 ps-2 pe-1 pb-2 mt-6 userAlert primary-gradient imageShadow">
							<DIV class="d-flex">
								<BUTTON class="btn btn-link text-white ms-auto fontSize22 m-0 pt-1 p-0 me-2 userAlertClose" type="button">
									<I class="fal fa-times fontSize25"></I>
								</BUTTON>
							</DIV>
							<DIV class="text-white">
								<DIV class="text-center text-sm text-bold mb-1">
									<DIV>禮貌詢問並參考使用教學</DIV>
									<DIV>甜蜜約會就在今天 !!</DIV>
									<BUTTON class="btn btn-outline-light btn-round py-2 px-3 my-2" data-bs-target="#guide" data-bs-toggle="modal" type="button">效率約會使用教學</BUTTON>
								</DIV>
							</DIV>
						</DIV>
						<DIV class="modal fade" id="guide" tabindex="-1">
							<DIV class="modal-dialog modal-dialog-centered">
								<DIV class="modal-content">
									<DIV class="modal-body p-0">
										<DIV class="closeGuidance">
											<BUTTON class="btn btn-link text-white ms-auto fontSize30 m-0 me-2 p-0" data-bs-dismiss="modal" type="button">
												<I class="fal fa-times"></I>
											</BUTTON>
										</DIV>
										<DIV class="carousel mx-auto">
											<xsl:if test="@male">
												<DIV class="carousel-cell king imageShadow d-flex flex-column justify-content-center">
													<DIV class="kingGuide">
														<DIV class="kingGuideSlogan">LET'S CHAT AND DATE</DIV>
														<DIV class="d-flex flex-column justify-content-center align-items-center mb-3 kingGuideContent">
															<DIV class="guideTitle text-bold text-white h4 mb-0">效率3句邀約</DIV>
															<DIV class="text-white h6 my-1 text-light">紳士邀約, 好感加倍</DIV>
															<DIV class="text-white text-sm">1. 詳盡向meQUEEN自我介紹</DIV>
															<DIV class="text-white text-sm">2. 提出約會地點時間內容</DIV>
															<DIV class="text-white text-sm">3. 詢問meQUEEN約會預算</DIV>
															<DIV class="kingFirstStep mt-1 text-sm text-white">
																<DIV>禮貌且明確的邀約</DIV>
																<DIV>禁止性交易與騷擾</DIV>
																<DIV class="text-bold">追求到夢想女孩</DIV>
															</DIV>
														</DIV>
													</DIV>
													<OL class="flickity-page-dots">
														<LI class="dot is-selected"></LI>
														<LI class="dot"></LI>
														<LI class="dot"></LI>
													</OL>
												</DIV>
												<DIV class="carousel-cell king imageShadow d-flex flex-column justify-content-center">
													<DIV class="kingGuide">
														<DIV class="kingGuideSlogan">LET'S CHAT AND DATE</DIV>
														<DIV class="d-flex flex-column justify-content-center align-items-center mb-3 kingGuideContent">
															<DIV class="guideTitle text-bold text-white h4 mb-0">發送好友邀請</DIV>
															<DIV class="text-white h6 mt-1 mb-0 text-light">詳盡對話確認意願後</DIV>
															<DIV class="text-white h6 mb-0 text-light">發送加入好友邀請</DIV>
															<DIV class="text-white text-sm mt-4">升級會員與meQUEEN成為好友</DIV>
															<DIV class="text-white text-xs">聊天室暢聊並獲得通訊軟體</DIV>
														</DIV>
													</DIV>
													<OL class="flickity-page-dots">
														<LI class="dot"></LI>
														<LI class="dot is-selected"></LI>
														<LI class="dot"></LI>
													</OL>
												</DIV>
												<DIV class="carousel-cell king imageShadow d-flex flex-column justify-content-center">
													<DIV class="kingGuide">
														<DIV class="kingGuideSlogan">LET'S CHAT AND DATE</DIV>
														<DIV class="d-flex flex-column justify-content-center align-items-center mb-3 kingGuideContent">
															<DIV class="guideTitle text-bold text-white h4 mb-0">發送約會見面費</DIV>
															<DIV class="text-white h6 mt-1 mb-0">甜蜜約會就差一步</DIV>
															<DIV class="text-white h6 mb-0 text-light">讓她打扮美美的與您見面</DIV>
															<DIV class="text-white h6 mt-4 mb-0 text-light">無須升級, 儲值發送</DIV>
															<DIV class="text-white text-xs">女生爽約可以聯繫客服退回</DIV>
														</DIV>
													</DIV>
													<OL class="flickity-page-dots">
														<LI class="dot"></LI>
														<LI class="dot"></LI>
														<LI class="dot is-selected"></LI>
													</OL>
												</DIV>
											</xsl:if>
											<xsl:if test="@female">
												<DIV class="carousel-cell imageShadow d-flex flex-column justify-content-center">
													<DIV class="d-flex flex-column justify-content-center align-items-center mb-3">
														<H4 class="text-white text-light">LET'S CHAT AND DATE</H4>
														<DIV class="guideTitle text-bold text-dark h4 mb-0">防止騷擾</DIV>
														<DIV class="text-white h6 mt-1 mb-0 text-light">先聊聊觀察與考慮</DIV>
													</DIV>
													<DIV class="d-flex flex-column justify-content-center align-items-center my-2">
														<DIV class="text-white text-bold">可以互相聊聊再決定加好友</DIV>
														<DIV class="text-white text-bold">12小時內對方只有3句話額度</DIV>
														<DIV class="text-white text-bold">防止被騷擾與浪費時間尬聊</DIV>
													</DIV>
													<DIV class="d-flex flex-column justify-content-center align-items-center my-3">
														<DIV>
															<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/queenChat1.jpg" width="150px"/>
														</DIV>
														<DIV class="queenSlogan mt-3 text-center text-white text-sm">
															<DIV>禮貌且明確的邀約</DIV>
															<DIV>禁止性交易與騷擾</DIV>
															<DIV class="text-bold">追求到夢想女孩</DIV>
														</DIV>
													</DIV>
													<OL class="flickity-page-dots">
														<LI class="dot is-selected"></LI>
														<LI class="dot"></LI>
														<LI class="dot"></LI>
													</OL>
												</DIV>
												<DIV class="carousel-cell imageShadow d-flex flex-column justify-content-center">
													<DIV class="d-flex flex-column justify-content-center align-items-center mb-2">
														<H4 class="text-white text-light">LET'S CHAT AND DATE</H4>
														<DIV class="guideTitle text-bold text-dark h4 mb-0">篩選後加好友</DIV>
														<DIV class="text-white h6 mt-1 mb-0 text-light">進一步認識</DIV>
													</DIV>
													<DIV class="d-flex flex-column justify-content-center align-items-center mt-2">
														<DIV class="text-white text-bold">讓有誠意的meKING</DIV>
														<DIV class="text-white text-bold">透過發送系統好友邀請</DIV>
														<DIV class="text-white text-bold">解鎖聊天室限制</DIV>
														<DIV class="text-white text-sm mt-2">同時獲得您綁定的通訊軟體</DIV>
														<DIV class="text-white text-sm">以便約會後續聯繫</DIV>
													</DIV>
													<DIV class="d-flex flex-column justify-content-center align-items-center my-2">
														<DIV>
															<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/queenChat2.jpg" width="150px"/>
														</DIV>
														<DIV class="queenSlogan mt-2 text-center text-white text-sm">
															<DIV>透過系統加入好友的男士</DIV>
															<DIV>均已進行財富認證升級</DIV>
														</DIV>
													</DIV>
													<OL class="flickity-page-dots">
														<LI class="dot"></LI>
														<LI class="dot is-selected"></LI>
														<LI class="dot"></LI>
													</OL>
												</DIV>
												<DIV class="carousel-cell imageShadow d-flex flex-column justify-content-center">
													<DIV class="d-flex flex-column justify-content-center align-items-center">
														<H4 class="text-white text-light">LET'S CHAT AND DATE</H4>
														<DIV class="guideTitle text-bold text-dark h4 mb-0">接受ME點</DIV>
														<DIV class="text-white h6 mt-1 mb-0 text-light">美美的您值得付費邀約</DIV>
													</DIV>
													<DIV class="d-flex flex-column justify-content-center align-items-center mt-2">
														<DIV class="text-white text-bold">確認約會，即刻見面</DIV>
														<DIV class="text-white text-bold">把邀約確定下來</DIV>
														<DIV class="text-white text-sm mt-2">接受約會見面車馬費</DIV>
														<DIV class="text-white text-sm">直接站內確認入帳</DIV>
														<DIV class="text-white text-sm">48小時內可反悔退回</DIV>
													</DIV>
													<DIV class="d-flex flex-column justify-content-center align-items-center my-2">
														<DIV>
															<IMG class="border-radius-sm imageShadow" src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/queenChat3.jpg" width="150px"/>
														</DIV>
														<DIV class="queenSlogan mt-2 text-center text-white text-sm">
															<DIV>使用現金交易有爽約賴帳風險</DIV>
															<DIV>提供匯款帳戶會洩漏個資</DIV>
															<DIV>同時有被詐騙洗錢等危險</DIV>
														</DIV>
													</DIV>
													<OL class="flickity-page-dots">
														<LI class="dot"></LI>
														<LI class="dot"></LI>
														<LI class="dot is-selected"></LI>
													</OL>
												</DIV>
											</xsl:if>
										</DIV>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
						<DIV class="messages position-relative" id="messages">
							<DIV id="messagesArea"></DIV>
							<INPUT name="nextMsgsPage" type="hidden" value="1" />
							<DIV class="loadingWrap position-absolute" id="loadingChat" style="display: none; background: #E5E7E9;">
								<DIV class="loading position-absolute">
									<DIV class="round"></DIV>
									<DIV class="round ms-1"></DIV>
									<DIV class="round ms-1"></DIV>
								</DIV>
							</DIV>
						</DIV>
						<DIV class="inputContainer">
							<DIV class="textareaBox"></DIV>
							<DIV class="customerBtnWrap">
								<A class="customerBtn d-flex align-items-center justify-content-center bg-dark fontSize22 opacity-8 text-white" href="https://line.me/R/ti/p/%40017zadfy">
									<I class="fad fa-user-headset"></I>
								</A>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="https://npmcdn.com/flickity@2/dist/flickity.pkgd.js"/>
			<SCRIPT src="/SCRIPT/chatroom.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocketChat.js"/>
				<SCRIPT src="/SCRIPT/websocketInbox.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>