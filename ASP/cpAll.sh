#!/bin/bash

# Vérifier si le nombre correct d'arguments est passé
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <file_list.txt> <destination_directory>"
    exit 1
fi

# Récupérer les arguments
file_list=$1
destination_dir=$2

# Vérifier l'existence du fichier contenant la liste
if [ ! -f "$file_list" ]; then
    echo "Le fichier liste '$file_list' n'existe pas."
    exit 1
fi

# Créer le répertoire de destination s'il n'existe pas
mkdir -p "$destination_dir"

# Lire chaque ligne du fichier liste et copier les fichiers
while IFS= read -r file
do
    # Assumer que chaque ligne dans file_list contient le chemin complet du fichier
    if [ -f "$file" ]; then
        cp "$file" "$destination_dir"
        echo "Copié: $file -> $destination_dir"
    else
        echo "Le fichier '$file' n'a pas été trouvé et n'a pas été copié."
    fi
done < "$file_list"

echo "Opération de copie terminée."
