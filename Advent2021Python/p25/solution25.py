f = open("input25.txt", "r")
grid = []
for line in f.readlines():
    row = []
    for ch in line.strip():
        row.append(ch)
    grid.append(row)

X = len(grid)
Y = len(grid[0])

def step_right(grid):
    new_grid = [["." for _ in range(Y)] for __ in range(X)]
    for x in range(X):
        for y in range(Y):
            if safe(grid, x, y - 1) == ">" and grid[x][y] == ".": #another one moves into empty
                new_grid[x][y] = ">"
            elif grid[x][y] == ">" and safe(grid, x, y + 1) == ".": #current one moved out 
                new_grid[x][y] = "."
            else: 
                new_grid[x][y] = grid[x][y]
            
    return new_grid

def step_down(grid):
    new_grid = [["." for _ in range(Y)] for __ in range(X)]
    for x in range(X):
        for y in range(Y):
            if safe(grid, x - 1, y) == "v" and grid[x][y] == ".": #another one moves into empty
                new_grid[x][y] = "v"
            elif grid[x][y] == "v" and safe(grid, x + 1, y) == ".": #current one moved out 
                new_grid[x][y] = "."
            else: 
                new_grid[x][y] = grid[x][y]
            
    return new_grid

def step(grid):
    grid = step_right(grid)
    # print(grid_to_string(grid))
    grid = step_down(grid)
    return grid

def safe_set(grid, x, y, char):
    if x < 0:
        x = X - 1
    if y < 0:
        y = Y - 1
    if x >= X:
        x = 0
    if y >= Y:
        y = 0
    grid[x][y] = char

def safe(grid, x, y):
    if x < 0:
        x = X - 1
    if y < 0:
        y = Y - 1
    if x >= X:
        x = 0
    if y >= Y:
        y = 0
    return grid[x][y]

def grid_to_string(grid):
    return "\n".join(["".join(row) for row in grid]) + "\n"

# print(grid_to_string(grid))
counter = 0
while True:
    counter += 1
    original_trace = grid_to_string(grid)
    grid = step(grid)
    if original_trace == grid_to_string(grid):
        break
print(counter)
