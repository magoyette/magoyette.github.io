(ns marcandregoyette.posts
  (:require [clojure.string :as string]
            [clojure.tools.reader.edn :as edn]
            [marcandregoyette.categories :as categories]
            [marcandregoyette.highlight :as highlight]
            [me.raynes.cegdown :as cegdown]
            [net.cgrand.enlive-html :as enlive]
            [stasis.core :as stasis]))

;; Metadata that describe the post.
(defrecord PostMetadata [title date category tags])

;; Blog post.
(defrecord Post [^PostMetadata metadata content])

(def post-file-extension-regex #"\.md$")

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

(defn- replace-category-name-by-record
  [metadata]
  (let [category (categories/get-category-by-name (:category metadata))]
    (assoc metadata :category category)))

(defn read-post-metadata
  "Retrieve the metadata of a post from its content."
  [post]
  (replace-category-name-by-record
   (edn/read-string
    (->> post
         (re-seq post-edn-header-regex)
         first
         second))))

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

(defn- build-post-url-path
  "Build the url path that is associated to a post."
  [export-path path]
  (str export-path (string/replace path post-file-extension-regex "/")))

(defn- load-markdown-posts
  "Load the markdown posts from the files in a directory."
  [markdown-posts-directory]
  (stasis/slurp-directory markdown-posts-directory post-file-extension-regex))

(defn build-posts
  "Loads all the posts in a directory, then build a map with the url path
  of a post as a key, and the Post as a value. The url-path will be used
  as an initial path to which the post path will be added."
  [url-path markdown-posts-directory]
  (let [posts (load-markdown-posts markdown-posts-directory)]
    (zipmap (map #(build-post-url-path url-path %) (keys posts))
            (map make-post (vals posts)))))
