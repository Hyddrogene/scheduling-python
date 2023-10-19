using JuMP
import HiGHS
import Cbc
import LinearAlgebra

N = 4

model = Model(Cbc.Optimizer)
set_silent(model)

@variable(model, x[1:N, 1:N], Bin);


for i in 1:N
    @constraint(model, sum(x[i, :]) == 1)
    @constraint(model, sum(x[:, i]) == 1)
end

for i in -(N - 1):(N-1)
    @constraint(model, sum(LinearAlgebra.diag(x, i)) <= 1)
    println(LinearAlgebra.diag(x, i))
    @constraint(model, sum(LinearAlgebra.diag(reverse(x; dims = 1), i)) <= 1)
end

optimize!(model)

solution = round.(Int, value.(x))
#println(solution)
for i in 1:N
    println(solution[i,:])
end
