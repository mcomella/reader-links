(ns reader-links.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [reader-links.core-test]))

(enable-console-print!)

(doo-tests 'reader-links.core-test)
