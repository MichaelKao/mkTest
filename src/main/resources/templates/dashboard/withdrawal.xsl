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
			<LINK href="/STYLE/withdrawal.css" rel="stylesheet"/>
		</HEAD>
		<BODY class="g-sidenav-show bg-gray-100">
			<xsl:call-template name="bootstrapToast"/>
			<xsl:call-template name="dashSideNavBar"/>
			<MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
				<xsl:call-template name="dashTopNavBar"/>
				<DIV class="container-fluid py-4 px-2">
					<DIV class="col-md-9 mx-auto">
						<xsl:apply-templates select="records"/>
					</DIV>
				</DIV>
			</MAIN>
			<xsl:call-template name="dashScriptTags"/>
			<SCRIPT src="/SCRIPT/withdrawalDash.js"/>
		</BODY>
	</xsl:template>
	<xsl:template match="records">
		<DIV class="nav-wrapper position-relative end-0 col-md-8 mx-auto">
			<UL class="nav nav-pills nav-fill">
				<LI class="nav-item">
					<A class="nav-link mb-0 px-0 py-1 active" data-bs-toggle="tab" href="#mainOne">
						<SPAN class="text-primary text-bold">待匯款</SPAN>
					</A>
				</LI>
				<LI class="nav-item">
					<A class="nav-link mb-0 px-0 py-1" data-bs-toggle="tab" href="#mainTwo">
						<SPAN class="text-primary text-bold">已完成匯款</SPAN>
					</A>
				</LI>
			</UL>
		</DIV>
		<DIV class="tab-content">
			<DIV class="tab-pane mt-3 active" id="mainOne">
				<DIV class="text-sm opacity-8 p-0">
					<UL class="list-group">
						<xsl:for-each select="record">
							<xsl:if test="@status = 'false'">
								<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2 border-radius-lg">
									<DIV class="d-flex flex-column">
										<DIV class="mb-3 text-sm">
											<A href="/profile/{@identifier}/">
												<xsl:value-of select="@name"/>
											</A>
											<SPAN class="text-dark ms-1 text-xs">
												<xsl:value-of select="@date"/>
											</SPAN>

										</DIV>
										<xsl:if test="wireTransfer">
											<DIV class="mb-2 text-xs">
												<SPAN>銀行代碼：</SPAN>
												<SPAN class="text-dark font-weight-bold">
													<xsl:value-of select="wireTransfer/@bankCode"/>
												</SPAN>
											</DIV>
											<DIV class="mb-2 text-xs">
												<SPAN>分行代碼：</SPAN>
												<SPAN class="text-dark font-weight-bold">
													<xsl:value-of select="wireTransfer/@branchCode"/>
												</SPAN>
											</DIV>
											<DIV class="mb-2 text-xs">
												<SPAN>戶名：</SPAN>
												<SPAN class="text-dark font-weight-bold">
													<xsl:value-of select="wireTransfer/@accountName"/>
												</SPAN>
											</DIV>
											<DIV class="mb-2 text-xs">
												<SPAN>匯款帳號：</SPAN>
												<SPAN class="text-dark font-weight-bold">
													<xsl:value-of select="wireTransfer/@accountNumber"/>
												</SPAN>
											</DIV>
										</xsl:if>
									</DIV>
									<DIV class="ms-auto text-end d-flex flex-column justify-content-around align-items-end">
										<INPUT name="identifier" type="hidden" value="{@identifier}"/>
										<INPUT name="status" type="hidden" value="{@status}"/>
										<INPUT name="timestamp" type="hidden" value="{@timestamp}"/>
										<BUTTON class="btn btn-success p-1 mb-0 success" type="button">
											<I class="fas fa-check me-1"></I>
											<SPAN>成功</SPAN>
										</BUTTON>
										<BUTTON class="btn btn-warning p-1 mb-0 fail" type="button">
											<I class="fas fa-times me-1"></I>
											<SPAN>失敗</SPAN>
										</BUTTON>
										<DIV>
											<SPAN class="text-xs">價值：</SPAN>
											<SPAN class="text-dark font-weight-bold text-sm">
												<xsl:value-of select="@points"/>
											</SPAN>
										</DIV>
										<BUTTON class="btn btn-info p-1 mb-0" data-bs-toggle="collapse" data-bs-target="#collapse{position()}" type="button">
											<I class="fas fa-eye me-1"></I>
											<SPAN>明細</SPAN>
										</BUTTON>
									</DIV>
								</LI>
								<DIV class="collapse border-radius-lg" id="collapse{position()}">
									<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2">
										<TABLE class="table align-items-center mb-0">
											<THEAD>
												<TR>
													<TH class="text-xs text-center">日期</TH>
													<TH class="text-xs text-center">男仕</TH>
													<TH class="text-xs text-center">ME點</TH>
													<TH class="text-xs text-center">價值</TH>
												</TR>
											</THEAD>
											<TBODY>
												<xsl:for-each select="history">
													<TR>
														<TD class="text-xs text-center">
															<SPAN>
																<xsl:value-of select="@date"/>
															</SPAN>
														</TD>
														<TD class="text-xs text-center">
															<A href="/profile/{@maleId}/">
																<xsl:value-of select="@male"/>
															</A>
														</TD>
														<TD class="text-xs text-center">
															<SPAN>
																<xsl:value-of select="@mePoints"/>
															</SPAN>
														</TD>
														<TD class="text-xs text-center">
															<SPAN>
																<xsl:value-of select="@points"/>
															</SPAN>
														</TD>
													</TR>
												</xsl:for-each>
											</TBODY>
										</TABLE>
									</LI>
								</DIV>
							</xsl:if>
						</xsl:for-each>
					</UL>
				</DIV>
			</DIV>
			<DIV class="tab-pane mt-3" id="mainTwo">
				<DIV class="text-sm opacity-8 p-0">
					<UL class="list-group">
						<xsl:for-each select="record">
							<xsl:if test="@status = 'true'">
								<LI class="list-group-item border-0 d-flex p-3 mb-2 border-radius-lg">
									<DIV class="text-sm m-0">
										<A href="/profile/{@identifier}/">
											<xsl:value-of select="@name"/>
										</A>
										<SPAN class="text-dark ms-1 text-xs">
											<xsl:value-of select="@date"/>
										</SPAN>

									</DIV>
									<DIV class="ms-auto d-flex align-items-center">
										<DIV>
											<SPAN class="text-xs">價值：</SPAN>
											<SPAN class="text-dark font-weight-bold text-sm">
												<xsl:value-of select="@points"/>
											</SPAN>
										</DIV>
										<BUTTON class="btn btn-info p-1 mb-0 ms-1" data-bs-toggle="collapse" data-bs-target="#success{position()}" type="button">
											<I class="fas fa-eye me-1"></I>
											<SPAN>明細</SPAN>
										</BUTTON>
									</DIV>
								</LI>
								<DIV class="collapse border-radius-lg" id="success{position()}">
									<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2">
										<TABLE class="table align-items-center mb-0">
											<THEAD>
												<TR>
													<TH class="text-xs text-center">日期</TH>
													<TH class="text-xs text-center">男仕</TH>
													<TH class="text-xs text-center">ME點</TH>
													<TH class="text-xs text-center">價值</TH>
												</TR>
											</THEAD>
											<TBODY>
												<xsl:for-each select="history">
													<TR>
														<TD class="text-xs text-center">
															<SPAN>
																<xsl:value-of select="@date"/>
															</SPAN>
														</TD>
														<TD class="text-xs text-center">
															<A href="/profile/{@maleId}/">
																<xsl:value-of select="@male"/>
															</A>
														</TD>
														<TD class="text-xs text-center">
															<SPAN>
																<xsl:value-of select="@mePoints"/>
															</SPAN>
														</TD>
														<TD class="text-xs text-center">
															<SPAN>
																<xsl:value-of select="@points"/>
															</SPAN>
														</TD>
													</TR>
												</xsl:for-each>
											</TBODY>
										</TABLE>
									</LI>
								</DIV>
							</xsl:if>
						</xsl:for-each>
					</UL>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>