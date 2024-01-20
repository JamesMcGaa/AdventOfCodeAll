f = open("input.txt", "r")
elves = [[]]


for line in f.readlines():
    if line != "\n":
        elves[-1].append(int(line))
    else:
        elves.append([])

totals = list(map(lambda x: sum(x), elves))
totals.sort()
print(max(totals))
print(sum(totals[-3:]))