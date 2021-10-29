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
                        <LINK href="/STYLE/genHashtags.css" rel="stylesheet"/>
                </HEAD>
                <BODY class="g-sidenav-show bg-gray-100">
                        <xsl:call-template name="bootstrapToast"/>
                        <xsl:call-template name="dashSideNavBar"/>
                        <MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
                                <xsl:call-template name="dashTopNavBar"/>
                                <DIV class="container-fluid py-4 px-3">
                                        <SECTION class="col-12 col-sm-10 col-md-8 col-lg-7 col-xl-6 mx-auto">
                                                <DIV class="text-center text-primary text-bold">新增文章標籤</DIV>
                                                <FORM action="/dashboard/addTrialCode.json" class="row justify-content-center" method="POST">
                                                        <DIV class="col-12 row justify-content-center mt-2">
                                                                <LABEL for="phrase" class="col-1 d-flex align-items-center justify-content-center m-0">
                                                                        <I class="far fa-hashtag text-lg text-primary"></I>
                                                                </LABEL>
                                                                <DIV class="col-10">
                                                                        <INPUT class="form-control" id="phrase" name="phrase" placeholder="標籤名稱" required="" type="text"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="col-11 mt-2">
                                                                <BUTTON class="btn btn-primary btn-round w-100 py-2 addDone" type="button">送出</BUTTON>
                                                        </DIV>
                                                </FORM>
                                        </SECTION>
                                        <SECTION class="mt-3 col-12 col-sm-10 col-md-8 col-lg-7 col-xl-6 mx-auto">
                                                <DIV class="text-center text-primary text-bold mb-3">標籤列表</DIV>
                                                <DIV class="">
                                                        <DIV class="row">
                                                                <DIV class="col-3 text-center text-sm">ID</DIV>
                                                                <DIV class="col-9 text-center text-sm">標籤名稱</DIV>
                                                        </DIV>
                                                        <HR class="my-2"/>
                                                        <xsl:for-each select="hashtag">
                                                                <DIV class="row mb-2">
                                                                        <DIV class="col-3 text-center text-sm d-flex align-items-center justify-content-center">
                                                                                <xsl:value-of select="@id"/>
                                                                        </DIV>
                                                                        <DIV class="col-9 text-center">
                                                                                <INPUT name="hashtagID" type="hidden" value="{@id}"/>
                                                                                <INPUT class="form-control editInput me-2" disabled="" name="editedPhrase" type="text" value="{@phrase}"/>
                                                                                <BUTTON class="btn btn-dark btn-round px-2 py-1 m-0 toggleEdit" type="button">編輯</BUTTON>
                                                                                <BUTTON class="btn btn-primary btn-round px-2 py-1 m-0 d-none editDone" type="button">完成</BUTTON>
                                                                        </DIV>
                                                                </DIV>
                                                        </xsl:for-each>
                                                </DIV>
                                        </SECTION>
                                </DIV>
                        </MAIN>
                        <xsl:call-template name="dashScriptTags"/>
                        <SCRIPT src="/SCRIPT/genHashtags.js"/>
                </BODY>
        </xsl:template>
</xsl:stylesheet>