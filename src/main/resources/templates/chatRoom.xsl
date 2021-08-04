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
			<xsl:call-template name="bootstrapToast"/>
			<div class="container text-center">
				<div id="chatBox" style="">
					<main class="chatroom-main" style="/* height: 792.5px; */height: calc(100% - 45px);overflow-y: auto;overflow-x: hidden;/* top: 62px; */position: absolute;left: 0;right: 0;margin: 0 auto;padding-top: 20p;-webkit-overflow-scrolling: touch;/* border: 1px solid #ccc; *//* border-top: none; *//* border-bottom: none; *//* background: blanchedalmond; */">
						<div class="chatroom-wrapper col-12 col-md-10 border-light mx-auto" style="height: 100%;/* background: bisque; */border: 4px solid #f3f3f3;">
							<section id="MsgRecord" class="chat-history d-flex flex-column justify-content-between " style="min-height: 100%;">
								<div>
									<div class="d-flex align-items-center ps-2" style="height: 60px;background: #f3f3f3;">
										<div class="me-4">
											<a class="text-primary h2" href="/">
												<i class="fad fa-chevron-double-left" aria-hidden="true"></i>
											</a>
										</div>
										<div class="">
											<a href="/profile/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/">
												<img alt="profileImage" class="rounded-circle" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" width="45"/>
											</a>
											<span class="text-bold text-lg ms-2">Peter
											</span>
										</div>
									</div>
									<div class="row justify-content-center">
										<div class="col-10 col-md-5 mx-auto">
											<div role="alert" class="alert alert-earning">
												<i class="fas fa-exclamation-circle text-warning" aria-hidden="true"></i>
												<p class="text-xs">禁止非法行為，約會時請注意自身安全，聊天時請注意禮貌尊重對方。</p>
												<p class="text-xs">提醒:為了預防詐騙，請勿聽從指示到別的付費網站或者匯款，建議先在公開場合見面在慢慢認識。</p>
											</div>
										</div>
									</div>
								</div>
								<div>
									<div>
										<div class="msg received-msg d-flex justify-content-start mb-4">
											<figure id="received-PeerPic" class="img-wrapper">
												<a href="/profile/bbcb1fe6-1d5b-48f8-b804-f0486353f8bc/">
													<img alt="profileImage" class="rounded-circle" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/5245e8f1-2fac-4f32-93fe-48d3db63165d" width="35"/>
												</a>
											</figure>
											<div class="msg-wrapper d-flex flex-column ms-2">
												<div class="msg-body ml-2 d-flex flex-column">
													<div class="bg-secondary text-light border-radius-xl text mb-0 text-black px-3 py-1 chat-history-peer">Hi</div>
												</div>
												<span class="text-muted text-xs">08/02 10:41</span>
											</div>
										</div>
									</div>
									<div>
										<div class="msg sent-msg d-flex justify-content-end mb-4">
											<div class="msg-wrapper d-flex flex-column">
												<div class="msg-body mr-2 d-flex flex-column mb-1">
													<div class="text mb-0 text-black text-right chat-history-my bg-primary text-light border-radius-xl px-3 py-1">Hello</div>
												</div>
												<span class="text-muted text-xs">
													<small>08/03 15:35</small>
												</span>
											</div>
										</div>
									</div>
								</div>
							</section>
						</div>
					</main>
					<section id="chat-input-wrapper" class="fixed-bottom col-12 col-md-10 mx-auto" style="background-color: rgb(238, 238, 238);">
						<div class="d-flex py-1 px-2">
							<div>
								<label for="file-upload" class="">
									<i aria-hidden="true" class="fas fa-image text-primary" style="font-size: 34px;"></i>
								</label>
								<input class="d-none" type="file" id="file-upload" name="file-upload" accept="image/gif, image/jpeg, image/png"/>
							</div>
							<textarea id="chat-input" rows="1" placeholder="說點什麼吧...!" maxlength="240" class="form-control bg-light"></textarea>
							<button type="button" class="btn btn-icon-only btn-link">
								<i class="fad fa-paper-plane" style="font-size: 34px;" aria-hidden="true"></i>
							</button>
						</div>
					</section>
				</div>
			</div>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>