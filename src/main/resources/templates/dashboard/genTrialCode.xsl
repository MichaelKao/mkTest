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
			<LINK href="/STYLE/genTrialCode.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-7 py-md-8">
				<SECTION class="col-12 col-md-7 col-xl-4 mx-auto">
					<DIV class="text-center text-primary text-bold">產生體驗碼</DIV>
					<FORM action="/dashboard/addTrialCode.json" class="row justify-content-center" method="POST">
						<DIV class="col-12 row justify-content-center mt-2">
							<LABEL for="keyOpinionLeader" class="col-1 d-flex align-items-center justify-content-center m-0">
								<I class="fab fa-youtube text-lg text-primary" aria-hidden="true"></I>
							</LABEL>
							<DIV class="col-10">
								<INPUT class="form-control" id="keyOpinionLeader" name="keyOpinionLeader" placeholder="網紅名稱" required="" type="text"/>
							</DIV>
						</DIV>
						<DIV class="col-12 row justify-content-center mt-2">
							<LABEL for="code" class="col-1 d-flex align-items-center justify-content-center m-0">
								<I class="fas fa-keyboard text-lg text-primary" aria-hidden="true"></I>
							</LABEL>
							<DIV class="col-10">
								<INPUT class="form-control" id="code" name="code" placeholder="體驗碼" required="" type="text"/>
							</DIV>
						</DIV>
						<DIV class="col-11 mt-2">
							<BUTTON class="btn btn-primary btn-round w-100 py-2 addDone" type="button">送出</BUTTON>
						</DIV>
					</FORM>
				</SECTION>
				<SECTION class="mt-3 col-12 col-md-7 col-xl-4 mx-auto">
					<DIV class="text-center text-primary text-bold mb-3">體驗碼名單</DIV>
					<DIV class="">
						<DIV class="row">
							<DIV class="col-3 text-center text-sm">網紅</DIV>
							<DIV class="col-9 text-center text-sm">體驗碼</DIV>
						</DIV>
						<HR class="my-2"/>
						<xsl:for-each select="trial">
							<DIV class="row mb-2">
								<DIV class="col-3 text-center text-sm d-flex align-items-center justify-content-center">
									<xsl:value-of select="@keyOpinionLeader"/>
								</DIV>
								<DIV class="col-9 text-center">
									<INPUT name="trialCodeID" type="hidden" value="{@trialCodeID}"/>
									<INPUT class="form-control editInput me-2" disabled="" name="editedCode" type="text" value="{@code}"/>
									<BUTTON class="btn btn-dark btn-round px-2 py-1 m-0 toggleEdit" type="button">編輯</BUTTON>
									<BUTTON class="btn btn-primary btn-round px-2 py-1 m-0 d-none editDone" type="button">完成</BUTTON>
								</DIV>
							</DIV>
						</xsl:for-each>
					</DIV>
				</SECTION>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/genTrialCode.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>