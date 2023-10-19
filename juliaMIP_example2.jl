using JuMP

import HiGHS
import Cbc
n = 4

function solveNQueen(n)
    Queen18 = Model(HiGHS.Optimizer)
    @variables(Queen18, begin
        0 <= t[1:n,1:n] <= 1
    end)
    
    @constraint(Queen18, sum(t)==n)
    
    @constraint(
        Queen18,
        [i = 1:n],
        sum(t[i, :])<=1
    )
    
    @constraint(
        Queen18,
        [i = 1:n],
        sum(t[:, i])<=1
    )
    
    @constraint(Queen18,[i = 1:n],sum(t[i, i])<=1)

    for i in 1:n
        @constraint(Queen18,[j = i:n] ,sum(t[j+1-i, i])<=1 )
    end
    
    for i in 1:n
        @constraint(Queen18,[j = i:n] ,sum(t[i,j+1-i])<=1 )
        #println([ [i,j+1-i] for j = i:n])
    end
    
    @constraint(Queen18,[i = 1:n],sum(t[i,(n+1)-i])<=1)

    tk = 0
    for i in 1:n
        print([[j,n-tk-j] for j = 1:n-i])
        @constraint(Queen18,[j = 1:n-i],sum(t[j,n-tk-j])<=1)
        tk += 1
    end
    
    for j in 1:n-1
        tk=0
        tmp = Vector{Int64}[]
        for l in j:n-1
            tk+=1
            push!(tmp,[l,n-tk])
        end
        @constraint(Queen18,[k = 1:length(tmp)],sum(t[tmp[k][1],tmp[k][2]])<=1)
    end
    
    optimize!(Queen18)
    println("""
    termination_status = $(termination_status(Queen18))
    primal_status      = $(primal_status(Queen18))
    objective_value    = $(objective_value(Queen18))
    """)
    solution = round.(Int, value.(t))
    for i in 1:n
        println(solution[i,:])
    end
    
    return

end

solveNQueen(n)
