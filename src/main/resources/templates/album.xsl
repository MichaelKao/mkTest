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
				<A class="text-primary text-lg" href="/profile/">
					<I class="fad fa-chevron-double-left"></I>
				</A>
				<HR class="horizontal dark"/>
				<ARTICLE class="py-4 text-center">
					<H2 class="h4 text-primary mb-4">大頭貼</H2>
					<DIV class="d-flex justify-content-center align-items-center">
						<DIV>
							<DIV id="fileDiv1">
								<xsl:if test="not(profileImage)">
									<xsl:attribute name="class" value="d-none">d-none</xsl:attribute>
								</xsl:if>
								<DIV class="avatarWrap">
									<BUTTON class="btnDel" id="delete1" type="button">
										<I class="fas fa-trash-alt text-primary"></I>
									</BUTTON>
									<IMG alt="profile_picture" class="border-radius-md" id="avatar1" src="https://via.placeholder.com/200" width="100">
										<xsl:choose>
											<xsl:when test="profileImage">
												<xsl:attribute name="src">
													<xsl:value-of select="profileImage"/>
												</xsl:attribute>
											</xsl:when>
											<xsl:otherwise>
												<xsl:attribute name="src">https://via.placeholder.com/200</xsl:attribute>
											</xsl:otherwise>
										</xsl:choose>
									</IMG>
								</DIV>
							</DIV>
							<DIV id="fileDiv1WithoutPic">
								<xsl:if test="profileImage">
									<xsl:attribute name="class" value="d-none">d-none</xsl:attribute>
								</xsl:if>
								<DIV class="avatarWrap">
									<LABEL>
										<INPUT accept="image/*" class="sr-only" id="input1" name="image" type="file"/>
										<IMG alt="upload_icon" src="/upload_icon.svg" width="100"/>
									</LABEL>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</ARTICLE>
				<ARTICLE class="py-4 text-center">
					<H2 class="h4 text-primary mb-4">其他照片</H2>
					<DIV class="d-flex justify-content-center my-5">
						<DIV class="me-4">
							<DIV class="d-none" id="fileDiv2">
								<DIV class="avatarWrap">
									<BUTTON class="btnDel" id="delete2" type="button">
										<I class="fas fa-trash-alt text-primary"></I>
									</BUTTON>
									<IMG alt="profile_picture" class="border-radius-md" src="https://via.placeholder.com/200" id="avatar2" width="90"/>
								</DIV>
							</DIV>
							<DIV id="fileDiv2WithoutPic">
								<DIV class="avatarWrap">
									<LABEL>
										<INPUT accept="image/*" class="sr-only" id="input2" name="image" type="file"/>
										<IMG alt="upload_icon" src="/upload_icon.svg" width="90"/>
									</LABEL>
								</DIV>
							</DIV>
						</DIV>
						<DIV class="me-4">
							<DIV class="d-none" id="fileDiv3">
								<DIV class="avatarWrap">
									<BUTTON class="btnDel" type="button" id="delete3">
										<I class="fas fa-trash-alt text-primary"></I>
									</BUTTON>
									<IMG alt="profile_picture" class="border-radius-md" src="https://via.placeholder.com/200" id="avatar3" width="90"/>
								</DIV>
							</DIV>
							<DIV id="fileDiv3WithoutPic">
								<DIV class="avatarWrap">
									<LABEL>
										<INPUT accept="image/*" class="sr-only" id="input3" name="image" type="file"/>
										<IMG alt="upload_icon" src="/upload_icon.svg" width="90"/>
									</LABEL>
								</DIV>
							</DIV>
						</DIV>
						<DIV>
							<DIV class="d-none" id="fileDiv4">
								<DIV class="avatarWrap">
									<BUTTON class="btnDel" type="button" id="delete4">
										<I class="fas fa-trash-alt text-primary"></I>
									</BUTTON>
									<IMG alt="profile_picture" class="border-radius-md" src="https://via.placeholder.com/200" id="avatar4" width="90"/>
								</DIV>
							</DIV>
							<DIV id="fileDiv4WithoutPic">
								<DIV class="avatarWrap">
									<LABEL>
										<INPUT accept="image/*" class="sr-only" id="input4" name="image" type="file"/>
										<IMG alt="upload_icon" src="/upload_icon.svg" width="90"/>
									</LABEL>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
					<DIV class="d-flex justify-content-center">
						<DIV class="me-4">
							<DIV class="d-none" id="fileDiv5">
								<DIV class="avatarWrap">
									<BUTTON class="btnDel" type="button" id="delete5">
										<I class="fas fa-trash-alt text-primary"></I>
									</BUTTON>
									<IMG alt="profile_picture" class="border-radius-md" src="https://via.placeholder.com/200" id="avatar5" width="90"/>
								</DIV>
							</DIV>
							<DIV id="fileDiv5WithoutPic">
								<DIV class="avatarWrap">
									<LABEL>
										<INPUT accept="image/*" class="sr-only" id="input5" name="image" type="file"/>
										<IMG alt="upload_icon" src="/upload_icon.svg" width="90"/>
									</LABEL>
								</DIV>
							</DIV>
						</DIV>
						<DIV class="me-4">
							<DIV class="d-none" id="fileDiv6">
								<DIV class="avatarWrap">
									<BUTTON class="btnDel" id="delete6" type="button">
										<I class="fas fa-trash-alt text-primary"></I>
									</BUTTON>
									<IMG alt="profile_picture" class="border-radius-md" src="https://via.placeholder.com/200" id="avatar6" width="90"/>
								</DIV>
							</DIV>
							<DIV id="fileDiv6WithoutPic">
								<DIV class="avatarWrap">
									<LABEL>
										<INPUT accept="image/*" class="sr-only" id="input6" name="image" type="file"/>
										<IMG alt="upload_icon" src="/upload_icon.svg" width="90"/>
									</LABEL>
								</DIV>
							</DIV>
						</DIV>
						<DIV>
							<DIV class="d-none" id="fileDiv7">
								<DIV class="avatarWrap">
									<BUTTON class="btnDel" type="button" id="delete7">
										<I class="fas fa-trash-alt text-primary"></I>
									</BUTTON>
									<IMG alt="profile_picture" class="border-radius-md" src="https://via.placeholder.com/200" id="avatar7" width="90"/>
								</DIV>
							</DIV>
							<DIV id="fileDiv7WithoutPic">
								<DIV class="avatarWrap">
									<LABEL>
										<INPUT accept="image/*" class="sr-only" id="input7" name="image" type="file"/>
										<IMG alt="upload_icon" src="/upload_icon.svg" width="90"/>
									</LABEL>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</ARTICLE>
				<DIV class="modal" id="confirmModal" role="dialog" tabindex="-1">
					<DIV class="modal-dialog" role="document">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title">提醒</H5>
								<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
							</DIV>
							<DIV class="modal-body">
								<P>是否確認刪除照片?</P>
							</DIV>
							<DIV class="modal-footer">
								<INPUT name="delete" type="hidden" value=""/>
								<BUTTON class="btn btn-secondary" data-bs-dismiss="modal" type="button">取消</BUTTON>
								<BUTTON class="btn btn-primary btnDelConfirm" type="button">確定</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV aria-labelledby="modalLabel" aria-hidden="true" class="modal fade" id="modal" role="dialog" tabindex="-1">
					<DIV class="modal-dialog" role="document">
						<DIV class="modal-content">
							<DIV class="modal-header">
								<H5 class="modal-title" id="modalLabel">裁切照片</H5>
								<BUTTON aria-label="Close" class="btn-close" data-bs-dismiss="modal" type="button"></BUTTON>
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
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="https://sdk.amazonaws.com/js/aws-sdk-2.207.0.min.js"/>
			<SCRIPT crossorigin="anonymous" src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.js" integrity="sha512-E4KfIuQAc9ZX6zW1IUJROqxrBqJXPuEcDKP6XesMdu2OV4LW7pj8+gkkyx2y646xEV7yxocPbaTtk2LQIJewXw==" referrerpolicy="no-referrer"/>
			<SCRIPT src="/SCRIPT/album.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>