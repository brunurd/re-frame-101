(ns counter.core
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]))

; Initialize the db.
(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:number 0}))

; Query the data.
(rf/reg-sub
 :count
 (fn [db _]
   (:number db)))

(defn handler-up
  "Count up a value."
  [coeffects event]
  (let [payload (second event)
        db (:db coeffects)]
    {:db (assoc db :number (+ (:number db) payload))}))

(defn handler-down
  "Count down a value."
  [coeffects event]
  (let [payload (second event)
        db (:db coeffects)]
    {:db (assoc db :number (- (:number db) payload))}))

(rf/reg-event-fx
 :count-up
 handler-up)

(rf/reg-event-fx
 :count-down
 handler-down)

(defn counter 
  "Single component app for re-frame test."
  []
  [:div {:style {:display "flex"}}
   [:button {:on-click #(rf/dispatch [:count-down 1])} "-"]
   [:p {} @(rf/subscribe [:count])]
   [:button {:on-click #(rf/dispatch [:count-up 1])} "+"]])

(defn start
  "The start function. Here start the reagent render and the re-frame."
  []
  (rf/dispatch-sync [:initialize])
  (r/render-component [counter]
                      (. js/document (getElementById "app"))))

(defn ^:export init
  []
  (start))

(defn stop []
  (js/console.log "stop"))
