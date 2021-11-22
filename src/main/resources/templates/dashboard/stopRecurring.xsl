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
			<STYLE>.nav.nav-pills {background: #EDEDED;}</STYLE>
		</HEAD>
		<BODY class="g-sidenav-show bg-gray-100">
			<xsl:call-template name="bootstrapToast"/>
			<xsl:call-template name="dashSideNavBar"/>
			<MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
				<xsl:call-template name="dashTopNavBar"/>
				<DIV class="container-fluid py-4 px-3">
					<DIV class="col-md-9 mx-auto">
						<DIV class="nav-wrapper position-relative end-0 col-md-8 mx-auto">
							<UL class="nav nav-pills nav-fill">
								<LI class="nav-item">
									<A class="nav-link mb-0 px-0 py-1 active" data-bs-toggle="tab" href="#mainOne">
										<SPAN class="text-primary text-bold">待處理</SPAN>
									</A>
								</LI>
								<LI class="nav-item">
									<A class="nav-link mb-0 px-0 py-1" data-bs-toggle="tab" href="#mainTwo">
										<SPAN class="text-primary text-bold">已處理</SPAN>
									</A>
								</LI>
							</UL>
						</DIV>
						<DIV class="tab-content">
							<DIV class="tab-pane mt-3 active" id="mainOne">
								<TABLE class="table align-items-center mb-0">
									<THEAD>
										<TR>
											<TH class="text-xs text-center">到期日期</TH>
											<TH class="text-xs text-center">用戶</TH>
											<TH class="text-xs text-center">用戶ID</TH>
											<TH class="text-xs text-center">作業</TH>
										</TR>
									</THEAD>
									<TBODY>
										<xsl:for-each select="pending">
											<TR>
												<TD class="text-xs text-center">
													<SPAN>
														<xsl:value-of select="@expiry"/>
													</SPAN>
												</TD>
												<TD class="text-xs text-center">
													<A href="/profile/{@identifier}/">
														<xsl:value-of select="@name"/>
													</A>
												</TD>
												<TD class="text-xs text-center">
													<SPAN>
														<xsl:value-of select="@id"/>
													</SPAN>
												</TD>
												<TD class="text-xs text-center">
													<INPUT name="applyID" type="hidden" value="{@applyID}"/>
													<BUTTON class="btn btn-success p-1 mb-0 success" type="button">
														<I class="fas fa-check me-1"></I>
														<SPAN>完成</SPAN>
													</BUTTON>
												</TD>
											</TR>
										</xsl:for-each>
									</TBODY>
								</TABLE>
							</DIV>
							<DIV class="tab-pane mt-3" id="mainTwo">
								<TABLE class="table align-items-center mb-0">
									<THEAD>
										<TR>
											<TH class="text-xs text-center">處理日期</TH>
											<TH class="text-xs text-center">用戶</TH>
											<TH class="text-xs text-center">用戶ID</TH>
										</TR>
									</THEAD>
									<TBODY>
										<xsl:for-each select="finished">
											<TR>
												<TD class="text-xs text-center">
													<SPAN>
														<xsl:value-of select="@handleDate"/>
													</SPAN>
												</TD>
												<TD class="text-xs text-center">
													<A href="/profile/{@identifier}/">
														<xsl:value-of select="@name"/>
													</A>
												</TD>
												<TD class="text-xs text-center">
													<SPAN>
														<xsl:value-of select="@id"/>
													</SPAN>
												</TD>
											</TR>
										</xsl:for-each>
									</TBODY>
								</TABLE>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</MAIN>
			<xsl:call-template name="dashScriptTags"/>
			<SCRIPT src="/SCRIPT/stopRecurringDash.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>