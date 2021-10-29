(ns ninja.cli.specs)


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
