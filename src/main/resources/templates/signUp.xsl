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
			<META content="IE=edge" http-equiv="X-UA-Compatible"/>
			<META content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport"/>
			<TITLE>
				<xsl:value-of select="@title"/>
			</TITLE>
			<LINK href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,400;0,700;1,400;1,700&#38;family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&#38;display=swap" rel="stylesheet"/>
			<LINK crossorigin="anonymous" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" rel="stylesheet"/>
		</HEAD>
		<BODY class="mt-3 mb-3">
			<DIV class="container">
				<FORM action="/signUp.asp" method="post">
					<DIV class="form-group mb-3">
						<DIV class="input-group">
							<SELECT class="custom-select" name="country" required="">
								<xsl:apply-templates select="countries/*"/>
							</SELECT>
							<DIV class="input-group-append">
								<INPUT class="form-control" name="login" required="" type="text" value="{login}"/>
							</DIV>
						</DIV>
						<xsl:if test="login/@reason">
							<SMALL class="form-text text-danger">
								<xsl:value-of select="login/@reason"/>
							</SMALL>
						</xsl:if>
					</DIV>
					<BUTTON class="btn btn-primary" id="signUpButton" type="submit">新建帳戶</BUTTON>
				</FORM>
			</DIV>
			<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
			<SCRIPT crossorigin="anonymous" integrity="sha384-Piv4xVNRyMGpqkS2by6br4gNJ7DXjqk09RmUpJ8jgGtD7zP9yug3goQfGII0yAns" src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.bundle.min.js"/>
			<SCRIPT crossorigin="anonymous" integrity="sha512-yUNtg0k40IvRQNR20bJ4oH6QeQ/mgs9Lsa6V+3qxTj58u2r+JiAYOhOW0o+ijuMmqCtCEg7LZRA+T4t84/ayVA==" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.5.0/perfect-scrollbar.min.js"/>
			<SCRIPT src="https://kit.fontawesome.com/5ed1767edc.js"/>
			<SCRIPT src="/SCRIPT/signUp.js"/>
		</BODY>
	</xsl:template>
</xsl:stylesheet>