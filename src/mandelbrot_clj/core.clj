(ns mandelbrot-clj.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [complex.core :as c]))

(def screen-width 1200)
(def screen-height 800)

(def re-start -2)
(def re-end 1)
(def im-start -1)
(def im-end 1)

(def max-iter 50)

(defn get-pixel-color-value [x y]
  (let [z (c/complex (+ re-start (* (/ x screen-width) (- re-end re-start)))
                     (+ im-start (* (/ y screen-height) (- im-end im-start))))
        p (loop [w (c/complex 0 0) i 0]
            (if (> i max-iter) 0
              (let [w2 (if (= w (c/complex 0 0)) z
                              (c/+ (c/pow w 2) z))]
                (if (> (c/abs w2) 2)
                  (/ i max-iter)
                  (recur w2 (inc i))))))
        col (int (* p 255))]
    (if (> p 0.5)
      (q/color col 255 col)
      (q/color 0 col 0))))

(defn get-mandelbrot-im []
  (let [im (q/create-image screen-width screen-height :rgb)]
    (dotimes [x screen-width]
      (dotimes [y screen-height]
        (q/set-pixel im x y (get-pixel-color-value x y))))
    im))

(defn draw [state]
  (q/background 0)
  (q/set-image 0 0 (get-mandelbrot-im))
  (q/no-loop))

(q/defsketch mandelbrot-clj
  :title "mandelbrot set"
  :size [screen-width screen-height]
  :draw draw
  :middleware [m/fun-mode])
