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
                        <LINK href="/STYLE/members.css" rel="stylesheet"/>
                        <STYLE>.nav.nav-pills {background: #EDEDED;}</STYLE>
                </HEAD>
                <BODY class="g-sidenav-show bg-gray-100">
                        <xsl:call-template name="dashSideNavBar"/>
                        <MAIN class="main-content position-relative max-height-vh-100 h-100 mt-1 border-radius-lg ">
                                <xsl:call-template name="dashTopNavBar"/>
                                <DIV class="container-fluid py-4 px-2">
                                        <DIV class="modal fade" id="referralCode" style="display: none;">
                                                <DIV class="modal-dialog modal-dialog-centered">
                                                        <DIV class="modal-content shadow">
                                                                <DIV class="modal-body p-2">
                                                                        <DIV class="d-flex">
                                                                                <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                                        <I class="fal fa-times"></I>
                                                                                </BUTTON>
                                                                        </DIV>
                                                                        <DIV class="my-4 text-center">
                                                                                <DIV class="text-dark text-bold myReferralCode">會員的邀請碼</DIV>
                                                                                <DIV class="text-dark text-bold invitedCode">使用的邀請碼</DIV>
                                                                                <HR/>
                                                                                <DIV class="text-dark text-bold">下線名單</DIV>
                                                                                <DIV class="position-relative">
                                                                                        <DIV class="descendants"></DIV>
                                                                                        <DIV class="referralPage text-center text-sm text-primary cursor-pointer"></DIV>
                                                                                </DIV>
                                                                        </DIV>
                                                                        <DIV class="text-center">
                                                                                <BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="modal fade" id="privilege" style="display: none;">
                                                <DIV class="modal-dialog modal-dialog-centered">
                                                        <DIV class="modal-content shadow">
                                                                <DIV class="modal-body p-2">
                                                                        <DIV class="d-flex">
                                                                                <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                                        <I class="fal fa-times"></I>
                                                                                </BUTTON>
                                                                        </DIV>
                                                                        <DIV class="my-4 text-center">
                                                                                <DIV class="text-dark text-bold myReferralCode">會員權限</DIV>
                                                                                <HR class="w-80 mx-auto"/>
                                                                                <xsl:for-each select="role">
                                                                                        <DIV class="form-check col-3 mx-auto">
                                                                                                <INPUT class="form-check-input" id="role{@roleID}" name="role" type="checkbox" value="{@roleID}"/>
                                                                                                <LABEL class="custom-control-label" for="role{@roleID}">
                                                                                                        <xsl:value-of select="."/>
                                                                                                </LABEL>
                                                                                        </DIV>
                                                                                </xsl:for-each>
                                                                        </DIV>
                                                                        <DIV class="text-center">
                                                                                <BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                        <DIV class="nav-wrapper position-relative end-0 col-md-8 mx-auto">
                                                <UL class="nav nav-pills nav-fill">
                                                        <LI class="nav-item">
                                                                <A class="nav-link mb-0 px-0 py-1 active" data-bs-toggle="tab" href="#mainOne">
                                                                        <SPAN class="text-primary text-bold">所有男士</SPAN>
                                                                </A>
                                                        </LI>
                                                        <LI class="nav-item">
                                                                <A class="nav-link mb-0 px-0 py-1" data-bs-toggle="tab" href="#mainTwo">
                                                                        <SPAN class="text-primary text-bold">所有甜心</SPAN>
                                                                </A>
                                                        </LI>
                                                </UL>
                                        </DIV>
                                        <DIV class="tab-content">
                                                <DIV class="tab-pane mt-3 active" id="mainOne">
                                                        <DIV class="my-4 col-9 col-md-5 mx-auto">
                                                                <DIV class="input-group">
                                                                        <INPUT class="form-control" name="searchValue" placeholder="暱稱或帳號" type="text"/>
                                                                        <INPUT name="searchGender" type="hidden" value="true"/>
                                                                        <BUTTON class="input-group-text text-body searchBtn" type="button">
                                                                                <I class="fas fa-search"></I>
                                                                        </BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="pagination malePagination" data-page="{male/@maleTotalPage}">
                                                                <UL></UL>
                                                        </DIV>
                                                        <DIV class="col-11 mx-auto">
                                                                <DIV class="row text-center mt-3 mb-1 text-xs">
                                                                        <DIV class="col-3 text-dark d-flex justify-content-start">暱稱/帳號</DIV>
                                                                        <DIV class="col-3 text-dark">註冊日期</DIV>
                                                                        <DIV class="col-3 text-dark">VIP</DIV>
                                                                        <DIV class="col-2 text-dark p-0">邀請碼</DIV>
                                                                        <DIV class="col-1 text-dark p-0">權限</DIV>
                                                                </DIV>
                                                                <DIV class="text-xs maleMembers">
                                                                        <xsl:for-each select="male/user">
                                                                                <DIV class="row text-center align-items-center py-2">
                                                                                        <xsl:if test="position() mod 2 = 1">
                                                                                                <xsl:attribute name="class">row text-center align-items-center bg-light border-radius-xl py-2</xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <DIV class="col-3 d-flex justify-content-start">
                                                                                                <A class="d-flex flex-column align-items-start" href="/profile/{identifier}/">
                                                                                                        <SPAN class="text-primary">
                                                                                                                <xsl:value-of select="nickname"/>
                                                                                                        </SPAN>
                                                                                                        <DIV class="text-secondary">
                                                                                                                <xsl:value-of select="login"/>
                                                                                                        </DIV>
                                                                                                </A>
                                                                                        </DIV>
                                                                                        <DIV class="col-3">
                                                                                                <SPAN>
                                                                                                        <xsl:value-of select="registered"/>
                                                                                                </SPAN>
                                                                                        </DIV>
                                                                                        <DIV class="col-3">
                                                                                                <xsl:choose>
                                                                                                        <xsl:when test="vvip">
                                                                                                                <SPAN>
                                                                                                                        <I class="fad fa-crown me-1"></I>
                                                                                                                        <SAPN>1288</SAPN>
                                                                                                                </SPAN>
                                                                                                                <DIV>
                                                                                                                        <xsl:value-of select="vvip"/>
                                                                                                                </DIV>
                                                                                                        </xsl:when>
                                                                                                        <xsl:when test="vip">
                                                                                                                <SPAN>
                                                                                                                        <I class="fad fa-crown me-1"></I>
                                                                                                                        <SAPN>1688</SAPN>
                                                                                                                </SPAN>
                                                                                                                <DIV>
                                                                                                                        <xsl:value-of select="vip"/>
                                                                                                                </DIV>
                                                                                                        </xsl:when>
                                                                                                        <xsl:when test="trial">
                                                                                                                <SPAN>
                                                                                                                        <I class="fad fa-crown me-1"></I>
                                                                                                                        <SAPN>單日</SAPN>
                                                                                                                </SPAN>
                                                                                                                <DIV>
                                                                                                                        <xsl:value-of select="trial"/>
                                                                                                                </DIV>
                                                                                                        </xsl:when>
                                                                                                </xsl:choose>
                                                                                        </DIV>
                                                                                        <DIV class="col-2 p-0">
                                                                                                <BUTTON class="btn btn-link m-0 px-0 py-1 referralCodeBtn" data-bs-target="#referralCode" data-bs-toggle="modal" data-id="{id}" type="button">
                                                                                                        <I class="fal fa-users-crown fontSize22"></I>
                                                                                                </BUTTON>
                                                                                        </DIV>
                                                                                        <DIV class="col-1 p-0">
                                                                                                <BUTTON class="btn btn-link m-0 px-0 py-1 privilegeBtn" data-bs-target="#privilege" data-bs-toggle="modal" data-id="{id}" type="button">
                                                                                                        <I class="fal fa-user-shield fontSize22"></I>
                                                                                                </BUTTON>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                                <DIV class="tab-pane mt-3" id="mainTwo">
                                                        <DIV class="my-4 col-9 col-md-5 mx-auto">
                                                                <DIV class="input-group">
                                                                        <INPUT class="form-control" name="searchValue" placeholder="暱稱或帳號" type="text"/>
                                                                        <INPUT name="searchGender" type="hidden" value="false"/>
                                                                        <BUTTON class="input-group-text text-body searchBtn" type="button">
                                                                                <I class="fas fa-search"></I>
                                                                        </BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="pagination femalePagination" data-page="{female/@femaleTotalPage}">
                                                                <UL></UL>
                                                        </DIV>
                                                        <DIV class="col-11 mx-auto">
                                                                <DIV class="row text-center mt-3 mb-1 text-xs">
                                                                        <DIV class="col-3 text-dark d-flex justify-content-start">暱稱/帳號</DIV>
                                                                        <DIV class="col-3 text-dark">註冊日期</DIV>
                                                                        <DIV class="col-3 text-dark">邀請碼</DIV>
                                                                        <DIV class="col-3 text-dark">權限</DIV>
                                                                </DIV>
                                                                <DIV class="text-xs femaleMembers">
                                                                        <xsl:for-each select="female/user">
                                                                                <DIV class="row text-center align-items-center py-2">
                                                                                        <xsl:if test="position() mod 2 = 1">
                                                                                                <xsl:attribute name="class">row text-center align-items-center bg-light border-radius-xl py-2</xsl:attribute>
                                                                                        </xsl:if>
                                                                                        <DIV class="col-3 d-flex justify-content-start">
                                                                                                <A class="d-flex flex-column align-items-start" href="/profile/{identifier}/">
                                                                                                        <SPAN class="text-primary">
                                                                                                                <xsl:value-of select="nickname"/>
                                                                                                        </SPAN>
                                                                                                        <DIV class="text-secondary">
                                                                                                                <xsl:value-of select="login"/>
                                                                                                        </DIV>
                                                                                                </A>
                                                                                        </DIV>
                                                                                        <DIV class="col-3">
                                                                                                <SPAN>
                                                                                                        <xsl:value-of select="registered"/>
                                                                                                </SPAN>
                                                                                        </DIV>
                                                                                        <DIV class="col-3">
                                                                                                <BUTTON class="btn btn-link m-0 px-2 py-1 referralCodeBtn" data-bs-target="#referralCode" data-bs-toggle="modal" data-id="{id}" type="button">
                                                                                                        <I class="fal fa-users-crown fontSize22"></I>
                                                                                                </BUTTON>
                                                                                        </DIV>
                                                                                        <DIV class="col-3">
                                                                                                <BUTTON class="btn btn-link m-0 px-0 py-1 privilegeBtn" data-bs-target="#privilege" data-bs-toggle="modal" data-id="{id}" type="button">
                                                                                                        <I class="fal fa-user-shield fontSize22"></I>
                                                                                                </BUTTON>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                        </MAIN>
                        <xsl:call-template name="dashScriptTags"/>
                        <SCRIPT src="/SCRIPT/members.js"/>
                </BODY>
        </xsl:template>
</xsl:stylesheet>