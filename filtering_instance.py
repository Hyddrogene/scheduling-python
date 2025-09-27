import re
import random
import sys

texte = """
config_maquette/config_20231202_004830_utpSolver4.xml, ./tmp/experiment_02-12-23/instanceUSP_generated_021223004831_realistic_utpSolver4.xml, Model[Modele USP], Solutions: 0, Building time : 3,723s, Fails: 1, Restarts: 0
config_maquette/config_20231202_004839_utpSolver7.xml, ./tmp/experiment_02-12-23/instanceUSP_generated_021223004840_realistic_utpSolver7.xml, Model[Modele USP], Solutions: 0, Building time : 10,003s, Fails: 21 045, Restarts: 0
"""


def readIntoFile(filename):
    tableau = []
    with open(filename, 'r') as fichier:
        lignes = fichier.readlines()
        for ligne in lignes:
            tableau.append(ligne.strip())
    return tableau
name_stat= ["MeanEcartTypeServiceTeacher","maxServEcartType", "gdcClassRoom","gdcClassTeachers","gdcClassGroups","gdcClass","sumClassesStudentsTimes","ratio_room_sessions","timeClasseMeanCalc","nrClassesPerStudent","nrStudentsPerClass","moyenne_capacity_room","Student/capacity","CountNoUseRoom","fails","solutions"]

stat_v2 = ["Buildingtime","Variables","Constraints","Nodes","Backtracks","Resolutiontime","Fails","Solutions"]

def readXml(filexml):
    #tableau = readIntoFile(filexml)
    #separator = ","
    tabu = ["Fails","Solutions"]
    res = filexml#separator.join(tableau)
    pattern=r"\s+"
    res = re.sub(pattern, "", res)
    tmp = []
    for j in stat_v2:
        if not( j in tabu):
            matches = re.findall(re.escape(j)+r'\s*:\s*([\d\.*\,*\s*E*\-*]+)s?,', res)
            match_without_spaces = matches[0].replace(",",".")
            nr  = float(match_without_spaces)
            tmp.append(nr)


    return tmp
    

filename = "/home/etud/timetabling/tools/tools_php/file_statistique_execution.txt"
filename = "/home/etud/AAAAOLD/file_statistique_execution.txt"
filename = "file_statistique_execution.txt"
filename= "file_statistique_execution_071223old.txt"
filename = "/home/etud/timetabling/tools/tools_php/file_statistique_execution_old111223.txt"
pathtoresume = ""

if len(sys.argv) == 3:
    print("Utilisation : python script.py <argument>")
    filename = sys.argv[1]
    pathtoresume = sys.argv[2]

needPCA = 1


tableau = readIntoFile(filename)
resFails = []
resSolutions = []
resBuildingTime = []

stat_instance = []
name_instance = []

dictFails = {}
dictSolutions = {}

for texte in tableau:
    if texte == "":
        pass
    instanceName = ""
    if needPCA ==1 :
        tablette = texte.split(",")
        #print("tablette ",tablette[1])
        #print("cut ",tablette[1].split("/"))
        #print(texte)
        instanceName = tablette[1].split("/")[-1][0:-4]
        filexml = pathtoresume+instanceName+"_resume"+".txt"
        pattern=r"\s+"
        filexml = re.sub(pattern, "", filexml)


    matches = re.findall(r'Fails:\s*([\d\s*]+),', texte)
    
    # Affichage des résultats
    for match in matches:
        pattern=r"\s+"
        match_without_spaces = re.sub(pattern, "", match)
        nr  = int(match_without_spaces)
        resFails.append(nr)
        #print(nr, type(nr))
        if needPCA ==1 :
            dictFails[instanceName] = nr
            nrfails = nr

        
    matches = re.findall(r'Solutions\s*:\s*([\d]+),', texte)
    nrsol = 0
    # Affichage des résultats
    for match in matches:
        pattern=r"\s+"
        match_without_spaces = re.sub(pattern, "", match)
        nr  = int(match_without_spaces)
        resSolutions.append(nr)
        if needPCA ==1 :
            dictSolutions[instanceName] = nr
            nrsol = nr

    matches = re.findall(r'Building\s*time\s*:\s*([\d\s*]+),', texte)
    # Affichage des résultats
    for match in matches:
        pattern=r"\s+"
        match_without_spaces = re.sub(pattern, "", match)
        nr  = int(match_without_spaces)
        resBuildingTime.append(nr)
        #print(nr, type(nr))

    if needPCA == 1:
        #print(filexml)
        t = readXml(texte)
        t.append(nrfails)
        t.append(nrsol)
        t.append(instanceName)
        stat_instance.append(t)
        name_instance.append(instanceName)
    
print(pathtoresume+'rm_in_'+pathtoresume[4:-2]+'.sh')
c = 0
with open(pathtoresume+'rm_in_'+pathtoresume[4:-2]+'.sh', 'w') as fichier:
    # Écriture dans le fichier
   
    for i in resFails:
        if i == 1:
             fichier.write("rm "+name_instance[c]+".xml"+"\n")
             fichier.write("rm "+name_instance[c]+".json"+"\n")
             fichier.write("rm "+name_instance[c]+"_resume.txt"+"\n")
        c+=1
    #print(random.choice(tableau))

# Triez le tableau en fonction de la deuxième colonne
tableau_trie = sorted(stat_instance, key=lambda x: x[1], reverse=True)
n = 10
# Récupérez les n plus grandes valeurs
resultat_final = tableau_trie[:n]

min_resultat_final = tableau_trie[len(stat_instance)-n:]

#print(stat_instance)
print(resultat_final)
print(" uwu ")
print(min_resultat_final)
print("mean building time : "+str(round(sum(resBuildingTime)/len(resBuildingTime),2))+"s;")
print("mean fails : "+str(round(sum(resFails)/len(resFails),2))+"; min fails : "+str(min(resFails))+"; max : "+str(max(resFails))+"; 1-fail  : "+str(len(list(filter(lambda x: x==1, resFails))))+"; percent1-fail  "+str(round((len(list(filter(lambda x: x==1, resFails)))/len(resFails)) *100,2))+"%")
print("; mean solutions : "+str(round(sum(resSolutions)/len(resSolutions),2))+"; nrSolutions : "+str(len(list(filter(lambda x: x==1, resSolutions))))+"; percentSolutions : "+str(round((len(list(filter(lambda x: x==1, resSolutions)))/len(resSolutions))*100,2))+"%"+"; solutionWihtout1-fail : "+str(round((len(list(filter(lambda x: x==1, resSolutions)))/(len(resSolutions)-len(list(filter(lambda x: x==1, resFails))))),2)*100)+"%" )

