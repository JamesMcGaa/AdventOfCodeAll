# f = open("input24.txt", "r")
# instructions = [line.strip() for line in f.readlines()]

# def run(model_number):
#     nums = str(model_number)
#     nums_counter = 0
#     registers = {"w":0, "x":0, "y":0, "z":0}
#     for instruction in instructions:
#         ops = instruction.split()

#         if ops[0] == "inp":
#             var_a = ops[1]
#             registers[var_a] = int(nums[nums_counter])
#             nums_counter += 1

#         elif ops[0] == "add":
#             var_a = ops[1]
#             var_b = ops[2]
#             if var_b.isalpha():
#                 b_val = registers[var_b]
#             else:
#                 b_val = int(var_b)
#             registers[var_a] = registers[var_a] + b_val

#         elif ops[0] == "mul":
#             var_a = ops[1]
#             var_b = ops[2]
#             if var_b.isalpha():
#                 b_val = registers[var_b]
#             else:
#                 b_val = int(var_b)
#             registers[var_a] = registers[var_a] * b_val

#         elif ops[0] == "div":
#             var_a = ops[1]
#             var_b = ops[2]
#             if var_b.isalpha(): 
#                 b_val = registers[var_b]
#             else:
#                 b_val = int(var_b)
#             if b_val == 0:
#                 return False

#             registers[var_a] = registers[var_a] // b_val

#         elif ops[0] == "mod":
#             var_a = ops[1]
#             var_b = ops[2]
#             if var_b.isalpha(): 
#                 b_val = registers[var_b]
#             else:
#                 b_val = int(var_b)
#             if registers[var_a] < 0 or b_val <= 0:
#                 return False

#             registers[var_a] = registers[var_a] % b_val
        
#         elif ops[0] == "eql":
#             var_a = ops[1]
#             var_b = ops[2]
#             if var_b.isalpha(): 
#                 b_val = registers[var_b]
#             else:
#                 b_val = int(var_b)
#             registers[var_a] = 1 if registers[var_a] == b_val else 0
    
#     print(registers['z'])
#     return registers['z'] == 0

# for num in reversed(range(100000000000000)):
#     str_num = str(num)
#     if str_num.find("0") == -1:
#         res = run(str_num)
#         if res:
#             print(str_num)
#             break

instr, stack = [*open("input24.txt")], []

p, q = 99999999999999, 11111111111111

for i in range(14):
    a = int(instr[18*i+5].split()[-1])
    b = int(instr[18*i+15].split()[-1])

    if a > 0: stack+=[(i, b)]; continue
    j, b = stack.pop()

    p -= abs((a+b)*10**(13-[i,j][a>-b]))
    q += abs((a+b)*10**(13-[i,j][a<-b]))

print(p, q)