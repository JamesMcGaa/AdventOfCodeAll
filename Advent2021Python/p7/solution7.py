import os 
f = open("input7.txt", "r")
input_cast = [int(line) for line in  f.readline().split(",")]

print(input_cast)

def cost(val, input_cast):
    counter = 0
    for crab in input_cast:
        x = abs(val - crab)
        # counter += x
        counter += x * (x+1) / 2
    return counter

low = min(input_cast)
hi = max(input_cast)

print(min([cost(val, input_cast) for val in range(low, hi+1)]))

