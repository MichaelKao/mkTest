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
			<LINK crossorigin="anonymous" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.css" integrity="sha512-Aix44jXZerxlqPbbSLJ03lEsUch9H/CmnNfWxShD6vJBbboR+rPdDXmKN+/QjISWT80D4wMjtM4Kx7+xkLVywQ==" referrerpolicy="no-referrer" rel="stylesheet"/>
			<LINK href="https://cdn.jsdelivr.net/jquery.slick/1.6.0/slick.css" rel="stylesheet"/>
			<LINK href="https://mreq.github.io/slick-lightbox/dist/slick-lightbox.css" rel="stylesheet"/>
			<LINK href="https://mreq.github.io/slick-lightbox/gh-pages/bower_components/slick-carousel/slick/slick-theme.css" rel="stylesheet"/>
			<LINK href="/STYLE/forum.css" rel="stylesheet"/>
			<LINK href="/STYLE/loading.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<BUTTON class="btn btn-link m-0 p-0 scrollTopFloatBtn d-flex align-items-center justify-content-center position-fixed bg-primary fontSize25 text-white opacity-9 shadow" type="button">
				<I class="fas fa-chevron-up"></I>
			</BUTTON>
			<DIV class="modal fade" id="addPostModal">
				<DIV class="modal-dialog modal-dialog-centered">
					<DIV class="modal-content">
						<DIV class="modal-body p-2">
							<DIV class="d-flex">
								<BUTTON class="btn btn-link ms-auto fontSize22 me-1 m-0 p-0" data-bs-dismiss="modal" type="button">
									<I class="fal fa-times"></I>
								</BUTTON>
							</DIV>
							<DIV class="my-2 text-center">
								<DIV class="addPost">
									<DIV class="py-1 px-2 d-flex flex-row justify-content-start align-items-center">
										<img alt="self-profileImage" class="avatar shadow me-2" src="{self/@profileImage}"/>
										<DIV class="username">
											<DIV class="name">
												<SPAN class="selfNickname">
													<xsl:value-of select="self/@nickname"/>
												</SPAN>
												<I class="fas fa-shield-check ms-1">
													<xsl:if test="self/@relief = 'true'">
														<xsl:attribute name="class">fas fa-shield-check text-success ms-1</xsl:attribute>
													</xsl:if>
												</I>
											</DIV>
										</DIV>
									</DIV>
									<DIV class="primary-gradient p-2 border-radius-xl">
										<DIV class="d-flex flex-wrap" id="imgBox"></DIV>
										<INPUT class="form-control addPostTextarea mb-2 py-2 text-lg text-white" name="title" placeholder="輸入標題"/>
										<textarea class="form-control text-white addPostTextarea" name="markdown" rows="8" placeholder="輸入內容...."></textarea>
										<DIV class="d-flex my-2">
											<LABEL class="m-0">
												<INPUT accept="image/*" class="sr-only" id="newPost" multiple="" name="illustrations" type="file"/>
												<SPAN class="addImages text-white cursor-pointer position-relative ms-1">
													<I class="fas fa-images fontSize22" aria-hidden="true"></I>
													<I class="fas fa-plus position-absolute text-xs addIcon" aria-hidden="true"></I>
												</SPAN>
											</LABEL>
										</DIV>
										<DIV class="d-flex flex-wrap mt-1 justify-content-start">
											<xsl:for-each select="forumThreadTag">
												<DIV class="form-check p-0">
													<INPUT class="form-check-input" id="hashTag{@id}" name="hashTag" type="radio" value="{@id}"/>
													<LABEL class="custom-control-label tag" for="hashTag{@id}">
														<SPAN>#</SPAN>
														<SPAN>
															<xsl:value-of select="."/>
														</SPAN>
													</LABEL>
												</DIV>
											</xsl:for-each>
										</DIV>
									</DIV>
								</DIV>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-outline-primary btn-round mx-1 px-3 py-2 mb-1" id="postBtn" type="button">發佈</BUTTON>
								<BUTTON class="btn btn-outline-dark btn-round mx-1 px-3 py-2 m-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<DIV class="modal fade" id="editPostModal">
				<DIV class="modal-dialog modal-dialog-centered">
					<DIV class="modal-content">
						<DIV class="modal-body p-2">
							<DIV class="d-flex">
								<BUTTON class="btn btn-link ms-auto fontSize22 me-1 m-0 p-0" data-bs-dismiss="modal" type="button">
									<I class="fal fa-times"></I>
								</BUTTON>
							</DIV>
							<DIV class="my-2 text-center">
								<DIV class="text-bold text-dark mb-2">
									<I class="fad fa-edit fontSize22 me-2"></I>
									<SPAN>編輯貼文</SPAN>
								</DIV>
								<DIV class="primary-gradient p-2 border-radius-xl">
									<DIV class="d-flex flex-wrap" id="imgOriginalBox"></DIV>
									<DIV class="d-flex flex-wrap" id="imgEditBox"></DIV>
									<INPUT class="form-control addPostTextarea mb-2 py-2 text-lg text-white" name="title" placeholder="輸入標題"/>
									<textarea class="form-control text-white addPostTextarea" name="markdown" rows="8" placeholder="輸入內容...."></textarea>
									<DIV class="d-flex my-2">
										<LABEL class="m-0">
											<INPUT accept="image/*" class="sr-only" id="editPost" multiple="" name="illustrations" type="file"/>
											<SPAN class="addImages text-white cursor-pointer position-relative ms-1">
												<I class="fas fa-images fontSize22" aria-hidden="true"></I>
												<I class="fas fa-plus position-absolute text-xs addIcon" aria-hidden="true"></I>
											</SPAN>
										</LABEL>
									</DIV>
									<DIV class="d-flex flex-wrap mt-1 justify-content-start">
										<xsl:for-each select="forumThreadTag">
											<DIV class="form-check p-0">
												<INPUT class="form-check-input" id="editHashTag{@id}" name="editHashTag" type="radio" value="{@id}"/>
												<LABEL class="custom-control-label tag" for="editHashTag{@id}">
													<SPAN>#</SPAN>
													<SPAN>
														<xsl:value-of select="."/>
													</SPAN>
												</LABEL>
											</DIV>
										</xsl:for-each>
									</DIV>
								</DIV>
							</DIV>
							<DIV class="text-center">
								<BUTTON class="btn btn-outline-primary btn-round mx-1 px-3 py-2 m-1" id="editBtn" type="button">完成編輯</BUTTON>
								<BUTTON class="btn btn-outline-dark btn-round mx-1 px-3 py-2 m-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<DIV class="wrap px-3 py-6 py-md-7 pb-3">
				<DIV class="mx-auto col-md-10 col-lg-8 col-xl-6">
					<BUTTON class="primary-gradient w-30 ms-auto border-radius-xl py-2 m-0 shadow btn btn-link d-flex align-items-center justify-content-center bg-primary text-white" data-bs-target="#addPostModal" data-bs-toggle="modal" type="button">
						<SPAN class="text-white cursor-pointer position-relative">
							<I class="fas fa-newspaper fontSize22"></I>
							<I class="fas fa-plus position-absolute text-xs top-1" style="right: -8px;"></I>
						</SPAN>
					</BUTTON>
				</DIV>
				<DIV class="d-flex justify-content-evenly my-3 col-md-10 col-lg-8 col-xl-6 mx-auto">
					<SPAN class="sort active" id="popular">
						<I class="fad fa-fire-alt me-1"></I>
						<SPAN>熱門</SPAN>
					</SPAN>
					<SPAN class="sort" id="latest">
						<I class="fad fa-newspaper me-1"></I>
						<SPAN>最新</SPAN>
					</SPAN>
					<SPAN class="sort" id="mine">
						<I class="fad fa-user me-1"></I>
						<SPAN>個人</SPAN>
					</SPAN>
					<INPUT name="nextPage" type="hidden" value="1"/>
					<INPUT name="sort" type="hidden" value="popular"/>
				</DIV>
				<DIV class="posts col-md-10 col-lg-8 col-xl-6 mx-auto"></DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<DIV class="loadingWrap" style="display: none;">
				<DIV class="loading">
					<DIV class="round"></DIV>
					<DIV class="round ms-1"></DIV>
					<DIV class="round ms-1"></DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT crossorigin="anonymous" src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.js" integrity="sha512-E4KfIuQAc9ZX6zW1IUJROqxrBqJXPuEcDKP6XesMdu2OV4LW7pj8+gkkyx2y646xEV7yxocPbaTtk2LQIJewXw==" referrerpolicy="no-referrer"/>
			<SCRIPT src="https://cdn.jsdelivr.net/jquery.slick/1.6.0/slick.min.js"/>
			<SCRIPT src="https://mreq.github.io/slick-lightbox/dist/slick-lightbox.js"/>
			<SCRIPT src="/SCRIPT/forum.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>