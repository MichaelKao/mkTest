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
                        <LINK href="/STYLE/forum.css" rel="stylesheet"/>
                </HEAD>
                <BODY>
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <div class="wrap px-3 pt-6 pt-md-7 pb-3">
                                <div class="content px-1">
                                        <div class="posts col-md-10 col-lg-8 col-xl-6 mx-auto">
                                                <div class="post">
                                                        <div class="header">
                                                                <div class="left">
                                                                        <img class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" />
                                                                        <div class="username">
                                                                                <div class="title text-dark">
                                                                                        被照顧的感覺真好
                                                                                </div>
                                                                                <div class="name text-xs">艾瑪
                                                                                        <i class="fas fa-shield-check text-success"></i>
                                                                                </div>
                                                                        </div>
                                                                </div>
                                                                <div class="right">
                                                                        <i class="far fa-ellipsis-v fontSize25"></i>
                                                                </div>
                                                        </div>
                                                        <div class="content">
                                                                "The greatest glory in living lies not in never falling, but in rising every time we fall." -Nelson Mandela.
                                                        </div>
                                                        <div class="postFooter">
                                                                <div class="d-flex">
                                                                        <div class="comments text-white me-auto cursor-pointer">
                                                                                <i class="fas fa-comment me-2"></i>
                                                                                <span>15</span>
                                                                        </div>
                                                                </div>
                                                        </div>
                                                </div>
                                                <div class="post">
                                                        <div class="header">
                                                                <div class="left">
                                                                        <img class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/pictures/77e5baa1-7f01-4ae6-bfc1-d3d2478b5476" />
                                                                        <div class="username">
                                                                                <div class="title text-dark">
                                                                                        支持夢想的最大幫手
                                                                                </div>
                                                                                <div class="name text-xs">小羽</div>
                                                                        </div>
                                                                </div>
                                                                <div class="right">
                                                                        <i class="far fa-ellipsis-v fontSize25"></i>
                                                                </div>
                                                        </div>
                                                        <div class="content">
                                                                "The greatest glory in living lies not in never falling, but in rising every time we fall." -Nelson Mandela.
                                                        </div>
                                                        <div class="postFooter">
                                                                <div class="d-flex">
                                                                        <div class="comments text-white me-auto cursor-pointer">
                                                                                <i class="fas fa-comment me-2"></i>
                                                                                <span>15</span>
                                                                        </div>
                                                                </div>
                                                        </div>
                                                </div>
                                                <div class="post">
                                                        <div class="header">
                                                                <div class="left">
                                                                        <img class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" />
                                                                        <div class="username">
                                                                                <div class="title text-dark">
                                                                                        送ME點破冰，找到約會
                                                                                </div>
                                                                                <div class="name text-xs">艾瑪
                                                                                        <i class="fas fa-shield-check text-success"></i>
                                                                                </div>
                                                                        </div>
                                                                </div>
                                                                <div class="right">
                                                                        <i class="far fa-ellipsis-v fontSize25"></i>
                                                                </div>
                                                        </div>
                                                        <div class="content">
                                                                "The greatest glory in living lies not in never falling, but in rising every time we fall." -Nelson Mandela.
                                                        </div>
                                                        <div class="postFooter">
                                                                <div class="d-flex">
                                                                        <div class="comments text-white me-auto cursor-pointer">
                                                                                <i class="fas fa-comment me-2"></i>
                                                                                <span>15</span>
                                                                        </div>
                                                                </div>
                                                        </div>
                                                </div>
                                                <div class="post">
                                                        <div class="header">
                                                                <div class="left">
                                                                        <img class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" />
                                                                        <div class="username">
                                                                                <div class="title text-dark">
                                                                                        送ME點破冰，找到約會
                                                                                </div>
                                                                                <div class="name text-xs">艾瑪
                                                                                        <i class="fas fa-shield-check text-success"></i>
                                                                                </div>
                                                                        </div>
                                                                </div>
                                                                <div class="right">
                                                                        <i class="far fa-ellipsis-v fontSize25"></i>
                                                                </div>
                                                        </div>
                                                        <div class="content">
                                                                <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg"/>
                                                        </div>
                                                        <div class="postFooter">
                                                                <div class="d-flex">
                                                                        <div class="comments text-white me-auto cursor-pointer">
                                                                                <i class="fas fa-comment me-2"></i>
                                                                                <span>15</span>
                                                                        </div>
                                                                </div>
                                                        </div>
                                                </div>
                                        </div>
                                </div>
                        </div>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT crossorigin="anonymous" src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.5/cropper.min.js" integrity="sha512-E4KfIuQAc9ZX6zW1IUJROqxrBqJXPuEcDKP6XesMdu2OV4LW7pj8+gkkyx2y646xEV7yxocPbaTtk2LQIJewXw==" referrerpolicy="no-referrer"/>
                        <xsl:if test="@signIn">
                                <SCRIPT src="/SCRIPT/websocket.js"/>
                        </xsl:if>
                </BODY>
        </xsl:template>
</xsl:stylesheet>