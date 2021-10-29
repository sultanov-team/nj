(ns ninja.cli.protocols
  (:refer-clojure :exclude [name]))


(defprotocol CLI
  :extend-via-metadata true
  (name [cli])
  (description [cli])
  (version [cli])
  (options [cli])
  (commands [cli])
  (command-keys [cli])
  (command [cli name]))


(defprotocol CLIBuilder
  :extend-via-metadata true
  (build [builder cli]))


(defprotocol Form
  :extend-via-metadata true
  (form [this]))
