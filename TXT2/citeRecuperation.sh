#!/bin/bash

# Vérifier si un fichier .tex est passé en argument
if [ -z "$1" ]; then
    echo "Usage: $0 <file.tex>"
    exit 1
fi

# Récupérer toutes les occurrences de \cite{} dans le fichier .tex
grep -oP '\\cite\{.*?\}' "$1" | sort | uniq > citations.txt

# Afficher les résultats
echo "Les citations ont été extraites et enregistrées dans citations.txt"
cat citations.txt
