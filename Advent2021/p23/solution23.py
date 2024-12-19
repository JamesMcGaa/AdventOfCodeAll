from dijkstar import Graph, find_path
from collections import deque
from constants import *

graph = Graph()
q = deque()
q.append(START_INPUT)
seen = set()
seen.add(START_INPUT.to_string())
while len(q) > 0: 
    current = q.popleft()
    neighbors = current.get_neighbors()
    for neighbor, dist in neighbors:
        new_state_str = neighbor.to_string()
        graph.add_edge(current.to_string(), new_state_str, dist)
        if new_state_str not in seen:
            seen.add(new_state_str)
            q.append(neighbor)

print(find_path(graph, START_INPUT.to_string(), END.to_string()))