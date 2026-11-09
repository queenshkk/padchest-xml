<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" encoding="UTF-8" indent="yes"
                doctype-system="2_structure/PadChest.dtd"/>

    <xsl:template match="/">
        <PadChest>
            <Rapports>
                <xsl:copy-of select="
                    /PadChest/Rapports/Rapport[
                        @id = (/PadChest/Patient/Etude/Image)[position() &lt;= 200]/@rapportRef                    ]"/>
            </Rapports>

            <xsl:for-each select="/PadChest/Patient[Etude/Image]">
                <xsl:variable name="imagesAvant"
                              select="count(preceding-sibling::Patient/Etude/Image)"/>

                <xsl:if test="$imagesAvant &lt; 200">
                    <Patient>
                        <xsl:copy-of select="@* | Naissance | Sexe"/>

                        <xsl:for-each select="Etude">
                            <xsl:variable name="etude" select="."/>

                            <xsl:if test="Image[
                                count($etude/preceding-sibling::Etude/Image)
                                + count($etude/../preceding-sibling::Patient/Etude/Image)
                                + count(preceding-sibling::Image) &lt; 200
                            ]">
                                <Etude>
                                    <xsl:copy-of select="@*"/>
                                    <xsl:copy-of select="Image[
                                        count($etude/preceding-sibling::Etude/Image)
                                        + count($etude/../preceding-sibling::Patient/Etude/Image)
                                        + count(preceding-sibling::Image) &lt; 200
                                    ]"/>
                                </Etude>
                            </xsl:if>
                        </xsl:for-each>
                    </Patient>
                </xsl:if>
            </xsl:for-each>
        </PadChest>
    </xsl:template>

</xsl:stylesheet>