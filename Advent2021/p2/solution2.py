import os 
f = open("input2.txt", "r")
input_cast = [line.split() for line in  f.readlines()]
print(input_cast)

depth = 0
distance = 0
for command in input_cast:
    if command[0] == "down":
        depth += int(command[1])
    elif command[0] == "up":
        depth -= int(command[1])
    else:
        distance += int(command[1])
print(depth * distance)

depth = 0
distance = 0
aim = 0
for command in input_cast:
    if command[0] == "down":
        aim += int(command[1])
    elif command[0] == "up":
        aim -= int(command[1])
    else:
        distance += int(command[1])
        depth +=  int(command[1]) * aim
print(depth * distance)