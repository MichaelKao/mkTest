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
			<DIV class="container py-7">
				<H4 class="text-primary text-center">
					<xsl:value-of select="@title"/>
				</H4>
				<DIV class="modal fade" id="modal" role="dialog" tabindex="-1">
					<DIV class="modal-dialog" role="document">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">和女生要求Line</H5>
								<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<DIV class="form-group col-8">
									<LABEL class="h6" for="hello">招呼語</LABEL>
									<TEXTAREA class="form-control" id="hello" name="what" type="text">
										<xsl:value-of select="@greeting"/>
									</TEXTAREA>
								</DIV>
							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">
									<xsl:value-of select="@i18n-cancel"/>
								</BUTTON>
								<BUTTON class="btn btn-primary confirmBtn" type="submit">
									<xsl:value-of select="@i18n-confirm"/>
								</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="d-flex flex-column flex-md-row flex-wrap justify-content-center align-items-center mt-3">
					<DIV class="col-12 col-md-8 card card-frame mb-3 mx-2">
						<xsl:for-each select="history">
							<DIV class="card-body d-flex align-items-center justify-content-start py-0">
								<DIV>
									<A href="/profile/{@identifier}/">
										<IMG alt="profile_photo" class="border-radius-lg" src="{@profileImage}" width="70"/>
									</A>
								</DIV>
								<DIV class="ms-4 w-100 d-flex flex-column flex-md-row">
									<DIV>
										<SPAN class="text-xs font-weight-bold my-2">
											<xsl:value-of select="@time"/>
										</SPAN>
										<DIV class="text-dark">
											<xsl:value-of select="@message"/>
										</DIV>

									</DIV>
									<xsl:if test="@button">
										<DIV class="ms-0 ms-md-auto buttons">
											<INPUT name="whom" type="hidden" value="{@identifier}"/>
											<BUTTON class="btn btn-sm btn-outline-primary px-2 py-1 p-md-2 m-0 me-1 accept" type="button">接受</BUTTON>
											<BUTTON class="btn btn-sm btn-outline-primary px-2 py-1 p-md-2 m-0 me-1 refuse" type="button">拒絕</BUTTON>
										</DIV>
									</xsl:if>
									<xsl:if test="@lineButton">
										<DIV class="ms-0 ms-md-auto">
											<A class="btn btn-success px-2 py-1 px-md-3 py-md-2" href="{@lineButton}">加入好友</A>
										</DIV>
									</xsl:if>
									<xsl:if test="@giveMeLineButton">
										<DIV class="ms-0 ms-md-auto">
											<INPUT name="whom" type="hidden" value="{@identifier}"/>
											<BUTTON class="btn btn-success px-2 py-1 px-md-3 py-md-2 requestLine" type="button">要求 LINE</BUTTON>
										</DIV>
									</xsl:if>
								</DIV>
							</DIV>
							<HR class="horizontal dark"/>
						</xsl:for-each>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/activeLogs.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>