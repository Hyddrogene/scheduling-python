constraints = ["Contrainte 1", "Contrainte 2", "Contrainte 3", "Contrainte 4", "Contrainte 5"]
problems = {
    "Problème 1": ["H", "S", "", "H", ""],
    "Problème 2": ["S", "", "H", "", "S"],
    "Problème 3": ["", "H", "S", "H", ""],
    "Problème 4": ["H", "S", "", "", "H"]
}

def generate_latex_table(constraints, problems):
    latex_code = r'''\documentclass{article}
\usepackage{colortbl}
\usepackage{xcolor}
\usepackage{geometry}
\geometry{a4paper, margin=1in}

\begin{document}

\definecolor{lightgray}{gray}{0.9}
\definecolor{softcolor}{rgb}{0.9,0.9,1}
\definecolor{hardcolor}{rgb}{1,0.9,0.9}

\begin{table}[h]
\centering
\setlength{\arrayrulewidth}{0.5mm}
\setlength{\tabcolsep}{18pt}
\renewcommand{\arraystretch}{2.5}
\arrayrulecolor[HTML]{000000}
\begin{tabular}{|c|''' + 'c|' * len(problems) + r'''}
\hline
\rowcolor{lightgray}
\textbf{Contrainte/Problème}'''

    for problem in problems.keys():
        latex_code += " & \\textbf{" + problem + "}"
    latex_code += r" \\ \hline" + "\n"

    for i, constraint in enumerate(constraints):
        latex_code += "\\textbf{" + constraint + "}"
        for problem in problems.values():
            cell = problem[i]
            if cell == "H":
                latex_code += " & \\cellcolor{hardcolor}H"
            elif cell == "S":
                latex_code += " & \\cellcolor{softcolor}S"
            else:
                latex_code += " & "
        latex_code += r" \\ \hline" + "\n"

    latex_code += r'''\end{tabular}
\caption{Matrice des contraintes pour différents problèmes}
\label{fig:constraints-matrix}
\end{table}

\end{document}
'''
    return latex_code

latex_code = generate_latex_table(constraints, problems)
print(latex_code)
