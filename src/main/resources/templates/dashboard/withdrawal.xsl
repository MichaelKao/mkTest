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
			<DIV class="container pt-7">
				<DIV class="modal fade" id="enterFailReason">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">審核不通過</H5>
							</DIV>
							<FORM action="/dashboard/fail.json" class="failForm" method="post">
								<DIV class="modal-body">
									<DIV class="form-group">
										<LABEL class="col-form-label" for="failReasonTextarea">原因：</LABEL>
										<TEXTAREA class="form-control" id="failReasonTextarea" name="failReason"></TEXTAREA>
									</DIV>
								</DIV>
								<DIV class="modal-footer">
									<BUTTON class="btn bg-gradient-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
									<BUTTON class="btn bg-gradient-primary" type="submit">送出</BUTTON>
								</DIV>
							</FORM>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="modal fade" id="failReason">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">審核不通過</H5>
							</DIV>
							<DIV class="modal-body">
								<DIV class="col-form-label">原因：</DIV>
								<DIV class="reason"></DIV>
							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn bg-gradient-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="col-md-9 mx-auto">
					<DIV class="card">
						<DIV class="card-header pb-0">
							<H6>甜心提領車馬費紀錄</H6>
						</DIV>
						<DIV class="card-body pt-0">
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
		<BUTTON class="btn btn-light px-3" data-bs-target="#collapseThree" data-bs-toggle="collapse" type="button">失敗</BUTTON>
		<DIV class="accordion" id="accordionRental">
			<DIV class="accordion-item mb-3">
				<DIV class="accordion-collapse collapse show" data-bs-parent="#accordionRental" id="collapseOne">
					<DIV class="accordion-body text-sm opacity-8 p-0">
						<DIV class="text-center">
							<H3 class="text-gradient text-primary">待匯款</H3>
						</DIV>
						<UL class="list-group">
							<xsl:for-each select="record">
								<xsl:if test="not(@status)">
									<INPUT name="id" type="hidden" value="{@id}"/>
									<LI class="list-group-item border-0 d-flex p-3 mb-2 bg-gray-100 border-radius-lg">
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
											<BUTTON class="btn btn-success p-1 mb-0 success" type="button">
												<I class="fas fa-check me-1"></I>
												<SPAN>成功</SPAN>
											</BUTTON>
											<BUTTON class="btn btn-warning p-1 mb-0 fail" data-bs-target="#enterFailReason" data-bs-toggle="modal" type="button">
												<I class="fas fa-times me-1"></I>
												<SPAN>失敗</SPAN>
											</BUTTON>
											<DIV>
												<SPAN class="text-xs">金額：</SPAN>
												<SPAN class="text-dark font-weight-bold text-sm">
													<xsl:value-of select="@points"/>
												</SPAN>
											</DIV>
										</DIV>
									</LI>
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
							<H3 class="text-gradient text-primary">成功</H3>
						</DIV>
						<UL class="list-group">
							<xsl:for-each select="record">
								<xsl:if test="@status = 'true'">
									<INPUT name="id" type="hidden" value="{@id}"/>
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
										</DIV>
									</LI>
								</xsl:if>
							</xsl:for-each>
						</UL>
					</DIV>
				</DIV>
			</DIV>
			<DIV class="accordion-item mb-3">
				<DIV class="accordion-collapse collapse" data-bs-parent="#accordionRental" id="collapseThree">
					<DIV class="accordion-body text-sm opacity-8 p-0">
						<DIV class="text-center">
							<H3 class="text-gradient text-primary">失敗</H3>
						</DIV>
						<UL class="list-group">
							<xsl:for-each select="record">
								<xsl:if test="@status = 'false'">
									<INPUT name="id" type="hidden" value="{@id}"/>
									<LI class="list-group-item border-0 d-flex p-3 mb-2 bg-gray-100 border-radius-lg">
										<DIV class="failReasonHidden d-none">
											<xsl:value-of select="@failReason"/>
										</DIV>
										<DIV class="text-sm h6 m-0 d-flex align-items-center">
											<A href="/profile/{@identifier}/">
												<xsl:value-of select="@name"/>
											</A>
											<SPAN class="text-dark font-weight-bold ms-1 text-xs">
												<xsl:value-of select="@date"/>
											</SPAN>

										</DIV>
										<DIV class="ms-auto text-end d-flex flex-column justify-content-around align-items-end">
											<BUTTON class="btn btn-danger p-1 mb-0 failReasonBtn" data-bs-target="#failReason" data-bs-toggle="modal">
												<I class="fas fa-question me-1"></I>
												<SPAN>原因</SPAN>
											</BUTTON>
											<DIV>
												<SPAN class="text-xs">金額：</SPAN>
												<SPAN class="text-dark font-weight-bold text-sm">
													<xsl:value-of select="@points"/>
												</SPAN>
											</DIV>
										</DIV>
									</LI>
								</xsl:if>
							</xsl:for-each>
						</UL>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>