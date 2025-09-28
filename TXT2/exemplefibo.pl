(defun fibonacci (n)
  (cond
    ((= n 0) 0)
    ((= n 1) 1)
    (t (+ (fibonacci (- n 1)) (fibonacci (- n 2))))
  )
)

(defun print-fibonacci (n)
  (dotimes (i n)
    (format t "~a " (fibonacci i))
  )
  (terpri)
)

(print-fibonacci 10) ; 
# <3
