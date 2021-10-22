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
                        <LINK href="/manifest.json" rel="manifest"/>
                        <xsl:call-template name="headLinkTags"/>
                        <LINK href="/STYLE/signIn.css" rel="stylesheet"/>
                </HEAD>
                <BODY>
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <DIV class="page-header section-height-100">
                                <DIV class="container">
                                        <xsl:apply-templates select="form"/>
                                </DIV>
                        </DIV>
                        <DIV class="iosAddToDesktop addToDeskTop position-fixed border-radius-xl py-3 px-2" style="display: none;">
                                <DIV class="text-xs text-center d-flex flex-column justify-content-center align-items-center mx-auto">
                                        <DIV class="text-dark text-bold">簡單步驟實現 app 體驗</DIV>
                                        <DIV class="d-flex align-items-center mt-3 text-dark">
                                                <DIV>
                                                        <DIV>
                                                                <IMG alt="iosShare" src="/IMAGE/iosShare.png" width="35"/>
                                                        </DIV>
                                                        <DIV class="pt-2">點擊</DIV>
                                                </DIV>
                                                <I class="fas fa-chevron-right px-3"></I>
                                                <DIV>
                                                        <DIV class="text-center">
                                                                <I class="fal fa-plus-square fontSize35 text-dark"></I>
                                                        </DIV>
                                                        <DIV class="pt-2">加入主畫面</DIV>
                                                </DIV>
                                                <I class="fas fa-chevron-right px-3"></I>
                                                <DIV>
                                                        <DIV>
                                                                <IMG alt="icon" class="border-radius-md" src="/ICON/logo.png" width="38"/>
                                                        </DIV>
                                                        <DIV class="pt-2">新增 Young Me</DIV>
                                                </DIV>
                                        </DIV>
                                        <BUTTON class="addDeskColse btn btn-link text-dark m-0 p-0" type="button">
                                                <I class="fal fa-times fontSize22"></I>
                                        </BUTTON>
                                </DIV>
                        </DIV>
                        <DIV class="androidAddToDesktop addToDeskTop position-fixed border-radius-xl py-3 px-2" style="display: none;">
                                <DIV class="text-xs text-center d-flex flex-column justify-content-center align-items-center mx-auto">
                                        <DIV class="text-dark text-bold">簡單步驟實現 app 體驗</DIV>
                                        <DIV class="d-flex align-items-center mt-3 text-dark">
                                                <DIV>
                                                        <DIV>
                                                                <I class="far fa-ellipsis-v fontSize35 text-dark"></I>
                                                        </DIV>
                                                        <DIV class="pt-2">點擊右上方</DIV>
                                                </DIV>
                                                <I class="fas fa-chevron-right px-3"></I>
                                                <DIV>
                                                        <DIV>
                                                                <IMG alt="icon" src="/IMAGE/chromeAdd.png" width="38"/>
                                                        </DIV>
                                                        <DIV class="pt-2">加到主畫面</DIV>
                                                </DIV>
                                                <I class="fas fa-chevron-right px-3"></I>
                                                <DIV>
                                                        <DIV>
                                                                <IMG alt="icon" class="border-radius-md" src="/ICON/logo.png" width="38"/>
                                                        </DIV>
                                                        <DIV class="pt-2">新增 Young Me</DIV>
                                                </DIV>
                                        </DIV>
                                        <BUTTON class="addDeskColse btn btn-link text-dark m-0 p-0" type="button">
                                                <I class="fal fa-times fontSize22"></I>
                                        </BUTTON>
                                </DIV>
                        </DIV>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT src="/SCRIPT/signIn.js"/>
                </BODY>
        </xsl:template>

        <xsl:template match="form">
                <DIV class="row">
                        <DIV class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-auto px-0">
                                <DIV class="card card-plain">
                                        <DIV class="p-1 p-sm-2 text-left">
                                                <H4 class="font-weight-bolder">
                                                        <xsl:value-of select="/document/@title"/>
                                                </H4>
                                        </DIV>
                                        <DIV class="card-body p-1 p-sm-2">
                                                <FORM action="/signIn.asp" method="post">
                                                        <INPUT class="d-none" id="username" name="username" type="hidden"/>
                                                        <DIV class="row align-items-center mb-2">
                                                                <DIV class="col-1 d-flex justify-content-start">
                                                                        <I class="fad fa-globe-americas text-lg"></I>
                                                                </DIV>
                                                                <DIV class="col-11">
                                                                        <SELECT class="form-control form-control-lg" id="country" required="">
                                                                                <xsl:apply-templates select="country/*"/>
                                                                        </SELECT>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="row align-items-center mb-3">
                                                                <DIV class="col-1 d-flex justify-content-start">
                                                                        <I class="fad fa-phone-square-alt text-lg"></I>
                                                                </DIV>
                                                                <DIV class="col-11">
                                                                        <INPUT class="form-control" id="cellularPhoneNumber" inputmode="numeric" placeholder="手機號碼" required="" type="text"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="row align-items-center">
                                                                <DIV class="col-1">
                                                                        <I class="fas fa-key text-lg"></I>
                                                                </DIV>
                                                                <DIV class="col-11">
                                                                        <INPUT class="form-control" name="password" placeholder="密碼" required="" type="password"/>
                                                                </DIV>
                                                        </DIV>
                                                        <P class="text-center text-sm mb-0 mt-1">
                                                                <A class="text-primary text-gradient font-weight-bold" href="/resetPassword/">忘記密碼?</A>
                                                        </P>
                                                        <DIV class="text-center">
                                                                <BUTTON class="btn btn-lg bg-gradient-primary btn-lg w-100 mt-4 mb-0" type="submit">
                                                                        <xsl:value-of select="@i18n-submit"/>
                                                                </BUTTON>
                                                        </DIV>
                                                </FORM>
                                        </DIV>
                                        <DIV class="card-footer text-center pt-0 px-lg-2 px-1">
                                                <P class="text-sm mx-auto">
                                                        <SPAN class="me-1">還沒有帳號嗎?</SPAN>
                                                        <A href="/signUp.asp" class="text-primary text-gradient font-weight-bold">免費註冊</A>
                                                </P>
                                        </DIV>
                                </DIV>
                        </DIV>
                </DIV>
        </xsl:template>
</xsl:stylesheet>