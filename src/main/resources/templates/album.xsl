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
			<LINK href="/STYLE/album.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container col-12 col-md-8 col-lg-6 px-3 py-6 py-md-7">
				<A class="text-primary" href="/profile/">
					<I class="fad fa-arrow-circle-left fontSize35"></I>
				</A>
				<ARTICLE class="card p-3 mt-3">
					<DIV class="mb-3">
						<DIV class="h4 titleBorder">主要照片</DIV>
						<DIV>
							<DIV class="text-xs text-dark">為開放照片，作為 meKING/meQUEEN 認識您的第一印象</DIV>
							<DIV class="text-danger text-bold text-sm">真人照片邀約成功率提高90%！</DIV>
						</DIV>
					</DIV>
					<DIV class="position-relative">
						<LABEL class="uploadProfileImage">
							<INPUT accept="image/*" class="sr-only" data-type="profileImage" name="image" type="file"/>
							<I class="fas fa-camera fontSize35 text-primary"></I>
						</LABEL>
						<IMG alt="profile_image" class="border-radius-md" src="{profileImage}" id="profileImageImg" width="130"/>
					</DIV>
				</ARTICLE>
				<ARTICLE class="card p-3 mt-3">
					<DIV class="mb-3">
						<DIV class="h4 titleBorder">其他照片</DIV>
						<DIV>
							<DIV class="text-xs text-dark">為私密照片專區，查看需要經過您的同意</DIV>
							<DIV class="text-danger text-bold text-sm">上傳更多您的照片，約會成功率提高70%！</DIV>
						</DIV>
					</DIV>
					<DIV class="d-flex flex-wrap pictures">
						<xsl:for-each select="picture">
							<DIV class="m-1 pictureWrap">
								<BUTTON class="btn btn-link btnDel" data-id="{@picIdentifier}" id="delete{position()}" type="button">
									<I class="fas fa-trash-alt text-primary fontSize25"></I>
								</BUTTON>
								<IMG alt="照片{position()}" class="border-radius-md" src="{.}" width="100"/>
							</DIV>
						</xsl:for-each>
						<DIV>
							<LABEL class="m-0">
								<INPUT accept="image/*" class="sr-only" data-type="picture" name="image" type="file"/>
								<DIV class="card card-plain border border-dark m-1 uploadPictureWrap d-flex align-items-center justify-content-center">
									<I class="fal fa-plus text-dark fontSize35"></I>
								</DIV>
							</LABEL>
						</DIV>
					</DIV>
				</ARTICLE>
				<DIV class="modal fade" id="confirmModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-body">
								<DIV class="d-flex">
									<BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
										<I class="fal fa-times"></I>
									</BUTTON>
								</DIV>
								<DIV class="my-4 text-center">
									<I class="fad fa-exclamation-circle text-warning mb-1 fontSize50"></I>
									<P class="text-warning text-bold">是否確定刪除照片?</P>
								</DIV>
								<DIV class="text-center">
									<BUTTON class="btn btn-outline-primary mx-2 px-3 py-2 btnDelConfirm" type="button">確認</BUTTON>
									<BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="modal fade" id="cropModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title" id="modalLabel">裁切照片</H5>
								<BUTTON aria-label="Close" class="btn-close text-dark" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<DIV class="imgContainer">
									<IMG alt="cropper" src="https://via.placeholder.com/200" id="image"/>
								</DIV>
								<DIV class="progress-wrapper">
									<DIV class="progress-info">
										<DIV class="progress-percentage">
											<span class="text-sm font-weight-bold">0%</span>
										</DIV>
									</DIV>
									<DIV class="progress">
										<DIV class="progress-bar bg-primary" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></DIV>
									</DIV>
								</DIV>
							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn btn-outline-primary px-3 py-2" id="crop" type="button">完成</BUTTON>
								<BUTTON class="btn btn-outline-dark px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT crossorigin="anonymous" src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.js" integrity="sha512-E4KfIuQAc9ZX6zW1IUJROqxrBqJXPuEcDKP6XesMdu2OV4LW7pj8+gkkyx2y646xEV7yxocPbaTtk2LQIJewXw==" referrerpolicy="no-referrer"/>
			<SCRIPT src="/SCRIPT/album.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>