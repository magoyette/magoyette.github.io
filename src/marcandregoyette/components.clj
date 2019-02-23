(ns marcandregoyette.components
  (:require [marcandregoyette.categories :as categories]
            [marcandregoyette.tags :as tags]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [clojure.string :as string]
            [rum.core :as rum])
  (:import java.util.Locale))

(def iso-8601-date-formatter (time-format/formatters :date-time-no-ms))

(def en-date-formatter
  (time-format/with-locale
    (time-format/formatter "dd MMMM YYYY") Locale/CANADA))

(def fr-date-formatter
  (time-format/with-locale
    (time-format/formatter "dd MMMM YYYY") Locale/CANADA_FRENCH))

(defn- format-date
  [date language]
  (->> date
       (time-format/parse iso-8601-date-formatter)
       (time-format/unparse
        (if (= language "fr") fr-date-formatter en-date-formatter))))

(rum/defc post-content [url metadata content]
  [:div.post-content
   {:dangerouslySetInnerHTML
    {:__html
     (str
      (rum/render-static-markup
       [:a.post-title {:href url} [:h2.ui.large.header (:title metadata)]])
      content)}}])

(defn render-post-content [url metadata content]
  (rum/render-static-markup (post-content url metadata content)))

(rum/defc post-layout [url metadata content]
  (let [date (:date metadata)
        language (get-in metadata [:category :lang])
        category (:category metadata)
        tags (:tags metadata)]
    [:div.ui.segment
     [:div.ui.ribbon.large.label.post-category
      (if (not (some #{category} categories/categories))
        [:div]
        [:a {:href (categories/build-category-url category)} (:name category)])]
     (if (string/blank? date)
       [:div]
       [:div.ui.top.right.attached.large.label.post-date
        (format-date date language)])
     (post-content url metadata content)
     [:div.post-tags
      (for [tag tags]
        (let [tag-url (tags/build-tag-url tag)]
          [:div.ui.large.label [:a.label {:href tag-url} tag]]))]]))

(defn render-post-layout [url metadata content]
  (rum/render-static-markup (post-layout url metadata content)))

(defn- include-meta []
  (seq [[:meta {:charset "utf-8"}]
        [:meta {:http-equiv "X-UA-Compatible"
                :content "IE=edge"}]
        [:meta {:name "viewport"
                :content "width=device-width, initial-scale=1.0"}]
        [:meta {:name "author"
                :content "Marc-Andr\u00E9 Goyette"}]]))

(rum/defc head [title]
  [:head
   [:title title]
   (include-meta)
   [:link {:type "text/css"
           :rel "stylesheet"
           :href (str "https://fonts.googleapis.com/css?family=Lato"
                      (java.net.URLEncoder/encode "|" "UTF-8")
                      "Open+Sans")}]
      [:link {:type "text/css"
           :rel "stylesheet"
           :href "/bundles/styles.css"}]])

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
  [category active-category]
  {:type :text-link
   :name (:name category)
   :link (categories/build-category-url category)
   :id (categories/build-category-id category)
   :element (if (= (:name category) (:name active-category))
              :a.item.active :a.item)})

(defn- build-menu-item [item]
  (let [href {:href (:link item) :title (:title item) :id (:id item)}]
    (case (:type item)
      :text-link [(:element item) href (:name item)]
      :icon-link [:a.icon.item href (:icon item)])))

(defn- menu-items [active-category]
  (concat
   (map #(category-menu-item % active-category)
        (categories/get-visible-categories))
   menu-items-without-categories))

(defn- menu [active-category]
  [:div.ui.inverted.stackable.borderless.menu
   [:div.ui.text.container
    [:div.item
     [:div.site-title
      [:a {:href "/"} "Marc-Andr\u00E9 Goyette"]]]
    (map build-menu-item (menu-items active-category))]])

(defn get-license []
  [:div.license-icon
   [:a
    {:rel "license"
     :href "https://creativecommons.org/licenses/by-sa/4.0/"}
    [:img {:alt "Creative Commons License"
           :src "https://i.creativecommons.org/l/by-sa/4.0/88x31.png"
           :style {:border-width 0}}]]
   [:br]
   "This work by Marc-Andr\u00E9 Goyette is licensed under a "
   [:a {:rel "license"
        :href "https://creativecommons.org/licenses/by-sa/4.0/"}
    "Creative Commons License"]
   "."])

(defn- footer []
  [:div.footer
   [:div.ui.segment.secondary
    [:div.footer-text
     (get-license)
     "Opinions and views expressed on this site are solely my own, "
     "not those of my present or past employers."]]])

(defn- post-grid [posts-content]
  [:div.ui.main.text.container
   [:div#posts-container
    {:dangerouslySetInnerHTML
     {:__html posts-content}}]
   [:div (footer)]])

(defn get-page-layout [title lang active-category posts-content]
  (str
   "<!DOCTYPE html>"
   (rum/render-static-markup
    [:html {:lang lang}
     (head title)
     [:body
      (menu active-category)
      (post-grid posts-content)]])))
