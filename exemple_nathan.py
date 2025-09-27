from pycsp3 import *
import numpy as np
def zebra_puzzle():
    clear() 
    position = range(1,6)
    # Entités
    Leslie, Holly, Andrea, Julie, Victoria = names = VarArray(size = 5, dom = position)
    Wilson, Miller, Davis, Brown, Lopes = surnames = VarArray(size = 5, dom = position)
    Red, Blue, White, Yellow, Green = shirts = VarArray(size = 5, dom = position)
    Argentine, Italian, Chilean, Australian, Bordeaux = wines = VarArray(size = 5, dom = position)
    Farfalle, Lasagne, Penne, Spaghetti, Ravioli = pastas = VarArray(size = 5, dom = position)
    y30, y35, y40, y45, y50 = age = VarArray(size = 5, dom = position)

    satisfy(
        AllDifferent(names),
        AllDifferent(surnames),
        AllDifferent(shirts),
        AllDifferent(wines),
        AllDifferent(pastas),
        AllDifferent(age))
    
    """
    # Contraintes
    satisfy(
        # Contraintes fournies dans la description
        abs(White - Italian) == 1,
        Davis < Miller,
        Miller < Brown, 
        Red < y45,
        Chilean == Farfalle,
        1 == Argentine,
        Andrea == y35 + 1,
        Davis < Blue,
        Blue < Holly,
        abs(Victoria - Leslie) == 1,
        Red < Australian,
        abs(Wilson - y30) == 1,
        Leslie == 30 - 1,
        Red < Holly,
        Brown == Julie - 1,
        y30 == Penne,
        Wilson == White,
        Italian < Lasagne,
        Lasagne < Spaghetti,
        Blue == 2,
        y40 == Lasagne,
        Lopes == 5,
        Victoria < Australian,
        Australian < Bordeaux,
        Yellow == y35 - 1,
    )
"""
    # Résoudre le problème
    #ace = solver(ACE)
    #result = ace.solve(instance)
    result = solve(solver=CHOCO)

    print("Result:", result)
    if result is SAT:
        # Afficher les résultats
        print("Solution found:")
        for i in range(0,5):
            print(str(names[i])+" : "+str(values(names[i])))
            print(str(surnames[i])+" : "+str(values(surnames[i])))
            print(str(shirts[i])+" : "+str(values(shirts[i])))
            print(str(wines[i])+" : "+str(values(wines[i])))
            print(str(pastas[i])+" : "+str(values(pastas[i])))
            print(str(age[i])+" : "+str(values(age[i])))
            tytyt = np.array(values(age))
            print(len(tytyt)," unique ",len(np.unique(tytyt)))
            #print(": "+str(result.names[i]))

            #, {result.value(surnames[i])}, {result.value(shirts[i])}, {result.value(wines[i])} wines, {result.value(pastas[i])} pasta, {result.value(age[i])} years old")
zebra_puzzle()
