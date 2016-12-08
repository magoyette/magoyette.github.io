(ns marcandregoyette.posts
  (:require [clojure.string :as string]
            [clojure.tools.reader.edn :as edn]
            [marcandregoyette.highlight :as highlight]
            [me.raynes.cegdown :as cegdown]
            [net.cgrand.enlive-html :as enlive]
            [stasis.core :as stasis]))

;; Metadata that describe the post.
(defrecord PostMetadata [title date lang category tags])

;; Blog post.
(defrecord Post [^PostMetadata metadata content])

(def post-edn-header-regex #"(?is)^---(.*?)---")

(defn- remove-post-metadata
  "Remove the metadata header from the content of a post."
  [post-content]
  (string/replace post-content post-edn-header-regex ""))

(def ^:private pegdown-options
  [:autolinks ; urls are considered as links
   :fenced-code-blocks ; wrap code blocks with ```
   :hardwraps ; alternate handling of new lines
   :strikethrough]) ; ~~text~~

(defn- markdown->html
  "Convert the provided markdown content to HTML."
  [content]
  (cegdown/to-html content pegdown-options))

(defn- read-post-metadata
  "Retrieve the metadata of a post from its content."
  [post]
  (edn/read-string
   (->> post
        (re-seq post-edn-header-regex)
        first
        second)))

(defn insert-post-title
  "Insert the title of a post as a h2 header before its content."
  [post-content post-title]
  (str (string/join (enlive/emit* (enlive/html [:h2.ui.large.header post-title])))
       post-content))

(defn- generate-post-content-html
  "Generate the html from the markdown content of a post. The edn metadata of
  the post is removed, the title of the post is added and the code blocks are
  highlighted."
  [post-title post-content]
  (-> post-content
      remove-post-metadata
      markdown->html
      (insert-post-title post-title)
      highlight/highlight-code-blocks))

(defn- make-post
  "Build a Post from the contents of a post markdown file."
  [post]
  (let [metadata (read-post-metadata post)]
    (map->Post
     {:metadata (map->PostMetadata metadata)
      :content (generate-post-content-html (:title metadata) post)})))

(defn- transform-path [export-path path file-extension-regex]
  (str export-path (string/replace path file-extension-regex "/")))

;; Need to add boolean for feed, could replace with configuration map
(defn build-posts [export-path directory file-extension-regex]
  (let [posts (stasis/slurp-directory directory file-extension-regex)]
    (zipmap (map #(transform-path export-path % file-extension-regex)
                 (keys posts))
            (map make-post (vals posts)))))
