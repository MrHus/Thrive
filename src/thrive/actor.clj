(ns thrive.actor
  (:gen-class))

(defprotocol Actor
  "Defines what an Actor should respond to."
  (live     [this world world-size] "The live method, IE what the actor does")
  (interval [this] "The interval in which the actor performs live")
  (alive?   [this] "When is the actor alive (true) and when is dead (false)"))

(defn living-actors
  "Returns all actors that are still alive."
  [actors]
  (do
    ;(. Thread sleep 1000)
    (println "cleanup")
    (filter #(= (alive? @%) true) actors)))

(defn loop-actor
  "This starts the actors loop. It performs the actors 'live' function each 'interval' if the actor
   is still alive."
  [actor world world-size]
  (do
    (. Thread sleep (interval actor))
    (if (true? (alive? actor))
      (let [a (live actor world world-size)]
        (send-off *agent* loop-actor world world-size)
        a)
      actor)))
