(ns ninja.cli.utils-test
  (:require
    #?(:clj [clojure.test :refer [deftest testing is]]
       :cljs [cljs.test :refer [deftest testing is]])
    [ninja.cli.utils :as sut]))


(deftest index-by-test
  (testing "indexing a collection using a keyword"
    (is (= {1 {:id 1} 2 {:id 2}}
           (sut/index-by :id [{:id 1} {:id 2}])))))
