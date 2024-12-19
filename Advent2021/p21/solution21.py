dice_location = 1
# p1_location = 4
# p2_location = 8

p1_location = 7
p2_location = 4

dice_counter = 0
p1_counter = 0
p2_counter = 0

while True:
    move_counter = 0
    for i in range(3):
        move_counter += 1
        p1_location = (p1_location + dice_location - 1) % 10 + 1
        dice_location = (dice_location % 100) + 1
    
    dice_counter += 3
    p1_counter += p1_location
    if p1_counter >= 1000:
        print(dice_counter * p2_counter)
        break

    move_counter = 0
    for i in range(3):
        move_counter += 1
        p2_location = (p2_location + dice_location - 1) % 10 + 1
        dice_location = (dice_location % 100) + 1
    
    dice_counter += 3
    p2_counter += p2_location
    if p2_counter >= 1000:
        print(dice_counter * p1_counter)
        break



p1_universes = 0
p2_universes = 0
gamestate_to_counts = {(7, 4, 0, 0, True) : 1} #(p1_location, p2_location, p1_counter, p2_counter, p1_goes_next): count
while len(gamestate_to_counts) > 0:
    new_gamestate_to_counts = {}
    for gamestate, count in gamestate_to_counts.items():
        p1_location, p2_location, p1_counter, p2_counter, p1_goes_next = gamestate

        if p1_goes_next:
            for roll1 in [1,2,3]:
                for roll2 in [1,2,3]:
                    for roll3 in [1,2,3]:
                        roll = roll1 + roll2 + roll3
                        p1_location_new = (p1_location + roll - 1) % 10 + 1
                        p1_counter_new = p1_counter + p1_location_new
                        if p1_counter_new >= 21:
                            p1_universes += count
                        else:
                            new_gamestate_to_counts[(p1_location_new, p2_location, p1_counter_new, p2_counter, False)] = \
                                new_gamestate_to_counts.get((p1_location_new, p2_location, p1_counter_new, p2_counter, False), 0) \
                                + gamestate_to_counts.get((p1_location_new, p2_location, p1_counter_new, p2_counter, False), 0) \
                                + count

        else:
            for roll1 in [1,2,3]:
                for roll2 in [1,2,3]:
                    for roll3 in [1,2,3]:
                        roll = roll1 + roll2 + roll3
                        p2_location_new = (p2_location + roll - 1) % 10 + 1
                        p2_counter_new = p2_counter + p2_location_new
                        if p2_counter_new >= 21:
                            p2_universes += count
                        else:
                            new_gamestate_to_counts[(p1_location, p2_location_new, p1_counter, p2_counter_new, True)] = \
                                new_gamestate_to_counts.get((p1_location, p2_location_new, p1_counter, p2_counter_new, True), 0) \
                                + gamestate_to_counts.get((p1_location, p2_location_new, p1_counter, p2_counter_new, True), 0) \
                                + count

    gamestate_to_counts = new_gamestate_to_counts

print(p1_universes, p2_universes, max(p1_universes, p2_universes))