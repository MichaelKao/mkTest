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
                                                                <div class="carousel">
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/indexBg.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/relief.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg"/>
                                                                                </a>
                                                                        </div>
                                                                </div>
                                                                <DIV class="articleBox">
                                                                        <DIV class="article">
                                                                                <p>工作忙碌加上生活圈封閉，其實被催婚很煩之外，假日休息都不知道約誰。</p>
                                                                                <p>加上又不太敢直接搭訕女生，怕尷尬也覺得身分年紀有差。</p>
                                                                                <p>嘗試一般交友軟體上的女生，又只看臉，不然就是詐騙投資客。</p>
                                                                                <p>在養蜜上，透過送喜歡的女孩ME點當作禮物，受到的回饋都很好，也有破冰的效果。</p>
                                                                                <p>出租約會的形式也讓邀約不會很奇怪，互動聊過後提出約會，並附上金額，也可以照顧到喜歡的女孩，後續也有繼續聯絡變成飯友。</p>
                                                                        </DIV>
                                                                </DIV>
                                                                <div class="date text-xs text-right">2021/10/21 16:24</div>
                                                        </div>
                                                        <div class="postFooter">
                                                                <div class="d-flex">
                                                                        <div class="seeComment text-white me-auto cursor-pointer">
                                                                                <i class="fas fa-comment me-2"></i>
                                                                                <span>2</span>
                                                                        </div>
                                                                </div>
                                                                <div class="comments mt-3" style="display: none;">
                                                                        <div class="comment d-flex mb-2">
                                                                                <div class="flex-shrink-0">
                                                                                        <div class="avatar rounded-circle">
                                                                                                <img class="avatar-img" src="https://d35hi420xc5ji7.cloudfront.net/pictures/77e5baa1-7f01-4ae6-bfc1-d3d2478b5476" alt=""/>
                                                                                        </div>
                                                                                </div>
                                                                                <div class="flex-grow-1 ms-2 ms-sm-3">
                                                                                        <div class="comment-meta d-flex align-items-baseline text-white">
                                                                                                <div class="me-2 text-bold">小羽</div>
                                                                                                <span class="text-xs">2021/10/21</span>
                                                                                        </div>
                                                                                        <div class="comment-body text-dark text-sm">
                                                                                                Lorem ipsum, dolor sit amet consectetur adipisicing elit. Non minima ipsum at amet doloremque qui magni, placeat deserunt pariatur itaque laudantium impedit aliquam eligendi repellendus excepturi quibusdam nobis esse accusantium.
                                                                                        </div>
                                                                                </div>
                                                                        </div>
                                                                        <div class="comment d-flex mb-2">
                                                                                <div class="flex-shrink-0">
                                                                                        <div class="avatar rounded-circle">
                                                                                                <img class="avatar-img" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/b421cf3b-8010-4156-8c79-72a756369b81" alt=""/>
                                                                                        </div>
                                                                                </div>
                                                                                <div class="flex-grow-1 ms-2 ms-sm-3">
                                                                                        <div class="comment-meta d-flex align-items-baseline text-white">
                                                                                                <div class="me-2 text-bold">Yumi</div>
                                                                                                <span class="text-xs">2021/10/22</span>
                                                                                        </div>
                                                                                        <div class="comment-body text-dark text-sm">
                                                                                                Lorem ipsum dolor sit amet consectetur adipisicing elit. Iusto laborum in corrupti dolorum, quas delectus nobis porro accusantium molestias sequi.
                                                                                        </div>
                                                                                </div>
                                                                        </div>
                                                                </div>
                                                                <div class="comment-form d-flex align-items-center mt-3">
                                                                        <div class="avatar rounded-circle">
                                                                                <img class="avatar-img" src="https://d35hi420xc5ji7.cloudfront.net/profileImage/591a8223-0a84-4e31-a03a-f7a5fa96e22b" alt=""/>
                                                                        </div>
                                                                        <div class="w-100 ms-2">
                                                                                <form class="position-relative">
                                                                                        <textarea class="form-control text-white" rows="1" placeholder="新增留言..."></textarea>
                                                                                        <div class="commentBtn text-sm text-bold text-light cursor-pointer">提交</div>
                                                                                </form>
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
                                                                <DIV class="articleBox">
                                                                        <DIV class="article">
                                                                                <p>工作忙碌加上生活圈封閉，其實被催婚很煩之外，假日休息都不知道約誰。</p>
                                                                                <p>加上又不太敢直接搭訕女生，怕尷尬也覺得身分年紀有差。</p>
                                                                                <p>嘗試一般交友軟體上的女生，又只看臉，不然就是詐騙投資客。</p>
                                                                                <p>在養蜜上，透過送喜歡的女孩ME點當作禮物，受到的回饋都很好，也有破冰的效果。</p>
                                                                                <p>出租約會的形式也讓邀約不會很奇怪，互動聊過後提出約會，並附上金額，也可以照顧到喜歡的女孩，後續也有繼續聯絡變成飯友。</p>
                                                                        </DIV>
                                                                </DIV>
                                                                <div class="date text-xs text-right">2021/10/21 16:24</div>
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
                                                                <DIV class="articleBox">
                                                                        <DIV class="article">
                                                                                <p>工作忙碌加上生活圈封閉，其實被催婚很煩之外，假日休息都不知道約誰。</p>
                                                                                <p>加上又不太敢直接搭訕女生，怕尷尬也覺得身分年紀有差。</p>
                                                                        </DIV>
                                                                </DIV>
                                                                <div class="date text-xs text-right">2021/10/21 16:24</div>
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
                                                                <div class="carousel">
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/about.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/woman.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                        <div class="single">
                                                                                <a href="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg">
                                                                                        <img src="https://d2wqx6u4nuhgzp.cloudfront.net/IMAGE/man.jpg" loading="lazy"/>
                                                                                </a>
                                                                        </div>
                                                                </div>
                                                                <DIV class="articleBox">
                                                                        <DIV class="article"></DIV>
                                                                </DIV>
                                                                <div class="date text-xs text-right">2021/10/21 16:24</div>
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
                        <SCRIPT src="https://cdn.jsdelivr.net/jquery.slick/1.6.0/slick.min.js"/>
                        <SCRIPT src="https://mreq.github.io/slick-lightbox/dist/slick-lightbox.js"/>
                        <SCRIPT src="/SCRIPT/forum.js"/>
                        <xsl:if test="@signIn">
                                <SCRIPT src="/SCRIPT/websocket.js"/>
                        </xsl:if>
                </BODY>
        </xsl:template>
</xsl:stylesheet>