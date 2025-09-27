import matplotlib.pyplot as plt
import numpy as np

def sierpinski_triangle(points, depth):
    if depth == 0:
        plt.fill(*zip(*points), "black")
    else:
        mid1 = (points[0] + points[1]) / 2
        mid2 = (points[1] + points[2]) / 2
        mid3 = (points[2] + points[0]) / 2
        sierpinski_triangle([points[0], mid1, mid3], depth - 1)
        sierpinski_triangle([mid1, points[1], mid2], depth - 1)
        sierpinski_triangle([mid3, mid2, points[2]], depth - 1)

plt.figure(figsize=(6,6))
points = np.array([[0, 0], [1, 0], [0.5, np.sqrt(3)/2]])
sierpinski_triangle(points, depth=5)
plt.axis("off")
plt.show()
