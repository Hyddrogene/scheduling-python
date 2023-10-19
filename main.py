from minizinc import Instance, Model, Solver

chuffed = Solver.lookup("chuffed")

model = Model()
model.add_string(
    """
    include "all_different.mzn";
    set of int: A;
    set of int: B;
    array[A] of var B: arr;
    var set of B: X;
    var set of B: Y;

    constraint all_different(arr);
    constraint forall (i in index_set(arr)) ( arr[i] in X );
    constraint forall (i in index_set(arr)) ( (arr[i] mod 2 = 0) <-> arr[i] in Y );
    """
)

instance = Instance(chuffed, model)
instance["A"] = range(3, 8)  # MiniZinc: 3..7
instance["B"] = {4, 3, 2, 1, 0}  # MiniZinc: {4, 3, 2, 1, 0}


result = instance.solve()
print(result["X"])  # range(0, 5)
assert isinstance(result["X"], range)
print(result["Y"])  # {0, 2, 4}
assert isinstance(result["Y"], set)

