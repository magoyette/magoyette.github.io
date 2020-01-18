(ns marcandregoyette.components
  (:require [marcandregoyette.tags :as tags]
            [marcandregoyette.translations :as translations]
            [clojure.string :as string]
            [rum.core :as rum])
  (:import java.time.OffsetDateTime
           java.time.format.DateTimeFormatter
           java.util.Locale))

(defn- format-date
  ([date language]
   (format-date date language "dd MMMM YYYY"))
  ([date language format]
   (let [locale (if (= language "fr") Locale/CANADA_FRENCH Locale/CANADA)]
     (.format
      (DateTimeFormatter/ofPattern format locale)
      (OffsetDateTime/parse date DateTimeFormatter/ISO_DATE_TIME)))))

(rum/defc article-content [url metadata content]
  [:div.content
   {:dangerouslySetInnerHTML
    {:__html
     (str
      (rum/render-static-markup
       [:a {:href url} [:h1.title.is-family-secondary (:title metadata)]])
      content)}}])

(defn- build-tag
  [tag lang]
  (let [tag-url (tags/build-tag-url tag lang)]
    [[:a.tag.is-primary.is-large {:href tag-url} tag] " "]))

(rum/defc article-layout [url metadata content]
  (let [{:keys [date lang tags]} metadata]
    [:article.card
     [:div.card-content
      (if (string/blank? date)
        [:div]
        [:div.date.has-text-grey-dark.has-text-right
         (str (translations/translate lang :article/written-on)
              (format-date date lang))])
      (article-content url metadata content)
      (if (seq tags)
        [:div.tags.has-addons
         [:span.tag.is-large.is-dark "Tags"]
         (mapcat #(build-tag % lang) tags)])]]))

(defn- get-page-description-or-default [description lang]
  (or description (translations/translate lang :page/description)))

(defn- include-meta [lang description]
  (seq [[:meta {:charset "utf-8"}]
        [:meta {:http-equiv "X-UA-Compatible"
                :content "IE=edge"}]
        [:meta {:name "viewport"
                :content "width=device-width, initial-scale=1.0"}]
        [:meta {:name "author"
                :content "Marc-Andr\u00E9 Goyette"}]
        [:meta {:name "description"
                :content (get-page-description-or-default description lang)}]]))

(rum/defc head [title lang description]
  [:head
   [:title title]
   (include-meta lang description)
   [:link {:type "text/css"
           :rel "stylesheet"
           :href "/styles.css"}]])

(defn- menu-item
  [href name]
  [[:a.navbar-item.is-tab {:href href} name] " "])

(def menu-items-en
  {"/en/articles" "Articles"
   "/en/about" "About"
   "/feeds/languages/en/atom.xml" "Atom/RSS"})

(def menu-items-fr
  {"/fr/articles" "Articles"
   "/fr/a-propos" "À propos"
   "/feeds/languages/fr/atom.xml" "Atom/RSS"})

(defn- build-menu
  [menu-items]
  [:div.navbar-start
   (mapcat #(menu-item (key %) (val %)) menu-items)])

(rum/defc menu [lang]
  [:nav.navbar.is-primary
   {:aria-label "main navigation"}
   [:div.container
    [:div.navbar-brand.is-family-secondary
     [:a.navbar-item.has-text-weight-bold
      {:href (if (= lang "en") "/en" "/")} "Marc-Andr\u00E9 Goyette"]]
    [:div.navbar-menu.is-active#topNavbar
     (build-menu
      (if (= lang "en") menu-items-en menu-items-fr))]]])

(rum/defc footer [lang]
  [:footer.footer
   [:p "Copyright © 2019-2020 Marc-André Goyette | "
    [:a {:href "https://github.com/magoyette/magoyette.github.io"}
     (translations/translate lang :footer/source-code)]]
   [:p
    (translations/translate lang :footer/license-sentence)
    [:a {:rel "license"
         :href (translations/translate lang :footer/license-url)}
     (translations/translate lang :footer/license-name)]
    "."]
   [:p
    (translations/translate lang :footer/disclaimer)]])

(defn get-page-layout [title lang description url article]
  (let [{:keys [metadata content]} article]
    (str
     "<!DOCTYPE html>"
     (rum/render-static-markup
      [:html {:lang lang}
       (head title lang description)
       [:body
        (menu lang)
        [:div.container
         [:main
          (article-layout url metadata content)]
         (footer lang)]]]))))

(rum/defc article-link-layout [url metadata]
  (let [{:keys [date lang tags]} metadata]
    (when-not (string/blank? date)
      [:p
       [:span.article-date.has-text-grey-dark
        (format-date date lang "YYYY-MM-dd")]
       "  "
       [:a {:href url} (:title metadata)]])))

(defn- get-articles-page-title [lang tag]
  (str "Articles"
       (if tag (str (translations/translate lang :articles/for-tag) tag))))

(rum/defc articles-links [lang tag articles-by-url]
  [:article.card
   [:div.card-content
    [:h1.title.is-family-secondary (get-articles-page-title lang tag)]
    (map #(article-link-layout (key %) (:metadata (val %))) articles-by-url)]])

(defn get-articles-page-layout [title lang tag articles-by-url]
  (str
   "<!DOCTYPE html>"
   (rum/render-static-markup
    [:html {:lang lang}
     (head title lang nil)
     [:body
      (menu lang)
      [:div.container
       [:main
        (articles-links lang tag articles-by-url)]
       (footer lang)]]])))
