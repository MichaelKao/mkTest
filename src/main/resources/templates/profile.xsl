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
			<LINK href="/STYLE/profile.css" rel="stylesheet"/>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-8">
				<DIV class="row justify-content-center">
					<xsl:apply-templates select="lover"/>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/profile.js"/>
		</BODY>
	</xsl:template>
	<xsl:template match="lover">
		<DIV class="col-md-5 mb-4">
			<DIV class="carousel slide" data-bs-ride="carousel" id="carousel">
				<DIV class="carousel-indicators">
					<BUTTON aria-current="true" type="button" data-bs-target="#carousel" data-bs-slide-to="0" class="active"></BUTTON>
					<xsl:for-each select="picture">
						<BUTTON data-bs-slide-to="{position()}" data-bs-target="#carousel" type="button"></BUTTON>
					</xsl:for-each>
				</DIV>
				<DIV class="carousel-inner">
					<DIV class="carousel-item active">
						<IMG class="d-block w-100" src="{profileImage}" alt="大頭照"/>
					</DIV>
					<xsl:for-each select="picture">
						<DIV class="carousel-item">
							<IMG class="d-block w-100" src="{.}" alt="照片{position()}"/>
						</DIV>
					</xsl:for-each>
				</DIV>
				<BUTTON class="carousel-control-prev" data-bs-slide="prev" data-bs-target="#carousel" type="button">
					<SPAN aria-hidden="true" class="carousel-control-prev-icon"></SPAN>
					<SPAN class="visually-hidden">Previous</SPAN>
				</BUTTON>
				<BUTTON class="carousel-control-next" data-bs-slide="next" data-bs-target="#carousel" type="button">
					<SPAN aria-hidden="true" class="carousel-control-next-icon"></SPAN>
					<SPAN class="visually-hidden">Next</SPAN>
				</BUTTON>
				<xsl:if test="/document/@me">
					<A href="/album.asp">
						<I class="fad fa-camera"></I>
					</A>
				</xsl:if>
			</DIV>
			<DIV class="d-flex">
				<xsl:choose>
					<xsl:when test="not(/document/@me)">
						<DIV class="d-flex justify-content-center justify-content-md-start" id="icon">
							<DIV>
								<BUTTON class="btn btn-icon-only btn-link fav" href="https://lin.ee/LJprCs3">
									<I class="fad fa-heart"></I>
								</BUTTON>
							</DIV>
							<DIV class="mx-2">
								<BUTTON class="btn btn-icon-only btn-link mx-4 ms-md-3" href="https://lin.ee/LJprCs3">
									<I class="fad fa-comment-plus"></I>
								</BUTTON>
							</DIV>
						</DIV>
					</xsl:when>
					<xsl:otherwise>
						<DIV class="ms-md-auto">
							<A class="btn btn-icon-only btn-link mx-md-4" href="/me.asp">
								<I class="fad fa-pen"></I>
							</A>
						</DIV>
					</xsl:otherwise>
				</xsl:choose>
			</DIV>
		</DIV>
		<DIV class="col-md-4 ms-4">
			<DIV class="d-flex align-items-center">
				<H3 class="text-primary me-2">
					<xsl:value-of select="nickname"/>
				</H3>
				<DIV class="text-dark text-bold mx-1">
					<xsl:value-of select="location"/>
				</DIV>
				<DIV class="text-dark text-bold mx-1">
					<xsl:value-of select="age"/>
				</DIV>
				<DIV class="text-dark text-bold mx-1">
					<xsl:value-of select="gender"/>
				</DIV>
			</DIV>
			<DIV class="d-flex align-items-center">
				<DIV class="text-dark text-bold me-1">上一次上線</DIV>
				<DIV class="text-primary text-bold mx-1">
					<xsl:value-of select="active"/>
				</DIV>

			</DIV>
			<HR class="horizontal dark my-4"/>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">身高：</SPAN>
				<SPAN>
					<xsl:value-of select="height"/>
				</SPAN>
				<SPAN>公分</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">體重：</SPAN>
				<SPAN>
					<xsl:value-of select="weight"/>
				</SPAN>
				<SPAN>公斤</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">體型：</SPAN>
				<SPAN>
					<xsl:value-of select="bodyType"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">學歷：</SPAN>
				<SPAN>
					<xsl:value-of select="education"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">婚姻狀態：</SPAN>
				<SPAN>
					<xsl:value-of select="marriage"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">職業：</SPAN>
				<SPAN>
					<xsl:value-of select="occupation"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">抽菸習慣：</SPAN>
				<SPAN>
					<xsl:value-of select="smoking"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">飲酒習慣：</SPAN>
				<SPAN>
					<xsl:value-of select="drinking"/>
				</SPAN>
			</DIV>
			<HR class="horizontal dark my-4"/>
			<DIV class="mb-2">
				<DIV class="font-weight-bold text-lg">關於我：</DIV>
				<DIV class="aboutMe">
					<xsl:value-of select="aboutMe"/>
				</DIV>
			</DIV>
			<DIV class="mb-2">
				<DIV class="font-weight-bold text-lg">理想中的約會對象：</DIV>
				<DIV class="idealConditions">
					<xsl:value-of select="idealConditions"/>
				</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>