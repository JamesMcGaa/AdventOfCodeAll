import os
import sys
import copy

f = open("tickets.txt", "r")
all_tickets = [[int(value) for value in line.strip().split(',')] for line in f.readlines()]
personal_ticket = all_tickets[0]

f = open("rules.txt", "r")
rules = [line.strip() for line in f.readlines()]
processed_rules = []
for rule in rules:
    rule_split = rule.split(":")
    values = rule_split[1].strip().split('or')
    processed_rule = []
    for value in values:
        value = value.strip()
        endpoints = value.split("-")
        processed_rule.append([int(endpoint) for endpoint in endpoints])
    processed_rules.append(tuple(processed_rule))

def passes_rule(rule, val):
    return (val >= rule[0][0] and val <= rule[0][1]) or (val >= rule[1][0] and val <= rule[1][1])

counter = 0 
valid_tickets = []
for ticket in all_tickets:
    all_values_in_ticket_are_valid = True 
    for val in ticket:
        passes_at_least_one_rule = False
        for rule in processed_rules:
            if passes_rule(rule, val): 
                passes_at_least_one_rule = True 
                break 
        if not passes_at_least_one_rule: # value failed all rulesidation, entire ticket is invalid
            all_values_in_ticket_are_valid = False
            counter += val

    if all_values_in_ticket_are_valid:
        valid_tickets.append(ticket)
print("Solution to part A: ", counter)


possible_solutions = {} #ticket index to set of rule indices
for ticket in valid_tickets: 
    for ticket_index, val in enumerate(ticket):
        matching_rules = set()
        for rule_index, rule in enumerate(processed_rules):
            if passes_rule(rule, val): 
                matching_rules.add(rule_index)
        if ticket_index not in possible_solutions:
            possible_solutions[ticket_index] = matching_rules
        else: 
            possible_solutions[ticket_index] = possible_solutions[ticket_index] & matching_rules

solution = {} #ticket index to rule index
while len(solution) != len(personal_ticket):
    for key, val_set in possible_solutions.items():
        if len(val_set) == 1:
            fixed_value = list(val_set)[0] #grab the only value from set
            solution[key] = fixed_value

            for other_key, other_val_set in possible_solutions.items(): #rule is solve -> we remove from our possible solution set 
                if fixed_value in other_val_set:
                    other_val_set.remove(fixed_value)
            del possible_solutions[key]
            break

inverse_solution = {v: k for k, v in solution.items()} #rule -> index so we can easily interate over the first 6 rules
counter = 1
for i in range(6):
    counter *= personal_ticket[inverse_solution[i]]
print("Solution to part B: ", counter)