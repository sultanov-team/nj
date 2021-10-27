(ns ninja.cli.protocols.commands
  (:refer-clojure :exclude [name]))


(defprotocol Command
  (name [command])
  (options [command])
  (arguments [command]))


(defprotocol CommandBuilder
  (build [builder command]))
