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
					<BUTTON aria-current="true" aria-label="Slide 1" type="button" data-bs-target="#carousel" data-bs-slide-to="0" class="active"></BUTTON>
					<BUTTON aria-label="Slide 2" data-bs-slide-to="1" data-bs-target="#carousel" type="button"></BUTTON>
					<BUTTON aria-label="Slide 3" data-bs-slide-to="2" data-bs-target="#carousel" type="button"></BUTTON>
				</DIV>
				<DIV class="carousel-inner">
					<DIV class="carousel-item active">
						<IMG class="d-block w-100" src="{@photo}" alt="大頭照"/>
					</DIV>
					<DIV class="carousel-item">
						<IMG class="d-block w-100" src="https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/WOMAN/WOMAN+(2).jpg" alt="照片2"/>
					</DIV>
					<DIV class="carousel-item">
						<IMG class="d-block w-100" src="https://s3-ap-southeast-1.amazonaws.com/www.youngme.vip/IMAGE/WOMAN/WOMAN+(3).jpg" alt="照片3"/>
					</DIV>
				</DIV>
				<BUTTON class="carousel-control-prev" data-bs-slide="prev" data-bs-target="#carousel" type="button">
					<SPAN aria-hidden="true" class="carousel-control-prev-icon"></SPAN>
					<SPAN class="visually-hidden">Previous</SPAN>
				</BUTTON>
				<BUTTON class="carousel-control-next" data-bs-slide="next" data-bs-target="#carousel" type="button">
					<SPAN aria-hidden="true" class="carousel-control-next-icon"></SPAN>
					<SPAN class="visually-hidden">Next</SPAN>
				</BUTTON>
				<A href="/album.asp">
					<I class="fad fa-camera"></I>
				</A>
			</DIV>
			<DIV class="d-flex">
				<DIV class="d-flex justify-content-center justify-content-md-start" id="footer">
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
				<DIV class="ms-md-auto">
					<A class="btn btn-icon-only btn-link mx-md-4" href="/me.asp">
						<I class="fad fa-pen"></I>
					</A>
				</DIV>
			</DIV>
		</DIV>
		<DIV class="col-md-4 ms-4">
			<H3 class="text-primary">
				<xsl:value-of select="@nickName"/>
				<SPAN class="text-dark ms-3 text-lg">
					<xsl:value-of select="@location"/>
				</SPAN>
				<SPAN class="text-dark ms-3 text-lg">
					<xsl:value-of select="@age"/>
				</SPAN>
				<SPAN class="text-dark ms-3 text-lg">
					<xsl:value-of select="@gender"/>
				</SPAN>
			</H3>
			<HR class="horizontal dark my-4"/>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">身高：</SPAN>
				<SPAN>
					<xsl:value-of select="@height"/>
				</SPAN>
				<SPAN>公分</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">體重：</SPAN>
				<SPAN>
					<xsl:value-of select="@weight"/>
				</SPAN>
				<SPAN>公斤</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">體型：</SPAN>
				<SPAN>
					<xsl:value-of select="@bodyType"/>
				</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">學歷：</SPAN>
				<SPAN>大專以上</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">婚姻狀態：</SPAN>
				<SPAN>單身</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">抽菸習慣：</SPAN>
				<SPAN>偶爾</SPAN>
			</DIV>
			<DIV class="mb-2">
				<SPAN class="font-weight-bold text-lg">飲酒習慣：</SPAN>
				<SPAN>偶爾</SPAN>
			</DIV>
			<HR class="horizontal dark my-4"/>
			<DIV class="mb-2">
				<DIV class="font-weight-bold text-lg">關於我：</DIV>
				<DIV>
					<xsl:value-of select="@intro"/>
				</DIV>
			</DIV>
			<DIV class="mb-2">
				<DIV class="font-weight-bold text-lg">理想中的約會對象：</DIV>
				<DIV>好相處<br/>可依靠<br/>你情我願</DIV>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>