<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output
		encoding="UTF-8"
		media-type="text/html"
		method="html"
		indent="no"
		omit-xml-declaration="yes"
	/>

	<xsl:include href="default.xsl"/>

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
			<LINK href="/STYLE/withdrawal.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-6 py-md-7 px-3">
				<DIV class="modal fade" id="paymentModal" tabindex="-1">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<DIV class="nav-wrapper col-11 mx-auto mt-3">
									<UL class="nav nav-pills nav-fill">
										<LI class="nav-item">
											<A class="nav-link mb-0 px-0 py-1 active" data-bs-toggle="tab" href="#wire">
												<SPAN class="text-primary text-bold">銀行匯款</SPAN>

											</A>
										</LI>
										<!--										<LI class="nav-item">
											<A class="nav-link mb-0 px-0 py-1" data-bs-toggle="tab" href="#paypal">
												<SPAN class="text-primary text-bold">Paypal</SPAN>
											</A>
										</LI>-->
									</UL>
								</DIV>
								<DIV class="tab-content">
									<DIV class="tab-pane active" id="wire">
										<DIV class="text-sm opacity-8 p-0">
											<FORM action="/wireTransfer.json" class="wireTransfer" method="post">
												<INPUT name="historyId" type="hidden" value=""/>
												<DIV class="card-body pb-2">
													<DIV class="row">
														<DIV class="col-md-6">
															<LABEL>銀行代碼</LABEL>
															<DIV class="input-group mb-2">
																<INPUT class="form-control" placeholder="銀行代碼" name="wireTransferBankCode" type="text" value="{wire/@bankCode}"/>
															</DIV>
														</DIV>
														<DIV class="col-md-6 ps-md-2">
															<LABEL>分行代碼</LABEL>
															<DIV class="input-group mb-2">
																<INPUT class="form-control" placeholder="分行代碼" name="wireTransferBranchCode" type="text" value="{wire/@branchCode}"/>
															</DIV>
														</DIV>
													</DIV>
													<DIV class="row">
														<DIV class="col-md-6">
															<LABEL>戶名</LABEL>
															<DIV class="input-group mb-2">
																<INPUT class="form-control" placeholder="帳戶所有人名稱" name="wireTransferAccountName" type="text" value="{wire/@accountName}"/>
															</DIV>
														</DIV>
														<DIV class="col-md-6 ps-md-2">
															<LABEL>帳號</LABEL>
															<DIV class="input-group mb-2">
																<INPUT class="form-control" placeholder="帳號" name="wireTransferAccountNumber" type="text" value="{wire/@accountNumber}"/>
															</DIV>
														</DIV>
													</DIV>
												</DIV>
												<DIV class="text-center">
													<SPAN class="text-primary text-bold">開立發票可減免一成手續費，請聯繫客服辦理</SPAN>
													<BUTTON class="btn btn-primary btn-lg mb-0" type="submit">使用銀行匯款</BUTTON>
												</DIV>
											</FORM>
										</DIV>
									</DIV>
									<!--									<DIV class="tab-pane" id="paypal">
										<DIV class="text-sm opacity-8">
											<FORM action="" method="post">
												<INPUT name="historyId" type="hidden" value=""/>
												<DIV class="card-body pb-2">
													<DIV class="row">
														<DIV class="col-md-6">
															<LABEL>名字</LABEL>
															<DIV class="input-group mb-4">
																<INPUT class="form-control" placeholder="全名" type="text"/>
															</DIV>
														</DIV>
														<DIV class="col-md-6 ps-md-2">
															<LABEL>Paypal Email</LABEL>
															<DIV class="input-group">
																<INPUT type="email" class="form-control" placeholder="Email"/>
															</DIV>
														</DIV>
													</DIV>
												</DIV>
												<DIV class="text-center">
													<BUTTON class="btn btn-lg bg-gradient-primary btn-lg mt-4 mb-0" type="submit">使用 Paypal</BUTTON>
												</DIV>
											</FORM>
										</DIV>
									</DIV>-->
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="nav-wrapper position-relative end-0 col-md-8 mx-auto">
					<UL class="nav nav-pills nav-fill">
						<LI class="nav-item">
							<A class="nav-link mb-0 px-0 py-1 active" data-bs-toggle="tab" href="#mainOne">
								<SPAN class="text-primary text-bold">提領</SPAN>
							</A>
						</LI>
						<LI class="nav-item">
							<A class="nav-link mb-0 px-0 py-1" data-bs-toggle="tab" href="#mainTwo">
								<SPAN class="text-primary text-bold">等待中</SPAN>
							</A>
						</LI>
						<LI class="nav-item">
							<A class="nav-link mb-0 px-0 py-1" data-bs-toggle="tab" href="#mainThree">
								<SPAN class="text-primary text-bold">歷史紀錄</SPAN>
							</A>
						</LI>
					</UL>
				</DIV>
				<DIV class="tab-content">
					<DIV class="tab-pane mt-3 active" id="mainOne">
						<DIV class="text-sm opacity-8 p-0 col-md-8 mx-auto">
							<DIV class="card p-3">
								<H6 class="text-center text-bold text-primary">目前可提領的總價值：<xsl:value-of select="@totalPoints"/></H6>
								<DIV class="text-xs text-center">
									<SPAN>僅能提領</SPAN>
									<xsl:value-of select="@before7days"/>
									<SPAN>前(7天以前)的紀錄</SPAN>
								</DIV>
								<DIV class="my-1 text-danger text-xs text-center">
									<SPAN>
										<I class="far fa-wallet me-1"></I>
									</SPAN>
									<SPAN>換算台幣 = 0.6 , 提領有一成金流手續費</SPAN>
								</DIV>
								<DIV class="my-1 text-danger text-xs text-center">
									<SPAN>
										<I class="far fa-wallet me-1"></I>
									</SPAN>
									<SPAN>透過邀請碼推薦男仕,再提升20%分潤!!</SPAN>
								</DIV>
								<TABLE class="table align-items-center mb-0">
									<THEAD>
										<TR>
											<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">日期</TH>
											<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">男仕</TH>
											<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">ME點</TH>
											<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">價值</TH>
										</TR>
									</THEAD>
									<TBODY>
										<TR style="background: #f3f3f3;">
											<TD class="text-center">
												<SPAN class="font-weight-bold text-xs">範例 </SPAN>
												<SPAN class="text-xs font-weight-bold">2021-05-25</SPAN>
											</TD>
											<TD class="text-sm text-center">
												<SPAN class="text-secondary text-xs font-weight-bold">養蜜</SPAN>
											</TD>
											<TD class="text-sm text-center">
												<SPAN class="text-secondary text-xs font-weight-bold">1000</SPAN>
											</TD>
											<TD class="text-sm text-center">
												<SPAN class="text-secondary text-xs font-weight-bold">540</SPAN>
											</TD>
										</TR>
										<xsl:for-each select="record">
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
														<xsl:value-of select="@mePoints"/>
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
								<xsl:if test="(@totalPoints &gt; 0)">
									<xsl:if test="(@Relief='1')">
										<BUTTON class="btn btn-block btn-primary m-0 py-2 withdrawal border-radius-xl mt-2" data-bs-toggle="modal" data-bs-target="#paymentModal" type="button">提領</BUTTON>
									</xsl:if>
									<xsl:if test="(@Relief='0')">
										<BUTTON class="btn btn-block btn-primary m-0 py-2 border-radius-xl mt-2"  type="button">您還不能執行此功能,請先完成安心認證,增加帳戶信任度</BUTTON>
									</xsl:if>

								</xsl:if>



							</DIV>
							<DIV class="card p-3 mt-3">
								<DIV class="text-center mb-3">
									<H6 class="text-primary">尚未能提領的紀錄</H6>
									<DIV class="text-bold text-xs">
										<DIV>如約會臨時無法出席，</DIV>
										<DIV>可以於48小時內退回 ME 點</DIV>
										<DIV>避免被檢舉與誤會的機會唷</DIV>
									</DIV>
								</DIV>
								<xsl:for-each select="notAbleToWithdrawal">
									<DIV class="d-flex">
										<DIV>
											<A href="/profile/{@maleId}/" class="text-bold text-sm">
												<xsl:value-of select="@male"/>
											</A>
											<SPAN class="text-dark font-weight-bold ms-1 text-xs">
												<xsl:value-of select="@date"/>
											</SPAN>
											<DIV>
												<SPAN>收到</SPAN>
												<SPAN>
													<xsl:value-of select="@type"/>
												</SPAN>
												<SPAN>
													<xsl:value-of select="@points"/>
												</SPAN>
												<SPAN class="ms-2 text-xs">(ME點: <xsl:value-of select="@mePoints"/>)</SPAN>
											</DIV>
										</DIV>
										<xsl:if test="@ableToReturn">
											<DIV class="ms-auto text-end d-flex flex-column justify-content-around align-items-end">
												<INPUT name="returnFareId" type="hidden" value="{@returnFareId}"/>
												<BUTTON class="btn btn-warning px-2 py-1 mb-0 returnFare" type="button">
													<I class="fas fa-trash-undo me-1"></I>
													<SPAN>退回</SPAN>
												</BUTTON>
											</DIV>
										</xsl:if>
									</DIV>
									<hr/>
								</xsl:for-each>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="tab-pane mt-3" id="mainTwo">
						<DIV class="text-sm opacity-8 p-0 col-md-8 mx-auto">
							<UL class="list-group">
								<xsl:for-each select="historyRecord">
									<xsl:if test="@status = 'false'">
										<LI class="list-group-item border-0 d-flex p-3 mb-2 border-radius-lg">
											<DIV class="text-sm h6 m-0">
												<DIV>
													<xsl:value-of select="@way"/>
												</DIV>
												<DIV class="text-dark font-weight-bold text-xs">
													<xsl:value-of select="@date"/>
												</DIV>

											</DIV>
											<DIV class="ms-auto d-flex align-items-center">
												<DIV>
													<SPAN class="text-xs">價值</SPAN>
													<SPAN class="text-dark font-weight-bold text-sm ms-1">
														<xsl:value-of select="@points"/>
													</SPAN>
												</DIV>
												<BUTTON class="btn btn-info p-1 mb-0 ms-1 details" data-bs-toggle="collapse" data-bs-target="#success{position()}" type="button">
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
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">日期</TH>
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">男仕</TH>
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">ME點</TH>
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">價值</TH>
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
																		<xsl:value-of select="@mePoints"/>
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
					<DIV class="tab-pane mt-3" id="mainThree">
						<DIV class="text-sm opacity-8 p-0 col-md-8 mx-auto">
							<UL class="list-group">
								<xsl:for-each select="historyRecord">
									<xsl:if test="@status = 'true'">
										<LI class="list-group-item border-0 d-flex p-3 mb-2 border-radius-lg">
											<DIV class="text-sm h6 m-0">
												<DIV>
													<xsl:value-of select="@way"/>
												</DIV>
												<DIV class="text-dark font-weight-bold text-xs">
													<xsl:value-of select="@date"/>
												</DIV>

											</DIV>
											<DIV class="ms-auto d-flex align-items-center">
												<DIV>
													<SPAN class="text-xs">價值</SPAN>
													<SPAN class="text-dark font-weight-bold text-sm ms-1">
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
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">日期</TH>
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">男仕</TH>
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">ME點</TH>
															<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">價值</TH>
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
																		<xsl:value-of select="@mePoints"/>
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
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/withdrawal.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>