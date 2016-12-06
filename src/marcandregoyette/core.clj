;; ## A static site generated with Stasis
;;
;; This is the annotated source code of
;; [marcandregoyette.com](http://www.marcandregoyette.com).
;;
;; The full source code, including project.clj and the tests is available at
;; the [GitHub repository](https://github.com/magoyette/marcandregoyette.com).
;;
;; This website is written in Clojure and generated with the help of
;; [Stasis](https://github.com/magnars/stasis).
;;
;; Some parts of the code are forked from the excellent article
;; ["Building Static Sites in Clojure with
;; Stasis"](http://cjohansen.no/building-static-sites-in-clojure-with-stasis)
;; and the source of
;; [whattheemacsd.com](https://github.com/magnars/what-the-emacsd).
(ns marcandregoyette.core
  (:require [marcandregoyette.custom-styles :as custom-styles]
            [marcandregoyette.pages :as pages]
            [me.raynes.fs :as fs]
            [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus.export :as export]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :as strategies]
            [ring.middleware.content-type :as ring-content-type]
            [ring.middleware.file-info :as ring-file-info]
            [ring.middleware.not-modified :as ring-not-modified]
            [ring.middleware.resource :as ring-resource]
            [stasis.core :as stasis]))

(def images-dir "images")
(def styles-dir "styles")
(def resources-dir "resources")
(def themes-dir "themes")
(def public-dir "public")
(def all-public-dir [images-dir styles-dir themes-dir])
(def cname-file "CNAME")
(def export-dir "dist")

(defn get-assets []
  (concat
   (assets/load-bundle "public"
                       "styles.css"
                       ["/styles/semantic.min.css"
                        "/styles/solarized-light.css"])
   (assets/load-assets "public"
                       [#"/images/.*\.png"
                        #"/themes/*"])
   [{:path "/styles/custom-styles.css"
     :contents (custom-styles/load-custom-styles)
     :bundle "styles.css"}]))

(defn optimizations [assets options]
  (-> assets
      (optimizations/minify-js-assets options)
      (optimizations/minify-css-assets options)
      (optimizations/inline-css-imports)
      (optimizations/concatenate-bundles)))

(def app (-> (stasis/serve-pages (pages/load-pages))
             (optimus/wrap get-assets
                           optimizations
                           strategies/serve-live-assets)
             (ring-content-type/wrap-content-type)))

(defn- copy-public-dir [subfolder]
  (fs/copy-dir-into (fs/file "." resources-dir public-dir subfolder)
                    (fs/file "." export-dir subfolder)))

(defn export
  "Export the generated web site into an export directory.
  Intended to be called by Leiningen."
  []
  (stasis/empty-directory! export-dir)
  (export/save-assets (optimizations (get-assets) {}) export-dir)
  (stasis/export-pages (pages/load-pages) export-dir)
  (fs/copy (str resources-dir "/" cname-file) (str export-dir "/" cname-file)))
