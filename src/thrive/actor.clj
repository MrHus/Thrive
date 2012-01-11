(ns thrive.actor
  (:gen-class))

(defprotocol Actor
  "Defines what an Actor should respond to."
  (live     [this world world-width-height] "The live method, IE what the actor does")
  (interval [this] "The interval in which the actor performs live"))

(defn loop-actor
  "This starts the actors loop. It performs the actors 'live' function each 'interval'"
  [actor world world-width-height]
  (do
    (. Thread sleep (interval actor))
    (send-off *agent* loop-actor world world-width-height)
    (live actor world world-width-height)))
