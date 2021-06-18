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
					<BUTTON class="navbar-toggler shadow-none ms-2" type="button" data-bs-toggle="collapse" data-bs-target="#navigation">
						<SPAN class="navbar-toggler-icon mt-2">
							<SPAN class="navbar-toggler-bar bar1"></SPAN>
							<SPAN class="navbar-toggler-bar bar2"></SPAN>
							<SPAN class="navbar-toggler-bar bar3"></SPAN>
						</SPAN>
					</BUTTON>
					<DIV class="collapse navbar-collapse pt-3 pb-2 py-lg-0 w-100" id="navigation">
						<UL class="navbar-nav navbar-nav-hover ms-lg-12 ps-lg-5 w-100 justify-content-end">
							<LI class="nav-item dropdown dropdown-hover mx-2">
								<A class="nav-link cursor-pointer" id="dropdownMenuPages" data-bs-toggle="dropdown">
									<SPAN class="mx-1">會員</SPAN>
									<i class="fas fa-chevron-down"></i>
								</A>
								<DIV class="dropdown-menu dropdown-menu-animation p-3 border-radius-lg mt-0 mt-lg-3">
									<DIV class="d-none d-lg-block">
										<H6 class="dropdown-header text-dark font-weight-bolder d-flex justify-content-cente align-items-center px-0">
											<I class="fas fa-home"></I>
											<SPAN class="ms-1">會員專區</SPAN>
										</H6>
										<xsl:choose>
											<xsl:when test="@me">
												<A class="dropdown-item border-radius-md" href="/signOut.asp">
													<span class="ps-3">登出</span>
												</A>
											</xsl:when>
											<xsl:otherwise>
												<A class="dropdown-item border-radius-md" href="/signIn.asp">
													<span class="ps-3">登入</span>
												</A>
												<A class="dropdown-item border-radius-md" href="/signUp.asp">
													<span class="ps-3">註冊</span>
												</A>
											</xsl:otherwise>
										</xsl:choose>

									</DIV>
									<DIV class="d-lg-none">
										<H6 class="dropdown-header text-dark font-weight-bolder d-flex justify-content-cente align-items-center px-0">
											<I class="fas fa-home"></I>
											<SPAN>會員專區</SPAN>
										</H6>
										<xsl:choose>
											<xsl:when test="@me">
												<A class="dropdown-item border-radius-md" href="/signOut.asp">
													<span class="ps-3">登出</span>
												</A>
											</xsl:when>
											<xsl:otherwise>
												<A class="dropdown-item border-radius-md" href="/signIn.asp">
													<span class="ps-3">登入</span>
												</A>
												<A class="dropdown-item border-radius-md" href="/signUp.asp">
													<span class="ps-3">註冊</span>
												</A>
											</xsl:otherwise>
										</xsl:choose>
									</DIV>
								</DIV>
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
	</xsl:template>

	<xsl:template name="headMetaTags">
		<META content="IE=edge" http-equiv="X-UA-Compatible"/>
		<META content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport"/>
	</xsl:template>

	<xsl:template name="bootstrapToast">
		<DIV class="position-fixed bottom-0 end-0 p-3" style="z-index: 5">
			<DIV class="toast hide" data-bs-delay="2500" >
				<BUTTON type="button" class="btn-close" data-bs-dismiss="toast">
					<I class="far fa-times ms-2 text-dark text-lg"></I>
				</BUTTON>
				<DIV class="toast-body"/>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>