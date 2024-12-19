import os 
import sys

f = open("input_7_james_test.txt", "r")
input_cast = [line.strip() for line in f.readlines()]

"""
  " 4 dim coral bags" ---> ("dim coral", 4)
"""
def alphabetize(s):
    base = s.strip()
    color = ''.join([i for i in base if i.isalpha() or i == " "])
    num = ''.join([i for i in base if i.isnumeric() or i == " "])
    try:
        num = int(num)
    except:
        num = 1
    color = color.replace("bags", "").replace("bag", "").strip()
    return (num, color)

graph = {} # part a
reversed_graph = {} # part b
rules = []
for line in input_cast:
    if "no other" in line:
        continue
    x = line.split("contain")
    end = alphabetize(x[0])[1]
    for start_raw in x[1].split(','):
        start = alphabetize(start_raw)
        start_num = start[0]
        start = start[1]

        if start not in graph:
            graph[start] = set()
        graph[start].add(end)

        if end not in reversed_graph:
            reversed_graph[end] = {}
        reversed_graph[end][start] = start_num

def subtree_sum(node):
    if node not in reversed_graph: # hits the above continue
        return 1
    
    counter = 1 # count the node itself
    for neighbor in reversed_graph[node]:
        weight = reversed_graph[node][neighbor]
        counter += weight * subtree_sum(neighbor)
    return counter

visited = set()
stack = ["shiny gold"]
while len(stack) > 0:
    current = stack.pop()
    if current in graph:
        for neighbor in graph[current]:
            if neighbor not in visited:
                visited.add(neighbor)
                stack.append(neighbor)
                
print("Solution to part A: ", len(visited))
print("Solution to part B: ", subtree_sum("shiny gold") - 1)
