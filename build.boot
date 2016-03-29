(set-env!
  :dependencies '[[org.clojure/clojure                       "1.8.0"  :scope "provided"]
                  [org.clojure/clojurescript                 "1.8.40" :scope "provided"]
                  [adzerk/bootlaces                          "0.1.13" :scope "test"]]
  :resource-paths #{"src/client" "src/worker"}
)

(require
  '[adzerk.bootlaces            :refer :all])

(def +version+ "0.1.4")

(bootlaces! +version+ :dont-modify-paths? true)

(task-options!
  pom {:project     'aatree/aaworker
       :version     +version+
       :description "An Extensible Web Worker for Hoplon."
       :url         "https://github.com/aatree/aaworker"
       :scm         {:url "https://github.com/aatree/aaworker"}
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask dev
  "Build project for development."
  []
  (comp
   (build-jar)))

(deftask deploy-release
 "Build for release."
 []
 (comp
   (build-jar)
   (push-release)))
