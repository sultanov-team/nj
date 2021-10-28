(ns ninja.cli.specs-test
  (:require
    #?(:clj  [clojure.test :refer [deftest testing is]]
       :cljs [cljs.test :refer [deftest testing is]])
    [ninja.cli.specs :as sut]))


(deftest parse-cli-test
  (testing "parse cli"
    (let [cli      [:git
                    [:rev-list {:--count {}}
                     [:commit]
                     [:path]]]
          expected {:cli/name     :git
                    :cli/options  nil
                    :cli/commands [{:command/name      :rev-list
                                    :command/options   {:--count {}}
                                    :command/arguments [{:argument/name    :commit
                                                         :argument/options nil
                                                         :argument/params  []}
                                                        {:argument/name    :path
                                                         :argument/options nil
                                                         :argument/params  []}]}]}]
      (testing "successful parsing is expected"
        (is (sut/valid? cli))
        (is (nil? (sut/explain cli)))
        (is (empty? (sut/humanize cli)))
        (is (= expected (sut/parse-cli cli))))


      (testing "a parsing error is expected"
        (is (= :ninja.cli/invalid (sut/parse-cli nil)))
        (is (not (sut/valid? (sut/parse-cli nil))))))))



(deftest humanize-test
  (testing "humanized errors"
    (testing "bad `:cli`"
      (is (= (sut/humanize nil)
             ["received `nil` - `:cli` should be a vector or list"]))
      (is (= (sut/humanize 42)
             ["received `42` - `:cli` should be a vector or list"]))
      (is (= (sut/humanize {})
             ["received `{}` - `:cli` should be a vector or list"])))


    (testing "bad `:cli/name`"
      (is (= (sut/humanize [])
             ["received `nil` - `:cli/name` should be a keyword"]))
      (is (= (sut/humanize [nil])
             ["received `nil` - `:cli/name` should be a keyword"]))
      (is (= (sut/humanize ["string"])
             ["received `string` - `:cli/name` should be a keyword"]))
      (is (= (sut/humanize [42])
             ["received `42` - `:cli/name` should be a keyword"])))


    (testing "bad `:cli/options` & `:cli/commands`"
      (is (= (sut/humanize [:ninja/cli])
             ["received `nil` - `:cli/options` should be a map of {:keyword :any}"
              "received `nil` - `:cli/commands` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli nil])
             ["received `nil` - `:cli/options` should be a map of {:keyword :any}"
              "received `nil` - `:cli/commands` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli 42])
             ["received `42` - `:cli/options` should be a map of {:keyword :any}"
              "received `42` - `:cli/commands` should be a vector or list"])))


    (testing "bad `:cli/options-key`"
      (is (= (sut/humanize [:ninja/cli {nil nil}])
             ["received `nil` - `:cli/options-key` should be a keyword"
              "received `{nil nil}` - `:cli/commands` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {42 nil, 84 nil}])
             ["received `42` - `:cli/options-key` should be a keyword"
              "received `84` - `:cli/options-key` should be a keyword"])))


    (testing "bad `:cli/commands`"
      (is (= (sut/humanize [:ninja/cli {}])
             ["received `nil` - `:cli/commands` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} 42])
             ["received `42` - `:cli/commands` should be a vector or list"])))


    (testing "bad `:command/name`"
      (is (= (sut/humanize [:ninja/cli {} []])
             ["received `nil` - `:command/name` should be a keyword"]))
      (is (= (sut/humanize [:ninja/cli {} 42])
             ["received `42` - `:cli/commands` should be a vector or list"])))


    (testing "bad `:command/options`"
      (is (= (sut/humanize [:ninja/cli {} [:init nil]])
             ["received `nil` - `:command/options` should be a map of {:keyword :any}"
              "received `nil` - `:command/arguments` should be a vector or list"
              "received `nil` - `:cli/commands` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init 42]])
             ["received `42` - `:command/options` should be a map of {:keyword :any}"
              "received `42` - `:command/arguments` should be a vector or list"
              "received `42` - `:cli/commands` should be a vector or list"])))


    (testing "bad `:command/options-key`"
      (is (= (sut/humanize [:ninja/cli {} [:init {nil nil}]])
             ["received `nil` - `:command/options-key` should be a keyword"
              "received `{nil nil}` - `:command/arguments` should be a vector or list"
              "received `{nil nil}` - `:cli/commands` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {42 nil, 84 nil}]])
             ["received `42` - `:command/options-key` should be a keyword"
              "received `84` - `:command/options-key` should be a keyword"])))


    (testing "bad `:command/arguments`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} nil]])
             ["received `nil` - `:command/arguments` should be a vector or list"
              "received `nil` - `:cli/commands` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} 42]])
             ["received `42` - `:command/arguments` should be a vector or list"
              "received `42` - `:cli/commands` should be a vector or list"])))


    (testing "bad `:argument/name`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} []]])
             ["received `nil` - `:argument/name` should be a keyword"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} [42]]])
             ["received `42` - `:argument/name` should be a keyword"])))


    (testing "bad `:argument/options`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace nil]]])
             ["received `nil` - `:argument/options` should be a map of {:keyword :any}"
              "received `nil` - `:argument/params` should be a vector or list"
              "received `nil` - `:command/arguments` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace []]]])
             ["received `[]` - `:argument/options` should be a map of {:keyword :any}"
              "received `[]` - `:command/arguments` should be a vector or list"])))


    (testing "bad `:argument/options-key`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {nil nil}]]])
             ["received `nil` - `:argument/options-key` should be a keyword"
              "received `{nil nil}` - `:argument/params` should be a vector or list"
              "received `{nil nil}` - `:command/arguments` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {42 nil, 84 nil}]]])
             ["received `42` - `:argument/options-key` should be a keyword"
              "received `84` - `:argument/options-key` should be a keyword"])))


    (testing "bad `:argument/params`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} nil]]])
             ["received `nil` - `:argument/params` should be a vector or list"
              "received `nil` - `:command/arguments` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} 42]]])
             ["received `42` - `:argument/params` should be a vector or list"
              "received `42` - `:command/arguments` should be a vector or list"])))


    (testing "bad `:param/name`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} []]]])
             ["received `nil` - `:param/name` should be a keyword"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} [42]]]])
             ["received `42` - `:param/name` should be a keyword"])))


    (testing "bad `:param/options`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} [:workspace/name nil]]]])
             ["received `nil` - `:param/options` should be a map of {:keyword :any}"
              "received `nil` - `:argument/params` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} [:workspace/name []]]]])
             ["received `[]` - `:param/options` should be a map of {:keyword :any}"
              "received `[]` - `:argument/params` should be a vector or list"])))


    (testing "bad `:param/options-key`"
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} [:workspace/name {nil nil}]]]])
             ["received `nil` - `:param/options-key` should be a keyword"
              "received `{nil nil}` - `:argument/params` should be a vector or list"]))
      (is (= (sut/humanize [:ninja/cli {} [:init {} [:workspace {} [:workspace/name {42 nil, 84 nil}]]]])
             ["received `42` - `:param/options-key` should be a keyword"
              "received `84` - `:param/options-key` should be a keyword"])))))
