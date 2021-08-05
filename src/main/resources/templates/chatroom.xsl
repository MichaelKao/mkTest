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
			<h3 id="statusOutput" class="statusOutput"></h3>
			<DIV id="row"></DIV>
			<DIV class="text-center">
				<DIV id="chatroom">
					<MAIN class="chatroom">
						<DIV class="chatroomWrapper col-12 col-md-10 mx-auto shadow">
							<SECTION class="chatHistory d-flex flex-column justify-content-between">
								<DIV id="messagesArea" class="panel message-area" ></DIV>
								<!--								<DIV>
									<DIV class="chatroomHeader d-flex align-items-center ps-2 shadow">
										<DIV class="me-3">
											<BUTTON class="btn btn-link text-primary h2 m-0 p-0 locationBack">
												<I class="fal fa-arrow-left"></I>
											</BUTTON>
										</DIV>
										<DIV>
											<A href="/profile/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/">
												<IMG alt="profileImage" class="rounded-circle" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" width="45"/>
											</A>
											<SPAN class="text-lg ms-2">Peter</SPAN>
										</DIV>
									</DIV>
								</DIV>
								<DIV>
									<DIV class="msg receivedMsg d-flex justify-content-start mb-4 ms-1">
										<A href="/profile/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/">
											<IMG alt="profileImage" class="rounded-circle" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" width="35"/>
										</A>
										<DIV class="msg-wrapper d-flex flex-column ms-2">
											<DIV class="msg-body d-flex flex-column">
												<DIV class="bg-secondary text-light border-radius-xl text mb-0 text-black px-3 py-1 chat-history-peer">Hi</DIV>
											</DIV>
											<SPAN class="text-muted text-xs">08/02 10:41</SPAN>
										</DIV>
									</DIV>
									<DIV class="msg sentMsg d-flex justify-content-end mb-4 me-1">
										<DIV class="msg-wrapper d-flex flex-column">
											<DIV class="msg-body d-flex flex-column mb-1">
												<DIV class="text mb-0 text-black text-right chat-history-my bg-primary text-light border-radius-xl px-3 py-1">Hello</DIV>
											</DIV>
											<SPAN class="text-muted text-xs">
												<SPAN class="text-muted text-xs">08/03 15:35</SPAN>
											</SPAN>
										</DIV>
									</DIV>
								</DIV>-->
							</SECTION>
						</DIV>
					</MAIN>
					<SECTION id="chatInputWrapper" class="fixed-bottom col-12 col-md-10 mx-auto shadow">
						<DIV class="d-flex align-items-center justify-content-center p-2">
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
				<SCRIPT src="/SCRIPT/websocket.js"/>
				<SCRIPT src="/SCRIPT/websocketChat.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>