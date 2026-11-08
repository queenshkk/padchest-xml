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