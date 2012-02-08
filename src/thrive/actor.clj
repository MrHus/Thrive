(ns thrive.actor
  (:gen-class))

(defprotocol Actor
  "Defines what an Actor should respond to."
  (live     [this actors world world-size] "The live method, IE what the actor does")
  (interval [this] "The interval in which the actor performs live")
  (alive?   [this] "When is the actor alive (true) and when is dead (false)"))

(defn loop-actor
  "This starts the actors loop. It performs the actors 'live' function each 'interval' if the actor
   is still alive."
  [actor actors world world-size]
  (do
    (. Thread sleep (interval actor))
    (if (true? (alive? actor))
      (let [a (live actor actors world world-size)]
        (send-off *agent* loop-actor actors world world-size)
        a)
      actor)))
