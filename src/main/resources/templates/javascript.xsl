<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output
		encoding="UTF-8"
		media-type="text/html"
		method="html"
		indent="no"
		omit-xml-declaration="yes"
	/>

	<xsl:template match="/">
		<SCRIPT>
			<xsl:copy>
				<xsl:value-of select="document" disable-output-escaping="yes"/>
			</xsl:copy>
		</SCRIPT>
	</xsl:template>
</xsl:stylesheet>