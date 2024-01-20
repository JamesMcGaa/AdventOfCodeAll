import os

f = open("input12.txt", "r")
input_cast = [line.strip().split("-") for line in  f.readlines()]
adjacency_matrix = {}

for line in input_cast:
    if line[0] not in adjacency_matrix:
        adjacency_matrix[line[0]] = set()
    if line[1] not in adjacency_matrix:
        adjacency_matrix[line[1]] = set()
    adjacency_matrix[line[0]].add(line[1])
    adjacency_matrix[line[1]].add(line[0])

counter = 0
stack = [("start", False)]
while len(stack) > 0:
    current, double_used = stack.pop()
    vals = current.split(',')
    most_recent = vals[-1]
    for adj in adjacency_matrix[most_recent]:
        if adj == "end":
            counter += 1

        elif adj == adj.lower() and adj in vals:
            if adj == "start" or double_used:
                pass
            else:
                stack.append((current+","+adj, True))

        # elif adj == adj.lower() and adj in vals:
        #     pass

        else: 
            stack.append((current+","+adj, double_used))
print(counter)



