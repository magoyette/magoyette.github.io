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
  (:require [marcandregoyette.pages :as pages]
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
(def export-dir "dist")

(defn get-assets []
  (assets/load-assets "public"
                      [#"/styles/.*\.css"
                       #"/images/.*\.png"
                       #"/themes/*"]))

(def app (-> (stasis/serve-pages (pages/load-pages))
             (optimus/wrap get-assets
                           optimizations/all
                           strategies/serve-live-assets)
             (ring-content-type/wrap-content-type)
             (ring-not-modified/wrap-not-modified)))

(defn- copy-public-dir [subfolder]
  (fs/copy-dir-into (fs/file "." resources-dir public-dir subfolder)
                    (fs/file "." export-dir subfolder)))

(defn export
  "Export the generated web site into an export directory.
  Intended to be called by Leiningen."
  []
  (stasis/empty-directory! export-dir)
  (export/save-assets (optimizations/all (get-assets) {}) export-dir)
  (stasis/export-pages (pages/load-pages) export-dir))
