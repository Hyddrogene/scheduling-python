from minizinc import Instance, Model, Solver

chuffed = Solver.lookup("chuffed")

file = "exemplemzn.mzn";

model = Model();
model.add_file(file);
instance = Instance(chuffed,model);
instance["n"] = 4;
result = instance.solve();
print(result["A_tab"]);
print(result["B_tab"]);
print(result.statistics);