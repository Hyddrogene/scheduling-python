from mip import Model, xsum, maximize, BINARY,INTEGER

Queen18 = Model("N-Queen")

nrQueen = 40

size = range(0,nrQueen)

x = [[Queen18.add_var(var_type=BINARY) for i in size] for j in size]

#Queen18.objective = maximize()
for j in size :
    Queen18 += xsum(x[j][i] for i in size) <= 1

for j in size :
    Queen18 += xsum(x[i][j] for i in size) <= 1
    
Queen18 += xsum(xsum(x[j][i] for i in size) for j in size) == nrQueen

for i in range(1,nrQueen-1):
    Queen18 += xsum(x[j-i][j] for j in range(i,nrQueen)) <= 1

for i in range(1,nrQueen-1):
    print([[j,j-i] for j in range(i,nrQueen)])
    Queen18 += xsum(x[j][j-i] for j in range(i,nrQueen)) <= 1
    
Queen18 += xsum(x[i][i] for i in size) <= 1
Queen18 += xsum(x[i][nrQueen -1- i] for i in size) <= 1

for i in range(0,nrQueen-1):
    #print(j,nrQueen-j-2)
    Queen18 += xsum(x[j][nrQueen-2-j] for j in range(0,nrQueen-1-i)) <= 1

for i in range(1,nrQueen):
    for j in range(1,nrQueen-1):
        t=0
        tmp = []
        for l in range(j,nrQueen):
            t+=1
            tmp.append([l,nrQueen-t])
        Queen18 += xsum(x[tmp[i][0]][tmp[i][1]] for i in range(0,len(tmp))) <= 1
    
Queen18.optimize()

selected = [[ int(x[i][j].x) for j in size] for i in size]
for j in size:
    print(selected[j])
#print("selected items: {}".format(selected))
