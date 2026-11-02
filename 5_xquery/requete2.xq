let $labels :=
    for $image in db:get("PadChest")//Image
    for $label in tokenize(
        translate(string($image/Labels), "[]'", ""),
        ","
    )
    return normalize-space($label)

return subsequence(
    for $label in $labels
    where $label != ""
    group by $nom := $label
    let $nombre := count($label)
    order by $nombre descending
    return concat($nom, " : ", $nombre),
    1,
    10
)