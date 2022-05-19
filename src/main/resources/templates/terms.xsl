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

			<style type="text/css">
				ol {
				margin: 0;
				padding: 0
				}

				table td,
				table th {
				padding: 0
				}

				.c84 {
				margin-left: 24.8pt;
				padding-top: 1.2pt;
				text-indent: -24pt;
				padding-bottom: 0pt;
				line-height: 1.3911094665527344;
				text-align: justify;
				margin-right: 6.5pt
				}

				.c57 {
				margin-left: 18.6pt;
				padding-top: 1.6pt;
				text-indent: -18.1pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.7pt
				}

				.c58 {
				margin-left: 48.8pt;
				padding-top: 1.3pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.3994402885437012;
				text-align: left;
				margin-right: 7.3pt
				}

				.c33 {
				margin-left: 24.8pt;
				padding-top: 5.9pt;
				text-indent: -23.8pt;
				padding-bottom: 0pt;
				line-height: 1.3911094665527344;
				text-align: justify;
				margin-right: 6.6pt
				}

				.c7 {
				margin-left: 1pt;
				padding-top: 6.2pt;
				text-indent: 23.5pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: left;
				margin-right: 6.6pt
				}

				.c72 {
				margin-left: 24.7pt;
				padding-top: 1.6pt;
				text-indent: -24pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.8pt
				}

				.c54 {
				margin-left: 24.8pt;
				padding-top: 1.2pt;
				text-indent: -24.2pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.6pt
				}

				.c17 {
				margin-left: 24.7pt;
				padding-top: 5.9pt;
				text-indent: -23.8pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.6pt
				}

				.c11 {
				margin-left: 18.6pt;
				padding-top: 5.9pt;
				text-indent: -17.9pt;
				padding-bottom: 0pt;
				line-height: 1.3994402885437012;
				text-align: left;
				margin-right: 7.2pt
				}

				.c82 {
				margin-left: 24.8pt;
				padding-top: 1.3pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.3911100625991821;
				text-align: justify;
				margin-right: 6.6pt
				}

				.c43 {
				margin-left: 24.8pt;
				padding-top: 1.6pt;
				text-indent: -24.4pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: left;
				margin-right: 6.7pt
				}

				.c18 {
				margin-left: 24.7pt;
				padding-top: 1.2pt;
				text-indent: -24pt;
				padding-bottom: 0pt;
				line-height: 1.382780909538269;
				text-align: left;
				margin-right: 7.3pt
				}

				.c22 {
				margin-left: 24.8pt;
				padding-top: 0pt;
				text-indent: 0.1pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.7pt
				}

				.c92 {
				margin-left: 24.7pt;
				padding-top: 1.2pt;
				text-indent: -24.7pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 7.3pt
				}

				.c63 {
				margin-left: 0.7pt;
				padding-top: 0pt;
				text-indent: 0.5pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c5 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 10.1pt;
				font-family: "Noto Sans Symbols";
				font-style: normal
				}

				.c29 {
				margin-left: 24.7pt;
				padding-top: 1.2pt;
				text-indent: -23.9pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c59 {
				margin-left: 0.5pt;
				padding-top: 1.5pt;
				text-indent: -0.2pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: left;
				margin-right: 6.6pt
				}

				.c4 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 10.1pt;
				font-family: "Arial";
				font-style: normal
				}

				.c71 {
				margin-left: 24.7pt;
				padding-top: 1.2pt;
				text-indent: -23.8pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c93 {
				margin-left: 24.8pt;
				padding-top: 1.3pt;
				text-indent: -24.8pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c85 {
				margin-left: 48.8pt;
				padding-top: 0pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.6pt
				}

				.c39 {
				margin-left: 18.8pt;
				padding-top: 1.6pt;
				text-indent: -18.1pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.6pt
				}

				.c60 {
				margin-left: 24.8pt;
				padding-top: 1.3pt;
				text-indent: -24.8pt;
				padding-bottom: 0pt;
				line-height: 1.3869447708129883;
				text-align: justify;
				margin-right: 6.8pt
				}

				.c14 {
				margin-left: 24.8pt;
				padding-top: 1.6pt;
				text-indent: -24.1pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c90 {
				margin-left: 24.8pt;
				padding-top: 1.6pt;
				text-indent: -23.9pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.7pt
				}

				.c48 {
				margin-left: 24.8pt;
				padding-top: 1.3pt;
				text-indent: -24.1pt;
				padding-bottom: 0pt;
				line-height: 1.382780909538269;
				text-align: left;
				margin-right: 7.2pt
				}

				.c13 {
				margin-left: 48.8pt;
				padding-top: 5.9pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.399442434310913;
				text-align: left;
				margin-right: 6.7pt
				}

				.c46 {
				margin-left: 0.7pt;
				padding-top: 0pt;
				text-indent: 23.9pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: left;
				margin-right: 6.6pt
				}

				.c49 {
				margin-left: 24.6pt;
				padding-top: 5.9pt;
				text-indent: -24.1pt;
				padding-bottom: 0pt;
				line-height: 1.386945128440857;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c1 {
				color: #000000;
				font-weight: 700;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 12pt;
				font-family: "Arial";
				font-style: normal
				}

				.c64 {
				margin-left: 24.7pt;
				padding-top: 5.9pt;
				text-indent: -23.8pt;
				padding-bottom: 0pt;
				line-height: 1.3911094665527344;
				text-align: justify;
				margin-right: 6.6pt
				}

				.c30 {
				margin-left: 24.7pt;
				padding-top: 1.2pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c6 {
				margin-left: 24.8pt;
				padding-top: 5.9pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.3894442319869995;
				text-align: justify;
				margin-right: 6.6pt
				}

				.c44 {
				margin-left: 25.5pt;
				padding-top: 1.2pt;
				text-indent: -25pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.8pt
				}

				.c51 {
				margin-left: 0.7pt;
				padding-top: 26.1pt;
				text-indent: -0.6pt;
				padding-bottom: 0pt;
				line-height: 1.3869447708129883;
				text-align: left;
				margin-right: 6.6pt
				}

				.c27 {
				margin-left: 17.9pt;
				padding-top: 1.5pt;
				text-indent: -17.2pt;
				padding-bottom: 0pt;
				line-height: 1.3869447708129883;
				text-align: left;
				margin-right: 2.9pt
				}

				.c77 {
				margin-left: 50.4pt;
				padding-top: 1.6pt;
				text-indent: -21.6pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.6pt
				}

				.c86 {
				margin-left: 18.5pt;
				padding-top: 5.9pt;
				text-indent: -17.5pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: left;
				margin-right: 6.6pt
				}

				.c21 {
				margin-left: 24.8pt;
				padding-top: 5.9pt;
				text-indent: -23.8pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.6pt
				}

				.c0 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 12pt;
				font-family: "Arial";
				font-style: normal
				}

				.c75 {
				margin-left: 24.8pt;
				padding-top: 1.6pt;
				text-indent: -24.8pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.5pt
				}

				.c69 {
				margin-left: 24.7pt;
				padding-top: 1.2pt;
				text-indent: -24.7pt;
				padding-bottom: 0pt;
				line-height: 1.3911094665527344;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c78 {
				margin-left: 24.8pt;
				padding-top: 5.9pt;
				text-indent: -24.1pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c55 {
				margin-left: 18.4pt;
				padding-top: 1.3pt;
				text-indent: -17.9pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c95 {
				margin-left: 18.7pt;
				padding-top: 1.1pt;
				text-indent: -18.7pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.9pt
				}

				.c70 {
				margin-left: 24.7pt;
				padding-top: 1.6pt;
				text-indent: -24.2pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c28 {
				margin-left: 24.8pt;
				padding-top: 1.2pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 7.2pt
				}

				.c26 {
				margin-left: 24.8pt;
				padding-top: 1.6pt;
				text-indent: -24.3pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: justify;
				margin-right: 6.7pt
				}

				.c31 {
				margin-left: 18.7pt;
				padding-top: 0pt;
				text-indent: 0pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.7pt
				}

				.c65 {
				margin-left: 24.8pt;
				padding-top: 5.9pt;
				text-indent: -23.8pt;
				padding-bottom: 0pt;
				line-height: 1.3875402212142944;
				text-align: left;
				margin-right: 6.6pt
				}

				.c41 {
				margin-left: 18.7pt;
				padding-top: 1.6pt;
				text-indent: -17.7pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 7pt
				}

				.c79 {
				margin-left: 18.8pt;
				padding-top: 5.9pt;
				text-indent: -17.8pt;
				padding-bottom: 0pt;
				line-height: 1.382780909538269;
				text-align: left;
				margin-right: 6.7pt
				}

				.c20 {
				margin-left: 24.5pt;
				padding-top: 1.6pt;
				text-indent: -24.5pt;
				padding-bottom: 0pt;
				line-height: 1.3883334398269653;
				text-align: left;
				margin-right: 6.6pt
				}

				.c76 {
				margin-left: 0.6pt;
				padding-top: 0pt;
				text-indent: 24.1pt;
				padding-bottom: 0pt;
				line-height: 1.3894442319869995;
				text-align: left;
				margin-right: 6.6pt
				}

				.c15 {
				margin-left: 24.7pt;
				padding-top: 5.9pt;
				text-indent: -23.7pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.8pt
				}

				.c62 {
				margin-left: 49.4pt;
				padding-top: 1.1pt;
				text-indent: -24.9pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 7.1pt
				}

				.c91 {
				margin-left: 24.7pt;
				padding-top: 1.3pt;
				text-indent: -24pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.6pt
				}

				.c2 {
				margin-left: 40.5pt;
				padding-top: 5.9pt;
				text-indent: -14.6pt;
				padding-bottom: 0pt;
				line-height: 1.3994402885437012;
				text-align: left;
				margin-right: 6.7pt
				}

				.c87 {
				margin-left: 50.3pt;
				padding-top: 0pt;
				text-indent: -21.6pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: justify;
				margin-right: 6.9pt
				}

				.c24 {
				margin-left: 24.8pt;
				padding-top: 6.2pt;
				text-indent: -23.8pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 6.7pt
				}

				.c74 {
				margin-left: 28.8pt;
				padding-top: 1.2pt;
				padding-bottom: 0pt;
				line-height: 1.388334035873413;
				text-align: left;
				margin-right: 6.6pt
				}

				.c42 {
				margin-left: 28.8pt;
				padding-top: 1.6pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: left;
				margin-right: 11.4pt
				}

				.c73 {
				margin-left: 24.8pt;
				padding-top: 1.2pt;
				text-indent: -24.2pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: left
				}

				.c12 {
				margin-left: 28.8pt;
				padding-top: 1.3pt;
				padding-bottom: 0pt;
				line-height: 1.3911106586456299;
				text-align: left;
				margin-right: 7pt
				}

				.c45 {
				margin-left: 28.8pt;
				padding-top: 1.2pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: center;
				margin-right: 6.6pt
				}

				.c40 {
				margin-left: 28.8pt;
				padding-top: 5.9pt;
				padding-bottom: 0pt;
				line-height: 1.3994402885437012;
				text-align: left;
				margin-right: 10.7pt
				}

				.c8 {
				margin-left: 0.5pt;
				padding-top: 1.6pt;
				padding-bottom: 0pt;
				line-height: 1.3827788829803467;
				text-align: center;
				margin-right: 8.4pt
				}

				.c3 {
				margin-left: 149.5pt;
				padding-top: 0pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c36 {
				margin-left: 167.4pt;
				padding-top: 47.4pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c67 {
				margin-left: 28.8pt;
				padding-top: 6.2pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c94 {
				margin-left: 167.4pt;
				padding-top: 427.6pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c68 {
				margin-left: 0.8pt;
				padding-top: 21.4pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c61 {
				margin-left: 24.5pt;
				padding-top: 1.1pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c53 {
				margin-left: 0.8pt;
				padding-top: 21.5pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c34 {
				margin-left: 167.4pt;
				padding-top: 42.6pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c88 {
				margin-left: 1pt;
				padding-top: 5.9pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c56 {
				margin-left: 167.4pt;
				padding-top: 22.9pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c96 {
				margin-left: 1.2pt;
				padding-top: 21.5pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c19 {
				margin-left: 0.7pt;
				padding-top: 21.5pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c52 {
				margin-left: 25.9pt;
				padding-top: 6.2pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c47 {
				margin-left: 24.5pt;
				padding-top: 1.6pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c66 {
				margin-left: 0.5pt;
				padding-top: 1.1pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c9 {
				margin-left: 167.4pt;
				padding-top: 27.5pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c10 {
				margin-left: 0.8pt;
				padding-top: 21.1pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c37 {
				margin-left: 24.5pt;
				padding-top: 1.3pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c32 {
				margin-left: 25.9pt;
				padding-top: 1.2pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c50 {
				margin-left: 167.4pt;
				padding-top: 22.8pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c25 {
				margin-left: 24.5pt;
				padding-top: 5.9pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c83 {
				margin-left: 25.9pt;
				padding-top: 5.9pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c23 {
				margin-left: 0.7pt;
				padding-top: 21.4pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c35 {
				margin-left: 24.5pt;
				padding-top: 6.2pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c16 {
				margin-left: 28.8pt;
				padding-top: 5.9pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c89 {
				padding-top: 1.5pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c80 {
				padding-top: 1.3pt;
				padding-bottom: 0pt;
				line-height: 1.0;
				text-align: left
				}

				.c38 {
<!--				background-color: #ffffff;-->
<!--				max-width: 424.1pt;-->
<!--				padding: 76.1pt 80.9pt 54.3pt 90.1pt-->
				}

				.c81 {
				margin-left: 28.8pt
				}

				.title {
				padding-top: 24pt;
				color: #000000;
				font-weight: 700;
				font-size: 36pt;
				padding-bottom: 6pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.subtitle {
				padding-top: 18pt;
				color: #666666;
				font-size: 24pt;
				padding-bottom: 4pt;
				font-family: "Georgia";
				line-height: 1.15;
				page-break-after: avoid;
				font-style: italic;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				li {
				color: #000000;
				font-size: 11pt;
				font-family: "Arial"
				}

				p {
				margin: 0;
				color: #000000;
				font-size: 11pt;
				font-family: "Arial"
				}

				h1 {
				padding-top: 24pt;
				color: #000000;
				font-weight: 700;
				font-size: 24pt;
				padding-bottom: 6pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h2 {
				padding-top: 18pt;
				color: #000000;
				font-weight: 700;
				font-size: 18pt;
				padding-bottom: 4pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h3 {
				padding-top: 14pt;
				color: #000000;
				font-weight: 700;
				font-size: 14pt;
				padding-bottom: 4pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h4 {
				padding-top: 12pt;
				color: #000000;
				font-weight: 700;
				font-size: 12pt;
				padding-bottom: 2pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h5 {
				padding-top: 11pt;
				color: #000000;
				font-weight: 700;
				font-size: 11pt;
				padding-bottom: 2pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h6 {
				padding-top: 10pt;
				color: #000000;
				font-weight: 700;
				font-size: 10pt;
				padding-bottom: 2pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}
			</style>
		</HEAD>
		<BODY>
			<xsl:call-template name="navbar"/>
			<xsl:call-template name="bootstrapToast"/>
			<DIV class="container py-6 py-md-7">
				<DIV class="col-11 col-md-8 col-lg-6 mx-auto">
					<H4 class="text-center text-primary">Youngme 使用者條款</H4>


				<div class="c38">
					<p class="c51"><span class="c0">Youngme 是由雷爾傳媒股份有限公司（以下簡稱「</span><span class="c1">本公司</span><span class="c0">」）所建置及提供之
						交友約會平台服務（以下簡稱「</span><span class="c1">本服務</span><span class="c0">」）。本使用者條款（以下簡稱「</span><span
							class="c1">本條款</span><span class="c0">」） 構成本公司與本服務使用者（以下簡稱「</span><span class="c1">用戶</span><span
							class="c0">」）間具有法律拘束力之契約關係。 於用戶開始使用本服務時，視為用戶已閱讀、暸解、並同意接受本條款之所有內 容。 </span></p>
					<p class="c23"><span class="c1">一、認知與接受條款 </span></p>
					<p class="c15"><span class="c0">1. 本公司有權隨時修訂本條款之內容。修訂後之本條款會透過本服務公告，以 供用戶瞭解。 </span></p>
					<p class="c70"><span class="c0">2. 本條款經修訂後之內容，除另有說明者外，自公告時起生效。自生效日起， 如用戶繼續使用本服務，即視為用戶已閱讀、瞭解、並同意修訂後條款之所
						有內容。如用戶不同意修改後之內容，用戶應停止使用本服務，且本公司得 終止與用戶間之合約，並不因此而對用戶負擔任何賠償或補償之責任。 </span></p>
					<p class="c91"><span class="c0">3. 本公司有權不定時針對本服務之全部或一部公告本條款以外之補充條款（包 括但不限於：對於特定事件之政策、優惠券使用條款等）。補充條款自公告
						時起生效，並構成本條款之一部，對用戶具有拘束效力。 </span></p>
					<p class="c10"><span class="c1">二、用戶及帳號 </span></p>
					<p class="c24"><span class="c0">1. 用戶申請使用本服務時，應按本服務之要求填入用戶資料、提供照片或其他 相關資訊以完成註冊本服務。 </span></p>
					<p class="c82"><span class="c0">2. 用戶擔保前一項所提供的所有資料均屬正確且即時，且不得以非本人之資料 註冊為用戶；如用戶所提供的資料事後有變更，用戶應即時更新其資料。如
						用戶未即時提供或更新資料、未按指定方式提供資料、所提供之資料不正確 或與事實不符，本公司有權不經事先通知即逕行暫停或終止用戶使用本服務 之權限。 </span></p>
					<p class="c84"><span class="c0">3. 本公司為確認個人資料之真實性，將適度於網頁中要求用戶提供</span><span class="c1">可供驗證之 身分證明文件</span><span
							class="c0">。如用戶拒絕提供可供驗證之身份證明文件，用戶應停止使用 本服務。 </span></p>
					<p class="c92"><span class="c0">4. 為符合我國對兒童及少年福利及權益之保障，</span><span class="c1">若用戶未滿 18 歲，不得使用 本服務</span><span
							class="c0">。 </span></p>
					<p class="c90"><span class="c0">5. 用戶欲使用本服務者，並應依本服務所訂之手機驗證方式完成帳號登入。若 用戶對於無法登入帳號或登入帳戶之程序存有疑問，用戶得與本公司聯繫；</span></p>
					<p class="c56"><span class="c4">第 1 頁，共 8 頁 </span></p>
					<p class="c76"><span class="c0">若用戶拒絕接受本服務所訂之驗證方式，用戶應停止使用本服務。 6. 任一用戶就使用本服務所取得之帳號，僅供用戶本人使用，該帳號不可轉讓、
						贈與、貸與他人使用、及繼承，且用戶不得以有償或無償之方式透露或提供 予任何第三人登錄或使用。任何經由輸入該組帳號所發生的交易、活動，均 視為註冊該帳號之用戶之行為，並應由註冊該帳號之用戶自行負責，均與本 公司無涉。
					</span></p>
					<p class="c30"><span class="c0">7. 用戶如果發現或懷疑其帳號遭第三人冒用或不當使用，應立即通知本公司， 以便本公司採取適當之因應措施。但上述因應措施不得解釋為本公司因此而
						明示或默示對用戶負有任何形式之賠償或補償義務。 </span></p>
					<p class="c54"><span class="c0">8. 用戶瞭解並同意，本公司得藉由簡訊、電子郵件、電話等方式提供用戶關於 本公司、本服務及合作商家之相關資訊。 </span></p>
					<p class="c8"><span class="c0">9. 用戶瞭解並同意，不論本公司因何種原因註銷用戶帳號或終止提供本服務， 本公司就用戶帳號之所有聊天紀錄及相關資訊等，均不負擔備份之義務。 </span></p>
					<p class="c19"><span class="c1">三、本服務內容 </span></p>
					<p class="c21"><span class="c0">1. 本服務係為提供自助見面之網路交友服務，用戶瞭解並同意本服務僅提供之 會員名單及聯繫途徑以供聊天、表達見面意願。本公司不擔保成立約會、或 於一定能找到約會對象。
					</span></p>
					<p class="c73"><span class="c0">2. 本服務於用戶雙方表達見面意願後，後續用戶之個人活動均由用戶自行安排， 由用戶自行決定赴約與否、後續感情、約會、甚至財務狀況，均後續用戶之
						個人活動均為用戶私領域，均與本網站無涉。 </span></p>
					<p class="c18"><span class="c0">3. </span><span class="c1">本服務提醒關於個人財務資訊（例如，您的信用卡或銀行帳號資訊），請用 戶妥善保管</span><span
							class="c0">，切勿提供財務資訊與其他用戶或不明使用者。 </span></p>
					<p class="c75"><span class="c0">4. 於遵守本條款之情形下，本公司授予用戶有限的、得撤銷的、非獨家的、不 得再授權的、不得轉讓之權限，得為下列行為： </span></p>
					<p class="c58"><span class="c5">• </span><span class="c0">造訪本服務網站和使用個人設備上本服務之應用程式，而造訪或使用內 容僅限於本公司提供本服務之範疇； </span></p>
					<p class="c62"><span class="c5">• </span><span class="c0">造訪或於合法權限內範圍使用透過本服務所獲取之任何內容、資訊和相 關資料。 </span></p>
					<p class="c96"><span class="c1">四、用戶行為 </span></p>
					<p class="c64"><span class="c0">1. 用戶所提供可供驗證之身分證明文件，本服務僅用於確認人別及年齡與用戶 註冊帳戶者之同一性</span><span class="c1">。</span><span
							class="c0">用戶於本服務所提供之資料（包括但不限於用戶之手 機號碼、照片、電子信箱、個人資料等） （以下簡稱「</span><span class="c1">提交內容</span><span
							class="c0">」），本公</span></p>
					<p class="c50"><span class="c4">第 2 頁，共 8 頁 </span></p>
					<p class="c22"><span class="c0">司並無事先過濾或審查其內容之義務，對於其合法性，本公司不負任何明示 或默示之承諾或擔保，均由個別用戶自行負責。 </span></p>
					<p class="c26"><span class="c0">2. 用戶提交內容如有違反法令、違背公序良俗、侵害第三人權益、或違反本條 款之虞者，為維護本公司及第三人之權益、或避免損害或爭議擴大，本公司
						得不經事先通知，直接加以移除，並得暫停或終止該用戶使用本服務。 </span></p>
					<p class="c14"><span class="c0">3. 當本公司合理懷疑用戶有不法行為或經第三人通知本公司用戶疑有不法行 為時，用戶瞭解並同意本公司得先行鎖定用戶之帳號，並且主動或配合相關
						單位，移送用戶的相關資料供檢警調查處理，並靜待終局司法判決。 </span></p>
					<p class="c20"><span class="c0">4. 用戶瞭解並同意，本公司依據法律的要求，或基於以下目的之合理必要範圍 內，認定必須將用戶的帳號資訊、提交內容、用戶交易資料加以保存或揭露
						予政府機關、司法警察或未成年人之監護人時，得加以保存及揭露： </span><span class="c5">• </span><span class="c0">遵守法令或政府機關之要求； </span></p>
					<p class="c37"><span class="c5">• </span><span class="c0">為提供本服務所必須者； </span></p>
					<p class="c25"><span class="c5">• </span><span class="c0">為防止他人權益之重大危害而有必要者； </span></p>
					<p class="c7"><span class="c5">• </span><span class="c0">為免除用戶及公眾之生命、身體、自由、權利、財產上之急迫危險者。 5.
						用戶應自行妥善保管帳號及密碼，密碼設定應具有安全性。若非可歸責於本 公司之事由而造成用戶帳號密碼受洩漏，致生用戶受有損害之行為，均與本 公司無關。非可歸責於本公司之事由，包括但不限於：用戶未按照系統提示
						操作本服務、洩漏登入本服務之帳號、密碼或驗證碼等、用戶於本服務之帳 戶、手機或電腦設備遭第三人侵入、用戶使用之手機或電腦設備遺失等。上 開損害將由用戶自行向侵權方追究相關之侵權與賠償責任。 </span></p>
					<p class="c48"><span class="c0">6. 用戶不得基於任何非法目的或以任何非法方式使用本服務，並承諾遵守適用 法律及一切使用網際網路之國際慣例。 </span></p>
					<p class="c43"><span class="c0">7. 用戶不得利用本服務從事侵害本公司或他人權益之行為，否則本公司有權拒 絕提供本服務，且用戶應自行承擔所有相關法律責任。如因用戶之不法行為，
						導致本公司、本公司僱員、其他用戶或本服務協力廠商受有損害者，用戶並 應承擔損害賠償責任。用戶之不當行為包括但不限於： </span></p>
					<p class="c80 c81"><span class="c5">• </span><span class="c0">冒用他人帳戶使用本服務，或刊登他人照片、影像。 </span></p>
					<p class="c16"><span class="c5">• </span><span class="c0">基於非法或本合約禁止之目的而使用本服務。 </span></p>
					<p class="c67"><span class="c5">• </span><span class="c0">基於任何傷人或惡意目的使用本服務。 </span></p>
					<p class="c16"><span class="c5">• </span><span class="c0">基於傷害本服務或本公司之意圖使用本服務。 </span></p>
					<p class="c40"><span class="c5">• </span><span class="c0">基於詐騙、募資意圖，向其他用戶寄發訊息、垃圾郵件等。 </span><span class="c5">•
					</span><span class="c0">利用本服務進行霸凌、跟蹤、威脅、攻擊、騷擾、虐待或毀謗任何人。</span></p>
					<p class="c34"><span class="c4">第 3 頁，共 8 頁 </span></p>
					<p class="c87"><span class="c5">• </span><span class="c0">於本服務刊登任何侵害第三人權益之內容（如刊登照片、影片、文字等），
						包括但不限於名譽、信用、肖像權、隱私權、著作權、商標權或其他智 慧財產權等。 </span></p>
					<p class="c45"><span class="c5">• </span><span class="c0">於本服務刊登任何仇恨性言論、種族歧視、個人、團體或特定族群之歧 視、威脅、裸露、裸體、色情、煽動、暴力、嚇人等內容。
					</span></p>
					<p class="c42"><span class="c5">• </span><span class="c0">自其他使用者基於任何目的而徵求帳號、密碼、基於商業或不法目的， 而請求個人識別資訊或未經當事人同意而散布他人的個人資訊。
					</span><span class="c5">• </span><span class="c0">使用他人的帳號。 </span></p>
					<p class="c77"><span class="c5">• </span><span class="c0">在本公司已終止用戶該帳號的情況下，又新增另外的帳號，除非您已獲 得我方允許。 </span></p>
					<p class="c12"><span class="c5">• </span><span class="c0">自本服務之任何部分刪除任何版權、商標或其他所有權聲明； </span><span class="c5">•
					</span><span class="c0">以其他方式取得本服務數據的任何部分，或者開啟使本服務過度負擔的 程式，或妨礙本服務任何方面的操作及（或）功能； </span></p>
					<p class="c74"><span class="c5">• </span><span class="c0">以任何方式干擾或中斷本服務或伺服器或連結本服務之網路； </span><span class="c5">•
					</span><span class="c0">從事任何可能含有病毒或可能侵害本服務系統、資料之行為； </span><span class="c5">• </span><span
							class="c0">於本服務媒介、仲介、居間或自行從事任何形式之色情、猥褻交易等行 為或提供相關資訊。 </span></p>
					<p class="c81 c89"><span class="c5">• </span><span class="c0">其他本公司有正當理由認為不適當之行為。 </span></p>
					<p class="c49"><span class="c0">8. 用戶有責任自行獲取使用本服務所需的網路數據。如用戶自無線設備造訪或 使用本服務，用戶的行動上網費用、消息傳送速率由用戶自行承擔。用戶並
						應負責獲取和更新使用本服務之應用程式，及其任何更新所需的相容硬體或 設備。本公司不擔保本服務或其任何部分將在任何特定的硬體或設備上成功 運作。此外，本服務亦可能會因使用網際網路和電子通訊而發生故障和延誤。 </span></p>
					<p class="c23"><span class="c1">五、責任之限制與排除 </span></p>
					<p class="c17"><span class="c0">1. 用戶瞭解並同意，本服務係按服務按現況提供。用戶瞭解並同意在適用法律 所許之最大範圍內，自行承擔使用本服務所可能產生之風險。且本公司不保 證下列事項： </span>
					</p>
					<p class="c32"><span class="c5">• </span><span class="c0">本服務得以不受干擾地持續運作； </span></p>
					<p class="c52"><span class="c5">• </span><span class="c0">系統、軟體及資料之錯誤，將被即時發現及更正； </span></p>
					<p class="c83"><span class="c5">• </span><span class="c0">用戶於本服務取得之任何服務或資料將符合用戶的期望； </span></p>
					<p class="c2"><span class="c5">• </span><span class="c0">本服務相關網站或應用程式不含任何瑕疵、程式錯誤、閃退或無法使用等 情形。 </span></p>
					<p class="c66"><span class="c0">2. 本服務之部分內容將須透過網路連接進行。本服務對於技術故障、斷電或任</span></p>
					<p class="c9"><span class="c4">第 4 頁，共 8 頁 </span></p>
					<p class="c46"><span class="c0">何非得由本公司控制之因素所造成之網路連接異常，均不負任何賠償責任。 3. 本公司對於任何用戶因使用本服務有關的直接、特殊、附隨、間接或衍生性
						損害（包括任何利益損失、資料遺失、人身傷害或財產損失），除其發生係 可完全歸責於本公司所致者，本公司不負擔賠償責任。 </span></p>
					<p class="c93"><span class="c0">4. 本服務之部分內容係透過協力廠商之服務所完成。用戶瞭解並同意，本公司 可能會為提供本服務之目的，而將透過本服務蒐集取得之資料與該協力廠商 共享。 </span></p>
					<p class="c71"><span class="c0">5. 用戶瞭解並同意，於使用本服務過程中所有之資料記錄，均以本服務系統所 儲存之電子資料為準。如遇有糾紛或訴訟，用戶瞭解並同意以本服務系統所 記錄之電子資料為認定標準。
					</span></p>
					<p class="c23"><span class="c1">六、協力廠商 </span></p>
					<p class="c86"><span class="c0">1. 本公司可通過本服務內提供之協力廠商廣告、行銷內容及其他機制（以下合稱 「</span><span class="c1">協力廠商廣告</span><span
							class="c0">」）來補貼服務及（或）獲得額外收入。透過同意使用本服務， 視為用戶瞭解並同意接受協力廠商廣告。如用戶選擇不接收協力廠商廣告，本 公司有權向用戶收取額外費用，或得拒絕用戶使用本服務。 </span></p>
					<p class="c55"><span class="c0">2. 用戶瞭解並同意，協力廠商就其所提供之產品或服務可能另有適用之服務條 款及政策。本公司對此類協力廠商之產品、服務和內容不負審核義務，且在任
						何情況下，本公司對此類協力廠商服務提供之任何產品、服務和內容將不承擔 任何責任。 </span></p>
					<p class="c27"><span class="c0">3. 用戶使用 Apple iOS、Android 行動設備開發之應用程式造訪本服務，Apple  Inc.、Google
						Inc.及（或）其適用之關係企業並非本條款締約當事方。用戶使 用這些設備以造訪本服務，將受到上述協力廠商之服務條款所約束，本公司對 Apple Inc.、Google Inc.及（或）其適用之關係企業提供之任何產品、服務和
						內容將不承擔任何責任。 </span></p>
					<p class="c23"><span class="c1">七、系統維護及服務停止 </span></p>
					<p class="c33"><span class="c0">1. 本服務將盡力提升服務內容，以提供額外之功能予用戶。本服務將不時進行 更新、新增、改善服務內容，包含移除某些功能，然如上該行為將不影響用
						戶權益，本服務將不會事前通知用戶。 </span></p>
					<p class="c28"><span class="c0">2. 如發生下列情形之一時，本公司有權停止本服務之提供，且不會事先通知用 戶，用戶不得因此請求賠償或補償： </span></p>
					<p class="c47"><span class="c5">• </span><span class="c0">對本服務相關軟硬體設備進行搬遷、更換、升級、保養或維修時。</span></p>
					<p class="c36"><span class="c4">第 5 頁，共 8 頁 </span></p>
					<p class="c85"><span class="c5">• </span><span class="c0">因第三人之行為、非本公司所得以控制之事項、或其他不可歸責於本公 司之事由所致之本服務暫停或無法正常運作。 </span>
					</p>
					<p class="c47"><span class="c5">• </span><span class="c0">天災或其他不可抗力所致之服務暫停或無法正常運作。 </span></p>
					<p class="c25"><span class="c5">• </span><span class="c0">因主管機關、司法機關或法律要求。 </span></p>
					<p class="c13"><span class="c5">• </span><span class="c0">因本公司自身業務考量之本服務終止，包括但不限於商業合併、公司解 散等。 </span></p>
					<p class="c61"><span class="c5">• </span><span class="c0">為因應緊急而應即時應變之情況。 </span></p>
					<p class="c78"><span class="c0">3. 本服務停止或發生故障等情形時，可能造成用戶使用上的不便、資料喪失或 其他經濟上損失等情形。用戶於使用本服務時宜自行採取防護措施。本公司
						對於用戶因使用（或無法使用）本服務而造成的損害概不負任何賠償責任。 </span></p>
					<p class="c23"><span class="c1">八、智慧財產權的保護 </span></p>
					<p class="c65"><span class="c0">1. 本服務所提供之軟體或應用程式、網站所有內容，包括但不限於著作、圖片、 檔案、資訊、資料、程式架構、應用程式或網站畫面的安排、網站架構、頁
						面設計等，均由本公司或其他權利人依法擁有其智慧財產權，包括但不限於 商標權、專利權、著作權、營業秘密與專有技術等。用戶不得逕自使用、修 改、重製、公開播送、改作、散布、發行、公開發表、進行還原工程、解編
						或反向組譯。若用戶欲引用或轉載前述軟體、程式或網站內容，必須依法取 得本公司或其他權利人的事前書面同意。如有違反，用戶應對本公司負損害 賠償責任（包括但不限於訴訟費用及律師費用等）。 </span></p>
					<p class="c59"><span class="c0">2. 本條款之存在並無向用戶轉讓、或授予用戶以任何方式使用或引用本公司的 公司名稱、標誌、產品和服務名稱、商標或服務標誌之權利。 3.
						對於用戶提供到本服務之提交內容，本公司並不聲明擁有所有權。然而，用 戶在張貼、上傳、輸入、提供或提交該內容時，即表示永久且不可撤回的授 予本公司得基於行銷及商業目的使用之，包含但不限於對於用戶的提交內容
						進行重製、散布、傳輸、公開展示、公開演出、編輯、翻譯等，以及公開與 用戶的名字及（或）暱稱相互連結。 </span></p>
					<p class="c60"><span class="c0">4. 用戶在張貼、上傳、輸入、提供或提交內容時，應擔保其擁有所有提交該內 容之權利，包括但不限於所有用戶有權提供、張貼、上傳、輸入或提交該內
						容所需之權利。否則若因此遭第三方主張侵害其權利者，本公司除得隨時移 除提交內容外，如因此導致本公司受有損害者，用戶應向本公司負損害賠償 責任（包括但不限於訴訟費用及律師費用等）。 </span></p>
					<p class="c23"><span class="c1">九、 個人資料之保護</span></p>
					<p class="c9"><span class="c4">第 6 頁，共 8 頁 </span></p>
					<p class="c63"><span class="c0">當用戶使用本服務時，用戶會向本公司提供相關的個人資料或其他資料。本公司 於蒐集個人資料或其他資料時，將依個人資料保護法及相關法令之規定為之，並
						僅於提供本服務之目的範圍內使用。關於更詳盡的說明，請參閱本服務之隱私權 政策（連結）。 </span></p>
					<p class="c68"><span class="c1">十、拒絕或終止使用 </span></p>
					<p class="c88"><span class="c0">1. 用戶可隨時以任何原因結束個人帳號。 </span></p>
					<p class="c6"><span class="c0">2. 用戶瞭解並同意，本公司得全權決定，於通知或未通知用戶之情形下，隨時 終止、拒絕或限制用戶使用本服務，或變更、限制、終止全部或一部本服務
						之內容（包括但不限於：用戶於一定期間內未使用本服務、本公司無法繼續 提供本服務、無法預期之技術或安全因素、用戶利用本服務從事違法行為、 其他經本公司認定用戶已違反本條款規定、違反本服務操作提示、本條款之 精神或脫法行為等）。
					</span></p>
					<p class="c29"><span class="c0">3. 用戶瞭解並同意，於前一項之情形時，本公司得立即關閉、刪除或限制存取 用戶的帳號及用戶帳號中全部或部分資料及紀錄。此外，本公司對用戶或任 何第三人均不因此承擔任何責任。
					</span></p>
					<p class="c69"><span class="c0">4. 用戶瞭解並同意，於本條第二項之情形時，本公司不因此承擔任何損害賠償 責任。如用戶因此對本公司或第三人造成損害者，並應負損害賠償責任（包
						括但不限於訴訟費用及律師費用等）。 </span></p>
					<p class="c68"><span class="c1">十一、 其他事項 </span></p>
					<p class="c79"><span class="c0">1. 非經本公司事前書面同意，用戶不得將本條款所定之權利義務全部或一部轉 讓予他人。 </span></p>
					<p class="c57"><span class="c0">2. 本公司有權將本條款所定之權利義務、本服務之系統資料全部或部分轉讓予 他人，包括但不限於： </span></p>
					<p class="c37"><span class="c5">• </span><span class="c0">轉讓予關係企業； </span></p>
					<p class="c35"><span class="c5">• </span><span class="c0">轉讓予本公司的股份或資產收購方； </span></p>
					<p class="c25"><span class="c5">• </span><span class="c0">轉讓予與本公司合併之存續公司。 </span></p>
					<p class="c11"><span class="c0">3. 除本條款所約定之內容外，本條款締約方間並不存在任何代理、合資或合作關 係。 </span></p>
					<p class="c95"><span class="c0">4. 如用戶有違反本條款之情形，而本公司未請求用戶依本條款履約者，不得視為 或構成本公司放棄請求用戶依本條款履約之權利。 </span></p>
					<p class="c41"><span class="c0">5. 本條款所有與本公司責任限制與排除相關之約款，或與用戶承擔風險、擔保和 賠償責任相關之約款，於本條款對該用戶終止後仍然有效。且在本條款對該用</span></p>
					<p class="c56"><span class="c4">第 7 頁，共 8 頁 </span></p>
					<p class="c31"><span class="c0">戶終止時，用戶尚未支付的所有應付款項視為到期款項，用戶應於本條款經終 止時向本公司給付之。 </span></p>
					<p class="c39"><span class="c0">6. 如用戶對於本條款、本服務有任何疑義，或權利受到用戶侵害者，得透過本公 司網站上公告之客服方式聯繫，本公司將儘速處理。 </span></p>
					<p class="c53"><span class="c1">十二、 準據法、管轄法院 </span></p>
					<p class="c17"><span class="c0">1. 若本條款的任何部分依相關法律規定被認定為無效或無法執行的，就該無效 或無法執行的部分，將被視為被一個有效可執行、且與原意圖最相符的條款
						所取代，而本條款的其餘部分仍然有效。 </span></p>
					<p class="c44"><span class="c0">2. 本條款及其相關補充條款、處理原則、政策、及相關服務說明之解釋，均以 中華民國法律為準據法。 </span></p>
					<p class="c72"><span class="c0">3. 用戶與本公司因本服務、本條款或其相關服務說明所生之爭議，應以臺灣臺 北地方法院為合意管轄法院。 </span></p>
					<p class="c80"><span class="c0">4. 本條轉各標題僅為查閱而設，不影響本條款之解釋及說明。</span></p>
				</div>
					<DIV class="text-center">
						<A href="/signUp.asp" class="btn primary-gradient text-white text-bold m-0 w-80 py-2">開始註冊</A>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>