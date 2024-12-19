import os 
import re
import sys
import copy 

f = open("input3.txt", "r")
input_cast = [re.sub("[^0-9]", "", line) for line in  f.readlines()]
strlen = len(input_cast[0])
print(input_cast)

gamma = 0
epsilon = 0
for character_index in range(strlen):
    counter = 0
    for binary_string_index in range(len(input_cast)):
        if input_cast[binary_string_index][character_index] == "1":
            counter += 1
    if counter > len(input_cast) / 2: ## doesnt account for ties well, however this case is underdefined
        gamma += 2**(strlen-1-character_index)
    else: 
        epsilon += 2**(strlen-1-character_index)
print(gamma, epsilon, gamma * epsilon)

def solve(binary_strings, primary, secondary):
    while True:
        for character_index in range(strlen):
            if len(binary_strings) == 1:
                decimal_conversion = int(binary_strings[0], 2)
                return decimal_conversion
            if len(binary_strings) == 0:
                sys.exit()

            counter = 0
            for binary_string_index in range(len(binary_strings)):
                if binary_strings[binary_string_index][character_index] == "1":
                    counter += 1

            if counter > len(binary_strings) / 2: # Majority 1
                binary_strings = list(filter(lambda x: x[character_index] == primary, binary_strings))
            elif counter == len(binary_strings) / 2 and len(binary_strings) % 2 == 0: # Tie
                binary_strings = list(filter(lambda x: x[character_index] == primary, binary_strings))
            else: #Majority 0
                binary_strings = list(filter(lambda x: x[character_index] == secondary, binary_strings))

print(solve(input_cast.copy(), "1", "0"),  solve(input_cast.copy(), "0", "1"), solve(input_cast.copy(), "1", "0") * solve(input_cast.copy(), "0", "1"))
