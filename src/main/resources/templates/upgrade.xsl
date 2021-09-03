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
			<DIV class="container pt-7 pt-md-8 px-2">
				<DIV class="text-center mx-sm-5 mx-lg-12 mb-3">
					<H4>
						<I class="fad fa-crown fontSize22 text-dark me-2"></I>
						<SPAN class="text-primary">
							<xsl:value-of select="@title"/>
						</SPAN>
					</H4>
				</DIV>
				<xsl:if test="@vvip or @vip">
					<DIV class="text-center border-radius-xl mx-auto col-11 col-md-10 col-xl-8 p-1 mb-3" style="border: 1px solid #D63384;">
						<xsl:if test="@vip">
							<DIV class="text-primary text-bold">目前為 $1688 VIP</DIV>
							<DIV class="text-dark text-bold text-xs">
								<SPAN class="me-1">到期日</SPAN>
								<SPAN>
									<xsl:value-of select="@vipExpiry"/>
								</SPAN>
							</DIV>
						</xsl:if>
						<xsl:if test="@vvip">
							<DIV class="text-primary text-bold">目前為 $1288 VIP</DIV>
							<DIV class="ms-auto text-dark text-xs">
								<xsl:if test="@isEligibleToStopRecurring">
									<SPAN class="me-1">下次扣款</SPAN>
								</xsl:if>
								<xsl:if test="not(@isEligibleToStopRecurring)">
									<SPAN class="me-1">到期日</SPAN>
								</xsl:if>
								<xsl:value-of select="@vvipExpiry"/>
							</DIV>
							<xsl:if test="@isEligibleToStopRecurring">
								<BUTTON class="ms-auto btn btn-sm btn-dark px-3 px-sm-4 m-0 stopRecurring my-2">解除定期定額</BUTTON>
								<DIV class="text-primary text-sm">解除將失去 VIP 功能</DIV>
							</xsl:if>
						</xsl:if>
					</DIV>
				</xsl:if>
				<xsl:if test="not(@vvip) and not(@vip)">
					<DIV class="mx-auto col-11 col-md-10 col-xl-8 trial">
						<BUTTON class="btn btn-light btn-round px-1 w-100 trialBtn">輸入體驗碼，兌換一日VIP身分</BUTTON>
						<DIV class="row mb-3 trialInput d-none">
							<DIV class="col-9 col-md-10">
								<INPUT class="form-control px-1" name="trialCode" placeholder="輸入體驗碼" type="text"/>
							</DIV>
							<DIV class="col-3 col-md-2">
								<BUTTON class="btn btn-primary btn-round px-3 m-0 w-100 confirmTrialCode">確認</BUTTON>
							</DIV>
						</DIV>
					</DIV>
				</xsl:if>
				<DIV class="row mx-auto justify-content-center">
					<DIV class="col-11 col-md-5 col-xl-4 card mb-3 mx-2 " style="background: #E176AB;">
						<DIV class="card-body p-4 p-lg-5">
							<DIV class="text-center bg-white border-radius-xl py-2">
								<SPAN class="text-dark text-lg font-weight-bold my-2">$1688/月 VIP</SPAN>
								<DIV class="text-lg text-bold text-center text-dark my-1">
									<DIV class="text-sm">單次付款</DIV>
									<DIV class="text-xs">不可取消</DIV>
								</DIV>
							</DIV>
							<DIV class="text-lg text-bold text-center text-white my-3">
								<DIV>
									<I class="fas fa-check-circle"></I>
									<SPAN class="ms-1">每日聊天會員無限制</SPAN>
								</DIV>
								<DIV class="text-sm mx-auto bg-dark border-radius-xl py-1 px-2">可與站內所有女會員聊</DIV>
							</DIV>
							<DIV class="text-lg text-bold text-center text-white my-3">
								<DIV>
									<I class="fas fa-check-circle"></I>
									<SPAN class="ms-1">加入好友與通訊軟體</SPAN>
								</DIV>
								<DIV class="text-sm mx-auto bg-dark border-radius-xl py-1 px-2">可加入 meQUEEN 的好友邀請</DIV>
							</DIV>
							<DIV class="text-lg text-bold text-center text-white my-3">
								<DIV>
									<I class="fas fa-check-circle"></I>
									<SPAN class="ms-1">查看會員評價詳情</SPAN>
								</DIV>
								<DIV class="text-sm mx-auto bg-dark border-radius-xl py-1 px-2">可看見 meQueen 文字評價</DIV>
							</DIV>
							<DIV class="text-lg text-bold text-center text-white my-3">
								<DIV>
									<I class="fas fa-check-circle"></I>
									<SPAN class="ms-1">要求看生活照</SPAN>
								</DIV>
								<DIV class="text-sm mx-auto bg-dark border-radius-xl py-1 px-2">可請求查看 meQUEEN 生活照，對方接受後可查看更多</DIV>
							</DIV>
							<xsl:if test="not(@vvip) and not(@vip)">
								<DIV class="text-center">
									<A class="btn btn-light btn-round px-4 m-0" href="/upgrade/1.asp">升級</A>
								</DIV>
							</xsl:if>
						</DIV>
					</DIV>
					<DIV class="col-11 col-md-5 col-xl-4 card mb-3 mx-2 " style="background: #D63384;">
						<DIV class="card-body p-4 p-lg-5">
							<DIV class="text-center bg-white border-radius-xl py-2">
								<SPAN class="text-dark text-lg font-weight-bold my-2">$1288/月 VIP</SPAN>
								<DIV class="text-lg text-bold text-center text-dark my-1">
									<DIV class="text-sm">定期定額扣款，扣繳12次</DIV>
									<DIV class="text-xs">可於期限內取消</DIV>
									<DIV class="text-xs">扣款申請時當月已扣款，則於次月取消</DIV>
								</DIV>
							</DIV>
							<DIV class="text-lg text-bold text-center text-white my-3">
								<DIV>
									<I class="fad fa-crown"></I>
									<SPAN class="ms-1">VIP 等級標章</SPAN>
								</DIV>
								<DIV class="text-sm mx-auto bg-dark border-radius-xl py-1 px-2">提高 meQueen 主動認識您的機會</DIV>
							</DIV>
							<DIV class="text-lg text-bold text-center text-white my-3">
								<DIV>
									<I class="fad fa-crown"></I>
									<SPAN class="ms-1">VIP 推薦專區</SPAN>
								</DIV>
								<DIV class="text-sm mx-auto bg-dark border-radius-xl py-1 px-2">於首頁 VIP 專區顯示您的資料，讓 meQUEEN 更容易注意到您</DIV>
							</DIV>
							<DIV class="text-lg text-bold text-center text-white my-3">
								<DIV>
									<I class="fad fa-crown"></I>
									<SPAN class="ms-1">所有 VIP 功能</SPAN>
								</DIV>
								<DIV class="text-sm mx-auto bg-dark border-radius-xl py-1 px-2">除了新增標章，也包含 $1688 VIP 功能</DIV>
							</DIV>
							<xsl:if test="not(@vvip)">
								<DIV class="text-center">
									<A class="btn btn-light btn-round px-4 m-0" href="/upgrade/2.asp">升級</A>
								</DIV>
							</xsl:if>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="text-center border-radius-xl mx-auto col-11 col-md-10 col-xl-8 px-1" style="border: 1px solid #D63384;">
					<SPAN class="text-primary">本筆款項將在信用卡帳單僅會顯示為 「線上儲值」</SPAN>
				</DIV>
				<xsl:call-template name="footer"/>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/upgrade.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>