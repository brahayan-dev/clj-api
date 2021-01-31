(ns integration.word-test
  (:require [clojure.test :refer [deftest is]]
            [io.pedestal.test :refer [response-for]]
            [io.pedestal.http :as bootstrap]
            [api.core :as api]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet api/service)))

(def answer-in-json-1
  "{\"message\":\"'original' and 'scrambled' query parameters are required\",\"has-match?\":false}")
(deftest invalid-params
  (is (=
       (:body (response-for service :get "/word"))
       answer-in-json-1))
  (is (=
       (:body (response-for service :get "/word?foo=bar"))
       answer-in-json-1))
  (is (=
       (:body (response-for service :get "/word?original=modelia&foo=bar"))
       answer-in-json-1)))

(def answer-in-json-2
  "{\"message\":\"query parameters must have alphabetic characters\",\"has-match?\":false}")
(deftest invalid-strings-in-params
  (is (=
       (:body (response-for service :get "/word?original=23232&scrambled=123423"))
       answer-in-json-2))
  (is (=
       (:body (response-for service :get "/word?original=32foo&scrambled=bar23"))
       answer-in-json-2))
  (is (=
       (:body (response-for service :get "/word?original=AlIcIa&scrambled=aLiCiA"))
       answer-in-json-2)))

(deftest valid-strings-in-params
  (is (=
       (:body (response-for service :get "/word?original=full&scrambled=flu"))
       "{\"has-match?\":false,\"original\":\"full\",\"scrambled\":\"flu\"}"))
  (is (=
       (:body (response-for service :get "/word?original=model&scrambled=task"))
       "{\"has-match?\":false,\"original\":\"model\",\"scrambled\":\"task\"}"))
  (is (=
       (:body (response-for service :get "/word?original=alicia&scrambled=ramon"))
       "{\"has-match?\":false,\"original\":\"alicia\",\"scrambled\":\"ramon\"}")))

(deftest valid-with-match-strings-in-params
  (is (=
       (:body (response-for service :get "/word?original=full&scrambled=fllu"))
       "{\"has-match?\":true,\"original\":\"full\",\"scrambled\":\"fllu\"}"))
  (is (=
       (:body (response-for service :get "/word?original=model&scrambled=elmoderno"))
       "{\"has-match?\":true,\"original\":\"model\",\"scrambled\":\"elmoderno\"}"))
  (is (=
       (:body (response-for service :get "/word?original=alicia&scrambled=aalliicciiaa"))
       "{\"has-match?\":true,\"original\":\"alicia\",\"scrambled\":\"aalliicciiaa\"}")))
