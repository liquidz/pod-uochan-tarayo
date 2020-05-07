(require '[babashka.pods :as pods])

(comment
  (pods/load-pod ["java" "-jar" "target/pod-uochan-tarayo.jar"]))

(comment
  (require '[pod.uochan.tarayo :as tarayo]))

(comment
  (tarayo/send {:host "localhost" :port 1025}
               {:from "alice@example.com"
                :to "bob@example.com"
                :subject "hello"
                :body "word"}))
