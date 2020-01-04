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

(defn- add-page-layout-to-post [post-by-url]
  (let [{:keys [metadata content]} (val post-by-url)]
    (build-page-layout metadata
                       (get-single-post (key post-by-url) metadata content))))

(defn add-page-layout-to-posts [posts]
  (zipmap (keys posts)
          (map add-page-layout-to-post posts)))

(defn build-articles-page-layout [lang tag posts-by-url]
  (let [title "Articles - Marc-Andr\u00E9 Goyette"]
    (components/get-articles-page-layout title lang tag posts-by-url)))
