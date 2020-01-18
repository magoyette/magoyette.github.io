(ns marcandregoyette.pages
  (:require [marcandregoyette.components :as components]
            [marcandregoyette.feed :as feed]
            [marcandregoyette.articles :as articles]
            [marcandregoyette.sitemap :as sitemap]
            [marcandregoyette.tags :as tags]
            [marcandregoyette.translations :as translations]
            [clojure.string :as string]
            [stasis.core :as stasis])
  (:import java.time.OffsetDateTime
           java.time.format.DateTimeFormatter))

(defn- get-page-title [is-home-page title]
  (str
   (when-not is-home-page (str title " - "))
   "Marc-Andr\u00E9 Goyette"))

(defn- build-page-layout [is-home-page article-by-url]
  (let [url (key article-by-url)
        article (val article-by-url)
        metadata (:metadata article)
        {:keys [lang description]} metadata
        title (get-page-title is-home-page (:title metadata))]
    (components/get-page-layout title lang description url article)))

(defn add-page-layout-to-articles [is-home-page articles]
  (zipmap (keys articles)
          (map #(build-page-layout is-home-page %) articles)))

(defn- get-articles-page-title [lang tag]
  (str "Articles"
       (if tag (str (translations/translate lang :articles/for-tag) tag))
       " - Marc-Andr\u00E9 Goyette"))

(defn build-articles-page-layout [lang tag articles-by-url]
  (let [title (get-articles-page-title lang tag)]
    (components/get-articles-page-layout title lang tag articles-by-url)))

(defn- article-date [article]
  (-> article
      val
      :metadata
      :date
      (OffsetDateTime/parse DateTimeFormatter/ISO_DATE_TIME)))

(defn- sort-articles [articles]
  (reverse (sort-by article-date articles)))

(defn- filter-articles [articles predicate]
  (select-keys articles (for [[url article] articles
                              :when (predicate article)]
                          url)))

(defn- has-tag [tag article]
  (some #(= tag %) (-> article :metadata :tags)))

(defn- filter-articles-by-tag [articles tag]
  (filter-articles articles (partial has-tag tag)))

(defn- has-lang [lang article]
  (= lang (-> article :metadata :lang)))

(defn- filter-articles-by-lang [articles lang]
  (filter-articles articles (partial has-lang lang)))

(defn- get-most-recent-article [articles lang]
  (first (sort-articles (filter-articles-by-lang articles lang))))

(defn- build-home-page [articles lang home-page-url]
  (let [most-recent-article (get-most-recent-article articles lang)
        article-url (key most-recent-article)
        home-page-article {article-url (val most-recent-article)}
        article-with-page-layout (add-page-layout-to-articles true home-page-article)]
    {home-page-url (get article-with-page-layout article-url)}))

(defn- build-articles-page [articles lang]
  (build-articles-page-layout
   lang nil (sort-articles (filter-articles-by-lang articles lang))))

(defn- get-tags [articles]
  (distinct (flatten (for [[url article] articles]
                       (-> article :metadata :tags)))))

(defn- get-articles-for-tag [articles lang tag]
  (build-articles-page-layout
   lang tag (sort-articles (filter-articles-by-tag articles tag))))

(defn- get-tags-pages-for-lang [articles lang]
  (let [articles-for-lang (filter-articles-by-lang articles lang)
        tags (get-tags articles-for-lang)]
    (zipmap (doall (map #(tags/build-tag-url % lang) tags))
            (map (partial get-articles-for-tag articles-for-lang lang) tags))))

(defn- get-tags-pages [articles]
  (merge (get-tags-pages-for-lang articles "en")
         (get-tags-pages-for-lang articles "fr")))

(defn- get-feed-for-lang
  [articles lang]
  (let [feed-path (str "/feeds/languages/" lang "/atom.xml")
        sorted-articles (sort-articles (filter-articles-by-lang articles lang))]
    [feed-path
     (feed/generate-feed feed-path sorted-articles lang nil)]))

(defn- generate-feeds-by-lang
  [articles]
  (let [languages ["en" "fr"]]
    (into {} (map #(get-feed-for-lang articles %) languages))))

(defn- get-feed-path-for-tag-and-lang
  [lang tag]
  (str "/feeds/languages/" lang
       "/tags/" (tags/get-tag-for-url tag) "/atom.xml"))

(defn- get-feed-for-tag-and-lang
  [articles tag lang]
  (let [feed-path (get-feed-path-for-tag-and-lang lang tag)
        sorted-articles (sort-articles (filter-articles-by-tag articles tag))]
    [feed-path
     (feed/generate-feed feed-path sorted-articles lang tag)]))

(defn- generate-feeds-by-tag-and-lang
  [articles lang]
  (let [articles-by-lang (filter-articles-by-lang articles lang)
        tags (get-tags articles-by-lang)]
      (into {} (map #(get-feed-for-tag-and-lang articles-by-lang % lang) tags))))

(defn- generate-feeds
  [articles]
  (merge (generate-feeds-by-lang articles)
         (generate-feeds-by-tag-and-lang articles "en")
         (generate-feeds-by-tag-and-lang articles "fr")))

(defn load-pages []
  (let [articles (articles/build-articles "" "resources/articles")
        pages (articles/build-articles "" "resources/pages")
        tag-pages (get-tags-pages articles)]
    (stasis/merge-page-sources
     {:pages (add-page-layout-to-articles false pages)
      :articles (add-page-layout-to-articles false articles)
      :index-en (build-home-page articles "en" "/en/")
      ;; index-fr exists in case someone edit the url manually from /en to /fr
      :index-fr (build-home-page articles "fr" "/fr/")
      :articles-en {"/en/articles/" (build-articles-page articles "en")}
      :articles-fr {"/fr/articles/" (build-articles-page articles "fr")}
      :index (build-home-page articles "fr" "/")
      :tags tag-pages
      :feeds (generate-feeds articles)
      :sitemap {"/sitemap.xml" (sitemap/generate-sitemap articles pages tag-pages)}})))
