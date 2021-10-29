(ns ninja.cli.protocols.arguments
  (:refer-clojure :exclude [name]))


(defprotocol Argument
  :extend-via-metadata true
  (name [argument])
  (description [argument])
  (options [argument])
  (params [argument])
  (param-keys [argument])
  (param [argument name]))


(defprotocol ArgumentBuilder
  :extend-via-metadata true
  (build [builder argument]))
