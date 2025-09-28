from manim import *

class SchedulingBoolean(Scene):
    def construct(self):
        TRANSITION_TIME = 1  # Configure ici le temps entre les transitions

        title = Text("Exemple de problème de Scheduling").scale(0.7).to_edge(UP)
        self.play(Write(title))
        self.wait(TRANSITION_TIME)

        # Grille de planning
        days = ["Lun", "Mar", "Mer"]
        times = ["8h", "10h", "14h"]
        grid = Table(
            [["" for _ in days] for _ in times],
            row_labels=[Text(t) for t in times],
            col_labels=[Text(d) for d in days],
            include_outer_lines=True,
        ).scale(0.7).shift(DOWN)

        self.play(Create(grid))
        self.wait(TRANSITION_TIME)

        # Ajoute des sessions fictives
        s1 = Text("S1").scale(0.5)
        s2 = Text("S2").scale(0.5)
        s3 = Text("S3").scale(0.5)

        self.play(
            FadeIn(s1.move_to(grid.get_cell((1, 1)))),
            FadeIn(s2.move_to(grid.get_cell((2, 2)))),
            FadeIn(s3.move_to(grid.get_cell((3, 3))))
        )
        self.wait(TRANSITION_TIME)

        # Formule logique 1 : pas deux cours en même temps pour un prof
        formula1 = MathTex(r"\\neg(\\text{ProfA}_{8h, Lun} \\land \\text{ProfA}_{8h, Mar})")
        formula1.scale(0.7).to_edge(LEFT).shift(DOWN*2.5)
        self.play(Write(formula1))
        self.wait(TRANSITION_TIME)

        self.play(FadeOut(formula1))

        # Formule logique 2 : session S1 doit être quelque part le lundi
        formula2 = MathTex(r"\\text{S1}_{8h, Lun} \\lor \\text{S1}_{10h, Lun} \\lor \\text{S1}_{14h, Lun}")
        formula2.scale(0.7).to_edge(LEFT).shift(DOWN*2.5)
        self.play(Write(formula2))
        self.wait(TRANSITION_TIME)

        self.play(FadeOut(formula2))

        # Résumé final
        conclusion = Text("Représentation booléenne des contraintes").scale(0.6).to_edge(DOWN)
        self.play(Write(conclusion))
        self.wait(2 * TRANSITION_TIME)
