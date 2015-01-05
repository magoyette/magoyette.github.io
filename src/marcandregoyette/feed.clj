;; ## Generation of the Atom feed
;;
;; The Atom feed can be generated from any combination of posts.
;;
;; The dates are already stored in the ISO 8601 format in the post metadata,
;; so no conversions are needed.
(ns marcandregoyette.feed
  (:require [clojure.data.xml :as xml]
            [clojure.string :as str]
            [net.cgrand.enlive-html :as en]))

(defn- generate-feed-entry-id [metadata]
  (str "urn:marcandregoyette-com:feed:post:"
       (-> (:title metadata)
           (str/replace " " "-")
           (str/lower-case))
       "-"
       (:lang metadata)))

(defn- remove-post-title [post]
  (en/sniptest post [:h2] nil))

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
