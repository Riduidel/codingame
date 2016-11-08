import sys
import math
from functools import reduce

def show(playground):
	def to_s(row):
		return ''.join(str(e) for e in row)
	def to_row(a, b):
		return a+"\n"+b
	return reduce(to_row, map(to_s, playground))

class Point(object):
	def __init__(self, x, y):
		self.x = x
		self.y = y
	def move(self, other):
		return Point(self.x+other.x, self.y+other.y)
	def get(self, storage):
		return storage[self.y][self.x]
	def is_in(self, storage):
		if self.y>=0 and self.y<len(playground):
			if self.x>=0 and self.x<len(playground[self.y]):
				return True
		return False
	def set(self, storage, value):
		if self.is_in(storage):
			storage[self.y][self.x] = value
	def __eq__(self, other):
		if isinstance(other, Point):
			return self.x==other.x and self.y==other.y
	def __repr__(self):
		return "(%d, %d)"%(self.x, self.y)
	def __eq__(self, other):
		if isinstance(other, self.__class__):
			return self.x==other.x and self.y==other.y
		else:
			return False
	def __hash__ (self):
		return 245*self.y+self.x
NAVIGABLE_DIRECTIONS = [
	Point(1, 0),
	Point(-1, 0),
	Point(0, 1),
	Point(0, -1)
]		

VIEW_DIRECTIONS = [
	Point(1, 1),
	Point(1, -1),
	Point(-1, -1),
	Point(-1, 1)
]
VIEW_DIRECTIONS.extend(NAVIGABLE_DIRECTIONS)

def init_flood(width, height):
	returned = []
	for i in range(0, height):
		row = []
		for j in range(0, width):
			row.append(False)
		returned.append(row)
	return returned
def load_borders_in(playground, flood, width, height):
	def load_elements_of(x_range, y_range):
		returned = set()
		for x in x_range:
			for y in y_range:
				p = Point(x, y)
				if p.get(playground)=='.':
					p.set(flood, True)
					returned.add(p)
		return returned
	returned = set()
	returned.update(load_elements_of(range(0, width), [0, height-1]))
	returned.update(load_elements_of([0, width-1], range(0, height)))
	return returned
def has_building_nearby(playground, point):
	for d in VIEW_DIRECTIONS:
		moved = point.move(d)
		if moved.is_in(playground):
			if moved.get(playground)=='B':
#				print("Point %s has building nearby ? True)"%(point), file=sys.stderr)
				return True
#	print("Point %s has building nearby ? False)"%(point), file=sys.stderr)
	return False
def fill_and_find_borders(playground, flood, width, height, known_points):
	print("Filling from points %s)"%(known_points), file=sys.stderr)
	returned = set()
	next_points = set()
	for point in known_points:
		# First check if there are buildings nearby, and if so, add point to returned list
		if has_building_nearby(playground, point):
			returned.add(point)
		for d in NAVIGABLE_DIRECTIONS:
			move = point.move(d)
			if move.is_in(playground):
				if move.get(playground)=='.':
					if not move.get(flood):
						move.set(flood, True)
						next_points.add(move)
	if next_points:
		returned.update(fill_and_find_borders(playground, flood, width, height, next_points))
	return returned
def zerg_rush(playground, width, height):
	flood = init_flood(width, height)
	# Bootstrap flood with border nodes
	border = load_borders_in(playground, flood, width, height)
	zerg_locations = fill_and_find_borders(playground, flood, width, height, border)
	print("Zerg locations are %s)"%(zerg_locations), file=sys.stderr)
	for p in zerg_locations:
		p.set(playground, 'z')
	return playground

w, h = [int(i) for i in input().split()]
playground = []
for i in range(h):
	playground.append(list(input()))

playground = zerg_rush(playground, w, h)
print(show(playground))