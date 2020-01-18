;; ## Generation of the Atom feed
;;
;; The Atom feed can be generated from any combination of articles.
;;
;; The dates are already stored in the ISO 8601 format in the article metadata,
;; so no conversions are needed.
(ns marcandregoyette.feed
  (:require [marcandregoyette.tags :as tags]
            [clojure.data.xml :as xml]
            [clojure.string :as string]))

(defn- generate-feed-id
  "Generate a unique id for the feed entry based on the language and tag."
  [lang feed-tag]
    (str "urn:marcandregoyette-com:feed:languages:"
         lang
         (if feed-tag (str ":tags:" (tags/get-tag-for-url feed-tag)) "")))

(defn- generate-feed-title
  "Generate a unique id for the feed entry based on the language and tag."
  [feed-tag]
  (str "Marc-Andr\u00E9 Goyette"
       (if feed-tag (str " - " feed-tag) "")))

(defn- generate-feed-entry-id
  "Generate a unique urn for the feed entry based on the title of the article and
  its language (to avoid name clashes when the same article is translated)."
  [metadata]
  (let [{:keys [lang title]} metadata]
    (str "urn:marcandregoyette-com:feed:languages:" lang ":"
       (string/lower-case (string/replace title " " "-")))))

(defn- generate-feed-entry
  "Builds the XML of an Atom feed entry for an article."
  [urlAndArticle]
  (let [{:keys [content metadata]} (val urlAndArticle)
        {:keys [title date]} metadata]
    [:entry
     [:title title]
     [:updated date]
     [:author [:name "Marc-Andr\u00E9 Goyette"]]
     [:link {:href (str "https://marcandregoyette.com" (key urlAndArticle))}]
     [:id (generate-feed-entry-id metadata)]
     [:content {:type "html"} content]]))

(defn- find-most-recent-date [articlesByUrl]
  (-> articlesByUrl
      first
      val
      :metadata
      :date))

(defn generate-feed
  "Builds the XML of an Atom feed that includes all the provided articles."
  ([feed-path articles-by-url feed-lang feed-tag]
   (xml/emit-str
    (xml/sexp-as-element
     [:feed {:xmlns "http://www.w3.org/2005/Atom"}
      [:id (generate-feed-id feed-lang feed-tag)]
      [:updated (find-most-recent-date articles-by-url)]
      [:title {:type "text"} (generate-feed-title feed-tag)]
      [:link {:rel "self" :href (str "https://marcandregoyette.com" feed-path)}]
      (map generate-feed-entry articles-by-url)]))))
