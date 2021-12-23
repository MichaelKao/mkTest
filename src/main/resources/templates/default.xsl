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
			<INPUT name="identifier" type="hidden" value="{@identifier}"/>
			<NAV class="navbar navbar-expand-lg blur blur-rounded top-0 z-index-3 shadow position-absolute my-3 py-0 py-md-2 py-lg-3 start-0 end-0 mx-2">
				<DIV class="container-fluid">
					<A class="navbar-brand font-weight-bolder m-0" href="/">
						<IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/icon.svg"/>
					</A>
					<DIV class="d-flex align-items-center">
						<xsl:if test="@signIn">
							<A class="d-lg-none pe-1" href="/activities.asp">
								<I class="fad fa-bell fontSize22 text-dark" style="z-index: -1;"/>
								<SPAN class="text-xs text-light bg-danger border-radius-md ms-n2 announcement" style="display: none;">
									<xsl:if test="@announcement">
										<xsl:attribute name="style">display: inline;</xsl:attribute>
										<xsl:value-of select="@announcement"/>
									</xsl:if>
								</SPAN>
							</A>
							<A class="d-lg-none px-1" href="/inbox.asp">
								<I class="fad fa-comments fontSize22 text-dark" style="z-index: -1;"/>
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
					<DIV class="collapse navbar-collapse justify-content-end" id="navigation">
						<UL class="navbar-nav navbar-nav-hover">
							<xsl:if test="@signIn">
								<LI class="nav-item dropdown dropdown-hover">
									<A class="nav-link cursor-pointer text-primary p-2 p-lg-0 me-2" id="dropdownMenuPages" data-bs-toggle="dropdown">
										<DIV class="d-flex align-items-center">
											<SPAN class="me-1">
												<I class="fad fa-user-cog fontSize22"/>
											</SPAN>
											<I class="fas fa-sort-down"/>
										</DIV>
										<DIV class="text-xs text-bold d-none d-lg-block">設定</DIV>
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
													<I class="fad fa-hand-heart fontSize22 width30"/>
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
													<I class="fad fa-hand-heart fontSize22 width30"/>
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
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon text-primary p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/dashboard/members.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-id-card-alt fontSize22 width30whenMobile"/>
												<SPAN class="d-lg-none">YoungMe 後台</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">後台</DIV>
										</A>
									</LI>
								</xsl:if>
								<xsl:if test="@signIn">
									<LI class="nav-item d-none d-lg-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/activities.asp">
											<DIV>
												<I class="fad fa-bell fontSize22" style="z-index: -1;"/>
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
										<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/inbox.asp">
											<DIV>
												<I class="fad fa-comments fontSize22" style="z-index: -1;"/>
												<SPAN class="text-xs text-light bg-danger border-radius-md ms-n2 inbox" style="display: none;">
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
									<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/">
										<DIV class="d-flex align-items-center">
											<I class="fad fa-home-heart fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">所有養蜜</SPAN>
										</DIV>
										<DIV class="d-none d-lg-block text-xs text-bold">養蜜</DIV>
									</A>
								</LI>
								<LI class="nav-item d-flex align-items-end">
									<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/filter.asp">
										<DIV class="d-flex align-items-center">
											<I class="fad fa-search fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">搜尋養蜜</SPAN>
										</DIV>
										<DIV class="d-none d-lg-block text-xs text-bold">搜尋</DIV>
									</A>
								</LI>
								<LI class="nav-item d-flex align-items-end">
									<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/forum/">
										<DIV class="d-flex align-items-center">
											<I class="fas fa-comment-alt-edit fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">討論區</SPAN>
										</DIV>
										<DIV class="d-none d-lg-block text-xs text-bold">討論</DIV>
									</A>
								</LI>
								<LI class="nav-item d-flex align-items-end">
									<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/favorite.asp">
										<DIV class="d-flex align-items-center">
											<I class="fad fa-box-heart fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">我的收藏</SPAN>
										</DIV>
										<DIV class="d-none d-lg-block text-xs text-bold">收藏</DIV>
									</A>
								</LI>
								<LI class="nav-item d-flex align-items-end">
									<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/looksMe.asp">
										<DIV class="d-flex align-items-center">
											<I class="fad fa-shoe-prints fontSize22 width30whenMobile"/>
											<SPAN class="d-lg-none">誰看過我</SPAN>
										</DIV>
										<DIV class="d-none d-lg-block text-xs text-bold">足跡</DIV>
									</A>
								</LI>
								<xsl:if test="@female">
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/withdrawal.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-badge-dollar fontSize22 width30whenMobile"/>
												<SPAN class="d-lg-none">提領ME點</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">帳戶</DIV>
										</A>
									</LI>
								</xsl:if>
								<xsl:if test="@male">
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/recharge.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-badge-dollar fontSize22 width30whenMobile"/>
												<SPAN class="d-lg-none">購買ME點</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">儲值</DIV>
										</A>
									</LI>
									<LI class="nav-item d-flex align-items-end">
										<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/upgrade.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-crown fontSize22 width30whenMobile"/>
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
										<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/signOut.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-sign-out fontSize22 width30whenMobile"/>
												<SPAN class="d-lg-none">登出</SPAN>
											</DIV>
											<DIV class="d-none d-lg-block text-xs text-bold">登出</DIV>
										</A>
									</xsl:when>
									<xsl:otherwise>
										<A class="nav-link nav-link-icon p-2 p-lg-0 me-3 d-flex flex-column align-items-center" href="/signIn.asp">
											<DIV class="d-flex align-items-center">
												<I class="fad fa-sign-in fontSize22 width30whenMobile"/>
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
	</xsl:template>

	<!--哇爪脚本-->
	<xsl:template name="bodyScriptTags">
		<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha512-PZrlUFhlOigX38TOCMdaYkhiqa/fET/Lztzjn+kdGxefUZanNUfmHv+9M/wSiOHzlcLX/vcCnmvOZSHi5Dqrsw==" referrerpolicy="no-referrer" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.11.0/umd/popper.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha512-OvBgP9A2JBgiRad/mM36mkzXSXaJE9BEIENnVEmeZdITvwT09xnxLtT4twkCa8m/loMbPHsvPl0T8lRGVBwjlQ==" referrerpolicy="no-referrer" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.1.3/js/bootstrap.min.js"/>
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
		<META charset="UTF-8"/>
		<META content="IE=edge" http-equiv="X-UA-Compatible"/>
		<META content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover" name="viewport"/>
		<META name="theme-color" content="#164353"/>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache"/>
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"/>
		<META HTTP-EQUIV="expires" CONTENT="0"/>
	</xsl:template>

	<xsl:template name="bootstrapToast">
		<DIV class="position-fixed top-5 end-0 p-3" style="z-index: 10000">
			<DIV class="toast fade hide bg-primary opacity-9" data-bs-delay="1800">
				<BUTTON class="btn-close ms-2 mt-1" type="button" data-bs-dismiss="toast"/>
				<DIV class="toast-body text-light"/>
			</DIV>
		</DIV>
	</xsl:template>

	<!--浮动式客服按钮-->
	<xsl:template name="customerFloatBtn">
		<A class="customerFloatBtn d-flex align-items-center justify-content-center position-fixed bg-dark fontSize25 text-white opacity-9 shadow" href="https://line.me/R/ti/p/%40017zadfy">
			<I class="fad fa-user-headset"/>
		</A>
	</xsl:template>

	<!--地-->
	<xsl:template name="footer">
		<FOOTER class="text-xs my-2">
			<DIV class="text-center">
				<I class="fal fa-copyright me-1"/>
				<SPAN>2021 Young Me 養蜜</SPAN>
			</DIV>
			<DIV class="text-center">
				<A class="m-2 text-secondary" href="/privacy.asp">隱私政策</A>
				<A class="m-2 text-secondary" href="/terms.asp">服務條款</A>
			</DIV>
		</FOOTER>
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
		<!-- Global site tag (gtag.js) - Google Analytics -->
		<SCRIPT async="" src="https://www.googletagmanager.com/gtag/js?id={/document/seo/googleAnalytics/@id}"/>
		<SCRIPT>
			<xsl:value-of
				select="/document/seo/googleAnalytics"
				disable-output-escaping="yes"
			/>
		</SCRIPT>
		<!-- Tawk.to Script -->
		<SCRIPT>
			<xsl:value-of
				select="/document/seo/tawk"
				disable-output-escaping="yes"
			/>
		</SCRIPT>
		<xsl:if test="/document/seo/isProductionHost">
			<SCRIPT>
				(function (g, d, o) {
				g._ltq = g._ltq || [];
				g._lt = g._lt || function () {
				g._ltq.push(arguments)
				};
				var h = location.protocol === 'https:' ? 'https://d.line-scdn.net' : 'http://d.line-cdn.net';
				var s = d.createElement('script');
				s.async = 1;
				s.src = o || h + '/n/line_tag/public/release/v1/lt.js';
				var t = d.getElementsByTagName('script')[0];
				t.parentNode.insertBefore(s, t);
				})(window, document);
				_lt('init', {
				customerType: 'lap',
				sharedCookieDomain: 'youngme.vip',
				tagId: 'de2dc0b2-f135-44ba-b7fe-31959fd7f2dd'
				});
				_lt('send', 'pv', ['de2dc0b2-f135-44ba-b7fe-31959fd7f2dd']);
			</SCRIPT>
			<NOSCRIPT>
				<IMG height="1" width="1" style="display:none"
				     src="https://tr.line.me/tag.gif?c_t=lap&#38;t_id=de2dc0b2-f135-44ba-b7fe-31959fd7f2dd&#38;e=pv&#38;noscript=1" />
			</NOSCRIPT>
		</xsl:if>
	</xsl:template>

	<!-- 後台需要的 -->
	<xsl:template name="dashScriptTags">
		<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha512-yUNtg0k40IvRQNR20bJ4oH6QeQ/mgs9Lsa6V+3qxTj58u2r+JiAYOhOW0o+ijuMmqCtCEg7LZRA+T4t84/ayVA==" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.5.0/perfect-scrollbar.min.js"/>
		<SCRIPT src="https://kit.fontawesome.com/5ed1767edc.js"/>
		<SCRIPT src="/SCRIPT/dashboard.js"/>
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
					<SPAN class="ms-1 font-weight-bold">養蜜 YoungMe 後台</SPAN>
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
						<A class="nav-link" href="/dashboard/vip.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-crown text-dark text-lg"></I>
							</DIV>
							<SPAN class="nav-link-text ms-1">男仕VIP紀錄</SPAN>
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
						<A class="nav-link" href="/dashboard/mePointsRecordsForMale.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-badge-dollar text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">男仕ME點紀錄</SPAN>
						</A>
					</LI>
					<LI class="nav-item">
						<A class="nav-link" href="/dashboard/broadcast.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-bullhorn text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">小編群發訊息</SPAN>
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
						<A class="nav-link" href="/dashboard/genHashtags.asp">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-comment-alt-edit text-dark text-lg"/>
							</DIV>
							<SPAN class="nav-link-text ms-1">新增文章標籤</SPAN>
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
					<LI class="nav-item mt-2">
						<SPAN class="ps-4 ms-2 text-xs font-weight-bolder text-primary">複製連結</SPAN>
					</LI>
					<LI class="nav-item">
						<DIV class="nav-link cursor-pointer" id="copyMemberLink">
							<DIV class="icon icon-shape icon-sm shadow border-radius-md me-2 d-flex align-items-center justify-content-center">
								<I class="fal fa-copy text-lg text-dark"></I>
							</DIV>
							<DIV class="m-0 p-0 nav-link-text ms-1">會員資料</DIV>
						</DIV>
					</LI>
				</UL>
			</DIV>
		</ASIDE>
	</xsl:template>

	<xsl:template name="dashTopNavBar">
		<!-- Navbar -->
		<NAV class="navbar navbar-main navbar-expand-lg px-0 mx-4 shadow-none border-radius-xl" id="navbarBlur" navbar-scroll="true">
			<DIV class="container-fluid py-1 px-2 px-sm-4">
				<H6 class="font-weight-bolder mb-0">養蜜後台</H6>
				<DIV class="collapse navbar-collapse" id="navbar">
					<UL class="navbar-nav justify-content-end ms-auto">
						<LI class="nav-item d-flex align-items-center">
							<A class="btn btn-outline-primary btn-round px-2 py-1 mb-0" href="/">
								<SPAN>前台</SPAN>
							</A>
						</LI>
						<LI class="nav-item">
							<A class="nav-link nav-link-icon d-flex align-items-center px-2" href="/signOut.asp">
								<I class="fad fa-sign-out fontSize22"/>
							</A>
						</LI>
						<LI class="nav-item d-xl-none d-flex align-items-center">
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