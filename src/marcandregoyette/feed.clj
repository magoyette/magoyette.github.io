;; ## Generation of the Atom feed
;;
;; The Atom feed can be generated from any combination of posts.
;;
;; The dates are already stored in the ISO 8601 format in the post metadata,
;; so no conversions are needed.
(ns marcandregoyette.feed
  (:require [clojure.data.xml :as xml]
            [clojure.string :as string]
            [marcandregoyette.categories :as categories]
            [net.cgrand.enlive-html :as enlive]))

(defn- generate-feed-entry-id
  "Generate a unique urn for the feed entry based on the title of the post and
  the language of its category (to avoid name clashes when the same post is
  translated)."
  [metadata]
  (str "urn:marcandregoyette-com:feed:post:"
       (-> (:title metadata)
           (string/replace " " "-")
           (string/lower-case))
       "-"
       (get-in metadata [:category :lang])))

(defn- remove-post-title [post]
  (enlive/sniptest post [:h2] nil))

(defn- generate-feed-entry-content [content]
  (remove-post-title content))

(defn- generate-feed-entry
  "Builds the XML of an Atom feed entry for a post."
  [urlAndPost]
  (let [content (:content (val urlAndPost))
        metadata (:metadata (val urlAndPost))]
    [:entry
     [:title (:title metadata)]
     [:updated (:date metadata)]
     [:author [:name "Marc-Andr\u00E9 Goyette"]]
     [:link {:href (str "http://www.marcandregoyette.com" (key urlAndPost))}]
     [:id (generate-feed-entry-id metadata)]
     [:content {:type "html"} (generate-feed-entry-content content)]]))

(defn- find-most-recent-date [postsByUrl]
  (-> postsByUrl
      first
      val
      :metadata
      :date))

(defn generate-feed
  "Builds the XML of an Atom feed that includes all the provided posts."
  [postsByUrl]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:id "urn:marcandregoyette-com:feed"]
     [:updated (find-most-recent-date postsByUrl)]
     [:title {:type "text"} "Marc-Andr\u00E9 Goyette"]
     [:link {:rel "self" :href "http://www.marcandregoyette.com/atom.xml"}]
     (map generate-feed-entry postsByUrl)])))
