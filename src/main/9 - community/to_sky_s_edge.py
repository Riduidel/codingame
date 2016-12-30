import sys
import math


from enum import Enum

class Destiny(Enum):
	everybody_die = 1
	survive = 2
	overcrowded = 3

class Spaceship:
	def __init__(self, expect):
		self.population = []
		self.expectancy = expect
	def add_group(self, age, number):
		self.population.append((age, number))
	
	def __repr__(self):
		returned = ""
		if self.population:
			for p in self.population:
				returned+= "%d aged %d, "%(p[1], p[0])
		else:
			returned+="Everybody died !"
		return returned
	def one_year_passed(self):
		def find_parents(pop):
			def good_age(p):
				return p[0]>=20 and p[0]<=int(self.expectancy/2)
			return list(filter(good_age, pop))
		next = []
		# age each passenger one more year
		for p in self.population:
			next.append((p[0]+1, p[1]))
		def kill_olders(tuple):
			return tuple[0]<=self.expectancy
		# kill the ones too old
		next = list(filter(kill_olders, next))
		# and add newborn
		parents = find_parents(next)
		c = self.count(parents)
#		print("parents are %s. There are %s parents"%(parents, c))
		if c>=10:
			next.append((0, int(c/10)))
		self.population = next
	def count(self, pop = None):
		if pop is None:
#			print("NO POP : pop=%s"%pop)
			pop = self.population
		returned = 0
		for p in pop:
			returned+=p[1]
		return returned

class GenerationFinder:
	def __init__(self, duration, capacity, population):
		self.duration = duration
		self.population = population
		self.capacity = capacity
	
	def get_destiny_for(self, expectancy):
		s = Spaceship(expectancy)
		s.population = self.population
		for age in range(0, self.duration):
			s.one_year_passed()
			c = s.count()
			if c==0:
				return Destiny.everybody_die
			elif c>self.capacity:
				return Destiny.overcrowded
		c = s.count()
#		print("for expectancy %s, %s colons arrived" %(expectancy, c))
		if c<200:
			return Destiny.everybody_die
		return Destiny.survive
	
	def find_range(self, lower = 40, upper=200):
		def found(dict, lower, upper):
			return (testable.get(lower)==Destiny.survive 
				and testable.get(lower-1)==Destiny.everybody_die 
				and testable.get(upper+1)==Destiny.overcrowded
				and testable.get(upper)==Destiny.survive)
		testable = dict.fromkeys(list(range(lower, upper)))
		extend = (upper-lower)/2
		while not found(testable, lower, upper):
			lower, upper = self.find_survivors_in(testable, lower, upper, extend)
			extend = max(int(extend/2), 1)
#			if extend<=1:
#				raise ValueError("fuck")
		return (lower, upper)

	def find_survivors_in(self, dict, lower, upper, extend = None):
#		print("lower=%s, upper=%s, extend=%s"%(lower, upper, extend))
		if extend is None:
			extend = upper-lower
		def move(value):
			if dict.get(value)==Destiny.everybody_die:
				value = value + extend
			if dict.get(value)==Destiny.overcrowded:
				value = value - extend
			return int(value)
		if not dict.get(lower):
			dict[lower]=self.get_destiny_for(lower)
		if not dict.get(upper):
			dict[upper]=self.get_destiny_for(upper)
		lower = move(lower)
		upper = move(upper)
#		print(dict)
		if dict.get(lower)==Destiny.survive and not dict.get(lower-1)==Destiny.everybody_die:
			lower = lower-1
		if dict.get(upper)==Destiny.survive and not dict.get(upper+1)==Destiny.overcrowded:
			upper = upper+1
		lower = max(lower, 0)
		upper = min(upper, len(dict))
		return (lower, upper)

duration = int(input())
capacity = int(input())
n = int(input())
population = []
for i in range(n):
	age, number = [int(j) for j in input().split()]
	population.append((age, number))

g = GenerationFinder(duration, capacity, population)
result = g.find_range()
print("%s %s"%(result[0], result[1]))
"""

population = [
(5 , 40),
(10, 50),
(15, 30),
(20, 40),
(25, 30),
(30, 50),
(35, 50),
(40, 30),
(45, 40),
(50, 30)
]

g = GenerationFinder(80, 10000, population)
result = g.find_range()
print("%s %s"%(result[0], result[1]))
"""
