(ns marcandregoyette.tags
  (:require [clojure.string :as string]))

(defn get-tag-for-url
  [tag]
  (string/lower-case (string/replace tag #"\s" "-")))

(defn build-tag-url
  "Build the url for a tag."
  [tag lang]
  (str "/" lang "/tags/" (get-tag-for-url tag) "/"))

(defn get-feed-path-for-tag-and-lang
  [lang tag]
  (str "/feeds/languages/" lang
       "/tags/" (get-tag-for-url tag) "/atom.xml"))
