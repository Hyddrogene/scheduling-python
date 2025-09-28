# -*- coding: utf-8 -*-
"""
Created on Fri Oct  4 13:25:54 2024

@author: Juliette BOURGEOIS
"""

import numpy as np
import matplotlib.pyplot as plt
data_name = "C:/Users/Juliette/Documents/Scolaire/Université/Poitiers/Python/data.txt"
data_name="data.txt"
data = np.loadtxt(data_name,delimiter=',')

posX, posY = data[:, 0], data[:, 1]

#1-2. Print posX, print posY
print("posX:", posX)
print("posY:", posY)

#1-3. Find and print the maximum of posY, find the index of the maximum value of posY.
max_posY = np.max(posY)
max_index = np.argmax(posY)
print("Maximum value of posY:", max_posY)
print("Index of maximum value:", max_index)

#1-4. Sort posY by ascending value, and write the sorted values in a file.
sorted_posY = np.sort(posY)
np.savetxt('sorted_posY.txt', sorted_posY)
print("Sorted values:", sorted_posY)

#2-1. Plot posY as a function of posX, add a legend, units, title, axis label.... nice color, or mark...
#2-2. Plot on the same graph the sorted values of posY as a function of Posx, with a different color. Add the name of the new curve in the legend.
fig = plt.figure()
ax = fig.add_subplot(1, 1, 1)
ax.plot(posX, posY, color='#2c811d',label='posY as a function of posX')
ax.plot(posX, sorted_posY, color='#7141f0',label="sorted values of posY as a function of posX")
ax.set_xlim([0, 103])
ax.set_ylim([0, 3])
ax.set_title('posY as a function of posX')
ax.set_ylabel('posY')
ax.set_xlabel('posX')
ax.legend(loc='upper left',fontsize=9)
plt.grid(True)
plt.show()

#2-3. Plot both curves on two separate graphs using subplots command.
fig, (ax1, ax2) = plt.subplots(2, 1, sharex=True, sharey=True, figsize=(10,5))
ax1.plot(posX, posY, color='#2c811d')
ax2.plot(posX, sorted_posY, color='#7141f0')
ax1.set_xlim([0, 103])
ax1.set_ylim([0, 3])
ax2.set_xlim([0, 103])
ax2.set_ylim([0, 3])
ax1.set_title('posY as a function of posX',color='#2c811d')
ax1.set_ylabel('posY')
ax1.set_xlabel('posX')
plt.grid(True)
ax2.set_title('sorted values of posY as a function of posX', color='#7141f0')
ax2.set_ylabel('sorted values of posY')
ax2.set_xlabel('posX')
plt.grid(True)
plt.gcf().subplots_adjust(hspace=0.3)
plt.show()

#3-1. Fit posY as a function of posX with the equation posY= a*np.sin(b*posX+c)+d where a, b, c, d are parameters to determine thanks to a curve fitting. To have an idea of the initial guess for some of the parameters: find the average value and the median value of Posy, the max, and min of Posy, and be lucky. Print the parameters.
import math
from scipy.optimize import curve_fit

def function(posX, a,b,c,d):
    return a*np.sin(b*posX+c)+ d
function(posX, 1, 1, 1, 1)

mean_posY = np.mean(posY)
median_posY = np.median(posY)
maxY = max(posY)
minY = min(posY)

d = ((maxY + minY) / 2)
a = maxY - d

def estimation_b_c(posX, posY, d, a):
    compt, start, end = 0, 0, 0
    positif = (posY[0] >= 0)
    sign = np.sign(posY[0])
    for i, y in enumerate(posY):
        if sign * y < sign * (d - a / 10) and positif:
            compt += 1
            end = posX[i]
            positif = False
            if compt == 1:
                start = posX[i]
        elif sign * y > sign * (a / 10 + d) and not positif:
            positif = True
    b = (start - end) / (2 * math.pi * compt) if compt else 1
    c = -start * b
    return b, c

b, c = estimation_b_c(posX, posY, d, a)
print("Old c=",c)
c= 1


print(f"Initial parameters: a={a}, b = {b}, c = {c}, d = {d}")

# Ajustement des paramètres avec curve_fit
popt, _ = curve_fit(function, posX, posY, p0=[a,b,c,d])
print(f"Fitted parameters: a={popt[0]}, b={popt[1]}, c={popt[2]}, d={popt[3]}")

a, b, c, d = popt


#3-2. Build the list model_posY based on the test_func and the fitted parameters for posX.
def test_func(x):
    listx = []
    for i in x:
        listx.append(a * np.sin(b * i + c) + d)
    return listx

model_posX = np.linspace(posX[0], posX[-1], len(posX))
model_posY = a * np.sin(b * model_posX + c) + d

#params, params_covariance = curve_fit(function, posX, posY, p0=[a, b, c, d])
#a, b, c, d = params
#print(f"Fitted parameters: a={a}, b={b}, c={c}, d={d}")

#3-2. Calculate the error between posY and its model at each point: posY-model_posY
error = posY - model_posY
mean_error = np.mean(error)

#3-3. Calculate the average of the error
avg_error = np.mean(error)
print(f"The average error is: {avg_error}")

#3-4. Calculate the standard deviation on the error.
std_error = np.std(error)
print(f"The standard deviation of error is: {std_error}")

#3-5. Plot posY and its model on the same graph
plt.figure()
plt.plot(posX, posY, color='#0b7ef2', label='posY')
plt.plot(posX, model_posY, color='#ed6006', label='model posY')
plt.xlabel('posX')
plt.ylabel('posY and its model')
plt.title('posY and its model as a fonction of posX')
plt.legend(loc='upper left',fontsize=9)
plt.grid(True)
plt.show()

#3-6. Make the derivation (finite differences) of the lists posy and its model model_posy. Compare with the exact solution obtained from the derivation of the equation in test_func. 
def centered_derivative(y, h):
    return np.array([(y[i+1] - y[i-1]) / (2*h) for i in range(1, len(y)-1)])
h = posX[1] - posX[0]

deriv_posY = centered_derivative(posY, h)
deriv_model_posY = centered_derivative(model_posY, h)

plt.figure()
plt.plot(posX[1:-1], deriv_posY, label='Derivative of posY', color='#1255f0')
plt.plot(posX[1:-1], deriv_model_posY, label='Derivative of model of posY', color='#f03412')
plt.xlabel('posX')
plt.ylabel('Derivative')
plt.title('Derivatives of posY and its model')
plt.legend(loc='upper left',fontsize=9)
plt.grid(True)
plt.show()

#3-6. Try different methods (at least centered derivative, eventually forward, backward) comments differences and the error on each method. 
def forward_derivative(y, h):
    return np.array([(y[i+1] - y[i]) / h for i in range(len(y)-1)])
def backward_derivative(y, h):
    return np.array([(y[i] - y[i-1]) / h for i in range(1, len(y))])
forward_deriv_posY = forward_derivative(posY, h)
backward_deriv_posY = backward_derivative(posY, h)

plt.figure()
plt.plot(posX[1:-1], deriv_posY, label='Centered Derivative', color='#1541f6')
plt.plot(posX[:-1], forward_deriv_posY, label='Forward Derivative', color='#dcf529', linestyle='--', linewidth=0.5)
plt.plot(posX[1:], backward_deriv_posY, label='Backward Derivative', color='#7b34e1', linestyle=':', linewidth=0.5)
plt.plot(posX[1:-1], deriv_model_posY, label='Derivative of model of posY', color='#f03412')
plt.xlabel('posX')
plt.ylabel('Derivative')
plt.title('Comparison of Derivatives: Forward, Backward, Centered')
plt.legend()
plt.grid(True)
plt.show()

"""The results are similar across different methods, but when we zoom in (after), we can observe slight shifts.
The centered derivative consistently provides a more accurate approximation compared to the forward and backward methods."""

#3-6. Make a zoom on the one period to see differences.
start = 0
end = 30

plt.figure()
plt.plot(posX[start:end], deriv_posY[start:end], label='Centered Derivative', color='#1541f6')
plt.plot(posX[start:end], deriv_model_posY[start:end], label='Derivative of model of posY', color='#f03412')
plt.plot(posX[start:end], forward_deriv_posY[start:end], label='Forward Derivative',color='#dcf529', linestyle='--')
plt.plot(posX[start:end], backward_deriv_posY[start:end], label='Backward Derivative', color='#7b34e1', linestyle=':')
plt.xlabel('posX')
plt.ylabel('Derivative')
plt.title('Zoom on [0:3]')
plt.legend()
plt.grid(True)
plt.show()

#3-8. Make the integration of the posy and its model. Try different methods (Trapezoidal, Simpson). Print the value of each integration. Comment the differences and the errors.
from scipy.integrate import simps, trapz

integral_trapz_posY = trapz(posY, posX)
print(f"Integration of posY (trapz): {integral_trapz_posY}")
integral_simps_posY = simps(posY, posX)
print(f"Integration of posY (simps): {integral_simps_posY}")
integral_trapz_model = trapz(model_posY, posX)
print(f"Integration of model (trapz): {integral_trapz_model}")
integral_simps_model = simps(model_posY, posX)
print(f"Integration of model (simps): {integral_simps_model}")
error_trapz = np.abs(integral_trapz_posY - integral_trapz_model)
print(f"Difference between posY and model (Trapz): {error_trapz}")
error_simps = np.abs(integral_simps_posY - integral_simps_model)
print(f"Difference between posY and model (Simps): {error_simps}")

"""The trapezoidal rule approximates the integral by dividing the area under a curve into multiple trapezoids.
It provides a linear approximation of the function, which means it is less accurate when the curve is nonlinear.
Simpson's rule is more accurate because it uses parabolas to approximate the curve, it follows more the shape of the function.
This second rule provides a better result because parabolas can more accurately capture changes in the curve’s shape."""

#3-9. Plot sin(x) + sin((10.0 / 3.0) * x) for x in [2, 7]. Find the minimums and the roots of the equation on the same interval.
from scipy.optimize import fmin, brentq

def equation(x):
    return np.sin(x) + np.sin((10.0 / 3.0) * x)

x_vals = np.linspace(2, 7, 500)
y_vals = equation(x_vals)

fig = plt.figure()
ax = fig.add_subplot(1, 1, 1)
ax.plot(x_vals, y_vals,color='#19d55b', label=r'$sin(x) + sin(\frac{10}{3}x)$')
ax.set_xlabel('x')
ax.set_ylabel('y')
ax.set_title(r'Plot of $sin(x) + sin(\frac{10}{3}x)$ in [2, 7]')
ax.legend(loc='upper right',fontsize=9)
plt.grid(True)
plt.show()

min_x1 = fmin(equation, 3)
min_y1 = equation(min_x1)
min_x2 = fmin(equation, 5)
min_y2 = equation(min_x2)
print(f"Minimums found at x={min_x1[0]} with y={min_y1[0]} and at x={min_x2[0]} with y={min_y2[0]}")

roots = []
intervals = np.linspace(2, 7, 10)
for i in range(len(intervals) - 1):
    a = intervals[i]
    b = intervals[i + 1]
    if equation(a) * equation(b) < 0:
        root = brentq(equation, a, b)
        roots.append(root)

print("Roots found in the interval [2, 7] :")
for root in roots:
    print(f"Root at x = {root}")
