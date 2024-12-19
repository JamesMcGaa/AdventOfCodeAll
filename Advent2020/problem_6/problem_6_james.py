import os 
f = open("input_6_james.txt", "r")
input_cast = [line.strip() for line in f.readlines()]
groups = []
current = []
for line in input_cast:
    if line == "":
        groups.append(current)
        current = []
    else:
        current.append(line)
groups.append(current)


counter = 0
for group in groups:
    group_set = set()
    for person_response in group:
        for question in person_response:
            group_set.add(question)
    counter += len(group_set)
print("Solution to part A: ", counter)



counter = 0
for group in groups:
    group_set = None
    for person_response in group:
        person_set = set()
        for question in person_response:
            person_set.add(question)
        
        if group_set == None:
            group_set = person_set
        else:
            group_set = group_set & person_set
    counter += len(group_set)
print("Solution to part B: ", counter)