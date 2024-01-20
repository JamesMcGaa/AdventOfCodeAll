import os 
import statistics

f = open("input10.txt", "r")
input_cast = [line.strip() for line in  f.readlines()]


close_to_open = {
    ')' : '(',
    ']' : '[',
    '}' : '{',
    '>' : '<',
}
improper_close_to_value = {
    ')' : 3,
    ']' : 57,
    '}' : 1197,
    '>' : 25137,
}
stack_remainder_to_closing_value = {
    '(' : 1,
    '[' : 2,
    '{' : 3,
    '<' : 4,
}

fixing_values = []
counter = 0
for line in input_cast:
    stacc = []
    corrupt = False
    for char in line: 
        if char not in close_to_open: #opening
            stacc.append(char)
        else:
            val = stacc.pop()
            if close_to_open[char] != val:
                corrupt = True
                counter += improper_close_to_value[char]
    
    if not corrupt:
        corrupt_counter = 0
        for char in stacc[::-1]:

            corrupt_counter *= 5
            corrupt_counter += stack_remainder_to_closing_value[char]
        fixing_values.append(corrupt_counter)
print(counter)
print(statistics.median(fixing_values))

