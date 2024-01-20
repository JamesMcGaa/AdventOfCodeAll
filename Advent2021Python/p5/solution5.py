import os 
import numpy as np

f = open("input5.txt", "r")
input_cast = [line.split("->") for line in  f.readlines()]

global_counts = {}
lines = []

for line in input_cast:
    a, b = line[0].split(",")
    c, d = line[1].split(",")
    a = int(a)
    b = int(b)
    c = int(c)
    d = int(d)

    # if a == c:
    #     for i in range(min(b,d), max(b,d)+1):
    #         if (a, i) not in global_counts:
    #             global_counts[(a, i)] = 1
    #         else:
    #             global_counts[(a, i)] += 1


    # if b == d:
    #     for i in range(min(a,c), max(a,c)+1):
    #         if (i, b) not in global_counts:
    #             global_counts[(i, b)] = 1
    #         else:
    #             global_counts[(i, b)] += 1
    
    magnitude = max(abs(c-a), abs(d-b))
    interpolated = np.rint(np.linspace(np.array([a,b]), np.array([c,d]), magnitude + 1)).astype(int)
    for point in interpolated:
        scalar_coords = (point.item(0), point.item(1))
        if scalar_coords not in global_counts:
            global_counts[scalar_coords] = 1
        else:
            global_counts[scalar_coords] += 1
    
counter = 0 
for key, value in global_counts.items():
    if value > 1:
        counter += 1 
print(counter)


