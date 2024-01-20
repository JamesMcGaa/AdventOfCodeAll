import os

f = open("input11.txt", "r")
input_cast = [[int(c) for c in line.strip()] for line in  f.readlines()]



def step(grid):
    firing_queue = []
    fired_set = set()
    shadow = [[0 for _ in range(len(grid[0]))] for _ in range(len(grid))]

    for x in range(len(grid)):
        for y in range(len(grid[x])):
            shadow[x][y] = grid[x][y] + 1
            if shadow[x][y] == 10:
                fired_set.add((x,y))
                firing_queue.append((x,y))
    
    while len(firing_queue) > 0:
        x, y = firing_queue.pop()
        for X_offset in [-1,0,1]:
            for y_offset in [-1,0,1]:
                if not (X_offset == 0 and y_offset == 0) \
                    and x + X_offset >= 0 and x + X_offset < len(grid) \
                    and y + y_offset >= 0 and y + y_offset < len(grid[x+X_offset]):
                    if shadow[x+X_offset][y+y_offset] == 9 and (x+X_offset, y+y_offset) not in fired_set:
                        firing_queue.append((x+X_offset, y+y_offset))
                        fired_set.add((x+X_offset, y+y_offset))
                    shadow[x+X_offset][y+y_offset] = min(10, shadow[x+X_offset][y+y_offset] + 1)
    
    for x in range(len(grid)):
        for y in range(len(grid[x])):
            if shadow[x][y] == 10:
                shadow[x][y] = 0
    
    return shadow, len(fired_set)


outer_counter = 0
for i in range(100):
    input_cast, counter = step(input_cast)
    outer_counter += counter
print(outer_counter)
    
step_counter = 0
while True:
    step_counter += 1
    input_cast, counter = step(input_cast)
    if counter == len(input_cast) * len(input_cast[0]):
        break
print(step_counter)
