import sys
import math
from operator import itemgetter, attrgetter

# Range of our detector
RANGE = 5

class Point(object):
	def __init__(self, x, y):
		self.x = x
		self.y = y
	def move_in(self, direction):
		x1 = int(self.x+direction.x)
		y1 = int(self.y+direction.y)
		return Point(x1, y1)
	def __eq__(self, other):
		if isinstance(other, self.__class__):
			return self.x==other.x and self.y==other.y
		else:
			return False
	def value_of(self, playground):
		return playground[self.y][self.x]
	def set_value_of(self, playground, value):
		playground[self.y][self.x]=value
	def can_be_in(self, playground):
		if self.y<0 or self.y>=len(playground):
			return False
		if self.x<0 or self.x>=len(playground[0]):
			return False
		return True
	def __repr__(self):
		return "(%d, %d)"%(self.x, self.y)
	def __hash__ (self):
		return 245*self.y+self.x
class Direction(Point):
	def __init__(self, x, y, move):
		super(Direction, self).__init__(x, y)
		self.move = move
	def __repr__(self):
		return "(%d, %d, move in %s)"%(self.x, self.y, self.move)
	def move_in(self, direction):
		x1 = int(self.x+direction.x)
		y1 = int(self.y+direction.y)
#		print("Moving %s in %s, which result in (%d, %d)"%(self, direction, x1, y1), file=sys.stderr)
		return Direction(x1, y1, move)
		

DIRECTIONS = [
	Direction(x=0, y=-1, move='UP'),
	Direction(x=1, y=0, move='RIGHT'),
	Direction(x=-1, y=0, move='LEFT'),
	Direction(x=0, y=1, move='DOWN'),
	]

class Agent(object):
	class Move(object):
		def __init__(self):
			pass
		
	class Strategy(object):
		def sort_directions_by_score(self, agent, playground):
			'''Find the direction which will make us discover the more empty zone'''
			scored = []
			for d in DIRECTIONS:
				on = d.move_in(agent.position())
				print("\nTesting direction %s"%(d), file=sys.stderr)
				on.move = d.move
				scored.append((on, self.score(on, playground)))
			scored = list(sorted(scored, key=itemgetter(1), reverse=True))
			return scored
		def move(self, agent, playground):
			scored = self.sort_directions_by_score(agent, playground)
			print("Scored directions are %s"%(scored), file=sys.stderr)
			# first [0] get list element, second [0] get tuple element
			return scored[0][0]
		def mutate_on(self, point):
			'''Mutate strategy on a given point'''
			return self
		def locate_explorable_points(self, playground, point, known_explorable):
			return []
		def can_move_from(self, point):
			return True
	class MoveTo(Strategy):
		def initialize_heatmap_from(self, playground):
			self.MAX_VALUE = len(playground)*len(playground[0])+1
			returned = []
			for y in playground:
				row = []
				for x in y:
					row.append(self.MAX_VALUE)
				returned.append(row)
			return returned
		def snapshot_directions_of(self, playground, heatmap, point, range, points = [], deepness = 0, show_log_in_snapshot = False):
#			print("--"*(RANGE-(range-deepness))+"Testing directions around point %s for strategy %s"%(point, self), file=sys.stderr)
			for d in DIRECTIONS:
				moved = point.move_in(d)
				self.snapshot(playground=playground, 
					heatmap=heatmap, 
					point=moved, 
					range=range, 
					points=points, 
					deepness=deepness+1,
					show_log = show_log_in_snapshot)
		def snapshot(self, playground, heatmap, point, range, points = [], deepness = 0, show_log = False):
			if range>0 and deepness>range:
				pass
			elif point in points:
				pass
			else:
				if point.can_be_in(playground):
					points.append(point)
					content = point.value_of(playground)
#					print("--"*(RANGE-(range-deepness))+"Testing point %s having for content '%s'"%(point, content), file=sys.stderr)
					if content=='#':
						return
					elif content=='?':
						return
					previous = point.value_of(heatmap)
					if previous>deepness:
						point.set_value_of(heatmap, deepness)
						self.snapshot_directions_of(playground=playground, 
							heatmap=heatmap, 
							point=point, 
							range=range, 
							points=points, 
							deepness=deepness,
							show_log_in_snapshot = show_log)
					points.remove(point)
				
		def create_heatmap(self, playground, destination, range):
			heatmap = self.initialize_heatmap_from(playground)
			self.snapshot(
				playground=playground, 
				heatmap=heatmap, 
				point=destination,
				range = range)
			return heatmap
		def __init__(self, playground, destination, range = -1):
			self.destination = destination
			# At strategy build, we use playground to create a heat map that we will use afterwards to move in the "good" direction
			self.heatmap = self.create_heatmap(playground, destination, range)
#			print("Created %s with heatmap\n%s"%(self, self.show_heatmap()), file=sys.stderr)

		def score(self, point, playground):
			# invert values to have values going up from the far objects to the near ones
			print("Point %s is scored %d"%(point, point.value_of(self.heatmap)), file=sys.stderr)
			return point.value_of(self.heatmap)*-1
		def show_heatmap(self):
			returned =""
			for y in self.heatmap:
				for x in y:
					returned+="\t%d,"%x
				returned+="\n"
			return returned
		def __str__(self):
			return "MoveTo %s"%(self.destination)
		def extend(self, playground, source, show_log_in_extend=False):
			'''Extend heatmap from source to destination'''
			self.snapshot_directions_of(
				playground=playground, 
				heatmap=self.heatmap, 
				point=source, 
				range=RANGE+source.value_of(self.heatmap), 
				points=[source], 
				deepness= source.value_of(self.heatmap),
				show_log_in_snapshot = False)
			if show_log_in_extend:
				print("%s heatmap is\n%s"%(self, self.show_heatmap()), file=sys.stderr)
		def mutate_on(self, point):
			'''When destination is reached, restart a lookup'''
			if point==self.destination:
				print("Destination %s reached ! Going back to lookup mode"%self.destination, file=sys.stderr)
				return Agent.Lookup()
			return self
		def move(self, agent, playground):
			print("moving using %s heatmap\n%s"%(self, self.show_heatmap()), file=sys.stderr)
			return super(Agent.MoveTo, self).move(agent, playground)
		def can_move_from(self, point):
			return point.value_of(self.heatmap)<self.MAX_VALUE
	class Lookup(Strategy):
		def move(self, agent, playground):
			scored = self.sort_directions_by_score(agent, playground)
			print("Scored directions are %s"%(scored), file=sys.stderr)
			# first [0] get list element, second [0] get tuple element
			best_direction = scored[0]
			if best_direction[1]==0:
				# We're in a dead-end, so get latest unexplored location, and move into that direction
				agent.strategy = agent.fallbacks.pop()
				while not agent.strategy.can_move_from(agent.position()) and len(agent.fallbacks)>2:
					agent.strategy = agent.fallbacks.pop()
				print("In a dead-end. Moving to %s\nheatmap is\n%s"%(agent.strategy, agent.strategy.show_heatmap()), file=sys.stderr)
				return agent.strategy.move(agent, playground)
			else:
				return scored[0][0]
		def score(self, point, playground, deepness = 0, points=[]):
			'''recursively score all points until deepness is reached. And never score the same point twice'''
			returned = 0
			MAX_DEEPNESS = RANGE
			bonus = MAX_DEEPNESS-deepness
			if deepness<MAX_DEEPNESS:
				if point in points:
					pass
				else:
					if point.can_be_in(playground):
						points.append(point)
						content = point.value_of(playground)
						if content=='#':
							return returned
						elif content=='?':
							returned+=2*bonus*bonus
						elif content=='C':
							returned+=10*bonus*bonus
							print("--"*deepness+"command center %s is scored %s"%(point, returned), file=sys.stderr)
						for d in DIRECTIONS:
							moved = point.move_in(d)
							returned += self.score(moved, playground, deepness+1)
						points.remove(point)
			return returned
		def __str__(self):
			return "Lookup"
		def locate_explorable_points(self, playground, point, known_explorable, deepness = 0, points=[]):
			returned = set()
			MAX_DEEPNESS = RANGE
			if deepness<MAX_DEEPNESS:
				if point in points:
					pass
				elif point in known_explorable:
					pass
				elif point in returned:
					pass
				else:
					if point.can_be_in(playground):
						points.append(point)
						content = point.value_of(playground)
						if content=='#':
							return returned
						elif content=='?':
							# do not add point itself, but its parent
							returned.add(points[-2])
						for d in DIRECTIONS:
							moved = point.move_in(d)
							returned.update(self.locate_explorable_points(playground, moved, known_explorable, deepness+1, points))
						points.remove(point)
			return returned
	def __init__(self):
		self.positions = []
		self.strategy = Agent.Lookup()
		self.fallbacks = []
		self.back_to_base = None
	
	def set_on(self, x, y):
		self.positions.append(Point(x, y))
	
	def move_on(self, playground):
		return self.strategy.move(self, playground)
	
	def scan(self, playground, move):
		if move:
			if move.value_of(playground)=='C':
				print("We found the control room ! Going back", file=sys.stderr)
				self.strategy = self.fallbacks.pop(0)
			else:
				# Check if end of moveto strategy reached
				self.strategy = self.strategy.mutate_on(move)
				# First of all, filter fallbacks which lead to known locations
				def remove_discovered(move_to):
					return move_to.destination.value_of(playground)!="."
#				self.fallbacks = list(filter(remove_discovered, self.fallbacks))
				# And adding "explorable" locations
				def find_destination(strategy):
					return strategy.destination
				known_explorable = list(map(find_destination, self.fallbacks))
#				print("Old fallback locations are %s"%(known_explorable), file=sys.stderr)
				new_explorable = self.strategy.locate_explorable_points(playground, move, known_explorable)
				if new_explorable:
					for p in new_explorable:
						self.fallbacks.append(Agent.MoveTo(playground, p, RANGE))
					print("New fallback locations are %s"%(new_explorable), file=sys.stderr)

				# Moving from one lookup point to another just imply diving deeper in the graph
				self.fallbacks[0].extend(playground, self.positions[-2])
				self.fallbacks[-1].extend(playground, self.positions[-2])
		else:
			# if no move is given, it's the first turn, and Kirk should be on start position (crazy, indeed)
			self.fallbacks.append(Agent.MoveTo(playground, self.position()))
	
	def __str__(self):
		return "%s applying strategy %s"%(self.position(), self.strategy)
	def position(self):
		return self.positions[-1]
		

# r: number of rows.
# c: number of columns.
# a: number of rounds between the time the alarm countdown is activated and the time the alarm goes off.
r, c, a = [int(i) for i in input().split()]

kirk = Agent()
move = None
# game loop
turn = 0
while True:
	turn=turn+1
	playground = []
	# kr: row where Kirk is located.
	# kc: column where Kirk is located.
	kr, kc = [int(i) for i in input().split()]
	kirk.set_on(kc, kr)
	print("Kirk is %s"%kirk, file=sys.stderr)
	for l in range(r):
		input_row = input()  # C of the characters in '#.TC?' (i.e. one line of the ASCII maze).
		print(input_row, file=sys.stderr)
		playground.append(list(input_row))
	kirk.scan(playground, move)

	if turn>100:
		raise ValueError("no, that is too long !")
	move = kirk.move_on(playground)
	print(move.move)
