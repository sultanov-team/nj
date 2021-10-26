(ns ninja.versioniq-test
  (:require
    #?(:clj [clojure.test :refer [deftest testing is]]
       :cljs [cljs.test :refer [deftest testing is]])
    [ninja.versioniq :as sut]))


(deftest square-test
  (testing "dummy test"
    (is (= 4 (sut/square 2)))))
