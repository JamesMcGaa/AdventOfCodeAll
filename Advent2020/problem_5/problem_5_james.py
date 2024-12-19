import os 
f = open("input_5_james.txt", "r")
input_cast = [line.strip() for line in f.readlines()]


def index(line):
    upper_height = 127
    lower_height = 0
    upper_width = 7
    lower_width = 0
    for c in line:
        if c == "F":
            upper_height = (lower_height + upper_height) // 2 
        if c == "B":
            lower_height = (lower_height + upper_height) // 2 + 1

        if c == "L":
            upper_width = (lower_width + upper_width) // 2 
        if c == "R":
            lower_width = (lower_width + upper_width) // 2 + 1
    
    return lower_height * 8 + lower_width

maximum = 0
minimum = 100000000
exists = set()
for line in input_cast:
    exists.add(index(line))
    if index(line) > maximum:
        maximum = index(line)
    if index(line) < minimum:
        minimum = index(line)
print("Solution to part A", maximum)

for i in range(minimum, maximum):
    if i not in exists and i+1 in exists and i-1 in exists:
        print("Solution to part B", i)
