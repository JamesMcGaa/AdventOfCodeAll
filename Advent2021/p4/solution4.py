import re

f = open("input4.txt", "r")
input_cast = [line for line in  f.readlines()]

BINGO_SIZE = 5

class Bingo:
    def __init__(self):
        self.rows = []
        self.marked = set()
    
    def add_row(self, row):
        self.rows.append(row)

    def mark(self, val):
        for i in range(BINGO_SIZE):
            for j in range(BINGO_SIZE):
                if self.rows[i][j] == val:
                    self.marked.add((i,j))
                    return self.check_complete()
    
    def check_complete(self):
        for i in range(BINGO_SIZE):
            if all([(i,j) in self.marked for j in range(BINGO_SIZE)]):
                return self.sum_unmarked()

        for j in range(BINGO_SIZE):
            if all([(i,j) in self.marked for i in range(BINGO_SIZE)]):
                return self.sum_unmarked()
        
        # if all([(i,BINGO_SIZE-1-i) in self.marked for i in range(BINGO_SIZE)]) or all([(i,i) in self.marked for i in range(BINGO_SIZE)]):
        #     return self.sum_unmarked()
        
        return None
    
    def sum_unmarked(self):
        counter = 0
        for i in range(BINGO_SIZE):
            for j in range(BINGO_SIZE):
                if (i,j) not in self.marked:
                    counter += self.rows[i][j]
        return counter

def process_input(input_cast):
    moves = [int(re.sub("[^0-9]", "", unclean_str_val)) for unclean_str_val in input_cast[0].split(',')]
    bingos = []

    current_bingo = Bingo()
    for line in input_cast[2:]:
        if len(line.split()) != 0:
            processed_row = [int(val) for val in line.split()]
            current_bingo.add_row(processed_row)
        else:
            bingos.append(current_bingo)
            current_bingo = Bingo()
    
    bingos.append(current_bingo)

    return moves, bingos

def simulate_4a(input_cast):
    moves, bingos = process_input(input_cast)

    for move in moves: 
        for bingo in bingos:
            val = bingo.mark(move)
            if val:
                return val * move

def simulate_4b(input_cast):
    moves, bingos = process_input(input_cast)

    for move in moves: 
        bingos_to_remove = []
        for bingo in bingos:
            val = bingo.mark(move)
            if val:
                if len(bingos) == 1:
                    return val * move
                else:
                    bingos_to_remove.append(bingo)

        for bingo in bingos_to_remove: # Be careful when removing an array being iterated on
            bingos.remove(bingo)

print(simulate_4a(input_cast))
print(simulate_4b(input_cast))