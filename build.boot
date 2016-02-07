(set-env!
  :dependencies '[[org.clojure/clojure                       "1.8.0"  :scope "provided"]
                  [org.clojure/clojurescript                 "1.7.228" :scope "provided"]
                  [adzerk/bootlaces                          "0.1.13" :scope "test"]
                  [adzerk/boot-test                          "1.1.0" :scope "test"]
                  [adzerk/boot-cljs                          "1.7.228-1" :scope "test"]
                  [crisptrutski/boot-cljs-test               "0.2.1" :scope "test"]
                  [org.clojure/core.async                    "0.2.374"]]
  :resource-paths #{"src/client" "src/worker"}
)

(require
  '[adzerk.boot-cljs            :refer [cljs]]
  '[adzerk.bootlaces            :refer :all])

(def +version+ "0.0.2")

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