import os
import sys
import copy
import re

f = open("input_18_james.txt", "r")
input_cast = [list(line.strip().replace(' ', '')) for line in f.readlines()]
expressions = [[int(character) if character in '1234567890' else character for character in line] for line in input_cast]
    
def evaluate(line, lower, upper):
    index = lower
    prev_operator = "+"
    prev_value = 0
    while index in range(lower, upper+1):
        character = line[index]

        if type(character) == int:
            if prev_operator == "+":
                prev_value += character
            elif prev_operator == "*":
                prev_value *= character

        elif character == "(":
            paren_counter = 1
            closing_index = index
            while paren_counter > 0:
                closing_index += 1
                if line[closing_index] == "(":
                    paren_counter += 1
                if line[closing_index] == ")":
                    paren_counter -= 1
            value = evaluate(line, index+1, closing_index-1)
            if prev_operator == "+":
                prev_value += value
            elif prev_operator == "*":
                prev_value *= value
            index = closing_index
        
        elif character in '+*':
            prev_operator = character
            
        index += 1

    return prev_value
    

from queue import Queue
 
def shunting_yard(line):
    output_queue = Queue(maxsize = 0)
    operation_stack = []
    for token in line:
        if type(token) == int:
            output_queue.put(token)
        elif token in "*+":
            while len(operation_stack) > 0 and (operation_stack[-1] == "+" and token == "*"):
                higher_priority_op = operation_stack.pop()
                output_queue.put(higher_priority_op)
            operation_stack.append(token)
        elif token == "(":
            operation_stack.append(token)
        elif token == ")":
            while operation_stack[-1] != "(":
                non_cancelling_left_bracket = operation_stack.pop()
                output_queue.put(non_cancelling_left_bracket)
            operation_stack.pop() #remove all brackets
        else:
            raise Exception("Improper Token")
    
    while len(operation_stack) > 0:
        output_queue.put(operation_stack.pop())
    return list(output_queue.queue)

def eval_reverse_polish(queue):
    evaluation_stack = []
    for token in queue:
        if type(token) == int:
            evaluation_stack.append(token)
        else:
            arg_2 = evaluation_stack.pop()
            arg_1 = evaluation_stack.pop()
            evaluation_stack.append(arg_1 + arg_2 if token == "+" else arg_1 * arg_2)
    return evaluation_stack[0]

def evaluate_with_addition_priority(line):
    return eval_reverse_polish(shunting_yard(line))

print("Solution for part A: ", sum([evaluate(line, 0, len(line)-1) for line in expressions]))
print("Solution for part B: ", sum([evaluate_with_addition_priority(line) for line in expressions]))