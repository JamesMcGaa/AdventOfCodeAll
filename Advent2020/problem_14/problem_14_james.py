import os
import sys
import copy
import re
f = open("input_14_james.txt", "r")
input_cast = [line.strip() for line in f.readlines()]

def to_binary_string(i):
    return '{0:036b}'.format(i)

def to_integer(b):
    return int(b, 2)

def apply_mask(mask, binary_string):
    ret = ""
    for i in range(len(binary_string)):
        if mask[i] != "X":
            ret += mask[i]
        else:
            ret += binary_string[i]
    return ret

memory = {}
mask = None
for line in input_cast:
    args = line.split("=")
    if args[0].strip() == "mask":
        mask = args[1].strip()
    else:
        index = int(re.sub("[^0-9]", "", args[0].strip()))
        val = int(args[1].strip())
        binary_val = to_binary_string(val)
        masked = apply_mask(mask, binary_val)
        converted = to_integer(masked)
        memory[index] = converted

counter = 0
for key in memory:
    counter += memory[key]
print("Solution for part A: ", counter)


def apply_mask_b(mask, binary_string):
    ret = ""
    for i in range(len(binary_string)):
        if mask[i] == "0":
            ret += binary_string[i]
        else:
            ret += mask[i]
    return ret

def mark(index, memory, binary_index, val):
    if index == len(binary_index):
        converted_index = to_integer(binary_index)
        memory[converted_index] = val
    elif binary_index[index] == "X":
        new_a = ""
        new_b = ""
        for i in range(len(binary_index)):
            if i == index:
                new_a += '0'
                new_b += '1'
            else:
                new_a += binary_index[i]
                new_b +=  binary_index[i]
        mark(index + 1, memory, new_a, val)
        mark(index + 1, memory, new_b, val)
    else: 
        mark(index + 1, memory, binary_index, val)

memory = {}
mask = None
for line in input_cast:
    args = line.split("=")
    if args[0].strip() == "mask":
        mask = args[1].strip()
    else:
        index = int(re.sub("[^0-9]", "", args[0].strip()))
        val = int(args[1].strip())
        binary_index = to_binary_string(index)
        masked = apply_mask_b(mask, binary_index)
        mark(0, memory, masked, val) 
        
counter = 0
for key in memory:
    counter += memory[key]
print("Solution for part B: ",counter)
