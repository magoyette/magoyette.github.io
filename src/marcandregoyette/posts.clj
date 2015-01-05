(ns marcandregoyette.posts
  (:require [clojure.tools.reader.edn :as edn]
            [marcandregoyette.highlight :refer [highlight-code-blocks]]
            [me.raynes.cegdown :as ceg]
            [net.cgrand.enlive-html :as en]
            [stasis.core :as s]))

(defn- remove-meta [page]
  (clojure.string/replace page #"(?is)^---(.*?)---" ""))

(def ^:private pegdown-options
  [:autolinks ; urls are considered as links
   :fenced-code-blocks ; wrap code blocks with ```
   :hardwraps ; alternate handling of new lines
   :strikethrough]) ; ~~text~~

(defn- parse-markdown [content]
  (ceg/to-html content pegdown-options))

(defn- extract-metadata [post]
  (edn/read-string (->> post
                        (re-seq #"(?is)^---(.*?)---")
                        first
                        second)))

(defn insert-post-title [post-content post-title]
  (str (apply str (en/emit* (en/html [:h2.ui.large.header post-title])))
       post-content))

(defn- transform-content [post-title post-content]
  (-> post-content
      remove-meta
      parse-markdown
      (insert-post-title post-title)
      highlight-code-blocks))

(defn build-post-map [post]
  (let [metadata (extract-metadata post)]
    {:metadata metadata
     :content (transform-content (:title metadata) post)}))

(defn- transform-path [export-path path file-extension-regex]
  (str export-path (clojure.string/replace path file-extension-regex "/")))

;; Need to add boolean for feed, could replace with configuration map
(defn build-posts [export-path directory file-extension-regex]
  (let [posts (s/slurp-directory directory file-extension-regex)]
    (zipmap (map #(transform-path export-path % file-extension-regex)
                 (keys posts))
            (map build-post-map (vals posts)))))
