import os
import subprocess
from GenerateurProduct import *
import re
import sys
import random
import time

ti_me = time.strftime("%Y%m%d_%H%M%S")

def generateConfigJava(arg1,arg2):
    uspconf = uspConfig()
    config_filename = generate_config_filename(arg1,arg2)
    print("Nom du fichier de configuration généré :", config_filename)
    uspconf.write(config_filename)
    return config_filename


def generateInstance(java_exec, java_file_path, config_filename,arg1 ,arg2,rseed):
    # Compile the Java file
    print(os.system("pwd"))
    run_command =[java_exec,"-jar",java_file_path, config_filename, "./../../src/ExperimentGenerator/configurationVolume.xml", "./../../src/ExperimentGenerator/configuration_rule.xml", "./../../src/ExperimentGenerator/teacherCalculationByEtape.xml","./../../src/ExperimentGenerator/effectifFormationsData.xml",arg1,arg2,str(rseed)]
    #result = subprocess.run(run_command, shell=True, capture_output=True, text=True)
    #run_command = " ".join(run_command)
    print(run_command)
    #result = subprocess.run(run_command, shell=True, capture_output=True , text=True,check=True)
    process = subprocess.Popen(run_command, stdout=subprocess.PIPE, text=True)
    #result = os.system(run_command)
    process.wait()
    stdout, stderr = process.communicate()
    #error = result.stderr
    #print(f'Error:\n{error}')
    # Access the result output
    #output = result.stdout
    print(f'Output:\n{stdout}')
    file_output = stdout.split("\n")[-2]
    print(file_output)
    fileOut = file_output.split(" ")[1]
    #fileOut = fileOut[2:-4]+str(arg1)+str(arg2)+'.xml'
    print("fileOut = "+fileOut)
    return fileOut

def generateJsonFile(java_exec,java_file_parser,fileOut):
    #command = [java_exec,"-jar",java_xms,java_xmx,java_file_parser,fileOut] 
    command = [java_exec,"-jar",java_file_parser,fileOut[2:]] 
    print(command)
    process = subprocess.Popen(command, stdout=subprocess.PIPE, text=True)
    stdout, stderr = process.communicate()
    jsonFile = stdout.split(":")[-1].replace("\n","")
    #print("PARSER stdout",stdout.split(":")[-1])
    print("jsonFile ",jsonFile)
    #if jsonFile == fileOut :
    print(stderr)
    print(stdout)
    return jsonFile

def generateSolution(config_filename,java_exec,java_file_choco,jsonFile,fileOut,strategieJson,rseed):
    command = [java_exec,"-jar",java_file_choco,jsonFile,fileOut,strategieJson]
    print(command)
    process = subprocess.Popen(command, stdout=subprocess.PIPE,stderr=subprocess.PIPE, text=True)
    stdout, stderr = process.communicate()
    print(stdout)
    matches = re.findall(r'(Model\s*\[[\w*\d\s*\,*]+\])', stdout)
    sol0 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol0)
    matches = re.findall(r'(Solutions:\s*[\d\s*\,*]+)', stdout)
    sol1 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol1)
    matches = re.findall(r'(Building time :\s*[\d\s*\,*]+s)', stdout)
    sol2 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol2)
    matches = re.findall(r'(Fails:\s*[\d\s*\,*]+)', stdout)
    sol3 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol3)
    matches = re.findall(r'(Restarts:\s*[\d\s*\,*]+)', stdout)
    sol4 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol4)
    matches = re.findall(r'(Variables\s*:\s*[\d\s*\,*]+)', stdout)
    sol5 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol5)
    matches = re.findall(r'(Constraints\s*:\s*[\d\s*\,*]+)', stdout)
    sol6 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol6)
    matches = re.findall(r'(Nodes\s*:\s*[\d\s*\,*]+)', stdout)
    sol7 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol7)
    matches = re.findall(r'(Backtracks\s*:\s*[\d\s*\,*]+)', stdout)
    sol8 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol8)

    matches = re.findall(r'(Resolution\s*time\s*:\s*[\d\s*\,*]+s)', stdout)
    sol9 = re.sub(r"\s+", "",  matches[0].replace("\n",""))
    #print(sol9)
    
    with open('file_statistique_execution.txt', 'a') as fichier:
        fichier.write(config_filename+", "+fileOut+", "+"Model[Modele USP][seed="+str(rseed)+"]"+", "+sol1+", "+sol2+", "+sol3+", "+sol4+", "+sol5+", "+sol6+", "+sol7+", "+sol8+", "+sol9+"\n")
   
    nrr = re.sub(r"\s+", "",  sol1.replace("\n",""))
    matches = re.findall(r'([\d]+)', nrr)
    numbers = [int(match) for match in matches]
    #print(numbers)
    if 1 == numbers[0]:
        with open(jsonFile[:-4]+'file_log_execution.log', 'a') as fichier:
            fichier.write(config_filename+", "+fileOut+", "+str(stderr)+"\n")
        return 1
    return 0

def writeIntoFile(filename, tableau):
    with open(filename, 'w') as fichier:
        for valeur in tableau:
            fichier.write(str(valeur) + '\n')
    return filename

def readIntoFile(filename):
    tableau = []
    with open(filename, 'r') as fichier:
        lignes = fichier.readlines()
        for ligne in lignes:
            tableau.append(ligne.strip())
    return tableau





def generateConfigNr():
    for i in range(0,nrConfig):
        config_filename = generateConfigJava("config",str(i))
        configs.append(config_filename)
    name_save = "configs_"+ti_me+"_filename.txt"
    writeIntoFile(name_save,configs)
    print("Config file : "+name_save+"\n")



def run(arg1,arg2,rseed):
    #arguments = sys.argv
    #arg1 = arguments[1] if len(arguments) > 1 else None
    #arg2 = arguments[2] if len(arguments) > 2 else None

    #java_file_path = './../choco/Constraint_model/'
    java_file_path = 'ExprimentGenerator.jar'
    java_file_parser = 'ParserJson.jar'
    java_file_choco = 'ChocoSolverForEDT.jar'

    java_exec = "./../../../jdk-20.0.2/bin/java"
    java_exec = "java"


    java_xms = "-Xms4096M"
    java_xmx = "-Xmx10240M"

    strategieJson = "test_strategie_simple_v2.json"
    #def chooseRandomConfig(tableau):
    #return random.choice(tableau)
    nrConfig = 100
    #config_filename ="config_maquette/config_20231201_111728.xml"
    configsPath = "configs_20231204_083316_filename.txt"
    configsPath = "configs_20240105_155800.txt"
    configs = readIntoFile(configsPath)
    
    try:
        randInt = random.choice([0,1])
        randBool = bool(randInt)
        #print(randBool)
        if randBool :
            config_filename = random.choice(configs)
        else :
            config_filename = generateConfigJava(arg1,arg2)
        
        fileOut = generateInstance(java_exec, java_file_path, config_filename,arg1 ,arg2,rseed)

        jsonFile = generateJsonFile(java_exec,java_file_parser,fileOut)
                        
        generateSolution(config_filename,java_exec,java_file_choco,jsonFile,fileOut,strategieJson,rseed)



    except Exception as e:
        # Bloc de code exécuté pour d'autres types d'exceptions
        print(f"Une exception s'est produite : {e}") 
