(ns marcandregoyette.pages
  (:require [clojure.string :as str]
            [marcandregoyette.feed :as feed]
            [marcandregoyette.posts :as posts]
            [marcandregoyette.tags :as tags]
            [marcandregoyette.templates :as templates]
            [stasis.core :as stasis])
  (:import java.time.OffsetDateTime
           java.time.format.DateTimeFormatter))

(defn- post-date [post]
  (-> post
      val
      :metadata
      :date
      (OffsetDateTime/parse DateTimeFormatter/ISO_DATE_TIME)))

(defn- sort-posts [posts]
  (reverse (sort-by post-date posts)))

(defn- filter-posts [posts predicate]
  (select-keys posts (for [[url post] posts
                           :when (predicate post)]
                       url)))

(defn- has-tag [tag post]
  (some #(= tag %) (-> post :metadata :tags)))

(defn- filter-posts-by-tag [posts tag]
  (filter-posts posts (partial has-tag tag)))

(defn- has-lang [lang post]
  (= lang (-> post :metadata :lang)))

(defn- filter-posts-by-lang [posts lang]
  (filter-posts posts (partial has-lang lang)))

(defn- build-index-page [posts lang]
  (templates/add-page-layout-many-posts
   (sort-posts (filter-posts-by-lang posts lang))))

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
  (let [feed-path (str "/feeds/languages/" lang "/atom.xml")]
    [feed-path
     (feed/generate-feed
      feed-path
      (sort-posts (filter-posts-by-lang posts lang)))]))

(defn- generate-feeds-by-lang
  [posts]
  (let [languages ["en" "fr"]]
    (into {} (map #(get-feed-for-lang posts %) languages))))

(defn- get-feed-for-tag
  [posts tag]
  (let [feed-path (str "/feeds/tags/" (tags/get-tag-for-html tag) "/atom.xml")]
    [feed-path
     (feed/generate-feed
      feed-path (sort-posts (filter-posts-by-tag posts tag)))]))

(defn- generate-feeds-by-tag
  [posts]
  (let [tags (get-tags posts)]
    (into {} (map #(get-feed-for-tag posts %) tags))))

(defn- generate-feeds
  [posts]
  (merge (generate-feeds-by-lang posts)
         (generate-feeds-by-tag posts)))

(defn load-pages []
  (let [posts (posts/build-posts "" "resources/posts")]
    (stasis/merge-page-sources
     {:pages (templates/add-page-layout
              (posts/build-posts "" "resources/pages"))
      :posts (templates/add-page-layout posts)
      :index-en {"/en/index.html" (build-index-page posts "en")}
      :index-fr {"/fr/index.html" (build-index-page posts "fr")}
      :index {"/index.html" (build-index-page posts "fr")}
      :tags (get-tags-pages posts)
      :feeds (generate-feeds posts)})))
