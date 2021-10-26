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
                                                                <p class="text-warning text-bold">刪除帳號將無法再恢復，確定要刪除？</p>
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
                                                                <IMG class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b"/>
                                                                <DIV class="username">
                                                                        <DIV class="name">艾瑪
                                                                                <I class="fas fa-shield-check text-success"></I>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="postFooter">
                                                        <DIV class="d-flex flex-wrap" id="imgBox"></DIV>
                                                        <INPUT class="form-control addPostTextarea mb-2 pt-2 pb-1 text-lg text-white" name="title" placeholder="輸入標題"/>
                                                        <TEXTAREA class="form-control text-white addPostTextarea" name="markdown" rows="3" placeholder="輸入內容...."></TEXTAREA>
                                                        <DIV class="d-flex flex-wrap mt-1 justify-content-end">
                                                                <xsl:for-each select="forumThreadTags/forumThreadTags">
                                                                        <DIV class="form-check p-0">
                                                                                <INPUT class="form-check-input" id="service{id}" name="hashTag" type="radio" value="{id}"/>
                                                                                <LABEL class="custom-control-label tag" for="service{id}">
                                                                                        <SPAN>#</SPAN>
                                                                                        <SPAN>
                                                                                                <xsl:value-of select="phrase"/>
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
                                        <DIV class="post">
                                                <DIV class="header">
                                                        <DIV class="left">
                                                                <IMG class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" />
                                                                <DIV class="username">
                                                                        <DIV class="title text-dark">
                                                                                被照顧的感覺真好
                                                                        </DIV>
                                                                        <DIV class="name text-xs">艾瑪
                                                                                <I class="fas fa-shield-check text-success"></I>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="right">
                                                                <I class="far fa-ellipsis-v fontSize25"></I>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="content">
                                                        <DIV class="carousel">
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg"/>
                                                                        </A>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="articleBox">
                                                                <DIV class="article">
                                                                        <P>工作忙碌加上生活圈封閉，其實被催婚很煩之外，假日休息都不知道約誰。</P>
                                                                        <P>加上又不太敢直接搭訕女生，怕尷尬也覺得身分年紀有差。</P>
                                                                        <P>嘗試一般交友軟體上的女生，又只看臉，不然就是詐騙投資客。</P>
                                                                        <P>在養蜜上，透過送喜歡的女孩ME點當作禮物，受到的回饋都很好，也有破冰的效果。</P>
                                                                        <P>出租約會的形式也讓邀約不會很奇怪，互動聊過後提出約會，並附上金額，也可以照顧到喜歡的女孩，後續也有繼續聯絡變成飯友。</P>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="date text-xs text-right">2021/10/21 16:24</DIV>
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
                                                                        <IMG class="avatar-img" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" alt=""/>
                                                                </DIV>
                                                                <DIV class="w-100 ms-2 position-relative">
                                                                        <TEXTAREA class="form-control text-white commentTextarea" rows="1" placeholder="新增留言..."></TEXTAREA>
                                                                        <BUTTON class="btn btn-link m-0 p-0 commentBtn text-sm text-bold" disabled="true">提交</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="post">
                                                <DIV class="header">
                                                        <DIV class="left">
                                                                <IMG class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/pictures/77e5baa1-7f01-4ae6-bfc1-d3d2478b5476" />
                                                                <DIV class="username">
                                                                        <DIV class="title text-dark">
                                                                                支持夢想的最大幫手
                                                                        </DIV>
                                                                        <DIV class="name text-xs">小羽</DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="right">
                                                                <I class="far fa-ellipsis-v fontSize25"></I>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="content">
                                                        <DIV class="articleBox">
                                                                <DIV class="article">
                                                                        <P>工作忙碌加上生活圈封閉，其實被催婚很煩之外，假日休息都不知道約誰。</P>
                                                                        <P>加上又不太敢直接搭訕女生，怕尷尬也覺得身分年紀有差。</P>
                                                                        <P>嘗試一般交友軟體上的女生，又只看臉，不然就是詐騙投資客。</P>
                                                                        <P>在養蜜上，透過送喜歡的女孩ME點當作禮物，受到的回饋都很好，也有破冰的效果。</P>
                                                                        <P>出租約會的形式也讓邀約不會很奇怪，互動聊過後提出約會，並附上金額，也可以照顧到喜歡的女孩，後續也有繼續聯絡變成飯友。</P>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="date text-xs text-right">2021/10/21 16:24</DIV>
                                                </DIV>
                                                <DIV class="postFooter">
                                                        <DIV class="d-flex">
                                                                <DIV class="seeComment text-white me-auto cursor-pointer">
                                                                        <I class="fas fa-comment me-2"></I>
                                                                        <SPAN class="commentCount">1</SPAN>
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
                                                                                <DIV class="commentMeta d-flex align-items-baseline text-sm">
                                                                                        <DIV class="me-2 text-bold">小羽</DIV>
                                                                                        <SPAN class="text-xs">2021/10/21</SPAN>
                                                                                </DIV>
                                                                                <DIV class="commentBody text-dark text-sm text-white">
                                                                                        Lorem ipsum, dolor sit amet consectetur adipisicing elit. Non minima ipsum at amet doloremque qui magni, placeat deserunt pariatur itaque laudantium impedit aliquam eligendi repellendus excepturi quibusdam nobis esse accusantium.
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="d-flex align-items-center mt-3">
                                                                <DIV class="avatar rounded-circle">
                                                                        <IMG class="avatar-img" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" alt=""/>
                                                                </DIV>
                                                                <DIV class="w-100 ms-2 position-relative">
                                                                        <TEXTAREA class="form-control text-white commentTextarea" rows="1" placeholder="新增留言..."></TEXTAREA>
                                                                        <BUTTON class="btn btn-link m-0 p-0 commentBtn text-sm text-bold" disabled="true">提交</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="post">
                                                <DIV class="header">
                                                        <DIV class="left">
                                                                <IMG class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" />
                                                                <DIV class="username">
                                                                        <DIV class="title text-dark">
                                                                                送ME點破冰，找到約會
                                                                        </DIV>
                                                                        <DIV class="name text-xs">艾瑪
                                                                                <I class="fas fa-shield-check text-success"></I>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="right">
                                                                <I class="far fa-ellipsis-v fontSize25"></I>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="content">
                                                        <DIV class="articleBox">
                                                                <DIV class="article">
                                                                        <P>工作忙碌加上生活圈封閉，其實被催婚很煩之外，假日休息都不知道約誰。</P>
                                                                        <P>加上又不太敢直接搭訕女生，怕尷尬也覺得身分年紀有差。</P>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="date text-xs text-right">2021/10/21 16:24</DIV>
                                                </DIV>
                                                <DIV class="postFooter">
                                                        <DIV class="d-flex">
                                                                <DIV class="comments text-white me-auto cursor-pointer">
                                                                        <I class="fas fa-comment me-2"></I>
                                                                        <SPAN>15</SPAN>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="post">
                                                <DIV class="header">
                                                        <DIV class="left">
                                                                <IMG class="avatar shadow me-2" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" />
                                                                <DIV class="username">
                                                                        <DIV class="title text-dark">
                                                                                送ME點破冰，找到約會
                                                                        </DIV>
                                                                        <DIV class="name text-xs">艾瑪
                                                                                <I class="fas fa-shield-check text-success"></I>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="right">
                                                                <I class="far fa-ellipsis-v fontSize25"></I>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="content">
                                                        <DIV class="carousel">
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="single">
                                                                        <A href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                <IMG src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg" loading="lazy"/>
                                                                        </A>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="articleBox">
                                                                <DIV class="article"></DIV>
                                                        </DIV>
                                                        <DIV class="date text-xs text-right">2021/10/21 16:24</DIV>
                                                </DIV>
                                                <DIV class="postFooter">
                                                        <DIV class="d-flex">
                                                                <DIV class="comments text-white me-auto cursor-pointer">
                                                                        <I class="fas fa-comment me-2"></I>
                                                                        <SPAN>15</SPAN>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
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