import os 
f = open("input14.txt", "r")
input_cast = [line.strip() for line in  f.readlines()]

print(input_cast)

rules = {}
base = input_cast[0]
for rule in input_cast[2:]:
    rules[rule.split("->")[0].strip()] = rule.split("->")[1].strip()
print(rules)

# for i in range(40):
#     print(i)
#     new = [base[0]]
#     ind = 1
#     while ind < len(base):
#         if base[ind-1] + base[ind] in rules:
#             new.append(rules[base[ind-1] + base[ind]])
#         new.append(base[ind])
#         ind += 1
#     base = "".join(new)

# from collections import Counter
# c = Counter(base)
# print(c)

#############################
# counter = {}
# for c in base:
#     if c not in counter:
#         counter[c] = 0
#     counter[c] += 1
    
# boundaries = []
# for i in range(len(base) - 1):
#     possible_boundary = base[i] + base[i + 1]
#     if possible_boundary in rules:
#         boundaries.append(possible_boundary)


# for i in range(40):
#     print(i)
#     new_boundaries = []
#     for boundary in boundaries:
#         a, b = boundary[0], boundary[1]
#         c = rules[a + b]
#         if c not in counter:
#             counter[c] = 0
#         counter[c] += 1
#         if a + c in rules:
#             new_boundaries.append(a+c)
#         if c + b in rules:
#             new_boundaries.append(c+b)
#     boundaries = new_boundaries
# print(max(counter.values()) - min(counter.values()))
#############################
        
        

counter = {}
for c in base:
    if c not in counter:
        counter[c] = 0
    counter[c] += 1
    
boundaries = {}
for i in range(len(base) - 1):
    possible_boundary = base[i] + base[i + 1]
    if possible_boundary in rules:
        if possible_boundary not in boundaries:
            boundaries[possible_boundary] = 0
        boundaries[possible_boundary] += 1


for i in range(40):
    print(i)
    new_boundaries = {}
    for boundary, count in boundaries.items():
        a, b = boundary[0], boundary[1]
        c = rules[a + b]
        if c not in counter:
            counter[c] = 0
        counter[c] += count
        if a + c in rules:
            if a + c not in new_boundaries:
                new_boundaries[a + c] = 0
            new_boundaries[a + c] += count
        if c + b in rules:
            if c + b not in new_boundaries:
                new_boundaries[c + b] = 0
            new_boundaries[c + b] += count
    boundaries = new_boundaries
print(max(counter.values()) - min(counter.values()))

