

class Cube:
    def __init__(self, bounds, sign):
        self.sign = sign
        self.bounds = bounds
    
    def intersection(self, other_cube):
        new_bounds = []
        for axis in range(3): 
            largest_min = max(self.bounds[axis][0], other_cube.bounds[axis][0])
            smallest_max = min(self.bounds[axis][1], other_cube.bounds[axis][1])
            if largest_min > smallest_max:
                return None
            new_bounds.append([largest_min, smallest_max])
        return new_bounds
    
    def signed_volume(self):
        vol = 1
        for bound in self.bounds:
            vol *= bound[1] - bound[0] + 1
        vol *= self.sign
        return vol



def runline(line, cubes):
    operation, remainder = line.strip().split()
    sections = remainder.split(",")
    bounds = [[int(num) for num in section.split("=")[1].split("..")] for section in sections]
    sign = 1 if operation == "on" else 0
    new_cube = Cube(bounds, sign)
    intersections = []
    for other_cube in cubes:
        intersection = new_cube.intersection(other_cube)
        if intersection is not None and other_cube.sign != 0:
            intersections.append(Cube(intersection, other_cube.sign * -1))
    if new_cube.sign != 0:
        intersections.append(new_cube)
    cubes.extend(intersections)
    return sum([cube.signed_volume() for cube in cubes])

cubes = []
su = 0
f = open("input22_test.txt", "r")
for line in f.readlines():
    su = runline(line, cubes)
print(su)
print(su - runline("off x=-50..50,y=-50..50,z=-50..50", cubes))