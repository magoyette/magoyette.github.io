;; ## Categories of the posts
;;
;; A categorie is a group of posts with a common topic that
;; share the same language.
(ns marcandregoyette.categories
  (:require [clojure.string :as string]))

(defrecord Category [name lang hidden])

(def categories [(->Category "Programming" "en" false)
                 (->Category "Programmation" "fr" false)
                 (->Category "About" "en" true)
                 (->Category "A propos" "fr" true)])

(defn get-category-by-name
  "Find a category by its name."
  [name]
  (first (filter #(= (:name %) name) categories)))

(defn get-default-category
  "Find the default category in a sequence of categories."
  []
  (get-category-by-name "Programming"))

(defn get-visible-categories
  []
  (remove :hidden categories))

(defn get-category-name-for-html
  [category]
  (string/lower-case (string/replace (:name category) #"\s" "_")))

(defn build-category-id
  "Build an HTML element id to identify a category."
  [category]
  (str "category-" (get-category-name-for-html category)))

(defn build-category-url
  "Build the url for a category."
  [category]
  (str "/categories/" (get-category-name-for-html category) "/"))
