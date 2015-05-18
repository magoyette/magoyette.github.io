;; ## Functions to build urls
;;
;; A few functions that are used to build urls.
(ns marcandregoyette.urls
  (:require [clojure.string :as string]))

(def categories ["Programming" "Programmation"])

(def default-category "Programming")

(defn- build-metadata-attribute-id
  "Build an HTML element id to identify the attribute of the metadata of a post."
  [metadata-attribute]
  (string/lower-case (string/replace metadata-attribute #"\s" "_")))

(defn- build-metadata-attribute-url
  "Build the url for an attribute of the metadata of a post."
  [url-path metadata-attribute]
  (str "/" url-path "/" (build-metadata-attribute-id metadata-attribute) "/"))

(defn build-category-id
  "Build an HTML element id to identify a category."
  [category]
  (str "category-" (build-metadata-attribute-id category)))

(defn build-category-url
  "Build the url for a category."
  [category]
  (build-metadata-attribute-url "categories" category))

(defn build-tag-url
  "Build the url for a tag."
  [tag]
  (build-metadata-attribute-url "tags" tag))
