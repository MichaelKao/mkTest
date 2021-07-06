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
			<DIV class="container pt-8 pb-4">
				<DIV class="text-center">
					<H3 class="text-primary">甜心車馬費提取方式</H3>
					<DIV>
						<BUTTON class="btn btn-light text-lg collapseBtn m-1" data-bs-target="#collapse1" data-bs-toggle="collapse" type="button">銀行匯款</BUTTON>
						<BUTTON class="btn btn-light text-lg collapseBtn m-1" data-bs-target="#collapse2" data-bs-toggle="collapse" type="button">Paypal</BUTTON>
					</DIV>
				</DIV>
				<DIV class="collapse" id="collapse1">
					<DIV class="col-lg-7 mx-auto my-3">
						<DIV class="card d-flex blur justify-content-center p-4 shadow-lg">
							<DIV class="text-center">
								<H3 class="text-gradient text-primary">銀行匯款</H3>
							</DIV>
							<FORM action="" method="post">
								<DIV class="card-body pb-2">
									<DIV class="row">
										<DIV class="col-md-6">
											<LABEL>銀行代碼</LABEL>
											<DIV class="input-group mb-3">
												<INPUT class="form-control" placeholder="銀行代碼" type="number"/>
											</DIV>
										</DIV>
										<DIV class="col-md-6 ps-md-2">
											<LABEL>分行代碼</LABEL>
											<DIV class="input-group mb-3">
												<INPUT class="form-control" placeholder="分行代碼" type="number"/>
											</DIV>
										</DIV>
									</DIV>
									<DIV class="row">
										<DIV class="col-md-6">
											<LABEL>戶名</LABEL>
											<DIV class="input-group mb-3">
												<INPUT class="form-control" placeholder="帳戶所有人名稱" type="text"/>
											</DIV>
										</DIV>
										<DIV class="col-md-6 ps-md-2">
											<LABEL>帳號</LABEL>
											<DIV class="input-group mb-3">
												<INPUT class="form-control" placeholder="帳號" type="number"/>
											</DIV>
										</DIV>
									</DIV>
									<DIV class="text-center">
										<BUTTON class="btn bg-gradient-primary btn-lg mt-4 mb-0" type="button">使用銀行匯款</BUTTON>
									</DIV>
								</DIV>
							</FORM>
						</DIV>
					</DIV>
				</DIV>
				<DIV class="collapse" id="collapse2">
					<DIV class="col-lg-7 mx-auto my-3">
						<DIV class="card d-flex blur justify-content-center p-4 shadow-lg">
							<DIV class="text-center">
								<H3 class="text-gradient text-primary">Paypal</H3>
							</DIV>
							<FORM action="" method="post">
								<DIV class="card-body pb-2">
									<DIV class="row">
										<DIV class="col-md-6">
											<LABEL>名字</LABEL>
											<DIV class="input-group mb-4">
												<INPUT class="form-control" placeholder="全名" type="text"/>
											</DIV>
										</DIV>
										<DIV class="col-md-6 ps-md-2">
											<LABEL>Paypal Email</LABEL>
											<DIV class="input-group">
												<INPUT type="email" class="form-control" placeholder="Email"/>
											</DIV>
										</DIV>
									</DIV>
								</DIV>
								<DIV class="text-center">
									<BUTTON class="btn bg-gradient-primary btn-lg mt-4 mb-0" type="button">使用 Paypal</BUTTON>
								</DIV>
							</FORM>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<SCRIPT src="/SCRIPT/withdrawal.js"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>