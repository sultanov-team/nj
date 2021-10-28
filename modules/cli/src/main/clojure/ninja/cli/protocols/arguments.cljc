(ns ninja.cli.protocols.arguments
  (:refer-clojure :exclude [name]))


(defprotocol Argument
  :extend-via-metadata true
  (name [argument])
  (options [argument])
  (params [argument]))


(defprotocol ArgumentBuilder
  :extend-via-metadata true
  (build [builder argument]))
