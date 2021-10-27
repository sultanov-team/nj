(ns build
  (:refer-clojure :exclude [test])
  (:require
    [clojure.pprint :as pprint]
    [clojure.set :as set]
    [clojure.tools.build.api :as b]
    [clojure.tools.build.util.file :as file]
    [org.corfield.build :as bb]))


(def defaults
  {:src-dirs       ["src/main/clojure"]
   :resource-dirs  ["src/main/resources"]
   :lib            'team.sultanov/ninja.cli
   :main           'ninja.cli
   :target         "target"
   :coverage-dir   "coverage"
   :dist-dir       "dist"
   :jar-file       "target/ninja.cli.jar"
   :uber-file      "target/ninja.cli.uber.jar"
   :build-meta-dir "src/main/resources/ninja/cli"})


(defn pretty-print
  [x]
  (binding [pprint/*print-right-margin* 130]
    (pprint/pprint x)))


(defn with-defaults
  [opts]
  (merge defaults opts))


(defn extract-meta
  [opts]
  (-> opts
    (select-keys [:lib
                  :version
                  :build-number
                  :build-timestamp
                  :git-url
                  :git-branch
                  :git-sha])
    (set/rename-keys {:lib :module})
    (update :module str)))


(defn write-meta
  [opts]
  (let [dir (:build-meta-dir opts)]
    (file/ensure-dir dir)
    (->> opts
      (extract-meta)
      (pretty-print)
      (with-out-str)
      (spit (format "%s/build.edn" dir)))))


(defn outdated
  [opts]
  (-> opts
    (with-defaults)
    (bb/run-task [:nop :antq])))


(defn outdated:upgrade
  [opts]
  (-> opts
    (with-defaults)
    (bb/run-task [:nop :antq :antq:upgrade])))


(defn clean
  [opts]
  (-> opts
    (with-defaults)
    (bb/clean)))


(defn repl
  [opts]
  (let [opts (with-defaults opts)]
    (write-meta opts)
    (bb/run-task opts [:bench :test :develop])))


(defn test
  [opts]
  (let [opts (with-defaults opts)]
    (write-meta opts)
    (bb/run-task opts [:test])))


(defn jar
  [opts]
  (let [opts (with-defaults opts)]
    (write-meta opts)
    (-> opts
      (assoc :scm {:url (:git-url opts)
                   :tag (:version opts)})
      (bb/jar))))


(defn uber
  [opts]
  (let [opts (with-defaults opts)]
    (write-meta opts)
    (-> opts
      (assoc :scm {:url (:git-url opts)
                   :tag (:version opts)})
      (bb/uber))))


(defn dist
  [opts]
  (let [{:keys [target dist-dir coverage-dir]} (with-defaults opts)]
    (b/delete {:path dist-dir})
    (b/copy-dir {:src-dirs   [target]
                 :target-dir dist-dir
                 :include    "*.jar"})
    (b/copy-dir {:src-dirs   [coverage-dir]
                 :target-dir (str dist-dir "/" coverage-dir)})))


(defn install
  [opts]
  (-> opts
    (with-defaults)
    (bb/install)))


(defn deploy
  [opts]
  (-> opts
    (with-defaults)
    (bb/deploy)))
