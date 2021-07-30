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

	<xsl:template name="navbar">
		<DIV class="container position-sticky z-index-sticky top-0">
			<INPUT name="identifier" type="hidden" value="{@identifier}"/>
			<NAV class="navbar navbar-expand-lg blur blur-rounded top-0 z-index-3 shadow position-absolute my-3 py-2 start-0 end-0 mx-4">
				<DIV class="container-fluid">
					<A class="navbar-brand font-weight-bolder ms-sm-3" href="/">Young Me 甜蜜約會</A>
					<DIV>
						<xsl:if test="@signIn">
							<A class="d-lg-none" href="/activeLogs.asp">
								<I class="fal fa-bell"></I>
								<SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 announcement" style="display: none;">
									<xsl:if test="@announcement">
										<xsl:attribute name="style">display: inline;</xsl:attribute>
										<xsl:value-of select="@announcement"/>
									</xsl:if>
								</SPAN>
							</A>
						</xsl:if>
						<BUTTON class="navbar-toggler shadow-none ms-2" type="button" data-bs-toggle="collapse" data-bs-target="#navigation">
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
									<A class="nav-link cursor-pointer text-primary" id="dropdownMenuPages" data-bs-toggle="dropdown">
										<SPAN class="me-1">會員</SPAN>
										<I class="fas fa-chevron-down"></I>
									</A>
									<DIV class="dropdown-menu dropdown-menu-animation p-3 border-radius-lg mt-0 mt-lg-3">
										<DIV class="d-none d-lg-block">
											<A class="dropdown-item border-radius-md" href="/profile/">
												<SPAN>個人檔案</SPAN>
											</A>
											<A class="dropdown-item border-radius-md" href="/favorite.asp">
												<SPAN>我的收藏</SPAN>
											</A>
											<A class="dropdown-item border-radius-md" href="/looksMe.asp">
												<SPAN>誰看過我</SPAN>
											</A>
											<xsl:if test="@female">
												<A class="dropdown-item border-radius-md" href="/groupGreeting.asp">
													<SPAN>群發打招呼</SPAN>
												</A>
											</xsl:if>
											<A class="dropdown-item border-radius-md" href="/setting.asp">
												<SPAN>進階設定</SPAN>
											</A>
										</DIV>
										<DIV class="d-lg-none">
											<A class="dropdown-item border-radius-md" href="/profile/">
												<SPAN>個人檔案</SPAN>
											</A>
											<A class="dropdown-item border-radius-md" href="/favorite.asp">
												<SPAN>我的收藏</SPAN>
											</A>
											<A class="dropdown-item border-radius-md" href="/looksMe.asp">
												<SPAN>誰看過我</SPAN>
											</A>
											<xsl:if test="@female">
												<A class="dropdown-item border-radius-md" href="/groupGreeting.asp">
													<SPAN>群發打招呼</SPAN>
												</A>
											</xsl:if>
											<A class="dropdown-item border-radius-md" href="/setting.asp">
												<SPAN>進階設定</SPAN>
											</A>
										</DIV>
									</DIV>
								</LI>
								<xsl:if test="@almighty or @finance">
									<LI class="nav-item dropdown dropdown-hover">
										<A class="nav-link cursor-pointer text-primary" id="dropdownMenuPages" data-bs-toggle="dropdown">
											<SPAN class="me-1">後台</SPAN>
											<I class="fas fa-chevron-down"></I>
										</A>
										<DIV class="dropdown-menu dropdown-menu-animation p-3 border-radius-lg mt-0 mt-lg-3">
											<DIV class="d-none d-lg-block">
												<A class="dropdown-item border-radius-md" href="/dashboard/withdrawal.asp">
													<SPAN>甜心提領紀錄</SPAN>
												</A>
												<A class="dropdown-item border-radius-md" href="/dashboard/certification.asp">
													<SPAN>安心認證審核</SPAN>
												</A>
											</DIV>
											<DIV class="d-lg-none">
												<A class="dropdown-item border-radius-md" href="/dashboard/withdrawal.asp">
													<SPAN>甜心提領紀錄</SPAN>
												</A>
												<A class="dropdown-item border-radius-md" href="/dashboard/certification.asp">
													<SPAN>安心認證審核</SPAN>
												</A>
											</DIV>
										</DIV>
									</LI>
								</xsl:if>
								<xsl:if test="@signIn">
									<LI class="nav-item d-none d-lg-block">
										<A class="nav-link nav-link-icon" href="/activeLogs.asp">
											<I class="fal fa-bell"></I>
											<SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 announcement" style="display: none;">
												<xsl:if test="@announcement">
													<xsl:attribute name="style">display: inline;</xsl:attribute>
													<xsl:value-of select="@announcement"/>
												</xsl:if>
											</SPAN>
										</A>
									</LI>
								</xsl:if>
								<LI class="nav-item">
									<A class="nav-link nav-link-icon" href="/">
										<SPAN>
											<xsl:if test="@male">所有甜心</xsl:if>
											<xsl:if test="@female">所有男仕</xsl:if>
										</SPAN>
									</A>
								</LI>
								<xsl:if test="@female">
									<LI class="nav-item">
										<A class="nav-link nav-link-icon" href="/withdrawal.asp">
											<SPAN>提領車馬費</SPAN>
										</A>
									</LI>
								</xsl:if>
								<xsl:if test="@male">
									<LI class="nav-item">
										<A class="nav-link nav-link-icon" href="/recharge.asp">
											<SPAN>儲值愛心</SPAN>
										</A>
									</LI>
									<LI class="nav-item">
										<A class="nav-link nav-link-icon" href="/upgrade.asp">
											<SPAN>升級 VIP</SPAN>
										</A>
									</LI>
								</xsl:if>
							</xsl:if>
							<LI class="nav-item">
								<xsl:choose>
									<xsl:when test="@signIn">
										<A class="nav-link nav-link-icon" href="/signOut.asp">
											<SPAN>登出</SPAN>
										</A>
									</xsl:when>
									<xsl:otherwise>
										<A class="nav-link nav-link-icon" href="/signIn.asp">
											<SPAN>登入</SPAN>
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

	<xsl:template name="bodyScriptTags">
		<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha512-yUNtg0k40IvRQNR20bJ4oH6QeQ/mgs9Lsa6V+3qxTj58u2r+JiAYOhOW0o+ijuMmqCtCEg7LZRA+T4t84/ayVA==" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.5.0/perfect-scrollbar.min.js"/>
		<SCRIPT src="https://kit.fontawesome.com/5ed1767edc.js"/>
		<SCRIPT src="/SCRIPT/soft-design-system.min.js"/>
	</xsl:template>

	<xsl:template name="headLinkTags">
		<LINK href="/ICON/apple-touch-icon.png" rel="apple-touch-icon" sizes="180x180"/>
		<LINK href="/ICON/apple-touch-icon.png" rel="apple-touch-startup-image"/>
		<LINK href="/ICON/favicon-32x32.png" rel="icon" sizes="32x32" type="image/png"/>
		<LINK href="/ICON/favicon-16x16.png" rel="icon" sizes="16x16" type="image/png"/>
		<LINK href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet"/>
		<LINK href="/STYLE/soft-design-system.css" rel="stylesheet"/>
		<LINK href="/STYLE/default.css" rel="stylesheet"/>
	</xsl:template>

	<xsl:template name="headMetaTags">
		<META content="IE=edge" http-equiv="X-UA-Compatible"/>
		<META content="width=device-width, height=device-height, initial-scale=1.0, minimum-scale=1.0, maximum-scale=0, user-scalable=no, shrink-to-fit=no" name="viewport"/>
	</xsl:template>

	<xsl:template name="bootstrapToast">
		<DIV class="position-fixed top-5 end-0 p-3" style="z-index: 10000">
			<DIV class="toast fade hide bg-primary opacity-9" data-bs-delay="1800" >
				<BUTTON type="button" class="btn-close ms-2 mt-1" data-bs-dismiss="toast"></BUTTON>
				<DIV class="toast-body text-light"/>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>