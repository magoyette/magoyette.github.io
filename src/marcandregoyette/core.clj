;; ## A static site generated with Stasis
(ns marcandregoyette.core
  (:require [marcandregoyette.custom-styles :refer [load-custom-styles]]
            [marcandregoyette.feed :refer [feed]]
            [marcandregoyette.pages :refer [add-page-layout]]
            [marcandregoyette.posts :refer [build-posts]]
            [me.raynes.fs :as fs]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.resource :refer [wrap-resource]]
            [stasis.core :as s]))

(def fonts-dir "fonts")
(def images-dir "images")
(def scripts-dir "scripts")
(def styles-dir "styles")
(def resources-dir "resources")
(def public-dir "public")
(def all-public-dir [fonts-dir images-dir scripts-dir styles-dir])
(def export-dir "dist")

(defn- build-pages [posts]
  (s/merge-page-sources
   {:css (load-custom-styles)
    :posts (add-page-layout posts)
    :other {"/atom.xml" (feed posts)}}))

(defn- load-pages []
  (build-pages (build-posts "resources/posts" #"\.md$")))

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
