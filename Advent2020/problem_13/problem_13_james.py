import os
import sys
import copy

f = open("input_13_james.txt", "r")
input_cast = [line.strip() for line in f.readlines()]
target = int(input_cast[0])
only_integer_times =  [int(time.strip()) for time in input_cast[1].split(',') if time.strip() != 'x']
all_times =  [int(time.strip()) if time.strip() != 'x' else 'x' for time in input_cast[1].split(',')]

def wait_time(target, bus):
    return  bus - (target - (target // bus) * bus)

min_wait_time = None
min_bus = None
for bus in only_integer_times:
    current_wait_time = wait_time(target, bus)
    if min_wait_time == None or current_wait_time < min_wait_time:
        min_wait_time = current_wait_time
        min_bus = bus
print("Solution to part A: ", min_wait_time*min_bus)

modulo_list = []
for i in range(len(all_times)):
    if all_times[i] != 'x':
        modulo_list.append([all_times[i], i % all_times[i]])
modulo_list.sort(key=lambda x: x[1])

current = modulo_list[0][0] + modulo_list[0][1]
multiplier = modulo_list[0][0]
locked_in = 1
while locked_in < len(modulo_list):
    while wait_time(current, modulo_list[locked_in][0]) != modulo_list[locked_in][1]:
        current += multiplier
    multiplier *= modulo_list[locked_in][0]
    locked_in += 1
print("Solution to part B: ", current)