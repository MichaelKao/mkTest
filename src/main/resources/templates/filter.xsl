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
                        <LINK crossorigin="anonymous" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/11.0.2/css/bootstrap-slider.css" integrity="sha512-SZgE3m1he0aEF3tIxxnz/3mXu/u/wlMNxQSnE0Cni9j/O8Gs+TjM9tm1NX34nRQ7GiLwUEzwuE3Wv2FLz2667w==" rel="stylesheet"/>
                        <LINK href="/STYLE/filter.css" rel="stylesheet"/>
                </HEAD>
                <BODY>
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <DIV class="container pt-7 pt-md-8 px-3">
                                <SECTION class="searchArea shadow open">
                                        <DIV class="row">
                                                <DIV class="col-6 px-2">
                                                        <DIV class="mb-3">
                                                                <LABEL for="nickname">
                                                                        <I class="fad fa-mask text-lg"></I>
                                                                </LABEL>
                                                                <INPUT class="form-control" id="nickname" name="nickname" placeholder="暱稱" type="text" value=""/>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="col-6">
                                                        <DIV class="mb-3">
                                                                <DIV>
                                                                        <I class="fad fa-birthday-cake text-lg me-1"></I>
                                                                        <SPAN>年齡(歲)</SPAN>
                                                                </DIV>
                                                                <DIV id="slider-outer-div">
                                                                        <DIV id="slider-div">
                                                                                <DIV>
                                                                                        <INPUT data-slider-min="18"
                                                                                               data-slider-max="100" data-slider-value="[18,80]"
                                                                                               data-slider-tooltip="hide" id="age" type="text" />
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="row">
                                                <DIV class="col-6">
                                                        <DIV class="mb-3">
                                                                <DIV>
                                                                        <I class="fad fa-ruler-vertical text-lg me-1"></I>
                                                                        <SPAN>身高(公分)</SPAN>
                                                                </DIV>
                                                                <DIV id="slider-outer-div">
                                                                        <DIV id="slider-div">
                                                                                <DIV>
                                                                                        <INPUT data-slider-min="100"
                                                                                               data-slider-max="250" data-slider-value="[150,200]"
                                                                                               data-slider-tooltip="hide" id="height" type="text" />
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="col-6">
                                                        <DIV class="mb-3">
                                                                <DIV>
                                                                        <I class="fad fa-weight text-lg me-1"></I>
                                                                        <SPAN>體重(公斤)</SPAN>
                                                                </DIV>
                                                                <DIV id="slider-outer-div">
                                                                        <DIV id="slider-div">
                                                                                <DIV>
                                                                                        <INPUT data-slider-min="30"
                                                                                               data-slider-max="200" data-slider-value="[40,100]"
                                                                                               data-slider-tooltip="hide" id="weight" type="text" />
                                                                                </DIV>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="row">
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <DIV>
                                                                        <I class="fad fa-street-view text-lg me-1"></I>
                                                                        <SPAN>體型</SPAN>
                                                                </DIV>
                                                                <SELECT class="form-control" id="bodyType" name="bodyType">
                                                                        <OPTION value="">全部</OPTION>
                                                                        <xsl:for-each select="bodyType">
                                                                                <OPTION value="{@bodyTypeEnum}">
                                                                                        <xsl:value-of select="."/>
                                                                                </OPTION>
                                                                        </xsl:for-each>
                                                                </SELECT>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <DIV>
                                                                        <I class="fad fa-graduation-cap text-lg me-1"></I>
                                                                        <SPAN>學歷</SPAN>
                                                                </DIV>
                                                                <SELECT class="form-control" id="education" name="education">
                                                                        <OPTION value="">全部</OPTION>
                                                                        <xsl:for-each select="education">
                                                                                <OPTION value="{@educationEnum}">
                                                                                        <xsl:value-of select="."/>
                                                                                </OPTION>
                                                                        </xsl:for-each>
                                                                </SELECT>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="row">
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <DIV>
                                                                        <I class="far fa-smoking text-lg me-1"></I>
                                                                        <SPAN>抽菸</SPAN>
                                                                </DIV>
                                                                <SELECT class="form-control" id="smoking" name="smoking">
                                                                        <OPTION value="">全部</OPTION>
                                                                        <xsl:for-each select="smoking">
                                                                                <OPTION value="{@smokingEnum}">
                                                                                        <xsl:value-of select="."/>
                                                                                </OPTION>
                                                                        </xsl:for-each>
                                                                </SELECT>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <DIV>
                                                                        <I class="fad fa-wine-glass-alt text-lg me-1"></I>
                                                                        <SPAN>飲酒</SPAN>
                                                                </DIV>
                                                                <SELECT class="form-control" id="drinking" name="drinking">
                                                                        <OPTION value="">全部</OPTION>
                                                                        <xsl:for-each select="drinking">
                                                                                <OPTION value="{@drinkingEnum}">
                                                                                        <xsl:value-of select="."/>
                                                                                </OPTION>
                                                                        </xsl:for-each>
                                                                </SELECT>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="row">
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <xsl:if test="@female">
                                                                        <DIV>
                                                                                <I class="fad fa-usd-circle text-lg me-1"></I>
                                                                                <SPAN>收入</SPAN>
                                                                        </DIV>
                                                                        <SELECT class="form-control" id="annualIncome" name="annualIncome">
                                                                                <OPTION value="0">全部</OPTION>
                                                                                <xsl:for-each select="annualIncome">
                                                                                        <OPTION value="{@annualIncomeID}">
                                                                                                <xsl:value-of select="."/>
                                                                                        </OPTION>
                                                                                </xsl:for-each>
                                                                        </SELECT>
                                                                </xsl:if>
                                                                <xsl:if test="@male">
                                                                        <DIV>
                                                                                <I class="fad fa-usd-circle text-lg me-1"></I>
                                                                                <SPAN>零用</SPAN>
                                                                        </DIV>
                                                                        <SELECT class="form-control" id="allowance" name="allowance">
                                                                                <OPTION value="0">全部</OPTION>
                                                                                <xsl:for-each select="allowance">
                                                                                        <OPTION value="{@allowanceID}">
                                                                                                <xsl:value-of select="."/>
                                                                                        </OPTION>
                                                                                </xsl:for-each>
                                                                        </SELECT>
                                                                </xsl:if>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <DIV>
                                                                        <I class="fad fa-heart text-lg me-1"></I>
                                                                        <SPAN>感情</SPAN>
                                                                </DIV>
                                                                <SELECT class="form-control" id="marriage" name="marriage">
                                                                        <OPTION value="">全部</OPTION>
                                                                        <xsl:for-each select="marriage">
                                                                                <OPTION value="{@marriageEnum}">
                                                                                        <xsl:value-of select="."/>
                                                                                </OPTION>
                                                                        </xsl:for-each>
                                                                </SELECT>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="row">
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <DIV>
                                                                        <I class="fad fa-map-marker-alt text-lg me-1"></I>
                                                                        <SPAN>約會地區</SPAN>
                                                                </DIV>
                                                                <SELECT class="form-control" id="location" name="location">
                                                                        <OPTION value="0">全部</OPTION>
                                                                        <xsl:for-each select="location">
                                                                                <OPTION value="{@locationID}">
                                                                                        <xsl:value-of select="."/>
                                                                                </OPTION>
                                                                        </xsl:for-each>
                                                                </SELECT>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="col-6">
                                                        <DIV class="mb-4">
                                                                <DIV>
                                                                        <I class="fad fa-book-heart text-lg me-1"></I>
                                                                        <SPAN>約會模式</SPAN>
                                                                </DIV>
                                                                <SELECT class="form-control" id="service" name="service">
                                                                        <OPTION value="0">全部</OPTION>
                                                                        <xsl:for-each select="service">
                                                                                <OPTION value="{@serviceID}">
                                                                                        <xsl:value-of select="."/>
                                                                                </OPTION>
                                                                        </xsl:for-each>
                                                                </SELECT>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="text-center">
                                                <BUTTON class="btn btn-primary btn-round w-50 filterBtn" type="button">
                                                        <I class="fad fa-search me-2"></I>
                                                        <SPAN>馬上搜尋</SPAN>
                                                </BUTTON>
                                        </DIV>
                                </SECTION>
                                <DIV class="text-lg text-primary text-bold d-flex justify-content-center mt-1">
                                        <DIV class="toggleSearchArea cursor-pointer open">
                                                <I class="fad fa-chevron-double-up me-1 openCloseBtn"></I>
                                                <SPAN>隱藏搜尋區塊</SPAN>
                                                <I class="fad fa-search ms-1"></I>
                                        </DIV>
                                </DIV>
                        </DIV>
                        <DIV class="container px-0 mt-4">
                                <H4 class="text-center">搜尋結果</H4>
                                <DIV class="d-flex flex-wrap justify-content-center mx-0 filterResult"></DIV>
                                <DIV class="d-flex mt-3 justify-content-center">
                                        <DIV id="pageBtnWrap"></DIV>
                                </DIV>
                        </DIV>
                        <xsl:call-template name="footer"/>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT crossorigin="anonymous" integrity="sha512-f0VlzJbcEB6KiW8ZVtL+5HWPDyW1+nJEjguZ5IVnSQkvZbwBt2RfCBY0CBO1PsMAqxxrG4Di6TfsCPP3ZRwKpA==" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/11.0.2/bootstrap-slider.min.js"/>
                        <SCRIPT src="/SCRIPT/filter.js"/>
                        <xsl:if test="@signIn">
                                <SCRIPT src="/SCRIPT/websocket.js"/>
                        </xsl:if>
                </BODY>
        </xsl:template>
</xsl:stylesheet>