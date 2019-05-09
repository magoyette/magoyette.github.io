;; ## Posts
;;
;; This namespace provide fonctions to parse posts and their metadata.
;; Each post starts with metadata stored in
;; [EDN](https://github.com/edn-format/edn).
;;
;; The content of the post is in Markdown. The Markdown parser is Flexmark
;; and is configured to use [CommonMark 0.28](https://spec.commonmark.org/0.28/).
;;
;; Two Flexmark extensions are used for
;; [footnotes](https://github.com/vsch/flexmark-java/wiki/Footnotes-Extension)
;; and [tables](https://github.com/vsch/flexmark-java/blob/master/flexmark-ext-tables/src/main/javadoc/overview.md).
(ns marcandregoyette.posts
  (:require [clojure.string :as string]
            [clojure.tools.reader.edn :as edn]
            [marcandregoyette.highlight :as highlight]
            [stasis.core :as stasis])
  (:import com.vladsch.flexmark.ext.footnotes.FootnoteExtension
           com.vladsch.flexmark.ext.tables.TablesExtension
           com.vladsch.flexmark.html.HtmlRenderer
           com.vladsch.flexmark.parser.Parser
           com.vladsch.flexmark.util.ast.Node
           com.vladsch.flexmark.util.options.MutableDataSet))

(defrecord PostTranslation [lang path])

;; Metadata that describe the post.
(defrecord PostMetadata [title date lang tags ^PostTranslation translations])

;; Blog post.
(defrecord Post [^PostMetadata metadata content])

(def post-file-extension-regex #"\.md$")

(def post-edn-header-regex #"(?is)^---(.*?)---")

(def markdown-options
  (doto (MutableDataSet.)
    (.set HtmlRenderer/FENCED_CODE_LANGUAGE_CLASS_PREFIX "")
    (.set Parser/EXTENSIONS [(FootnoteExtension/create)
                             (TablesExtension/create)])))

(defn- remove-post-metadata
  "Remove the metadata header from the content of a post."
  [post-content]
  (string/replace post-content post-edn-header-regex ""))

(defn- parse-markdown-to-html
  "Convert the provided markdown content to HTML."
  [content]
  (let [parser (.build (Parser/builder markdown-options))
        renderer (.build (HtmlRenderer/builder markdown-options))]
    (->> content
         (.parse parser)
         (.render renderer))))

(defn- read-post-header
  [post]
  (edn/read-string
   (->> post
        (re-seq post-edn-header-regex)
        first
        second)))

(defn- generate-post-content-html
  "Generate the html from the markdown content of a post. The edn metadata of
  the post is removed and the code blocks are highlighted."
  [post-content]
  (-> post-content
      remove-post-metadata
      parse-markdown-to-html
      highlight/highlight-code-blocks))

(defn- make-post
  "Build a Post from the contents of a post markdown file."
  [post]
  (map->Post
   {:metadata (map->PostMetadata (read-post-header post))
    :content (generate-post-content-html post)}))

(defn- build-post-url-path
  "Build the url path that is associated to a post."
  [export-path path]
  (str export-path (string/replace path post-file-extension-regex "/")))

(defn- load-markdown-posts
  "Load the markdown posts from the files in a directory."
  [markdown-posts-directory]
  (stasis/slurp-directory markdown-posts-directory post-file-extension-regex))

(defn build-posts-from-stasis-map
  [url-path posts]
  (zipmap (map #(build-post-url-path url-path %) (keys posts))
            (map make-post (vals posts))))

(defn build-posts
  "Loads all the posts in a directory, then build a map with the url path
  of a post as a key, and the Post as a value. The url-path will be used
  as an initial path to which the post path will be added."
  [url-path markdown-posts-directory]
  (let [posts (load-markdown-posts markdown-posts-directory)]
    (build-posts-from-stasis-map url-path posts)))
