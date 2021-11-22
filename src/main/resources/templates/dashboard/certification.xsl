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
			<STYLE>.nav.nav-pills {background: #EDEDED;}</STYLE>
		</HEAD>
		<BODY class="g-sidenav-show bg-gray-100">
			<xsl:call-template name="bootstrapToast"/>
			<xsl:call-template name="dashSideNavBar"/>
			<MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
				<xsl:call-template name="dashTopNavBar"/>
				<DIV class="container-fluid py-4 px-2">
					<H4 class="text-center text-primary">安心認證審核</H4>
					<DIV class="nav-wrapper position-relative end-0 col-md-8 mx-auto">
						<UL class="nav nav-pills nav-fill">
							<LI class="nav-item">
								<A class="nav-link mb-0 px-0 py-1 active" data-bs-toggle="tab" href="#mainOne">
									<SPAN class="text-primary text-bold">待審核</SPAN>
								</A>
							</LI>
							<LI class="nav-item">
								<A class="nav-link mb-0 px-0 py-1" data-bs-toggle="tab" href="#mainTwo">
									<SPAN class="text-primary text-bold">審核結果</SPAN>
								</A>
							</LI>
						</UL>
					</DIV>
					<DIV class="tab-content">
						<DIV class="tab-pane mt-3 active" id="mainOne">
							<DIV class="col-11 col-md-8 mx-auto">
								<DIV class="row text-center mt-3 mb-1 text-xs">
									<DIV class="col-4 text-dark d-flex justify-content-start">審核結果</DIV>
									<DIV class="col-4 text-dark">會員</DIV>
									<DIV class="col-4 text-dark">照片</DIV>
								</DIV>
								<DIV>
									<xsl:for-each select="lover">
										<DIV class="row text-center align-items-center py-2 reliefWrap">
											<xsl:if test="position() mod 2 = 1">
												<xsl:attribute name="class">row text-center align-items-center bg-light border-radius-xl py-2 reliefWrap</xsl:attribute>
											</xsl:if>
											<INPUT name="id" type="hidden" value="{@id}"/>
											<DIV class="col-4 pe-0 d-flex flex-column flex-sm-row align-items-start">
												<DIV>
													<BUTTON class="btn btn-success p-2 mb-0 me-1 success" type="button">
														<I class="fas fa-check me-1"></I>
														<SPAN>通過</SPAN>
													</BUTTON>
												</DIV>
												<DIV>
													<BUTTON class="btn btn-warning dropdown-toggle p-2 mb-0 mt-1 mt-sm-0" data-bs-toggle="dropdown" type="button">
														<I class="fas fa-times me-1"></I>
														<SPAN>不通過</SPAN>
													</BUTTON>
													<UL class="dropdown-menu">
														<LI>
															<BUTTON class="dropdown-item fail" data-reason="AN_XIN_SHI_BAI_1">1. 本人和證件清晰需可辨識合照</BUTTON>
														</LI>
														<LI>
															<BUTTON class="dropdown-item fail" data-reason="AN_XIN_SHI_BAI_2">2. 照片中證件不可辨識</BUTTON>
														</LI>
														<LI>
															<BUTTON class="dropdown-item fail" data-reason="AN_XIN_SHI_BAI_3">3. 照片中本人不可辨識</BUTTON>
														</LI>
													</UL>
												</DIV>
											</DIV>
											<DIV class="col-4">
												<A class="text-secondary text-sm font-weight-bold" href="/profile/{@identifier}/">
													<xsl:value-of select="@name"/>
												</A>
											</DIV>
											<DIV class="col-4 p-0">
												<BUTTON class="btn btn-dark p-2 mb-0 seePic" data-bs-target="#seePicModal" data-bs-toggle="modal" type="button">
													<I class="fas fa-eye me-1"></I>
													<SPAN>查看</SPAN>
												</BUTTON>
											</DIV>
										</DIV>
									</xsl:for-each>
								</DIV>
							</DIV>
						</DIV>
						<DIV class="tab-pane mt-3" id="mainTwo">
							<DIV class="col-11 col-md-8 mx-auto">
								<DIV class="row text-center mt-3 mb-1 text-xs">
									<DIV class="col-4 text-dark d-flex justify-content-start">會員</DIV>
									<DIV class="col-4 text-dark">審核日期</DIV>
									<DIV class="col-4 text-dark">審核結果</DIV>
								</DIV>
								<DIV>
									<xsl:for-each select="record">
										<DIV class="row text-center align-items-center py-2">
											<xsl:if test="position() mod 2 = 1">
												<xsl:attribute name="class">row text-center align-items-center bg-light border-radius-xl py-2</xsl:attribute>
											</xsl:if>
											<DIV class="col-4 d-flex flex-column flex-sm-row align-items-start">
												<A class="text-sm font-weight-bold" href="/profile/{@identifier}/">
													<xsl:value-of select="@name"/>
												</A>
											</DIV>
											<DIV class="col-4">
												<SPAN class="text-sm">
													<xsl:value-of select="@date"/>
												</SPAN>
											</DIV>
											<DIV class="col-4">
												<SPAN class="text-sm">
													<xsl:value-of select="@result"/>
												</SPAN>
											</DIV>
										</DIV>
									</xsl:for-each>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="modal fade" id="seePicModal">
						<DIV class="modal-dialog modal-dialog-centered">
							<DIV class="modal-content">
								<DIV class="modal-header">
									<H5 class="modal-title">手持證件</H5>
								</DIV>
								<DIV class="modal-body">
									<IMG alt="identity" src="" style="max-width: 100%;"/>
								</DIV>
								<DIV class="modal-footer">
									<BUTTON class="btn btn-outline-dark" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</MAIN>
			<xsl:call-template name="dashScriptTags"/>
			<SCRIPT src="/SCRIPT/certificationDash.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>