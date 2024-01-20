import os
import sys
import copy


def safe_index(generation, r, c, offset_r, offset_c, projection):
    while True:
        if r >= 0 and r < len(generation) and c >= 0 and c < len(generation[r]):
            if projection and generation[r][c] == ".":
                r += offset_r
                c += offset_c
            else:
                return generation[r][c]
        else:
            return None


def advance(generation, projection, dense_threshold):
    new_generation = copy.deepcopy(generation)
    for r in range(len(generation)):
        for c in range(len(generation[r])):
            occupied_count = 0
            for r_offset in range(-1, 2):
                for c_offset in range(-1, 2):
                    if not (r_offset == 0 and c_offset == 0):
                        val = safe_index(
                            generation,
                            r + r_offset,
                            c + c_offset,
                            r_offset,
                            c_offset,
                            projection,
                        )
                        if val == "#":
                            occupied_count += 1

            if generation[r][c] == "L" and occupied_count == 0:
                new_generation[r][c] = "#"

            elif generation[r][c] == "#" and occupied_count >= dense_threshold:
                new_generation[r][c] = "L"

            else:
                new_generation[r][c] = generation[r][c]

    return new_generation


f = open("input_11_james.txt", "r")
input_cast = [[c for c in line.strip()] for line in f.readlines()]
while advance(input_cast, False, 4) != input_cast:
    input_cast = advance(input_cast, False, 4)
count = 0
for r in range(len(input_cast)):
    for c in range(len(input_cast[r])):
        if input_cast[r][c] == "#":
            count += 1
print("Solution to part A", count)

f = open("input_11_james.txt", "r")
input_cast = [[c for c in line.strip()] for line in f.readlines()]
while advance(input_cast, True, 5) != input_cast:
    input_cast = advance(input_cast, True, 5)

count = 0
for r in range(len(input_cast)):
    for c in range(len(input_cast[r])):
        if input_cast[r][c] == "#":
            count += 1
print("Solution to part B", count)
