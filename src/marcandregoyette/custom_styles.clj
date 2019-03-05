;; ## Custom styles
;;
;; Custom styles defined with Garden.
(ns marcandregoyette.custom-styles
  (:require [clojure.string :as string]
            [garden.core :as css]
            [garden.stylesheet :as css-stylesheet]
            [garden.units :as css-units]))

(def coral "#FF7F50")
(def blue "#008EAC")
(def grey "#E8E8E8")
(def green "#03B255")
(def white "#FFFFFF")
(def dark-grey "#2E2E2E")
(def solarized-light-background "#eee8d5")
(def solarized-dark-background "#073642")

(def lato-font
  ["@font-face" {:font-family "Lato"
                 :font-style "normal"
                 :font-weight 400
                 :src "url('../fonts/lato-regular.woff2') format('woff2')"}])

(def html
  [:html {:overflow-y "scroll"}])

(def body
  [:body {:background-color grey
          :font-family ["Lato" "sans-serif"]
          :margin (css-units/px 0)
          :padding (css-units/px 0)}])

(def inverted-menu
  [:.ui.inverted.menu {:background-color blue}])

(def menu
  [:.ui.menu
   [:.header.item {:background-color blue
                   :color white}]
   [".item > .custom-blue.label" {:background-color coral}]
   [:.item {:padding-left (css-units/em 0.8)
            :padding-right (css-units/em 0.8)}]
   [:.item.icon {:padding-left (css-units/em 0.6)
                 :padding-right (css-units/em 0.6)}]])

(def vertical-menu
  [:.ui.stackable.menu
   [".icon.item > .icon" {:margin (css-units/px 0)}]])

(def post-content
  [:.post-content {:padding-bottom "2.5%"
                   :padding-left "5%"
                   :padding-right "5%"
                   :padding-top "2.5%"}
   [:p {:font-family "'Open Sans', sans-serif"}]])

(def footer
  [:.footer {:padding-top (css-units/em 1)
             :padding-bottom (css-units/em 1)}])

(def footer-text
  [:.footer-text {:font-family "'Open Sans', sans-serif"
                  :font-size (css-units/em 0.9)}])

(def site-title
  [:.site-title [:a {:color white
                     :font-size (css-units/px 20)
                     :font-weight "bold"
                     :text-decoration "none"}]])

(def code
  [:.ui.segment.code {:background-color solarized-light-background
                      :overflow "auto"}
   [:code {:font-family (str "Menlo, Monaco, 'DejaVu Sans Mono', Consolas, "
                             "'Courier New', monospace")}]])

(def label
  [:.ui.label {:color white
               :background-color coral
               :font-weight "normal"}
   [:a {:opacity "1"
        :text-decoration "none"}]])

(def css-lines
  [lato-font
   html
   body
   inverted-menu
   menu
   vertical-menu
   post-content
   footer
   footer-text
   label
   site-title
   code
   [:.main.container {:margin-top (css-units/em 2)}]
   [:.ui.large.header {:color dark-grey}]
   [:.ui.orange.label {:background-color coral}]
   [:a.post-title {:text-decoration "none"}]
   [(css-stylesheet/at-media [:.right {:float "none"}])]])

(defn load-custom-styles
  "Load the custom CSS of the site as a string."
  []
  (string/join "\n\n" (map css/css css-lines)))
