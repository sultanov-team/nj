(ns ninja.cli.protocols.params
  (:refer-clojure :exclude [name]))


(defprotocol Param
  :extend-via-metadata true
  (name [param])
  (description [param])
  (options [param])
  (option-keys [param]))


(defprotocol ParamBuilder
  :extend-via-metadata true
  (build [builder param]))
