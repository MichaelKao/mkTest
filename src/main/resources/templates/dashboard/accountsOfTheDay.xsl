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
                        <LINK href="/STYLE/accountsOfTheDay.css" rel="stylesheet"/>
                </HEAD>
                <BODY class="g-sidenav-show bg-gray-100">
                        <xsl:call-template name="dashSideNavBar"/>
                        <MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg">
                                <xsl:call-template name="dashTopNavBar"/>
                                <DIV class="container-fluid py-4 px-2">
                                        <DIV class="my-4 col-9 col-md-5 mx-auto">
                                                <DIV class="form-group">
                                                        <DIV class="input-group date" id="registeredDate">
                                                                <SPAN class="input-group-addon input-group-text">
                                                                        <SPAN class="fal fa-calendar"></SPAN>
                                                                </SPAN>
                                                                <INPUT class="form-control" name="registeredDate" type="text"/>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="my-4 col-9 col-md-5 text-center mx-auto text-bold text-dark">
                                                <SPAN class="registered">
                                                        <xsl:value-of select="registered"/>
                                                </SPAN>
                                                <SPAN class="ms-1">新進會員</SPAN>
                                        </DIV>
                                        <DIV class="col-11 col-md-7 mx-auto">
                                                <DIV class="row text-center mt-3 mb-1 text-xs">
                                                        <DIV class="col-4 text-dark d-flex justify-content-start">暱稱/帳號</DIV>
                                                        <DIV class="col-4 text-dark">性別</DIV>
                                                        <DIV class="col-4 text-dark">優質會員</DIV>
                                                </DIV>
                                                <DIV class="text-xs maleMembers">
                                                        <xsl:for-each select="accounts/account">
                                                                <DIV class="row text-center align-items-center py-2">
                                                                        <xsl:if test="position() mod 2 = 1">
                                                                                <xsl:attribute name="class">row text-center align-items-center bg-light border-radius-xl py-2</xsl:attribute>
                                                                        </xsl:if>
                                                                        <DIV class="col-4 d-flex justify-content-start">
                                                                                <A class="d-flex flex-column align-items-start" href="/profile/{identifier}/">
                                                                                        <SPAN class="text-primary">
                                                                                                <xsl:value-of select="nickname"/>
                                                                                        </SPAN>
                                                                                        <DIV class="text-secondary">
                                                                                                <xsl:value-of select="login"/>
                                                                                        </DIV>
                                                                                </A>
                                                                        </DIV>
                                                                        <DIV class="col-4">
                                                                                <SPAN>
                                                                                        <xsl:value-of select="gender"/>
                                                                                </SPAN>
                                                                        </DIV>
                                                                        <DIV class="col-4">
                                                                                <xsl:if test="fake = 'true'">
                                                                                        <I class="fal fa-times fontSize22"></I>
                                                                                </xsl:if>
                                                                                <xsl:if test="fake = 'false'">
                                                                                        <I class="fal fa-check fontSize22 text-primary"></I>
                                                                                </xsl:if>
                                                                        </DIV>
                                                                </DIV>
                                                        </xsl:for-each>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </MAIN>
                        <xsl:call-template name="dashScriptTags"/>
                        <SCRIPT src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"/>
                        <SCRIPT src="/SCRIPT/accountsOfTheDay.js"/>
                </BODY>
        </xsl:template>
</xsl:stylesheet>