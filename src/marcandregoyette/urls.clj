;; ## Functions to build urls
;;
;; A few functions that are used to build urls.
(ns marcandregoyette.urls
  (:require [clojure.string :as str]))

(defn- build-metadata-attribute-url
  "Builds the url for an attribute of the metadata of a post."
  [url-path metadata-attribute]
  (str "/"
       url-path
       "/"
       (str/lower-case (str/replace metadata-attribute #"\s" "_"))
       "/"))

(defn build-category-url
  "Build the url for a category."
  [category]
  (build-metadata-attribute-url "categories" category))

(defn build-tag-url
  "Build the url for a tag."
  [tag]
  (build-metadata-attribute-url "tags" tag))
