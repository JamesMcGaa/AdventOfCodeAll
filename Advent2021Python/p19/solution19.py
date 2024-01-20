import numpy as np 
from itertools import permutations

# Generate Valid Rotation Matrices
base_permutation_matrix = np.array([[ 1, 0, 0],
                                    [ 0, 1, 0],
                                    [ 0, 0, 1]])
all_perms = [np.array(perm) for perm in permutations(base_permutation_matrix)]
valid_perms = []
for permutation in all_perms:
    for x in [-1, 1]:
        for y in [-1, 1]:
            for z in [-1, 1]:
                perm_copy = permutation.copy()
                perm_copy[0,:] *= x
                perm_copy[1,:] *= y
                perm_copy[2,:] *= z
                if int(np.linalg.det(perm_copy)) == 1:
                    valid_perms.append(perm_copy)

# Create a set of Scan-0-oriented points
absolute_points = set() 
def absolute_point_to_pairwise_dists_with_other_abs(abs):
    return set([np.sum((np.array(abs) - np.array(other_abs))**2) for other_abs in absolute_points])

class Scan:
    def __init__(self, data):
        self.data = data # list of lists
        self.perms = self.list_full_permutations() # list of numpy arrays (representing rotated point matrix)
    
    def list_full_permutations(self):
        return [np.matmul(self.data, perm) for perm in valid_perms]

# Read files and setup scans and absolute_points
f = open("input19.txt", "r")
lines = [line.strip() for line in f.readlines()]
data = None
scans = []
for line in lines:
    if line == "":
        scans.append(Scan(data))
        data = None
    elif line.find("---") != -1:
        continue
    else:
        if data == None:
            data = []
        data.append([int(coord) for coord in line.split(',')])
scans.append(Scan(data))

for abs in scans[0].data:
    absolute_points.add(tuple(abs))

# Orient successive scans
def solve_scan(scan, abs_to_abs_dists, true_offsets):
    for perm in scan.perms:
        offsets = []
        for point in perm:
            dists = set([np.sum((point-other)**2) for other in perm])

            for abs, abs_dists in abs_to_abs_dists.items():
                if len(abs_dists & dists) >= 12: # If this dists match a similar fingerprint
                    cast_abs = np.array(abs)
                    offsets.append(cast_abs - point)
        
        #check that the permutation/orientation aligns everything properly
        if len(offsets) > 0 and all(np.array_equal(offset, offsets[0]) for offset in offsets):
                true_offset_for_this_scan = offsets[0]
                true_offsets.append(true_offset_for_this_scan)
                for point in perm: 
                    scan_0_oriented_point = tuple(point + true_offset_for_this_scan)
                    if scan_0_oriented_point not in absolute_points:
                        absolute_points.add(scan_0_oriented_point)
                print("SUCCESS")
                return True
    
    print("FAILURE")
    return False
                
queue = []
for scan in scans[1:]:
    queue.append(scan)

abs_to_abs_dists = {}
for abs in absolute_points:
    abs_to_abs_dists[abs] = absolute_point_to_pairwise_dists_with_other_abs(abs)

true_offsets = [np.array((0,0,0))]
while len(queue) > 0:
    print("\nCurrent Queue Length:", len(queue))
    scan = queue.pop(0)
    result = solve_scan(scan, abs_to_abs_dists, true_offsets)
    if result: 
        for abs in absolute_points: # recompute
            abs_to_abs_dists[abs] = absolute_point_to_pairwise_dists_with_other_abs(abs)
    if not result: 
        queue.append(scan)

# Part A
print(len(absolute_points))

# Part B
print(max([np.abs(np.array(A) - np.array(B)).sum() for A in true_offsets for B in true_offsets]))