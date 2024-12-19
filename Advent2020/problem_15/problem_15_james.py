import os
import sys
import copy

starts = [
    # [0,3,6],
    # [1,3,2],
    # [2,1,3],
    # [1,2,3],
    # [2,3,1],
    # [3,2,1],
    # [3,1,2],
    [7,14,0,17,11,1,2],
]

def solve(start, end_index):
    memory ={}
    for i, n in enumerate(start):
        memory[n] = i+1
    index = len(start)+1
    last_move = 0
    while index < end_index: #final value of last_move will occur at end_index
        if last_move not in memory:
            memory[last_move] = index
            last_move = 0
        else:
            new_last_move = index - memory[last_move]
            memory[last_move] = index
            last_move = new_last_move
        index += 1
    return last_move

for start in starts:
    print("Solution to part A: ", solve(start, 2020))
for start in starts:
    print("Solution to part B: ", solve(start, 30000000))

