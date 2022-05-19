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
				Youngme陪玩須知
			</TITLE>
			<xsl:call-template name="headLinkTags"/>

			<style type="text/css">
				ol.lst-kix_qk6k1tvty1hf-5 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-6 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-7 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-8 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-1 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-2 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-3 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-4 {
				list-style-type: none
				}

				.lst-kix_yvqc3zdfhx8n-7>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-7
				}

				.lst-kix_bukj9yunqdtp-2>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-2
				}

				.lst-kix_4g3mv9s074cy-8>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-8
				}

				.lst-kix_2i391xhm6ry-4>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-4
				}

				.lst-kix_4g3mv9s074cy-6>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-6
				}

				.lst-kix_qk6k1tvty1hf-1>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-1
				}

				.lst-kix_yvqc3zdfhx8n-5>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-5
				}

				.lst-kix_2i391xhm6ry-6>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-6
				}

				ul.lst-kix_g4duux1resp7-4 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-4.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-4 0
				}

				ul.lst-kix_g4duux1resp7-5 {
				list-style-type: none
				}

				ul.lst-kix_g4duux1resp7-2 {
				list-style-type: none
				}

				ul.lst-kix_g4duux1resp7-3 {
				list-style-type: none
				}

				ul.lst-kix_g4duux1resp7-8 {
				list-style-type: none
				}

				ul.lst-kix_g4duux1resp7-6 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-5.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-5 0
				}

				ul.lst-kix_g4duux1resp7-7 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-5.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-5 0
				}

				ol.lst-kix_qk6k1tvty1hf-0 {
				list-style-type: none
				}

				ul.lst-kix_g4duux1resp7-0 {
				list-style-type: none
				}

				ul.lst-kix_g4duux1resp7-1 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-0.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-0 0
				}

				ol.lst-kix_2i391xhm6ry-6.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-6 0
				}

				.lst-kix_qk6k1tvty1hf-3>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-3
				}

				ol.lst-kix_qk6k1tvty1hf-6.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-6 0
				}

				ol.lst-kix_4g3mv9s074cy-7 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-8 {
				list-style-type: none
				}

				.lst-kix_qk6k1tvty1hf-8>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-8, lower-roman) ". "
				}

				.lst-kix_qk6k1tvty1hf-7>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-7, lower-latin) ". "
				}

				ol.lst-kix_4g3mv9s074cy-5 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-6 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-3 {
				list-style-type: none
				}

				.lst-kix_qk6k1tvty1hf-4>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-4, lower-latin) ". "
				}

				.lst-kix_qk6k1tvty1hf-6>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-6, decimal) ". "
				}

				ol.lst-kix_4g3mv9s074cy-4 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-1 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-2 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-4.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-4 0
				}

				.lst-kix_qk6k1tvty1hf-5>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-5, lower-roman) ". "
				}

				ol.lst-kix_4g3mv9s074cy-0 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-5.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-5 0
				}

				ol.lst-kix_qk6k1tvty1hf-0.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-0 0
				}

				.lst-kix_2i391xhm6ry-8>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-8
				}

				.lst-kix_qk6k1tvty1hf-0>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-0, decimal) ". "
				}

				.lst-kix_qk6k1tvty1hf-2>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-2, lower-roman) ". "
				}

				.lst-kix_qk6k1tvty1hf-3>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-3, decimal) ". "
				}

				.lst-kix_bukj9yunqdtp-4>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-4
				}

				.lst-kix_2i391xhm6ry-2>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-2
				}

				.lst-kix_qk6k1tvty1hf-1>li:before {
				content: ""counter(lst-ctn-kix_qk6k1tvty1hf-1, decimal) ") "
				}

				ol.lst-kix_4g3mv9s074cy-1.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-1 0
				}

				.lst-kix_yvqc3zdfhx8n-1>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-1
				}

				.lst-kix_c6va3znsqoro-8>li:before {
				content: "  "
				}

				.lst-kix_4g3mv9s074cy-2>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-2
				}

				.lst-kix_qk6k1tvty1hf-5>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-5
				}

				ol.lst-kix_bukj9yunqdtp-6.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-6 0
				}

				.lst-kix_c6va3znsqoro-4>li:before {
				content: "  "
				}

				.lst-kix_c6va3znsqoro-2>li:before {
				content: "  "
				}

				.lst-kix_c6va3znsqoro-6>li:before {
				content: "  "
				}

				ol.lst-kix_qk6k1tvty1hf-1.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-1 0
				}

				ol.lst-kix_bukj9yunqdtp-3.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-3 0
				}

				.lst-kix_2i391xhm6ry-0>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-0
				}

				ol.lst-kix_2i391xhm6ry-7.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-7 0
				}

				.lst-kix_4g3mv9s074cy-0>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-0
				}

				ol.lst-kix_bukj9yunqdtp-7 {
				list-style-type: none
				}

				.lst-kix_qk6k1tvty1hf-6>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-6
				}

				ol.lst-kix_bukj9yunqdtp-8 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-5 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-6 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-8.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-8 0
				}

				ol.lst-kix_bukj9yunqdtp-3 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-4 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-1 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-2 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-0 {
				list-style-type: none
				}

				.lst-kix_yvqc3zdfhx8n-0>li:before {
				content: ""counter(lst-ctn-kix_yvqc3zdfhx8n-0, decimal) ") "
				}

				ol.lst-kix_qk6k1tvty1hf-3.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-3 0
				}

				.lst-kix_yvqc3zdfhx8n-2>li:before {
				content: ""counter(lst-ctn-kix_yvqc3zdfhx8n-2, lower-roman) ") "
				}

				.lst-kix_yvqc3zdfhx8n-4>li:before {
				content: "("counter(lst-ctn-kix_yvqc3zdfhx8n-4, lower-latin) ") "
				}

				.lst-kix_2i391xhm6ry-1>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-1
				}

				.lst-kix_bukj9yunqdtp-5>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-5
				}

				.lst-kix_bukj9yunqdtp-0>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-0
				}

				.lst-kix_bukj9yunqdtp-6>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-6
				}

				.lst-kix_2i391xhm6ry-7>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-7
				}

				.lst-kix_bukj9yunqdtp-1>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-1, decimal) "). "
				}

				.lst-kix_c6va3znsqoro-0>li:before {
				content: "  "
				}

				ol.lst-kix_4g3mv9s074cy-0.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-0 0
				}

				ol.lst-kix_bukj9yunqdtp-5.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-5 0
				}

				ol.lst-kix_qk6k1tvty1hf-4.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-4 0
				}

				.lst-kix_bukj9yunqdtp-7>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-7, lower-latin) ". "
				}

				.lst-kix_yvqc3zdfhx8n-0>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-0
				}

				.lst-kix_yvqc3zdfhx8n-8>li:before {
				content: ""counter(lst-ctn-kix_yvqc3zdfhx8n-8, lower-roman) ". "
				}

				.lst-kix_yvqc3zdfhx8n-6>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-6
				}

				.lst-kix_4g3mv9s074cy-1>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-1
				}

				.lst-kix_yvqc3zdfhx8n-6>li:before {
				content: ""counter(lst-ctn-kix_yvqc3zdfhx8n-6, decimal) ". "
				}

				.lst-kix_4g3mv9s074cy-7>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-7
				}

				.lst-kix_bukj9yunqdtp-5>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-5, lower-roman) ". "
				}

				.lst-kix_bukj9yunqdtp-3>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-3, decimal) ". "
				}

				ol.lst-kix_2i391xhm6ry-8.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-8 0
				}

				ol.lst-kix_4g3mv9s074cy-8.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-8 0
				}

				ol.lst-kix_yvqc3zdfhx8n-6.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-6 0
				}

				.lst-kix_qk6k1tvty1hf-0>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-0
				}

				ol.lst-kix_qk6k1tvty1hf-8.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-8 0
				}

				.lst-kix_bukj9yunqdtp-3>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-3
				}

				.lst-kix_g4duux1resp7-3>li:before {
				content: "  "
				}

				.lst-kix_yvqc3zdfhx8n-8>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-8
				}

				.lst-kix_2i391xhm6ry-3>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-3
				}

				ol.lst-kix_bukj9yunqdtp-7.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-7 0
				}

				.lst-kix_g4duux1resp7-0>li:before {
				content: "  "
				}

				.lst-kix_g4duux1resp7-4>li:before {
				content: "  "
				}

				.lst-kix_2i391xhm6ry-2>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-2, lower-roman) ". "
				}

				.lst-kix_2i391xhm6ry-3>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-3, decimal) ". "
				}

				.lst-kix_g4duux1resp7-1>li:before {
				content: "  "
				}

				.lst-kix_g4duux1resp7-2>li:before {
				content: "  "
				}

				.lst-kix_2i391xhm6ry-5>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-5, lower-roman) ". "
				}

				.lst-kix_2i391xhm6ry-4>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-4, lower-latin) ". "
				}

				.lst-kix_2i391xhm6ry-6>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-6, decimal) ". "
				}

				.lst-kix_g4duux1resp7-8>li:before {
				content: "  "
				}

				.lst-kix_g4duux1resp7-7>li:before {
				content: "  "
				}

				.lst-kix_2i391xhm6ry-8>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-8, lower-roman) ". "
				}

				ol.lst-kix_qk6k1tvty1hf-2.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-2 0
				}

				.lst-kix_2i391xhm6ry-7>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-7, lower-latin) ". "
				}

				.lst-kix_g4duux1resp7-5>li:before {
				content: "  "
				}

				ol.lst-kix_4g3mv9s074cy-2.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-2 0
				}

				.lst-kix_g4duux1resp7-6>li:before {
				content: "  "
				}

				ul.lst-kix_c6va3znsqoro-0 {
				list-style-type: none
				}

				ul.lst-kix_c6va3znsqoro-1 {
				list-style-type: none
				}

				ul.lst-kix_c6va3znsqoro-2 {
				list-style-type: none
				}

				ul.lst-kix_c6va3znsqoro-3 {
				list-style-type: none
				}

				ul.lst-kix_c6va3znsqoro-8 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-3.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-3 0
				}

				.lst-kix_yvqc3zdfhx8n-4>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-4
				}

				.lst-kix_4g3mv9s074cy-5>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-5
				}

				ul.lst-kix_c6va3znsqoro-4 {
				list-style-type: none
				}

				ul.lst-kix_c6va3znsqoro-5 {
				list-style-type: none
				}

				ul.lst-kix_c6va3znsqoro-6 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-1.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-1 0
				}

				ul.lst-kix_c6va3znsqoro-7 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-1.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-1 0
				}

				.lst-kix_4g3mv9s074cy-7>li:before {
				content: ""counter(lst-ctn-kix_4g3mv9s074cy-7, lower-latin) ". "
				}

				.lst-kix_4g3mv9s074cy-6>li:before {
				content: ""counter(lst-ctn-kix_4g3mv9s074cy-6, decimal) ". "
				}

				ol.lst-kix_yvqc3zdfhx8n-7.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-7 0
				}

				.lst-kix_2i391xhm6ry-1>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-1, decimal) ") "
				}

				.lst-kix_2i391xhm6ry-0>li:before {
				content: ""counter(lst-ctn-kix_2i391xhm6ry-0, decimal) ". "
				}

				ol.lst-kix_bukj9yunqdtp-8.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-8 0
				}

				.lst-kix_4g3mv9s074cy-8>li:before {
				content: ""counter(lst-ctn-kix_4g3mv9s074cy-8, lower-roman) ". "
				}

				.lst-kix_qk6k1tvty1hf-4>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-4
				}

				.lst-kix_4g3mv9s074cy-1>li:before {
				content: ""counter(lst-ctn-kix_4g3mv9s074cy-1, lower-latin) ") "
				}

				.lst-kix_qk6k1tvty1hf-7>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-7
				}

				ol.lst-kix_bukj9yunqdtp-2.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-2 0
				}

				.lst-kix_4g3mv9s074cy-3>li:before {
				content: "("counter(lst-ctn-kix_4g3mv9s074cy-3, decimal) ") "
				}

				.lst-kix_4g3mv9s074cy-2>li:before {
				content: ""counter(lst-ctn-kix_4g3mv9s074cy-2, lower-roman) ") "
				}

				.lst-kix_bukj9yunqdtp-7>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-7
				}

				.lst-kix_4g3mv9s074cy-5>li:before {
				content: "("counter(lst-ctn-kix_4g3mv9s074cy-5, lower-roman) ") "
				}

				.lst-kix_4g3mv9s074cy-4>li:before {
				content: "("counter(lst-ctn-kix_4g3mv9s074cy-4, lower-latin) ") "
				}

				ol.lst-kix_yvqc3zdfhx8n-0.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-0 0
				}

				.lst-kix_2i391xhm6ry-5>li {
				counter-increment: lst-ctn-kix_2i391xhm6ry-5
				}

				ol.lst-kix_qk6k1tvty1hf-7.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-7 0
				}

				.lst-kix_4g3mv9s074cy-0>li:before {
				content: ""counter(lst-ctn-kix_4g3mv9s074cy-0, decimal) ") "
				}

				.lst-kix_bukj9yunqdtp-1>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-1
				}

				.lst-kix_bukj9yunqdtp-8>li {
				counter-increment: lst-ctn-kix_bukj9yunqdtp-8
				}

				ol.lst-kix_2i391xhm6ry-1.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-1 0
				}

				ol.lst-kix_yvqc3zdfhx8n-2.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-2 0
				}

				.lst-kix_c6va3znsqoro-7>li:before {
				content: "  "
				}

				.lst-kix_c6va3znsqoro-1>li:before {
				content: "  "
				}

				.lst-kix_c6va3znsqoro-5>li:before {
				content: "  "
				}

				ol.lst-kix_2i391xhm6ry-4.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-4 0
				}

				.lst-kix_c6va3znsqoro-3>li:before {
				content: "  "
				}

				ol.lst-kix_2i391xhm6ry-8 {
				list-style-type: none
				}

				.lst-kix_4g3mv9s074cy-3>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-3
				}

				ol.lst-kix_2i391xhm6ry-7 {
				list-style-type: none
				}

				ol.lst-kix_bukj9yunqdtp-0.start {
				counter-reset: lst-ctn-kix_bukj9yunqdtp-0 0
				}

				ol.lst-kix_yvqc3zdfhx8n-5.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-5 0
				}

				ol.lst-kix_2i391xhm6ry-2 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-1 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-0 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-6 {
				list-style-type: none
				}

				.lst-kix_yvqc3zdfhx8n-2>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-2
				}

				ol.lst-kix_2i391xhm6ry-5 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-4 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-3 {
				list-style-type: none
				}

				ol.lst-kix_2i391xhm6ry-3.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-3 0
				}

				.lst-kix_qk6k1tvty1hf-8>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-8
				}

				.lst-kix_yvqc3zdfhx8n-3>li:before {
				content: "("counter(lst-ctn-kix_yvqc3zdfhx8n-3, decimal) ") "
				}

				.lst-kix_yvqc3zdfhx8n-5>li:before {
				content: "("counter(lst-ctn-kix_yvqc3zdfhx8n-5, lower-roman) ") "
				}

				.lst-kix_bukj9yunqdtp-8>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-8, lower-roman) ". "
				}

				ol.lst-kix_4g3mv9s074cy-6.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-6 0
				}

				.lst-kix_yvqc3zdfhx8n-1>li:before {
				content: ""counter(lst-ctn-kix_yvqc3zdfhx8n-1, lower-latin) ") "
				}

				ol.lst-kix_yvqc3zdfhx8n-4.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-4 0
				}

				ol.lst-kix_2i391xhm6ry-2.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-2 0
				}

				ol.lst-kix_yvqc3zdfhx8n-3.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-3 0
				}

				.lst-kix_bukj9yunqdtp-0>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-0, decimal) ". "
				}

				.lst-kix_bukj9yunqdtp-2>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-2, lower-roman) ". "
				}

				ol.lst-kix_4g3mv9s074cy-7.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-7 0
				}

				ol.lst-kix_yvqc3zdfhx8n-0 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-1 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-2 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-3 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-4 {
				list-style-type: none
				}

				.lst-kix_bukj9yunqdtp-6>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-6, decimal) ". "
				}

				ol.lst-kix_yvqc3zdfhx8n-5 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-6 {
				list-style-type: none
				}

				li.li-bullet-0:before {
				margin-left: -18pt;
				white-space: nowrap;
				display: inline-block;
				min-width: 18pt
				}

				ol.lst-kix_yvqc3zdfhx8n-7 {
				list-style-type: none
				}

				ol.lst-kix_yvqc3zdfhx8n-8 {
				list-style-type: none
				}

				.lst-kix_yvqc3zdfhx8n-3>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-3
				}

				.lst-kix_yvqc3zdfhx8n-7>li:before {
				content: ""counter(lst-ctn-kix_yvqc3zdfhx8n-7, lower-latin) ". "
				}

				.lst-kix_qk6k1tvty1hf-2>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-2
				}

				.lst-kix_bukj9yunqdtp-4>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-4, lower-latin) ". "
				}

				.lst-kix_4g3mv9s074cy-4>li {
				counter-increment: lst-ctn-kix_4g3mv9s074cy-4
				}

				ol {
				margin: 0;
				padding: 0
				}

				table td,
				table th {
				padding: 0
				}

				.c3 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 20pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c8 {
				padding-top: 20pt;
				padding-bottom: 6pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: center
				}

				.c1 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 16pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c12 {
				padding-top: 16pt;
				padding-bottom: 4pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c2 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 11pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c13 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 16pt;
				font-family: "Arial";
				font-style: normal
				}

				.c14 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 11pt;
				font-family: "Arial";
				font-style: normal
				}

				.c5 {
				padding-top: 18pt;
				padding-bottom: 6pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c11 {
				color: #434343;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 14pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c9 {
				color: #ff0000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 11pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c0 {
				padding-top: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c6 {
				<!--				background-color: #ffffff;-->
				<!--				max-width: 451.4pt;-->
				<!--				padding: 72pt 72pt 72pt 72pt-->
				}

				.c10 {
				padding: 0;
				margin: 0
				}

				.c4 {
				margin-left: 36pt;
				padding-left: 0pt
				}

				.c7 {
				height: 11pt
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
					<H4 class="text-center text-primary">Youngme 使用者條款</H4>
					<DIV class="c6">

						<h1 class="c8" id="h.vai39ld9lwk"><span class="c3">Youngme陪玩須知</span></h1>
						<h2 class="c5" id="h.wivyogekgdf9"><span class="c1">服務內容</span></h2>
						<p class="c0"><span class="c2">本站養蜜提供陪伴媒合服務，租賃陪玩師一小段的療癒時光，與包養及八大相關產業性質不同，不提供任何性相關服務，因此為了防止發生意外，禁止在封閉空間進行約會。</span></p>
						<h3 class="c12" id="h.4km40m2nyakk"><span class="c11">基本服務</span></h3>
						<ul class="c10 lst-kix_g4duux1resp7-0 start">
							<li class="c0 c4 li-bullet-0"><span class="c2">陪伴及約會</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">勾手臂、牽手、摟肩、摟腰及摸頭</span></li>
						</ul>
						<p class="c0"><span class="c2">           * 根據陪玩師不同，提供不同服務</span></p>
						<p class="c0"><span class="c2">           * 禁止上述以外的身體接觸與性交易</span></p>
						<p class="c0 c7"><span class="c14"></span></p>
						<h2 class="c5" id="h.7ns3agtkvazh"><span class="c13">工作須知</span></h2>
						<ol class="c10 lst-kix_bukj9yunqdtp-0 start" start="1">
							<li class="c0 c4 li-bullet-0"><span class="c2">養蜜陪玩師需滿18歲</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">須完成安全認證再進行提領薪水</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">無須輪班可打工兼職，時間您自由安排</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">不得脫離平台交易，通過任何其他途徑進行非平台訂單交易，因此產生糾紛怒不負責</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">不得透過本平台服務販賣商品或散佈商業廣告</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">嚴禁在封閉空間進行約會，例如 : 住宅、旅館、私人包廂、汽車等私人空間</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">性服務和過度的身體接觸完全禁止</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">不得進行非法相關行為</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">若在接洽時，客戶發送不當訊息，請提高警覺酌情接單</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c9">不得侵犯客戶隱私，例如 : 索取私人通訊方式</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">不得提供個人資料，例如 : 銀行帳戶、身分證等</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">若您與客戶互動間有任何疑慮，請隨時向客服諮詢</span></li>
							<li class="c0 c4 li-bullet-0"><span class="c2">若您與客戶陪玩過程中，客戶進行不恰當行為，例如 : 騷擾、強制進行非約定的行程等，請立即通報客服</span></li>
						</ol>
						<p class="c0 c7"><span class="c2"></span></p>
					</DIV>
				</DIV>

				<DIV class="text-center">
					<A onClick="javascript :history.back(1);" class="btn primary-gradient text-white text-bold m-0 w-80 py-2">回上一頁</A>
				</DIV>

			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>