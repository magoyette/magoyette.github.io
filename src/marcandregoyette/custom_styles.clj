;; ## Custom styles
;;
;; Custom styles defined with Garden.
(ns marcandregoyette.custom-styles
  (:require [clojure.string :as str]
            [garden.core :refer [css]]
            [garden.stylesheet :refer [at-media]]
            [garden.units :refer [px em]]))

(def coral "#FF7F50")
(def blue "#008EAC")
(def grey "#E8E8E8")
(def green "#03B255")
(def white "#FFFFFF")
(def dark-grey "#2E2E2E")
(def solarized-light-background "#eee8d5")
(def solarized-dark-background "#073642")
(def content-max-width (px 900))

(def body
  [:body {:background-color grey
          :font-family ["Lato" "sans-serif"]
          :font-size (px 18)
          :line-height 1.5
          :margin (px 0)
          :padding (px 0)}])

(defn- grid-at-media [width-type min-width horizontal-padding]
  [(at-media {:screen :only width-type (px min-width)}
             [:.ui.page.grid {:padding (str "0% " horizontal-padding "%")}])])

(def inverted-menu
  [:.ui.inverted.menu {:background-color blue}])

(def menu
  [:.ui.menu
   [:.header.item {:background-color blue
                   :color white}]
   [".item > .custom-blue.label" {:background-color coral}]])

(def post-content
  [:.post-content {:padding-bottom "2.5%"
                   :padding-left "5%"
                   :padding-right "5%"
                   :padding-top "2.5%"}
   [:p {:font-family "Verdana"
        :font-size (px 14)}]])

(def footer
  [:.footer {:font-family "Verdana"
             :font-size (px 12)
             :max-width content-max-width}])

(def css-lines
  [body
   (grid-at-media :max-width 991 0)
   (grid-at-media :min-width 992 0)
   (grid-at-media :min-width 1250 7.5)
   (grid-at-media :min-width 1500 10)
   (grid-at-media :min-width 1750 20)
   (grid-at-media :min-width 2000 22.5)
   inverted-menu
   menu
   post-content
   footer
   [:#posts-container {:max-width content-max-width}]
   [:.ui.large.header {:color dark-grey}]
   [:.ui.orange.label {:background-color coral}]
   [:.ui.label {:color white
                :background-color coral
                :font-weight "normal"}
    [:a {:opacity "1"
         :text-decoration "none"}]]
   [:a.post-title {:text-decoration "none"}]
   [:.right {:float "right"}]
   [:.site-title [:a {:color white
                      :font-size (px 20)
                      :font-weight "bold"
                      :text-decoration "none"}]]
   [:div.disqus-comments {:padding-top (px 10)}
    [:a {:font-size (px 14)}]]
   [:.ui.segment.code {:background-color solarized-light-background
                       :overflow "auto"}
    [:code {:font-family (str "Menlo, Monaco, 'DejaVu Sans Mono', Consolas, "
                              "'Courier New', monospace")
            :font-size (px 13)}]]])

(defn load-custom-styles
  "Load a map with css paths as keys and css styles as values."
  []
  {"/styles/custom-styles.css"
   (str/join "\n\n" (map css css-lines))})
