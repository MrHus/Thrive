(ns thrive.actor
  (:gen-class))

(defprotocol Actor
  "Defines what an Actor should respond to."
  (live     [this world] "The live method, IE what the actor does")
  (interval [this] "The interval in which the actor performs live"))

(defn loop-actor
  "This starts the actors loop. It performs the actors 'live' function each 'interval'"
  [actor world]
  (do
    (. Thread sleep (interval actor))
    (send-off *agent* loop-actor world)
    (live actor world)))