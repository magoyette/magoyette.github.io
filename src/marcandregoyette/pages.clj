(ns marcandregoyette.pages
  (:require [clojure.string :as str]
            [marcandregoyette.categories :as categories]
            [marcandregoyette.feed :as feed]
            [marcandregoyette.posts :as posts]
            [marcandregoyette.tags :as tags]
            [marcandregoyette.templates :as templates]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [stasis.core :as stasis]))

(def iso-8601-date-formatter (time-format/formatters :date-time-no-ms))

(defn- post-date [post]
  (->> post
       val
       :metadata
       :date
       (time-format/parse iso-8601-date-formatter)))

(defn- sort-posts [posts]
  (reverse (sort-by post-date posts)))

(defn- filter-posts [posts predicate]
  (select-keys posts (for [[url post] posts
                           :when (predicate post)]
                       url)))

(defn- has-category [category post]
  (= category (-> post :metadata :category)))

(defn- filter-posts-by-category [posts category]
  (filter-posts posts (partial has-category category)))

(defn- has-tag [tag post]
  (some #(= tag %) (-> post :metadata :tags)))

(defn- filter-posts-by-tag [posts tag]
  (filter-posts posts (partial has-tag tag)))

(defn- has-lang [lang post]
  (= lang (-> post :metadata :category :lang)))

(defn- filter-posts-by-lang [posts lang]
  (filter-posts posts (partial has-lang lang)))

(defn- build-index-page [posts]
  (templates/add-page-layout-many-posts (sort-posts posts)))

(defn- get-categories [posts]
  (remove #(= (:name %) "") (distinct (for [[url post] posts]
                                        (-> post :metadata :category)))))

(defn- get-posts-for-category [posts category]
  (templates/add-page-layout-many-posts
   (sort-posts (filter-posts-by-category posts category))))

(defn- get-categories-pages [posts]
  (let [categories (get-categories posts)]
    (zipmap (doall (map categories/build-category-url categories))
            (map (partial get-posts-for-category posts) categories))))

(defn- get-tags [posts]
  (distinct (flatten (for [[url post] posts]
                       (-> post :metadata :tags)))))

(defn- get-posts-for-tag [posts tag]
  (templates/add-page-layout-many-posts
   (sort-posts (filter-posts-by-tag posts tag))))

(defn- get-tags-pages [posts]
  (let [tags (get-tags posts)]
    (zipmap (doall (map tags/build-tag-url tags))
            (map (partial get-posts-for-tag posts) tags))))

(defn- get-feed-for-lang
  [posts lang]
  [(str "/feeds/languages/" lang "/atom.xml")
   (feed/generate-feed
    (sort-posts (filter-posts-by-lang posts lang)))])

(defn- generate-feeds-by-lang
  [posts]
  (let [languages (distinct (map :lang (categories/get-visible-categories)))]
    (into {} (map #(get-feed-for-lang posts %) languages))))

(defn- get-category-atom-feed-path [category]
  (str "/feeds/categories/" (categories/get-category-name-for-html category) "/atom.xml"))

(defn- get-feed-for-category
  [posts category]
  [(get-category-atom-feed-path category)
   (feed/generate-feed
    (sort-posts (filter-posts-by-category posts category)))])

(defn- generate-feeds-by-category
  [posts]
  (let [categories (categories/get-visible-categories)]
    (into {} (map #(get-feed-for-category posts %) categories))))

(defn- get-tag-atom-feed-path [tag]
  (str "/feeds/tags/" (tags/get-tag-for-html tag) "/atom.xml"))

(defn- get-feed-for-tag
  [posts tag]
  [(get-tag-atom-feed-path tag)
   (feed/generate-feed
    (sort-posts (filter-posts-by-tag posts tag)))])

(defn- generate-feeds-by-tag
  [posts]
  (let [tags (get-tags posts)]
    (into {} (map #(get-feed-for-tag posts %) tags))))

(defn- generate-feeds
  [posts]
  (merge {"/atom.xml" (feed/generate-feed (sort-posts posts))}
         (generate-feeds-by-lang posts)
         (generate-feeds-by-category posts)
         (generate-feeds-by-tag posts)))

(defn load-pages []
  (let [posts (posts/build-posts "/posts" "resources/posts")]
    (stasis/merge-page-sources
     {:pages (templates/add-page-layout
              (posts/build-posts "" "resources/pages"))
      :posts (templates/add-page-layout posts)
      :index {"/index.html" (build-index-page posts)}
      :categories (get-categories-pages posts)
      :tags (get-tags-pages posts)
      :feeds (generate-feeds posts)})))
