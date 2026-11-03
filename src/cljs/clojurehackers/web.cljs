(ns clojurehackers.web)

(defn- today []
  (.slice (.toISOString (js/Date.)) 0 10))

(defn- empty-message [list-id]
  (if (= list-id "upcoming-events")
    "No upcoming meetups yet."
    "No past meetups yet."))

(defn- add-empty-state [list]
  (when (zero? (.-childElementCount list))
    (let [li (.createElement js/document "li")]
      (set! (.-className li) "empty")
      (set! (.-textContent li) (empty-message (.-id list)))
      (.appendChild list li))))

(defn- categorize-events []
  (let [all (.getElementById js/document "all-events")
        upcoming (.getElementById js/document "upcoming-events")
        past (.getElementById js/document "past-events")
        items (js/Array.from (.-children all))
        now (today)]
    (doseq [item items]
      (let [date (.getAttribute item "data-date")]
        (if (neg? (compare date now))
          (.insertBefore past item (.-firstChild past))
          (.appendChild upcoming item))))
    (add-empty-state upcoming)
    (add-empty-state past)
    (set! (.-hidden all) true)
    (set! (.-hidden (.getElementById js/document "tabbed-events")) false)))

(defn- select-tab [tabs tab]
  (doseq [other tabs]
    (let [selected? (= other tab)
          panel (.getElementById js/document (.getAttribute other "aria-controls"))]
      (.setAttribute other "aria-selected" (str selected?))
      (set! (.-tabIndex other) (if selected? 0 -1))
      (set! (.-hidden panel) (not selected?))
      (.toggle (.-classList panel) "is-active" selected?))))

(defn- init-tabs []
  (let [tabs (array-seq (.querySelectorAll js/document ".event-tab"))]
    (doseq [tab tabs]
      (.addEventListener tab "click" (fn [_] (select-tab tabs tab))))))

(defn- init []
  (categorize-events)
  (init-tabs))

(.addEventListener js/document "DOMContentLoaded" init)
