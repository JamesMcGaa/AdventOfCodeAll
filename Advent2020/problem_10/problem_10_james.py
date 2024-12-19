import os
import sys
import copy

f = open("input_10_james.txt", "r")
input_cast = [int(line.strip()) for line in f.readlines()]
input_cast.append(0)
input_cast.sort()
input_cast.append(input_cast[len(input_cast) - 1] + 3)
input_cast.sort()

three_count = 0
one_count = 0
for i in range(len(input_cast) - 1):
    if input_cast[i+1] - input_cast[i] == 3:
        three_count += 1
    if input_cast[i+1] - input_cast[i] == 1:
        one_count += 1
print("Solution to part A", three_count * one_count)

dp = {0: 1}
for i in range(1, len(input_cast)):
    val = input_cast[i]
    dp[val] = dp.get(val-1, 0) + dp.get(val-2, 0) + dp.get(val-3, 0)
print("Solution to part B", dp[input_cast[len(input_cast) - 1]])
