import os 
f = open("input_4_james.txt", "r")
input_cast = f.readlines()

formatted = []
current = {}
for line in input_cast:
    if len(line) == 1:
        formatted.append(current)
        current = {}
    else:
        for keyvals in line.split():
            keyval =  keyvals.split(":")
            key = keyval[0]
            val = keyval[1]
            current[key] = val
formatted.append(current)


def validate(passport):
    if len(passport['byr']) == 4:
        try:
            x = int(passport['byr'])
            if x < 1920 or x > 2002:
                return False
        except:
            return False
    else:
        return False


    if len(passport['iyr']) == 4:
        try:
            x = int(passport['iyr'])
            if x < 2010 or x > 2020:
                return False
        except:
            return False
    else:
        return False


    if len(passport['eyr']) == 4:
        try:
            x = int(passport['eyr'])
            if x < 2020 or x > 2030:
                return False
        except:
            return False
    else:
        return False


    if len(passport['hgt']) > 2:
        if passport['hgt'][-2:] == 'cm':
            try:
                x = int(passport['hgt'][:-2])
                if x < 150 or x > 193:
                    return False
            except:
                return False  
         
        elif passport['hgt'][-2:] == 'in':
            try:
                x = int(passport['hgt'][:-2])
                if x < 59 or x > 76:
                    return False
            except:
                return False  
        else:
            return False
    else:
        return False


    if len(passport['hcl']) != 7 \
     or passport['hcl'][0] != '#' \
     or len(set(passport['hcl'][1:]) | set('#1234567890abcdef')) != 17:
        return False


    if passport['ecl'] not in ['amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth']:
        return False


    if len(passport['pid']) != 9 or len(set(passport['pid']) | set('0123456789')) != 10: 
        return False


    return True
    

counter = 0
validated_counter = 0
for passport in formatted:
    if not len(set(['byr', 'iyr', 'eyr', 'hgt', 'hcl', 'ecl', 'pid', ]) & set(passport.keys())) == 7:
        continue
    counter += 1
    if not validate(passport):
        continue
    validated_counter += 1
print("Part A Solution: ", counter)
print("Part B Solution: ", validated_counter)