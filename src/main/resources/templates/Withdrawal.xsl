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
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container pt-8 pb-4">
				<DIV class="modal fade" id="exampleModalLong" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
					<DIV class="modal-dialog" role="document">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title" id="exampleModalLongTitle">請選擇提領方式</H5>
								<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<BUTTON class="btn btn-light collapsed me-2" data-bs-target="#collapseOne" data-bs-toggle="collapse" type="button">銀行匯款</BUTTON>
								<BUTTON class="btn btn-light" data-bs-target="#collapseTwo" data-bs-toggle="collapse" type="button">Paypal</BUTTON>
								<DIV class="accordion" id="accordionRental">
									<DIV class="accordion-item mb-3">
										<DIV class="accordion-collapse collapse" data-bs-parent="#accordionRental" id="collapseOne">
											<DIV class="accordion-body text-sm opacity-8">
												<DIV class="text-center">
													<H3 class="text-gradient text-primary">銀行匯款</H3>
												</DIV>
												<FORM action="/wireTransfer.json" class="wireTransfer" method="post">
													<DIV class="card-body pb-2">
														<DIV class="row">
															<DIV class="col-md-6">
																<LABEL>銀行代碼</LABEL>
																<DIV class="input-group mb-3">
																	<INPUT class="form-control" placeholder="銀行代碼" name="wireTransferBankCode" type="text" value="{wire/@bankCode}"/>
																</DIV>
															</DIV>
															<DIV class="col-md-6 ps-md-2">
																<LABEL>分行代碼</LABEL>
																<DIV class="input-group mb-3">
																	<INPUT class="form-control" placeholder="分行代碼" name="wireTransferBranchCode" type="text" value="{wire/@branchCode}"/>
																</DIV>
															</DIV>
														</DIV>
														<DIV class="row">
															<DIV class="col-md-6">
																<LABEL>戶名</LABEL>
																<DIV class="input-group mb-3">
																	<INPUT class="form-control" placeholder="帳戶所有人名稱" name="wireTransferAccountName" type="text" value="{wire/@accountName}"/>
																</DIV>
															</DIV>
															<DIV class="col-md-6 ps-md-2">
																<LABEL>帳號</LABEL>
																<DIV class="input-group mb-3">
																	<INPUT class="form-control" placeholder="帳號" name="wireTransferAccountNumber" type="text" value="{wire/@accountNumber}"/>
																</DIV>
															</DIV>
														</DIV>
													</DIV>
													<DIV class="text-center">
														<BUTTON class="btn btn-lg bg-gradient-primary btn-lg mt-4 mb-0" type="submit">使用銀行匯款</BUTTON>
													</DIV>
												</FORM>
											</DIV>
										</DIV>
									</DIV>
									<DIV class="accordion-item mb-3">
										<DIV class="accordion-collapse collapse" data-bs-parent="#accordionRental" id="collapseTwo">
											<DIV class="accordion-body text-sm opacity-8">
												<DIV class="text-center">
													<H3 class="text-gradient text-primary">Paypal</H3>
												</DIV>
												<FORM action="" method="post">
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
										</DIV>
									</DIV>
								</DIV>
							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn bg-gradient-secondary" data-bs-dismiss="modal" type="button">Close</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="d-flex justify-content-center align-items-baseline">
					<DIV>目前可提領的車馬費：<xsl:value-of select="@points"/></DIV>
					<xsl:if test="@points != '0'">
						<BUTTON class="btn btn-block btn-default ms-2 p-2" data-bs-toggle="modal" data-bs-target="#exampleModalLong" type="button">提領</BUTTON>
					</xsl:if>
				</DIV>
				<DIV class="card col-md-8 mx-auto">
					<DIV class="table-responsive">
						<TABLE class="table align-items-center mb-0">
							<THEAD>
								<TR>
									<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">日期</TH>
									<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">提領金額</TH>
									<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">狀態</TH>
								</TR>
							</THEAD>
							<TBODY>
								<xsl:for-each select="record">
									<TR>
										<TD class="text-sm text-center">
											<P class="text-xs font-weight-bold mb-0">
												<xsl:value-of select="@date"/>
											</P>
											<P class="text-xs text-secondary mb-0">
												<xsl:value-of select="@way"/>
											</P>
										</TD>
										<TD class="text-sm text-center">
											<SPAN class="text-secondary text-xs font-weight-bold">
												<xsl:value-of select="@points"/>
											</SPAN>
										</TD>
										<TD class="text-sm text-center">
											<xsl:choose>
												<xsl:when test="@status = 'true'">
													<SPAN class="badge bg-gradient-success">已完成</SPAN>
												</xsl:when>
												<xsl:when test="@status = 'false'">
													<SPAN class="badge bg-gradient-warning">審核失敗</SPAN>
												</xsl:when>
												<xsl:otherwise>
													<SPAN class="badge bg-gradient-info">等待中</SPAN>
												</xsl:otherwise>
											</xsl:choose>
										</TD>
									</TR>
								</xsl:for-each>
							</TBODY>
						</TABLE>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/withdrawal.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>