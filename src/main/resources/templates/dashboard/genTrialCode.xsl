<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
        <xsl:output
                encoding="UTF-8"
                media-type="text/html"
                method="html"
                indent="no"
                omit-xml-declaration="yes"
        />

        <xsl:include href="../default.xsl"/>

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
                        <xsl:call-template name="dashHeadLinkTags"/>
                        <LINK href="/STYLE/genTrialCode.css" rel="stylesheet"/>
                </HEAD>
                <BODY class="g-sidenav-show bg-gray-100">
                        <xsl:call-template name="bootstrapToast"/>
                        <xsl:call-template name="dashSideNavBar"/>
                        <DIV class="modal fade" id="listModal">
                                <DIV class="modal-dialog modal-dialog-centered">
                                        <DIV class="modal-content">
                                                <DIV class="modal-body p-2">
                                                        <DIV class="d-flex">
                                                                <BUTTON class="btn btn-link ms-auto fontSize22 me-1 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                        <I class="fal fa-times"></I>
                                                                </BUTTON>
                                                        </DIV>
                                                        <DIV class="my-2 text-center">
                                                                <DIV class="users">
                                                                        <DIV class="d-flex align-items-center w-70 mx-auto">
                                                                                <A class="text-sm" href="">
                                                                                        <I class="far fa-user me-1"></I>
                                                                                        <SPAN>Jack</SPAN>
                                                                                </A>
                                                                                <SPAN class="ms-auto text-xs text-dark">2021-09-29 16:18</SPAN>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="text-center">
                                                                <BUTTON class="btn btn-outline-dark btn-round mx-1 px-3 py-2 m-1" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </DIV>
                        <MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
                                <xsl:call-template name="dashTopNavBar"/>
                                <DIV class="container-fluid py-4 px-3">
                                        <SECTION class="col-12 col-sm-10 col-md-8 col-lg-7 col-xl-6 mx-auto">
                                                <DIV class="text-center text-primary text-bold">產生體驗碼</DIV>
                                                <FORM action="/dashboard/addTrialCode.json" class="row justify-content-center" method="POST">
                                                        <DIV class="col-12 row justify-content-center mt-2">
                                                                <LABEL for="keyOpinionLeader" class="col-1 d-flex align-items-center justify-content-center m-0">
                                                                        <I class="fab fa-youtube text-lg text-primary" aria-hidden="true"></I>
                                                                </LABEL>
                                                                <DIV class="col-10">
                                                                        <INPUT class="form-control" id="keyOpinionLeader" name="keyOpinionLeader" placeholder="網紅名稱" required="" type="text"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="col-12 row justify-content-center mt-2">
                                                                <LABEL for="code" class="col-1 d-flex align-items-center justify-content-center m-0">
                                                                        <I class="fas fa-meteor text-lg text-primary" aria-hidden="true"></I>
                                                                </LABEL>
                                                                <DIV class="col-10">
                                                                        <INPUT class="form-control" id="code" name="code" placeholder="體驗碼" required="" type="text"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="col-11 mt-2">
                                                                <BUTTON class="btn btn-primary btn-round w-100 py-2 addDone" type="button">送出</BUTTON>
                                                        </DIV>
                                                </FORM>
                                        </SECTION>
                                        <SECTION class="mt-3 col-12 col-sm-10 col-md-8 col-lg-7 col-xl-6 mx-auto">
                                                <DIV class="text-center text-primary text-bold mb-3">體驗碼名單</DIV>
                                                <DIV class="">
                                                        <DIV class="row">
                                                                <DIV class="col-3 text-center text-xs">網紅</DIV>
                                                                <DIV class="col-7 text-center text-xs">體驗碼</DIV>
                                                                <DIV class="col-2 text-center text-xs ps-0">使用數</DIV>
                                                        </DIV>
                                                        <HR class="my-2"/>
                                                        <xsl:for-each select="trial">
                                                                <DIV class="trial row mb-2">
                                                                        <DIV class="col-3 text-center text-sm d-flex align-items-center justify-content-center">
                                                                                <xsl:value-of select="@keyOpinionLeader"/>
                                                                        </DIV>
                                                                        <DIV class="col-7 text-center">
                                                                                <INPUT name="trialCodeID" type="hidden" value="{@trialCodeID}"/>
                                                                                <INPUT class="form-control editInput" disabled="" name="editedCode" type="text" value="{@code}"/>
                                                                                <BUTTON class="btn btn-dark btn-round px-2 py-1 m-0 toggleEdit" type="button">
                                                                                        <I class="fal fa-pen"></I>
                                                                                </BUTTON>
                                                                                <BUTTON class="btn btn-primary btn-round px-2 py-1 m-0 d-none editDone" type="button">
                                                                                        <I class="fal fa-check"></I>
                                                                                </BUTTON>
                                                                        </DIV>
                                                                        <DIV class="col-2 text-center d-flex align-items-center justify-content-center ps-0 text-bold">
                                                                                <BUTTON class="btn btn-link m-0 p-0 text-lg text-secondary trialCodeList" data-bs-target="#listModal" data-bs-toggle="modal">
                                                                                        <xsl:value-of select="@count"/>
                                                                                </BUTTON>
                                                                        </DIV>
                                                                </DIV>
                                                        </xsl:for-each>
                                                </DIV>
                                        </SECTION>
                                </DIV>
                        </MAIN>
                        <xsl:call-template name="dashScriptTags"/>
                        <SCRIPT src="/SCRIPT/genTrialCode.js"/>
                </BODY>
        </xsl:template>
</xsl:stylesheet>