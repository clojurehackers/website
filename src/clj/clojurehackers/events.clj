(ns clojurehackers.events
  (:require
   [clojure.edn :as edn])
  (:import
   (java.time LocalDate)
   (java.time.format DateTimeFormatter)
   (java.util Locale)))

(def ^:private date-formatter
  (DateTimeFormatter/ofPattern "d 'de' MMMM 'de' yyyy" (Locale/forLanguageTag "pt-BR")))

(defn load-events [path]
  (->> (slurp path)
       edn/read-string
       (sort-by :date)
       (map-indexed (fn [i event] (assoc event :number (inc i))))))

(defn format-date [iso-date]
  (.format (LocalDate/parse iso-date) date-formatter))
