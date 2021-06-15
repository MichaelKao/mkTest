<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output
		encoding="UTF-8"
		media-type="text/html"
		method="html"
		indent="no"
		omit-xml-declaration="yes"
	/>

	<xsl:template match="optgroup">
		<OPTGROUP label="{@label}">
			<xsl:apply-templates select="option"/>
		</OPTGROUP>
	</xsl:template>

	<xsl:template match="option">
		<OPTION>
			<xsl:if test="@selected">
				<xsl:attribute name="selected"/>
			</xsl:if>
			<xsl:if test="@value">
				<xsl:attribute name="value">
					<xsl:value-of select="@value"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:value-of select="."/>
		</OPTION>
	</xsl:template>

	<xsl:template name="bodyScriptTags">
		<SCRIPT src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha384-Piv4xVNRyMGpqkS2by6br4gNJ7DXjqk09RmUpJ8jgGtD7zP9yug3goQfGII0yAns" src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.bundle.min.js"/>
		<SCRIPT crossorigin="anonymous" integrity="sha512-yUNtg0k40IvRQNR20bJ4oH6QeQ/mgs9Lsa6V+3qxTj58u2r+JiAYOhOW0o+ijuMmqCtCEg7LZRA+T4t84/ayVA==" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/1.5.0/perfect-scrollbar.min.js"/>
		<SCRIPT src="https://kit.fontawesome.com/5ed1767edc.js"/>
	</xsl:template>

	<xsl:template name="headLinkTags">
		<LINK href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,400;0,700;1,400;1,700&#38;family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&#38;display=swap" rel="stylesheet"/>
		<LINK crossorigin="anonymous" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" rel="stylesheet"/>
	</xsl:template>

	<xsl:template name="headMetaTags">
		<META content="IE=edge" http-equiv="X-UA-Compatible"/>
		<META content="width=device-width, initial-scale=1.0, shrink-to-fit=no" name="viewport"/>
	</xsl:template>

	<xsl:template name="bootstrapToast">
		<DIV class="p-3 position-absolute" style="top:0;right:0">
			<DIV class="toast hide" data-delay="3000" role="alert">
				<DIV class="toast-header">
					<STRONG class="mr-auto"/>
					<BUTTON type="button" class="ml-2 mb-1 close" data-dismiss="toast">
						<SPAN>&#215;</SPAN>
					</BUTTON>
				</DIV>
				<DIV class="toast-body"/>
			</DIV>
		</DIV>
	</xsl:template>
</xsl:stylesheet>