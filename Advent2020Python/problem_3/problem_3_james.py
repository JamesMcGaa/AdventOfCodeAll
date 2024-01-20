import os 
f = open("input_3_james.txt", "r")
input_cast = f.readlines()

def crash_at_height(height):
    line = input_cast[height]
    bad_modulos = set()
    for i in range(len(line)):
        if line[i] == "#":
            bad_modulos.add(i)
    horizontal_offset = 3*height
    return horizontal_offset % (len(line) - 1) in bad_modulos #Account for newline

def driver_a():
    counter = 0
    for height in range(len(input_cast)):
        if crash_at_height(height):
            counter += 1
    print("Part A Solution: ", counter)


def crash_at_height_b(height, right):
    line = input_cast[height]
    bad_modulos = set()
    for i in range(len(line)):
        if line[i] == "#":
            bad_modulos.add(i)
    return right % (len(line) - 1) in bad_modulos #Account for newline

def driver_b():
    global_counter = 1
    for tup in [(1,1), (3,1), (5,1), (7,1), (1,2), ]:
        height, right = 0, 0
        counter = 0
        while height < len(input_cast):
            if crash_at_height_b(height, right):
                counter += 1
            right += int(tup[0])
            height += int(tup[1])
        global_counter *= counter
    print("Part B Solution: ", global_counter)
        
driver_a()  
driver_b()
