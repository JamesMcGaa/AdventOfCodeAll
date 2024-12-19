import sys

INPUT_X = (265, 287)
INPUT_Y = (-103, -58)

EXAMPLE_X = (20, 30)
EXAMPLE_Y = (-10, -5)

# returns timestamps [], is_final_timestamp_perpetual boolean 
def hits_x_timestamps(x_velocity, x_ranges):
    timestamps = []

    timestamp = 0
    x_pos = 0 
    while x_pos < x_ranges[1] and x_velocity != 0: #while still further valids to go and still going
        timestamp += 1
        new_x_pos = x_pos + x_velocity
        new_x_velocity = max(0, x_velocity - 1)

        if new_x_pos >= x_ranges[0] and new_x_pos <= x_ranges[1]:
            timestamps.append(timestamp)

        if new_x_velocity == 0:
            return timestamps, True
        else:
            x_pos = new_x_pos
            x_velocity = new_x_velocity

    return timestamps, False

# returns timestamps []
def hits_y_timestamps(y_velocity, y_ranges):
    timestamps = []

    timestamp = 0
    y_pos = 0 
    while y_pos >= y_ranges[0] or y_velocity > 0: #if we are both falling and below, return
        timestamp += 1
        new_y_pos = y_pos + y_velocity
        new_y_velocity = y_velocity - 1

        if new_y_pos >= y_ranges[0] and new_y_pos <= y_ranges[1]:
            timestamps.append(timestamp)

        y_pos = new_y_pos
        y_velocity = new_y_velocity

    return timestamps

def solve(x_range, y_range):
    perpetuals = {} # {timestamp: [x_vels, ...]}
    valid_timestamp_counts = {} # {timestamp: [x_vels, ...]}
    for x_vel in range(1, x_range[1] + 1):
        timestamps, perpetual = hits_x_timestamps(x_vel, x_range)
        for timestamp in timestamps:
            valid_timestamp_counts[timestamp] = valid_timestamp_counts.get(timestamp, []) + [x_vel]

        if perpetual and len(timestamps) > 0:
            perpetuals[timestamps[-1]] = perpetuals.get(timestamps[-1], []) + [x_vel]

    counter = 0
    for y_vel in reversed(range(y_range[0] - 1, abs(y_range[0]) + 1)):
        possibles = hits_y_timestamps(y_vel, y_range)
        matches = set()

        if len(possibles) == 0:
            continue

        for timestamp in possibles:
            # if timestamp in valid_timestamp_counts or timestamp >= min(perpetuals.keys()):
            #     print(y_vel * (y_vel + 1) // 2)
            #     sys.exit()

            for x_vel in valid_timestamp_counts.get(timestamp, []):
                matches.add(x_vel)
            
            for perpetual in perpetuals: 
                if perpetual <= timestamp:
                    for x_vel in valid_timestamp_counts.get(perpetual, []):
                        matches.add(x_vel)

        counter += len(matches)
    
    return counter


print(solve(EXAMPLE_X, EXAMPLE_Y))
print(solve(INPUT_X, INPUT_Y))


