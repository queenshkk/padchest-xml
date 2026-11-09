declare variable $labelRecherche external := "pneumonia";

for $patient in db:get("PadChest")//Patient
where $patient/Etude/Image[
    tokenize(
        translate(string(Labels), "[]'", ""),
        ","
    ) ! normalize-space(.) = $labelRecherche
]
order by $patient/Naissance empty greatest
return concat(
    $patient/@id,
    " : ",
    if ($patient/Naissance) then string($patient/Naissance) else "naissance inconnue"
)