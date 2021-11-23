<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output
		encoding="UTF-8"
		media-type="text/html"
		method="html"
		indent="no"
		omit-xml-declaration="yes"
	/>

	<xsl:include href="../default.xsl"/>

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
			<xsl:call-template name="dashHeadLinkTags"/>
		</HEAD>
		<BODY class="g-sidenav-show bg-gray-100">
			<xsl:call-template name="bootstrapToast"/>
			<xsl:call-template name="dashSideNavBar"/>
			<MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg">
				<xsl:call-template name="dashTopNavBar"/>
				<DIV class="container-fluid py-4 px-2">
					<DIV class="col-11 col-md-7 mx-auto">
						<FORM action="/dashboard/searchVip.json" method="post">
							<DIV class="form-group mb-0">
								<LABEL for="vipType">VIP類型</LABEL>
								<SELECT class="form-control" id="vipType" name="vipType">
									<OPTION value="all">全部</OPTION>
									<OPTION value="1288">1288長期</OPTION>
									<OPTION value="1688">1688短期</OPTION>
									<OPTION value="trial">單日體驗</OPTION>
								</SELECT>
							</DIV>
							<DIV class="form-group m-0">
								<LABEL for="login">手機號碼</LABEL>
								<INPUT class="form-control" id="login" inputmode="numeric" name="login" placeholder="0912345678" type="text"/>
							</DIV>
							<BUTTON class="btn btn-dark btn-round w-100 my-2 py-2" type="submit">搜尋</BUTTON>
						</FORM>
						<HR/>
						<DIV class="row text-center mt-3 mb-1 text-xs">
							<DIV class="col-2 text-dark d-flex justify-content-start">暱稱/帳號</DIV>
							<DIV class="col-1 text-dark">類型</DIV>
							<DIV class="col-3 text-dark">付款日期</DIV>
							<DIV class="col-3 text-dark">開始日期</DIV>
							<DIV class="col-3 text-dark">結束日期</DIV>
						</DIV>
						<DIV class="text-xs maleMembers">
							<xsl:for-each select="vips/vip">
								<DIV class="row text-center align-items-center py-2">
									<xsl:if test="position() mod 2 = 1">
										<xsl:attribute name="class">row text-center align-items-center bg-light border-radius-xl py-2</xsl:attribute>
									</xsl:if>
									<DIV class="col-2 d-flex justify-content-start">
										<A class="d-flex flex-column align-items-start" href="/profile/{@maleIdentifier}/">
											<SPAN class="text-primary">
												<xsl:value-of select="@nickname"/>
											</SPAN>
											<DIV class="text-secondary">
												<xsl:value-of select="@login"/>
											</DIV>
										</A>
									</DIV>
									<DIV class="col-1 d-flex justify-content-center">
										<SPAN>
											<xsl:value-of select="@type"/>
										</SPAN>
									</DIV>
									<DIV class="col-3 d-flex justify-content-center">
										<SPAN>
											<xsl:value-of select="@paidDate"/>
										</SPAN>
									</DIV>
									<DIV class="col-3 d-flex justify-content-center">
										<SPAN>
											<xsl:value-of select="@startDate"/>
										</SPAN>
									</DIV>
									<DIV class="col-3">
										<SPAN>
											<xsl:value-of select="@endDate"/>
										</SPAN>
									</DIV>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
				</DIV>
			</MAIN>
			<xsl:call-template name="bootstrapToast"/>
			<xsl:call-template name="dashScriptTags"/>
			<SCRIPT src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"/>
			<SCRIPT src="/SCRIPT/vip.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>