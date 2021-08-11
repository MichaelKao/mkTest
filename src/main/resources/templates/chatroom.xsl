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
			<LINK href="/STYLE/chatroom.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="bootstrapToast"/>
			<INPUT type="hidden" name="selfIdentifier" value="{@selfIdentifier}"/>
			<INPUT type="hidden" name="friendIdentifier" value="{@friendIdentifier}"/>
			<INPUT type="hidden" name="gender" value="{@gender}"/>
			<DIV>
				<DIV id="chatroom">
					<SECTION class="fixed-top col-12 col-md-10 mx-auto shadow">
						<DIV class="chatroomHeader d-flex align-items-center ps-2 shadow">
							<DIV class="me-3">
								<BUTTON class="btn btn-link text-primary h2 m-0 p-0 locationBack">
									<I class="fal fa-arrow-left"></I>
								</BUTTON>
							</DIV>
							<DIV>
								<A href="/profile/{@friendIdentifier}/">
									<IMG alt="profileImage" class="rounded-circle" src="{@friendProfileImage}" width="45"/>
								</A>
								<SPAN class="text-lg ms-2">
									<xsl:value-of select="@friendNickname"/>
								</SPAN>
							</DIV>
						</DIV>
					</SECTION>
					<MAIN class="chatroom">
						<DIV class="chatroomWrapper col-12 col-md-10 mx-auto">
							<SECTION class="chatHistory d-flex flex-column justify-content-end">
								<DIV class="mb-5" id="messagesArea"></DIV>
							</SECTION>
						</DIV>
					</MAIN>
					<SECTION id="chatInputWrapper" class="fixed-bottom col-12 col-md-10 mx-auto shadow">
						<DIV class="d-flex align-items-center justify-content-center p-2 position-relative">
							<xsl:if test="/document/@gender = 'false' and @decideButton">
								<DIV class="d-flex align-items-center justify-content-center femaleResponseBtn">
									<BUTTON class="btn btn-sm btn-outline-primary px-3 py-2 m-0 me-1 border-radius-xl accept" type="button">接受</BUTTON>
									<BUTTON class="btn btn-sm btn-outline-dark px-3 py-2 m-0 me-1 border-radius-xl refuse" type="button">拒絕</BUTTON>
								</DIV>
							</xsl:if>
							<xsl:if test="/document/@gender = 'true'">
								<DIV class="d-flex align-items-center justify-content-center femaleResponseBtn">
									<BUTTON class="btn btn-sm btn-info px-3 py-2 m-0 border-radius-xl" type="button">
										<xsl:choose>
											<xsl:when test="/document/@matched">
												<xsl:attribute name="id">openLine</xsl:attribute>
											</xsl:when>
											<xsl:otherwise>
												<xsl:attribute name="id">giveMeLine</xsl:attribute>
											</xsl:otherwise>
										</xsl:choose>
										<SPAN>要求通訊軟體</SPAN>
										<I class="far fa-user-plus ms-1"></I>
									</BUTTON>
								</DIV>
							</xsl:if>
							<TEXTAREA class="form-control bg-light" id="chatInput" maxlength="240" placeholder="說點什麼吧...!" rows="1"></TEXTAREA>
							<BUTTON class="btn btn-icon-only btn-link m-0 p-0 sendMsgBtn" type="button">
								<I class="fal fa-paper-plane" style="font-size: 30px;"></I>
							</BUTTON>
						</DIV>
					</SECTION>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/chatroom.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocketChat.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>