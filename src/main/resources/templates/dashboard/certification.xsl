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
				<H4 class="text-center text-primary">安心認證審核</H4>
				<DIV class="card col-md-8 mx-auto">
					<DIV class="table-responsive">
						<TABLE class="table align-items-center mb-0">
							<THEAD>
								<TR>
									<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">會員</TH>
									<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">照片</TH>
									<TH class="text-secondary text-center text-xxs font-weight-bolder opacity-7">審核結果</TH>
								</TR>
							</THEAD>
							<TBODY>
								<xsl:for-each select="lover">
									<INPUT name="id" type="hidden" value="{@id}"/>
									<TR>
										<TD class="text-sm text-center">
											<SPAN class="text-secondary text-xs font-weight-bold">
												<xsl:value-of select="@name"/>
											</SPAN>
										</TD>
										<TD class="text-sm text-center">
											<BUTTON class="btn btn-dark p-1 mb-0 seePic" data-bs-target="#seePicModal" data-bs-toggle="modal" type="button">
												<I class="fas fa-eye me-1"></I>
												<SPAN>查看</SPAN>
											</BUTTON>
										</TD>
										<TD class="text-sm text-center">
											<BUTTON class="btn btn-success p-1 mb-0 me-2 success" type="button">
												<I class="fas fa-check me-1"></I>
												<SPAN>通過</SPAN>
											</BUTTON>
											<BUTTON class="btn btn-warning p-1 mb-0 fail" type="button">
												<I class="fas fa-times me-1"></I>
												<SPAN>不通過</SPAN>
											</BUTTON>
										</TD>
									</TR>
								</xsl:for-each>
							</TBODY>
						</TABLE>
					</DIV>
				</DIV>
				<DIV class="modal fade" id="seePicModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">審核不通過</H5>
							</DIV>
							<DIV class="modal-body">
								<IMG alt="identity" src="" style="max-width: 100%;"/>
							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn bg-gradient-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/certificationDash.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>