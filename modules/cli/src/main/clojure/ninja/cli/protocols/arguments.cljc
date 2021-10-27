(ns ninja.cli.protocols.arguments
  (:refer-clojure :exclude [name]))


(defprotocol Argument
  (name [argument])
  (options [argument])
  (params [argument]))


(defprotocol ArgumentBuilder
  (build [builder argument]))
