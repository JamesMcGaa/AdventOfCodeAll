import copy

TOWER_IDX_TO_VALUE = {
    3: "A",
    5: "B",
    7: "C",
    9: "D",
}

VALUE_TO_TOWER_IDX = {
    "A" : 3,
    "B" : 5,
    "C" : 7,
    "D" : 9,
}

VALUE_TO_COST = {
    "A" : 1,
    "B" : 10,
    "C" : 100,
    "D" : 1000,
}

class State():
    def __init__(self, towers, hallways):
        # {
        #     3: ["B", "D", "D", "A"],
        #     5: ["C", "C", "B", "D"],
        #     7: ["B", "B", "A", "C"],
        #     9: ["D", "A", "C", "A"],
        # }
        self.towers = towers


        # {
        #     1: ".",
        #     2: ".",
        #     4: ".",
        #     6: ".",
        #     8: ".",
        #     10: ".",
        #     11: ".",
        # }
        self.hallways = hallways

    
    def to_string(self):
        return str(self.towers) + str(self.hallways)

    def get_pop_neighbors(self):
        neighbors = []
        #pop from tower to hallway 
        for tower_idx, tower_list in self.towers.items():
            
            #find the top nonzero value
            top_nonzero = None
            height = None
            for h, value in enumerate(tower_list):
                if value != ".":
                    top_nonzero = value
                    height = h
                    break
                
            #dont pop from a good stack
            if TOWER_IDX_TO_VALUE[tower_idx] == top_nonzero and \
                all([other_val == TOWER_IDX_TO_VALUE[tower_idx] for other_val in self.towers[tower_idx][height:]]):
                continue
            
            if top_nonzero is not None:
                #try to place it in every hallway spot
                for target_hallway in self.hallways.keys():
                    all_clear = True
                    low = min(target_hallway, tower_idx)
                    hi = max(target_hallway, tower_idx)

                    #make sure everything in between is clear
                    for other_hallway_idx, other_hallway_val in self.hallways.items():
                        if low <= other_hallway_idx and other_hallway_idx <= hi and other_hallway_val != ".":
                            all_clear = False
                            break
                    
                    if all_clear:
                        new_hallways = copy.deepcopy(self.hallways)
                        new_towers = copy.deepcopy(self.towers)
                        new_hallways[target_hallway] = top_nonzero
                        new_towers[tower_idx][height] = "."
                        neighbors.append(
                            (State(new_towers, new_hallways), (abs(target_hallway - tower_idx) + height + 1) * VALUE_TO_COST[top_nonzero])
                        )
        return neighbors
    
    def get_push_neighbors(self):
        neighbors = []
        #push from hallway to tower
        for hallway_idx, hallway_value in self.hallways.items():
            #non . chars
            if hallway_value in VALUE_TO_TOWER_IDX: 
                tower_idx = VALUE_TO_TOWER_IDX[hallway_value]

                exists_space = set(self.towers[tower_idx]) == set([TOWER_IDX_TO_VALUE[tower_idx], "."]) or set(self.towers[tower_idx]) == set(".")

                open_path = True
                #make sure everything in between is clear
                for other_hallway_idx, other_hallway_val in self.hallways.items():
                    low = min(hallway_idx, tower_idx)
                    hi = max(hallway_idx, tower_idx)
                    if other_hallway_idx != hallway_idx and low <= other_hallway_idx and other_hallway_idx <= hi and other_hallway_val != ".":
                        open_path = False
                        break

                if exists_space and open_path:

                    tower_height = 3
                    while self.towers[tower_idx][tower_height] == TOWER_IDX_TO_VALUE[tower_idx]:
                        tower_height -= 1
                        
                    new_hallways = copy.deepcopy(self.hallways)
                    new_towers = copy.deepcopy(self.towers)
                    new_hallways[hallway_idx] = "."
                    new_towers[tower_idx][tower_height] = hallway_value

                    neighbors.append(
                        (State(new_towers, new_hallways), (abs(hallway_idx - tower_idx) + tower_height + 1) * VALUE_TO_COST[hallway_value])
                    )
        return neighbors
        
    
    def get_neighbors(self):
        return self.get_pop_neighbors() + self.get_push_neighbors()