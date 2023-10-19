from mip import Model, xsum, maximize, BINARY,INTEGER

p = [10, 13, 18, 31, 7, 15]
w = [11, 15, 20, 35, 10, 33]
c = 58
I = range(len(w))

m = Model(name="Pas vraiment")
course = 2
teacher = 6
teachers = range(1,teacher+1)
prof = [
    [6,1],
    [3,6]
    ]

prof_ens = [[1 if j in prof[i] else 0  for j in range(1,7)]for i in range(0,len(prof))]
print(prof_ens)

x = [m.add_var(var_type=BINARY) for i in I]
y = [m.add_var(name='zCost', var_type=INTEGER, lb=-10, ub=10) for i in range(11)]
x_teacher = [m.add_var(name='x_teacher', var_type=INTEGER, lb=0, ub=teacher) for i in range(0,course)]
x_slot = [m.add_var(name='x_slot', var_type=INTEGER, lb=1, ub=10) for i in range(0,course)]


m.objective = maximize(xsum(p[i] * x[i] for i in I)+xsum(y[i] for i in range(11)))

m += xsum(w[i] * x[i] for i in I) <= c
for i in range(1,11):
    m += y[i-1] - y[i]>=1
for i in range(0,course):
    m += x_teacher[i] >= 1
for i in range(0,course):
    m += xsum(x_teacher)
    m += x_teacher[i]*prof_ens[i][x_teacher[i]] >= 1

m.optimize()

selected = [i for i in I if x[i].x >= 0.99]
print("selected items: {}".format(selected))
selected = [y[i].x for i in range(11)]
print("selected items: {}".format(selected))
