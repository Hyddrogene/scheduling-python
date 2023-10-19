from minizinc import Instance, Model, Solver

chuffed = Solver.lookup("chuffed")

file = "exemplemzn02.mzn";

model = Model();
model.add_file(file);
instance = Instance(chuffed,model);
instance["n"] = 4;
result = instance.solve();
print(result.status);
if result.status.has_solution() :
    print(result["X_tab"]);
    print(result.statistics);