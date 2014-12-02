;; ## Generation of the Atom feed
;;
;; The Atom feed can be generated from any combination of posts.
;;
;; The dates are already stored in the ISO 8601 format in the post metadata,
;; so no conversions are needed.
;;
;; The code of this namespace was adapted from https://github.com/magnars/what-the-emacsd.
(ns marcandregoyette.feed
  (:require [clojure.data.xml :as xml]
            [clojure.string :as str]))

(defn- generate-feed-item
  "Builds the XML of an Atom feed entry for a post."
  [urlAndPost]
  (let [metadata (:metadata (val urlAndPost))]
    [:entry
     [:title (:title metadata)]
     [:updated (:date metadata)]
     [:author [:name "Marc-Andr\u00E9 Goyette"]]
     [:link {:href (str "http://www.marcandregoyette.com" (key urlAndPost))}]
     [:id (str "urn:marcandregoyette-com:feed:post:"
               (-> (:title metadata)
                   (str/replace " " "-")
                   (str/lower-case)))]
     [:content {:type "html"} (:content (val urlAndPost))]]))

(defn generate-feed
  "Builds the XML of an Atom feed that includes all the provided posts."
  [postsByUrl]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:id "urn:marcandregoyette-com:feed"]
     [:updated (-> postsByUrl
                   first
                   val
                   :metadata
                   :date)]
     [:title {:type "text"} "Marc-Andr\u00E9 Goyette"]
     [:link {:rel "self" :href "http://www.marcandregoyette.com/atom.xml"}]
     (map generate-feed-item postsByUrl)])))
