(ns marcandregoyette.translations
  (:require [tongue.core :as tongue]))

(def dictionaries
  {:en
   {:page
    {:description
     (str "Essays written by Marc-André about software development, "
          "interactive fiction and litterature.")}
    :article
    {:written-on "Written on "
     :all-articles-link "All articles"
     :tag-link "Articles with the tag "}
    :articles
    {:for-tag " with the tag "}
    :tags
    {:tags-list "List of all tags"
     :tags-page "Tags"
     :tags-page-text
     (str "List of articles by tag: ")}
    :feeds
    {:feed-page-url "/en/feeds"
     :feed-page "Atom/RSS Feeds"
     :feed-page-text
     (str "Atom feeds (also compatible with RSS software) "
          "for articles in English.")}
    :footer
    {:source-code
     "Source code"
     :license-name
     "Creative Commons License (CC BY-ND 4.0)"
     :license-url
     "https://creativecommons.org/licenses/by-nd/4.0/"
     :license-sentence
     "The content of this site is licensed under a "
     :disclaimer
     (str "Opinions expressed on this site are solely my own, "
          "not those of my present or past employers.")}}
   :fr
   {:page
    {:description
     (str "Essais de Marc-André Goyette sur le développement logiciel, "
          "la fiction interactive et la littérature.")}
    :article
    {:written-on "Écrit le "
     :all-articles-link "Tous les articles"
     :tag-link "Articles avec le tag "}
    :articles
    {:for-tag " avec le tag "}
    :tags
    {:tags-list "Liste de tous les tags"
     :tags-page "Tags"
     :tags-page-text
     (str "Listes des articles par tag: ")}
    :feeds
    {:feed-page-url "/fr/fils"
     :feed-page "Fils Atom/RSS"
     :feed-page-text
     (str "Fils Atom (aussi compatibles avec les logiciels RSS) "
          "pour les articles en français.")}
    :footer
    {:source-code
     "Code source"
     :license-name
     "licence Creative Commons (CC BY-ND 4.0)"
     :license-url
     "https://creativecommons.org/licenses/by-nd/4.0/deed.fr"
     :license-sentence
     "Le contenu de ce site est disponible selon les termes d'une "
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
