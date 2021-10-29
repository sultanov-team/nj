(ns ninja.cli.protocols.commands
  (:refer-clojure :exclude [name]))


(defprotocol Command
  :extend-via-metadata true
  (name [command])
  (description [command])
  (options [command])
  (arguments [command])
  (argument-keys [command])
  (argument [command name]))


(defprotocol CommandBuilder
  :extend-via-metadata true
  (build [builder command]))
