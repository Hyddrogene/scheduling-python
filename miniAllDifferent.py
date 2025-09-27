class Variable:
    def __init__(self, name, domain):
        self.name = name
        self.domain = list(domain)  # Copie pour éviter la modification durant les itérations

def build_compatibility_graph(variables):
    nodes = {}
    for var1 in variables:
        for val1 in var1.domain:
            node1 = (var1.name, val1)
            nodes[node1] = []
            for var2 in variables:
                if var1 != var2:
                    for val2 in var2.domain:
                        if val1 != val2:
                            node2 = (var2.name, val2)
                            nodes[node1].append(node2)
    return nodes

def tarjan_scc(nodes):
    index = {}
    lowlink = {}
    on_stack = {}
    stack = []
    components = []
    idx = [0]

    def strongconnect(node):
        index[node] = idx[0]
        lowlink[node] = idx[0]
        idx[0] += 1
        stack.append(node)
        on_stack[node] = True

        for neighbor in nodes[node]:
            if neighbor not in index:
                strongconnect(neighbor)
                lowlink[node] = min(lowlink[node], lowlink[neighbor])
            elif on_stack[neighbor]:
                lowlink[node] = min(lowlink[node], index[neighbor])

        if lowlink[node] == index[node]:
            component = []
            while True:
                w = stack.pop()
                on_stack[w] = False
                component.append(w)
                if w == node:
                    break
            components.append(component)

    for node in nodes:
        if node not in index:
            strongconnect(node)

    return components

def filter_domains(variables, components):
    valid_values = {var.name: set() for var in variables}
    for component in components:
        if len(component) == len(variables):  # Check if the component covers all variables
            for (name, value) in component:
                valid_values[name].add(value)
    
    for var in variables:
        var.domain = [val for val in var.domain if val in valid_values[var.name]]

# Example usage
variables = [Variable('X', [1, 2, 3]), Variable('Y', [2, 3, 4]), Variable('Z', [1, 3, 4])]
graph = build_compatibility_graph(variables)
components = tarjan_scc(graph)
filter_domains(variables, components)

for var in variables:
    print(f"Variable {var.name} has filtered domain: {var.domain}")
