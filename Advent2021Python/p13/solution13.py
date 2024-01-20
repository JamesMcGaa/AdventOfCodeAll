import os

f = open("input13.txt", "r")
instructions = []
coords = set()
for line in  f.readlines():
    if "=" in line:
        wei = line.split("=")
        axis = wei[0][-1]
        num = int(wei[1].strip())
        instructions.append((axis, num))
    elif line.strip() != "":
        wei = line.split(",")
        x = int(wei[0])
        y = int(wei[1].strip())
        coords.add((x,y))



for instruction in instructions:
    new_coords = set()
    axis, num = instruction

    if axis == "x":
        for coord in coords:
            x, y = coord
            if x > num:
                new_coords.add((2 * num - x, y))
            else: 
                new_coords.add(coord)
    
    elif axis == "y":
        for coord in coords:
            x, y = coord
            if y > num:
                new_coords.add((x, 2 * num - y))
            else: 
                new_coords.add(coord)
    print(len(new_coords))
    coords = new_coords
print(coords)

for y in range(100):
    print(" ".join(["*" if (c, y) in coords else " " for c in range(100)]))
    