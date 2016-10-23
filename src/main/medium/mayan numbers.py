import sys
import math
from functools import reduce

def to_mayan_number(number, NUMBERS):
	if number<20:
		return NUMBERS[number]
	else:
		remainder = number%20
		text = to_mayan_number(int(number/20), NUMBERS)
		return line_to_string(text, NUMBERS[remainder])

def to_arabian_number(number, NUMBERS):
	returned = 0
	power = 1
	for i, n in enumerate(reversed(number)):
		index = NUMBERS.index(n)
#		print("number\n%s is at index %s"%(n, index), file=sys.stderr)
		if i==0:
			returned = index
		else:
			power = power*20
			returned += index*power
	return returned

def print_mayan_number(number):
	for i, n in enumerate(number):
		print("\n=== %d\n%s"%(i, n), file=sys.stderr)

def line_to_string(a, b):
	return "%s\n%s"%(a, b)

def parse_mayan_numbers(l, h, lines):
	returned=[]
	for i in range(int(len(lines[0])/l)):
#		print("i is %d"%i, file=sys.stderr)
		number = []
		for j in range(h):
			start=i*l
			end=(i+1)*l
			number.append(lines[j][start:end])
		returned.append(reduce(line_to_string, number))
	return returned

def parse_mayan_value(l, h, lines):
	returned=[]
	number = ""
	for i in range(int(len(lines)/h)):
		number = []
		for j in range(h):
			number.append(lines[i*h+j])
		returned.append(reduce(line_to_string, number))
	return returned

l, h = [int(i) for i in input().split()]
print("Mayan numbers uses L=%d, H=%d"%(l,h), file=sys.stderr)
# Here we read mayan numbers
lines = []
for i in range(h):
	lines.append(input())

NUMBERS = parse_mayan_numbers(l, h, lines)
# print_mayan_number(NUMBERS)
# Now we read the numbers on which we want to do operation
s1 = int(input())
lines = []
for i in range(s1):
	lines.append(input())
n1mayan = parse_mayan_value(l, h, lines)
n1arabian = to_arabian_number(n1mayan, NUMBERS)
print("Number 1 is", file=sys.stderr)
print_mayan_number(n1mayan)
# print("In arabian, it means %d"%n1arabian, file=sys.stderr)
s2 = int(input())
lines = []
for i in range(s2):
	lines.append(input())
n2mayan = parse_mayan_value(l, h, lines)
n2arabian = to_arabian_number(n2mayan, NUMBERS)
print("Number 2 is", file=sys.stderr)
print_mayan_number(n2mayan)
# print("In arabian, it means %d"%n2arabian, file=sys.stderr)
operation = input()
full_op = "%d %s %d"%(n1arabian, operation, n2arabian)
print("Operation to perform is %s"%full_op, file=sys.stderr)
result = int(eval(full_op))
print("%s = %d"%(full_op, result), file=sys.stderr)

print(to_mayan_number(result, NUMBERS))
