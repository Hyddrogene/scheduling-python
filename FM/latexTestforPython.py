from graphviz import Digraph

dot = Digraph(comment='Feature Model Example')
dot.attr(splines='line', rankdir='TB')
# Root node
dot.node('Move', 'Move', style='bold')

# Level 1 nodes
dot.node('windowLayout', 'windowLayout', style='filled', fillcolor='lightgrey')
dot.node('moveIcon', 'moveIcon', shape='none')
dot.node('abortMoveOp', 'abortMoveOp', shape='none')
dot.node('moveInput', 'moveInput', shape='none')
dot.node('partiallyOffScreen', 'partiallyOffScreen', shape='none')
dot.node('moveResizeFeedback', 'moveResizeFeedback', shape='none')
dot.node('constrainedMove', 'constrainedMove', shape='none')
dot.node('moveErasure', 'moveErasure', shape='none')
dot.node('exposeAfterMove', 'exposeAfterMove', shape='none')

# Connecting Level 1 to root
dot.edge('Move', 'windowLayout',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'moveIcon',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'abortMoveOp',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'moveInput',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'partiallyOffScreen',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'moveResizeFeedback',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'constrainedMove',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'moveErasure',arrowhead='odot', tailport='s', headport='n')
dot.edge('Move', 'exposeAfterMove',arrowhead='odot', tailport='s', headport='n')

# Level 2 nodes
dot.node('overlappedLayout', 'overlappedLayout', shape='none')
dot.node('tiledLayout', 'tiledLayout', shape='none')
dot.node('border', 'border', shape='none')
dot.node('interior', 'interior', shape='none')
dot.node('zapEffect', 'zapEffect', shape='none')
dot.node('windowConfiguration', 'windowConfiguration', shape='none')
dot.node('interactiveFeedback', 'interactiveFeedback', shape='none')
dot.node('eraseBefore', 'eraseBefore', shape='none')
dot.node('eraseAfter', 'eraseAfter', shape='none')

# Connecting Level 2 to Level 1
dot.edge('windowLayout', 'overlappedLayout',arrowhead='odot', tailport='s', headport='n')
dot.edge('windowLayout', 'tiledLayout',arrowhead='odot', tailport='s', headport='n')
dot.edge('moveInput', 'border',arrowhead='odot', tailport='s', headport='n')
dot.edge('moveInput', 'interior',arrowhead='odot', tailport='s', headport='n')
dot.edge('partiallyOffScreen', 'zapEffect',arrowhead='odot', tailport='s', headport='n')
dot.edge('moveResizeFeedback', 'windowConfiguration',arrowhead='odot', tailport='s', headport='n')
dot.edge('moveResizeFeedback', 'interactiveFeedback',arrowhead='odot', tailport='s', headport='n')
dot.edge('moveErasure', 'eraseBefore',arrowhead='odot', tailport='s', headport='n')
dot.edge('moveErasure', 'eraseAfter',arrowhead='odot', tailport='s', headport='n')

# Level 3 nodes
dot.node('tiledColumns', 'tiledColumns', shape='none')
dot.node('tiledArbitrary', 'tiledArbitrary', shape='none')
dot.node('ghostFeedback', 'ghostFeedback', shape='none')
dot.node('opaqueFeedback', 'opaqueFeedback', shape='none')

# Connecting Level 3 to Level 2
dot.edge('tiledLayout', 'tiledColumns',arrowhead='odot', tailport='s', headport='n')
dot.edge('tiledLayout', 'tiledArbitrary',arrowhead='odot', tailport='s', headport='n')
dot.edge('interactiveFeedback', 'ghostFeedback',arrowhead='odot', tailport='s', headport='n')
dot.edge('interactiveFeedback', 'opaqueFeedback',arrowhead='odot', tailport='s', headport='n')

# Render the graph
dot.render('feature_model_example', format='png', view=True)
