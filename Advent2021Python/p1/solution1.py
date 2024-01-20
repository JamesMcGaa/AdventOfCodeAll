import os 
f = open("input1.txt", "r")
input_cast = [int(line) for line in  f.readlines()]

print(input_cast)

counter = 0 
for i in range(1, len(input_cast)):
    if input_cast[i] - input_cast[i-1] > 0:
        counter += 1
print(counter)

counter_b = 0
for j in range(3, len(input_cast)):
    if sum(input_cast[j-2:j+1]) - sum(input_cast[j-3:j]) > 0:
        counter_b += 1
print(counter_b)
