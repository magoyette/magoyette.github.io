(ns marcandregoyette.urls
  (:require [clojure.string :as str]))

(defn- build-metadata-value-url [url-path metadata-value]
  (let [value-in-url (str/lower-case (str/replace metadata-value #"\s" "_"))]
    (str "/" url-path "/" value-in-url "/")))

(defn build-category-url [category]
  (build-metadata-value-url "categories" category))

(defn build-tag-url [tag]
  (build-metadata-value-url "tags" tag))
