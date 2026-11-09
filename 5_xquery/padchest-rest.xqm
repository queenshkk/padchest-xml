module namespace pad = "http://padchest.hepl.be/rest";

declare
  %rest:path("/padchest/bonjour")
  %rest:GET
  %output:method("text")
function pad:bonjour() {
  "Bonjour depuis PadChest RESTXQ !"
};

declare
  %rest:path("/padchest/images")
  %rest:GET
  %output:method("text")
function pad:images() {
  count(db:get("PadChest")//Image)
};

declare
  %rest:path("/padchest/patients")
  %rest:GET
  %output:method("text")
function pad:patients() {
  count(db:get("PadChest")//Patient)
};

declare
  %rest:path("/padchest/right")
  %rest:GET
  %output:method("text")
function pad:right() {
  count(
    db:get("PadChest")//Image[
      tokenize(
        translate(
          normalize-space(Localizations),
          "[]'",
          ""
        ),
        "\s*,\s*"
      ) = "loc right"
    ]
  )
};

declare
  %rest:path("/padchest/labels")
  %rest:GET
  %output:method("text")
function pad:labels() {
  let $labels :=
    for $image in db:get("PadChest")//Image
    for $label in tokenize(translate(string($image/Labels), "[]'", ""), ",")
    return normalize-space($label)
  return string-join(
    subsequence(
      for $label in $labels
      where $label != ""
      group by $nom := $label
      let $nombre := count($label)
      order by $nombre descending
      return concat($nom, " : ", $nombre),
      1,
      10
    ),
    "&#10;"
  )
};

declare
  %rest:path("/padchest/patients/{$label}")
  %rest:GET
  %output:method("text")
function pad:patients-par-label($label as xs:string) {
  string-join(
    for $patient in db:get("PadChest")//Patient
    where $patient/Etude/Image[
      tokenize(translate(string(Labels), "[]'", ""), ",") ! normalize-space(.) = $label
    ]
    order by $patient/Naissance empty greatest
    return concat(
      $patient/@id,
      " : ",
      if ($patient/Naissance) then string($patient/Naissance) else "naissance inconnue"
    ),
    "&#10;"
  )
};

declare
  %rest:path("/padchest/paires")
  %rest:GET
  %output:method("text")
function pad:paires() {
  let $paires :=
    for $image in db:get("PadChest")//Image
    let $texte := string($image/LabelsLocalizationsBySentence)
    for $groupe in tokenize($texte, "\],\s*\[")
    let $valeurs := tokenize(translate($groupe, "[]'", ""), ",") ! normalize-space(.)
    for $label in $valeurs
    for $localisation in $valeurs
    where $label != ""
      and not(starts-with($label, "loc "))
      and starts-with($localisation, "loc ")
    return concat($label, " | ", $localisation)
  return string-join(
    subsequence(
      for $paire in $paires
      group by $nom := $paire
      let $nombre := count($paire)
      order by $nombre descending
      return concat($nom, " : ", $nombre),
      1,
      10
    ),
    "&#10;"
  )
};