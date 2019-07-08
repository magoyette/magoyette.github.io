;; ## Generation of the Atom feed
;;
;; The Atom feed can be generated from any combination of posts.
;;
;; The dates are already stored in the ISO 8601 format in the post metadata,
;; so no conversions are needed.
(ns marcandregoyette.feed
  (:require [clojure.data.xml :as xml]
            [clojure.string :as string]))

(defn- generate-feed-entry-id
  "Generate a unique urn for the feed entry based on the title of the post and
  its language (to avoid name clashes when the same post is translated)."
  [metadata]
  (let [{:keys [lang title]} metadata]
    (str "urn:marcandregoyette-com:feed:post:"
         lang
         ":"
       (-> title
        (string/replace " " "-")
        (string/lower-case)))))

(defn- generate-feed-entry
  "Builds the XML of an Atom feed entry for a post."
  [urlAndPost]
  (let [{:keys [content metadata]} (val urlAndPost)
        {:keys [title date]} metadata]
    [:entry
     [:title title]
     [:updated date]
     [:author [:name "Marc-Andr\u00E9 Goyette"]]
     [:link {:href (str "https://marcandregoyette.com" (key urlAndPost))}]
     [:id (generate-feed-entry-id metadata)]
     [:content {:type "html"} content]]))

(defn- find-most-recent-date [postsByUrl]
  (-> postsByUrl
      first
      val
      :metadata
      :date))

(defn generate-feed
  "Builds the XML of an Atom feed that includes all the provided posts."
  [feed-path posts-by-url]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:id "urn:marcandregoyette-com:feed"]
     [:updated (find-most-recent-date posts-by-url)]
     [:title {:type "text"} "Marc-Andr\u00E9 Goyette"]
     [:link {:rel "self" :href (str "https://marcandregoyette.com" feed-path)}]
     (map generate-feed-entry posts-by-url)])))
