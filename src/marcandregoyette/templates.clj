(ns marcandregoyette.templates
  (:require [clojure.string :as string]
            [marcandregoyette.components :as components]
            [rum.core :as rum]))

(defn- build-page-layout [metadata posts-content]
  (let [{:keys [lang description]} metadata
        title (str (:title metadata) " - Marc-Andr\u00E9 Goyette")]
    (components/get-page-layout title lang description posts-content)))

(defn- get-single-post [url metadata content]
  (rum/render-static-markup
   (components/post-layout url metadata content)))

(defn- build-post-for-index-page [post-by-url]
  (let [url (key post-by-url)
        metadata (:metadata (val post-by-url))
        content (:content (val post-by-url))]
    (get-single-post url metadata content)))

(defn- get-posts-for-index-page [posts-by-url]
  (string/join (map build-post-for-index-page posts-by-url)))

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

(defn build-index-page-layout [posts-by-url]
  (let [title "Marc-Andr\u00E9 Goyette"
        lang (find-language posts-by-url)
        description nil
        posts-content (get-posts-for-index-page posts-by-url)]
    (components/get-page-layout title lang description posts-content)))

(defn- apply-page-layout [post-by-url]
  (let [{:keys [metadata content]} (val post-by-url)]
    (build-page-layout metadata
                       (get-single-post (key post-by-url) metadata content))))

(defn add-page-layout [posts]
  (zipmap (keys posts)
          (map apply-page-layout posts)))

(defn add-page-layout-many-posts [posts]
  (build-index-page-layout posts))

(defn build-articles-page-layout [lang tag posts-by-url]
  (let [title "Articles - Marc-Andr\u00E9 Goyette"]
    (components/get-articles-page-layout title lang tag posts-by-url)))
