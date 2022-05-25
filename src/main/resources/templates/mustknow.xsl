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

				ol.lst-kix_jgczz3gscok8-7.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-7 0
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

				ol.lst-kix_jgczz3gscok8-1.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-1 0
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

				ol.lst-kix_jgczz3gscok8-2.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-2 0
				}

				.lst-kix_jgczz3gscok8-0>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-0
				}

				ol.lst-kix_2i391xhm6ry-6.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-6 0
				}

				.lst-kix_qk6k1tvty1hf-3>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-3
				}

				ul.lst-kix_n1fnbl7wa7dq-5 {
				list-style-type: none
				}

				ol.lst-kix_qk6k1tvty1hf-6.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-6 0
				}

				ul.lst-kix_n1fnbl7wa7dq-6 {
				list-style-type: none
				}

				ul.lst-kix_n1fnbl7wa7dq-7 {
				list-style-type: none
				}

				ul.lst-kix_n1fnbl7wa7dq-8 {
				list-style-type: none
				}

				ul.lst-kix_n1fnbl7wa7dq-1 {
				list-style-type: none
				}

				ul.lst-kix_n1fnbl7wa7dq-2 {
				list-style-type: none
				}

				ul.lst-kix_n1fnbl7wa7dq-3 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-7 {
				list-style-type: none
				}

				ul.lst-kix_n1fnbl7wa7dq-4 {
				list-style-type: none
				}

				ol.lst-kix_4g3mv9s074cy-8 {
				list-style-type: none
				}

				.lst-kix_jgczz3gscok8-5>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-5
				}

				ul.lst-kix_n1fnbl7wa7dq-0 {
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

				ol.lst-kix_jgczz3gscok8-0.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-0 0
				}

				.lst-kix_jgczz3gscok8-3>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-3
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

				.lst-kix_n1fnbl7wa7dq-7>li:before {
				content: "  "
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

				.lst-kix_n1fnbl7wa7dq-3>li:before {
				content: "  "
				}

				.lst-kix_n1fnbl7wa7dq-5>li:before {
				content: "  "
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

				.lst-kix_jgczz3gscok8-4>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-4
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

				.lst-kix_jgczz3gscok8-1>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-1, decimal) "). "
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

				.lst-kix_n1fnbl7wa7dq-1>li:before {
				content: "  "
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

				.lst-kix_jgczz3gscok8-3>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-3, decimal) ". "
				}

				.lst-kix_jgczz3gscok8-7>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-7, lower-latin) ". "
				}

				.lst-kix_bukj9yunqdtp-5>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-5, lower-roman) ". "
				}

				.lst-kix_bukj9yunqdtp-3>li:before {
				content: ""counter(lst-ctn-kix_bukj9yunqdtp-3, decimal) ". "
				}

				.lst-kix_jgczz3gscok8-5>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-5, lower-roman) ". "
				}

				ol.lst-kix_2i391xhm6ry-8.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-8 0
				}

				.lst-kix_jgczz3gscok8-8>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-8
				}

				ol.lst-kix_jgczz3gscok8-0 {
				list-style-type: none
				}

				ol.lst-kix_jgczz3gscok8-1 {
				list-style-type: none
				}

				ol.lst-kix_jgczz3gscok8-2 {
				list-style-type: none
				}

				ol.lst-kix_jgczz3gscok8-4.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-4 0
				}

				ol.lst-kix_4g3mv9s074cy-8.start {
				counter-reset: lst-ctn-kix_4g3mv9s074cy-8 0
				}

				ol.lst-kix_yvqc3zdfhx8n-6.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-6 0
				}

				ol.lst-kix_jgczz3gscok8-3 {
				list-style-type: none
				}

				ol.lst-kix_jgczz3gscok8-4 {
				list-style-type: none
				}

				.lst-kix_qk6k1tvty1hf-0>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-0
				}

				ol.lst-kix_qk6k1tvty1hf-8.start {
				counter-reset: lst-ctn-kix_qk6k1tvty1hf-8 0
				}

				ol.lst-kix_jgczz3gscok8-5 {
				list-style-type: none
				}

				ol.lst-kix_jgczz3gscok8-6 {
				list-style-type: none
				}

				ol.lst-kix_jgczz3gscok8-7 {
				list-style-type: none
				}

				ol.lst-kix_jgczz3gscok8-8 {
				list-style-type: none
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

				.lst-kix_jgczz3gscok8-6>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-6
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

				ol.lst-kix_jgczz3gscok8-3.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-3 0
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

				.lst-kix_jgczz3gscok8-2>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-2
				}

				ol.lst-kix_2i391xhm6ry-4.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-4 0
				}

				.lst-kix_c6va3znsqoro-3>li:before {
				content: "  "
				}

				ol.lst-kix_jgczz3gscok8-8.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-8 0
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

				ol.lst-kix_jgczz3gscok8-5.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-5 0
				}

				.lst-kix_n1fnbl7wa7dq-8>li:before {
				content: "  "
				}

				.lst-kix_n1fnbl7wa7dq-6>li:before {
				content: "  "
				}

				ol.lst-kix_2i391xhm6ry-3.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-3 0
				}

				.lst-kix_jgczz3gscok8-8>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-8, lower-roman) ". "
				}

				.lst-kix_qk6k1tvty1hf-8>li {
				counter-increment: lst-ctn-kix_qk6k1tvty1hf-8
				}

				.lst-kix_n1fnbl7wa7dq-4>li:before {
				content: "  "
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

				ol.lst-kix_jgczz3gscok8-6.start {
				counter-reset: lst-ctn-kix_jgczz3gscok8-6 0
				}

				ol.lst-kix_2i391xhm6ry-2.start {
				counter-reset: lst-ctn-kix_2i391xhm6ry-2 0
				}

				.lst-kix_jgczz3gscok8-7>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-7
				}

				ol.lst-kix_yvqc3zdfhx8n-3.start {
				counter-reset: lst-ctn-kix_yvqc3zdfhx8n-3 0
				}

				.lst-kix_jgczz3gscok8-1>li {
				counter-increment: lst-ctn-kix_jgczz3gscok8-1
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

				.lst-kix_jgczz3gscok8-0>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-0, decimal) ". "
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

				.lst-kix_jgczz3gscok8-4>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-4, lower-latin) ". "
				}

				.lst-kix_n1fnbl7wa7dq-0>li:before {
				content: "  "
				}

				.lst-kix_yvqc3zdfhx8n-3>li {
				counter-increment: lst-ctn-kix_yvqc3zdfhx8n-3
				}

				.lst-kix_yvqc3zdfhx8n-7>li:before {
				content: ""counter(lst-ctn-kix_yvqc3zdfhx8n-7, lower-latin) ". "
				}

				.lst-kix_jgczz3gscok8-2>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-2, lower-roman) ". "
				}

				.lst-kix_jgczz3gscok8-6>li:before {
				content: ""counter(lst-ctn-kix_jgczz3gscok8-6, decimal) ". "
				}

				.lst-kix_n1fnbl7wa7dq-2>li:before {
				content: "  "
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

				.c0 {
				margin-left: 36pt;
				padding-top: 0pt;
				padding-left: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c7 {
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
				font-size: 16pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c11 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 11pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c9 {
				padding-top: 18pt;
				padding-bottom: 6pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c1 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 12pt;
				font-family: "Microsoft JhengHei";
				font-style: normal
				}

				.c4 {
				color: #000000;
				font-weight: 400;
				text-decoration: none;
				vertical-align: baseline;
				font-size: 12pt;
				font-family: "Arial";
				font-style: normal
				}

				.c5 {
				padding-top: 20pt;
				padding-bottom: 6pt;
				line-height: 1.15;
				page-break-after: avoid;
				orphans: 2;
				widows: 2;
				text-align: center
				}

				.c12 {
				padding-top: 0pt;
				padding-bottom: 0pt;
				line-height: 1.15;
				orphans: 2;
				widows: 2;
				text-align: left
				}

				.c13 {
				color: #434343;
				text-decoration: none;
				vertical-align: baseline;
				font-style: normal
				}

				.c8 {
				color: #ff0000;
				text-decoration: none;
				vertical-align: baseline;
				font-style: normal
				}

				.c6 {
				font-size: 12pt;
				font-family: "Microsoft JhengHei";
				font-weight: 400
				}

				.c10 {
				/* background-color: #ffffff;
				max-width: 451.4pt;
				padding: 72pt 72pt 72pt 72pt */
				}

				.c3 {
				padding: 0;
				margin: 0
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
					<div class="c10">
						<h2 class="c9" id="h.wivyogekgdf9"><span class="c1">服務內容</span></h2>
						<p class="c12"><span
								class="c1">本站養蜜提供陪伴媒合服務，租賃陪玩師一小段的療癒時光，與包養及八大相關產業性質不同，不提供任何性相關服務，因此為了防止發生意外，禁止在封閉空間進行約會。</span>
						</p>
						<h3 class="c7" id="h.4km40m2nyakk"><span class="c6 c13">基本服務</span></h3>
						<ul class="c3 lst-kix_n1fnbl7wa7dq-0 start">
							<li class="c0 li-bullet-0"><span class="c1">陪伴及約會</span></li>
							<li class="c0 li-bullet-0"><span
									class="c1">勾手臂、牽手、摟肩、摟腰及摸頭</span>
							</li>
						</ul>
						<p class="c12"><span class="c1">           *
							根據陪玩師不同，提供不同服務</span>
						</p>
						<p class="c12"><span class="c6">           *
							禁止上述以外的身體接觸與性交易</span>
						</p>
						<h2 class="c9" id="h.7ns3agtkvazh"><span class="c4">工作須知</span></h2>
						<ol class="c3 lst-kix_jgczz3gscok8-0 start" start="1">
							<li class="c0 li-bullet-0"><span
									class="c1">養蜜陪玩師需滿18歲</span></li>
							<li class="c0 li-bullet-0"><span
									class="c1">須完成真人認證再進行提領薪水</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">不得脫離平台交易，通過任何其他途徑進行非平台訂單交易，因此產生糾紛恕不負責</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">不得透過本平台服務販賣商品或散佈商業廣告</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">嚴禁在封閉空間進行約會，例如
								:
								住宅、旅館、私人包廂、汽車等私人空間</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">建議定點約會，若無可避免，請搭乘大眾運輸工具或計程車，請勿搭乘私人汽機車，因此產生糾紛恕不負責</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">性服務和過度的身體接觸完全禁止</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c6">不得進行非法相關行為</span></li>
							<li class="c0 li-bullet-0"><span
									class="c1">不得提供個人資料給客戶，例如
								:
								銀行帳戶、身分證、索取私人通訊方式等</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">若您與客戶互動間有任何疑慮，請隨時通知經紀</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">若您與客戶陪玩過程中，客戶進行不恰當行為，例如
								:
								騷擾、強制進行非約定的行程等，請立即通報經紀</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">陪玩費用七天後會透過金流系統匯款至您的帳戶，匯款證明將傳送至您的LINE</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">取消約會請於預約時間24小時前聯繫官方客服</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">確定接單約會後，無故缺席、失蹤放鳥客戶，恕不支付陪玩薪水，第二次出現該情況將終止您的合作</span>
							</li>
							<li class="c0 li-bullet-0"><span
									class="c1">若您約會遲到需補足時數，ex
								: 約會1小時、10點開始，您10 :
								15開始進行陪玩，陪玩時間則需延後至11
								: 15結束</span></li>
							<li class="c0 li-bullet-0"><span
									class="c6">若因電影場次有延後需加時，請立即通知客服或經紀協助客戶預先付清加時費</span>
							</li>
						</ol>
					</div>
				</DIV>

				<DIV class="text-center">
					<A onClick="javascript :history.back(1);" class="btn primary-gradient text-white text-bold m-0 w-40 py-2">回上一頁</A>
				</DIV>

			</DIV>
			<xsl:call-template name="bodyScriptTags"/>
			<xsl:if test="@signIn">
				<SCRIPT src="/SCRIPT/websocket.js"/>
			</xsl:if>
		</BODY>
	</xsl:template>
</xsl:stylesheet>