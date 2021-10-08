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
                </HEAD>
                <BODY>
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <DIV class="page-header section-height-100">
                                <DIV class="container">
                                        <xsl:apply-templates select="form"/>
                                </DIV>
                        </DIV>
                        <xsl:call-template name="footer"/>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT src="/SCRIPT/activated.js"/>
                </BODY>
        </xsl:template>

        <xsl:template match="form">
                <DIV class="row">
                        <DIV class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-auto">
                                <DIV class="card card-plain">
                                        <DIV class="p-1 p-sm-2 text-left">
                                                <H4 class="font-weight-bolder">
                                                        <xsl:value-of select="/document/@title"/>
                                                </H4>
                                                <P class="mb-0">設定您的密碼</P>
                                        </DIV>
                                        <DIV class="card-body p-1 p-sm-2">
                                                <FORM action="/activated.asp" method="post">
                                                        <INPUT class="d-none" name="identifier" type="hidden" value="{identifier}"/>
                                                        <DIV class="row align-items-center mb-3">
                                                                <DIV class="col-1 d-flex justify-content-start">
                                                                        <I class="fas fa-key text-lg"></I>
                                                                </DIV>
                                                                <DIV class="col-11">
                                                                        <INPUT class="form-control" id="shadow" name="shadow" placeholder="{shadow/@i18n}" required="" type="password"/>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="text-center">
                                                                <BUTTON class="btn btn-lg bg-gradient-primary btn-lg w-100 mt-4 mb-0" type="submit">
                                                                        <xsl:value-of select="@i18n-submit"/>
                                                                </BUTTON>
                                                        </DIV>
                                                </FORM>
                                        </DIV>
                                </DIV>
                        </DIV>
                </DIV>
        </xsl:template>
</xsl:stylesheet>