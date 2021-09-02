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
				<DIV class="col-md-9 mx-auto">
					<DIV class="card">
						<DIV class="card-body px-2 px-md-3">
							<BUTTON class="btn btn-light collapsed me-2 px-3" data-bs-target="#collapseOne" data-bs-toggle="collapse" type="button">待處理</BUTTON>
							<BUTTON class="btn btn-light me-2 px-3" data-bs-target="#collapseTwo" data-bs-toggle="collapse" type="button">已處理</BUTTON>
							<DIV class="accordion" id="accordionRental">
								<DIV class="accordion-item mb-3">
									<DIV class="accordion-collapse collapse show" data-bs-parent="#accordionRental" id="collapseOne">
										<DIV class="accordion-body text-sm opacity-8 p-0">
											<DIV class="text-center">
												<H3 class="text-primary">待處理</H3>
											</DIV>
											<UL class="list-group">
												<DIV class="border-radius-lg">
													<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2 bg-gray-100">
														<TABLE class="table align-items-center mb-0">
															<THEAD>
																<TR>
																	<TH class="text-secondary text-xxs font-weight-bolder opacity-7">到期日期</TH>
																	<TH class="text-secondary text-xxs font-weight-bolder opacity-7">用戶</TH>
																	<TH class="text-secondary text-xxs font-weight-bolder opacity-7">用戶ID</TH>
																	<TH class="text-secondary text-xxs font-weight-bolder opacity-7">作業</TH>
																</TR>
															</THEAD>
															<TBODY>
																<xsl:for-each select="pending">
																	<TR>
																		<TD class="text-sm">
																			<SPAN class="text-xs font-weight-bold">
																				<xsl:value-of select="@expiry"/>
																			</SPAN>
																		</TD>
																		<TD class="text-sm">
																			<A class="text-secondary text-xs font-weight-bold" href="/profile/{@identifier}/">
																				<xsl:value-of select="@name"/>
																			</A>
																		</TD>
																		<TD class="text-sm">
																			<SPAN class="text-secondary text-xs font-weight-bold">
																				<xsl:value-of select="@id"/>
																			</SPAN>
																		</TD>
																		<TD class="text-sm">
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
													</LI>
												</DIV>
											</UL>
										</DIV>
									</DIV>
								</DIV>
								<DIV class="accordion-item mb-3">
									<DIV class="accordion-collapse collapse" data-bs-parent="#accordionRental" id="collapseTwo">
										<DIV class="accordion-body text-sm opacity-8 p-0">
											<DIV class="text-center">
												<H3 class="text-primary">已處理</H3>
											</DIV>
											<UL class="list-group">
												<DIV class="border-radius-lg">
													<LI class="list-group-item border-0 d-flex p-2 p-md-3 mb-2 bg-gray-100">
														<TABLE class="table align-items-center mb-0">
															<THEAD>
																<TR>
																	<TH class="text-secondary text-xxs font-weight-bolder opacity-7">處理日期</TH>
																	<TH class="text-secondary text-xxs font-weight-bolder opacity-7">用戶</TH>
																	<TH class="text-secondary text-xxs font-weight-bolder opacity-7">用戶ID</TH>
																</TR>
															</THEAD>
															<TBODY>
																<xsl:for-each select="finished">
																	<TR>
																		<TD class="text-sm">
																			<SPAN class="text-xs font-weight-bold">
																				<xsl:value-of select="@handleDate"/>
																			</SPAN>
																		</TD>
																		<TD class="text-sm">
																			<A class="text-secondary text-xs font-weight-bold" href="/profile/{@identifier}/">
																				<xsl:value-of select="@name"/>
																			</A>
																		</TD>
																		<TD class="text-sm">
																			<SPAN class="text-secondary text-xs font-weight-bold">
																				<xsl:value-of select="@id"/>
																			</SPAN>
																		</TD>
																	</TR>
																</xsl:for-each>
															</TBODY>
														</TABLE>
													</LI>
												</DIV>
											</UL>
										</DIV>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/stopRecurringDash.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>