f = open("input20_test.txt", "r")
lines = [line.strip() for line in f.readlines()]

formula = lines[0]
coords = {}
for x_idx, line in enumerate(lines[2:]):
    for y_idx, char in enumerate(line):
        coords[(x_idx, y_idx)] = 1 if char == "#" else 0

global outer_parity
outer_parity = 0

def lookup_value(coord):
    binary_str = ""
    for x in [-1, 0, 1]:
        for y in [-1, 0, 1]:
            modified_coord = (coord[0] + x, coord[1] + y)
            if modified_coord in coords:
                binary_str += str(coords[modified_coord])
            else:
                binary_str += str(outer_parity)
    idx = int(binary_str, 2)
    new_char = 1 if formula[idx] == "#" else 0
    return new_char 



min_x = min([coord[0] for coord in coords]) - 1
max_x = max([coord[0] for coord in coords]) + 1
min_y = min([coord[1] for coord in coords]) - 1
max_y = max([coord[1] for coord in coords]) + 1

for i in range(50):
    print(i)
    new_coords = {}
    for x in range(min_x, max_x + 1):
        for y in range(min_y, max_y + 1):
            new_coords[(x, y)] = lookup_value((x, y))
    coords = new_coords
    min_x -= 1
    min_y -= 1
    max_x += 1
    max_y += 1
    # outer_parity = (outer_parity + 1) % 2

print(sum(coords.values()))