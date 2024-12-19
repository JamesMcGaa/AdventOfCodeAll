import os 
import sys
import copy
f = open("input_8_james.txt", "r")
input_cast = [line.strip() for line in f.readlines()]

raw_ops = []
for line in input_cast:
    spl = line.split()
    op = spl[0]
    sign = spl[1][0]
    num = int(spl[1][1:])
    if sign == "-":
        num *= -1
    raw_ops.append([op, num, False])

def execute_operations(ops):
    pc = 0
    aggregator = 0
    while pc < len(ops) and not ops[pc][2]:
        ops[pc][2] = True #visited
        if ops[pc][0] == "jmp":
            pc += ops[pc][1]
        elif ops[pc][0] == "acc":
            aggregator += ops[pc][1]
            pc += 1
        else: # ops[pc][0] == "nop"
            pc += 1

    if pc >= len(ops): #terminates 
        return (True, aggregator)
    else: #loops
        return (False, aggregator)

print("Solution for part A: ", execute_operations(copy.deepcopy(raw_ops))[1])

for i in range(len(raw_ops)):
    copy_ops = copy.deepcopy(raw_ops)
    if copy_ops[i][0] == "nop":
        copy_ops[i][0] = "jmp"
    elif copy_ops[i][0] == "jmp":
        copy_ops[i][0] = "nop"
    
    if copy_ops[i][0] == "nop" or copy_ops[i][0] == "jmp":
        status, aggregator = execute_operations(copy_ops)
        if status:
            print("Solution for part B: ", aggregator)
            exit()

 
