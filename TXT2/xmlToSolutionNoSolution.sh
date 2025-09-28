#!/bin/bash

# Vérifier si le nombre d'arguments est valide
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <fichier_liste>"
    exit 1
fi

# Vérifier si le fichier liste existe
if [ ! -f "$1" ]; then
    echo "Erreur: Le fichier $1 n'existe pas."
    exit 1
fi

# Lire chaque ligne du fichier liste
while IFS= read -r ligne; do
    # Remplacer l'extension .xml par .json
    nom_fichier_json=$(echo "$ligne" | sed 's|\(.*\/\)\(.*\)|\1solution_\2|')
    nom_fichier_json="${nom_fichier_json%.xml}*.xml"
    fichier=$(find $(dirname "$nom_fichier_json") -type f -name "$(basename "$nom_fichier_json")" -print -quit)
    echo "$fichier"
done < "$1"
