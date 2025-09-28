from manim import *

class BougeEtChange(Scene):
    def construct(self):
        # Créer un cercle bleu
        cercle = Circle(radius=1, color=BLUE)
        self.play(Create(cercle))  # Animation d'apparition

        # Le cercle se déplace vers la droite
        self.play(cercle.animate.shift(RIGHT * 3), run_time=1.5)

        # Le cercle devient rouge et grossit
        self.play(cercle.animate.set_color(RED).scale(1.5), run_time=1.5)

        # Le cercle tourne
        self.play(Rotate(cercle, angle=PI), run_time=2)

        # Fondu de sortie
        self.play(FadeOut(cercle))
 
