(ns ninja.cli.specs
  (:require
    [malli.core :as m]
    [malli.error :as me]
    [ninja.cli.utils :as utils]))


(def default-opts
  {:param/name           {:error/message {:en "`:param/name` should be a keyword"}}
   :param/options        {:error/message {:en "`:param/options` should be a map of {:keyword :any}"}}
   :param/options-key    {:error/message {:en "`:param/options-key` should be a keyword"}}

   :argument/name        {:error/message {:en "`:argument/name` should be a keyword"}}
   :argument/options     {:error/message {:en "`:argument/options` should be a map of {:keyword :any}"}}
   :argument/options-key {:error/message {:en "`:argument/options-key` should be a keyword"}}
   :argument/params      {:error/message {:en "`:argument/params` should be a vector or list"}}

   :command/name         {:error/message {:en "`:command/name` should be a keyword"}}
   :command/options      {:error/message {:en "`:command/options` should be a map of {:keyword :any}"}}
   :command/options-key  {:error/message {:en "`:command/options-key` should be a keyword"}}
   :command/arguments    {:error/message {:en "`:command/arguments` should be a vector or list"}}

   :cli/name             {:error/message {:en "`:cli/name` should be a keyword"}}
   :cli/options          {:error/message {:en "`:cli/options` should be a map of {:keyword :any}"}}
   :cli/options-key      {:error/message {:en "`:cli/options-key` should be a keyword"}}
   :cli/commands         {:error/message {:en "`:cli/commands` should be a vector or list"}}

   :cli                  {:error/message {:en "`:cli` should be a vector or list"}}})



(def CLI
  [:schema {:registry {"param"    [:catn (:argument/params default-opts)
                                   [:param/name [:keyword (:param/name default-opts)]]
                                   [:param/options [:? [:map-of (:param/options default-opts)
                                                        [:keyword (:param/options-key default-opts)]
                                                        [:any]]]]]
                       "argument" [:catn (:command/arguments default-opts)
                                   [:argument/name [:keyword (:argument/name default-opts)]]
                                   [:argument/options [:?
                                                       [:map-of (:argument/options default-opts)
                                                        [:keyword (:argument/options-key default-opts)]
                                                        [:any]]]]
                                   [:argument/params [:* [:schema [:ref "param"]]]]]
                       "command"  [:catn (:cli/commands default-opts)
                                   [:command/name [:keyword (:command/name default-opts)]]
                                   [:command/options [:? [:map-of (:command/options default-opts)
                                                          [:keyword (:command/options-key default-opts)]
                                                          [:any]]]]
                                   [:command/arguments [:* [:schema [:ref "argument"]]]]]}}
   [:catn (:cli default-opts)
    [:cli/name [:keyword (:cli/name default-opts)]]
    [:cli/options [:? [:map-of (:cli/options default-opts)
                       [:keyword (:cli/options-key default-opts)]
                       [:any]]]]
    [:cli/commands [:+ [:schema [:ref (:cli/commands default-opts) "command"]]]]]])



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
  (let [parse (m/parser CLI)]
    (fn [cli]
      (let [res (parse cli)]
        (if (= ::m/invalid res)
          :cli/invalid
          res)))))



(def ^{:arglists '([cli])
       :doc      "Returns `true` if the given `cli` is valid. Otherwise, returns `false`."}
  valid?
  (m/validator CLI))



(def ^{:arglists '([cli])
       :doc      "Returns an explanation of the errors. Returns `nil` if the given `cli` is valid."}
  explain
  (m/explainer CLI))



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
