#!/bin/bash

# Exécuter une commande pour mesurer le temps (par exemple ls)
output=$( { time ls; } 2>&1 )

# Extraire la partie spécifique du résultat (par exemple, le temps réel)
real_time=$(echo "$output" | grep sys | cut -d' ' -f2)

# Afficher le résultat
echo "Temps réel: $real_time"
