import os 
import numpy as np

f = open("input9.txt", "r")
grid = [[int(digit) for digit in line.strip()] for line in  f.readlines()]


def is_low(x, y, grid):
    val = grid[x][y]

    for x_offset in [-1, 1]:
        if 0 <= x + x_offset and x + x_offset < len(grid) and grid[x + x_offset][y] <= val:
            return False

    for y_offset in [-1, 1]:
        if 0 <= y + y_offset and y + y_offset < len(grid[x]) and grid[x][y + y_offset] <= val:
            return False
    
    return True

counter = 0 
for x in range(len(grid)):
    for y in range(len(grid[0])):
        if is_low(x, y, grid):
            counter += grid[x][y] + 1

print(counter)


#b
def bfs(x, y, grid, connected_component_sizes, seen):
    stack = [(x,y)]
    pre_size = len(seen)
    while len(stack) > 0:
        current = stack.pop()
        i,j = current
        seen.add(current)
        for x_offset in [-1, 1]:
            if (i + x_offset, j) not in seen and \
                0 <= i + x_offset and i + x_offset < len(grid) and \
                grid[i + x_offset][j] >= grid[i][j] and \
                grid[i + x_offset][j] != 9:

                stack.append((i + x_offset, j))

        for y_offset in [-1, 1]:
            if (i, j + y_offset) not in seen and \
                0 <= j + y_offset and j + y_offset < len(grid[i]) and \
                grid[i][j + y_offset] >= grid[i][j]  and \
                grid[i][j + y_offset] != 9:

                stack.append((i, j + y_offset))

    connected_component_sizes.append(len(seen) - pre_size)
    


connected_component_sizes = []
seen = set()
for x in range(len(grid)):
    for y in range(len(grid[0])):
        if not (x,y) in seen and is_low(x, y, grid):
            bfs(x, y, grid, connected_component_sizes, seen)

print(np.prod(list(sorted(connected_component_sizes, reverse=True))[:3]))