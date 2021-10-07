<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
                        <LINK href="/manifest.json" rel="manifest"/>
                        <xsl:call-template name="headLinkTags"/>
                        <LINK href="https://fonts.googleapis.com/css2?family=Crimson+Text:wght@600" rel="stylesheet"/>
                        <LINK href="https://npmcdn.com/flickity@2/dist/flickity.css" rel="stylesheet"/>
                        <LINK href="/STYLE/index.css" rel="stylesheet"/>
                </HEAD>
                <BODY class="pb-7">
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <xsl:if test="not(@signIn)">
                                <HEADER class="header-2">
                                        <INPUT name="signIn" type="hidden" value="false"/>
                                        <DIV class="page-header section-height-100 relative indexBg">
                                                <DIV class="container text-center">
                                                        <DIV class="d-flex flex-column flex-md-row align-items-center">
                                                                <DIV class="text-center mx-auto">
                                                                        <IMG class="iconImg" src="/ICON/youngme.png" width="300"/>
                                                                </DIV>
                                                                <DIV class="text-center mx-auto px-2 px-sm-4 px-md-6 py-4 mt-3 border-radius-md introBox">
                                                                        <DIV class="fontSize22 text-bolder text-dark">
                                                                                <DIV>出租約會 · 終結孤單</DIV>
                                                                                <DIV>比閨密更親密的美好關係</DIV>
                                                                        </DIV>
                                                                        <xsl:if test="not(@signIn)">
                                                                                <DIV class="mt-2">
                                                                                        <A class="btn btn-primary btn-round btn-sm px-4 py-2 mx-2 mb-0 imageShadow" href="/signIn.asp">登入</A>
                                                                                        <A class="btn btn-primary btn-round btn-sm px-4 py-2 mx-2 mb-0 imageShadow" href="/signUp.asp" onclick="fbq('track','StartTrial');">註冊</A>
                                                                                </DIV>
                                                                        </xsl:if>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </HEADER>
                                <SECTION class="pb-3 container">
                                        <DIV class="text-primary aboutTitle text-right pe-xl-7">ABOUT</DIV>
                                        <DIV class="d-flex flex-column flex-lg-row justify-content-center align-items-center">
                                                <DIV class="position-relative text-dark aboutIntro px-3 py-4 px-md-5 py-md-4">
                                                        <DIV class="position-absolute fontSize35 aboutSubTitle">YOUNG ME</DIV>
                                                        <DIV class="aboutIntroMainContent">
                                                                <DIV>
                                                                        <SPAN class="fontSize30 contentTitle">養蜜</SPAN>
                                                                        <SPAN>，是我們提倡的新形態關係</SPAN>
                                                                </DIV>
                                                                <DIV>透過實質寵愛，培養感情找到伴侶</DIV>
                                                                <DIV class="my-1 my-md-3">
                                                                        <DIV>
                                                                                <SPAN>每天，都是人生中，最</SPAN>
                                                                                <SPAN class="ms-1 text-primary">YOUNG</SPAN>
                                                                                <SPAN>，最青春的一天。</SPAN>
                                                                        </DIV>
                                                                        <DIV>
                                                                                <SPAN>成為自己</SPAN>
                                                                                <SPAN class="mx-1 text-primary">ME</SPAN>
                                                                                <SPAN>的主人，找到最好的對待。</SPAN>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV>不是買賣對價是對等且優良的關係，</DIV>
                                                                <DIV>共享時間與喜悅。</DIV>
                                                        </DIV>
                                                        <DIV class="position-absolute text-bold text-white bg-pink p-1 p-md-2 aboutIntroTag">#用甜蜜生活滋養你</DIV>
                                                </DIV>
                                                <DIV class="aboutImgDiv">
                                                        <IMG class="imageShadow" src="/IMAGE/about.jpg"/>
                                                </DIV>
                                        </DIV>
                                </SECTION>
                                <SECTION class="py-6 container">
                                        <DIV class="meMbers">
                                                <DIV class="position-relative text-dark px-3 py-4 meMbersWrap">
                                                        <DIV class="d-flex flex-column flex-lg-row justify-content-between">
                                                                <DIV class="position-relative meQueenWrap">
                                                                        <DIV class="meQueen imageShadow">
                                                                                <DIV class="meQueenIntro imageShadow position-absolute border-radius-2xl border-radius-bottom-start-0 position-relative ps-5 d-flex text-white">
                                                                                        <DIV class="d-flex flex-column justify-content-center text-white introLarge">
                                                                                                <DIV>MY DEAR, 妳的青春洋溢與活力讓人嚮往</DIV>
                                                                                                <DIV>迷人的妳，絕對值得最好的對待與寵愛</DIV>
                                                                                                <DIV>
                                                                                                        <A class="btn btn-dark py-2 px-3 m-0" href="/signUp.asp">開啟養蜜旅程</A>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                        <DIV class="d-flex flex-column justify-content-center text-white introSmall">
                                                                                                <DIV>MY DEAR, </DIV>
                                                                                                <DIV>妳的青春洋溢與活力讓人嚮往</DIV>
                                                                                                <DIV>迷人的妳</DIV>
                                                                                                <DIV>絕對值得最好的對待與寵愛</DIV>
                                                                                                <DIV>
                                                                                                        <A class="btn btn-dark py-2 px-3 m-0" href="/signUp.asp">開啟養蜜旅程</A>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                        <DIV class="position-absolute meQueenImg">
                                                                                                <IMG class="imageShadow" src="/IMAGE/whiteQueen.jpg"/>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="meKingWrap position-absolute">
                                                                        <DIV class="meKing position-relative imageShadow">
                                                                                <DIV class="meKingIntro imageShadow position-absolute border-radius-2xl border-radius-bottom-end-0 position-relative ps-3 d-flex">
                                                                                        <DIV class="d-flex flex-column justify-content-center align-items-end text-white introLarge">
                                                                                                <DIV>MY MEN, 事業有成重視效率，是職場的王者</DIV>
                                                                                                <DIV>但有害羞矜持的時刻，或缺乏合適管道與方法</DIV>
                                                                                                <DIV>
                                                                                                        <A class="btn btn-primary py-2 px-3 m-0" href="/signUp.asp">開啟養蜜旅程</A>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                        <DIV class="d-flex flex-column justify-content-center align-items-end text-white introSmall">
                                                                                                <DIV>MY MEN,</DIV>
                                                                                                <DIV>事業有成重視效率</DIV>
                                                                                                <DIV>是職場的王者</DIV>
                                                                                                <DIV>但有害羞矜持的時刻</DIV>
                                                                                                <DIV>或缺乏合適管道與方法</DIV>
                                                                                                <DIV>
                                                                                                        <A class="btn btn-primary py-2 px-3 m-0" href="/signUp.asp">開啟養蜜旅程</A>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                        <DIV class="position-absolute meKingImg">
                                                                                                <IMG class="imageShadow" src="/IMAGE/whiteKing.jpg"/>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="mbersTitle position-absolute">meMBERS</DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </SECTION>
                                <SECTION class="py-6 container px-1 px-md-4">
                                        <DIV class="carousel">
                                                <DIV class="carousel-cell row">
                                                        <DIV class="datingMode col-md-6 d-flex flex-column align-items-center">
                                                                <IMG class="imageShadow" src="/IMAGE/約會模式.jpg"/>
                                                        </DIV>
                                                        <DIV class="col-md-6 d-flex flex-column justify-content-center align-items-center mb-4 mb-md-0">
                                                                <H3 class="text-dark">
                                                                        <I class="fad fa-book-heart fontSize30 me-2"></I>
                                                                        <SPAN>多元約會模式</SPAN>
                                                                </H3>
                                                                <DIV class="text-white text-lg text-bolder">。透過約會標籤方便找到養蜜。</DIV>
                                                                <DIV class="text-white text-lg text-bolder">。全台地區多選，快速約會。</DIV>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="carousel-cell row">
                                                        <DIV class="socialMedia col-md-6 d-flex flex-column align-items-center">
                                                                <IMG class="imageShadow border-radius-2xl border-radius-top-end-0 border-radius-top-start-0" src="/IMAGE/通訊軟體.jpg"/>
                                                        </DIV>
                                                        <DIV class="col-md-6 socialMediaIntro d-flex flex-column justify-content-center align-items-center">
                                                                <H3 class="text-dark">
                                                                        <I class="fab fa-line fontSize35 me-1"></I>
                                                                        <I class="fab fa-weixin fontSize35 me-1"></I>
                                                                        <SPAN>通訊軟體綁定</SPAN>
                                                                </H3>
                                                                <DIV class="text-white text-lg text-bolder">。支援LINE/WeChat</DIV>
                                                                <DIV class="text-white text-lg text-bolder">系統轉跳加入好友。</DIV>
                                                                <DIV class="text-white text-lg text-bolder">。避免ID洩漏個資。</DIV>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="carousel-cell row">
                                                        <DIV class="reliefIntro col-md-6 d-flex justify-content-center align-items-center">
                                                                <DIV class="position-relative">
                                                                        <IMG class="imageShadow border-radius-2xl" src="/IMAGE/relief.jpg"/>
                                                                        <DIV class="position-absolute right-0 text-center" style="width: 55px; top: 10px;">
                                                                                <I class="fas fa-shield-check fontSize30 text-success text-shadow"></I>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="pb-5 pb-md-0 col-md-6 d-flex flex-column justify-content-center align-items-center">
                                                                <H3 class="text-dark">
                                                                        <I class="fas fa-shield-check fontSize30 me-2"></I>
                                                                        <SPAN>安心認證</SPAN>
                                                                </H3>
                                                                <DIV class="text-white text-lg text-bolder">。真人審核安全把關。</DIV>
                                                                <DIV class="text-white text-lg text-bolder">。認真專區加強曝光。</DIV>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="carousel-cell row">
                                                        <DIV class="mePoint col-md-6 d-flex justify-content-center align-items-start align-items-md-end">
                                                                <IMG class="imageShadow border-radius-2xl border-radius-bottom-end-0 border-radius-bottom-start-0" src="/IMAGE/me點.jpg"/>
                                                        </DIV>
                                                        <DIV class="mePoint2 col-md-6 d-flex flex-column justify-content-end justify-content-md-start align-items-center">
                                                                <IMG class="imageShadow border-radius-2xl border-radius-top-end-0 border-radius-top-start-0 order-2" src="/IMAGE/點數購買.jpg"/>
                                                                <DIV class="mb-6 mb-md-0 mt-md-4 order-md-2">
                                                                        <H3 class="text-dark">
                                                                                <I class="fad fa-badge-dollar fontSize30 me-2"></I>
                                                                                <SPAN>站內安全付款</SPAN>
                                                                        </H3>
                                                                        <DIV class="text-white text-lg text-bolder">。站內系統金流可檢舉退回。</DIV>
                                                                        <DIV class="text-white text-lg text-bolder">。付款交易隱藏私密。</DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </SECTION>
                        </xsl:if>
                        <xsl:if test="not(@lineNotify) and @signIn">
                                <DIV class="modal fade" id="lineNotifyModal" tabindex="-1">
                                        <DIV class="modal-dialog modal-dialog-centered">
                                                <DIV class="modal-content">
                                                        <DIV class="modal-body">
                                                                <DIV class="d-flex">
                                                                        <BUTTON class="btn-close bg-dark ms-auto" data-bs-dismiss="modal" type="button"></BUTTON>
                                                                </DIV>
                                                                <DIV class="mt-3 text-center">
                                                                        <I class="far fa-bell-on text-success mb-1" style="font-size: 50px;"></I>
                                                                        <H4>接收Line Notify即時通知?</H4>
                                                                        <P>這是LINE官方推出的新功能，若是有通知則可以推送到您的LINE上，第一時間收到通知消息，隱私又安全。</P>
                                                                </DIV>
                                                                <DIV class="text-center">
                                                                        <A class="btn btn-primary mx-2" href="/notify-bot.line.me/authorize.asp">接收</A>
                                                                        <BUTTON class="btn btn-secondary mx-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </xsl:if>
                        <xsl:if test="@signIn">
                                <INPUT name="signIn" type="hidden" value="true"/>
                                <DIV class="container px-0 px-md-3 pt-7">
                                        <INPUT name="gender" type="hidden">
                                                <xsl:if test="@male">
                                                        <xsl:attribute name="value">male</xsl:attribute>
                                                </xsl:if>
                                                <xsl:if test="@female">
                                                        <xsl:attribute name="value">female</xsl:attribute>
                                                </xsl:if>
                                        </INPUT>
                                        <xsl:apply-templates select="area"/>
                                </DIV>
                        </xsl:if>
                        <xsl:call-template name="footer"/>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT src="https://npmcdn.com/flickity@2/dist/flickity.pkgd.js"/>
                        <SCRIPT src="/SCRIPT/index.js"/>
                        <xsl:choose>
                                <xsl:when test="@signIn">
                                        <SCRIPT><![CDATA[fbq('track', 'memberspageview');]]></SCRIPT>
                                        <SCRIPT src="/SCRIPT/websocket.js"/>
                                </xsl:when>
                                <xsl:otherwise>
                                        <SCRIPT><![CDATA[fbq('track', 'toppageview');]]></SCRIPT>
                                </xsl:otherwise>
                        </xsl:choose>
                </BODY>
        </xsl:template>

        <xsl:template match="area">
                <!--貴賓會員區塊-->
                <xsl:if test="vip">
                        <ARTICLE class="border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
                                <DIV class="d-flex m-2 align-items-center">
                                        <DIV class="text-lg m-2 text-bold text-primary">貴賓會員</DIV>
                                        <xsl:if test="not(vip/@lastPage)">
                                                <BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="vip" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
                                        </xsl:if>
                                </DIV>
                                <DIV class="d-flex flex-wrap justify-content-center mx-2 vip">
                                        <xsl:for-each select="vip/section">
                                                <A class="position-relative m-1" href="/profile/{identifier}/">
                                                        <IMG class="border-radius-md" src="{profileImage}" width="152"/>
                                                        <DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
                                                                <xsl:if test="@vvip">
                                                                        <I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
                                                                </xsl:if>
                                                                <xsl:if test="@relief = 'true'">
                                                                        <I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
                                                                </xsl:if>
                                                        </DIV>
                                                        <DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
                                                                <DIV>
                                                                        <SPAN>
                                                                                <xsl:value-of select="nickname"/>
                                                                        </SPAN>
                                                                        <SPAN class="ms-2">
                                                                                <xsl:value-of select="age"/>
                                                                        </SPAN>
                                                                </DIV>
                                                                <xsl:if test="relationship">
                                                                        <DIV>
                                                                                <SPAN>尋找</SPAN>
                                                                                <SPAN>
                                                                                        <xsl:value-of select="relationship"/>
                                                                                </SPAN>
                                                                        </DIV>
                                                                </xsl:if>
                                                                <DIV>
                                                                        <xsl:for-each select="location">
                                                                                <SPAN class="me-1">
                                                                                        <xsl:if test="position() = last()">
                                                                                                <xsl:attribute name="class"></xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <xsl:value-of select="."/>
                                                                                </SPAN>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                        </DIV>
                                                        <xsl:if test="@following">
                                                                <DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
                                                                        <I class="fas fa-heart-circle text-primary fontSize22"></I>
                                                                </DIV>
                                                        </xsl:if>
                                                </A>
                                        </xsl:for-each>
                                </DIV>
                        </ARTICLE>
                </xsl:if>
                <!--安心認證區塊-->
                <xsl:if test="relief">
                        <ARTICLE class="border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
                                <DIV class="d-flex m-2 align-items-center">
                                        <DIV class="text-lg m-2 text-bold text-primary">安心認證</DIV>
                                        <xsl:if test="not(relief/@lastPage)">
                                                <BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="relief" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
                                        </xsl:if>
                                </DIV>
                                <DIV class="d-flex flex-wrap justify-content-center mx-2 relief">
                                        <xsl:for-each select="relief/section">
                                                <A class="position-relative m-1" href="/profile/{identifier}/">
                                                        <IMG class="border-radius-md" src="{profileImage}" width="152"/>
                                                        <DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
                                                                <xsl:if test="@vvip">
                                                                        <I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
                                                                </xsl:if>
                                                                <xsl:if test="@relief = 'true'">
                                                                        <I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
                                                                </xsl:if>
                                                        </DIV>
                                                        <DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
                                                                <DIV>
                                                                        <SPAN>
                                                                                <xsl:value-of select="nickname"/>
                                                                        </SPAN>
                                                                        <SPAN class="ms-2">
                                                                                <xsl:value-of select="age"/>
                                                                        </SPAN>
                                                                </DIV>
                                                                <xsl:if test="relationship">
                                                                        <DIV>
                                                                                <SPAN>尋找</SPAN>
                                                                                <SPAN>
                                                                                        <xsl:value-of select="relationship"/>
                                                                                </SPAN>
                                                                        </DIV>
                                                                </xsl:if>
                                                                <DIV>
                                                                        <xsl:for-each select="location">
                                                                                <SPAN class="me-1">
                                                                                        <xsl:if test="position() = last()">
                                                                                                <xsl:attribute name="class"></xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <xsl:value-of select="."/>
                                                                                </SPAN>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                        </DIV>
                                                        <xsl:if test="@following">
                                                                <DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
                                                                        <I class="fas fa-heart-circle text-primary fontSize22"></I>
                                                                </DIV>
                                                        </xsl:if>
                                                </A>
                                        </xsl:for-each>
                                </DIV>
                        </ARTICLE>
                </xsl:if>
                <!--最近活躍區塊-->
                <xsl:if test="active">
                        <ARTICLE class="border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
                                <DIV class="d-flex m-2 align-items-center">
                                        <DIV class="text-lg m-2 text-bold text-primary">最近活躍</DIV>
                                        <xsl:if test="not(active/@lastPage)">
                                                <BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="active" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
                                        </xsl:if>
                                </DIV>
                                <DIV class="d-flex flex-wrap justify-content-center mx-2 active">
                                        <xsl:for-each select="active/section">
                                                <A class="position-relative m-1" href="/profile/{identifier}/">
                                                        <IMG class="border-radius-md" src="{profileImage}" width="152"/>
                                                        <DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
                                                                <xsl:if test="@vvip">
                                                                        <I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
                                                                </xsl:if>
                                                                <xsl:if test="@relief = 'true'">
                                                                        <I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
                                                                </xsl:if>
                                                        </DIV>
                                                        <DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
                                                                <DIV>
                                                                        <SPAN>
                                                                                <xsl:value-of select="nickname"/>
                                                                        </SPAN>
                                                                        <SPAN class="ms-2">
                                                                                <xsl:value-of select="age"/>
                                                                        </SPAN>
                                                                </DIV>
                                                                <xsl:if test="relationship">
                                                                        <DIV>
                                                                                <SPAN>尋找</SPAN>
                                                                                <SPAN>
                                                                                        <xsl:value-of select="relationship"/>
                                                                                </SPAN>
                                                                        </DIV>
                                                                </xsl:if>
                                                                <DIV>
                                                                        <xsl:for-each select="location">
                                                                                <SPAN class="me-1">
                                                                                        <xsl:if test="position() = last()">
                                                                                                <xsl:attribute name="class"></xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <xsl:value-of select="."/>
                                                                                </SPAN>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                        </DIV>
                                                        <xsl:if test="@following">
                                                                <DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
                                                                        <I class="fas fa-heart-circle text-primary fontSize22"></I>
                                                                </DIV>
                                                        </xsl:if>
                                                </A>
                                        </xsl:for-each>
                                </DIV>
                        </ARTICLE>
                </xsl:if>
                <!--最新註冊區塊-->
                <xsl:if test="register">
                        <ARTICLE class="border-radius-xl shadow-lg pb-2 mx-auto mb-3 d-none d-md-block">
                                <DIV class="d-flex m-2 align-items-center">
                                        <DIV class="text-lg m-2 text-bold text-primary">最新註冊</DIV>
                                        <xsl:if test="not(register/@lastPage)">
                                                <BUTTON class="ms-auto btn btn-link seeMoreBtn text-lg mx-2 my-0 p-0" data-type="register" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"></I></BUTTON>
                                        </xsl:if>
                                </DIV>
                                <DIV class="d-flex flex-wrap justify-content-center mx-2 register">
                                        <xsl:for-each select="register/section">
                                                <A class="position-relative m-1" href="/profile/{identifier}/">
                                                        <IMG class="border-radius-md" src="{profileImage}" width="152"/>
                                                        <DIV class="position-absolute right-0 text-center" style="width: 32px; top: 5px;">
                                                                <xsl:if test="@vvip">
                                                                        <I class="fad fa-crown fontSize22 text-yellow text-shadow"></I>
                                                                </xsl:if>
                                                                <xsl:if test="@relief = 'true'">
                                                                        <I class="fas fa-shield-check fontSize22 text-success text-shadow"></I>
                                                                </xsl:if>
                                                        </DIV>
                                                        <DIV class="position-absolute bottom-0 right-0 text-bold text-light bg-dark opacity-7 border-radius-md p-1 text-xs text-center">
                                                                <DIV>
                                                                        <SPAN>
                                                                                <xsl:value-of select="nickname"/>
                                                                        </SPAN>
                                                                        <SPAN class="ms-2">
                                                                                <xsl:value-of select="age"/>
                                                                        </SPAN>
                                                                </DIV>
                                                                <xsl:if test="relationship">
                                                                        <DIV>
                                                                                <SPAN>尋找</SPAN>
                                                                                <SPAN>
                                                                                        <xsl:value-of select="relationship"/>
                                                                                </SPAN>
                                                                        </DIV>
                                                                </xsl:if>
                                                                <DIV>
                                                                        <xsl:for-each select="location">
                                                                                <SPAN class="me-1">
                                                                                        <xsl:if test="position() = last()">
                                                                                                <xsl:attribute name="class"></xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <xsl:value-of select="."/>
                                                                                </SPAN>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                        </DIV>
                                                        <xsl:if test="@following">
                                                                <DIV class="position-absolute left-0 text-center" style="width: 32px; top: 5px;">
                                                                        <I class="fas fa-heart-circle text-primary fontSize22"></I>
                                                                </DIV>
                                                        </xsl:if>
                                                </A>
                                        </xsl:for-each>
                                </DIV>
                        </ARTICLE>
                </xsl:if>
                <SECTION class="d-block d-md-none text-center">
                        <DIV class="d-flex flex-wrap justify-content-center mobileMode"></DIV>
                        <BUTTON class="btn btn-link text-lg seeMoreBtn" id="mobileMode" data-page="0">看更多<I class="fad fa-angle-double-right ms-1"/></BUTTON>
                </SECTION>
                <SECTION class="fixed-bottom d-block d-md-none bg-light shadow m-2 bottom13rem text-lg">
                        <UL class="navbar-nav flex-row mobileMode">
                                <xsl:if test="vip">
                                        <LI class="nav-item col-3 text-center">
                                                <A class="nav-link cursor-pointer vipA mobileModeA" data-type="vip">VIP</A>
                                        </LI>
                                </xsl:if>
                                <LI class="nav-item col-4 text-center">
                                        <xsl:if test="vip">
                                                <xsl:attribute name="class">nav-item col-3 text-center</xsl:attribute>
                                        </xsl:if>
                                        <A class="nav-link cursor-pointer reliefA mobileModeA" data-type="relief">安心</A>
                                </LI>
                                <LI class="nav-item col-4 text-center">
                                        <xsl:if test="vip">
                                                <xsl:attribute name="class">nav-item col-3 text-center</xsl:attribute>
                                        </xsl:if>
                                        <A class="nav-link cursor-pointer mobileModeA" data-type="active">活躍</A>
                                </LI>
                                <LI class="nav-item col-4 text-center">
                                        <xsl:if test="vip">
                                                <xsl:attribute name="class">nav-item col-3 text-center</xsl:attribute>
                                        </xsl:if>
                                        <A class="nav-link cursor-pointer mobileModeA" data-type="register">新會員</A>
                                </LI>
                        </UL>
                </SECTION>
        </xsl:template>
</xsl:stylesheet>