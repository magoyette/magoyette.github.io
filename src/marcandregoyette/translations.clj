(ns marcandregoyette.translations
  (:require [tongue.core :as tongue]))

(def dictionaries
  {:en {:post
        {:written-on "Written on "}
        :footer
        {:license-name
         "Creative Commons License (CC BY-ND 4.0)"
         :license-url
         "https://creativecommons.org/licenses/by-nd/4.0/"
         :license-sentence
         "The content of this site is licensed under a "
         :disclaimer
         (str "Opinions expressed on this site are solely my own, "
              "not those of my present or past employers.")}}
   :fr {:post
        {:written-on "Écrit le "}
        :footer
        {:license-name
         "licence Creative Commons (CC BY-ND 4.0)"
         :license-url
         "https://creativecommons.org/licenses/by-nd/4.0/deed.fr"
         :license-sentence
         (str "Le contenu de ce site est disponible selon les termes d'une ")
         :disclaimer
         (str "Les opinions exprimées sur ce site sont uniquement les miennes "
              "et ne représentent pas celles de mes employeurs actuels ou "
              "passés.")}}})

;; tongue builds a translate function with the definition:
;; [locale key & args] => string
(def translate-function
  (tongue/build-translate dictionaries))

(defn translate [lang key & args]
  (translate-function (keyword lang) key args))
