(ns marcandregoyette.page-layout
  (:require [hiccup.element :as element]
            [hiccup.page :as page]
            [optimus.link :as link]
            [marcandregoyette.urls :as urls]))

(defn- include-meta []
  (seq [[:meta {:charset "utf-8"}]
        [:meta {:http-equiv "X-UA-Compatible"
                :content "IE=edge,chrome=1"}]
        [:meta {:name "viewport"
                :content "width=device-width, initial-scale=1.0"}]
        [:meta {:name "author"
                :content "Marc-Andr\u00E9 Goyette"}]]))

(def head
  [:head
   [:title]
   (include-meta)
   [:link {:type "text/css"
           :rel "stylesheet"
           :href "http://fonts.googleapis.com/css?family=Lato|Open+Sans"}]
   (page/include-css "/bundles/styles.css")])

(def menu-items-without-categories
  [{:type :icon-link
    :icon [:i.help.large.icon]
    :link "/about"
    :title "About / À propos"
    :id (urls/build-category-id "About")}
   {:type :icon-link
    :icon [:i.rss.large.icon]
    :link "http://feeds.feedburner.com/marcandregoyette"
    :title "Subscribe to the feed of this blog"}
   {:type :icon-link
    :icon [:i.code.large.icon]
    :link "/source"
    :title "Source code"}
   {:type :icon-link
    :icon [:i.linkedin.large.icon]
    :link "http://www.linkedin.com/in/marcandregoyette"
    :title "LinkedIn"}
   {:type :icon-link
    :icon [:i.github.alternate.large.icon]
    :link "https://github.com/magoyette"
    :title "GitHub"}])

(defn- category-menu-item
  [category]
  {:type :text-link
   :name category
   :link (urls/build-category-url category)
   :id (urls/build-category-id category)})

(defn- build-menu-item [item]
  (let [href {:href (:link item) :title (:title item) :id (:id item)}]
    (case (:type item)
      :text-link [:a.item href (:name item)]
      :icon-link [:a.icon.item href (:icon item)])))

(defn- menu-items []
  (concat
   (map category-menu-item urls/categories)
   menu-items-without-categories))

(defn- menu []
  [:div.ui.inverted.stackable.borderless.menu
   [:div.ui.text.container
    [:div.item
     [:div.site-title
      (element/link-to "/" "Marc-Andr\u00E9 Goyette")]]
    (map build-menu-item (menu-items))]])

(defn- footer []
  [:div.footer
   [:div.ui.segment.secondary
    [:div
     "Copyright \u00A9 "
     (element/link-to "/about" "Marc-Andr\u00E9 Goyette")
     [:div.right
      "Built with Clojure and Semantic UI ("
      (element/link-to "/source" "Source code")
      " | "
      (element/link-to "https://github.com/magoyette" "GitHub")
      ")"]]]])

(defn- post-grid []
  [:div.ui.main.text.container
   [:div#posts-container]
   [:div (footer)]])

(defn get-page-layout []
  (page/html5 head
              [:body
               (menu)
               (post-grid)
               [:div.additional-scripts]]))
