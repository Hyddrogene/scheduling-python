"""
@author : Corentin Behuet
@date : mars 2023

L1 - TP3 : Carré magique

Consignes : Réalisez un programme permettant de générer un carré magique pour une taille donnée.

Conseils/Infos :
    -> Ce code est directement testable en l'état (si pycsp3 et tkinter sont installés). Vous pouvez tester différentes
        génération d'ordre n en modifiant la variable taille du main (l.108).
    -> La résolution du carré bimagique n'a pas été testée car trop de temps de calcul.
        Pour ce faire permetre la résolution par le solveur dans un temps "testable" on pourrait casser plein de
        symétries en préfixant certaines cases.
    -> Une solution pour tester le premier bimagique (n=8) a été proposée en s'appuyant sur la solution de Pfeffermann
"""
import pycsp3
import tkinter as tk
from tkinter import messagebox
from tkinter import StringVar
import random as rd
import time
import sys

def magicSquare(size:int = 4,grid:array = []):
    sumValue = int((size*(size*size+1))/2)
    #sumValue = pycsp3.var(sumValue)
    print("magic value = ",sumValue)

    #collumn are equals to magic value
    for i in range(0,size) :
        pycsp3.satisfy( pycsp3.Sum([grid[i][j] for j in range(0,size)]) == sumValue )

    #line are equals to magic value
    for i in range(0,size) :
        pycsp3.satisfy( pycsp3.Sum([grid[j][i] for j in range(0,size)]) == sumValue )

    #diagonal are equals to magic value
    pycsp3.satisfy( pycsp3.Sum([grid[i][i] for i in range(0,size)]) == sumValue )
    pycsp3.satisfy( pycsp3.Sum([grid[size-(i+1)][i] for i in range(0,size)]) == sumValue )
    #all values from the grid are different
    pycsp3.satisfy(pycsp3.AllDifferent(grid))

def  doubleMagicSquare(size,grid):
    magicSquare(size,grid)
    sumValue = int((size*((size**2)+1)*(2*(size**2)+1))/6)
    #line_value = pycsp3.VarArray(size=[size*2+2], dom=range(sumValue-1, sumValue+1))

    print("double magic value = ",sumValue)
    count = 0

    #collumn are equals to magic value
    for i in range(0,size) :
        #pycsp3.satisfy( pycsp3.Sum([grid[i][j]**2 for j in range(0,size)]) == line_value[count])
        pycsp3.satisfy( pycsp3.Sum([grid[i][j]**2 for j in range(0,size)]) == sumValue)
        count+=1
    #line are equals to magic value
    for i in range(0,size) :
        pycsp3.satisfy( pycsp3.Sum([grid[j][i]**2 for j in range(0,size)]) == sumValue )
        #pycsp3.satisfy( pycsp3.Sum([grid[j][i]**2 for j in range(0,size)]) == line_value[count] )
        count+=1
    pycsp3.satisfy( pycsp3.Sum([grid[i][i]**2 for i in range(0,size)]) == sumValue )
    #pycsp3.satisfy( pycsp3.Sum([grid[i][i]**2 for i in range(0,size)]) == line_value[count] )
    count+=1
    pycsp3.satisfy( pycsp3.Sum([grid[size-(i+1)][i]**2 for i in range(0,size)]) == sumValue)
    #pycsp3.satisfy( pycsp3.Sum([grid[size-(i+1)][i]**2 for i in range(0,size)]) == line_value[count] )

    '''for i in range(0,size*2+2):
        pycsp3.satisfy(line_value[i] == sumValue )'''

    #pycsp3.satisfy(pycsp3.AllEqual(line_value))
    #diagonal are equals to magic value



def symetrie_broken(size,grid,lineCard,lineTemp,collCard,collTemp):
    #global lineCard,lineTemp,collCard,collTemp
    for i in range(0,size):
        #x = [pycsp3.Var(dom = grid[i][j]%2)   for j in range(0,size)  ]
        
        for j in range(0,size):
            pycsp3.satisfy(lineTemp[i][j] == grid[i][j]%2 )
        pycsp3.satisfy(pycsp3.Cardinality([lineTemp[i][j] for j in range(0,size)],occurrences={0 : lineCard[i][0], 1:lineCard[i][1]}))
        pycsp3.satisfy(lineCard[i][0] %2 == 0, lineCard[i][1] %2 ==0)
    for j in range(0,size):
        #x = [pycsp3.Var(dom = grid[i][j]%2)   for j in range(0,size)  ]
        
        for i in range(0,size):
            pycsp3.satisfy(collTemp[i][j] == grid[i][j]%2 )
        pycsp3.satisfy(pycsp3.Cardinality([collTemp[i][j] for i in range(0,size)],occurrences={0 : collCard[j][0], 1:collCard[j][1]}))
        pycsp3.satisfy(collCard[j][0] %2 == 0, collCard[i][1] %2 ==0)
    #pycsp3.satisfy( pycsp3.LexDecreasing([grid[j][0] for j in range(0,size)],[grid[j][size-1] for j in range(0,size)]   ))
    for i in range(0,int(size//2)):
        pycsp3.satisfy( pycsp3.LexDecreasing([grid[i][j] for j in range(0,size)],[grid[size-1-i][j] for j in range(0,size)]   ))
    for i in range(0,int(size//2)):
        pycsp3.satisfy( pycsp3.LexDecreasing([grid[i][j] for j in range(0,size)],[grid[j][size-1-i] for j in range(0,size)]   ))

    '''for i in range(0,size):
        pycsp3.satisfy( pycsp3.LexDecreasing([grid[i][j] for j in range(0,size)],[grid[j][i] for j in range(0,size)]   ))'''
    '''for i in range(0,size):
        pycsp3.satisfy( pycsp3.LexDecreasing([grid[i][j] for j in range(0,size)],[grid[j][0] for j in range(0,size)]   ))'''

    for i in range(0,int(size//2)):
        pycsp3.satisfy( pycsp3.LexDecreasing([grid[i][j] for j in range(0,size)],[grid[j][-1-i] for j in range(0,size)]   ,strict=False))
    #pycsp3.satisfy( pycsp3.LexDecreasing([grid[0][j] for j in range(0,size)],[grid[j][size-1] for j in range(0,size)]   ))
    #pycsp3.satisfy( pycsp3.LexDecreasing([grid[0][j] for j in range(0,size)],[grid[j][0] for j in range(0,size)]   ))
    #pycsp3.satisfy( pycsp3.LexDecreasing([grid[0][j] for j in range(0,size)],[grid[0][size-1-j] for j in range(0,size)]   ))
    #pycsp3.satisfy( pycsp3.LexDecreasing([grid[0][j] for j in range(0,size)],[grid[j][size-1] for j in range(0,size)]   ))
    #line are equals to magic value
    #for i in range(0,size) :
    #    pycsp3.satisfy( pycsp3.LexIncreasing([grid[j][i] for j in range(0,size)],[grid[j][size-(i+1)] for j in range(0,size)]   ))

#choose problem type
'''dmagic = 0;
if dmagic :
    doubleMagicSquare(size,grid)
else :
    magicSquare(size,grid)
'''

def solution_execution():
    #messagebox.showinfo("Solution","la solution")
    global tab,size,grid,varTimer
    #variables of problem
    grid = pycsp3.VarArray(size=[size, size], dom=range(1, size*size+1))
    lineCard = pycsp3.VarArray(size=[size, 2], dom=range(1, size+1))
    lineTemp = pycsp3.VarArray(size=[size,size],dom=range(0,2))
    collCard = pycsp3.VarArray(size=[size, 2], dom=range(1, size+1))
    collTemp = pycsp3.VarArray(size=[size,size],dom=range(0,2))
    #add Clues 
    for i in range(0,len(tab)):
        for j in range(0,len(tab)):
            a = tab[i][j].get()
            if a != "" and int(a) > 0 and int(a) <= size**2:
                pycsp3.satisfy(grid[i][j] == int(a))
    symetrie_broken(size,grid,lineCard,lineTemp,collCard,collTemp)
    magicSquare(size,grid)
    #doubleMagicSquare(size,grid)
    instance = pycsp3.compile()
    ace = pycsp3.solver(pycsp3.CHOCO)
    t = time.perf_counter()
    result = ace.solve(instance)
    elapsed_time = time.perf_counter() - t
    #print(elapsed_time)
    if result is pycsp3.SAT:
            varTimer.set("%.4f s"%(elapsed_time))
            for i in range(0,size):
                for j in range(0,size):
                    tab[i][j].delete(0,len(tab[i][j].get()))
                    tab[i][j].insert(0,pycsp3.values(grid[i][j]))
                    #print("%3d"%(pycsp3.values(grid[i][j])),end=" ")
                    #print(pycsp3.values(lineTemp[i][j]),end=" ")
                print()
                    
    else:
        messagebox.showinfo("WARNING","!!!! UNSAT !!!!")
    pycsp3.clear()

def clear_tab():
    global tab,size,tab_modif,tab_modif_vert
    for i in range(0,len(tab)):
        a = 0
        for j in range(0,len(tab)):
            tab[i][j].delete(0,len(tab[i][j].get()))
        tab_modif_vert[i].set(a)
        tab_modif[i].set(a)

    
    
def test_execution():
    messagebox.showinfo("Test","Le test")

def calcul_value():
    global tab,tab_modif,tab_modif_vert
    for i in range(0,len(tab)):
        a = 0
        for j in range(0,len(tab)):
            if tab[i][j].get() != "":
                a += int(tab[i][j].get())
        tab_modif_vert[i].set(a)

    for i in range(0,len(tab)):
        a = 0
        for j in range(0,len(tab)):
            if tab[j][i].get() != "":
                a += int(tab[j][i].get())
        tab_modif[i].set(a)
        #tab_modif.insert(0,str(a))

def generate_grid_format(window,n):
    tab = []
    for i in range(0,n):
        tab_tmp = []
        for j in range(0,n):
            t = tk.Entry(window,width=6)            
            tab_tmp.append(t)
            t.grid(row=i,column=j)
        tab.append(tab_tmp)
    tab_modif  = []
    for i in range(0,size):     
            var = StringVar()
            t = tk.Label(window, textvariable=var, relief=tk.RAISED)
            var.set("0")     
            tab_modif.append(var)
            t.grid(row=size,column=i)
    tab_modif_vert = []
    for i in range(0,size):     
            var = StringVar()
            t = tk.Label(window, textvariable=var, relief=tk.RAISED)
            var.set("0")     
            tab_modif_vert.append(var)
            t.grid(row=i,column=size)

    btn1 = tk.Button(window,text="Solution",command=solution_execution).grid(row=0,column=n+1)
    #btn1.pack()
    #btn2 = tk.Button(window,text="Test",command=test_execution).grid(row=1,column=n+1)
    btn3 = tk.Button(window,text="Clear",command=clear_tab).grid(row=2,column=n+1)
    varTimer = StringVar()
    varLabelMagicValue = StringVar()
    btn4 = tk.Label(window, textvariable=varTimer, relief=tk.RAISED).grid(row=3,column=n+1)
    lbl1 = tk.Label(window, textvariable=varLabelMagicValue, relief=tk.RAISED).grid(row=4,column=n+1)
    btn5 = tk.Button(window,text="Calcul",command=calcul_value).grid(row=5,column=n+1)
    #btn2.pack()
    return tab,tab_modif,tab_modif_vert,varTimer,varLabelMagicValue
    #tk.Button(window,"Go").grid(row=1,column=n)
size = 8
if len(sys.argv) >1 :
    size = int(sys.argv[1])


grid = 0
lineCard = 0
lineTemp = 0
collCard = 0
collTemp = 0
window = tk.Tk()
window.title("Magic Square "+str(size))
tab,tab_modif,tab_modif_vert,varTimer,varLabelMagicValue = generate_grid_format(window, size)
a = int((size*(size*size+1))/2)
varLabelMagicValue.set("Val = "+str(a))
window.mainloop()
