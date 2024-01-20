import os 
from itertools import permutations

f = open("input8.txt", "r")
input_cast = [[group.split() for group in line.split("|")] for line in  f.readlines()]

# part a
counter = 0
for line in input_cast:
    post = line[1]
    for seq in post:
        if len(seq) in [2,3,4,7]:
            counter += 1
print(counter)


# part b
ALPHABET = 'abcdefg'
BIG_NUMS_TO_TRUES = {
    0:set('abcefg'),
    1:set('cf'),
    2:set('acdeg'),
    3:set('acdfg'),
    4:set('bcdf'),
    5:set('abdfg'),
    6:set('abdefg'),
    7:set('acf'),
    8:set('acbdefg'),
    9:set('abcdfg'),
}

def get_translation(alphabet_perm):
    translation = {}
    for i, true_letter in enumerate(alphabet_perm):
        translation[ALPHABET[i]] = true_letter
    return translation

def get_big_number(seq, translation):
    activated_letters = set([translation[letter] for letter in seq])  
    for big_number, required_letters in BIG_NUMS_TO_TRUES.items():
        if required_letters == activated_letters:
            return big_number
    return None

def solve(pre, post):
    alphabet_perms = list(permutations([ALPHABET[index] for index in range(len(ALPHABET))]))
    for alphabet_perm in alphabet_perms:
        solved = set()
        translation = get_translation(alphabet_perm)
        for seq in pre:
            potential_big_number = get_big_number(seq, translation)
            if potential_big_number is not None:
                solved.add(potential_big_number)
        if solved == set(BIG_NUMS_TO_TRUES.keys()):
            return translation


outer_counter = 0 
for line in input_cast:
    pre, post = line

    translation = solve(pre, post)

    counter = 0
    for post_seq in post:
        counter *= 10
        counter += get_big_number(post_seq, translation)
    outer_counter += counter

print(outer_counter)