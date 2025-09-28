# -*- coding: utf-8 -*-
"""
Created on Fri Oct  4 13:25:54 2024

@author: Juliette BOURGEOIS
"""

###The goal of the exercise is to learn the basic numerical tools for data analysis with python. After reading the scipy lecture you must do this work alone. If I find similar files, points will be removed. Once done, please upload your python script with all the input and output files on the depot box below before the deadline of December 1st. Please upload an example of the output of your script in a separate file (txt or odt), in case my version of python could not run your script.
#First, you have to download the file data.txt before writing a python script. All the action must be done with a small python script.

#1 - Manipulating numerical data, read and write data to a file,
#Use a python program to read the joined file 'data.txt' as two numpy list named posX and posY.
#Print posX, print posY
#Find and print the maximum of posY, find the index of the maximum value of posY.
#Sort posY by ascending value, and write the sorted values in a file.
#2 - Plotting
#Plot posY as a function of posX, add a legend, units, title, axis label.... nice color, or mark...
#Plot on the same graph the sorted values of posY as a function of Posx, with a different color. Add the name of the new curve in the legend.
#Plot both curves on two separate graphs using subplots command.
#3 - Scientific computing
#Fit posY as a function of posX with the equation posY= a*np.sin(b*posX+c)+d where a, b, c, d are parameters to determine thanks to a curve fitting. To have an idea of the initial guess for some of the parameters: find the average value and the median value of Posy, the max, and min of Posy, and be lucky. Print the parameters.
#Build the list model_posY based on the test_func and the fitted parameters for posX. Calculate the error between posY and its model at each point: posY-model_posY
#Calculate the average of the error
#Calculate the standard deviation on the error.
#plot posY and its model on the same graph
#(option: only for advanced students :), but I recommend that you try it because derivatives are very often used to analyze data ) Make the derivation (finite differences) of the lists posy and its model model_posy. Compare with the exact solution obtained from the derivation of the equation in test_func. Try different methods (at least centered derivative, eventually forward, backward) comments differences and the error on each method. Make a zoom on the one period to see differences.
#Have a look at these sites to help you to build a python function 'centered_derivatied': def centered_derivative(y,h):...
#https://pythonnumericalmethods.berkeley.edu/notebooks/chapter20.02-Finite-Difference-Approximating-Derivatives.html
#https://numpy.org/doc/stable/reference/generated/numpy.diff.html

#8. Make the integration of the posy and its model. Try different methods ( Trapezoidal, Simpson). Print the value of each integration. Comment the differences and the errors.
#9. Plot sin(x) + sin((10.0 / 3.0) * x) for x in [2, 7]. Find the minimums amd the roots of the equation on the same interval.###


import os

file_path = os.path.abspath(__file__)
directory_path = os.path.dirname(file_path)

print("File Path:", file_path)
print("Directory Path:", directory_path)


import numpy as np

#url = https://C:/Users/Juliette/Documents/Scolaire/Université/Poitiers/Python/DM- Python.py
data = np.loadtxt('data.txt', delimiter=',')
posX, posY = data[:, 0], data[:, 1]

# 1.2 Print posX and posY
print("posX:", posX)
print("posY:", posY)

# 1.3 Find and print the maximum of posY and its index
max_posY = np.max(posY)
max_index = np.argmax(posY)
print("Maximum value of posY:", max_posY)
print("Index of maximum value:", max_index)

# 1.4 Sort posY in ascending order and write to a file
sorted_posY = np.sort(posY)
np.savetxt('sorted_posY.txt', sorted_posY)



