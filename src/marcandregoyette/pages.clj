(ns marcandregoyette.pages
  (:require [marcandregoyette.feed :as feed]
            [marcandregoyette.posts :as posts]
            [marcandregoyette.tags :as tags]
            [marcandregoyette.templates :as templates]
            [marcandregoyette.sitemap :as sitemap]
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

(defn- get-tags-pages-for-lang [posts lang]
  (let [posts-for-lang (filter-posts-by-lang posts lang)
        tags (get-tags posts-for-lang)]
    (zipmap (doall (map #(tags/build-tag-url % lang) tags))
            (map (partial get-posts-for-tag posts-for-lang) tags))))

(defn- get-tags-pages [posts]
  (merge (get-tags-pages-for-lang posts "en")
         (get-tags-pages-for-lang posts "fr")))

(defn- get-feed-for-lang
  [posts lang]
  (let [feed-path (str "/feeds/languages/" lang "/atom.xml")
        sorted-posts (sort-posts (filter-posts-by-lang posts lang))]
    [feed-path
     (feed/generate-feed feed-path sorted-posts lang nil)]))

(defn- generate-feeds-by-lang
  [posts]
  (let [languages ["en" "fr"]]
    (into {} (map #(get-feed-for-lang posts %) languages))))

(defn- get-feed-path-for-tag-and-lang
  [lang tag]
  (str "/feeds/languages/" lang
       "/tags/" (tags/get-tag-for-url tag) "/atom.xml"))

(defn- get-feed-for-tag-and-lang
  [posts tag lang]
  (let [feed-path (get-feed-path-for-tag-and-lang lang tag)
        sorted-posts (sort-posts (filter-posts-by-tag posts tag))]
    [feed-path
     (feed/generate-feed feed-path sorted-posts lang tag)]))

(defn- generate-feeds-by-tag-and-lang
  [posts lang]
  (let [posts-by-lang (filter-posts-by-lang posts lang)
        tags (get-tags posts-by-lang)]
      (into {} (map #(get-feed-for-tag-and-lang posts-by-lang % lang) tags))))

(defn- generate-feeds
  [posts]
  (merge (generate-feeds-by-lang posts)
         (generate-feeds-by-tag-and-lang posts "en")
         (generate-feeds-by-tag-and-lang posts "fr")))

(defn load-pages []
  (let [posts (posts/build-posts "" "resources/posts")
        pages (posts/build-posts "" "resources/pages")
        tag-pages (get-tags-pages posts)]
    (stasis/merge-page-sources
     {:pages (templates/add-page-layout pages)
      :posts (templates/add-page-layout posts)
      :index-en {"/en/index.html" (build-index-page posts "en")}
      ;; index-fr exists in case someone edit the url manually from /en to /fr
      :index-fr {"/fr/index.html" (build-index-page posts "fr")}
      :index {"/index.html" (build-index-page posts "fr")}
      :tags tag-pages
      :feeds (generate-feeds posts)
      :sitemap {"/sitemap.xml" (sitemap/generate-sitemap posts pages tag-pages)}})))
