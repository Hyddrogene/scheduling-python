import csv

csv_file = "data.txt"


class csv_reader():
    def __init__(self, csv_file):
        with open(csv_file, newline='') as csvfile:
            reader = csv.reader(csvfile)
            rows = list(reader)
            self.header = [k.replace("#","") for k in rows[0]]
            self.rows = rows[1:]
            
    def getRows(self):
        return self.rows
    
    def getHeader(self):
        return self.header

# Fonction pour convertir le CSV en tableau
def csv_to_array(csv_file):
    with open(csv_file, newline='') as csvfile:
        reader = csv.reader(csvfile)
        rows = list(reader)
        header = [k.replace("#","") for k in rows[0]]
        rows = rows[1:]
        # Parcours des lignes du fichier CSV
        rowfois10 = [int(float(row[0])*10) for row in rows]#je multiplie par 10 pour pouvoir avoir des entiers
        print(rowfois10)
        
        return rows

# Conversion et affichage du code LaTeX
results = csv_to_array(csv_file)
print(results)


import os

file_path = os.path.abspath(__file__)
directory_path = os.path.dirname(file_path)

print("File Path:", file_path)
print("Directory Path:", directory_path)
