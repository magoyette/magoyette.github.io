(ns marcandregoyette.pages
  (:require [clojure.string :as str]
            [marcandregoyette.custom-styles :refer [load-custom-styles]]
            [marcandregoyette.feed :refer [generate-feed]]
            [marcandregoyette.posts :refer [build-posts]]
            [marcandregoyette.templates :refer [add-page-layout
                                                add-page-layout-many-posts]]
            [marcandregoyette.urls :refer [build-category-url build-tag-url]]
            [stasis.core :as s]))

(defn- filter-posts [posts predicate]
  (select-keys posts (for [[url post] posts
                           :when (predicate post)]
                       url)))

(defn- has-category [category post]
  (= category (-> post :metadata :category)))

(defn- has-tag [tag post]
  (some #(= tag %) (-> post :metadata :tags)))

(defn- get-normal-posts [posts]
  (filter-posts posts #(not (has-category "" %))))

(defn- build-index-page [posts]
  (-> posts
      get-normal-posts
      add-page-layout-many-posts))

(defn- get-categories [posts]
  (remove #(= % "") (distinct (for [[url post] posts]
                                (-> post :metadata :category)))))

(defn- get-posts-for-category [posts category]
  (add-page-layout-many-posts (filter-posts posts (partial has-category category))))

(defn- get-categories-pages [posts]
  (let [categories (get-categories posts)]
    (zipmap (doall (map build-category-url categories))
            (map (partial get-posts-for-category posts) categories))))

(defn- get-tags [posts]
  (distinct (flatten (for [[url post] posts]
                       (-> post :metadata :tags)))))

(defn- get-posts-for-tag [posts tag]
  (add-page-layout-many-posts (filter-posts posts (partial has-tag tag))))

(defn- get-tags-pages [posts]
  (let [tags (get-tags posts)]
    (zipmap (doall (map build-tag-url tags))
            (map (partial get-posts-for-tag posts) tags))))

(defn load-pages []
  (let [posts (build-posts "/posts" "resources/posts" #"\.md$")]
    (s/merge-page-sources
     {:css (load-custom-styles)
      :pages (add-page-layout (build-posts ""  "resources/pages" #"\.md$"))
      :posts (add-page-layout posts)
      :index {"/index.html" (build-index-page posts)}
      :categories (get-categories-pages posts)
      :tags (get-tags-pages posts)
      :other {"/atom.xml" (-> posts get-normal-posts generate-feed)}})))
