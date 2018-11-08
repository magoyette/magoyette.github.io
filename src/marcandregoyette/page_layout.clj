(ns marcandregoyette.page-layout
  (:require [hiccup.element :as element]
            [hiccup.page :as page]
            [optimus.link :as link]
            [marcandregoyette.categories :as categories]))

(defn- include-meta []
  (seq [[:meta {:charset "utf-8"}]
        [:meta {:http-equiv "X-UA-Compatible"
                :content "IE=edge"}]
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
           :href (str "https://fonts.googleapis.com/css?family=Lato"
                      (java.net.URLEncoder/encode "|" "UTF-8")
                      "Open+Sans")}]
   (page/include-css "/bundles/styles.css")])

(def menu-items-without-categories
  [{:type :icon-link
    :icon [:i.help.large.icon]
    :link "/about"
    :title "About / À propos"
    :id (categories/build-category-id (categories/get-category-by-name "About"))}
   {:type :icon-link
    :icon [:i.rss.large.icon]
    :link "/atom.xml"
    :title "Subscribe to the feed of this blog"}
   {:type :icon-link
    :icon [:i.code.large.icon]
    :link "/source"
    :title "Source code"}
   {:type :icon-link
    :icon [:i.linkedin.large.icon]
    :link "https://www.linkedin.com/in/marcandregoyette"
    :title "LinkedIn"}
   {:type :icon-link
    :icon [:i.github.alternate.large.icon]
    :link "https://github.com/magoyette"
    :title "GitHub"}])

(defn- category-menu-item
  [category]
  {:type :text-link
   :name (:name category)
   :link (categories/build-category-url category)
   :id (categories/build-category-id category)})

(defn- build-menu-item [item]
  (let [href {:href (:link item) :title (:title item) :id (:id item)}]
    (case (:type item)
      :text-link [:a.item href (:name item)]
      :icon-link [:a.icon.item href (:icon item)])))

(defn- menu-items []
  (concat
   (map category-menu-item (categories/get-visible-categories))
   menu-items-without-categories))

(defn- menu []
  [:div.ui.inverted.stackable.borderless.menu
   [:div.ui.text.container
    [:div.item
     [:div.site-title
      (element/link-to "/" "Marc-Andr\u00E9 Goyette")]]
    (map build-menu-item (menu-items))]])

(defn get-license []
  [:div.license-icon
   [:a
    {:rel "license"
     :href "https://creativecommons.org/licenses/by-sa/4.0/"}
    (element/image {:style "border-width:0"}
                   "https://i.creativecommons.org/l/by-sa/4.0/88x31.png"
                   "Creative Commons License")]
   [:br]
   "This work by "
   [:a {(keyword "xmlns:cc") "https://creativecommons.org/ns#"
        :href "https://marcandregoyette.com"
        :property "cc:attributionName"
        :rel "cc:attributionURL"}
    "Marc-Andr\u00E9 Goyette"]
   " is licensed under a "
   [:a {:rel "license"
        :href "https://creativecommons.org/licenses/by-sa/4.0/"}
    "Creative Commons Attribution-ShareAlike 4.0 International License"]
   "."])

(defn- footer []
  [:div.footer
   [:div.ui.segment.secondary
    [:div.footer-text
     (get-license)
     "Built with Clojure and Semantic UI ("
     (element/link-to "/source" "Source code")
     " | "
     (element/link-to "https://github.com/magoyette" "GitHub")
     ")"]]])

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
