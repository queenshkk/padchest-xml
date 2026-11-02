let $paires :=
    for $image in db:get("PadChest")//Image
    let $texte := string($image/LabelsLocalizationsBySentence)

    for $groupe in tokenize($texte, "\],\s*\[")
    let $valeurs := tokenize(
        translate($groupe, "[]'", ""),
        ","
    ) ! normalize-space(.)

    for $label in $valeurs
    for $localisation in $valeurs

    where $label != ""
      and not(starts-with($label, "loc "))
      and starts-with($localisation, "loc ")

    return concat($label, " | ", $localisation)

return subsequence(
    for $paire in $paires
    group by $nom := $paire
    let $nombre := count($paire)
    order by $nombre descending
    return concat($nom, " : ", $nombre),
    1,
    10
)