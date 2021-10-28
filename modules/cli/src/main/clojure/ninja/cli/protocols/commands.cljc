(ns ninja.cli.protocols.commands
  (:refer-clojure :exclude [name]))


(defprotocol Command
  :extend-via-metadata true
  (name [command])
  (options [command])
  (arguments [command]))


(defprotocol CommandBuilder
  :extend-via-metadata true
  (build [builder command]))
