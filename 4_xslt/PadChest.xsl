<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">

        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>PadChest - Radiographies</title>

                <style>
                    body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f6f9;
                    margin: 30px;
                    color: #333;
                    }

                    h1 {
                    text-align: center;
                    color: #23466b;
                    }

                    table {
                    width: 100%;
                    border-collapse: collapse;
                    background-color: white;
                    }

                    th {
                    background-color: #23466b;
                    color: white;
                    padding: 12px;
                    text-align: left;
                    position: sticky;
                    top: 0;
                    }

                    td {
                    padding: 10px;
                    border-bottom: 1px solid #ddd;
                    }

                    tr:nth-child(even) {
                    background-color: #f2f5f8;
                    }

                    tr:hover {
                    background-color: #e1ecf7;
                    }

                    details {
                    min-width: 180px;
                    }

                    summary {
                    cursor: pointer;
                    color: #23466b;
                    font-weight: bold;
                    }

                    details p {
                    margin: 8px 0;
                    line-height: 1.5;
                    overflow-wrap: anywhere;
                    }
                </style>
            </head>

            <body>
                <h1>Liste des radiographies</h1>

                <table>
                    <tr>
                        <th>Patient</th>
                        <th>Naissance</th>
                        <th>Sexe</th>
                        <th>Image</th>
                        <th>Projection</th>
                        <th>Labels</th>
                        <th>Détails</th>
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


                            <td>
                                <details>
                                    <summary>Voir les détails</summary>

                                    <p><strong>Étude :</strong>
                                        <xsl:value-of select="../@id"/>
                                    </p>

                                    <p><strong>Répertoire image :</strong>
                                        <xsl:value-of select="ImageDir"/>
                                    </p>

                                    <p><strong>Méthode de projection :</strong>
                                        <xsl:value-of select="MethodProjection"/>
                                    </p>

                                    <p><strong>Pediatric :</strong>
                                        <xsl:value-of select="Pediatric"/>
                                    </p>

                                    <p><strong>Méthode de labellisation :</strong>
                                        <xsl:value-of select="MethodLabel"/>
                                    </p>

                                    <p><strong>Localisations :</strong>
                                        <xsl:value-of select="Localizations"/>
                                    </p>

                                    <p><strong>Labels par phrase :</strong>
                                        <xsl:value-of select="LabelsLocalizationsBySentence"/>
                                    </p>

                                    <p><strong>Label CUIS :</strong>
                                        <xsl:value-of select="LabelCUIS"/>
                                    </p>

                                    <p><strong>Localisations CUIS :</strong>
                                        <xsl:value-of select="LocalizationsCUIS"/>
                                    </p>

                                    <p><strong>Identifiant du rapport :</strong>
                                        <xsl:value-of select="@rapportRef"/>
                                    </p>

                                    <p><strong>Rapport médical :</strong>
                                        <xsl:variable name="refRapport" select="@rapportRef"/>
                                        <xsl:value-of select="/PadChest/Rapports/Rapport[@id = $refRapport]"/>
                                    </p>
                                </details>
                            </td>

                        </tr>
                    </xsl:for-each>

                </table>
            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>