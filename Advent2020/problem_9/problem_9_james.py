import os
import sys
import copy

f = open("input_9_james.txt", "r")
input_cast = [int(line.strip()) for line in f.readlines()]

def two_sum(arr, target):
    nums = set(arr)
    for num in arr:
        if target - num in nums:
            return True
    return False


inconsistency = None
for i in range(25, len(input_cast)):
    prev = input_cast[i - 25 : i]
    if not two_sum(prev, input_cast[i]):
        inconsistency = input_cast[i]
        print("Solution for part A: ", inconsistency)

low_pointer = 0
hi_pointer = 1
while sum(input_cast[low_pointer : hi_pointer + 1]) != inconsistency:  # sliding window
    if sum(input_cast[low_pointer : hi_pointer + 1]) > inconsistency:
        low_pointer += 1
    else:
        hi_pointer += 1
print(
    "Solution for part B: ",
    min(input_cast[low_pointer : hi_pointer + 1])
    + max(input_cast[low_pointer : hi_pointer + 1]),
)
