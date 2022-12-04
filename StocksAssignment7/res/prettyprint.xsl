<!--  Source: https://www.baeldung.com/java-pretty-print-xml-->
<!--  As of Java 9, the Transformer class's pretty-print feature doesn't define the actual format.
Therefore, whitespace-only nodes are outputted as well. Therefore, this stylesheet has been
implemented from the above source (this is just for formatting and does not implement or affect any
of the Stock Application's functionality-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:strip-space elements="*"/>
    <xsl:output method="xml" encoding="UTF-8"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
