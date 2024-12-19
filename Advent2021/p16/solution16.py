import os 
import numpy as np
import math
f = open("input16.txt", "r")
input_raw = f.readline().strip()
input_formatted = ''.join(bin(int(c, 16))[2:].zfill(4) for c in input_raw)

TYPE_LITERAL = "TYPE_LITERAL"
TYPE_OPERATOR_BITS = "TYPE_OPERATOR_BITS"
TYPE_OPERATOR_PACKETS = "TYPE_OPERATOR_PACKETS"

class packet:
    def __init__(self, type, value, type_id, exit_ind):
        self.type = type
        self.value = value
        self.type_id = type_id
        self.exit_ind = exit_ind
    
    def __str__(self):
        return "TYPE: " + str(self.type) + "\n" \
            + "VALUE: " + str(self.value) + "\n" \
            + "TYPE_ID: " + str(self.type_id) + "\n" \
            + "EXIT_IND: " + str(self.exit_ind) + "\n"

class holder:
    def __init__(self, inp):
        self.data = inp
        self.ind = 0
        self.packet_version_counter = 0 
    
    def pop(self):
        packet_version = int(self.data[self.ind:self.ind+3], 2) #unused
        self.packet_version_counter += packet_version
        type_id = int(self.data[self.ind+3:self.ind+6], 2)
        self.ind += 6
        if type_id == 4: #literal
            binary_literal_str = ""
            while True:
                terminal = self.data[self.ind] == "0"
                binary_literal_str += self.data[self.ind+1:self.ind+5]
                self.ind += 5
                if terminal:
                    break
            literal_value = int(binary_literal_str, 2)
            return packet(TYPE_LITERAL, literal_value, type_id, self.ind)
        
        else: #operator
            length_type_id = self.data[self.ind]
            self.ind += 1
            if length_type_id == "0": # subpackets total bits = next 15 bits
                total_subpacket_len = int(self.data[self.ind:self.ind+15],2)
                self.ind += 15
                return packet(TYPE_OPERATOR_BITS, total_subpacket_len, type_id, self.ind)
            
            else: # total subpackets = next 11 bits
                total_subpackets = int(self.data[self.ind:self.ind+11],2)
                self.ind += 11
                return packet(TYPE_OPERATOR_PACKETS, total_subpackets, type_id, self.ind)
    
    def countPacketVersions(self):
        try:
            while True:
                self.pop()
        except:
            pass
        return self.packet_version_counter
    
    def evaluate(self):
        current = self.pop()

        buffer = []
        current_end = current.exit_ind
        ind = current_end

        if current.type == TYPE_LITERAL:
            return current.value, current.exit_ind

        elif current.type == TYPE_OPERATOR_PACKETS:
            for i in range(current.value):
                next_value, next_exit = self.evaluate()
                ind = next_exit
                buffer.append(next_value)
            
        else: # TYPE_OPERATOR_BITS
            while ind - current_end < current.value:
                next_value, next_exit = self.evaluate()
                ind = next_exit
                buffer.append(next_value)

        match current.type_id:
            case 0:
                return sum(buffer), ind
            case 1:
                # print(np.prod(buffer), math.prod(buffer), buffer)
                return math.prod(buffer), ind # numpy prod was overflowing here
            case 2:
                return min(buffer), ind
            case 3:
                return max(buffer), ind
            case 5:
                return (1, ind) if buffer[0] > buffer[1] else (0, ind)
            case 6:
                return (1, ind) if buffer[0] < buffer[1] else (0, ind)
            case 7:
                return (1, ind) if buffer[0] == buffer[1] else (0, ind)


holder_instance = holder(input_formatted)
print(holder_instance.countPacketVersions())

holder_instance = holder(input_formatted)
print(holder_instance.evaluate()[0])

