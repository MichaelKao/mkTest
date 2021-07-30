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
						<DIV class="modal-header">
							<H6 class="modal-title">群發紀錄</H6>
							<SPAN class="text-xs ms-1">查看五天內的招呼紀錄</SPAN>
							<BUTTON class="btn-close bg-dark" data-bs-dismiss="modal" type="button"></BUTTON>
						</DIV>
						<DIV class="modal-body">
							<xsl:for-each select="history">
								<DIV class="d-flex align-items-center justify-content-start py-1">
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
											<xsl:if test="@vip">
												<SPAN class="ms-1">
													<IMG class="border-radius-md" src="/vip.svg" width="20"/>
												</SPAN>
											</xsl:if>
											<xsl:if test="@relief = 'true'">
												<SPAN class="ms-1">
													<IMG class="border-radius-md" src="/accept.svg" width="20"/>
												</SPAN>
											</xsl:if>
											<DIV class="text-xs text-secondary text-lighter">
												<xsl:value-of select="@date"/>
											</DIV>
										</A>
									</DIV>
								</DIV>
								<HR class="horizontal dark mb-1"/>
							</xsl:for-each>
						</DIV>
						<DIV class="modal-footer">
							<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">關閉</BUTTON>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<DIV class="container py-8 text-center">
				<DIV class="row">
					<DIV class="col-lg-7 mx-auto d-flex justify-content-center flex-column">
						<H3 class="text-center">群發打招呼</H3>
						<BUTTON class="btn bg-gradient-dark w-40 mx-auto" data-bs-target="#greetingRecord" data-bs-toggle="modal" type="button">群發紀錄</BUTTON>
						<P class="px-4">每 24 小時可依照您的地區標籤來向 30 位男仕群發打招呼一次。</P>
						<xsl:if test="not(@within24hr)">
							<FORM action="/groupGreeting.json" method="post">
								<DIV class="card-body">
									<DIV class="form-group mb-4">
										<LABEL for="greetingMessage">招呼語</LABEL>
										<TEXTAREA class="form-control" id="greetingMessage" name="greetingMessage" rows="4">
											<xsl:value-of select="@greeting"/>
										</TEXTAREA>
									</DIV>
									<DIV class="row">
										<DIV class="col-md-12">
											<button class="btn bg-gradient-primary w-100" type="submit">打招呼</button>
										</DIV>
									</DIV>
								</DIV>
							</FORM>
						</xsl:if>
						<xsl:if test="@within24hr">
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