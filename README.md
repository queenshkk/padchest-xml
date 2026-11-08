# Projet PadChest - XML et BD avancées
## 1. Présentation
Dans ce projet, nous avons dû convertir un fichier CSV contenant des informations sur des radiographies en un fichier XML.
Ensuite, nous avons utilisé une DTD pour vérifier la structure du XML, SAX pour calculer des statistiques, XSLT pour afficher les données en HTML et BaseX pour faire des requêtes XQuery.

## 2. Structure du XML
Nous avons organisé notre XML de cette manière :

PadChest --> Patient --> Etude --> Image

Un patient peut avoir plusieurs études et une étude peut contenir plusieurs images.
Nous avons choisi cette structure pour éviter de répéter les informations du patient à chaque image. 
Nous avons utilisé des attributs id pour identifier les éléments et patientRef pour faire référence au patient.

### Problèmes rencontrés avec le CSV
Pendant la conversion, nous avons dû gérer plusieurs problèmes :
- Certains enregistrements du CSV occupent plusieurs lignes. Nous les avons donc regroupés avant de les traiter.
- Si l'identifiant du patient ou de l'étude est manquant, l'image est ignorée.
- Pour les autres champs manquants, nous gardons des éléments XML vides.
- Le dernier enregistrement du CSV est incomplet. Il est donc ignoré.
- Nous avons trouvé une projection UNK, qui n'était pas indiquée dans l'énoncé. Nous l'avons conservée.
- Nous avons aussi remplacé les caractères spéciaux pour éviter les erreurs dans le XML.

## 3. Exécution du projet
Nous avons utilisé Java, Maven, Saxon-HE et BaseX.

- Pour compiler le projet, ouvrir un terminal dans le dossier du projet et exécuter :

.\mvnw.cmd clean compile

- Pour lancer les programmes depuis le terminal :

        Conversion CSV vers XML :

.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.conversion.ConversionXML"

        Validation DTD et statistiques SAX :

.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.sax.ValidationSAX"

        Transformation XSLT :

.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.xslt.TransformationXSLT"


Nous avons aussi exécuté les programmes directement dans IntelliJ IDEA, dans cet ordre :

- ConversionXML.java pour créer PadChest.xml.

- ValidationSAX.java pour vérifier le XML et afficher les statistiques.

- TransformationXSLT.java pour créer 4_xslt/resultat.html à partir de extrait.xml.

Pour XQuery, nous avons créé une base de données PadChest dans BaseX avec PadChest.xml, puis exécuté les quatre requêtes du dossier 5_xquery.

## 4. Résultats
**Statistiques SAX :**
- Images : 82 702
- Patients distincts : 43 293
- Images contenant `loc right` : 15 204

**Images par projection :**

| Projection | Nombre |
|---|---:|
| PA | 48 620 |
| L | 26 026 |
| AP_horizontal | 6 412 |
| AP | 1 397 |
| COSTAL | 246 |
| UNK | 1 |

**Top 10 des labels :**

| Label | Nombre |
|---|---:|
| normal | 25 633 |
| COPD signs | 12 047 |
| unchanged | 8 857 |
| cardiomegaly | 7 911 |
| aortic elongation | 5 975 |
| chronic changes | 5 482 |
| scoliosis | 5 228 |
| pleural effusion | 4 639 |
| interstitial pattern | 4 140 |
| pneumonia | 3 744 |

**Résultats XQuery :**
- Requête 1 : 15 204 images contenant loc right.
- Requête 2 : les 10 labels les plus fréquents. Nous avons retrouvé les mêmes résultats qu'avec SAX.
- Requête 3 : 2 103 patients avec le label pneumonia, triés par année de naissance.
- Requête 4 : les 10 associations entre labels et localisations les plus fréquentes, à partir de LabelsLocalizationsBySentence. Nous avons associé les valeurs présentes dans un même groupe.

## 5. Fonctionnalités supplémentaires — Niveau PRO
**Validation XSD**
Nous avons créé un fichier XSD pour mieux vérifier notre XML.
Nous avons ajouté des règles pour les champs Naissance, Sexe et Projection. Nous avons aussi utilisé key et keyref pour vérifier les références entre les patients et les études.
Pendant les tests, nous avons trouvé des valeurs comme 1930.0, O et nan. Nous avons donc adapté notre XSD.
Nous avons testé le fichier XML complet avec ValidationXSD.java. La validation a réussi.

**Analyse DOM**
Nous avons créé ValidationDOM.java pour lire le XML avec DOM.
Nous avons calculé les mêmes statistiques : le nombre d'images, les patients distincts, les images avec loc right, les projections et le top 10 des labels.
Nous avons obtenu les mêmes résultats qu'avec SAX.

**Amélioration XSLT**
Nous avons amélioré notre page HTML en ajoutant du CSS dans le fichier XSLT.
Nous avons ajouté des couleurs, des espacements et des titres de colonnes qui restent visibles quand on descend dans la page.

**RESTXQ avec BaseX**
Nous avons utilisé le serveur HTTP de BaseX pour afficher des résultats XQuery dans un navigateur.
Nous avons créé le fichier padchest-rest.xqm et testé 3 adresses :
- http://localhost:8080/padchest/images : 82 702 images
- http://localhost:8080/padchest/patients : 43 293 patients
- http://localhost:8080/padchest/right : 15 204 images avec loc right

## 6. Répartition du travail
- Dang Quynh Nguyen :
- Guillaumy Ntema'A Sonfack Wamba : 

## 7. Utilisation de l'IA
Nous avons utilisé ChatGPT pour nous aider à comprendre certaines notions du cours et à corriger des erreurs dans notre code.
Par exemple, ChatGPT nous a aidés à gérer les enregistrements du CSV sur plusieurs lignes et la dernière ligne incomplète.
Nous l'avons aussi utilisé pour comprendre et corriger nos requêtes XQuery, notre fichier XSD, le programme DOM et RESTXQ.
Nous avons ensuite testé nos programmes dans IntelliJ IDEA, nos requêtes dans BaseX et nos adresses RESTXQ dans le navigateur.