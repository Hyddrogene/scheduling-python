import pandas as pd

# Charger les données CSV
file_path = "data.csv"  # Remplace par le chemin de ton fichier
df = pd.read_csv(file_path,header=None)

# Afficher les premières lignes pour vérifier le contenu
df.head()

pd.set_option('future.no_silent_downcasting', True)
df = df.fillna(0)
df = df.replace(r'^\s*$', 0, regex=True)  # Replace empty values with 0
df = df.replace(['H', '\tH'], 1)  # Replace 'H' and variations with 1
df = df.replace(['S', '\tS'], 2)  # Replace 'S' and variations with 2
df = df.replace(['O', '\tO'], 2)  # Replace 'S' and variations with 2
df = df.set_index(df.columns[0], drop=True)

print(df)
print("==================================")

# Supposons que les valeurs non-nulles ou non égales à 0 représentent des contraintes
zero_count_per_row = (df == 0).sum(axis=1)
# Step 3: Calculate the length of each row (number of columns in the row)
row_length = df.shape[1]
# Step 4: Subtract the count of zeros from the length of each row
non_zero_count_per_row = row_length - zero_count_per_row
# Add this result as a new column for visibility
df['non_zero_count'] = non_zero_count_per_row
print(df['non_zero_count'])
print(df)





# Afficher le résultat
print("==================================")
print("Nombre de contraintes par article :")
# Step 1: Count the number of 0's in each column
#zero_count_per_column = df.iloc[:, 1:].(df > 0).sum(axis=0)
#zero_count_per_column = df.iloc[:, 1:][df.iloc[:, 1:] > 0].sum()

zero_count_per_column = (df > 0).sum(axis=0)


df.loc['nrConstraint'] =zero_count_per_column
#columns_except_first_sum_positive_with_label = pd.concat([pd.Series(['nrConstraint'], index=[0]), zero_count_per_column])
#df = df._append(columns_except_first_sum_positive_with_label, ignore_index=True)

#df.loc['nrConstraint'] = zero_count_per_column

print(df)

print("==================================")
# Calculer la corrélation entre les articles
correlation_articles = df.corr()
print("Corrélation entre les articles :")
print(correlation_articles)


print("==================================")
# Trouver les articles qui partagent exactement les mêmes contraintes

import matplotlib.pyplot as plt
import seaborn as sns

import numpy as np
"""
# Histogramme des contraintes par article
plt.figure(figsize=(10, 6))
#df["non_zero_count"][:-1].plot(kind='bar')
df.loc["nrConstraint"][:-1].plot(kind='bar')


plt.title('Occurence des contraintes')
plt.xlabel('Contraintes')
plt.ylabel('Nombre d\'occurence')
plt.show()

"""


df = df.apply(pd.to_numeric, errors='coerce')
for i in range(len(df) - 1):
    df.iloc[i, :-1] = df.iloc[i, :-1].apply(lambda x: df["non_zero_count"][i] if x > 0 else x)
    #df.iloc[i, :-1] = df.iloc[i, :-1].apply(lambda x: 1 if x > 0 else 0)


# Heatmap des contraintes par article
plt.figure(figsize=(14, 6))
sns.heatmap(df.iloc[:-1, :-1], annot=True, cmap="coolwarm", cbar=True)
plt.title('Heatmap des contraintes par article')
plt.show()

"""
max_values = 1
# Heatmap des contraintes par article
plt.figure(figsize=(12, 6))  # Taille ajustée pour plus de compacité
sns.heatmap(df.iloc[:-1, :-1], annot=True, cmap="coolwarm", cbar=True, 
            linewidths=0.5, linecolor='gray', vmin=0, vmax=max_values)  # Ajout de lignes pour séparer les cellules
#plt.title('Heatmap des contraintes par article', fontsize=14)  # Titre ajusté
plt.xticks(rotation=45, ha='right', fontsize=10)  # Rotation et ajustement des étiquettes sur l'axe X
plt.yticks(rotation=0, fontsize=10)  # Ajustement des étiquettes sur l'axe Y
plt.tight_layout()  # Réduit les marges pour mieux utiliser l'espace disponible
plt.show()
"""