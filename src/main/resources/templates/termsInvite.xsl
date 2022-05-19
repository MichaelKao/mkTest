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
				ol.lst-kix_jtb7ikstxfg-8.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-8 0
				}

				.lst-kix_jtb7ikstxfg-7>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-7
				}

				ol.lst-kix_awu13of27gcm-2.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-2 0
				}

				.lst-kix_5br7g3r5ctuk-7>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-7
				}

				ol.lst-kix_5br7g3r5ctuk-4.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-4 0
				}

				.lst-kix_2pz2nrumza1n-5>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-5
				}

				ol.lst-kix_jl4njug3qzp8-3.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-3 0
				}

				ol.lst-kix_u2d48aryzgd4-2.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-2 0
				}

				.lst-kix_u2d48aryzgd4-1>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-1, decimal) "). "
				}

				.lst-kix_u2d48aryzgd4-0>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-0, decimal) ". "
				}

				.lst-kix_u2d48aryzgd4-2>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-2, lower-roman) ". "
				}

				.lst-kix_u2d48aryzgd4-4>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-4
				}

				.lst-kix_jl4njug3qzp8-0>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-0
				}

				.lst-kix_awu13of27gcm-0>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-0
				}

				.lst-kix_u2d48aryzgd4-6>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-6, decimal) ". "
				}

				.lst-kix_u2d48aryzgd4-7>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-7, lower-latin) ". "
				}

				ol.lst-kix_5br7g3r5ctuk-6 {
				list-style-type: none
				}

				ol.lst-kix_5br7g3r5ctuk-5 {
				list-style-type: none
				}

				ol.lst-kix_5br7g3r5ctuk-4 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-4.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-4 0
				}

				ol.lst-kix_5br7g3r5ctuk-3 {
				list-style-type: none
				}

				ol.lst-kix_u2d48aryzgd4-8.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-8 0
				}

				ol.lst-kix_5br7g3r5ctuk-2 {
				list-style-type: none
				}

				ol.lst-kix_5br7g3r5ctuk-1 {
				list-style-type: none
				}

				ol.lst-kix_5br7g3r5ctuk-0 {
				list-style-type: none
				}

				.lst-kix_2pz2nrumza1n-3>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-3
				}

				.lst-kix_u2d48aryzgd4-5>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-5, lower-roman) ". "
				}

				ol.lst-kix_jtb7ikstxfg-3.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-3 0
				}

				.lst-kix_u2d48aryzgd4-4>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-4, lower-latin) ". "
				}

				ol.lst-kix_2pz2nrumza1n-0.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-0 0
				}

				.lst-kix_u2d48aryzgd4-3>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-3, decimal) ". "
				}

				ol.lst-kix_5br7g3r5ctuk-8 {
				list-style-type: none
				}

				ol.lst-kix_5br7g3r5ctuk-7 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-8 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-7 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-6 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-5 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-7.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-7 0
				}

				ol.lst-kix_y0d463ggmyv9-4 {
				list-style-type: none
				}

				.lst-kix_jl4njug3qzp8-2>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-2
				}

				.lst-kix_5br7g3r5ctuk-0>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-0
				}

				.lst-kix_awu13of27gcm-0>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-0, decimal) ". "
				}

				ol.lst-kix_y0d463ggmyv9-3 {
				list-style-type: none
				}

				ol.lst-kix_u2d48aryzgd4-7.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-7 0
				}

				ol.lst-kix_y0d463ggmyv9-2 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-5.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-5 0
				}

				.lst-kix_awu13of27gcm-1>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-1, decimal) ") "
				}

				ol.lst-kix_y0d463ggmyv9-1 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-0 {
				list-style-type: none
				}

				.lst-kix_y0d463ggmyv9-8>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-8
				}

				.lst-kix_86mc2wk5ty4g-4>li:before {
				content: "  "
				}

				.lst-kix_86mc2wk5ty4g-6>li:before {
				content: "  "
				}

				.lst-kix_86mc2wk5ty4g-3>li:before {
				content: "  "
				}

				.lst-kix_86mc2wk5ty4g-7>li:before {
				content: "  "
				}

				ol.lst-kix_awu13of27gcm-1.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-1 0
				}

				.lst-kix_2pz2nrumza1n-1>li:before {
				content: ""counter(lst-ctn-kix_2pz2nrumza1n-1, lower-latin) ") "
				}

				.lst-kix_86mc2wk5ty4g-0>li:before {
				content: "  "
				}

				.lst-kix_86mc2wk5ty4g-2>li:before {
				content: "  "
				}

				.lst-kix_86mc2wk5ty4g-8>li:before {
				content: "  "
				}

				.lst-kix_2pz2nrumza1n-0>li:before {
				content: ""counter(lst-ctn-kix_2pz2nrumza1n-0, decimal) ") "
				}

				.lst-kix_2pz2nrumza1n-1>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-1
				}

				.lst-kix_86mc2wk5ty4g-1>li:before {
				content: "  "
				}

				.lst-kix_2pz2nrumza1n-7>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-7
				}

				ol.lst-kix_u2d48aryzgd4-8 {
				list-style-type: none
				}

				.lst-kix_5br7g3r5ctuk-3>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-3
				}

				.lst-kix_2pz2nrumza1n-5>li:before {
				content: "("counter(lst-ctn-kix_2pz2nrumza1n-5, lower-roman) ") "
				}

				.lst-kix_2pz2nrumza1n-6>li:before {
				content: ""counter(lst-ctn-kix_2pz2nrumza1n-6, decimal) ". "
				}

				ol.lst-kix_u2d48aryzgd4-7 {
				list-style-type: none
				}

				.lst-kix_u2d48aryzgd4-2>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-2
				}

				ol.lst-kix_u2d48aryzgd4-6 {
				list-style-type: none
				}

				ol.lst-kix_u2d48aryzgd4-5 {
				list-style-type: none
				}

				.lst-kix_awu13of27gcm-2>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-2
				}

				.lst-kix_5br7g3r5ctuk-7>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-7, lower-latin) ". "
				}

				.lst-kix_5br7g3r5ctuk-6>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-6, decimal) ". "
				}

				ol.lst-kix_u2d48aryzgd4-0 {
				list-style-type: none
				}

				.lst-kix_2pz2nrumza1n-2>li:before {
				content: ""counter(lst-ctn-kix_2pz2nrumza1n-2, lower-roman) ") "
				}

				ol.lst-kix_jl4njug3qzp8-4.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-4 0
				}

				.lst-kix_u2d48aryzgd4-8>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-8
				}

				.lst-kix_5br7g3r5ctuk-4>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-4, lower-latin) ". "
				}

				ol.lst-kix_awu13of27gcm-8.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-8 0
				}

				.lst-kix_jtb7ikstxfg-5>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-5
				}

				ol.lst-kix_u2d48aryzgd4-4 {
				list-style-type: none
				}

				.lst-kix_2pz2nrumza1n-3>li:before {
				content: "("counter(lst-ctn-kix_2pz2nrumza1n-3, decimal) ") "
				}

				.lst-kix_2pz2nrumza1n-4>li:before {
				content: "("counter(lst-ctn-kix_2pz2nrumza1n-4, lower-latin) ") "
				}

				ol.lst-kix_u2d48aryzgd4-3 {
				list-style-type: none
				}

				.lst-kix_5br7g3r5ctuk-5>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-5, lower-roman) ". "
				}

				ol.lst-kix_u2d48aryzgd4-2 {
				list-style-type: none
				}

				.lst-kix_86mc2wk5ty4g-5>li:before {
				content: "  "
				}

				ol.lst-kix_u2d48aryzgd4-1 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-0 {
				list-style-type: none
				}

				.lst-kix_5br7g3r5ctuk-3>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-3, decimal) ". "
				}

				.lst-kix_y0d463ggmyv9-5>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-5
				}

				ol.lst-kix_2pz2nrumza1n-7.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-7 0
				}

				.lst-kix_jl4njug3qzp8-5>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-5
				}

				.lst-kix_5br7g3r5ctuk-8>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-8, lower-roman) ". "
				}

				.lst-kix_2pz2nrumza1n-7>li:before {
				content: ""counter(lst-ctn-kix_2pz2nrumza1n-7, lower-latin) ". "
				}

				.lst-kix_5br7g3r5ctuk-1>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-1, lower-latin) ". "
				}

				.lst-kix_jl4njug3qzp8-8>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-8, lower-roman) ". "
				}

				ol.lst-kix_y0d463ggmyv9-3.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-3 0
				}

				.lst-kix_y0d463ggmyv9-4>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-4
				}

				ol.lst-kix_2pz2nrumza1n-4.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-4 0
				}

				.lst-kix_jl4njug3qzp8-2>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-2, lower-roman) ". "
				}

				.lst-kix_jl4njug3qzp8-6>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-6, decimal) ". "
				}

				ol.lst-kix_2pz2nrumza1n-6 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-5 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-8 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-7 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-2 {
				list-style-type: none
				}

				ol.lst-kix_y0d463ggmyv9-0.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-0 0
				}

				ol.lst-kix_2pz2nrumza1n-1 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-4 {
				list-style-type: none
				}

				.lst-kix_jl4njug3qzp8-4>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-4, lower-latin) ". "
				}

				ol.lst-kix_2pz2nrumza1n-3 {
				list-style-type: none
				}

				.lst-kix_awu13of27gcm-3>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-3, decimal) ". "
				}

				.lst-kix_awu13of27gcm-5>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-5, lower-roman) ". "
				}

				ol.lst-kix_5br7g3r5ctuk-2.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-2 0
				}

				ol.lst-kix_jtb7ikstxfg-7.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-7 0
				}

				ol.lst-kix_u2d48aryzgd4-4.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-4 0
				}

				.lst-kix_jtb7ikstxfg-0>li:before {
				content: ""counter(lst-ctn-kix_jtb7ikstxfg-0, decimal) ") "
				}

				ol.lst-kix_awu13of27gcm-6.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-6 0
				}

				ol.lst-kix_jl4njug3qzp8-2.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-2 0
				}

				ol.lst-kix_awu13of27gcm-3.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-3 0
				}

				.lst-kix_jtb7ikstxfg-2>li:before {
				content: ""counter(lst-ctn-kix_jtb7ikstxfg-2, lower-roman) ") "
				}

				.lst-kix_jtb7ikstxfg-4>li:before {
				content: "("counter(lst-ctn-kix_jtb7ikstxfg-4, lower-latin) ") "
				}

				ol.lst-kix_u2d48aryzgd4-1.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-1 0
				}

				.lst-kix_2pz2nrumza1n-0>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-0
				}

				.lst-kix_awu13of27gcm-7>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-7, lower-latin) ". "
				}

				.lst-kix_jtb7ikstxfg-6>li:before {
				content: ""counter(lst-ctn-kix_jtb7ikstxfg-6, decimal) ". "
				}

				ol.lst-kix_5br7g3r5ctuk-5.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-5 0
				}

				.lst-kix_y0d463ggmyv9-3>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-3
				}

				.lst-kix_awu13of27gcm-4>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-4
				}

				ol.lst-kix_5br7g3r5ctuk-3.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-3 0
				}

				ol.lst-kix_y0d463ggmyv9-1.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-1 0
				}

				.lst-kix_5br7g3r5ctuk-5>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-5
				}

				.lst-kix_jtb7ikstxfg-8>li:before {
				content: ""counter(lst-ctn-kix_jtb7ikstxfg-8, lower-roman) ". "
				}

				.lst-kix_jtb7ikstxfg-4>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-4
				}

				.lst-kix_u2d48aryzgd4-8>li:before {
				content: ""counter(lst-ctn-kix_u2d48aryzgd4-8, lower-roman) ". "
				}

				.lst-kix_jtb7ikstxfg-3>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-3
				}

				ol.lst-kix_2pz2nrumza1n-6.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-6 0
				}

				.lst-kix_5br7g3r5ctuk-4>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-4
				}

				ol.lst-kix_awu13of27gcm-4.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-4 0
				}

				ol.lst-kix_y0d463ggmyv9-2.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-2 0
				}

				.lst-kix_jl4njug3qzp8-0>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-0, decimal) ". "
				}

				ol.lst-kix_jl4njug3qzp8-1.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-1 0
				}

				.lst-kix_jl4njug3qzp8-4>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-4
				}

				ol.lst-kix_u2d48aryzgd4-3.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-3 0
				}

				ol.lst-kix_2pz2nrumza1n-5.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-5 0
				}

				ol.lst-kix_awu13of27gcm-5.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-5 0
				}

				ol.lst-kix_jl4njug3qzp8-1 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-2 {
				list-style-type: none
				}

				ol.lst-kix_u2d48aryzgd4-5.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-5 0
				}

				.lst-kix_awu13of27gcm-1>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-1
				}

				ol.lst-kix_jl4njug3qzp8-0 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-0.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-0 0
				}

				.lst-kix_y0d463ggmyv9-8>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-8, lower-roman) ". "
				}

				.lst-kix_5br7g3r5ctuk-8>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-8
				}

				.lst-kix_jtb7ikstxfg-6>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-6
				}

				ol.lst-kix_5br7g3r5ctuk-1.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-1 0
				}

				.lst-kix_u2d48aryzgd4-3>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-3
				}

				.lst-kix_u2d48aryzgd4-1>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-1
				}

				.lst-kix_awu13of27gcm-3>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-3
				}

				.lst-kix_y0d463ggmyv9-0>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-0, decimal) ". "
				}

				.lst-kix_y0d463ggmyv9-1>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-1, lower-latin) ". "
				}

				.lst-kix_2pz2nrumza1n-6>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-6
				}

				ol.lst-kix_2pz2nrumza1n-3.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-3 0
				}

				ol.lst-kix_jl4njug3qzp8-6.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-6 0
				}

				ol.lst-kix_y0d463ggmyv9-7.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-7 0
				}

				.lst-kix_y0d463ggmyv9-4>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-4, lower-latin) ". "
				}

				.lst-kix_y0d463ggmyv9-5>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-5, lower-roman) ". "
				}

				.lst-kix_y0d463ggmyv9-2>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-2, lower-roman) ". "
				}

				.lst-kix_y0d463ggmyv9-3>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-3, decimal) ". "
				}

				.lst-kix_y0d463ggmyv9-6>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-6, decimal) ". "
				}

				.lst-kix_y0d463ggmyv9-7>li:before {
				content: ""counter(lst-ctn-kix_y0d463ggmyv9-7, lower-latin) ". "
				}

				ol.lst-kix_jtb7ikstxfg-0.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-0 0
				}

				.lst-kix_y0d463ggmyv9-0>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-0
				}

				.lst-kix_jl4njug3qzp8-8>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-8
				}

				ul.lst-kix_86mc2wk5ty4g-6 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-2.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-2 0
				}

				ul.lst-kix_86mc2wk5ty4g-7 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-0.start {
				counter-reset: lst-ctn-kix_awu13of27gcm-0 0
				}

				ul.lst-kix_86mc2wk5ty4g-8 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-5.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-5 0
				}

				ol.lst-kix_jtb7ikstxfg-6.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-6 0
				}

				.lst-kix_y0d463ggmyv9-2>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-2
				}

				ol.lst-kix_u2d48aryzgd4-0.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-0 0
				}

				ol.lst-kix_5br7g3r5ctuk-6.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-6 0
				}

				.lst-kix_jl4njug3qzp8-3>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-3
				}

				ol.lst-kix_2pz2nrumza1n-8.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-8 0
				}

				.lst-kix_jl4njug3qzp8-6>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-6
				}

				ol.lst-kix_u2d48aryzgd4-6.start {
				counter-reset: lst-ctn-kix_u2d48aryzgd4-6 0
				}

				.lst-kix_jtb7ikstxfg-2>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-2
				}

				ol.lst-kix_awu13of27gcm-3 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-4 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-5 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-6 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-7 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-8 {
				list-style-type: none
				}

				.lst-kix_awu13of27gcm-8>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-8
				}

				ol.lst-kix_5br7g3r5ctuk-7.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-7 0
				}

				ol.lst-kix_5br7g3r5ctuk-0.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-0 0
				}

				.lst-kix_2pz2nrumza1n-4>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-4
				}

				ol.lst-kix_jtb7ikstxfg-5.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-5 0
				}

				.lst-kix_u2d48aryzgd4-5>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-5
				}

				ol.lst-kix_awu13of27gcm-0 {
				list-style-type: none
				}

				.lst-kix_awu13of27gcm-5>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-5
				}

				.lst-kix_5br7g3r5ctuk-6>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-6
				}

				ol.lst-kix_awu13of27gcm-1 {
				list-style-type: none
				}

				ol.lst-kix_awu13of27gcm-2 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-5 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-6 {
				list-style-type: none
				}

				ul.lst-kix_86mc2wk5ty4g-0 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-3 {
				list-style-type: none
				}

				ul.lst-kix_86mc2wk5ty4g-1 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-4 {
				list-style-type: none
				}

				ul.lst-kix_86mc2wk5ty4g-2 {
				list-style-type: none
				}

				ul.lst-kix_86mc2wk5ty4g-3 {
				list-style-type: none
				}

				.lst-kix_jtb7ikstxfg-8>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-8
				}

				ul.lst-kix_86mc2wk5ty4g-4 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-7 {
				list-style-type: none
				}

				ul.lst-kix_86mc2wk5ty4g-5 {
				list-style-type: none
				}

				ol.lst-kix_jl4njug3qzp8-8 {
				list-style-type: none
				}

				.lst-kix_5br7g3r5ctuk-2>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-2, lower-roman) ". "
				}

				.lst-kix_awu13of27gcm-7>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-7
				}

				ol.lst-kix_5br7g3r5ctuk-8.start {
				counter-reset: lst-ctn-kix_5br7g3r5ctuk-8 0
				}

				ol.lst-kix_y0d463ggmyv9-6.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-6 0
				}

				.lst-kix_5br7g3r5ctuk-2>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-2
				}

				.lst-kix_jtb7ikstxfg-1>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-1
				}

				.lst-kix_5br7g3r5ctuk-0>li:before {
				content: ""counter(lst-ctn-kix_5br7g3r5ctuk-0, decimal) ". "
				}

				ol.lst-kix_jtb7ikstxfg-1.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-1 0
				}

				.lst-kix_2pz2nrumza1n-8>li:before {
				content: ""counter(lst-ctn-kix_2pz2nrumza1n-8, lower-roman) ". "
				}

				ol.lst-kix_jtb7ikstxfg-8 {
				list-style-type: none
				}

				.lst-kix_jl4njug3qzp8-5>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-5, lower-roman) ". "
				}

				ol.lst-kix_jtb7ikstxfg-4 {
				list-style-type: none
				}

				ol.lst-kix_jtb7ikstxfg-4.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-4 0
				}

				.lst-kix_jl4njug3qzp8-3>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-3, decimal) ". "
				}

				.lst-kix_jl4njug3qzp8-7>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-7, lower-latin) ". "
				}

				ol.lst-kix_jtb7ikstxfg-5 {
				list-style-type: none
				}

				ol.lst-kix_jtb7ikstxfg-6 {
				list-style-type: none
				}

				ol.lst-kix_jtb7ikstxfg-7 {
				list-style-type: none
				}

				ol.lst-kix_jtb7ikstxfg-0 {
				list-style-type: none
				}

				ol.lst-kix_jtb7ikstxfg-1 {
				list-style-type: none
				}

				ol.lst-kix_jtb7ikstxfg-2 {
				list-style-type: none
				}

				ol.lst-kix_2pz2nrumza1n-1.start {
				counter-reset: lst-ctn-kix_2pz2nrumza1n-1 0
				}

				ol.lst-kix_jtb7ikstxfg-3 {
				list-style-type: none
				}

				.lst-kix_jtb7ikstxfg-0>li {
				counter-increment: lst-ctn-kix_jtb7ikstxfg-0
				}

				.lst-kix_awu13of27gcm-4>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-4, lower-latin) ". "
				}

				.lst-kix_u2d48aryzgd4-7>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-7
				}

				.lst-kix_awu13of27gcm-2>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-2, lower-roman) ". "
				}

				.lst-kix_awu13of27gcm-6>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-6, decimal) ". "
				}

				.lst-kix_jtb7ikstxfg-1>li:before {
				content: ""counter(lst-ctn-kix_jtb7ikstxfg-1, lower-latin) ") "
				}

				.lst-kix_awu13of27gcm-6>li {
				counter-increment: lst-ctn-kix_awu13of27gcm-6
				}

				.lst-kix_jtb7ikstxfg-3>li:before {
				content: "("counter(lst-ctn-kix_jtb7ikstxfg-3, decimal) ") "
				}

				.lst-kix_jtb7ikstxfg-7>li:before {
				content: ""counter(lst-ctn-kix_jtb7ikstxfg-7, lower-latin) ". "
				}

				.lst-kix_awu13of27gcm-8>li:before {
				content: ""counter(lst-ctn-kix_awu13of27gcm-8, lower-roman) ". "
				}

				.lst-kix_y0d463ggmyv9-6>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-6
				}

				.lst-kix_jtb7ikstxfg-5>li:before {
				content: "("counter(lst-ctn-kix_jtb7ikstxfg-5, lower-roman) ") "
				}

				.lst-kix_2pz2nrumza1n-2>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-2
				}

				ol.lst-kix_y0d463ggmyv9-8.start {
				counter-reset: lst-ctn-kix_y0d463ggmyv9-8 0
				}

				.lst-kix_jl4njug3qzp8-7>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-7
				}

				.lst-kix_y0d463ggmyv9-7>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-7
				}

				.lst-kix_y0d463ggmyv9-1>li {
				counter-increment: lst-ctn-kix_y0d463ggmyv9-1
				}

				.lst-kix_u2d48aryzgd4-0>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-0
				}

				.lst-kix_u2d48aryzgd4-6>li {
				counter-increment: lst-ctn-kix_u2d48aryzgd4-6
				}

				.lst-kix_2pz2nrumza1n-8>li {
				counter-increment: lst-ctn-kix_2pz2nrumza1n-8
				}

				ol.lst-kix_jl4njug3qzp8-8.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-8 0
				}

				ol.lst-kix_jtb7ikstxfg-2.start {
				counter-reset: lst-ctn-kix_jtb7ikstxfg-2 0
				}

				.lst-kix_5br7g3r5ctuk-1>li {
				counter-increment: lst-ctn-kix_5br7g3r5ctuk-1
				}

				.lst-kix_jl4njug3qzp8-1>li:before {
				content: ""counter(lst-ctn-kix_jl4njug3qzp8-1, decimal) ") "
				}

				li.li-bullet-0:before {
				margin-left: -18pt;
				white-space: nowrap;
				display: inline-block;
				min-width: 18pt
				}

				.lst-kix_jl4njug3qzp8-1>li {
				counter-increment: lst-ctn-kix_jl4njug3qzp8-1
				}

				ol.lst-kix_jl4njug3qzp8-7.start {
				counter-reset: lst-ctn-kix_jl4njug3qzp8-7 0
				}

				ol {
				margin: 0;
				padding: 0
				}

				table td,
				table th {
				padding: 0
				}

				.c1 {
				margin-left: 36pt;
				padding-top: 0pt;
				padding-left: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c2 {
				margin-left: 72pt;
				padding-top: 0pt;
				padding-left: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c6 {
				margin-left: 36pt;
				padding-top: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c21 {
				padding-top: 20pt;
				padding-bottom: 6pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: center
				}

				.c11 {
				padding-top: 18pt;
				padding-bottom: 6pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c16 {
				padding-top: 16pt;
				padding-bottom: 4pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c0 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 11pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c7 {
				padding-top: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left;
				height: 11pt
				}

				.c3 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 12pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c14 {
				padding-top: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c12 {
				color: #434343;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 14pt;
				font-style: normal
				}

				.c9 {
				color: #000000;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 16pt;
				font-style: normal
				}

				.c17 {
				color: #000000;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 20pt;
				font-style: normal
				}

				.c13 {
				color: #332500;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 12pt;
				font-style: normal
				}

				.c8 {
				color: #332500;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 11.5pt;
				font-style: normal
				}

				.c10 {
				color: #000000;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 11.5pt;
				font-style: normal
				}

				.c20 {
				}

				.c4 {
				font-weight: 400;
				font-family: "Microsoft JhengHei"
				}

				.c5 {
				padding: 0;
				margin: 0
				}

				.c18 {
				font-size: 11.5pt;
				color: #332500
				}

				.c19 {
				font-size: 12pt
				}

				.c15 {
				margin-left: 72pt
				}

				.title {
				padding-top: 0pt;
				color: #000000;
				font-size: 26pt;
				padding-bottom: 3pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.subtitle {
				padding-top: 0pt;
				color: #666666;
				font-size: 15pt;
				padding-bottom: 16pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
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
				padding-top: 20pt;
				color: #000000;
				font-size: 20pt;
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
				font-size: 16pt;
				padding-bottom: 6pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h3 {
				padding-top: 16pt;
				color: #434343;
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
				padding-top: 14pt;
				color: #666666;
				font-size: 12pt;
				padding-bottom: 4pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h5 {
				padding-top: 12pt;
				color: #666666;
				font-size: 11pt;
				padding-bottom: 4pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				h6 {
				padding-top: 12pt;
				color: #666666;
				font-size: 11pt;
				padding-bottom: 4pt;
				font-family: "Arial";
				line-height: 1.15;
				page-break-after: avoid;
				font-style: italic;
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
					<H4 class="text-center text-primary">服務條款</H4>


				<div class="c20">
					<h2 class="c11" id="h.wivyogekgdf9"><span class="c9 c4">服務內容</span></h2>
					<p class="c14"><span class="c0">本站養蜜提供陪伴媒合服務，租賃陪玩師一小段的療癒時光，與包養及八大相關產業性質不同，不提供任何性相關服務，因此為了防止發生意外，禁止在封閉空間進行約會。</span>
					</p>
					<h3 class="c16" id="h.4km40m2nyakk"><span class="c4 c12">基本服務</span></h3>
					<ul class="c5 lst-kix_86mc2wk5ty4g-0 start">
						<li class="c1 li-bullet-0"><span class="c0">陪伴及約會</span></li>
						<li class="c1 li-bullet-0"><span class="c0">勾手臂、牽手、摟肩、摟腰及摸頭</span></li>
					</ul>
					<p class="c14"><span class="c0">           * 根據陪玩師不同，提供不同服務</span></p>
					<p class="c14"><span class="c0">           * 禁止上述以外的身體接觸和強迫</span></p>
					<h2 class="c11" id="h.8x0gndncfu67"><span class="c4 c9">使用條款</span></h2>
					<h3 class="c16" id="h.z5cdmxrg66bh"><span class="c12 c4">一. 登入</span></h3>
					<p class="c14"><span class="c0">表示您以閱讀並同意此使用者條款。</span></p>
					<p class="c7"><span class="c0"></span></p>
					<h3 class="c16" id="h.yj20hlmra1ry"><span class="c12 c4">二. 使用</span></h3>
					<p class="c14"><span class="c8 c4">希望用戶依照本站規定的方式使用本服務，並同意條款後開始使用。若用戶有以下原因，我們將不再提通服務，並且我們沒有義務揭露原因。</span></p>
					<ol class="c5 lst-kix_jtb7ikstxfg-0 start" start="1">
						<li class="c1 li-bullet-0"><span class="c8 c4">過去曾違反本條款。</span></li>
						<li class="c1 li-bullet-0"><span class="c4 c18">涉及虛假陳述及造假身份。</span></li>
					</ol>
					<p class="c7"><span class="c8 c4"></span></p>
					<h3 class="c16" id="h.4gwkdgnhyevv"><span class="c12 c4">三. 取消和改期</span></h3>
					<ol class="c5 lst-kix_2pz2nrumza1n-0 start" start="1">
						<li class="c1 li-bullet-0"><span class="c8 c4">如果您為自己的方便取消使用本服務，赴約時間24小時內取消我們將無法退回點數，需在赴約前24小時前取消將全額退回。</span></li>
						<li class="c1 li-bullet-0"><span class="c4 c18">付款後，為方便用戶，可更改行程及時間，但須在赴約時間24小時前提出，之後則視同取消。</span></li>
						<li class="c1 li-bullet-0"><span class="c8 c4">因用戶方便而變更日程後1週內未確定日程時，將視為取消全額退回此次的點數。</span></li>
						<li class="c1 li-bullet-0"><span class="c8 c4">用戶未在預約時間抵達集合地點，且在30分鐘內仍無法聯繫，我們將停止服務，並不退回點數。</span></li>
						<li class="c1 li-bullet-0"><span class="c8 c4">天然災害如 : 地震、雷電、火災、停電及疫情等，依政府機關發布內容，酌情退回點數。</span></li>
						<li class="c1 li-bullet-0"><span class="c8 c4">用戶與陪玩師集合後，亦表示媒合完成，將無法退回點數。</span></li>
					</ol>
					<h3 class="c16" id="h.6nxl1z3axios"><span class="c12 c4">四. 禁止</span></h3>
					<ol class="c5 lst-kix_awu13of27gcm-0 start" start="1">
						<li class="c1 li-bullet-0"><span class="c4 c19">我們不允許以下情況的人，使用本服務</span><span class="c4 c18">。</span></li>
					</ol>
					<ol class="c5 lst-kix_awu13of27gcm-1 start" start="1">
						<li class="c2 li-bullet-0"><span class="c3">未滿18歲的用戶</span></li>
						<li class="c2 li-bullet-0"><span class="c3">以性服務為目標的人</span></li>
						<li class="c2 li-bullet-0"><span class="c3">曾違反本站條款的人</span></li>
						<li class="c2 li-bullet-0"><span class="c3">帶有傳染疾病的人</span></li>
						<li class="c2 li-bullet-0"><span class="c3">缺乏控制行為及情緒的人</span></li>
						<li class="c2 li-bullet-0"><span class="c4 c13">被本站判定為與黑幫等反社會勢力有關的人</span></li>
						<li class="c2 li-bullet-0"><span class="c13 c4">涉嫌非法行為及通緝犯</span></li>
						<li class="c2 li-bullet-0"><span class="c13 c4">被本站判定為不適合使用的人</span></li>
					</ol>
					<ol class="c5 lst-kix_awu13of27gcm-0" start="2">
						<li class="c1 li-bullet-0"><span class="c8 c4">用戶在使用本服務時，禁止以下行為。</span></li>
					</ol>
					<ol class="c5 lst-kix_awu13of27gcm-1 start" start="1">
						<li class="c2 li-bullet-0"><span class="c8 c4">違反法律法規或公共秩序和道德的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">犯罪及相關行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">可能干擾本服務運營的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">冒充行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">直接或針對反社會勢力間接受益的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">侵犯陪玩師的隱私和跟踪</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">收集陪玩師的個人信息（包括個人聯繫信息）</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">要求陪玩師提供有關服務的機密信息的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">以竊取與本服務相關的東西為目的的使用行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4"> 邀請或帶入用戶家等封閉房間或私人房間（餐廳除外）的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4"> 未經本站許開車約會(含租賃車)</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4"> 強迫飲酒</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4"> 過度身體接觸等性騷擾</span></li>
						<li class="c2 li-bullet-0"><span class="c4 c10"> 限制或強制銷售的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4"> 未經本站許可，對陪玩師進行聯繫、會面和約會等行為，以及要求和承諾的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">  未經本站許進行採訪、拍攝（包括偷窺）、錄音等行為，或類似行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">  未經本站許推薦用戶食物和飲料的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">  可能會遇到傷害、事故或麻煩的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">  本站認為不適當的其他行為<br/>*
							如果您試圖建議或強制執行這些禁令，本服務將取消，且不予退點，視情況採取報警等法律措施。</span></li>
					</ol>
					<ol class="c5 lst-kix_awu13of27gcm-0" start="3">
						<li class="c1 li-bullet-0"><span class="c8 c4">用戶在使用本服務時，禁止在聊天室中進行以下行為。</span></li>
					</ol>
					<ol class="c5 lst-kix_awu13of27gcm-1 start" start="1">
						<li class="c2 li-bullet-0"><span class="c8 c4">發送不愉快的內容</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">請求性服務</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">侵犯陪玩師私生活的請求，例如: 散布私人通訊方式</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">恐嚇和看似恐嚇的行為</span></li>
						<li class="c2 li-bullet-0"><span class="c8 c4">聯繫與服務相關以外的內容，例如 : 私約見面</span></li>
					</ol>
					<p class="c14"><span class="c8 c4">* 對於做過上述違禁事項的用戶，我們將酌情阻止或拒絕所有陪玩師的接待，此後將停止用戶使用此服務，並無法退回點數。</span></p>
					<p class="c7"><span class="c8 c4"></span></p>
					<h3 class="c16" id="h.5h34g7ivqzwr"><span class="c4">五. </span><span class="c12 c4">停止提供本服務</span></h3>
					<p class="c6"><span class="c8 c4">如果我們確定有以下原因，我們不會事先通知用戶暫停提供全部或部分本服務。</span></p>
					<ol class="c5 lst-kix_jl4njug3qzp8-1 start" start="1">
						<li class="c2 li-bullet-0"><span class="c8 c4">本站相關的系統進行維護、檢查或更新</span></li>
						<li class="c2 li-bullet-0"><span class="c4 c8">因地震、雷電、火災、停電等不可抗力而難以提供本服務</span></li>
					</ol>
					<p class="c14 c15"><span class="c4 c18">* 本店因暫停對客戶或第三方造成任何不利影響或中斷本服務提供，無論何種原因，我們對損害不承擔任何責任。</span></p>
				</div>

				<DIV class="text-center">
					<A onClick="javascript :history.back(1);" class="btn primary-gradient text-white text-bold m-0 w-80 py-2">回上一頁</A>
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