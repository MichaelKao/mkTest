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
			<DIV class="container py-6 py-md-7">
				<DIV class="col-11 col-md-8 col-lg-6 mx-auto">
					<H4 class="text-center text-primary">服務條款</H4>
					<DIV>
						<DIV class="mb-2">1. 使用本網站，即表示您完全同意接受這些條款和條件。如果您不同意這些條款和條件的任何部分，則不得使用本網站。</DIV>
						<DIV class="mb-2">2. 您必須年滿18歲才能使用此網站，註冊表示您保證年滿18歲。</DIV>
						<DIV class="mb-2">3. https://youngme.vip/網站上的所有內容和素材屬於我們完全擁有，不可以複製轉發到任何其他地方作為商業用途，也不可以隨意搜集轉賣會員資料。</DIV>
						<DIV class="mb-2">4. 用戶的帳號和密碼必須自行保密，並且我們可以隨時禁用您的帳號和密碼，不另行通知或解釋。</DIV>
						<DIV class="mb-2">5. 我們保留編輯或刪除提交到本網站，或存儲在服務器上或託管或發佈在本網站上的任何資料的權利。</DIV>
						<DIV class="mb-2">6. 任何目的您提交給本網站的材料(包括但不限於文本，圖像，音頻材料，視頻材料和視聽材料)。您授予一個全球性的，不可撤銷的，非排他的，免版稅的許可，可以在任何現有或將來的媒體中使用，複製，改編，發布，翻譯用戶內容。您還授予我們再許可這些權利的權利，以及提起侵犯這些權利的訴訟的權利。</DIV>
						<DIV class="mb-2">7. 用戶內容不得為非法，不得侵犯任何第三方的合法權利。您不得向網站提交任何曾經或曾經遭受任何威脅或實際法律訴訟或其他類似投訴的用戶內容。</DIV>
					</DIV>
					<DIV class="text-center">
						<A href="/signUp.asp" class="btn primary-gradient text-white text-bold m-0 w-80 py-2">開始註冊</A>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>