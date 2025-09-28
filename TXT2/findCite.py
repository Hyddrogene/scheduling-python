import sys
import re

# Vérifier si un fichier .tex est passé en argument
if len(sys.argv) < 2:
    print("Usage: python3 find_section_for_cite.py <file.tex>")
    sys.exit(1)

# Récupérer le nom du fichier
filename = sys.argv[1]

# Variables pour stocker la dernière section trouvée
last_section = ""
last_section_line = 0
current_line = 0

# Ouvrir et lire le fichier ligne par ligne
with open(filename, 'r') as file:
    for line in file:
        current_line += 1
        
        if re.search(r'\\[sub]*section{.*}', line):
            last_section = re.search(r'\\[sub]*section{.*}', line).group(0)
            last_section_line = current_line

        match = re.search(r'\\cite{(.*?)}', line)
        if match:
            citation = match.group(1)
            print(f"Citation trouvée à la ligne {current_line} : \\cite{{{citation}}}")
            print(f"  -> La section la plus proche est : {last_section} (ligne {last_section_line})")

