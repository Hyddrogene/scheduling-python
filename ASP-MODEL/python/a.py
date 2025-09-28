from collections import deque

def shortest_path_in_grid(grid):
    rows, cols = len(grid), len(grid[0])
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]  # Right, Down, Left, Up
    
    # BFS Initialization
    queue = deque([(0, 0, 0)])  # (row, col, distance)
    visited = set()
    visited.add((0, 0))
    
    while queue:
        row, col, dist = queue.popleft()
        
        # If we reach the bottom-right corner, return the distance
        if row == rows - 1 and col == cols - 1:
            return dist + 1
        
        # Explore neighbors
        for dr, dc in directions:
            new_row, new_col = row + dr, col + dc
            
            if (0 <= new_row < rows and 0 <= new_col < cols and
                (new_row, new_col) not in visited and grid[new_row][new_col] == 1):
                queue.append((new_row, new_col, dist + 1))
                visited.add((new_row, new_col))
    
    return -1  # If there's no path

# Input example
grid = [
    [1, 1, 0],
    [0, 1, 0],
    [0, 1, 1]
]

result = shortest_path_in_grid(grid)
print(result)
