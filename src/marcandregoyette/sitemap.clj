(ns marcandregoyette.sitemap
  (:require [clojure.data.xml :as xml]))

(def site-url "https://marcandregoyette.com")

(defn- generate-alternate-link [translation]
  (let [{:keys [lang path]} translation]
    [:xhtml:link {:rel "alternate"
                  :hreflang lang
                  :href (str site-url "/" lang path)}]))

(defn- generate-url-entry [post-by-url]
  (let [metadata (:metadata (val post-by-url))
        {:keys [translations lang]} metadata
        post-url (str site-url (key post-by-url))]
    (into (if translations
            [:url [:loc post-url]
             [:xhtml:link {:rel "alternate"
                           :hreflang lang
                           :href post-url}]]
            [:url [:loc post-url]])
          (map generate-alternate-link translations))))

(def urlset
  [:urlset {:xmlns "http://www.sitemaps.org/schemas/sitemap/0.9"
            :xmlns:xhtml "http://www.w3.org/1999/xhtml"}
   [:url
    [:loc site-url]
    [:xhtml:link {:rel "alternate"
                  :hreflang "en"
                  :href (str site-url "/en")}]
    [:xhtml:link {:rel "alternate"
                  :hreflang "fr"
                  :href site-url}]]])

(defn generate-sitemap [posts-by-url pages-by-url tag-pages-by-url]
  (xml/emit-str
   (xml/sexp-as-element
    (into urlset
          (map
           generate-url-entry
           (concat posts-by-url pages-by-url tag-pages-by-url))))))
