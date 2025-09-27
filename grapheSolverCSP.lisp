(load "utils.lisp")

;; Structure pour les variables et leurs domaines
(defstruct cspvariable
  name
  domain)

;; Structure pour les contraintes
(defstruct constraint
  vars
  predicate)


(defun check-constraint (constraint assignment)
  (let ((vars (constraint-vars constraint))
        (predicate (constraint-predicate constraint)))
    ;; Collecter les valeurs pour toutes les variables impliquées dans la contrainte
    (let ((values (mapcar #'(lambda (v) (cdr (assoc v assignment))) vars)))
      ;; S'assurer que toutes les variables ont des valeurs non-NIL
      (if (every #'identity values)
          ;; Si toutes les variables sont assignées, appliquer le prédicat
          (apply predicate values)
          ;; Sinon, retourner T ou NIL selon la logique souhaitée (peut dépendre de votre stratégie de propagation de contraintes)
          t))))


(defun display-solution (assignment)
  (if assignment
      (progn
        (format t "Solution trouvée :~%")
        (dolist (var assignment)
          (format t "~A = ~A~%" (car var) (cdr var))))
      (format t "Aucune solution n'a été trouvée.~%")))



(defun csp-backtracking (assignment variables constraints)
  (if (null variables)
      (return-from csp-backtracking assignment)
      (let* ((var (first variables))
             (remaining-vars (rest variables)))
        (dolist (value (cspvariable-domain var))
          (let ((new-assignment (cons (cons (cspvariable-name var) value) assignment)))
            (when (every #'(lambda (c) (check-constraint c new-assignment)) constraints)
              (let ((result (csp-backtracking new-assignment remaining-vars constraints)))
                (when result
                  (return-from csp-backtracking result))))))
        )
    )
)

(defun solve-csp (variables constraints)
  (let ((assignment (csp-backtracking nil variables constraints)))
    (display-solution assignment)
    assignment))


(defvar *domains* (generate-list 5))
(defvar *domains1* (invert-list (generate-list-offset 1000000 10)))

;;(defun solve-csp (variables constraints)
;;  (csp-backtracking nil variables constraints))
(let ((variables (list (make-cspvariable :name 'X :domain *domains1*)
                       (make-cspvariable :name 'W :domain '(1 2 3))
                       (make-cspvariable :name 'Y :domain *domains1*)))
      (constraints (list 
      (make-constraint :vars '(X Y) :predicate #'(lambda (x y) (/= x y)))
      (make-constraint :vars '(W) :predicate #'(lambda (x) (= x 3)))
      (make-constraint :vars '(X) :predicate #'(lambda (x) (> x 999999)))
      (make-constraint :vars '(X Y) :predicate #'(lambda (x y) (> (+ x y) 2000000))))))

(solve-csp variables constraints))
