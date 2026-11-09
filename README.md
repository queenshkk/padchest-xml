# Projet PadChest - XML et BD avancées
## 1. Présentation
Dans ce projet, nous avons dû convertir un fichier CSV contenant des informations sur des radiographies en un fichier XML.
Ensuite, nous avons utilisé une DTD pour vérifier la structure du XML, SAX pour calculer des statistiques, XSLT pour afficher les données en HTML et BaseX pour faire des requêtes XQuery.

## 2. Structure du XML
Nous avons organisé notre XML de cette manière :

PadChest --> Patient --> Etude --> Image

Un patient peut avoir plusieurs études et une étude peut contenir plusieurs images.

### Problèmes rencontrés avec le CSV
Pendant la conversion, nous avons dû gérer plusieurs problèmes :
- Certains enregistrements du CSV occupent plusieurs lignes. Nous les avons donc regroupés avant de les traiter.
- Si l'identifiant du patient ou de l'étude est manquant, l'image est ignorée.
- Pour la naissance et le sexe, quand la valeur est `nan`, nous n'écrivons pas l'élément dans le XML. Ils sont donc facultatifs dans la DTD et le XSD.
- Pour les autres champs, nous gardons la valeur du CSV telle quelle.
- Les années de naissance sont écrites `1930.0` dans le CSV. Nous les avons transformées en `1930`, car l'énoncé demande le format YYYY.
- Notre programme ignore un dernier enregistrement incomplet. Avec le CSV complet, il n'y en a pas.
- Nous avons trouvé deux projections qui n'étaient pas indiquées dans l'énoncé : UNK (8 images) et EXCLUDE (11 images). Nous les avons conservées.
- Le sexe `O` existe dans les données. Nous l'avons gardé.
- Un même patient peut avoir plusieurs années de naissance ou plusieurs sexes selon les lignes. Nous gardons la valeur de la première image rencontrée pour ce patient.
- Nous avons aussi remplacé les caractères spéciaux pour éviter les erreurs dans le XML.

### Justification de nos choix de modélisation
Nous avons choisi une structure hiérarchique Patient → Etude → Image, car un patient peut avoir plusieurs études et une étude peut contenir plusieurs images. Cela permet d'éviter de répéter les informations du patient pour chaque radiographie.
Nous avons placé les rapports médicaux dans un élément Rapports séparé, car plusieurs images peuvent partager le même rapport. Dans notre extrait de 200 images, il n'y a que 130 rapports différents. Cette organisation permet de stocker chaque rapport une seule fois.
Nous avons utilisé des attributs id de type ID pour identifier les éléments et un attribut rapportRef de type IDREF pour relier chaque image à son rapport médical.
Enfin, nous avons choisi de représenter les identifiants et les références par des attributs XML, tandis que les informations comme la naissance, le sexe, la projection et les labels sont représentées par des éléments XML. Les labels, les localisations et les codes CUI sont gardés sous forme de texte, comme dans le CSV.

## 3. Exécution du projet
Nous avons utilisé Java, Maven, Saxon-HE et BaseX.

Le fichier CSV doit être placé à la racine du projet.

- Pour compiler le projet, ouvrir un terminal dans le dossier du projet et exécuter :

.\mvnw.cmd clean compile

- Pour lancer les programmes depuis le terminal :

Conversion CSV vers XML (crée PadChest.xml) :
.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.conversion.ConversionXML"

Génération de l'extrait XML (200 images, crée extrait.xml) :
.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.xslt.GenerationExtrait"

Validation DTD et statistiques SAX :
.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.sax.ValidationSAX"

Validation XSD :
.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.xsd.ValidationXSD"

Validation DTD et statistiques DOM :
.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.dom.ValidationDOM"

Transformation XSLT (crée 4_xslt/resultat.html à partir de extrait.xml) :
.\mvnw.cmd exec:java "-Dexec.mainClass=hepl.xml.laboxml.xslt.TransformationXSLT"

Nous avons aussi exécuté les programmes directement dans IntelliJ IDEA, dans cet ordre :
- ConversionXML.java pour créer PadChest.xml.
- GenerationExtrait.java pour créer extrait.xml contenant 200 images et leurs rapports associés.
- ValidationSAX.java pour vérifier le XML et afficher les statistiques.
- ValidationXSD.java pour valider le XML avec le schéma XSD.
- ValidationDOM.java pour valider la DTD et calculer les statistiques avec DOM.
- TransformationXSLT.java pour créer 4_xslt/resultat.html à partir de extrait.xml.

- Pour les validations XSD et DOM, nous pouvons choisir de vérifier PadChest.xml ou extrait.xml en modifiant le nom du fichier dans le programme.
- Pour XQuery, nous avons créé une base de données PadChest dans BaseX avec PadChest.xml (Database > New), puis exécuté les quatre requêtes du dossier 5_xquery.
- Pour RESTXQ, nous avons copié padchest-rest.xqm dans le dossier webapp de BaseX et lancé « BaseX HTTP Server (Start) ».

## 4. Résultats
**Statistiques SAX et DOM (mêmes résultats) :**
- Images : 160 861
- Patients distincts : 67 625
- Images contenant `loc right` : 30 123

Les résultats des programmes sont dans 3_parsing/Resultats_SAX.txt et 3_parsing/Resultats_DOM.txt.

**Images par projection :**

| Projection | Nombre |
|---|---:|
| PA | 91 728 |
| L | 49 579 |
| AP_horizontal | 14 346 |
| AP | 4 559 |
| COSTAL | 630 |
| EXCLUDE | 11 |
| UNK | 8 |

**Top 10 des labels :**

| Label | Nombre |
|---|---:|
| normal | 50 616 |
| COPD signs | 23 280 |
| cardiomegaly | 15 028 |
| unchanged | 14 349 |
| aortic elongation | 10 824 |
| pleural effusion | 10 026 |
| scoliosis | 8 335 |
| pneumonia | 8 037 |
| interstitial pattern | 7 831 |
| chronic changes | 7 343 |

**Résultats XQuery :**
- Requête 1 : 30 123 images contenant loc right.
- Requête 2 : les 10 labels les plus fréquents. Nous avons retrouvé les mêmes résultats qu'avec SAX.
- Requête 3 : 4 133 patients avec le label pneumonia, triés par année de naissance. Le label est un paramètre de la requête.
- Requête 4 : les 10 associations entre labels et localisations les plus fréquentes, à partir de LabelsLocalizationsBySentence. Nous avons associé les valeurs présentes dans une même phrase, et chaque paire est comptée une fois par phrase.

| Paire | Nombre |
|---|---:|
| cardiomegaly \| loc cardiac | 16 714 |
| normal \| loc cardiac | 14 145 |
| pleural effusion \| loc pleural | 11 017 |
| aortic elongation \| loc aortic | 10 953 |
| normal \| loc right | 9 117 |
| costophrenic angle blunting \| loc costophrenic angle | 6 679 |
| normal \| loc left | 6 457 |
| pleural effusion \| loc right | 6 296 |
| exclude \| loc right | 5 697 |
| pleural effusion \| loc left | 5 370 |

### Observations sur les données
L'analyse du fichier PadChest nous a permis de faire plusieurs observations :

- Nous avons obtenu **160 861 images pour 67 625 patients**. Certains patients possèdent donc plusieurs radiographies.
- La projection **PA est la plus fréquente**, avec 91 728 images, suivie de la projection L avec 49 579 images.
- Le label **normal** est le plus fréquent, avec 50 616 occurrences.
- Nous avons trouvé **30 123 images** contenant la localisation `loc right`.
- Certaines associations entre labels et localisations sont particulièrement fréquentes, par exemple `cardiomegaly` avec `loc cardiac` (16 714 occurrences) et `aortic elongation` avec `loc aortic` (10 953 occurrences).
- Plusieurs images partagent le même rapport (130 rapports pour 200 images dans l'extrait), ce qui justifie notre utilisation de `ID/IDREF`.

Nous avons aussi constaté que certaines données nécessitaient un traitement particulier : les projections `UNK` et `EXCLUDE`, des valeurs de naissance comme `1930.0`, ainsi que des valeurs de sexe comme `O` ou `nan`.

## 5. Fonctionnalités supplémentaires — Niveau PRO
**Validation XSD**
Nous avons créé un fichier XSD pour mieux vérifier notre XML.
Nous avons ajouté des règles pour les champs Naissance, Sexe, Projection, Pediatric, MethodProjection, MethodLabel et ImageDir.Nous avons aussi utilisé xs:key et xs:keyref pour vérifier que chaque image fait référence à un rapport médical existant.
La DTD vérifie la structure mais ne peut pas contrôler le texte des éléments. Le XSD ajoute les énumérations et les types.
Les listes de labels, de localisations et de codes sont restées en xs:string, car ce sont des listes écrites en texte dans le CSV.
En validant le fichier complet, nous avons trouvé la projection EXCLUDE, qui n'était pas prévue. Nous l'avons ajoutée au XSD.
Nous avons testé le fichier XML complet avec ValidationXSD.java. La validation a réussi.

**Analyse DOM**
Nous avons créé ValidationDOM.java pour lire le XML avec DOM.
Nous avons calculé les mêmes statistiques : le nombre d'images, les patients distincts, les images avec loc right, les projections et le top 10 des labels.
Nous avons obtenu les mêmes résultats qu'avec SAX.
SAX lit le fichier de haut en bas et ne garde que ses compteurs, alors que DOM construit tout l'arbre du document en mémoire. DOM demande donc beaucoup plus de mémoire.
Sur notre ordinateur, DOM a fonctionné avec les réglages par défaut de Java. Sur un ordinateur avec moins de mémoire, il pourrait s'arrêter avec une erreur OutOfMemoryError, alors que SAX fonctionnerait toujours.

**Amélioration XSLT**
Nous avons amélioré notre page HTML en ajoutant du CSS dans le fichier XSLT.
Nous avons ajouté des couleurs, des espacements et des titres de colonnes qui restent visibles quand on descend dans la page.
Nous avons également ajouté un bouton « Voir les détails » pour chaque image. Il permet d'afficher les informations complémentaires et le rapport médical associé, sans surcharger le tableau principal.

**RESTXQ avec BaseX**
Nous avons utilisé le serveur HTTP de BaseX pour afficher des résultats XQuery dans un navigateur.
Nous avons créé le fichier padchest-rest.xqm et testé 6 adresses :
- http://localhost:8080/padchest/images : 160 861 images
- http://localhost:8080/padchest/patients : 67 625 patients
- http://localhost:8080/padchest/right : 30 123 images avec loc right
- http://localhost:8080/padchest/labels : les 10 labels les plus fréquents
- http://localhost:8080/padchest/patients/pneumonia : la liste des patients ayant le label pneumonia (on peut changer le label dans l'adresse)
- http://localhost:8080/padchest/paires : les 10 paires (label, localisation) les plus fréquentes

## 6. Répartition du travail
- Dang Quynh Nguyen : (à compléter)
- Guillaumy Ntema'A Sonfack Wamba : (à compléter)

## 7. Utilisation de l'IA
Nous avons utilisé ChatGPT pour nous aider à comprendre certaines notions du cours et à corriger des erreurs dans notre code.
Par exemple, ChatGPT nous a aidés à gérer les enregistrements du CSV sur plusieurs lignes et la dernière ligne incomplète.
Nous l'avons aussi utilisé pour comprendre et corriger nos requêtes XQuery, notre fichier XSD, le programme DOM et RESTXQ.
ChatGPT nous a également aidés à comprendre les références ID/IDREF, à organiser les rapports médicaux partagés et à créer la génération de extrait.xml avec XSLT.
Pour la version finale, nous avons aussi utilisé Claude pour relire le projet. Il nous a signalé les problèmes (CSV tronqué, copies de code en double, extrait trop gros, XSD trop permissif) et nous a expliqué comment les corriger. Nous avons fait les modifications nous-mêmes dans IntelliJ, puis nous avons tout retesté (programmes Java, BaseX et adresses RESTXQ).