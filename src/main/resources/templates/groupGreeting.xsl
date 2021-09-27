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
			<DIV class="modal fade" id="greetingRecord" tabindex="-1">
				<DIV class="modal-dialog modal-dialog-centered">
					<DIV class="modal-content">
						<DIV class="modal-body">
							<DIV class="d-flex">
								<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
									<I class="fal fa-times"></I>
								</BUTTON>
							</DIV>
							<DIV class="mt-3">
								<DIV class="mb-3">
									<DIV class="d-flex justify-content-start align-items-baseline mb-3">
										<I class="fad fa-comments-alt text-info fontSize50"></I>
										<SPAN class="text-bold text-dark mx-2">群發紀錄</SPAN>
									</DIV>
									<SPAN class="text-xs">可查看五天內的招呼紀錄</SPAN>
								</DIV>
								<DIV>
									<xsl:for-each select="records/record">
										<DIV class="mb-3">
											<DIV class="text-primary">
												<xsl:value-of select="@date"/>
											</DIV>
											<xsl:for-each select="history">
												<DIV class="d-flex align-items-center justify-content-start">
													<DIV>
														<A href="/profile/{@identifier}/">
															<IMG alt="profileImage" class="rounded-circle" src="{@profileImage}" width="40"/>
														</A>
													</DIV>
													<DIV class="ms-4">
														<A href="/profile/{@identifier}/">
															<SPAN class="me-2">
																<xsl:value-of select="@nickname"/>
															</SPAN>
															<SPAN>
																<xsl:value-of select="@age"/>
															</SPAN>
															<xsl:if test="@vvip">
																<SPAN class="ms-1">
																	<I class="fad fa-crown text-yellow text-shadow"></I>
																</SPAN>
															</xsl:if>
															<xsl:if test="@relief = 'true'">
																<SPAN class="ms-1">
																	<I class="fas fa-shield-check text-success text-shadow"></I>
																</SPAN>
															</xsl:if>
														</A>
													</DIV>
												</DIV>
												<HR class="horizontal dark my-1"/>
											</xsl:for-each>
										</DIV>
									</xsl:for-each>
								</DIV>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<DIV class="container pt-7 pt-md-8 text-center">
				<DIV class="row">
					<DIV class="col-lg-7 mx-auto d-flex justify-content-center flex-column">
						<H3 class="text-center">群發打招呼</H3>
						<BUTTON class="btn btn-outline-dark btn-round w-40 mx-auto" data-bs-target="#greetingRecord" data-bs-toggle="modal" type="button">群發紀錄</BUTTON>
						<P class="px-4">每 24 小時可依照您的地區標籤來向 30 位男仕群發打招呼一次。</P>
						<xsl:if test="not(@within24hr)">
							<FORM action="/groupGreeting.json" method="post">
								<DIV class="card-body">
									<DIV class="form-group mb-4">
										<LABEL for="greeting">招呼語</LABEL>
										<TEXTAREA class="form-control" id="greeting" name="greetingMessage" rows="4">
											<xsl:value-of select="@greeting"/>
										</TEXTAREA>
									</DIV>
									<DIV class="row">
										<DIV class="col-md-12">
											<BUTTON class="btn btn-primary btn-round w-100" type="submit">打招呼</BUTTON>
										</DIV>
									</DIV>
								</DIV>
							</FORM>
						</xsl:if>
						<xsl:if test="@within24hr">
							<I class="fad fa-alarm-exclamation text-primary fontSize50"></I>
							<DIV class="text-primary">24小時僅能群發一次</DIV>
							<SPAN class="text-primary">您在 <xsl:value-of select="@within24hr"/> 已有群發打招呼</SPAN>
						</xsl:if>
					</DIV>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/groupGreeting.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>