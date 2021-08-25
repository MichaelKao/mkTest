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
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-7">
				<DIV class="col-12 col-md-7 mx-auto shadow m-2 shadow tabs">
					<UL class="nav nav-tabs flex-row">
						<LI class="nav-item col-6 text-center">
							<A class="nav-link cursor-pointer active" data-bs-toggle="tab" href="#matched">
								<I class="fad fa-users"></I>
								<SPAN class="ms-1">好友</SPAN>
								<xsl:if test="@matchedNotSeenCount">
									<SPAN class="text-xs border-radius-md px-1 ms-1 notSeen">
										<xsl:value-of select="@matchedNotSeenCount"/>
									</SPAN>
								</xsl:if>
							</A>
						</LI>
						<LI class="nav-item col-6 text-center">
							<A class="nav-link cursor-pointer" data-bs-toggle="tab" href="#non-matched">
								<I class="fad fa-users-slash"></I>
								<SPAN class="ms-1">非好友</SPAN>
								<xsl:if test="@notMatchedNotSeenCount">
									<SPAN class="text-xs border-radius-md px-1 ms-1 notSeen">
										<xsl:value-of select="@notMatchedNotSeenCount"/>
									</SPAN>
								</xsl:if>
							</A>
						</LI>
					</UL>
				</DIV>
				<DIV class="tab-content">
					<DIV class="row justify-content-center mx-0 tab-pane active" id="matched">
						<xsl:for-each select="conversation">
							<xsl:if test="@matched = 'true'">
								<DIV class="card col-12 col-md-7 my-1 mx-auto conversationWrap position-relative shadow">
									<A class="inboxLink" href="/chatroom/{@identifier}/"></A>
									<DIV class="d-flex justify-content-between align-items-center py-2">
										<DIV>
											<IMG src="{@profileImage}" alt="大頭貼" class="rounded-circle" width="60px"/>
										</DIV>
										<DIV class="me-auto" style="overflow: hidden;">
											<DIV class="d-flex flex-column align-items-start ms-3">
												<A class=" font-weight-bold text-dark text-sm mb-1">
													<xsl:value-of select="@nickname"/>
												</A>
												<P class="text-sm mb-0 content">
													<xsl:value-of select="@content"/>
												</P>
											</DIV>
										</DIV>
										<DIV class="col-2 d-flex">
											<DIV class="ms-auto d-flex flex-column">
												<SPAN class="text-xs mb-1">
													<xsl:value-of select="@occurredTime"/>
												</SPAN>
												<xsl:if test="@notSeenCount">
													<DIV class="d-flex justify-content-center">
														<SPAN class="text-xs text-light bg-primary border-radius-md px-1">
															<xsl:value-of select="@notSeenCount"/>
														</SPAN>
													</DIV>
												</xsl:if>
											</DIV>
										</DIV>
									</DIV>
								</DIV>
							</xsl:if>
						</xsl:for-each>
					</DIV>
					<DIV class="row justify-content-center mx-0 tab-pane" id="non-matched">
						<xsl:for-each select="conversation">
							<xsl:if test="@matched = 'false'">
								<DIV class="card col-12 col-md-7 my-1 mx-auto conversationWrap position-relative shadow">
									<A class="inboxLink" href="/chatroom/{@identifier}/"></A>
									<DIV class="d-flex justify-content-between align-items-center py-2">
										<DIV>
											<IMG src="{@profileImage}" alt="大頭貼" class="rounded-circle" width="60px"/>
										</DIV>
										<DIV class="me-auto" style="overflow: hidden;">
											<DIV class="d-flex flex-column align-items-start ms-3">
												<A class=" font-weight-bold text-dark text-sm mb-1">
													<xsl:value-of select="@nickname"/>
												</A>
												<P class="text-sm mb-0 content">
													<xsl:value-of select="@content"/>
												</P>
											</DIV>
										</DIV>
										<DIV class="col-2 d-flex">
											<DIV class="ms-auto d-flex flex-column">
												<SPAN class="text-xs mb-1">
													<xsl:value-of select="@occurredTime"/>
												</SPAN>
												<xsl:if test="@notSeenCount">
													<DIV class="d-flex justify-content-center">
														<SPAN class="text-xs text-light bg-primary border-radius-md px-1">
															<xsl:value-of select="@notSeenCount"/>
														</SPAN>
													</DIV>
												</xsl:if>
											</DIV>
										</DIV>
									</DIV>
								</DIV>
							</xsl:if>
						</xsl:for-each>
					</DIV>
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