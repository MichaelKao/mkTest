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
			<NAV class="navbar navbar-expand-lg blur blur-rounded top-0 z-index-3 shadow position-absolute my-3 py-2 start-0 end-0 mx-4">
				<DIV class="container-fluid">
					<A class="navbar-brand font-weight-bolder ms-sm-3" href="/">訂製情人</A>
					<DIV>
						<xsl:if test="@signIn">
							<A class="d-lg-none" href="/activeLogs.asp">
								<I class="fal fa-bell"></I>
								<xsl:if test="@announcement">
									<SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 announcement">
										<xsl:value-of select="@announcement"/>
									</SPAN>
								</xsl:if>
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
											<A class="dropdown-item border-radius-md" href="/activeLogs.asp">
												<SPAN>動態紀錄</SPAN>
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
											<A class="dropdown-item border-radius-md" href="/activeLogs.asp">
												<SPAN>動態紀錄</SPAN>
											</A>
										</DIV>
									</DIV>
								</LI>
								<xsl:if test="@signIn">
									<LI class="nav-item d-none d-lg-block">
										<A class="nav-link nav-link-icon" href="/activeLogs.asp">
											<I class="fal fa-bell"></I>
											<xsl:if test="@announcement">
												<SPAN class="text-xs text-light bg-warning border-radius-md ms-n1 announcement">
													<xsl:value-of select="@announcement"/>
												</SPAN>
											</xsl:if>
										</A>
									</LI>
								</xsl:if>
								<xsl:if test="@male">
									<LI class="nav-item">
										<A class="nav-link nav-link-icon" href="/deposit.asp">
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
		<LINK href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet"/>
		<LINK href="/STYLE/soft-design-system.css" rel="stylesheet"/>
		<LINK href="/STYLE/default.css" rel="stylesheet"/>
	</xsl:template>

	<xsl:template name="headMetaTags">
		<META content="IE=edge" http-equiv="X-UA-Compatible"/>
		<META content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport"/>
	</xsl:template>

	<xsl:template name="bootstrapToast">
		<DIV class="position-fixed top-5 end-0 p-3" style="z-index: 10000">
			<DIV class="toast hide" data-bs-delay="1500" >
				<BUTTON type="button" class="btn-close" data-bs-dismiss="toast">
					<I class="far fa-times ms-2 text-dark text-lg"></I>
				</BUTTON>
				<DIV class="toast-body"/>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>