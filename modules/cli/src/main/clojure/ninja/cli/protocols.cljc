(ns ninja.cli.protocols
  (:refer-clojure :exclude [name]))


(defprotocol CLI
  (name [cli])
  (options [cli])
  (commands [cli])
  (describe [cli]))


(defprotocol CLIBuilder
  (build [builder cli]))
