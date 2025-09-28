import numpy as np
import matplotlib.pyplot as plt

# Define the function for the series t+1 = t / (1 + B * t)
def series(t, B):
    return t / (1 + B * t)

def series2(t, B):
    return B * t


def parabolique(t,it):
    alpha = 1 / np.log(1 + it)  
    T_next = alpha * t 

    return T_next


# Define the range for Beta
betas = np.linspace(0.001, 0.006, 6)

# Define the range for t (initial conditions)
t_values = np.linspace(0, 540, 540)
print(t_values)
# Create a plot
plt.figure(figsize=(10, 6))

# Plot the series for different Beta values
for B in betas:
    t_series = [0.001]#[t_values[0]]  # Initialize the series with the first value
    t_normalized = [150]
    for t in range(1, len(t_values)):
        t_next = series(t_series[-1], B)
        t_series.append(t_next)
        
    plt.plot(t_values, t_series, label=f"Beta = {B:.3f}")
    
t_series = [150]#[t_values[0]]  # Initialize the series with the first value
B = 0.99
for t in range(1, len(t_values)):
    t_next = series2(t_series[-1], B)
    t_series.append(t_next)
#plt.plot(t_values, t_series, label=f"Beta = {B:.3f}")


t_series = [0.63]#[t_values[0]]  # Initialize the series with the first value
for t in range(2, len(t_values)+1):
    t_next = parabolique(t_series[-1], t)
    t_series.append(t_next)
print(t_series)
value = np.count_nonzero(t_series)
print("VALUE : ",value," diffrence : ",len(t_values) - value )
#plt.plot(t_values, t_series, label=f"Beta = {B:.3f}")


# Adding labels and title
plt.title("Evolution of the Series for Different Beta Values")
plt.xlabel("Time Steps")
plt.ylabel("t+1")
plt.legend()

# Show the plot
plt.grid(True)
plt.show()
