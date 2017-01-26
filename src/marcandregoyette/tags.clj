(ns marcandregoyette.tags
  (:require [clojure.string :as string]))

(defn build-tag-url
  "Build the url for a tag."
  [tag]
  (str "/tags/" (string/lower-case (string/replace tag #"\s" "_")) "/"))
