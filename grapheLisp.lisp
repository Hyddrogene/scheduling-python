;; Définition du graphe par liste d'adjacence
(defvar *graph* '((A B C) (B A C D) (C A B D) (D B C)))

;; Liste tabou pour stocker les mouvements interdits
(defvar *tabu-list* nil)
(defvar *tabu-duration* 5)

(defun valid-colors (vertex colors assignment)
  "Retourne les couleurs valides qui peuvent être assignées au sommet."
  (let ((neighbors (cdr (assoc vertex *graph*))))
    (remove-if (lambda (color) (member color (mapcar (lambda (x) (cdr (assoc x assignment))) neighbors)))
               colors)))

(defun initial-coloring (vertices colors)
  "Assigner une couleur initiale à chaque sommet du graphe."
  (mapcar (lambda (vertex) (cons vertex (first (valid-colors vertex colors ()))))
          vertices))

(defun find-conflicts (assignment)
  "Trouver les sommets avec des conflits de couleurs."
  (remove-if-not (lambda (vertex)
                   (not (null (valid-colors vertex (list (cdr (assoc vertex assignment))) assignment))))
                 (mapcar 'car assignment)))

(defun tabu-search (max-iterations colors)
  "Effectuer la recherche tabou pour colorer le graphe."
  (let ((assignment (initial-coloring (mapcar 'car *graph*) colors))
        (best-solution nil)
        (iterations 0))
    (loop while (< iterations max-iterations) do
      (let ((conflicts (find-conflicts assignment)))
        (unless conflicts
          (return (setf best-solution assignment)))
        (let ((vertex (nth (random (length conflicts)) conflicts))
              (new-color (nth (random (length colors)) colors)))
          (unless (find (cons vertex new-color) *tabu-list* :test 'equal)
            (setf (cdr (assoc vertex assignment)) new-color)
            (push (cons vertex new-color) *tabu-list*)
            (when (> (length *tabu-list*) *tabu-duration*)
              (pop *tabu-list*)))
        (incf iterations)))
    finally (return best-solution))))

;; Exemple d'utilisation
;;(tabu-search 100 '(red green blue yellow))

(defun solve-problem ()
  (let ((result (tabu-search 10000 '(green red blue yellow))))
    (format t "La solution optimale trouvée est: ~A" result))
    (format t "\n")
    )


(solve-problem)
