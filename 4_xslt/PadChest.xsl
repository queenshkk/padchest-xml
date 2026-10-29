<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">

        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>PadChest - Radiographies</title>
            </head>

            <body>
                <h1>Liste des radiographies</h1>

                <table border="1">
                    <tr>
                        <th>Patient</th>
                        <th>Naissance</th>
                        <th>Sexe</th>
                        <th>Image</th>
                        <th>Projection</th>
                        <th>Labels</th>
                    </tr>

                    <xsl:for-each select="//Image">
                        <tr>
                            <td>
                                <xsl:value-of select="../../@id"/>
                            </td>

                            <td>
                                <xsl:value-of select="../../Naissance"/>
                            </td>

                            <td>
                                <xsl:value-of select="../../Sexe"/>
                            </td>

                            <td>
                                <xsl:value-of select="@id"/>
                            </td>

                            <td>
                                <xsl:value-of select="Projection"/>
                            </td>

                            <td>
                                <xsl:value-of select="Labels"/>
                            </td>
                        </tr>
                    </xsl:for-each>

                </table>
            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>