#!/bin/bash

# Vérifier si un fichier .tex est passé en argument
if [ -z "$1" ]; then
    echo "Usage: $0 <file.tex>"
    exit 1
fi

# Variables pour stocker les dernières sections
last_section=""
last_section_line=0
current_line=0

# Parcourir le fichier ligne par ligne
while IFS= read -r line || [ -n "$line" ]; do
    # Obtenir le numéro de la ligne actuelle
    current_line=$((current_line + 1))

# Si une section est trouvée, la mémoriser
    if echo "$line" | grep -q '\\subsection{'; then
        last_section=$(echo "$line" | grep -o '\\section{.*}')
        last_section_line=$current_line
    fi

    # Si une citation est trouvée
    if [[ $line =~ \\cite\{.*\} ]]; then
        echo "Citation trouvée à la ligne $current_line : $line"
        #if [ ! -z "$last_section" ]; then
        echo "  -> La section la plus proche est : $last_section (ligne $last_section_line)"
        #else
        #    echo "  -> Aucune section trouvée avant cette citation."
        #fi
    fi

done < "$1"
