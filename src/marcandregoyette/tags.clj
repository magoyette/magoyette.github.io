(ns marcandregoyette.tags
  (:require [clojure.string :as string]))

(defn get-tag-for-html
  [tag]
  (string/lower-case (string/replace tag #"\s" "_")))

(defn build-tag-url
  "Build the url for a tag."
  [tag lang]
  (str "/" lang "/tags/" (get-tag-for-html tag) "/"))
