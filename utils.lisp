(defun generate-list (x)
    (let ((list nil))
        (dotimes (i x)
        (setf list (cons i list))
        )
        list
    )
)


(defun generate-list-offset (x n)
    (let ((list nil))
        (dotimes (i x)
        (setf list (cons (+ i n) list))
        )
        list
    )
)

(defun invert-list (lst)
  (let ((result nil))
    (dolist (item lst)
      (setf result (cons item result)))
    result))