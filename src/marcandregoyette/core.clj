;; ## A static site generated with Stasis
;;
;; This is the annotated source code of [marcandregoyette.com](http:www.marcandregoyette.com).
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
  (:require [marcandregoyette.pages :refer [load-pages]]
            [me.raynes.fs :as fs]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.resource :refer [wrap-resource]]
            [stasis.core :as s]))

(def images-dir "images")
(def styles-dir "styles")
(def resources-dir "resources")
(def themes-dir "themes")
(def public-dir "public")
(def all-public-dir [images-dir styles-dir themes-dir])
(def export-dir "dist")

(def app (-> (s/serve-pages (load-pages))
             (wrap-resource "public")
             (wrap-file-info)
             (wrap-content-type)))

(defn- copy-public-dir [subfolder]
  (fs/copy-dir-into (fs/file "." resources-dir public-dir subfolder)
                    (fs/file "." export-dir subfolder)))

(defn export
  "Export the generated web site into an export directory.
  Intended to be called by Leiningen."
  []
  (s/empty-directory! export-dir)
  (s/export-pages (load-pages) export-dir)
  (dorun (map copy-public-dir all-public-dir)))
