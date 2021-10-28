(ns ninja.cli.protocols.params
  (:refer-clojure :exclude [name]))


(defprotocol Param
  :extend-via-metadata true
  (name [param])
  (options [param]))


(defprotocol ParamBuilder
  :extend-via-metadata true
  (build [builder param]))
