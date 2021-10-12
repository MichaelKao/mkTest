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
                        <LINK href="/STYLE/chatroom.css" rel="stylesheet"/>
                        <LINK href="/STYLE/rateStar.css" rel="stylesheet"/>
                </HEAD>
                <BODY>
                        <xsl:call-template name="bootstrapToast"/>
                        <INPUT name="identifier" type="hidden" value="{@identifier}"/>
                        <INPUT name="friendIdentifier" type="hidden" value="{@friendIdentifier}"/>
                        <INPUT type="hidden" name="gender" value="{@gender}"/>
                        <DIV class="modal fade" id="rateModal">
                                <DIV class="modal-dialog modal-dialog-centered">
                                        <DIV class="modal-content">
                                                <DIV class="modal-body">
                                                        <DIV class="d-flex">
                                                                <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                        <I class="fal fa-times"></I>
                                                                </BUTTON>
                                                        </DIV>
                                                        <DIV class="mt-3 text-center">
                                                                <I class="far fa-comment-alt-smile text-success mb-1" style="font-size: 50px;"></I>
                                                                <H5 class="modal-title">給予對方評價</H5>
                                                        </DIV>
                                                        <DIV class="form-group mx-auto col-10">
                                                                <DIV class="rating d-flex flex-row-reverse justify-content-center">
                                                                        <INPUT class="d-none" id="rating-5" name="rating" type="radio" value="5"/>
                                                                        <LABEL for="rating-5"></LABEL>
                                                                        <INPUT class="d-none" id="rating-4" name="rating" type="radio" value="4"/>
                                                                        <LABEL for="rating-4"></LABEL>
                                                                        <INPUT class="d-none" id="rating-3" name="rating" type="radio" value="3"/>
                                                                        <LABEL for="rating-3"></LABEL>
                                                                        <INPUT class="d-none" id="rating-2" name="rating" type="radio" value="2"/>
                                                                        <LABEL for="rating-2"></LABEL>
                                                                        <INPUT class="d-none" id="rating-1" name="rating" type="radio" value="1"/>
                                                                        <LABEL for="rating-1"></LABEL>
                                                                </DIV>
                                                                <TEXTAREA class="form-control" name="comment" placeholder="留下評價..." type="text"></TEXTAREA>
                                                        </DIV>
                                                        <DIV class="text-center">
                                                                <BUTTON class="btn btn-outline-primary commentBtn mx-1 px-3 py-2" type="button">確認</BUTTON>
                                                                <BUTTON class="btn btn-outline-dark mx-1 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </DIV>
                        <DIV class="modal fade" id="fareModal">
                                <DIV class="modal-dialog modal-dialog-centered">
                                        <DIV class="modal-content">
                                                <DIV class="modal-body">
                                                        <DIV class="d-flex">
                                                                <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                        <I class="fal fa-times"></I>
                                                                </BUTTON>
                                                        </DIV>
                                                        <DIV class="mt-3 text-center">
                                                                <I class="fad fa-taxi text-success mb-1" style="font-size: 50px;"></I>
                                                                <H5 class="modal-title">
                                                                        <xsl:if test="@male">車馬費</xsl:if>
                                                                        <xsl:if test="@female">要求車馬費</xsl:if>
                                                                </H5>
                                                        </DIV>
                                                        <DIV class="form-group mx-auto col-10 text-center">
                                                                <xsl:if test="@male">
                                                                        <LABEL class="text-xs" for="fare">使用平台支付不必擔心私下給甜心爽約，可檢舉查證屬實退回</LABEL>
                                                                </xsl:if>
                                                                <INPUT class="form-control" id="fare" inputmode="numeric" min="1" name="howMany" required="" type="number"/>
                                                        </DIV>
                                                        <DIV class="text-center">
                                                                <BUTTON class="btn btn-outline-primary confirmFare mx-1 px-3 py-2" type="button">確認</BUTTON>
                                                                <BUTTON class="btn btn-outline-dark mx-1 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </DIV>
                        <xsl:if test="@male">
                                <DIV class="modal fade" id="weChatModel">
                                        <DIV class="modal-dialog modal-dialog-centered">
                                                <DIV class="modal-content">
                                                        <DIV class="modal-body">
                                                                <DIV class="d-flex">
                                                                        <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                                <I class="fal fa-times"></I>
                                                                        </BUTTON>
                                                                </DIV>
                                                                <DIV class="my-3 text-center">
                                                                        <I class="fad fa-user-plus mb-1" style="font-size: 50px;"></I>
                                                                        <H5 class="modal-title">微信 WeCaht QRcode</H5>
                                                                </DIV>
                                                                <DIV class="form-group">
                                                                        <DIV class="text-center">使用微信 APP 掃描加入好友</DIV>
                                                                        <DIV class="text-center">若是用手機，需以截圖或下載 QRCode 的方式使用微信 APP 加入好友</DIV>
                                                                        <DIV class="text-center text-primary">點擊 QRcode 可直接下載</DIV>
                                                                        <A class="weChatQRcode d-flex justify-content-center" href="" download="weChatQRcode.png">
                                                                                <IMG alt="weChatQRCode" class="weChatQRcode" src=""/>
                                                                        </A>
                                                                </DIV>
                                                                <DIV class="text-center">
                                                                        <BUTTON class="btn btn-outline-dark px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </xsl:if>
                        <DIV class="modal fade" id="blockModal">
                                <DIV class="modal-dialog modal-dialog-centered">
                                        <DIV class="modal-content">
                                                <DIV class="modal-body">
                                                        <DIV class="d-flex">
                                                                <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                        <I class="fal fa-times"></I>
                                                                </BUTTON>
                                                        </DIV>
                                                        <DIV class="mt-3">
                                                                <I class="fas fa-exclamation-circle text-info mb-1" style="font-size: 50px;"></I>
                                                                <P class="text-bold">1. 對方不會知道您封鎖他</P>
                                                                <P class="text-bold">2. 對方瀏覽您的網頁，會出現您的帳戶已關閉</P>
                                                        </DIV>
                                                        <DIV>
                                                                <BUTTON type="button" class="btn btn-outline-primary px-3 py-2 block">確認</BUTTON>
                                                                <BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </DIV>
                        <DIV class="fixed-top container chatNavbar">
                                <NAV class="navbar navbar-expand-lg top-0 position-absolute p-0 start-0 end-0">
                                        <DIV class="fixed-top container mx-auto bg-light">
                                                <A class="navbar-brand font-weight-bolder m-0" href="/">YOUNG ME 養蜜</A>
                                                <DIV class="d-flex align-items-center">
                                                        <xsl:if test="@signIn">
                                                                <A class="d-lg-none pe-1" href="/activeLogs.asp">
                                                                        <I class="fad fa-bell fontSize22"></I>
                                                                        <SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 announcement" style="display: none;">
                                                                                <xsl:if test="@announcement">
                                                                                        <xsl:attribute name="style">display: inline;</xsl:attribute>
                                                                                        <xsl:value-of select="@announcement"/>
                                                                                </xsl:if>
                                                                        </SPAN>
                                                                </A>
                                                                <A class="d-lg-none px-2" href="/inbox.asp">
                                                                        <I class="fad fa-comment-smile fontSize22"></I>
                                                                        <SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 inbox" style="display: none;">
                                                                                <xsl:if test="@inbox">
                                                                                        <xsl:attribute name="style">display: inline;</xsl:attribute>
                                                                                        <xsl:value-of select="@inbox"/>
                                                                                </xsl:if>
                                                                        </SPAN>
                                                                </A>
                                                        </xsl:if>
                                                        <BUTTON class="navbar-toggler shadow-none px-1" type="button" data-bs-toggle="collapse" data-bs-target="#navigation">
                                                                <SPAN class="navbar-toggler-icon mt-2">
                                                                        <SPAN class="navbar-toggler-bar bar1"></SPAN>
                                                                        <SPAN class="navbar-toggler-bar bar2"></SPAN>
                                                                        <SPAN class="navbar-toggler-bar bar3"></SPAN>
                                                                </SPAN>
                                                        </BUTTON>
                                                </DIV>
                                                <DIV class="navbar-collapse justify-content-end bg-light collapse" id="navigation">
                                                        <UL class="navbar-nav navbar-nav-hover">
                                                                <xsl:if test="@signIn">
                                                                        <LI class="nav-item dropdown dropdown-hover">
                                                                                <A class="nav-link cursor-pointer text-primary" id="dropdownMenuPages" data-bs-toggle="dropdown">
                                                                                        <SPAN class="me-1">
                                                                                                <I class="fad fa-user-cog fontSize22"></I>
                                                                                        </SPAN>
                                                                                        <I class="fas fa-chevron-down"></I>
                                                                                </A>
                                                                                <DIV class="dropdown-menu dropdown-menu-animation p-3 border-radius-lg mt-0 mt-lg-3">
                                                                                        <DIV class="d-none d-lg-block">
                                                                                                <A class="dropdown-item border-radius-md row" href="/profile/">
                                                                                                        <I class="fad fa-user-edit fontSize22 col-1"></I>
                                                                                                        <SPAN class="ms-1">個人檔案</SPAN>
                                                                                                </A>
                                                                                                <A class="dropdown-item border-radius-md row" href="/referralCode.asp">
                                                                                                        <I class="fad fa-users-crown fontSize22 col-1"></I>
                                                                                                        <SPAN class="ms-1">好友邀請碼</SPAN>
                                                                                                </A>
                                                                                                <xsl:if test="@female">
                                                                                                        <A class="dropdown-item border-radius-md row" href="/groupGreeting.asp">
                                                                                                                <I class="fad fa-comments-alt fontSize22 col-1"></I>
                                                                                                                <SPAN class="ms-1">群發打招呼</SPAN>
                                                                                                        </A>
                                                                                                </xsl:if>
                                                                                                <A class="dropdown-item border-radius-md row" href="/setting.asp">
                                                                                                        <I class="fad fa-cog fontSize22 col-1"></I>
                                                                                                        <SPAN class="ms-1">進階設定</SPAN>
                                                                                                </A>
                                                                                        </DIV>
                                                                                        <DIV class="d-lg-none">
                                                                                                <A class="dropdown-item border-radius-md row" href="/profile/">
                                                                                                        <I class="fad fa-user-edit fontSize22 col-1"></I>
                                                                                                        <SPAN class="ms-1">個人檔案</SPAN>
                                                                                                </A>
                                                                                                <A class="dropdown-item border-radius-md row" href="/referralCode.asp">
                                                                                                        <I class="fad fa-users-crown fontSize22 col-1"></I>
                                                                                                        <SPAN class="ms-1">好友邀請碼</SPAN>
                                                                                                </A>
                                                                                                <xsl:if test="@female">
                                                                                                        <A class="dropdown-item border-radius-md row" href="/groupGreeting.asp">
                                                                                                                <I class="fad fa-comments-alt fontSize22 col-1"></I>
                                                                                                                <SPAN class="ms-1">群發打招呼</SPAN>
                                                                                                        </A>
                                                                                                </xsl:if>
                                                                                                <A class="dropdown-item border-radius-md row" href="/setting.asp">
                                                                                                        <I class="fad fa-cog fontSize22 col-1"></I>
                                                                                                        <SPAN class="ms-1">進階設定</SPAN>
                                                                                                </A>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </LI>
                                                                        <xsl:if test="@almighty or @finance">
                                                                                <LI class="nav-item">
                                                                                        <A class="nav-link nav-link-icon d-flex align-items-center text-primary" href="/dashboard/certification.asp">
                                                                                                <I class="fad fa-id-card-alt fontSize22 width30whenMobile" aria-hidden="true"></I>
                                                                                                <SPAN class="d-lg-none">YoungMe 後台</SPAN>
                                                                                        </A>
                                                                                </LI>
                                                                        </xsl:if>
                                                                        <xsl:if test="@signIn">
                                                                                <LI class="nav-item d-none d-lg-block">
                                                                                        <A class="nav-link nav-link-icon" href="/activeLogs.asp">
                                                                                                <I class="fad fa-bell fontSize22"></I>
                                                                                                <SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 announcement" style="display: none;">
                                                                                                        <xsl:if test="@announcement">
                                                                                                                <xsl:attribute name="style">display: inline;</xsl:attribute>
                                                                                                                <xsl:value-of select="@announcement"/>
                                                                                                        </xsl:if>
                                                                                                </SPAN>
                                                                                        </A>
                                                                                </LI>
                                                                                <LI class="nav-item d-none d-lg-block">
                                                                                        <A class="nav-link nav-link-icon" href="/inbox.asp">
                                                                                                <I class="fad fa-comment-smile fontSize22"></I>
                                                                                                <SPAN class="text-xs text-light bg-warning border-radius-md ms-n2 inbox" style="display: none;">
                                                                                                        <xsl:if test="@inbox">
                                                                                                                <xsl:attribute name="style">display: inline;</xsl:attribute>
                                                                                                                <xsl:value-of select="@inbox"/>
                                                                                                        </xsl:if>
                                                                                                </SPAN>
                                                                                        </A>
                                                                                </LI>
                                                                        </xsl:if>
                                                                        <LI class="nav-item">
                                                                                <A class="nav-link nav-link-icon d-flex align-items-center" href="/">
                                                                                        <I class="fad fa-home-heart fontSize22 me-1"></I>
                                                                                        <SPAN class="d-lg-none">
                                                                                                <xsl:if test="@male">所有甜心</xsl:if>
                                                                                                <xsl:if test="@female">所有男仕</xsl:if>
                                                                                        </SPAN>
                                                                                </A>
                                                                        </LI>
                                                                        <LI class="nav-item">
                                                                                <A class="nav-link nav-link-icon d-flex align-items-center" href="/favorite.asp">
                                                                                        <I class="fad fa-box-heart fontSize22 me-1"></I>
                                                                                        <SPAN class="d-lg-none">我的收藏</SPAN>
                                                                                </A>
                                                                        </LI>
                                                                        <LI class="nav-item">
                                                                                <A class="nav-link nav-link-icon d-flex align-items-center" href="/looksMe.asp">
                                                                                        <I class="fad fa-shoe-prints fontSize22 me-1"></I>
                                                                                        <SPAN class="d-lg-none">誰看過我</SPAN>
                                                                                </A>
                                                                        </LI>
                                                                        <xsl:if test="@female">
                                                                                <LI class="nav-item">
                                                                                        <A class="nav-link nav-link-icon d-flex align-items-center" href="/withdrawal.asp">
                                                                                                <I class="fad fa-badge-dollar fontSize22 me-1"></I>
                                                                                                <SPAN class="d-lg-none">提領車馬費</SPAN>
                                                                                        </A>
                                                                                </LI>
                                                                        </xsl:if>
                                                                        <xsl:if test="@male">
                                                                                <LI class="nav-item">
                                                                                        <A class="nav-link nav-link-icon d-flex align-items-center" href="/recharge.asp">
                                                                                                <I class="fad fa-badge-dollar fontSize22 me-1"></I>
                                                                                                <SPAN class="d-lg-none">購買ME點</SPAN>
                                                                                        </A>
                                                                                </LI>
                                                                                <LI class="nav-item">
                                                                                        <A class="nav-link nav-link-icon d-flex align-items-center" href="/upgrade.asp">
                                                                                                <I class="fad fa-crown fontSize22 me-1"></I>
                                                                                                <SPAN class="d-lg-none">升級 VIP</SPAN>
                                                                                        </A>
                                                                                </LI>
                                                                        </xsl:if>
                                                                </xsl:if>
                                                                <LI class="nav-item">
                                                                        <xsl:choose>
                                                                                <xsl:when test="@signIn">
                                                                                        <A class="nav-link nav-link-icon d-flex align-items-center" href="/signOut.asp">
                                                                                                <I class="fad fa-sign-out fontSize22 me-1"></I>
                                                                                                <SPAN class="d-lg-none">登出</SPAN>
                                                                                        </A>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                        <A class="nav-link nav-link-icon d-flex align-items-center" href="/signIn.asp">
                                                                                                <I class="fad fa-sign-in fontSize22 me-1"></I>
                                                                                                <SPAN class="d-lg-none">登入</SPAN>
                                                                                        </A>
                                                                                </xsl:otherwise>
                                                                        </xsl:choose>
                                                                </LI>
                                                        </UL>
                                                </DIV>
                                        </DIV>
                                </NAV>
                        </DIV>
                        <DIV class="d-flex chatContainer container px-0">
                                <DIV class="d-none d-lg-block col-lg-3 chatList">
                                        <DIV class="list shadow">
                                                <DIV class="tabs">
                                                        <UL class="nav nav-tabs flex-row">
                                                                <LI class="nav-item col-6 text-center">
                                                                        <A class="nav-link cursor-pointer" data-bs-toggle="tab" href="#first">
                                                                                <xsl:if test="@male">
                                                                                        <I class="fad fa-users"></I>
                                                                                        <SPAN class="ms-1">好友</SPAN>
                                                                                </xsl:if>
                                                                                <xsl:if test="@female">
                                                                                        <I class="fad fa-crown"></I>
                                                                                        <SPAN class="ms-1">VIP</SPAN>
                                                                                </xsl:if>
                                                                                <xsl:if test="@matchedOrVipNotSeenCount">
                                                                                        <SPAN class="text-xs border-radius-md px-1 ms-1 firstNotSeen notSeen">
                                                                                                <xsl:value-of select="@matchedOrVipNotSeenCount"/>
                                                                                        </SPAN>
                                                                                </xsl:if>
                                                                        </A>
                                                                </LI>
                                                                <LI class="nav-item col-6 text-center">
                                                                        <A class="nav-link cursor-pointer" data-bs-toggle="tab" href="#second">
                                                                                <xsl:if test="@male">
                                                                                        <I class="fad fa-users-slash"></I>
                                                                                        <SPAN class="ms-1">非好友</SPAN>
                                                                                </xsl:if>
                                                                                <xsl:if test="@female">
                                                                                        <SPAN class="ms-1">一般</SPAN>
                                                                                </xsl:if>
                                                                                <xsl:if test="@notMatchedOrNotVipNotSeenCount">
                                                                                        <SPAN class="text-xs border-radius-md px-1 ms-1 secondNotSeen notSeen">
                                                                                                <xsl:value-of select="@notMatchedOrNotVipNotSeenCount"/>
                                                                                        </SPAN>
                                                                                </xsl:if>
                                                                        </A>
                                                                </LI>
                                                        </UL>
                                                </DIV>
                                                <DIV class="tab-content">
                                                        <DIV class="row justify-content-center mx-0 tab-pane" id="first">
                                                                <xsl:for-each select="conversation">
                                                                        <xsl:if test="@isMatchedOrIsVip = 'true'">
                                                                                <DIV class="card my-2 px-2 mx-auto conversationWrap position-relative shadow">
                                                                                        <xsl:if test="/document/@friendIdentifier = @identifier">
                                                                                                <xsl:attribute name="class">card my-2 px-2 mx-auto conversationWrap position-relative shadow active</xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <A class="inboxLink" href="/chatroom/{@identifier}/"></A>
                                                                                        <DIV class="d-flex justify-content-between align-items-center py-2">
                                                                                                <DIV>
                                                                                                        <IMG alt="大頭貼" class="rounded-circle" src="{@profileImage}" width="50px"/>
                                                                                                </DIV>
                                                                                                <DIV class="me-auto" style="overflow: hidden;">
                                                                                                        <DIV class="d-flex flex-column align-items-start ms-2">
                                                                                                                <A class=" font-weight-bold text-dark text-sm mb-1">
                                                                                                                        <xsl:value-of select="@nickname"/>
                                                                                                                </A>
                                                                                                                <P class="text-sm mb-0 content">
                                                                                                                        <xsl:if test="/document/@friendIdentifier = @identifier">
                                                                                                                                <xsl:attribute name="class">text-sm mb-0 content currentContent</xsl:attribute>
                                                                                                                        </xsl:if>
                                                                                                                        <xsl:value-of select="@content"/>
                                                                                                                </P>
                                                                                                        </DIV>
                                                                                                </DIV>
                                                                                                <DIV class="col-2 d-flex">
                                                                                                        <DIV class="ms-auto d-flex flex-column">
                                                                                                                <SPAN class="text-xs mb-1">
                                                                                                                        <xsl:value-of select="@occurredTime"/>
                                                                                                                </SPAN>
                                                                                                                <xsl:if test="@notSeenCount">
                                                                                                                        <DIV class="d-flex justify-content-center">
                                                                                                                                <SPAN class="text-xs text-light bg-primary border-radius-md px-1">
                                                                                                                                        <xsl:value-of select="@notSeenCount"/>
                                                                                                                                </SPAN>
                                                                                                                        </DIV>
                                                                                                                </xsl:if>
                                                                                                        </DIV>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </xsl:if>
                                                                </xsl:for-each>
                                                        </DIV>
                                                        <DIV class="row justify-content-center mx-0 tab-pane" id="second">
                                                                <xsl:for-each select="conversation">
                                                                        <xsl:if test="@isMatchedOrIsVip = 'false'">
                                                                                <DIV class="card my-2 px-2 mx-auto conversationWrap position-relative shadow">
                                                                                        <xsl:if test="/document/@friendIdentifier = @identifier">
                                                                                                <xsl:attribute name="class">card my-2 px-2 mx-auto conversationWrap position-relative shadow active</xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <A class="inboxLink" href="/chatroom/{@identifier}/"></A>
                                                                                        <DIV class="d-flex justify-content-between align-items-center py-2">
                                                                                                <DIV>
                                                                                                        <IMG alt="大頭貼" class="rounded-circle" src="{@profileImage}" width="50px"/>
                                                                                                </DIV>
                                                                                                <DIV class="me-auto" style="overflow: hidden;">
                                                                                                        <DIV class="d-flex flex-column align-items-start ms-2">
                                                                                                                <A class=" font-weight-bold text-dark text-sm mb-1">
                                                                                                                        <xsl:value-of select="@nickname"/>
                                                                                                                </A>
                                                                                                                <P class="text-sm mb-0 content">
                                                                                                                        <xsl:if test="/document/@friendIdentifier = @identifier">
                                                                                                                                <xsl:attribute name="class">text-sm mb-0 content currentContent</xsl:attribute>
                                                                                                                        </xsl:if>
                                                                                                                        <xsl:value-of select="@content"/>
                                                                                                                </P>
                                                                                                        </DIV>
                                                                                                </DIV>
                                                                                                <DIV class="col-2 d-flex">
                                                                                                        <DIV class="ms-auto d-flex flex-column">
                                                                                                                <SPAN class="text-xs mb-1">
                                                                                                                        <xsl:value-of select="@occurredTime"/>
                                                                                                                </SPAN>
                                                                                                                <xsl:if test="@notSeenCount">
                                                                                                                        <DIV class="d-flex justify-content-center">
                                                                                                                                <SPAN class="text-xs text-light bg-primary border-radius-md px-1">
                                                                                                                                        <xsl:value-of select="@notSeenCount"/>
                                                                                                                                </SPAN>
                                                                                                                        </DIV>
                                                                                                                </xsl:if>
                                                                                                        </DIV>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </xsl:if>
                                                                </xsl:for-each>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="hideSideBar d-lg-none position-absolute top-0 bottom-0 bg-dark opacity-7 text-white fontSize35 text-center cursor-pointer">
                                                        <I class="fal fa-angle-left"></I>
                                                </DIV>
                                        </DIV>
                                </DIV>
                                <DIV class="showSideBar d-lg-none position-absolute top-0 bottom-0 left-0 bg-dark opacity-7 text-white fontSize35 text-center cursor-pointer">
                                        <I class="fal fa-angle-right"></I>
                                </DIV>
                                <DIV class="col-12 col-lg-9">
                                        <DIV class="list">
                                                <DIV class="d-flex align-items-center bg-dark py-1 px-2 chatHeader">
                                                        <DIV>
                                                                <A href="/profile/{@friendIdentifier}/">
                                                                        <IMG alt="profileImage" class="rounded-circle" src="{@friendProfileImage}" width="35"/>
                                                                        <SPAN class="ms-2 text-white">
                                                                                <xsl:value-of select="@friendNickname"/>
                                                                        </SPAN>
                                                                </A>
                                                        </DIV>
                                                        <xsl:if test="not(@blocking) and not(@blockedBy)">
                                                                <DIV class="ms-auto">
                                                                        <BUTTON class="btn btn-link m-0 p-2" data-bs-target="#fareModal" data-bs-toggle="modal" type="button">
                                                                                <xsl:if test="@male">
                                                                                        <xsl:attribute name="id">fare</xsl:attribute>
                                                                                </xsl:if>
                                                                                <xsl:if test="@female">
                                                                                        <xsl:attribute name="id">reqFare</xsl:attribute>
                                                                                </xsl:if>
                                                                                <I class="fas fa-badge-dollar fontSize25 text-white"></I>
                                                                        </BUTTON>
                                                                        <A class="btn btn-link m-0 p-2" data-bs-toggle="dropdown">
                                                                                <I class="fas fa-comment-alt-times mb-0 fontSize25 text-white"></I>
                                                                        </A>
                                                                        <DIV class="dropdown-menu shadow">
                                                                                <!--										<BUTTON class="dropdown-item">
                                                                                        <I class="fad fa-lightbulb-exclamation fontSize22 col-2 text-center"></I>
                                                                                        <SPAN class="ms-2">檢舉對方</SPAN>
                                                                                </BUTTON>-->
                                                                                <BUTTON class="dropdown-item" data-bs-target="#blockModal" data-bs-toggle="modal">
                                                                                        <I class="fad fa-user-slash fontSize22 col-2"></I>
                                                                                        <SPAN class="ms-2">封鎖對方</SPAN>
                                                                                </BUTTON>
                                                                        </DIV>
                                                                </DIV>
                                                        </xsl:if>
                                                </DIV>
                                                <DIV class="messages">
                                                        <xsl:if test="@male">
                                                                <DIV class="border-radius-lg mx-auto position-absolute left-0 right-0 p-2 maleAlert">
                                                                        <DIV class="d-flex">
                                                                                <BUTTON class="btn btn-link text-white ms-auto fontSize22 m-0 p-0 me-2 maleAlertClose" data-bs-dismiss="modal" type="button">
                                                                                        <I class="fal fa-times"></I>
                                                                                </BUTTON>
                                                                        </DIV>
                                                                        <DIV class="text-white text-center text-sm">
                                                                                <DIV>非好友12小時內僅能傳送三句話</DIV>
                                                                                <DIV>想辦法用甜言蜜語打動甜心吧！</DIV>
                                                                        </DIV>
                                                                </DIV>
                                                        </xsl:if>
                                                        <DIV id="messagesArea"></DIV>
                                                </DIV>
                                                <DIV class="inputContainer">
                                                        <xsl:choose>
                                                                <xsl:when test="not(@blocking) and not(@blockedBy)">
                                                                        <xsl:if test="@female">
                                                                                <xsl:if test="@decideBtn">
                                                                                        <DIV class="d-flex justify-content-center femaleBtn floatBtn">
                                                                                                <DIV class="border border-primary border-radius-xl text-xs px-3 py-1 bg-light shadow wordBreak text-center floatWrap">
                                                                                                        <DIV class="text-primary">
                                                                                                                <DIV>
                                                                                                                        <I class="fad fa-user-plus"></I>
                                                                                                                        <SPAN>確認後對方將取得您的通訊軟體</SPAN>
                                                                                                                </DIV>
                                                                                                                <DIV>拒絕後對方12小時後可再次提出邀請</DIV>
                                                                                                        </DIV>
                                                                                                        <BUTTON class="btn btn-outline-primary btn-round px-2 py-1 m-0 me-1 accept" type="button">接受</BUTTON>
                                                                                                        <BUTTON class="btn btn-outline-dark btn-round px-2 py-1 m-0 border-radius-xl refuse" type="button">拒絕</BUTTON>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                </xsl:if>
                                                                                <xsl:if test="@rateBtn">
                                                                                        <DIV class="d-flex justify-content-center femaleBtn floatBtn">
                                                                                                <DIV class="border border-primary border-radius-xl text-xs px-3 py-1 bg-light shadow wordBreak text-center floatWrap">
                                                                                                        <DIV class="text-primary">
                                                                                                                <DIV>
                                                                                                                        <I class="fad fa-star-half"></I>
                                                                                                                        <SPAN>給予對方評價</SPAN>
                                                                                                                </DIV>
                                                                                                        </DIV>
                                                                                                        <BUTTON class="btn btn-sm btn-primary btn-round px-2 py-1 m-0 rate" data-bs-target="#rateModal" data-bs-toggle="modal" type="button">評價</BUTTON>
                                                                                                </DIV>
                                                                                        </DIV>
                                                                                </xsl:if>
                                                                        </xsl:if>
                                                                        <xsl:if test="@male">
                                                                                <xsl:choose>
                                                                                        <xsl:when test="@reqSocialMediaBtn">
                                                                                                <DIV class="d-flex justify-content-center maleBtn floatBtn">
                                                                                                        <DIV class="border border-primary border-radius-xl text-xs px-3 py-1 bg-light shadow wordBreak text-center floatWrap">
                                                                                                                <DIV class="text-primary">
                                                                                                                        <I class="far fa-user-plus ms-1"></I>
                                                                                                                        <SPAN>向對方提出通訊軟體要求</SPAN>
                                                                                                                </DIV>
                                                                                                                <BUTTON type="button" id="giveMeLine" class="btn btn-primary btn-round px-2 py-1 m-0">要求</BUTTON>
                                                                                                        </DIV>
                                                                                                </DIV>
                                                                                        </xsl:when>
                                                                                        <xsl:when test="@waitingForRes">
                                                                                                <DIV class="d-flex justify-content-center maleBtn floatBtn">
                                                                                                        <DIV class="border border-primary border-radius-xl text-xs px-3 py-1 bg-light shadow wordBreak text-center floatWrap">
                                                                                                                <DIV class="text-primary">
                                                                                                                        <DIV>
                                                                                                                                <I class="fad fa-user-plus"></I>
                                                                                                                                <SPAN>您已送出邀請加入通訊軟體邀請，</SPAN>
                                                                                                                        </DIV>
                                                                                                                        <DIV>請等待對方回應。</DIV>
                                                                                                                </DIV>
                                                                                                        </DIV>
                                                                                                </DIV>
                                                                                        </xsl:when>
                                                                                        <xsl:when test="@addLineBtn">
                                                                                                <DIV class="d-flex justify-content-center maleBtn floatBtn">
                                                                                                        <DIV class="border border-primary border-radius-xl text-xs px-3 py-1 bg-light shadow wordBreak text-center floatWrap">
                                                                                                                <DIV class="text-primary">
                                                                                                                        <DIV>
                                                                                                                                <I class="fad fa-star-half"></I>
                                                                                                                                <SPAN>加入對方通訊軟體</SPAN>
                                                                                                                        </DIV>
                                                                                                                </DIV>
                                                                                                                <BUTTON class="btn btn-sm btn-primary btn-round px-2 py-1 m-0 openSocialMedia" type="button">
                                                                                                                        <SPAN>加入好友</SPAN>
                                                                                                                        <xsl:if test="@remindDeduct">
                                                                                                                                <DIV class="text-xxs">需 100 愛心</DIV>
                                                                                                                        </xsl:if>
                                                                                                                </BUTTON>
                                                                                                                <xsl:if test="@rateBtn">
                                                                                                                        <BUTTON class="btn btn-sm btn-dark btn-round px-2 py-1 m-0 ms-1 rate" data-bs-target="#rateModal" data-bs-toggle="modal" type="button">評價</BUTTON>
                                                                                                                </xsl:if>
                                                                                                        </DIV>
                                                                                                </DIV>
                                                                                        </xsl:when>
                                                                                </xsl:choose>
                                                                        </xsl:if>
                                                                        <xsl:if test="not(@notAbleToSendMsgs)">
                                                                                <DIV class="textareaContainer">
                                                                                        <TEXTAREA id="chatInput" placeholder="說點什麼吧..." type="text"></TEXTAREA>
                                                                                </DIV>
                                                                                <BUTTON class="btn btn-link m-0 p-0 fontSize25 sendMsgBtn" disabled="">
                                                                                        <I class="fa fa-paper-plane"></I>
                                                                                </BUTTON>
                                                                        </xsl:if>
                                                                        <xsl:if test="@notAbleToSendMsgs">
                                                                                <SPAN>12小時內僅能發送3句話給甜心</SPAN>
                                                                        </xsl:if>
                                                                </xsl:when>
                                                                <xsl:when test="@blockedBy">
                                                                        <SPAN>此用戶已不存在</SPAN>
                                                                </xsl:when>
                                                                <xsl:when test="@blocking">
                                                                        <SPAN>您已封鎖對方</SPAN>
                                                                </xsl:when>
                                                        </xsl:choose>
                                                        <DIV class="customerBtnWrap">
                                                                <A class="customerBtn d-flex align-items-center justify-content-center bg-dark fontSize22 opacity-8 text-white" href="https://line.me/R/ti/p/%40017zadfy">
                                                                        <I class="fad fa-user-headset"></I>
                                                                </A>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </DIV>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT src="/SCRIPT/chatroom.js"/>
                        <xsl:if test="@signIn">
                                <SCRIPT src="/SCRIPT/websocketChat.js"/>
                                <SCRIPT src="/SCRIPT/websocketInbox.js"/>
                        </xsl:if>
                </BODY>
        </xsl:template>
</xsl:stylesheet>