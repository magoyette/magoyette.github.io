(ns marcandregoyette.templates
  (:require [marcandregoyette.categories :as categories]
            [marcandregoyette.components :as components]
            [rum.core :as rum])
  (:import java.io.StringReader))

(defn- category-selector [category]
  (keyword (str "a#"
                (if category
                  (categories/build-category-id category)
                  nil))))

(defn- build-page-layout [metadata posts-content]
  (let [title (str (:title metadata) " - Marc-Andr\u00E9 Goyette")
        lang (get-in metadata [:category :lang])
        active-category (:category metadata)]
    (components/get-page-layout title lang active-category posts-content)))

(defn- get-single-post [url metadata content]
  (components/render-post-layout url metadata content))

(defn- build-post-for-index-page [post-by-url]
  (let [url (key post-by-url)
        metadata (:metadata (val post-by-url))
        content (:content (val post-by-url))]
    (get-single-post url metadata content)))

(defn- get-posts-for-index-page [posts-by-url]
  (apply str (map build-post-for-index-page posts-by-url)))

(defn- get-category [post-by-url]
  (-> post-by-url
      (val)
      :metadata
      :category))

(defn- find-category [posts-by-url]
  (let [categories (map get-category posts-by-url)]
    (if (= (count categories) 1)
      (first categories)
      (categories/get-default-category))))

(defn- get-language [post-by-url]
  (-> post-by-url
      (val)
      :metadata
      :category
      :lang))

(defn- find-language [posts-by-url]
  (let [languages (map get-language posts-by-url)]
    (if (= (count languages) 1)
      (first languages)
      (:lang (categories/get-default-category)))))

(defn build-index-page-layout [posts-by-url]
  (let [title "Marc-Andr\u00E9 Goyette"
        lang (find-language posts-by-url)
        active-category (find-category posts-by-url)
        posts-content (get-posts-for-index-page posts-by-url)]
    (components/get-page-layout title lang active-category posts-content)))

(defn- apply-page-layout [post-by-url]
  (let [{:keys [metadata content]} (val post-by-url)]
    (build-page-layout metadata
                       (get-single-post (key post-by-url) metadata content))))

(defn add-page-layout [posts]
  (zipmap (keys posts)
          (map apply-page-layout posts)))

(defn add-page-layout-many-posts [posts]
  (build-index-page-layout posts))
