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
                        <LINK href="https://npmcdn.com/flickity@2/dist/flickity.css" rel="stylesheet"/>
                        <LINK href="/STYLE/upgrade.css" rel="stylesheet"/>
                </HEAD>
                <BODY>
                        <xsl:call-template name="navbar"/>
                        <xsl:call-template name="bootstrapToast"/>
                        <DIV class="container pt-7 pt-md-8 px-2">
                                <DIV class="carousel mb-5 mx-auto col-12 col-md-10 col-xl-8">
                                        <DIV class="carousel-cell card d-flex flex-column justify-content-center align-items-center">
                                                <DIV>
                                                        <SPAN class="text-yellow me-1">
                                                                <I class="fad fa-crown fontSize22"></I>
                                                        </SPAN>
                                                        <SPAN class="text-white text-bold">等級徽章</SPAN>
                                                </DIV>
                                                <DIV>
                                                        <SPAN class="text-white text-sm">提高 meQueen 主動找您的機會</SPAN>
                                                </DIV>
                                                <DIV>
                                                        <SPAN class="text-yellow text-bolder">PRO</SPAN>
                                                        <SPAN class="text-white text-sm">only</SPAN>
                                                </DIV>
                                        </DIV>
                                        <DIV class="carousel-cell card d-flex flex-column justify-content-center align-items-center">
                                                <DIV>
                                                        <SPAN class="text-white text-bold">推薦專區</SPAN>
                                                        <SPAN class="text-white ms-1">
                                                                <I class="fad fa-thumbs-up fontSize22"></I>
                                                        </SPAN>
                                                </DIV>
                                                <DIV class=" d-flex flex-column align-items-center">
                                                        <SPAN class="text-white text-sm">於首頁 VIP 專區顯示您的資訊</SPAN>
                                                        <SPAN class="text-white text-sm">讓 meQueen 更容易注意到您</SPAN>
                                                </DIV>
                                                <DIV>
                                                        <SPAN class="text-yellow text-bolder">PRO</SPAN>
                                                        <SPAN class="text-white text-sm">only</SPAN>
                                                </DIV>
                                        </DIV>
                                        <DIV class="carousel-cell card d-flex flex-column justify-content-center align-items-center">
                                                <DIV>
                                                        <SPAN class="text-white text-bold">無限聊天會員</SPAN>
                                                        <SPAN class="text-white ms-1">
                                                                <I class="fad fa-comment-smile fontSize22"></I>
                                                        </SPAN>
                                                </DIV>
                                                <DIV class=" d-flex flex-column">
                                                        <SPAN class="text-white text-sm">和 meQueen 聊天次數無限制</SPAN>
                                                </DIV>
                                                <DIV>
                                                        <SPAN class="text-yellow text-bolder">PRO</SPAN>
                                                        <SPAN class="text-white text-bolder mx-1">·</SPAN>
                                                        <SPAN class="text-yellow">
                                                                <I class="fas fa-plus"></I>
                                                        </SPAN>
                                                </DIV>
                                        </DIV>
                                        <DIV class="carousel-cell card d-flex flex-column justify-content-center align-items-center">
                                                <DIV>
                                                        <SPAN class="text-white text-bold">加入好友與通訊軟體</SPAN>
                                                        <SPAN class="text-white ms-1">
                                                                <I class="fad fa-comment-plus fontSize22"></I>
                                                        </SPAN>
                                                </DIV>
                                                <DIV class=" d-flex flex-column align-items-center">
                                                        <SPAN class="text-white text-sm">可加入 meQUEEN 的好友邀請</SPAN>
                                                        <SPAN class="text-white text-sm">同時得到對方的通訊軟體聯繫方式</SPAN>
                                                </DIV>
                                                <DIV>
                                                        <SPAN class="text-yellow text-bolder">PRO</SPAN>
                                                        <SPAN class="text-white text-bolder mx-1">·</SPAN>
                                                        <SPAN class="text-yellow">
                                                                <I class="fas fa-plus"></I>
                                                        </SPAN>
                                                </DIV>
                                        </DIV>
                                        <DIV class="carousel-cell card d-flex flex-column justify-content-center align-items-center">
                                                <DIV>
                                                        <SPAN class="text-white text-bold">查看會員評價詳情</SPAN>
                                                        <SPAN class="text-white ms-1">
                                                                <I class="fad fa-star fontSize22"></I>
                                                        </SPAN>
                                                </DIV>
                                                <DIV class=" d-flex flex-column">
                                                        <SPAN class="text-white text-sm">可看見 meQueen 文字評價</SPAN>
                                                </DIV>
                                                <DIV>
                                                        <SPAN class="text-yellow text-bolder">PRO</SPAN>
                                                        <SPAN class="text-white text-bolder mx-1">·</SPAN>
                                                        <SPAN class="text-yellow">
                                                                <I class="fas fa-plus"></I>
                                                        </SPAN>
                                                </DIV>
                                        </DIV>
                                        <DIV class="carousel-cell card d-flex flex-column justify-content-center align-items-center">
                                                <DIV>
                                                        <SPAN class="text-white text-bold">要求看生活照</SPAN>
                                                        <SPAN class="text-white ms-1">
                                                                <I class="fad fa-leaf-heart fontSize22"></I>
                                                        </SPAN>
                                                </DIV>
                                                <DIV class=" d-flex flex-column align-items-center">
                                                        <SPAN class="text-white text-sm">可邀請查看 meQUEEN 生活照</SPAN>
                                                        <SPAN class="text-white text-sm">邀請接受後可查看更多</SPAN>
                                                </DIV>
                                                <DIV>
                                                        <SPAN class="text-yellow text-bolder">PRO</SPAN>
                                                        <SPAN class="text-white text-bolder mx-1">·</SPAN>
                                                        <SPAN class="text-yellow">
                                                                <I class="fas fa-plus"></I>
                                                        </SPAN>
                                                </DIV>
                                        </DIV>
                                </DIV>
                                <xsl:if test="@vvip or @vip or @trial">
                                        <DIV class="card text-center border-radius-xl mx-auto col-12 col-md-10 col-xl-8 p-1 mb-3">
                                                <xsl:if test="@trial">
                                                        <DIV class="text-primary text-bold">目前為單日體驗 VIP</DIV>
                                                        <DIV class="text-dark text-bold text-xs">
                                                                <SPAN class="me-1">到期時間</SPAN>
                                                                <SPAN>
                                                                        <xsl:value-of select="@trialExpiry"/>
                                                                </SPAN>
                                                        </DIV>
                                                </xsl:if>
                                                <xsl:if test="@vip">
                                                        <DIV class="text-primary text-bold">
                                                                <SPAN>目前為 meKing</SPAN>
                                                                <SPAN class="text-yellow fontSize22 ms-1">
                                                                        <I class="fas fa-plus"></I>
                                                                </SPAN>
                                                        </DIV>
                                                        <DIV class="text-dark text-bold text-xs">
                                                                <SPAN class="me-1">到期日</SPAN>
                                                                <SPAN>
                                                                        <xsl:value-of select="@vipExpiry"/>
                                                                </SPAN>
                                                        </DIV>
                                                </xsl:if>
                                                <xsl:if test="@vvip">
                                                        <DIV class="text-primary text-bold">
                                                                <SPAN>目前為 meKing</SPAN>
                                                                <SPAN class="text-yellow text-bolder fontSize22 ms-1">PRO</SPAN>
                                                        </DIV>
                                                        <DIV class="text-dark text-xs">
                                                                <xsl:if test="@isEligibleToStopRecurring">
                                                                        <SPAN class="me-1">下次扣款</SPAN>
                                                                </xsl:if>
                                                                <xsl:if test="not(@isEligibleToStopRecurring)">
                                                                        <SPAN class="me-1">到期日</SPAN>
                                                                </xsl:if>
                                                                <xsl:value-of select="@vvipExpiry"/>
                                                        </DIV>
                                                        <xsl:if test="@isEligibleToStopRecurring">
                                                                <BUTTON class="btn btn-round btn-dark py-2 px-3 px-sm-4 m-0 stopRecurring my-2 mx-auto">解除定期定額</BUTTON>
                                                                <DIV class="text-primary text-sm">解除將失去 VIP 功能</DIV>
                                                        </xsl:if>
                                                </xsl:if>
                                        </DIV>
                                </xsl:if>
                                <xsl:if test="not(@vvip) and not(@vip) and not(@trial) and @ableToTrial">
                                        <DIV class="mx-auto col-12 col-md-10 col-xl-8 trial text-center">
                                                <BUTTON class="btn btn-outline-dark btn-round px-3 trialBtn">輸入體驗碼，兌換一日VIP身分</BUTTON>
                                                <DIV class="row mb-3 trialInput d-none">
                                                        <DIV class="col-9 col-md-10">
                                                                <INPUT class="form-control px-1" name="code" placeholder="輸入體驗碼" type="text"/>
                                                        </DIV>
                                                        <DIV class="col-3 col-md-2">
                                                                <BUTTON class="btn btn-primary btn-round px-2 px-sm-3 m-0 w-100 confirmTrialCode">確認</BUTTON>
                                                        </DIV>
                                                </DIV>
                                        </DIV>
                                </xsl:if>
                                <DIV class="row mx-auto justify-content-center">
                                        <DIV class="col-6 col-md-5 col-xl-4 mb-3 pe-1 ps-0">
                                                <DIV class="p-2 card h-100">
                                                        <DIV class="text-center border-radius-xl py-2">
                                                                <SPAN class="text-primary text-lg font-weight-bold my-2">meKing</SPAN>
                                                                <SPAN class="text-yellow fontSize22 ms-1">
                                                                        <I class="fas fa-plus"></I>
                                                                </SPAN>
                                                                <DIV class="text-lg text-bold text-center my-1">
                                                                        <DIV class="text-sm">
                                                                                <DIV class="fontSize35">1</DIV>
                                                                                <DIV>個月</DIV>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="text-lg text-bold text-center my-1">
                                                                        <DIV class="text-lg">$1688/月</DIV>
                                                                        <DIV class="text-xs">單次付款</DIV>
                                                                        <DIV class="text-xs">不可取消</DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <xsl:if test="not(@vvip) and not(@vip)">
                                                                <A class="btn btn-primary btn-round text-center py-2 col-11 col-md-8 mx-auto" href="/upgrade/1.asp" onclick="fbq('track', 'InitiateCheckout');">升級</A>
                                                        </xsl:if>
                                                </DIV>
                                        </DIV>
                                        <DIV class="col-6 col-md-5 col-xl-4 mb-3 ps-1 pe-0" style="">
                                                <DIV class="p-2 card h-100">
                                                        <DIV class="text-center border-radius-xl py-2">
                                                                <SPAN class="text-primary text-lg font-weight-bold my-2">meKing</SPAN>
                                                                <SPAN class="text-yellow text-bolder fontSize22 ms-1">PRO</SPAN>
                                                                <DIV class="text-lg text-bold text-center my-1">
                                                                        <DIV class="text-sm">
                                                                                <DIV class="fontSize35">12</DIV>
                                                                                <DIV>個月</DIV>
                                                                        </DIV>
                                                                </DIV>
                                                                <DIV class="text-lg text-bold text-center my-1">
                                                                        <DIV class="text-lg">$1288/月</DIV>
                                                                        <DIV class="text-xs">定期定額扣款12次</DIV>
                                                                        <DIV class="text-xs">可於期限內取消</DIV>
                                                                </DIV>
                                                        </DIV>
                                                        <xsl:if test="not(@vvip)">
                                                                <A class="btn btn-primary btn-round text-center py-2 col-11 col-md-8 mx-auto" href="/upgrade/2.asp" onclick="fbq('track', 'InitiateCheckout');">升級</A>
                                                        </xsl:if>
                                                </DIV>
                                        </DIV>
                                        <DIV class=""></DIV>
                                </DIV>
                                <DIV class="text-center card border-radius-xl text-xs mx-auto col-12 col-md-10 col-xl-8 p-1">
                                        <DIV class="text-primary">本筆款項將在信用卡帳單</DIV>
                                        <DIV class="text-primary">僅會顯示為 「遊戲點數購買」</DIV>
                                </DIV>
                                <xsl:call-template name="footer"/>
                        </DIV>
                        <xsl:call-template name="bodyScriptTags"/>
                        <SCRIPT src="https://npmcdn.com/flickity@2/dist/flickity.pkgd.js"/>
                        <SCRIPT src="/SCRIPT/upgrade.js"/>
                        <xsl:if test="@signIn">
                                <SCRIPT src="/SCRIPT/websocket.js"/>
                        </xsl:if>
                </BODY>
        </xsl:template>
</xsl:stylesheet>