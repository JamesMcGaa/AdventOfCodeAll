import os
import sys
import copy
import re

f = open("input_21_james.txt", "r")
input_cast = [line.strip() for line in f.readlines()]

allergen_to_occurance = {}
ingredients_to_occurance = {}
def parse_line(line_index, line):
    sections = line.split("(")
    allergens = sections[1].split()
    for allergen in allergens[1:]: #remove contains
        parsed_allergen = allergen[:-1] #remove trailing comma or parenthesis
        if parsed_allergen not in allergen_to_occurance:
            allergen_to_occurance[parsed_allergen] = set()
        allergen_to_occurance[parsed_allergen].add(line_index)
    ingredients = sections[0].split()
    for ingredient in ingredients:
        if ingredient not in ingredients_to_occurance:
            ingredients_to_occurance[ingredient] = set()
        ingredients_to_occurance[ingredient].add(line_index)

for line_index, line in enumerate(input_cast):
    parse_line(line_index, line)

could_have_allergen = set()
allergen_to_possible_ingredient = {}
for ingredient in ingredients_to_occurance:
    for allergen in allergen_to_occurance:
        if ingredients_to_occurance[ingredient].issuperset(allergen_to_occurance[allergen]):
            could_have_allergen.add(ingredient)
            if allergen not in allergen_to_possible_ingredient:
                allergen_to_possible_ingredient[allergen] = set()
            allergen_to_possible_ingredient[allergen].add(ingredient)
            
cannot_have_allergen = set(ingredients_to_occurance.keys()) - set(could_have_allergen)

print("Solution to part A: ", sum([len(ingredients_to_occurance[ingredient]) for ingredient in cannot_have_allergen]))

allergen_to_solution = {}
while len(allergen_to_solution) != len(allergen_to_possible_ingredient): #while all are not locked in 
    for allergen, possible_ingredients in allergen_to_possible_ingredient.items():
        if len(possible_ingredients) == 1: #find an ingredient to lock in
            fixed_ingredient = list(possible_ingredients)[0]
            allergen_to_solution[allergen] = fixed_ingredient
            for other_allergen, other_possible_ingredients in allergen_to_possible_ingredient.items():
                if fixed_ingredient in other_possible_ingredients:
                    other_possible_ingredients.remove(fixed_ingredient)
            break

temp = []
for allergen, ingredient in allergen_to_solution.items():
    temp.append((allergen, ingredient))
temp.sort(key=lambda x: x[0]) 
sorted_ingredients = [pair[1] for pair in temp]
print("Solution to part B: ", ",".join(sorted_ingredients))