import math
import ast
from abc import ABC, ABCMeta, abstractmethod

class RecursiveInterface(ABC):
    @abstractmethod
    def explode(self): raise NotImplementedError
    @abstractmethod
    def split(self): raise NotImplementedError
    @abstractmethod
    def magnitude(self): raise NotImplementedError


class Leaf(RecursiveInterface):
    def __init__(self, value, parent):
        self.value = value
        self.parent = parent
        self.left = None
        self.rigth = None
    
    def toArray(self):
        return self.value
    
    def explode(self):
        return False
    
    def split(self):
        if self.value >= 10:
            values = [self.value // 2, int(math.ceil(self.value / 2))]
            replacement = SnailfishNumber(values, self.parent)
            if self.parent.right == self:
                self.parent.right = replacement
            else:
                self.parent.left = replacement
            return True
        
        return False
    
    def magnitude(self):
        return self.value
    

class SnailfishNumber(RecursiveInterface):
    def __init__(self, inp, parent = None, additionLeft = None, additionRight = None): # final 2 args should be internal only
        if inp != None:
            self.left = SnailfishNumber.snailfish_or_regular(inp[0], self)
            self.right = SnailfishNumber.snailfish_or_regular(inp[1], self)
            self.parent = parent
        else:
            self.left = additionLeft
            self.left.parent = self
            self.right = additionRight
            self.right.parent = self
            self.parent = None
    
    def toArray(self):
        return [self.left.toArray(), self.right.toArray()]
    
    def next_right_leaf(self, val):
        current = self
        while current.parent != None and current.parent.right == current:
            current = current.parent
        
        if current.parent != None:
            current = current.parent.right
        else:
            return None

        while type(current) != Leaf:
            current = current.left
        
        current.value += val
        return current.value

    def next_left_leaf(self, val):
        current = self
        while current.parent != None and current.parent.left == current:
            current = current.parent
        
        if current.parent != None:
            current = current.parent.left
        else:
            return None

        while type(current) != Leaf:
            current = current.right
        
        current.value += val    
        return current.value

    def explode(self):
        if self.depth() == 4:
            self.next_right_leaf(self.right.value)
            self.next_left_leaf(self.left.value)
            if self.parent.right == self:
                self.parent.right = Leaf(0, self.parent)
            else:
                self.parent.left = Leaf(0, self.parent)
            return True
        else:
            return self.left.explode() or self.right.explode()
    
    def depth(self):
        current = self
        depth = 0
        while current.parent != None:
            depth += 1
            current = current.parent
        return depth
    
    def split(self):
        return self.left.split() or self.right.split()
    
    def magnitude(self):
        return 3 * self.left.magnitude() + 2 * self.right.magnitude()
    
    @staticmethod
    def add_and_reduce(sfn1, sfn2):
        result = SnailfishNumber(None, None, sfn1, sfn2)
        while True:
            if result.explode():
                continue
            if result.split():
                continue
            break
        return result
    
    @classmethod
    def snailfish_or_regular(cls, inp, parent):
        if type(inp) == list:
            return cls(inp, parent)

        return Leaf(inp, parent)
    
    
f = open("input18.txt", "r")
largest_mag = 0
expressions = [ast.literal_eval(line.strip()) for line in f.readlines()]

cumulative_sfn = SnailfishNumber(expressions[0])
for expression in expressions[1:]:
    sfn_2 =  SnailfishNumber(expression)
    cumulative_sfn = SnailfishNumber.add_and_reduce(cumulative_sfn, sfn_2)
    print(cumulative_sfn.toArray())
print("Cumulative Magnitude:", cumulative_sfn.magnitude())

for e1 in expressions:
    for e2 in expressions: 
        if e1 != e2:
            e1_snail = SnailfishNumber(e1)
            e2_snail = SnailfishNumber(e2)
            result = SnailfishNumber.add_and_reduce(e1_snail, e2_snail)
            largest_mag = max(largest_mag, result.magnitude())
print("Max Pairwise Magnitude:", largest_mag)
