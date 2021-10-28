(ns ninja.cli.utils
  (:refer-clojure :exclude [format])
  (:require
    #?@(:clj [[clojure.core :as c]]
        :cljs
        [[goog.string :as gstr]
         [goog.string.format]])))


(def format
  #?(:clj c/format
     :cljs gstr/format))


(defn index-by
  [f coll]
  (->> coll
       (reduce
         (fn [acc v]
           (assoc! acc (f v) v))
         (transient {}))
       (persistent!)))
