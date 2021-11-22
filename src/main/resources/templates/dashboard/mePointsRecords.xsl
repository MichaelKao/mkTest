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
			<LINK href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" rel="stylesheet"/>
		</HEAD>
		<BODY class="g-sidenav-show bg-gray-100">
			<xsl:call-template name="bootstrapToast"/>
			<xsl:call-template name="dashSideNavBar"/>
			<MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg">
				<xsl:call-template name="dashTopNavBar"/>
				<DIV class="container-fluid py-4 px-2">
					<DIV class="col-11 col-md-7 mx-auto">
						<DIV class="row text-center mt-3 mb-1 text-xs">
							<DIV class="col-3 text-dark d-flex justify-content-start">暱稱/帳號</DIV>
							<DIV class="col-3 text-dark">收到的甜心</DIV>
							<DIV class="col-3 text-dark">日期</DIV>
							<DIV class="col-2 text-dark p-0">ME點</DIV>
							<DIV class="col-1 text-dark p-0">退回</DIV>
						</DIV>
						<DIV class="text-xs maleMembers">
							<xsl:for-each select="records/record">
								<DIV class="row text-center align-items-center py-2">
									<xsl:if test="position() mod 2 = 1">
										<xsl:attribute name="class">row text-center align-items-center bg-light border-radius-xl py-2</xsl:attribute>
									</xsl:if>
									<DIV class="col-3 d-flex justify-content-start">
										<A class="d-flex flex-column align-items-start" href="/profile/{@maleIdentifier}/">
											<SPAN class="text-primary">
												<xsl:value-of select="@maleNickname"/>
											</SPAN>
											<DIV class="text-secondary">
												<xsl:value-of select="@maleLogin"/>
											</DIV>
										</A>
									</DIV>
									<DIV class="col-3 d-flex justify-content-center">
										<A class="d-flex flex-column align-items-start" href="/profile/{@femaleIdentifier}/">
											<SPAN class="text-primary">
												<xsl:value-of select="@femaleNickname"/>
											</SPAN>
											<DIV class="text-secondary">
												<xsl:value-of select="@femaleLogin"/>
											</DIV>
										</A>
									</DIV>
									<DIV class="col-3 d-flex justify-content-center">
										<SPAN>
											<xsl:value-of select="@date"/>
										</SPAN>
									</DIV>
									<DIV class="col-2">
										<SPAN>
											<xsl:value-of select="@mePoints"/>
										</SPAN>
									</DIV>
									<DIV class="col-1 p-0">
										<xsl:if test="not(@notAbleToReturn)">
											<BUTTON class="btn btn-link m-0 px-0 py-1 returnFareBtn" data-id="{@historyId}" type="button">
												<I class="fal fa-undo-alt fontSize22"></I>
											</BUTTON>
										</xsl:if>
									</DIV>
								</DIV>
							</xsl:for-each>
						</DIV>
					</DIV>
				</DIV>
			</MAIN>
			<xsl:call-template name="bootstrapToast"/>
			<xsl:call-template name="dashScriptTags"/>
			<SCRIPT src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"/>
			<SCRIPT src="/SCRIPT/mePointsRecords.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>