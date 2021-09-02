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
			<DIV class="container pt-7 pt-md-8">
				<DIV class="text-center mx-sm-5 mx-lg-12 mb-3">
					<H4>
						<I class="fad fa-badge-dollar fontSize22 text-dark me-2"></I>
						<SPAN class="text-primary">
							<xsl:value-of select="@title"/>
						</SPAN>
					</H4>
				</DIV>
				<DIV class="text-center text-dark text-sm">
					<DIV>可以用於站內發送ME點</DIV>
					<DIV>讓您更快找到養蜜</DIV>
				</DIV>
				<DIV class="text-center text-primary text-sm">
					<DIV>站內發送如遇到爽約詐騙</DIV>
					<DIV>48小時內提出申請</DIV>
					<DIV>經檢舉查證可退回點數</DIV>
				</DIV>
				<DIV class="text-center text-primary text-bold border-radius-xl mx-auto col-11 col-md-6 p-1 my-3" style="border: 1px solid #D63384;">
					<SPAN>您剩餘的ME點：</SPAN>
					<SPAN>
						<xsl:value-of select="hearts"/>
					</SPAN>
				</DIV>
				<DIV class="row mx-auto justify-content-center">
					<xsl:for-each select="plan">
						<DIV class="col-11 col-md-6 card mb-3 mx-2">
							<xsl:if test="position() = 1">
								<xsl:attribute name="style">background: #E176AB;</xsl:attribute>
							</xsl:if>
							<xsl:if test="position() = 2">
								<xsl:attribute name="style">background: #D63384;</xsl:attribute>
							</xsl:if>
							<xsl:if test="position() = 3">
								<xsl:attribute name="style">background: #9F2360;</xsl:attribute>
							</xsl:if>
							<DIV class="card-body p-2">
								<DIV class="text-bold text-white text-center">
									<SPAN>ME點</SPAN>
									<SPAN class="mx-1">
										<xsl:value-of select="@points"/>
									</SPAN>
									<SPAN>｜ NT$</SPAN>
									<SPAN>
										<xsl:value-of select="@amount"/>
									</SPAN>
									<DIV class="text-center mt-2">
										<A class="btn btn-round btn-light px-4 py-2 m-0" href="/recharge/{position()}.asp">購買</A>
									</DIV>
								</DIV>
							</DIV>
						</DIV>
					</xsl:for-each>
				</DIV>
				<DIV class="text-center text-primary text-sm border-radius-xl mx-auto col-11 col-md-6 p-1" style="border: 1px solid #D63384;">
					<SPAN class="text-primary">本筆款項將在信用卡帳單僅會顯示為 「線上儲值」</SPAN>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>