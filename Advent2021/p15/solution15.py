import os 
f = open("input15.txt", "r")
input_cast = [line.strip() for line in  f.readlines()]

new = []
for j in range(5):
    for line in input_cast:
        new_line = ""
        for i in range(5):
            for digit in line:
                digitized = int(digit)
                new_digitized = digitized + i + j
                if new_digitized > 9:
                    new_digitized -= 9
                new_line += str(new_digitized)
        new.append(new_line)
input_cast = new

START = (0, 0)
END = (len(input_cast)-1, len(input_cast[0])-1)
INFINITY = 1000000000000000000

graph = {}
for x in range(len(input_cast)):
    for y in range(len(input_cast[x])):

        for x_offset in [-1,1]:
            if x + x_offset >= 0 and x + x_offset < len(input_cast):
                if (x + x_offset, y) not in graph:
                    graph[(x + x_offset, y)] = {}
                graph[(x + x_offset, y)][(x,y)] = int(input_cast[x][y])

        for y_offset in [-1,1]:
            if y + y_offset >= 0 and y + y_offset < len(input_cast[0]):
                if (x, y + y_offset) not in graph:
                    graph[(x, y + y_offset)] = {}
                graph[(x, y + y_offset)][(x,y)] = int(input_cast[x][y])

##################################### LIST ###############################

# vertex_set = []
# dist = {}
# prev = {}
# for vertex in graph:
#     dist[vertex] = 1000000000000000
#     prev[vertex] = None
#     vertex_set.append(vertex)
# dist[(0,0)] = 0

# while len(vertex_set) > 0:
#     print(len(vertex_set))
#     vertex_set.sort(key=lambda x: dist[x], reverse=True)

#     u = vertex_set.pop()

#     for neighbor in graph[u]:
#         if neighbor in vertex_set:
#             alt = dist[u] + graph[u][neighbor]
#             if alt < dist[neighbor]:
#                 dist[neighbor] = alt
#                 prev[neighbor] = u

# print(dist[(len(input_cast)-1, len(input_cast[0])-1)])


##################################### BAD ###############################

# from heapq import *

# vertex_set = []
# heap_set = set()
# dist = {}
# for vertex in graph:
#     if vertex != START:
#         dist[vertex] = INFINITY
#         heappush(vertex_set, (INFINITY, vertex))
#         heap_set.add(vertex)
        
# dist[START] = 0
# heappush(vertex_set, (0, START))
# heap_set.add(START)

# while len(vertex_set) > 0:
#     u = heappop(vertex_set)[1]
#     heap_set.remove(u)
#     for neighbor in graph[u]:
#         if neighbor in heap_set:
#             alt = dist[u] + graph[u][neighbor]
#             if alt < dist[neighbor]:
#                 dist[neighbor] = alt

# print(dist[(len(input_cast)-1, len(input_cast[0])-1)])

##################################### PRIORITY QUEUE + RETIREMENT ###############################

from heapq import *

vertex_set = []
retired = set()
dist = {}
for vertex in graph:
    if vertex != START:
        dist[vertex] = INFINITY
        heappush(vertex_set, (INFINITY, vertex))

        
dist[START] = 0
heappush(vertex_set, (0, START))


while len(vertex_set) > 0:
    d, u = heappop(vertex_set)

    if u not in retired:
        for neighbor in graph[u]:
            if neighbor not in retired:
                alt = dist[u] + graph[u][neighbor]
                if alt < dist[neighbor]:
                    heappush(vertex_set, (alt, neighbor)) # normally we would call decrease_priority here
                    dist[neighbor] = alt
        retired.add(u)

print(dist[END])


##################################### DIJKSTAR ###############################

# from dijkstar import Graph, find_path
# graph = Graph()

# for x in range(len(input_cast)):
#     for y in range(len(input_cast[x])):

#         for x_offset in [-1,1]:
#             if x + x_offset >= 0 and x + x_offset < len(input_cast):
#                 graph.add_edge((x + x_offset, y), (x,y), int(input_cast[x][y]))

#         for y_offset in [-1,1]:
#             if y + y_offset >= 0 and y + y_offset < len(input_cast[0]):
#                 graph.add_edge((x, y + y_offset), (x,y), int(input_cast[x][y]))

# print(find_path(graph, (0,0), (len(input_cast)-1, len(input_cast[0])-1)))