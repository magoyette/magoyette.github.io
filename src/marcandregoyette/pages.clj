(ns marcandregoyette.pages
  (:require [marcandregoyette.page-layout :refer [get-page-layout]]
            [marcandregoyette.post-layout :refer [get-post-layout]]
            [clj-time.core :as t]
            [clj-time.format :as tf]
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
  (en/html-content (format-date (:date metadata) (:lang metadata))))

(defn- category-label [metadata]
  (let [category (:category metadata)
        category-url (str "/categories/" category)]
    (en/content (en/html [:a {:href category-url} category]))))

(defsnippet single-post (get-post-layout-stream) [en/root] [metadata content]
  [:div#post-date] (post-date-label metadata)
  [:div#category] (category-label metadata)
  [:div.post-content] (en/html-content content))

(defn- tag-labels [metadata]
  (en/content (en/html (for [tag (:tags metadata)]
                         (let [tag-url (str "/tags/" tag)]
                           [:div.ui.label [:a.label {:href tag-url} tag]])))))

(deftemplate page-layout (get-page-layout-stream) [metadata posts-content]
  [:html] (en/set-attr :lang (:lang metadata))
  [:title] (en/html-content (str (:title metadata)
                                 " - Marc-Andr\u00E9 Goyette"))
  [:div#posts-container] (en/append posts-content)
  [:div#tags] (tag-labels metadata))

(defn- apply-page-layout [post]
  (let [{:keys [metadata content]} post]
    (apply str (page-layout metadata
                            (single-post metadata content)))))

(defn add-page-layout [posts]
  (zipmap (keys posts)
          (map apply-page-layout (vals posts))))
