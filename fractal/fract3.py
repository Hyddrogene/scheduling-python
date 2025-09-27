import turtle

def koch(t, order, size):
    if order == 0:
        t.forward(size)
    else:
        size /= 3.0
        koch(t, order-1, size)
        t.left(60)
        koch(t, order-1, size)
        t.right(120)
        koch(t, order-1, size)
        t.left(60)
        koch(t, order-1, size)

# Initialisation de Turtle
screen = turtle.Screen()
t = turtle.Turtle()
t.speed(0)

# Position de départ
t.penup()
t.goto(-150, 90)
t.pendown()

# Dessin du flocon
for _ in range(3):
    koch(t, order=4, size=300)
    t.right(120)

screen.mainloop()
 
