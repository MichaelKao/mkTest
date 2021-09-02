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
			<xsl:call-template name="headLinkTags"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container pt-7 px-2 px-md-3">
				<DIV class="modal fade" id="details">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">明細</H5>
							</DIV>
							<DIV class="modal-body">

							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn bg-gradient-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="col-md-9 mx-auto">
					<DIV class="card">
						<DIV class="card-body px-2 px-md-3">
							<xsl:apply-templates select="records"/>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/withdrawalDash.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
	<xsl:template match="records">
		<BUTTON class="btn btn-light collapsed me-2 px-3" data-bs-target="#collapseOne" data-bs-toggle="collapse" type="button">待匯款</BUTTON>
		<BUTTON class="btn btn-light me-2 px-3" data-bs-target="#collapseTwo" data-bs-toggle="collapse" type="button">成功</BUTTON>
		<DIV class="accordion" id="accordionRental">
			<DIV class="accordion-item mb-3">
				<DIV class="accordion-collapse collapse show" data-bs-parent="#accordionRental" id="collapseOne">
					<DIV class="accordion-body text-sm opacity-8 p-0">
						<DIV class="text-center">
							<H3 class="text-primary">待匯款</H3>
						</DIV>
						<UL class="list-group">
							<xsl:for-each select="record">
								<xsl:if test="@status = 'false'">
									<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2 bg-gray-100 border-radius-lg">
										<DIV class="d-flex flex-column">
											<DIV class="mb-3 text-sm h6">
												<A href="/profile/{@identifier}/">
													<xsl:value-of select="@name"/>
												</A>
												<SPAN class="text-dark font-weight-bold ms-1 text-xs">
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
												<SPAN class="text-xs">金額：</SPAN>
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
										<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2 bg-gray-100">
											<TABLE class="table align-items-center mb-0">
												<THEAD>
													<TR>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">日期</TH>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">男仕</TH>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">項目</TH>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">金額</TH>
													</TR>
												</THEAD>
												<TBODY>
													<xsl:for-each select="history">
														<TR>
															<TD class="text-sm text-center">
																<SPAN class="text-xs font-weight-bold">
																	<xsl:value-of select="@date"/>
																</SPAN>
															</TD>
															<TD class="text-sm text-center">
																<A class="text-secondary text-xs font-weight-bold" href="/profile/{@maleId}/">
																	<xsl:value-of select="@male"/>
																</A>
															</TD>
															<TD class="text-sm text-center">
																<SPAN class="text-secondary text-xs font-weight-bold">
																	<xsl:value-of select="@type"/>
																</SPAN>
															</TD>
															<TD class="text-sm text-center">
																<SPAN class="text-secondary text-xs font-weight-bold">
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
			<DIV class="accordion-item mb-3">
				<DIV class="accordion-collapse collapse" data-bs-parent="#accordionRental" id="collapseTwo">
					<DIV class="accordion-body text-sm opacity-8 p-0">
						<DIV class="text-center">
							<H3 class="text-primary">成功</H3>
						</DIV>
						<UL class="list-group">
							<xsl:for-each select="record">
								<xsl:if test="@status = 'true'">
									<LI class="list-group-item border-0 d-flex p-3 mb-2 bg-gray-100 border-radius-lg">
										<DIV class="text-sm h6 m-0">
											<A href="/profile/{@identifier}/">
												<xsl:value-of select="@name"/>
											</A>
											<SPAN class="text-dark font-weight-bold ms-1 text-xs">
												<xsl:value-of select="@date"/>
											</SPAN>

										</DIV>
										<DIV class="ms-auto d-flex align-items-center">
											<DIV>
												<SPAN class="text-xs">金額：</SPAN>
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
										<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2 bg-gray-100">
											<TABLE class="table align-items-center mb-0">
												<THEAD>
													<TR>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">日期</TH>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">男仕</TH>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">項目</TH>
														<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">金額</TH>
													</TR>
												</THEAD>
												<TBODY>
													<xsl:for-each select="history">
														<TR>
															<TD class="text-sm text-center">
																<SPAN class="text-xs font-weight-bold">
																	<xsl:value-of select="@date"/>
																</SPAN>
															</TD>
															<TD class="text-sm text-center">
																<A class="text-secondary text-xs font-weight-bold" href="/profile/{@maleId}/">
																	<xsl:value-of select="@male"/>
																</A>
															</TD>
															<TD class="text-sm text-center">
																<SPAN class="text-secondary text-xs font-weight-bold">
																	<xsl:value-of select="@type"/>
																</SPAN>
															</TD>
															<TD class="text-sm text-center">
																<SPAN class="text-secondary text-xs font-weight-bold">
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
		</DIV>
	</xsl:template>
</xsl:stylesheet>