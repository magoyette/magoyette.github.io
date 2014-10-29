;; ## Generation of an Atom feed
;;
;; The Atom feed includes all posts from the blog.
;;
;; The dates are already stored in the ISO 8601 format in the post metadata,
;; so no conversions are needed.
(ns marcandregoyette.feed
  (:require [clojure.data.xml :as xml]
            [clojure.string :as str]))

(defn- feed-item
  "Builds the XML of an Atom feed entry from a post."
  [post]
  (let [metadata (:metadata (val post))]
    [:entry
     [:title (:title metadata)]
     [:updated (:date metadata)]
     [:author [:name "Marc-Andr\u00E9 Goyette"]]
     [:link {:href (str "http://www.marcandregoyette.com" (key post))}]
     [:id (str "urn:marcandregoyette-com:feed:post:"
               (-> (:title metadata)
                   (str/replace " " "-")
                   (str/lower-case)))]
     [:content {:type "html"} (:content post)]]))

(defn feed
  "Builds the XML of an Atom feed that include all posts."
  [posts]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:id "urn:marcandregoyette-com:feed"]
     [:updated (-> posts
                   first
                   val
                   :metadata
                   :date)]
     [:title {:type "text"} "Marc-Andr\u00E9 Goyette"]
     [:link {:rel "self" :href "http://www.marcandregoyette.com/atom.xml"}]
     (map feed-item posts)])))
