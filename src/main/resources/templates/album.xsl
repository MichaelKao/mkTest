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
			<DIV class="container content py-7">
				<A class="text-primary h2" href="/profile/">
					<I class="fad fa-chevron-double-left"></I>
				</A>
				<HR class="horizontal dark"/>
				<ARTICLE class="py-4">
					<DIV class="h4 text-primary mb-3 d-flex align-items-baseline">
						<SPAN>大頭貼</SPAN>
						<LABEL>
							<INPUT accept="image/*" class="sr-only" data-type="profileImage" name="image" type="file"/>
							<I class="far fa-pen h4"></I>
						</LABEL>
					</DIV>
					<DIV>
						<IMG alt="profile_image" class="border-radius-md" src="{profileImage}" id="profileImageImg" width="100"/>
					</DIV>
				</ARTICLE>
				<ARTICLE class="py-4">
					<DIV class="h4 text-primary mb-3 d-flex align-items-baseline">
						<SPAN>其他照片</SPAN>
						<LABEL>
							<INPUT accept="image/*" class="sr-only" data-type="picture" name="image" type="file"/>
							<I class="far fa-plus-circle h4"></I>
						</LABEL>
					</DIV>
					<DIV class="d-flex flex-wrap pictures">
						<xsl:for-each select="picture">
							<DIV class="m-1 pictureWrap">
								<BUTTON class="btnDel" data-id="{@picIdentifier}" id="delete{position()}" type="button">
									<I class="fas fa-trash-alt text-primary"></I>
								</BUTTON>
								<IMG alt="照片{position()}" class="border-radius-md" src="{.}" width="100"/>
							</DIV>
						</xsl:for-each>
					</DIV>
				</ARTICLE>
				<DIV class="modal fade" id="confirmModal">
					<DIV class="modal-dialog modal-dialog-centered">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">提醒</H5>
								<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<P>是否確認刪除照片?</P>
							</DIV>
							<DIV class="modal-footer">
								<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
								<BUTTON class="btn btn-primary btnDelConfirm" type="button">確定</BUTTON>
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
								<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
								<BUTTON class="btn btn-primary" id="crop" type="button">完成</BUTTON>
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