from graphviz import Digraph

def create_feature_model():
    dot = Digraph(comment='Feature Model Example')
    
    dot.attr(splines='line', rankdir='TB')  # Ensuring the edges are straight and the graph is top-bottom

    # Root node
    dot.node('Move', '', shape='none', fontsize='20', fontcolor='black', style='bold')
    dot.edge('Move', 'Move_label', style='invis', arrowhead='none')
    dot.node('Move_label', 'Move', shape='none', fontsize='12', fontcolor='black')

    # Level 1 nodes
    nodes_lvl1 = ['windowLayout', 'moveIcon', 'abortMoveOp', 'moveInput', 'partiallyOffScreen', 
                  'moveResizeFeedback', 'constrainedMove', 'moveErasure', 'exposeAfterMove']
    for node in nodes_lvl1:
        dot.node(node, '', shape='none', fontsize='12', fontcolor='black')
        dot.edge(node, f'{node}_label', style='invis', arrowhead='none')
        dot.node(f'{node}_label', node, shape='none', fontsize='12', fontcolor='black')

    # Connecting Level 1 to root
    for node in nodes_lvl1:
        dot.edge('Move', node, arrowhead='dot')

    # Level 2 nodes
    nodes_lvl2 = {
        'windowLayout': ['overlappedLayout', 'tiledLayout'],
        'moveInput': ['border', 'interior'],
        'partiallyOffScreen': ['zapEffect'],
        'moveResizeFeedback': ['windowConfiguration', 'interactiveFeedback'],
        'moveErasure': ['eraseBefore', 'eraseAfter']
    }
    for parent, children in nodes_lvl2.items():
        for child in children:
            dot.node(child, '', shape='none', fontsize='10', fontcolor='black')
            dot.edge(child, f'{child}_label', style='invis', arrowhead='none')
            dot.node(f'{child}_label', child, shape='none', fontsize='10', fontcolor='black')
            dot.edge(parent, child, arrowhead='dot')

    # Level 3 nodes
    nodes_lvl3 = {
        'tiledLayout': ['tiledColumns', 'tiledArbitrary'],
        'interactiveFeedback': ['ghostFeedback', 'opaqueFeedback']
    }
    for parent, children in nodes_lvl3.items():
        for child in children:
            dot.node(child, '', shape='none', fontsize='10', fontcolor='black')
            dot.edge(child, f'{child}_label', style='invis', arrowhead='none')
            dot.node(f'{child}_label', child, shape='none', fontsize='10', fontcolor='black')
            dot.edge(parent, child, arrowhead='dot')

    # OR and XOR nodes
    dot.node('or1', '', shape='point', width='0.1', style='filled', fillcolor='black')
    dot.node('xor1', '', shape='diamond', width='0.1', style='filled', fillcolor='white')

    # Connecting OR and XOR nodes to relevant features
    dot.edge('windowLayout', 'or1', arrowhead='dot')
    dot.edge('or1', 'overlappedLayout', arrowhead='dot')
    dot.edge('or1', 'tiledLayout', arrowhead='dot')
    dot.edge('interactiveFeedback', 'xor1', arrowhead='dot')
    dot.edge('xor1', 'ghostFeedback', arrowhead='dot')
    dot.edge('xor1', 'opaqueFeedback', arrowhead='dot')

    return dot

dot = create_feature_model()
dot.render('feature_model_example', format='png', view=True)
