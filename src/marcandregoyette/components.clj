(ns marcandregoyette.components
  (:require [marcandregoyette.tags :as tags]
            [clojure.string :as string]
            [rum.core :as rum])
  (:import java.time.OffsetDateTime
           java.time.format.DateTimeFormatter
           java.util.Locale))

(defn- format-date
  [date language]
  (let [locale (if (= language "fr") Locale/CANADA_FRENCH Locale/CANADA)]
    (->> (OffsetDateTime/parse date DateTimeFormatter/ISO_DATE_TIME)
         (.format (DateTimeFormatter/ofPattern "dd MMMM YYYY" locale)))))

(rum/defc post-content [url metadata content]
  [:div.content
   {:dangerouslySetInnerHTML
    {:__html
     (str
      (rum/render-static-markup
       [:a {:href url} [:h1.title.is-family-secondary (:title metadata)]])
      content)}}])

(rum/defc post-layout [url metadata content]
  (let [{:keys [date lang tags]} metadata]
    [:article.card.post
     [:div.card-content
      (if (string/blank? date)
        [:div]
        [:div.has-text-grey-dark.has-text-right
         (str "Written on " (format-date date lang))])
      (post-content url metadata content)
      [:div.tags
       (for [tag (:tags metadata)]
         (let [tag-url (tags/build-tag-url tag)]
           [:a.tag.is-medium {:href tag-url} tag]))]]]))

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
           :href "/styles/styles.css"}]])

(defn- menu-item
  [href name]
  [:a.navbar-item.is-tab {:href href} name])

(rum/defc menu []
  [:nav.navbar.is-primary
   {:role "navigation" :aria-label "main navigation"}
   [:div.container
    [:div.navbar-brand.is-family-secondary
     [:a.navbar-item.has-text-weight-bold
      {:href "/"} "Marc-Andr\u00E9 Goyette"]]
    [:div.navbar-menu.is-active#topNavbar
     [:div.navbar-start
      (menu-item "/" "Blog")
      (menu-item "/en/about" "About")
      (menu-item "/feeds/languages/en/atom.xml" "Atom/RSS")
      (menu-item "/source" "Source")]]]])

(rum/defc footer []
  [:footer.footer
   [:a
    {:rel "license"
     :href "https://creativecommons.org/licenses/by-sa/4.0/"}
    [:img {:alt "Creative Commons License"
           :src "https://licensebuttons.net/l/by-sa/4.0/88x31.png"
           :style {:border-width 0}}]]
   [:p
    "This work by Marc-Andr\u00E9 Goyette is licensed under a "
    [:a {:rel "license"
         :href "https://creativecommons.org/licenses/by-sa/4.0/"}
     "Creative Commons License"]
    "."]
   [:p
    "Opinions and views expressed on this site are solely my own, "
    "not those of my present or past employers."]])

(defn- posts-grid [posts-content]
  [:main
    {:dangerouslySetInnerHTML
     {:__html posts-content}}])

(defn get-page-layout [title lang posts-content]
  (str
   "<!DOCTYPE html>"
   (rum/render-static-markup
    [:html {:lang lang}
     (head title)
     [:body
      (menu)
      [:div.container
       (posts-grid posts-content)
       (footer)]]])))
