import os
import sys
import copy
import re

def parse_input():
    f = open("input_17_james.txt", "r")
    input_cast = [line.strip() for line in f.readlines()]
    active = set()
    for x, line in enumerate(input_cast):
        for y, value in enumerate(line):
            if value == "#":
                active.add((x, y, 0, 0))
    return active


def get_neighbors(triplet, dimension, include_self):
    neighbors = []
    for x in range(triplet[0] - 1, triplet[0] + 2):
        for y in range(triplet[1] - 1, triplet[1] + 2):
            for z in range(triplet[2] - 1, triplet[2] + 2):
                if dimension == 3:
                    if include_self or not (
                        x == triplet[0] and y == triplet[1] and z == triplet[2]
                    ):
                        neighbors.append((x, y, z, 0))
                elif dimension == 4:
                    for w in range(triplet[3] - 1, triplet[3] + 2):
                        if include_self or not (
                            x == triplet[0]
                            and y == triplet[1]
                            and z == triplet[2]
                            and w == triplet[3]
                        ):
                            neighbors.append((x, y, z, w))
    return neighbors


def count_neighbors(triplet, active, dimension):
    count = 0
    for neighbor in get_neighbors(
        triplet,
        dimension,
        include_self=False,
    ):
        if neighbor in active:
            count += 1
    return count


def cycle(active, dimension, cycles):
    for _ in range(cycles):
        new_active = set()
        for coord in active:
            for neighbor in get_neighbors(
                coord,
                dimension,
                include_self=True,
            ):
                if neighbor in active:
                    if (
                        count_neighbors(neighbor, active, dimension) == 2
                        or count_neighbors(neighbor, active, dimension) == 3
                    ):
                        new_active.add(neighbor)
                else:
                    if count_neighbors(neighbor, active, dimension) == 3:
                        new_active.add(neighbor)
        active = new_active
    return active


print("Solution to part A: ", len(cycle(parse_input(), dimension=3, cycles=6)))
print("Solution to part B: ", len(cycle(parse_input(), dimension=4, cycles=6)))