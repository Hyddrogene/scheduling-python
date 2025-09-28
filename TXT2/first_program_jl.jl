println("hello world")
println("pi = ",pi)
println("pi*3 = ",pi*3)


function sphere_volume(val)::Float64
    return 4/3*pi*val^3
end

function disque_perimeter(r)::Float64
    return 2*pi*r
end

function area_disk(r)::Float64
    return pi*r^2
end

println("Value of sphere = ",sphere_volume(3))
println("Value of perimeter = ",disque_perimeter(3))
println("Value of area = ",area_disk(3))

str1 = "Value of area = $(area_disk(3))"

println(str1)

eps = disque_perimeter(3) - disque_perimeter(2)

using Printf

eps_string = @sprintf("%0.3f", eps)
val = "la valeur est $(eps_string)"
println(val)

value_f = parse(Float64,eps_string)
println("val = ",typeof(value_f))

#value = convert(Int64,value_f)
#println("val = ",value)

value = floor(Int,value_f)
println("val = ",value)

#phrase
s1 = "The quick brown fox jumps over the lazy dog α,β,γ banana"

i = findfirst(isequal('b'),s1)
#i = findfirst('b',s1)
r = findfirst("brown", s1)

println("i = ",i)
println("type(r) = ",typeof(r[1]))
println("r = ",r[2])

function remplacementdemot(r)
    return "caca"
end

r = replace(s1, "brown" => "red")
show(r);println() 

r = replace(s1, r"b[\w]*n" => "red")
println(r)

r = match(r"b[\w]*n", s1)
println(typeof(r))

r = eachmatch(r"[\w]{4,5}", s1)
for i in r print("\"$(i.match)\" ") end
#> "quick" "brown" "jumps" "over" "lazy"
println()

r = eachmatch(r"b[\w]*n", s1)
println(typeof(r))
for i in r print("\"$(i.match)\" ") end
#> "quick" "brown" "jumps" "over" "lazy"
println()

c = collect(1:10)
println(typeof(c))

t = join(c,"caca")
println(t)

function printsum(a)
    # summary generates a summary of an object
    println(summary(a), ": ", repr(a))
end

for i in c 
println(i)
end 
 a = Int64[12,45,89]
printsum(a)

println(a[end])

b = 1:20
println(b)
println(collect(b))

b = [i^2 for i = b]
println(b)

a6 = (Array{Int64, 1})[]
printsum(a6)

a6 = [[1,2],[3,4]]
printsum(a6)
push!(a6,[6,5])
println(a6)

a7 = repeat(a6,inner=[1],outer=[2])
println(a7)

println(collect(1:2:10))
println(collect(10:-1:1))

m2 = repeat(b,1,4)     # replicate a9 once into dim1 and twice into dim2
println("size: ", size(m2))
println("size: ",typeof(size(m2)))
println("size: ", m2)

k = 0
m4 = [k+(10*j)+(100*i) for i=1:2, j=1:3, k=1:2] # creates a 2x3x2 array of Int64
sm4 = size(m4)
for i in range(1,sm4[1])
    show("[")
    for j in range(1,sm4[2])
        show("[")
        for k in range(1,sm4[3])
            show(m4[i,j,k]);show(' ')
        end
        println("]")
    end
    println("]")
end
println(m4)
println(m4[1,1,2])

t= b[1:5]
println(t)
