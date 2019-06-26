(ns marcandregoyette.components
  (:require [marcandregoyette.tags :as tags]
            [marcandregoyette.translations :as translations]
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
        [:div.date.has-text-grey-dark.has-text-right
         (str (translations/translate lang :post/written-on)
              (format-date date lang))])
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

(def menu-en
  [:div.navbar-start
   (menu-item "/en" "Blog")
   (menu-item "/en/about" "About")
   (menu-item "/feeds/languages/en/atom.xml" "Atom/RSS")])

(def menu-fr
  [:div.navbar-start
   (menu-item "/" "Blogue")
   (menu-item "/fr/apropos" "À propos")
   (menu-item "/feeds/languages/fr/atom.xml" "Atom/RSS")])

(rum/defc menu [lang]
  [:nav.navbar.is-primary
   {:aria-label "main navigation"}
   [:div.container
    [:div.navbar-brand.is-family-secondary
     [:a.navbar-item.has-text-weight-bold
      {:href (if (= lang "en") "/en" "/")} "Marc-Andr\u00E9 Goyette"]]
    [:div.navbar-menu.is-active#topNavbar
     (if (= lang "en") menu-en menu-fr)]]])

(rum/defc footer [lang]
  [:footer.footer
   [:p "Copyright © 2019 Marc-André Goyette"]
   [:p
    (translations/translate lang :footer/license-sentence)
    [:a {:rel "license"
         :href (translations/translate lang :footer/license-url)}
     (translations/translate lang :footer/license-name)]
    "."]
   [:p
    (translations/translate lang :footer/disclaimer)]])

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
      (menu lang)
      [:div.container
       (posts-grid posts-content)
       (footer lang)]]])))
