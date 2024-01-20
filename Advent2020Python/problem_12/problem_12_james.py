import os
import sys
import copy

f = open("input_12_james.txt", "r")
input_cast = [line.strip() for line in f.readlines()]

angle = 0
N = 0
E = 0
for command in input_cast:
    command_type = command[0]
    command_num = int(command[1:])
    if command_type == 'N':
        N += command_num
    if command_type == 'S':
        N -= command_num
    if command_type == 'E':
        E += command_num
    if command_type == 'W':
        E -= command_num

    if command_type == 'L':
        angle = (angle + command_num) % 360
    if command_type == 'R':
        angle = (angle - command_num) % 360

    if command_type == 'F':
        if angle == 0:
            E += command_num
        if angle == 90:
            N += command_num
        if angle == 180:
            E -= command_num
        if angle == 270:
            N -= command_num
print("Solution to part A", abs(N) + abs(E))
    

absolute_N = 0 # absolute ship positions
absolute_E = 0
waypoint_N = 1 # relative waypoint positions
waypoint_E = 10
for command in input_cast:
    command_type = command[0]
    command_num = int(command[1:])

    if command_type == 'R': #normalize angle
        command_num = 360 - command_num

    if command_type == 'N':
        waypoint_N += command_num
    if command_type == 'S':
        waypoint_N -= command_num
    if command_type == 'E':
        waypoint_E += command_num
    if command_type == 'W':
        waypoint_E -= command_num

    if command_type == 'L' or command_type == 'R': # standard rotation rules
        if command_num == 90:
            new_waypoint_N = waypoint_E
            new_waypoint_E = -waypoint_N
        if command_num == 180:
            new_waypoint_N = -waypoint_N
            new_waypoint_E = -waypoint_E
        if command_num == 270:
           new_waypoint_N = -waypoint_E
           new_waypoint_E = waypoint_N
        waypoint_E = new_waypoint_E
        waypoint_N = new_waypoint_N

    if command_type == 'F':
        absolute_N += waypoint_N * command_num
        absolute_E += waypoint_E * command_num

print("Solution to part B: ", abs(absolute_N) + abs(absolute_E))