(ns pod-uochan-tarayo.core-test
  (:require [clojure.test :as t]
            [pod-uochan-tarayo.core :as sut]))

(t/deftest greet-test
  (t/is (= (sut/greet "world") "hello world")))
