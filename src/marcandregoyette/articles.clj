;; ## Articles
;;
;; This namespace provide fonctions to parse articles and their metadata.
;; Each article starts with metadata stored in
;; [EDN](https://github.com/edn-format/edn).
;;
;; The content of the article is in Markdown. The Markdown parser is Flexmark
;; and is configured to use [CommonMark 0.28](https://spec.commonmark.org/0.28/).
;;
;; Two Flexmark extensions are used for
;; [footnotes](https://github.com/vsch/flexmark-java/wiki/Footnotes-Extension)
;; and [tables](https://github.com/vsch/flexmark-java/blob/master/flexmark-ext-tables/src/main/javadoc/overview.md).
(ns marcandregoyette.articles
  (:require [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojure.tools.reader.edn :as edn]
            [marcandregoyette.highlight :as highlight]
            [stasis.core :as stasis])
  (:import com.vladsch.flexmark.ext.footnotes.FootnoteExtension
           com.vladsch.flexmark.ext.tables.TablesExtension
           com.vladsch.flexmark.html.HtmlRenderer
           com.vladsch.flexmark.parser.Parser
           com.vladsch.flexmark.util.ast.Node
           com.vladsch.flexmark.util.data.MutableDataSet))

(defrecord ArticleTranslation [lang path])

;; Metadata that describe the article.
(defrecord ArticleMetadata [title date lang tags ^ArticleTranslation translations])

(defrecord Article [^ArticleMetadata metadata content])

(def article-file-extension-regex #"\.md$")

(def article-edn-header-regex #"(?is)^---(.*?)---")

(def markdown-options
  (doto (MutableDataSet.)
    (.set HtmlRenderer/FENCED_CODE_LANGUAGE_CLASS_PREFIX "")
    (.set Parser/EXTENSIONS [(FootnoteExtension/create)
                             (TablesExtension/create)])))

(defn- remove-article-metadata
  "Remove the metadata header from the content of an article."
  [article-content]
  (string/replace article-content article-edn-header-regex ""))

(defn- parse-markdown-to-html
  "Convert the provided markdown content to HTML."
  [content]
  (let [parser (.build (Parser/builder markdown-options))
        renderer (.build (HtmlRenderer/builder markdown-options))]
    (->> content
         (.parse parser)
         (.render renderer))))

(defn- read-article-header
  [article]
  (edn/read-string
   (->> article
        (re-seq article-edn-header-regex)
        first
        second)))

(defn- generate-article-content-html
  "Generate the html from the markdown content of an article. The edn metadata of
  the article is removed and the code blocks are highlighted."
  [article-content]
  (-> article-content
      remove-article-metadata
      parse-markdown-to-html
      highlight/highlight-code-blocks))

(defn- make-article
  "Build an article from the contents of a markdown file."
  [article]
  (map->Article
   {:metadata (map->ArticleMetadata (read-article-header article))
    :content (generate-article-content-html article)}))

(defn- build-article-url-path
  "Build the url path that is associated to a article."
  [export-path path]
  (str export-path (string/replace path article-file-extension-regex "/")))

(defn- load-markdown-articles
  "Load the articles stored in Markdown files in a directory."
  [markdown-articles-directory]
  (stasis/slurp-directory markdown-articles-directory article-file-extension-regex))

(defn build-articles-from-stasis-map
  [url-path articles]
  (zipmap (map #(build-article-url-path url-path %) (keys articles))
            (map make-article (vals articles))))

(def max-title-length 78)

(defn- validate-title-length [metadata quoted-title]
  (let [title (:title metadata)]
    (when (> (count title) max-title-length)
      (log/warn "Title of the article" quoted-title
                "is too long."
                "Actual length:" (count title)
                "Max length:" max-title-length))))

(def max-description-length 155)

(defn- validate-description-length [metadata quoted-title]
  (let [description (:description metadata)]
    (when (> (count description) max-description-length)
      (log/warn "Description of the article" quoted-title
                "is too long."
                "Actual length:" (count description)
                "Max length:" max-description-length
                "Description:" description))))

(defn- validate-article [article]
  (let [metadata (:metadata article)
        title (str "\"" (:title metadata) "\"")]
    (validate-title-length metadata title)
    (validate-description-length metadata title)))

(defn- validate-articles [articles]
  (run! validate-article (vals articles)))

(defn build-articles
  "Loads all the articles in a directory, then build a map with the url path
  of an article as a key, and the Article as a value. The url-path will be used
  as an initial path to which the article path will be added."
  [url-path markdown-articles-directory]
  (let [articles (load-markdown-articles markdown-articles-directory)
        built-articles (build-articles-from-stasis-map url-path articles)]
      (validate-articles built-articles)
      built-articles))
