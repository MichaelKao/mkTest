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
                        <LINK href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" rel="stylesheet"/>
                        <LINK href="/STYLE/datepicker.css" rel="stylesheet"/>
                        <LINK href="/STYLE/broadcast.css" rel="stylesheet"/>
                </HEAD>
                <BODY class="g-sidenav-show bg-gray-100">
                        <xsl:call-template name="bootstrapToast"/>
                        <xsl:call-template name="dashSideNavBar"/>
                        <MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
                                <xsl:call-template name="dashTopNavBar"/>
                                <DIV class="container-fluid py-4 px-3">
                                        <SECTION class="col-12 col-sm-10 col-md-8 col-lg-7 col-xl-6 mx-auto">
                                                <DIV class="text-center text-primary text-bold">小編群發訊息</DIV>
                                                <FORM action="/dashboard/broadcast.asp" class="row justify-content-center" method="POST">
                                                        <INPUT name="date" type="hidden" value=""/>
                                                        <DIV class="d-flex justify-content-around mt-4">
                                                                <DIV class="form-check">
                                                                        <INPUT class="form-check-input" id="queen" name="gender" type="radio" value="false"/>
                                                                        <LABEL class="custom-control-label" for="queen">
                                                                                <SAPN>meQUEEN</SAPN>
                                                                                <I class="fal fa-venus text-lg ms-2"></I>
                                                                        </LABEL>
                                                                </DIV>
                                                                <DIV class="form-check">
                                                                        <INPUT class="form-check-input" id="king" name="gender" type="radio" value="true"/>
                                                                        <LABEL class="custom-control-label" for="king">
                                                                                <SAPN>meKING</SAPN>
                                                                                <I class="fal fa-mars text-lg ms-2"></I>
                                                                        </LABEL>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="d-flex justify-content-around mt-4">
                                                                <DIV class="form-check">
                                                                        <INPUT class="form-check-input" id="specificDate" type="checkbox"/>
                                                                        <LABEL class="custom-control-label" for="specificDate">
                                                                                <SPAN>指定註冊日期後的用戶</SPAN>
                                                                        </LABEL>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="form-group">
                                                                <DIV class="input-group date" id="registeredDate">
                                                                        <SPAN class="input-group-addon input-group-text">
                                                                                <SPAN class="fal fa-calendar"></SPAN>
                                                                        </SPAN>
                                                                        <INPUT class="form-control" disabled="true" inputmode="none" name="registeredDate" type="text"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="form-group">
                                                                <DIV class="input-group">
                                                                        <SPAN class="input-group-addon input-group-text">
                                                                                <SPAN class="fal fa-clock"></SPAN>
                                                                        </SPAN>
                                                                        <INPUT class="form-control" disabled="true" id="timePicker" inputmode="none" type="text"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="mt-3">
                                                                <TEXTAREA class="form-control" name="content" placeholder="輸入群發內容...." rows="8" style="resize: none;"></TEXTAREA>
                                                        </DIV>
                                                        <DIV class="mt-2">
                                                                <BUTTON class="btn btn-primary btn-round w-100 py-2" type="submit">送出</BUTTON>
                                                        </DIV>
                                                </FORM>
                                        </SECTION>
                                </DIV>
                        </MAIN>
                        <xsl:call-template name="dashScriptTags"/>
                        <SCRIPT src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"/>
                        <SCRIPT src="/SCRIPT/broadcast.js"/>
                </BODY>
        </xsl:template>
</xsl:stylesheet>