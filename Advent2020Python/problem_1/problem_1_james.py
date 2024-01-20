import os 
f = open("input_1_james.txt", "r")
input_cast = [int(line) for line in  f.readlines()]

def two_sum(input_set, desired_sum):
    for first in input_set: 
        second = desired_sum-first
        if second in input_set:
            return first * second
    return None

def three_sum(input_list, desired_sum_initial):
    for i in range(len(input_cast)): 
        first = input_cast[i]
        remainder = set(input_cast[i+1:])
        desired_sum = desired_sum_initial - first
        ret = two_sum(remainder, desired_sum)
        if ret is not None:
            return ret * first
    return None

print("1a solution: ", two_sum(set(input_cast), 2020))
print("1b solution: ", three_sum(input_cast, 2020))