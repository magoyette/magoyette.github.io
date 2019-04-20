(ns marcandregoyette.translations
  (:require [tongue.core :as tongue]))

(def dictionaries
  {:en {:post
        {:written-on "Written on "}
        :footer
        {:license-name
         "Creative Commons License"
         :license-url
         "https://creativecommons.org/licenses/by-nd/4.0/"
         :license-sentence
         "This work by Marc-Andr\u00E9 Goyette is licensed under a "
         :disclaimer
         (str "Opinions and views expressed on this site are solely my own, "
              "not those of my present or past employers.")}}
   :fr {:post
        {:written-on "Écrit le "}
        :footer
        {:license-name
         "licence Creative Commons"
         :license-url
         "https://creativecommons.org/licenses/by-nd/4.0/deed.fr"
         :license-sentence
         (str "Cette création de Marc-Andr\u00E9 Goyette est mise à disposition "
              "selon les termes d'une ")
         :disclaimer
         (str "Les opinions et points de vus exprimés sur ce site sont "
              "uniquement les miens, et ne sont pas ceux de mes employeurs "
              "actuels ou passés.")}}})

;; tongue builds a translate function with the definition:
;; [locale key & args] => string
(def translate-function
  (tongue/build-translate dictionaries))

(defn translate [lang key & args]
  (translate-function (keyword lang) key args))
