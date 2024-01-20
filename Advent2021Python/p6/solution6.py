import os 
from collections import Counter

f = open("input6.txt", "r")
input_cast = dict(Counter([int(fish) for fish in f.readline().split(",")]))

# def cycle(input_cast):
#     additions = []
#     for idx, fish in enumerate(input_cast):
#         input_cast[idx] -= 1
#         if input_cast[idx] == -1:
#             input_cast[idx] = 6
#             additions.append(8)
#     input_cast.extend(additions)

# for i in range(80):
#     cycle(input_cast)
# print(input_cast)

def cycle(prior):
    post = {}
    for i in range(0,8):
        post[i] = prior.get(i+1, 0)
    post[8] = prior.get(0, 0)
    post[6] += prior.get(0, 0)
    return post

for i in range(256):
    input_cast = cycle(input_cast)

counter = 0
for key, val in input_cast.items():
    counter += val
print(counter)