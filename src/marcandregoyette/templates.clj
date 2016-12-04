(ns marcandregoyette.templates
  (:require [marcandregoyette.page-layout :as page-layout]
            [marcandregoyette.post-layout :as post-layout]
            [marcandregoyette.urls :as urls]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [clojure.string :as string]
            [net.cgrand.enlive-html :as enlive])
  (:import java.io.StringReader
           java.util.Locale))

(defn- get-page-layout-stream []
  (StringReader. (page-layout/get-page-layout)))

(defn- get-post-layout-stream[]
  (StringReader. (post-layout/get-post-layout)))

(def iso-8601-date-formatter (time-format/formatters :date-time-no-ms))

(def en-date-formatter
  (time-format/with-locale
    (time-format/formatter "dd MMMM YYYY") Locale/CANADA))

(def fr-date-formatter
  (time-format/with-locale
    (time-format/formatter "dd MMMM YYYY") Locale/CANADA_FRENCH))

(defn- format-date
  [date lang]
  (->> date
       (time-format/parse iso-8601-date-formatter)
       (time-format/unparse
        (if (= lang "fr") fr-date-formatter en-date-formatter))))

(defn- post-date-label [metadata]
  (if (string/blank? (:date metadata))
    (enlive/substitute (enlive/html [:div]))
    (enlive/html-content (format-date (:date metadata) (:lang metadata)))))

(defn- category-label [metadata]
  (let [category (:category metadata)]
    (if (not (some #{category} urls/categories))
      (enlive/substitute (enlive/html [:div]))
      (enlive/content
       (enlive/html
        [:a {:href (urls/build-category-url category)} category])))))

(defn- tag-labels [metadata]
  (enlive/content
   (enlive/html
    (for [tag (:tags metadata)]
      (let [tag-url (urls/build-tag-url tag)]
        [:div.ui.large.label [:a.label {:href tag-url} tag]])))))

(enlive/defsnippet single-post
  (get-post-layout-stream) [:div.ui.segment] [url metadata content]
  [:div.post-date] (post-date-label metadata)
  [:div.post-category] (category-label metadata)
  [:div.post-content] (enlive/html-content content)
  [:div.post-tags] (tag-labels metadata)
  [:h2.header] (enlive/wrap :a {:class "post-title" :href url}))

(defn- category-selector [category]
  (keyword (str "a#" (urls/build-category-id category))))

(enlive/deftemplate page-layout
  (get-page-layout-stream) [metadata posts-content]
  [:html] (enlive/set-attr :lang (:lang metadata))
  [:title] (enlive/html-content (str (:title metadata)
                                     " - Marc-Andr\u00E9 Goyette"))
  [(category-selector (:category metadata))] (enlive/add-class "active")
  [:div#posts-container] (enlive/append posts-content))

(defn- build-post-for-index-page [post-by-url]
  (let [url (key post-by-url)
        metadata (:metadata (val post-by-url))
        content (:content (val post-by-url))]
    (single-post url metadata content)))

(defn- append-posts-to-index-page [posts-by-url]
  (enlive/append (map build-post-for-index-page posts-by-url)))

(defn- get-category [post-by-url]
  (-> post-by-url
      (val)
      :metadata
      :category))

(defn- find-category [posts-by-url]
  (let [categories (map get-category posts-by-url)]
    (if (= (count categories) 1)
      (first categories)
      urls/default-category)))

(defn- get-language [post-by-url]
  (-> post-by-url
      (val)
      :metadata
      :lang))

(defn- find-language [posts-by-url]
  (let [languages (map get-language posts-by-url)]
    (if (= (count languages) 1)
      (first languages)
      "en")))

(enlive/deftemplate index-page-layout (get-page-layout-stream) [posts-by-url]
  [:html] (enlive/set-attr :lang (find-language posts-by-url))
  [(category-selector (find-category posts-by-url))] (enlive/add-class "active")
  [:title] (enlive/html-content "Marc-Andr\u00E9 Goyette")
  [:div#posts-container] (append-posts-to-index-page posts-by-url))

(defn- apply-page-layout [post-by-url]
  (let [{:keys [metadata content]} (val post-by-url)]
    (apply str (page-layout
                metadata
                (single-post (key post-by-url) metadata content)))))

(defn add-page-layout [posts]
  (zipmap (keys posts)
          (map apply-page-layout posts)))

(defn add-page-layout-many-posts [posts]
  (apply str (index-page-layout posts)))
