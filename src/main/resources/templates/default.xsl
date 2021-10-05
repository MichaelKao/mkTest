<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output
		encoding="UTF-8"
		media-type="text/html"
		method="html"
		indent="no"
		omit-xml-declaration="yes"
	/>

	<xsl:template match="optgroup">
		<OPTGROUP label="{@label}">
			<xsl:apply-templates select="option"/>
		</OPTGROUP>
	</xsl:template>

	<xsl:template match="option">
		<OPTION>
			<xsl:if test="@selected">
				<xsl:attribute name="selected"/>
			</xsl:if>
			<xsl:if test="@value">
				<xsl:attribute name="value">
					<xsl:value-of select="@value"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:value-of select="."/>
		</OPTION>
	</xsl:template>

	<!--导航列-->
	<xsl:template name="navbar">
		<xsl:call-template name="customerFloatBtn"/>
		<DIV class="container position-sticky z-index-sticky top-0">
			<INPUT
				name="identifier"
				type="hidden"
				value="{/document/@identifier}"
			/>
			<NAV class="navbar navbar-expand-lg blur blur-rounded top-0 z-index-3 shadow position-absolute my-3 py-1 py-md-3 start-0 end-0 mx-3">
				<DIV class="container-fluid">
					<A class="navbar-brand font-weight-bolder m-0" href="/">YOUNG ME 養蜜</A>
					<DIV class="d-flex align-items-center">
						<xsl:if test="/document/@signIn">
							<A class="d-lg-none pe-1" href="/activeLogs.asp">
								<I class="fad fa-bell fontSize22"/>
								<SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 announcement d-none">
									<xsl:if test="/document/@announcement">
										<xsl:attribute name="class">text-xs text-light bg-warning border-radius-md ms-n2 announcement d-inline</xsl:attribute>
										<xsl:value-of
											select="/document/@announcement"
										/>
									</xsl:if>
								</SPAN>
							</A>
							<A class="d-lg-none px-2" href="/inbox.asp">
								<I class="fad fa-comment-smile fontSize22"/>
								<SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 inbox d-none">
									<xsl:if test="/document/@inbox">
										<xsl:attribute name="class">text-xs text-light bg-warning border-radius-md ms-n2 inbox d-inline</xsl:attribute>
										<xsl:value-of
											select="/document/@inbox"
										/>
									</xsl:if>
								</SPAN>
							</A>
						</xsl:if>
						<BUTTON class="navbar-toggler shadow-none px-1" type="button" data-bs-target="#navigation" data-bs-toggle="collapse">
							<SPAN class="navbar-toggler-icon mt-2">
								<SPAN class="navbar-toggler-bar bar1"/>
								<SPAN class="navbar-toggler-bar bar2"/>
								<SPAN class="navbar-toggler-bar bar3"/>
							</SPAN>
						</BUTTON>
					</DIV>
					<DIV class="collapse navbar-collapse justify-content-end" id="navigation">
						<UL class="navbar-nav navbar-nav-hover">
							<xsl:if test="/document/@signIn">
								<LI class="nav-item dropdown dropdown-hover">
									<A class="nav-link cursor-pointer text-primary" id="dropdownMenuPages" data-bs-toggle="dropdown">
										<SPAN class="me-1">
											<I class="fad fa-user-cog fontSize22"/>
										</SPAN>
										<I class="fas fa-chevron-down"/>
									</A>
									<DIV class="dropdown-menu dropdown-menu-animation p-3 border-radius-lg mt-0 mt-lg-3">
										<DIV class="d-none d-lg-block">
											<A class="dropdown-item border-radius-md" href="/profile/">
												<I class="fad fa-user-edit fontSize22 width30"/>
												<SPAN class="ms-1">個人檔案</SPAN>
											</A>
											<A class="dropdown-item border-radius-md" href="/referralCode.asp">
												<I class="fad fa-users-crown fontSize22 width30"/>
												<SPAN class="ms-1">好友邀請碼</SPAN>
											</A>
											<xsl:if test="@female">
												<A class="dropdown-item border-radius-md" href="/groupGreeting.asp">
													<I class="fad fa-comments-alt fontSize22 width30"/>
													<SPAN class="ms-1">群發打招呼</SPAN>
												</A>
											</xsl:if>
											<A class="dropdown-item border-radius-md" href="/setting.asp">
												<I class="fad fa-cog fontSize22 width30"/>
												<SPAN class="ms-1">進階設定</SPAN>
											</A>
										</DIV>
										<DIV class="d-lg-none">
											<A class="dropdown-item border-radius-md" href="/profile/">
												<I class="fad fa-user-edit fontSize22 width30"/>
												<SPAN class="ms-1">個人檔案</SPAN>
											</A>
											<A class="dropdown-item border-radius-md" href="/referralCode.asp">
												<I class="fad fa-users-crown fontSize22 width30"/>
												<SPAN class="ms-1">好友邀請碼</SPAN>
											</A>
											<xsl:if test="@female">
												<A class="dropdown-item border-radius-md" href="/groupGreeting.asp">
													<I class="fad fa-comments-alt fontSize22 width30"/>
													<SPAN class="ms-1">群發打招呼</SPAN>
												</A>
											</xsl:if>
											<A class="dropdown-item border-radius-md" href="/setting.asp">
												<I class="fad fa-cog fontSize22 width30"/>
												<SPAN class="ms-1">進階設定</SPAN>
											</A>
										</DIV>
									</DIV>
								</LI>
								<xsl:if test="@almighty or @finance">
									<LI class="nav-item">
										<A class="nav-link nav-link-icon d-flex align-items-center text-primary" href="/dashboard/members.asp">
											<I class="fad fa-id-card-alt fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">YoungMe 後台</SPAN>
										</A>
									</LI>
								</xsl:if>
								<xsl:if test="/document/@signIn">
									<LI class="nav-item d-none d-lg-block">
										<A class="nav-link nav-link-icon" href="/activeLogs.asp">
											<I class="fad fa-bell fontSize22"/>
											<SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 announcement d-none">
												<xsl:if test="/document/@announcement">
													<xsl:attribute name="class">text-xs text-light bg-warning border-radius-md ms-n2 announcement d-inline</xsl:attribute>
													<xsl:value-of select="/document/@announcement"/>
												</xsl:if>
											</SPAN>
										</A>
									</LI>
									<LI class="nav-item d-none d-lg-block">
										<A class="nav-link nav-link-icon" href="/inbox.asp">
											<I class="fad fa-comment-smile fontSize22"/>
											<SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 inbox d-none">
												<xsl:if test="/document/@inbox">
													<xsl:attribute name="class">text-xs text-light bg-warning border-radius-md ms-n2 inbox d-inline</xsl:attribute>
													<xsl:value-of select="/document/@inbox"/>
												</xsl:if>
											</SPAN>
										</A>
									</LI>
								</xsl:if>
								<LI class="nav-item">
									<A class="nav-link nav-link-icon d-flex align-items-center" href="/">
										<I class="fad fa-home-heart fontSize22 width30whenMobile"/>
										<SPAN class="d-lg-none">
											<xsl:if test="@male">所有甜心</xsl:if>
											<xsl:if test="@female">所有男仕</xsl:if>
										</SPAN>
									</A>
								</LI>
								<LI class="nav-item">
									<A class="nav-link nav-link-icon d-flex align-items-center" href="/favorite.asp">
										<I class="fad fa-box-heart fontSize22 width30whenMobile"/>
										<SPAN class="d-lg-none">我的收藏</SPAN>
									</A>
								</LI>
								<LI class="nav-item">
									<A class="nav-link nav-link-icon d-flex align-items-center" href="/looksMe.asp">
										<I class="fad fa-shoe-prints fontSize22 width30whenMobile"/>
										<SPAN class="d-lg-none">誰看過我</SPAN>
									</A>
								</LI>
								<xsl:if test="@female">
									<LI class="nav-item">
										<A class="nav-link nav-link-icon d-flex align-items-center" href="/withdrawal.asp">
											<I class="fad fa-badge-dollar fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">提領車馬費</SPAN>
										</A>
									</LI>
								</xsl:if>
								<xsl:if test="@male">
									<LI class="nav-item">
										<A class="nav-link nav-link-icon d-flex align-items-center" href="/recharge.asp">
											<I class="fad fa-badge-dollar fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">購買 ME 點</SPAN>
										</A>
									</LI>
									<LI class="nav-item">
										<A class="nav-link nav-link-icon d-flex align-items-center" href="/upgrade.asp">
											<I class="fad fa-crown fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">升級 VIP</SPAN>
										</A>
									</LI>
								</xsl:if>
							</xsl:if>
							<LI class="nav-item">
								<xsl:choose>
									<xsl:when test="/document/@signIn">
										<A class="nav-link nav-link-icon d-flex align-items-center" href="/signOut.asp">
											<I class="fad fa-sign-out fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">登出</SPAN>
										</A>
									</xsl:when>
									<xsl:otherwise>
										<A class="nav-link nav-link-icon d-flex align-items-center" href="/signIn.asp">
											<I class="fad fa-sign-in fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">登入</SPAN>
										</A>
									</xsl:otherwise>
								</xsl:choose>
							</LI>
						</UL>
					</DIV>
				</DIV>
			</NAV>
		</DIV>
	</xsl:template>

	<!--哇爪脚本-->
	<xsl:template name="bodyScriptTags">
		<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha512-yUNtg0k40IvRQNR20bJ4oH6QeQ/mgs9Lsa6V+3qxTj58u2r+JiAYOhOW0o+ijuMmqCtCEg7LZRA+T4t84/ayVA==" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.5.0/perfect-scrollbar.min.js"/>
		<SCRIPT src="https://kit.fontawesome.com/5ed1767edc.js"/>
		<SCRIPT src="/SCRIPT/soft-design-system.min.js"/>
	</xsl:template>

	<!--样式表-->
	<xsl:template name="headLinkTags">
		<LINK href="/favicon.ico" rel="icon" type="image/x-icon"/>
		<LINK href="/ICON/apple-touch-icon.png" rel="apple-touch-icon"/>
		<LINK href="/ICON/apple-touch-icon-57x57.png" rel="apple-touch-icon" sizes="57x57"/>
		<LINK href="/ICON/apple-touch-icon-72x72.png" rel="apple-touch-icon" sizes="72x72"/>
		<LINK href="/ICON/apple-touch-icon-76x76.png" rel="apple-touch-icon" sizes="76x76"/>
		<LINK href="/ICON/apple-touch-icon-114x114.png" rel="apple-touch-icon" sizes="114x114"/>
		<LINK href="/ICON/apple-touch-icon-120x120.png" rel="apple-touch-icon" sizes="120x120"/>
		<LINK href="/ICON/apple-touch-icon-144x144.png" rel="apple-touch-icon" sizes="144x144"/>
		<LINK href="/ICON/apple-touch-icon-152x152.png" rel="apple-touch-icon" sizes="152x152"/>
		<LINK href="/ICON/apple-touch-icon-180x180.png" rel="apple-touch-icon" sizes="180x180"/>
		<LINK href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet"/>
		<LINK href="/STYLE/soft-design-system.css" rel="stylesheet"/>
		<LINK href="/STYLE/default.css" rel="stylesheet"/>
	</xsl:template>

	<xsl:template name="headMetaTags">
		<META content="IE=edge" http-equiv="X-UA-Compatible"/>
		<META content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover" name="viewport"/>
	</xsl:template>

	<xsl:template name="bootstrapToast">
		<DIV class="position-fixed top-5 end-0 p-3" style="z-index: 10000">
			<DIV class="toast fade hide bg-primary opacity-9" data-bs-delay="1800" >
				<BUTTON class="btn-close ms-2 mt-1" type="button" data-bs-dismiss="toast"/>
				<DIV class="toast-body text-light"/>
			</DIV>
		</DIV>
	</xsl:template>

	<!--浮动式客服按钮-->
	<xsl:template name="customerFloatBtn">
		<A class="customerFloatBtn d-flex align-items-center justify-content-center position-fixed bg-dark fontSize25 text-white opacity-9" href="https://line.me/R/ti/p/%40017zadfy">
			<I class="fad fa-user-headset"/>
		</A>
	</xsl:template>

	<!--地-->
	<xsl:template name="footer">
		<FOOTER class="text-xs my-4">
			<DIV class="text-center">
				<I class="fal fa-copyright me-1"/>
				<SPAN>2021 Young Me 養蜜</SPAN>
			</DIV>
			<DIV class="text-center">
				<A class="m-2 text-secondary" href="/privacy.asp">隱私政策</A>
				<A class="m-2 text-secondary" href="/terms.asp">服務條款</A>
			</DIV>
		</FOOTER>
		<!-- Global site tag (gtag.js) - Google Analytics -->
		<SCRIPT async="" src="https://www.googletagmanager.com/gtag/js?id={/document/seo/googleAnalytics/@id}"/>
		<SCRIPT>
			<xsl:value-of
				select="/document/seo/googleAnalytics"
				disable-output-escaping="yes"
			/>
		</SCRIPT>
		<!-- Facebook Pixel Code -->
		<SCRIPT>
			<xsl:value-of
				select="/document/seo/facebookPixel"
				disable-output-escaping="yes"
			/>
		</SCRIPT>
		<NOSCRIPT>
			<IMG
				height="1"
				width="1"
				style="display:none"
				src="https://www.facebook.com/tr?id={/document/seo/facebookPixel/@id}&#38;ev=PageView&#38;noscript=1"
			/>
		</NOSCRIPT>
		<!-- End Facebook Pixel Code -->
	</xsl:template>

	<!-- 後台需要的 -->
	<xsl:template name="dashScriptTags">
		<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha512-yUNtg0k40IvRQNR20bJ4oH6QeQ/mgs9Lsa6V+3qxTj58u2r+JiAYOhOW0o+ijuMmqCtCEg7LZRA+T4t84/ayVA==" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.5.0/perfect-scrollbar.min.js"/>
		<SCRIPT src="https://kit.fontawesome.com/5ed1767edc.js"/>
		<SCRIPT src="/SCRIPT/soft-ui-dashboard.min.js"/>
	</xsl:template>

	<xsl:template name="dashHeadLinkTags">
		<LINK href="/STYLE/soft-ui-dashboard.css" rel="stylesheet"/>
		<LINK href="/STYLE/default.css" rel="stylesheet"/>
	</xsl:template>

	<!--侧导条-->
	<xsl:template name="dashSideNavBar">
		<ASIDE class="sidenav navbar navbar-vertical navbar-expand-xs border-0 border-radius-xl my-3 fixed-start ms-3 bg-white" id="sidenav-main">
			<DIV class="sidenav-header">
				<I class="fas fa-times p-3 cursor-pointer text-secondary opacity-5 position-absolute end-0 top-0 d-none d-xl-none" id="iconSidenav"/>
				<DIV class="navbar-brand m-0">
					<I class="fal fa-id-card-alt text-lg"/>
					<SPAN class="ms-1 font-weight-bold">養蜜YoungMe後台</SPAN>
				</DIV>
			</DIV>
			<HR class="horizontal dark mt-0"/>
			<DIV class="collapse navbar-collapse w-auto max-height-vh-100 h-100" id="sidenav-collapse-main">
				<UL class="navbar-nav">
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/members.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-users-cog text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">會員管理</SPAN>
						</A>
					</LI>
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/certification.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-shield-check text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">安心認證審核</SPAN>
						</A>
					</LI>
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/withdrawal.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-badge-dollar text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">甜心提領紀錄</SPAN>
						</A>
					</LI>
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/genTrialCode.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-meteor text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">產生體驗碼</SPAN>
						</A>
					</LI>
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/stopRecurring.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-credit-card text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">解除定期定額</SPAN>
						</A>
					</LI>
					<LI class="nav-item mt-2">
						<SPAN class="ps-4 ms-2 text-xs font-weight-bolder text-primary">報表</SPAN>
					</LI>
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/{@year}/{@month}/{@date}/newAccounts.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-users text-lg text-dark"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">每日新進會員</SPAN>
						</A>
					</LI>
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/log/chat.xls">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-comments text-lg text-dark"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">聊天訊息報表</SPAN>
						</A>
					</LI>
				</UL>
			</DIV>
		</ASIDE>
	</xsl:template>

	<xsl:template name="dashTopNavBar">
		<!-- Navbar -->
		<NAV class="navbar navbar-main navbar-expand-lg px-0 mx-4 shadow-none border-radius-xl" id="navbarBlur" navbar-scroll="true">
			<DIV class="container-fluid py-1 px-2 px-sm-4">
				<H6 class="font-weight-bolder mb-0">養蜜 YoungMe 後台</H6>
				<DIV class="collapse navbar-collapse" id="navbar">
					<UL class="navbar-nav justify-content-end ms-auto">
						<LI class="nav-item d-flex align-items-center">
							<A class="btn btn-outline-primary btn-round px-2 py-1 mb-0" href="/">
								<I class="fal fa-home-heart text-lg"/>
								<SPAN class="ms-1">前台</SPAN>
							</A>
						</LI>
						<LI class="nav-item d-xl-none ps-2 d-flex align-items-center">
							<A href="javascript:;" class="nav-link text-body p-0" id="iconNavbarSidenav">
								<DIV class="sidenav-toggler-inner">
									<I class="sidenav-toggler-line"/>
									<I class="sidenav-toggler-line"/>
									<I class="sidenav-toggler-line"/>
								</DIV>
							</A>
						</LI>
					</UL>
				</DIV>
			</DIV>
		</NAV>
		<!-- End Navbar -->
	</xsl:template>

</xsl:stylesheet>