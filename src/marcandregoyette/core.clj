;; ## A static site generated with Stasis
;;
;; This is the annotated source code of [marcandregoyette.com](http://www.marcandregoyette.com).
;;
;; The full source code, including project.clj and the tests is available at
;; the [GitHub repository](https://github.com/magoyette/marcandregoyette.com).
;;
;; This website is written in Clojure and generated with the help of
;; [Stasis](https://github.com/magnars/stasis).
;;
;; Some parts of the code are forked from the excellent article
;; ["Building Static Sites in Clojure with Stasis"](http://cjohansen.no/building-static-sites-in-clojure-with-stasis) and the source of
;; [whattheemacsd.com](https://github.com/magnars/what-the-emacsd).
(ns marcandregoyette.core
  (:require [marcandregoyette.pages :as pages]
            [me.raynes.fs :as fs]
            [ring.middleware.content-type :as ring-content-type]
            [ring.middleware.file-info :as ring-file-info]
            [ring.middleware.resource :as ring-resource]
            [stasis.core :as stasis]))

(def images-dir "images")
(def styles-dir "styles")
(def resources-dir "resources")
(def themes-dir "themes")
(def public-dir "public")
(def all-public-dir [images-dir styles-dir themes-dir])
(def export-dir "dist")

(def app (-> (stasis/serve-pages (pages/load-pages))
             (ring-resource/wrap-resource "public")
             (ring-file-info/wrap-file-info)
             (ring-content-type/wrap-content-type)))

(defn- copy-public-dir [subfolder]
  (fs/copy-dir-into (fs/file "." resources-dir public-dir subfolder)
                    (fs/file "." export-dir subfolder)))

(defn export
  "Export the generated web site into an export directory.
  Intended to be called by Leiningen."
  []
  (stasis/empty-directory! export-dir)
  (stasis/export-pages (pages/load-pages) export-dir)
  (dorun (map copy-public-dir all-public-dir)))
