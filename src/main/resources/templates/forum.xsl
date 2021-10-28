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
                </HEAD>
                <BODY>
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <BUTTON class="btn btn-link m-0 p-0 addPostFloatBtn d-flex align-items-center justify-content-center position-fixed bg-primary fontSize25 text-white opacity-9" data-bs-target="#addPostModal" data-bs-toggle="modal" type="button">
                                <I class="fas fa-edit ms-1"></I>
                        </BUTTON>
                        <DIV class="modal fade" id="addPostModal">
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
                                                        </DIV>
                                                        <DIV class="text-center">
                                                                <BUTTON class="btn btn-outline-primary mx-2 px-3 py-2 confirmBtn" type="button">確認</BUTTON>
                                                                <BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </DIV>
                        <DIV class="wrap px-3 pt-6 pt-md-7 pb-3">
                                <DIV class="addPost col-md-10 col-lg-8 col-xl-6 mx-auto">
                                        <DIV class="post">
                                                <DIV class="header py-2">
                                                        <DIV class="left">
                                                                <IMG alt="self-profileImage" class="avatar shadow me-2" src="{self/@profileImage}"/>
                                                                <DIV class="username">
                                                                        <DIV class="name">
                                                                                <SPAN>
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
                                                </DIV>
                                                <DIV class="postFooter">
                                                        <DIV class="d-flex flex-wrap" id="imgBox"></DIV>
                                                        <INPUT class="form-control addPostTextarea mb-2 pt-2 pb-1 text-lg text-white" name="title" placeholder="輸入標題"/>
                                                        <TEXTAREA class="form-control text-white addPostTextarea" name="markdown" rows="3" placeholder="輸入內容...."></TEXTAREA>
                                                        <DIV class="d-flex flex-wrap mt-1 justify-content-end">
                                                                <xsl:for-each select="forumThreadTag">
                                                                        <DIV class="form-check p-0">
                                                                                <INPUT class="form-check-input" id="service{@id}" name="hashTag" type="radio" value="{@id}"/>
                                                                                <LABEL class="custom-control-label tag" for="service{@id}">
                                                                                        <SPAN>#</SPAN>
                                                                                        <SPAN>
                                                                                                <xsl:value-of select="."/>
                                                                                        </SPAN>
                                                                                </LABEL>
                                                                        </DIV>
                                                                </xsl:for-each>
                                                        </DIV>
                                                        <DIV class="d-flex mt-1">
                                                                <DIV class="ms-auto d-flex">
                                                                        <LABEL class="m-0 me-2">
                                                                                <INPUT accept="image/*" class="sr-only" id="file" multiple="" name="illustrations" type="file"/>
                                                                                <SPAN class="addImages text-white cursor-pointer position-relative me-3">
                                                                                        <I class="fas fa-images fontSize22"></I>
                                                                                        <I class="fas fa-plus position-absolute text-xs addIcon"></I>
                                                                                </SPAN>
                                                                        </LABEL>
                                                                        <SPAN class="text-white">|</SPAN>
                                                                        <BUTTON class="btn btn-link btn-round text-white text-lg m-0 p-0 ms-3" id="postBtn" type="button">發佈</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                                <DIV class="posts col-md-10 col-lg-8 col-xl-6 mx-auto">
                                        <xsl:for-each select="forumThreads/forumThread">
                                                <DIV class="post">
                                                        <DIV class="header">
                                                                <DIV class="left">
                                                                        <IMG class="avatar shadow me-2" src="{author/@profileImage}" />
                                                                        <DIV class="username">
                                                                                <DIV class="title text-dark">
                                                                                        <xsl:value-of select="title"/>
                                                                                </DIV>
                                                                                <DIV class="name text-xs">
                                                                                        <SPAN>
                                                                                                <xsl:value-of select="author/@nickname"/>
                                                                                        </SPAN>
                                                                                        <I class="fas fa-shield-check ms-1">
                                                                                                <xsl:if test="author/@relief = 'true'">
                                                                                                        <xsl:attribute name="class">fas fa-shield-check text-success ms-1</xsl:attribute>
                                                                                                </xsl:if>
                                                                                        </I>
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="content">
                                                                <DIV class="carousel">
                                                                        <xsl:for-each select="illustration">
                                                                                <DIV class="single">
                                                                                        <A href="{.}">
                                                                                                <IMG alt="illustration" src="{.}" loading="lazy"/>
                                                                                        </A>
                                                                                </DIV>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                                <DIV class="articleBox">
                                                                        <DIV class="article">
                                                                                <xsl:value-of disable-output-escaping="yes" select="markdown"/>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="date text-xs text-right">
                                                                        <xsl:value-of select="date"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="postFooter">
                                                                <DIV class="d-flex">
                                                                        <DIV class="seeComment text-white me-auto cursor-pointer">
                                                                                <I class="fas fa-comment me-2"></I>
                                                                                <SPAN class="commentCount">2</SPAN>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="comments mt-3" style="display: none;">
                                                                        <DIV class="comment d-flex mb-2">
                                                                                <DIV class="flex-shrink-0">
                                                                                        <DIV class="avatar rounded-circle">
                                                                                                <IMG class="avatar-img" src="https://d35hi420xc5ji7.cloudfront.net/pictures/77e5baa1-7f01-4ae6-bfc1-d3d2478b5476" alt=""/>
                                                                                        </DIV>
                                                                                </DIV>
                                                                                <DIV class="flex-grow-1 ms-2 ms-sm-3">
                                                                                        <DIV class="commentMeta d-flex align-items-baseline">
                                                                                                <DIV class="me-2 text-bold">小羽</DIV>
                                                                                                <SPAN class="text-xs">2021/10/21</SPAN>
                                                                                        </DIV>
                                                                                        <DIV class="commentBody text-dark text-sm text-white">
                                                                                                Lorem ipsum, dolor sit amet consectetur adipisicing elit. Non minima ipsum at amet doloremque qui magni, placeat deserunt pariatur itaque laudantium impedit aliquam eligendi repellendus excepturi quibusdam nobis esse accusantium.
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </DIV>
                                                                        <DIV class="comment d-flex mb-2">
                                                                                <DIV class="flex-shrink-0">
                                                                                        <DIV class="avatar rounded-circle">
                                                                                                <IMG class="avatar-img" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/b421cf3b-8010-4156-8c79-72a756369b81" alt=""/>
                                                                                        </DIV>
                                                                                </DIV>
                                                                                <DIV class="flex-grow-1 ms-2 ms-sm-3">
                                                                                        <DIV class="commentMeta d-flex align-items-baseline text-sm">
                                                                                                <DIV class="me-2 text-bold">Yumi</DIV>
                                                                                                <SPAN class="text-xs">2021/10/22</SPAN>
                                                                                        </DIV>
                                                                                        <DIV class="commentBody text-dark text-sm text-white">
                                                                                                Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto laborum in corrupti dolorum, quas delectus nobis porro accusantium molestias sequi.
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="d-flex align-items-center mt-3">
                                                                        <DIV class="avatar rounded-circle">
                                                                                <IMG alt="self-profileImage" class="avatar-img" src="{/document/self/@profileImage}"/>
                                                                        </DIV>
                                                                        <DIV class="w-100 ms-2 position-relative">
                                                                                <TEXTAREA class="form-control text-white commentTextarea" rows="1" placeholder="新增留言..."></TEXTAREA>
                                                                                <BUTTON class="btn btn-link m-0 p-0 commentBtn text-sm text-bold" disabled="true">提交</BUTTON>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </xsl:for-each>
                                </DIV>
                        </DIV>
                        <xsl:call-template name="footer"/>
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