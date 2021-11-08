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
                        <STYLE>BODY{background: #F3F3F3 !important;} .resultIcon{font-size: 60px;}</STYLE>
                </HEAD>
                <BODY>
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <DIV class="container py-8 px-3">
                                <DIV class="modal fade" id="deleteModal">
                                        <DIV class="modal-dialog modal-dialog-centered">
                                                <DIV class="modal-content">
                                                        <DIV class="modal-body">
                                                                <DIV class="d-flex">
                                                                        <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                                <I class="fal fa-times"></I>
                                                                        </BUTTON>
                                                                </DIV>
                                                                <DIV class="my-4 text-center">
                                                                        <I class="fad fa-exclamation-circle text-warning mb-1 fontSize50"></I>
                                                                        <P class="text-warning text-bold">刪除帳號將無法再恢復，確定要刪除？</P>
                                                                </DIV>
                                                                <DIV class="text-center">
                                                                        <BUTTON class="btn btn-outline-primary mx-2 px-3 py-2 confirmBtn" type="button">確認</BUTTON>
                                                                        <BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                                <DIV class="modal fade" id="pwdModal">
                                        <DIV class="modal-dialog modal-dialog-centered">
                                                <DIV class="modal-content">
                                                        <DIV class="modal-body">
                                                                <DIV class="d-flex">
                                                                        <BUTTON class="btn btn-link ms-auto fontSize22 m-0 p-0" data-bs-dismiss="modal" type="button">
                                                                                <I class="fal fa-times"></I>
                                                                        </BUTTON>
                                                                </DIV>
                                                                <DIV class="row align-items-center my-3">
                                                                        <DIV class="col-1">
                                                                                <I class="fas fa-key text-lg"></I>
                                                                        </DIV>
                                                                        <DIV class="col-11">
                                                                                <INPUT class="form-control" name="password" placeholder="輸入新密碼" required="" type="password"/>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="d-flex">
                                                                        <DIV class="ms-auto">
                                                                                <BUTTON class="btn btn-outline-primary mx-2 resetPwdBtn px-3 py-2" type="button">確認</BUTTON>
                                                                                <BUTTON class="btn btn-outline-dark mx-2 px-3 py-2" data-bs-dismiss="modal" type="button">取消</BUTTON>
                                                                        </DIV>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                                <DIV class="text-center">
                                        <H4 class="text-primary">
                                                <xsl:value-of select="@title"/>
                                        </H4>
                                </DIV>
                                <DIV class="col-lg-8 mx-auto d-flex justify-content-center align-items-center">
                                        <DIV class="col-12 col-md-8 card card-frame">
                                                <DIV class="card-body p-3">
                                                        <DIV class="d-flex justify-content-center align-items-center">
                                                                <DIV>
                                                                        <DIV class="text-bold">
                                                                                <I class="fal fa-bell-plus fontSize22"></I>
                                                                                <SPAN class="ms-2">LINE Notify 通知</SPAN>
                                                                        </DIV>
                                                                        <DIV class="text-xs text-lighter">綁定LINE NOTIFY</DIV>
                                                                        <DIV class="text-xs text-lighter">重要訊息不漏接</DIV>
                                                                </DIV>
                                                                <DIV class="ms-auto">
                                                                        <xsl:choose>
                                                                                <xsl:when test="not(@lineNotify)">
                                                                                        <A class="btn btn-sm btn-round btn-primary m-0 px-2 py-1" href="/notify-bot.line.me/authorize.asp">綁定</A>
                                                                                </xsl:when>
                                                                                <xsl:otherwise>
                                                                                        <A class="btn btn-sm btn-round btn-primary m-0 px-2 py-1" href="/notify-bot.line.me/revoke.asp">取消綁定</A>
                                                                                </xsl:otherwise>
                                                                        </xsl:choose>
                                                                </DIV>
                                                        </DIV>
                                                        <HR class="horizontal dark"/>
                                                        <DIV class="d-flex justify-content-center align-items-center my-2">
                                                                <DIV class="text-bold">
                                                                        <I class="fal fa-key fontSize22"></I>
                                                                        <SPAN class="ms-2">重設密碼</SPAN>
                                                                </DIV>
                                                                <DIV class="ms-auto">
                                                                        <BUTTON class="btn btn-sm btn-round btn-primary m-0 px-2 py-1" data-bs-target="#pwdModal" data-bs-toggle="modal" type="button">重設密碼</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                        <HR class="horizontal dark"/>
                                                        <DIV class="d-flex justify-content-center align-items-center my-2">
                                                                <DIV>
                                                                        <DIV class="text-bold">
                                                                                <I class="fal fa-user-times fontSize22"></I>
                                                                                <SPAN class="ms-2">刪除帳號</SPAN>
                                                                        </DIV>
                                                                        <DIV class="text-xs text-lighter">清空現有帳號，資料將不會保存</DIV>
                                                                </DIV>
                                                                <DIV class="ms-auto">
                                                                        <BUTTON class="btn btn-sm btn-round btn-primary m-0 px-2 py-1" data-bs-target="#deleteModal" data-bs-toggle="modal" type="button">刪除帳號</BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                        <HR class="horizontal dark"/>
                                                        <DIV class="d-flex justify-content-center align-items-center my-2">
                                                                <DIV>
                                                                        <DIV class="text-bold">
                                                                                <I class="fal fa-user-slash fontSize22"></I>
                                                                                <SPAN class="ms-2">封鎖名單</SPAN>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="ms-auto">
                                                                        <BUTTON class="btn btn-sm btn-round btn-dark m-0 px-2 py-1" data-bs-toggle="collapse" data-bs-target="#blockLists" type="button">
                                                                                <SPAN>查看</SPAN>
                                                                                <I class="fas fa-sort-down text-lg"></I>
                                                                        </BUTTON>
                                                                </DIV>
                                                        </DIV>
                                                        <DIV class="collapse" id="blockLists">
                                                                <DIV class="d-flex">
                                                                        <xsl:if test="not(blocked)">
                                                                                <DIV class="text-xs">無封鎖名單</DIV>
                                                                        </xsl:if>
                                                                        <xsl:for-each select="blocked">
                                                                                <DIV class="d-flex align-items-center me-2">
                                                                                        <DIV class="badge bg-light text-dark text-xs py-0">
                                                                                                <BUTTON class="btn btn-link m-0 p-0 unblock" id="{@identifier}">
                                                                                                        <I class="fal fa-times-circle text-warning fontSize22 me-1 py-1"></I>
                                                                                                </BUTTON>
                                                                                                <A href="/profile/{@identifier}/">
                                                                                                        <xsl:value-of select="@nickname"/>
                                                                                                </A>
                                                                                        </DIV>
                                                                                </DIV>
                                                                        </xsl:for-each>
                                                                </DIV>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </DIV>
                                <xsl:call-template name="footer"/>
                        </DIV>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT src="/SCRIPT/setting.js"/>
                        <xsl:if test="@signIn">
                                <SCRIPT src="/SCRIPT/websocket.js"/>
                        </xsl:if>
                </BODY>
        </xsl:template>
</xsl:stylesheet>