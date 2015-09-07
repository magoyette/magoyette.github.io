(ns marcandregoyette.pages
  (:require [clojure.string :as str]
            [marcandregoyette.feed :as feed]
            [marcandregoyette.posts :as posts]
            [marcandregoyette.templates :as templates]
            [marcandregoyette.urls :as urls]
            [stasis.core :as stasis]))

(defn- filter-posts [posts predicate]
  (select-keys posts (for [[url post] posts
                           :when (predicate post)]
                       url)))

(defn- has-category [category post]
  (= category (-> post :metadata :category)))

(defn- has-tag [tag post]
  (some #(= tag %) (-> post :metadata :tags)))

(defn- build-index-page [posts]
  (templates/add-page-layout-many-posts posts))

(defn- get-categories [posts]
  (remove #(= % "") (distinct (for [[url post] posts]
                                (-> post :metadata :category)))))

(defn- get-posts-for-category [posts category]
  (templates/add-page-layout-many-posts
   (filter-posts posts (partial has-category category))))

(defn- get-categories-pages [posts]
  (let [categories (get-categories posts)]
    (zipmap (doall (map urls/build-category-url categories))
            (map (partial get-posts-for-category posts) categories))))

(defn- get-tags [posts]
  (distinct (flatten (for [[url post] posts]
                       (-> post :metadata :tags)))))

(defn- get-posts-for-tag [posts tag]
  (templates/add-page-layout-many-posts
   (filter-posts posts (partial has-tag tag))))

(defn- get-tags-pages [posts]
  (let [tags (get-tags posts)]
    (zipmap (doall (map urls/build-tag-url tags))
            (map (partial get-posts-for-tag posts) tags))))

(defn load-pages []
  (let [posts (posts/build-posts "/posts" "resources/posts" #"\.md$")]
    (stasis/merge-page-sources
     {:pages (templates/add-page-layout
              (posts/build-posts ""  "resources/pages" #"\.md$"))
      :posts (templates/add-page-layout posts)
      :index {"/index.html" (build-index-page posts)}
      :categories (get-categories-pages posts)
      :tags (get-tags-pages posts)
      :other {"/atom.xml" (feed/generate-feed posts)}})))
