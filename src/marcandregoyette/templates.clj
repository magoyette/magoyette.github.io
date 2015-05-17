(ns marcandregoyette.templates
  (:require [marcandregoyette.page-layout :refer [get-page-layout]]
            [marcandregoyette.post-layout :refer [get-post-layout]]
            [marcandregoyette.urls :as urls]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [clojure.string :as str]
            [net.cgrand.enlive-html :as en :refer [defsnippet deftemplate]]))

(defn- get-page-layout-stream []
  (java.io.StringReader. (get-page-layout)))

(defn- get-post-layout-stream[]
  (java.io.StringReader. (get-post-layout)))

(def iso-8601-date-formatter (tf/formatters :date-time-no-ms))

(def en-date-formatter
  (tf/with-locale (tf/formatter "dd MMMM YYYY") java.util.Locale/CANADA))

(def fr-date-formatter
  (tf/with-locale (tf/formatter "dd MMMM YYYY") java.util.Locale/CANADA_FRENCH))

(defn- format-date
  [date lang]
  (->> date
       (tf/parse iso-8601-date-formatter)
       (tf/unparse (if (= lang "fr") fr-date-formatter en-date-formatter))))

(defn- post-date-label [metadata]
  (if (str/blank? (:date metadata))
    (en/substitute (en/html [:div]))
    (en/html-content (format-date (:date metadata) (:lang metadata)))))

(defn- category-label [metadata]
  (let [category (:category metadata)]
    (if (not (some #{category} urls/categories))
      (en/substitute (en/html [:div]))
      (en/content (en/html [:a {:href (urls/build-category-url category)} category])))))

(defn- tag-labels [metadata]
  (en/content (en/html (for [tag (:tags metadata)]
                         (let [tag-url (urls/build-tag-url tag)]
                           [:div.ui.label [:a.label {:href tag-url} tag]])))))

(defn- comments-link [url single-post]
  (en/content (en/html (if single-post
                         [:div#disqus_thread]
                         [:a {:href (str url "#disqus_thread")} ""]))))

(defsnippet single-post (get-post-layout-stream) [en/root] [url single-post metadata content]
  [:div#post-date] (post-date-label metadata)
  [:div#category] (category-label metadata)
  [:div.post-content] (en/html-content content)
  [:div#tags] (tag-labels metadata)
  [:div.disqus-comments] (comments-link url single-post)
  [:h2.header] (en/wrap :a {:class "post-title" :href url}))

(defn disqus-config [lang]
  (format "var disqus_config = function() {this.language = \"%s\"}" lang))

(defn- category-selector [category]
  (keyword (str "a#" (urls/build-category-id category))))

(deftemplate page-layout (get-page-layout-stream) [metadata posts-content]
  [:html] (en/set-attr :lang (:lang metadata))
  [:title] (en/html-content (str (:title metadata)
                                 " - Marc-Andr\u00E9 Goyette"))
  [(category-selector (:category metadata))] (en/add-class "active")
  [:div#posts-container] (en/append posts-content)
  [:div.disqus-config] (en/substitute (en/html [:script {:type "text/javascript"} (disqus-config (:lang metadata))])))

(defn- build-post-for-index-page [post-by-url]
  (let [url (key post-by-url)
        metadata (:metadata (val post-by-url))
        content (:content (val post-by-url))]
    (single-post url false metadata content)))

(defn- append-posts-to-index-page [posts-by-url]
  (en/append (map build-post-for-index-page posts-by-url)))

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

(deftemplate index-page-layout (get-page-layout-stream) [posts-by-url]
  [:html] (en/set-attr :lang "en")
  [(category-selector (find-category posts-by-url))] (en/add-class "active")
  [:title] (en/html-content "Marc-Andr\u00E9 Goyette")
  [:div#posts-container] (append-posts-to-index-page posts-by-url))

(defn- apply-page-layout [post-by-url]
  (let [{:keys [metadata content]} (val post-by-url)]
    (apply str (page-layout metadata
                            (single-post (key post-by-url) true metadata content)))))

(defn add-page-layout [posts]
  (zipmap (keys posts)
          (map apply-page-layout posts)))

(defn add-page-layout-many-posts [posts]
  (apply str (index-page-layout posts)))
