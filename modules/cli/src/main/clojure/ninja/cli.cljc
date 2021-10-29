(ns ninja.cli
  (:require
    [malli.core :as m]
    [malli.error :as me]
    [ninja.cli.specs :as specs]
    [ninja.cli.utils :as utils]))


(def ^{:arglists '([cli])
       :doc      "Returns a parsed `cli`.
       Returns `:ninja.cli/invalid` if `cli` is not valid.

       Example:

         ```clojure
         (parse-cli
           [:git
            [:push {:force true}]])
         =>
         {:cli/name    :git
          :cli/options  nil
          :cli/commands [{:command/name      :push
                          :command/options   {:force true}
                          :command/arguments []}]}
         ```"}
  parse-cli
  (let [parse (m/parser specs/CLI)]
    (fn [cli]
      (let [res (parse cli)]
        (if (= ::m/invalid res)
          :ninja.cli/invalid
          res)))))



(def ^{:arglists '([cli])
       :doc      "Returns `true` if the given `cli` is valid. Otherwise, returns `false`."}
  valid?
  (m/validator specs/CLI))



(def ^{:arglists '([cli])
       :doc      "Returns an explanation of the errors. Returns `nil` if the given `cli` is valid."}
  explain
  (m/explainer specs/CLI))



(defn prettify-errors
  "Prettifies errors in explanation."
  [errors]
  (reduce
    (fn [acc error]
      (cond
        (nil? error) acc
        (map? error) (as-> error $$ (dissoc $$ :malli/error) (vals $$) (into acc $$))
        (vector? error) (into acc (prettify-errors error))
        (string? error) (conj acc error)
        :else acc))
    [] errors))



(def default-humanize-opts
  {:wrap (fn [{:keys [type value message]}]
           (cond
             (and (contains? #{::m/invalid-type ::m/end-of-input} type)
                  (nil? value)) (when-not (contains? #{"invalid type"} message)
                                  (utils/format "received `nil` - %s" message))
             (and (contains? #{::m/invalid-type} type)
                  (contains? #{"invalid type"} message)
                  (some? value)) nil
             (and value (nil? type)) (utils/format "received `%s` - %s" value message)
             (nil? value) (utils/format "received `nil` - %s" message)
             value (utils/format "received `%s` - %s" value message)
             :else nil))})



(defn humanize
  "Returns a humanized explanation of the errors."
  ([x]
   (humanize default-humanize-opts x))
  ([opts x]
   (let [errors (-> x
                    (explain)
                    (me/humanize opts)
                    (prettify-errors)
                    (flatten))]
     (when (seq errors)
       (vec errors)))))
