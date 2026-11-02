for $patient in db:get("PadChest")//Patient
where $patient/Etude/Image[
    tokenize(
        translate(string(Labels), "[]'", ""),
        ","
    ) ! normalize-space(.) = "pneumonia"
]
order by $patient/Naissance
return concat(
    $patient/@id,
    " : ",
        format-number(number($patient/Naissance), "0")

)