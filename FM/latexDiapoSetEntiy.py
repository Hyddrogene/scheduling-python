from graphviz import Digraph

# Create a new directed graph
dot = Digraph()

# Define node attributes for different shapes and styles
node_attr = {
    'entity': {'shape': 'rectangle', 'style': 'rounded', 'width': '1.5', 'height': '0.75'},
    'relationship': {'shape': 'ellipse', 'width': '1.5', 'height': '0.75'},
    'slot': {'shape': 'box', 'width': '1.5', 'height': '0.75'}
}

# Add nodes
dot.node('student', 'student', **node_attr['entity'])
dot.node('room', 'room\ncapacity\nvirtual', **node_attr['entity'])
dot.node('lecturer', 'lecturer', **node_attr['entity'])
dot.node('course', 'course', **node_attr['entity'])
dot.node('part', 'part\nnrSessionsPerClass\nsessionDuration\nnrRoomsPerSession\nnrTeachersPerSession', **node_attr['entity'])
dot.node('class', 'class\nparent', **node_attr['entity'])
dot.node('session', 'session', **node_attr['entity'])
dot.node('week', 'week\n1  2', **node_attr['slot'])
dot.node('weekday', 'weekday\n1 2 3 4 5 1 2 3', **node_attr['slot'])
dot.node('daily_slot', 'daily slot', **node_attr['slot'])
dot.node('allowed', 'is allowed for\nmandatory', **node_attr['relationship'])
dot.node('teaches', 'teaches\nnrSessionsPerPart', **node_attr['relationship'])

dot.node("Invisible1", style="invis")
dot.node("Invisible2", style="invis")

dot.edge("course", "Invisible1")
dot.edge("course", "Invisible2")

with dot.subgraph() as s:
    s.attr(rank='same')
    s.node('Invisible1')
    s.node('Invisible2')
    #s.node("part")
    
with dot.subgraph() as s:
    s.attr(rank='same')
    s.node('room')
    s.node('allowed')
    s.node("part")

with dot.subgraph() as s:
    s.attr(rank='same')
    s.node('course')
    s.node('student')

# Add edges
dot.edge('student', 'course', label='registers to')
dot.edge('room', 'allowed')
dot.edge('allowed', 'part')
dot.edge('lecturer', 'teaches')
dot.edge('teaches', 'part')
dot.edge('course', 'part')
dot.edge('part', 'class', label='can occur')
dot.edge('class', 'session')
dot.edge('week', 'weekday')
dot.edge('weekday', 'daily_slot')

# Render the graph to a file
dot.render('uml_diagram', format='png', view=True)
