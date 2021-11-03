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
                        <LINK href="/STYLE/groupMsg.css" rel="stylesheet"/>
                </HEAD>
                <BODY class="g-sidenav-show bg-gray-100">
                        <xsl:call-template name="bootstrapToast"/>
                        <xsl:call-template name="dashSideNavBar"/>
                        <MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
                                <xsl:call-template name="dashTopNavBar"/>
                                <DIV class="container-fluid py-4 px-3">
                                        <section class="col-12 col-sm-10 col-md-8 col-lg-7 col-xl-6 mx-auto">
                                                <div class="text-center text-primary text-bold">新增文章標籤</div>
                                                <form action="/dashboard/" class="row justify-content-center" method="POST">
                                                        <div class="d-flex justify-content-around mt-2">
                                                                <div class="form-check">
                                                                        <input class="form-check-input" type="radio" name="flexRadioDefault" id="customRadio1"/>
                                                                        <label class="custom-control-label" for="customRadio1">meQUEEN<i class="fal fa-venus text-lg ms-2"></i></label>
                                                                </div>
                                                                <div class="form-check">
                                                                        <input class="form-check-input" type="radio" name="flexRadioDefault" id="customRadio2"/>
                                                                        <label class="custom-control-label" for="customRadio2">meKING<i class="fal fa-mars text-lg ms-2">
                                                                                </i>
                                                                        </label>
                                                                </div>
                                                        </div>
                                                        <div class="md-form">
                                                                <input placeholder="Selected time" type="text" id="input_starttime" class="form-control timepicker"/>
                                                                <label for="input_starttime">Light version, 12hours</label>
                                                        </div>
                                                        <div>
                                                                <textarea class="form-control" name="markdown" rows="8" placeholder="輸入群發內容...." style="resize: none;"></textarea>
                                                        </div>
                                                        <div class="mt-2">
                                                                <button class="btn btn-primary btn-round w-100 py-2 addDone" type="button">送出</button>
                                                        </div>
                                                </form>
                                        </section>
                                </DIV>
                        </MAIN>
                        <xsl:call-template name="dashScriptTags"/>
                        <SCRIPT src="/SCRIPT/groupMsg.js"/>
                </BODY>
        </xsl:template>
</xsl:stylesheet>