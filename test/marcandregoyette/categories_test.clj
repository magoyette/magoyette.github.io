(ns marcandregoyette.categories-test
  (:require [marcandregoyette.categories :refer :all]
            [clojure.test :refer :all]))

(deftest build-category-id-test
  (is (= "category-programming"
         (build-category-id (->Category "Programming" :en false))))
  (is (= "category-object-oriented_programming"
         (build-category-id (->Category "Object-Oriented Programming" :en false)))))

(deftest build-category-url-test
  (is (= "/categories/programming/"
         (build-category-url (->Category "Programming" :en false)))))

(deftest get-default-category-test
  (is (= "Programming"
         (:name (get-default-category)))))

(deftest get-visible-categories-test
  (is (= ["Programming" "Programmation"]
         (map :name (get-visible-categories)))))
