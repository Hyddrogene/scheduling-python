import cpmpy as cp

# Decision variables
(x,y,z) = cp.intvar(1,10, shape=3)  # Python unpacks the array into the individual variables

# Initialise the model, here with 2 constraints
"""m = cp.Model(
   x == 1,
   x + y > 5
)"""

xs = cp.intvar(1,10, shape=3)
m = cp.Model(cp.AllDifferent(xs), maximize=cp.sum(xs))

# Adding more constraints
m += (y - z != x)
m += (x + y + z <= 15)
# you can also add a list of constraints, which is interpreted as a conjunction of constraints
m += [v <= 9 for v in [x,y,z]]

print(f"The model contains {len(m.constraints)} constraints")
print(m)  # pretty printing of the model, very useful for debuggin

hassol = m.solve()
print("Status:", m.status())  # Status: ExitStatus.OPTIMAL (0.03033301 seconds)
if hassol:
    print(m.objective_value(), xs.value())  # 27 [10  9  8]
else:
    print("No solution found.")