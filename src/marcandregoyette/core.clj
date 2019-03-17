;; ## A static site generated with Stasis
;;
;; This is the annotated source code of
;; [marcandregoyette.com](https://marcandregoyette.com).
;;
;; The full source code, including project.clj and the tests is available at
;; the [GitHub repository](https://github.com/magoyette/magoyette.github.io).
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
            [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            [ring.middleware.content-type :as ring-content-type]
            [ring.middleware.resource :as ring-resource]
            [stasis.core :as stasis]))

(def resources-dir "resources")
(def cname-file "CNAME")
(def export-dir "dist")

(def app (-> (stasis/serve-pages (pages/load-pages))
             (ring-resource/wrap-resource "public")
             (ring-content-type/wrap-content-type)))

(defn export
  "Export the generated web site into an export directory.
  Intended to be called by Leiningen."
  []
  (stasis/empty-directory! export-dir)
  (stasis/export-pages (pages/load-pages) export-dir)
  (fs/copy-dir (str resources-dir "/public/fonts") export-dir)
  (fs/copy-dir (str resources-dir "/public/styles") export-dir)
  (fs/copy (str resources-dir "/" cname-file) (str export-dir "/" cname-file)))
