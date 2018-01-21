(ns super-human-mobile.core
    (:use-macros [purnam.core :only [def*]])
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [super-human-mobile.handlers]
              [super-human-mobile.subs]))

(def ReactNative (js/require "react-native"))

(def firebase (js/require "firebase"))

(def* firebase-config
  {:apiKey        "AIzaSyAgafACEe0ZwHnv-Lc-A2Iq3MmQGhuFEHg"
   :authDomain    "super-human-4913b.firebaseapp.com"
   :databaseURL   "https://super-human-4913b.firebaseio.com"
   :storageBucket "super-human-4913b.appspot.com"})

(js/firebase.initializeApp firebase-config)

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(defn alert [title]
  (.alert Alert title))

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [image {:source (js/require "./assets/images/cljs.png")
               :style {:width 200
                       :height 200}}]
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @greeting]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(alert "HELLO!")}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
