(ns ninja.cli.protocols
  (:refer-clojure :exclude [name]))


(defprotocol CLI
  :extend-via-metadata true
  (name [cli])
  (options [cli])
  (commands [cli])
  (describe [cli]))


(defprotocol CLIBuilder
  :extend-via-metadata true
  (build [builder cli]))
