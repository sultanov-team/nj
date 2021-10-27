(ns ninja.cli.protocols.params
  (:refer-clojure :exclude [name]))


(defprotocol Param
  (name [param])
  (options [param]))


(defprotocol ParamBuilder
  (build [builder param]))
