f = open("input.txt", "r")
inp = []
WINS = {
    "X" : "C",
    "Y" : "A",
    "Z" : "B",
}
DRAWS = {
    "X" : "A",
    "Y" : "B",
    "Z" : "C",
}
LOSSES = {
    "X" : "B",
    "Y" : "C",
    "Z" : "A",
}
SCORE = {
    "X" : 1,
    "Y" : 2,
    "Z" : 3,
}
for line in f.readlines():
    inp.append(line.split())

score = 0
for opponent, yours in inp:
    if WINS[yours] == opponent:
        score += 6
    if DRAWS[yours] == opponent:
        score += 3
    score += SCORE[yours]
print(score)

WIN_MAP = dict((v, k) for k, v in WINS.items())
DRAW_MAP = dict((v, k) for k, v in DRAWS.items())
LOSS_MAP = dict((v, k) for k, v in LOSSES.items())

score_pt2 = 0
for opponent, yours in inp:
    if yours == "X":
        adj_move = LOSS_MAP[opponent]
    if yours == "Y":
        adj_move = DRAW_MAP[opponent]
        score_pt2 += 3
    if yours == "Z":
        adj_move = WIN_MAP[opponent]
        score_pt2 += 6
    score_pt2 += SCORE[adj_move]
print(score_pt2)