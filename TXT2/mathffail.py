import re
import random
texte = """
config_maquette/config_20231202_004830_utpSolver4.xml, ./tmp/experiment_02-12-23/instanceUSP_generated_021223004831_realistic_utpSolver4.xml, Model[Modele USP], Solutions: 0, Building time : 3,723s, Fails: 1, Restarts: 0
config_maquette/config_20231202_004839_utpSolver7.xml, ./tmp/experiment_02-12-23/instanceUSP_generated_021223004840_realistic_utpSolver7.xml, Model[Modele USP], Solutions: 0, Building time : 10,003s, Fails: 21 045, Restarts: 0
"""

filename = "/home/etud/timetabling/tools/tools_php/file_statistique_execution.txt"

def readIntoFile(filename):
    tableau = []
    with open(filename, 'r') as fichier:
        lignes = fichier.readlines()
        for ligne in lignes:
            tableau.append(ligne.strip())
    return tableau



tableau = readIntoFile(filename)
resFails = []
resSolutions = []
resBuildingTime = []

for texte in tableau:
    matches = re.findall(r'Fails:\s*([\d\s*]+),', texte)
    # Affichage des résultats
    
    for match in matches:
        pattern=r"\s+"
        match_without_spaces = re.sub(pattern, "", match)
        nr  = int(match_without_spaces)
        resFails.append(nr)
        print(nr, type(nr))
        
    matches = re.findall(r'Solutions\s*:\s*([\d]+),', texte)
    
    # Affichage des résultats
    for match in matches:
        pattern=r"\s+"
        match_without_spaces = re.sub(pattern, "", match)
        nr  = int(match_without_spaces)
        resSolutions.append(nr)

    matches = re.findall(r'Building time :\s*([\d\s*]+),', texte)
    # Affichage des résultats
    for match in matches:
        pattern=r"\s+"
        match_without_spaces = re.sub(pattern, "", match)
        nr  = int(match_without_spaces)
        resBuildingTime.append(nr)
        print(nr, type(nr))
        
#print(random.choice(tableau))



print("mean building time : "+str(round(sum(resBuildingTime)/len(resBuildingTime),2))+"s;")
print("mean fails : "+str(round(sum(resFails)/len(resFails),2))+"; min fails : "+str(min(resFails))+"; max : "+str(max(resFails))+"; 1-fail  : "+str(len(list(filter(lambda x: x==1, resFails))))+"; percent1-fail  "+str(round((len(list(filter(lambda x: x==1, resFails)))/len(resFails)) *100,2))+"%")
print("; mean solutions : "+str(round(sum(resSolutions)/len(resSolutions),2))+"; nrSolutions : "+str(len(list(filter(lambda x: x==1, resSolutions))))+"; percentSolutions : "+str(round((len(list(filter(lambda x: x==1, resSolutions)))/len(resSolutions))*100,2))+"%"+"; solutionWihtout1-fail : "+str(round((len(list(filter(lambda x: x==1, resSolutions)))/(len(resSolutions)-len(list(filter(lambda x: x==1, resFails))))),2)*100)+"%" )

