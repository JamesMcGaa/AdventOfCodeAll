import os 
f = open("input_2_james.txt", "r")
input_cast = [line.split() for line in  f.readlines()]

def valid(line):
    lower = int(line[0].split('-')[0])
    upper = int(line[0].split('-')[1])
    needle = line[1][0]
    haystack = line[2]
    counter = 0 
    for c in haystack:
        if c == needle:
            counter+= 1
            if counter > upper:
                return 0 
    if counter >= lower:
        return 1
    return 0 

def valid_b(line):
    lower = int(line[0].split('-')[0]) - 1
    upper = int(line[0].split('-')[1]) - 1
    needle = line[1][0]
    haystack = line[2]
    if (haystack[lower] == needle or haystack[upper] == needle) and not (haystack[lower] == needle and haystack[upper] == needle):
        return 1
    return 0

global_counter = 0
for line in input_cast:
    global_counter += valid(line)
print("Part a solution: ", global_counter)

global_counter = 0
for line in input_cast:
    global_counter += valid_b(line)
print("Part b solution: ", global_counter)