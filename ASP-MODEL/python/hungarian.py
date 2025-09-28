import numpy as np

def hungarian_algorithm(cost_matrix):
    """
    Implementation of the Hungarian algorithm for solving the assignment problem.
    """
    cost_matrix = np.array(cost_matrix)
    n, m = cost_matrix.shape

    # Step 1: Subtract row minima
    for i in range(n):
        cost_matrix[i] -= cost_matrix[i].min()

    # Step 2: Subtract column minima
    for j in range(m):
        cost_matrix[:, j] -= cost_matrix[:, j].min()

    # Step 3: Cover zeros with minimum number of lines
    def cover_zeros(matrix):
        """
        Cover all zeros in the matrix with a minimum number of horizontal and vertical lines.
        """
        row_covered = [False] * n
        col_covered = [False] * m
        marked_zeros = np.zeros_like(matrix, dtype=bool)

        # Mark independent zeros
        for i in range(n):
            for j in range(m):
                if matrix[i, j] == 0 and not row_covered[i] and not col_covered[j]:
                    marked_zeros[i, j] = True
                    row_covered[i] = True
                    col_covered[j] = True

        row_covered = [False] * n
        col_covered = [False] * m

        while True:
            # Cover all columns containing marked zeros
            for i in range(n):
                for j in range(m):
                    if marked_zeros[i, j]:
                        col_covered[j] = True

            # Check if all columns are covered
            if sum(col_covered) == min(n, m):
                break

            # Find a minimum uncovered value
            uncovered_min = np.min(matrix[~np.array(row_covered), :][:, ~np.array(col_covered)])
            
            # Add this value to all covered rows and subtract it from uncovered columns
            for i in range(n):
                if row_covered[i]:
                    matrix[i] += uncovered_min
            for j in range(m):
                if not col_covered[j]:
                    matrix[:, j] -= uncovered_min

        return marked_zeros

    marked_zeros = cover_zeros(cost_matrix)
    
    # Find the optimal assignment
    assignments = []
    for i in range(n):
        for j in range(m):
            if marked_zeros[i, j]:
                assignments.append((i, j))

    return assignments, cost_matrix

# Example input
cost_matrix = [
    [2, 4, 5],
    [3, 5, 1],
    [4, 6, 5]
]

assignments, reduced_cost_matrix = hungarian_algorithm(cost_matrix)
print(assignments)
