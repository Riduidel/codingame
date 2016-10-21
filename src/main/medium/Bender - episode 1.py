import sys
import math

class Looping(Exception):
	'''raised when bender is looping'''

class Direction:
	def __init__(self, name, x, y):
		self.name = name
		self.x = x
		self.y = y
	def __str__(self):
		return "%s (%d, %d)"%(self.name, self.x, self.y)

class Directions:
	def __init__(self):
		self.ORDER = [
			Direction('SOUTH', 0, 1),
			Direction('EAST', 1, 0),
			Direction('NORTH', 0, -1),
			Direction('WEST', -1, 0),
			]
	def byName(self, name):
		def find_by_name(d):
			return d.name==name
		return filter(find_by_name, self.ORDER)[0]
	def order(self):
		def get_name(d):
			return d.name
		return map(get_name, self.ORDER)

DIRECTIONS = Directions()

def show_playground(playground):
	returned = ""
	for row in playground:
		for cell in row:
			returned+=cell
		returned+='\n'
	return returned

class AbstractAction(object):
	def canTraverse(self, playground, x, y, possibleActions, context):
		return (True, x, y)
	def run_to_suicide(self, playground, previousMoves, possibleActions, context):
		lastMove = previousMoves[-1]
		x = lastMove['x']
		y = lastMove['y']
		lastMoveDirection = lastMove['name']
		return self.run_to_suicide_in_direction(playground, previousMoves, possibleActions, context, x, y, lastMoveDirection)

	def run_to_suicide_in_direction(self, playground, previousMoves, possibleActions, context, x, y, lastMoveDirection):
		direction = DIRECTIONS.byName(lastMoveDirection)
		x1 = x+direction.x
		y1 = y+direction.y
		content = playground[y1][x1]
#		print >> sys.stderr, "Bender is at (%s, %s) heading in %s on '%s'" %(x, y, lastMoveDirection, content)
		action = possibleActions[content]
		(canTraverse, x1, y1) = action.canTraverse(playground, x1, y1, possibleActions, context)
		if canTraverse:
			previousMoves.append({'x':x1, 'y':y1, 'name':lastMoveDirection, 'b':context['beer'], 'i':context['inverted']})
			return action.run_to_suicide(playground, previousMoves, possibleActions, context)
		else:
			# Find an alternative direction
#			print >> sys.stderr, "Position (%d, %d) is unreachable : '%s'"%(x1, y1, content)
			for name in context['ORDER']:
				tested = DIRECTIONS.byName(name)
#				print >> sys.stderr, "Testing direction %s"%(tested)
				x2 = x+tested.x
				y2 = y+tested.y
				action = possibleActions[playground[y2][x2]]
				(canTraverse, x2, y2) = action.canTraverse(playground, x2, y2, possibleActions, context)
				if canTraverse:
					previousMoves.append({'x':x2, 'y':y2, 'name':name, 'b':context['beer'], 'i':context['inverted']})
					return action.run_to_suicide(playground, previousMoves, possibleActions, context)

class OnOpenPath(AbstractAction):
	pass
class OnWall(AbstractAction):
	def canTraverse(self, playground, x, y, possibleActions, context):
		return (False, x, y)
class OnDestructible(AbstractAction):
	def canTraverse(self, playground, x, y, possibleActions, context):
		if context['beer']:
			playground[y][x]=' '
			return (True, x, y)
		else:
			return (False, x, y)
class OnSuicide(AbstractAction):
	def canTraverse(self, playground, x, y, possibleActions, context):
		return (True, x, y)
	def run_to_suicide(self, playground, previousMoves, possibleActions, context):
		def to_s(a, b):
			if isinstance(a, basestring):
				returned = a
			else:
				returned = a['name']
			return returned + "\n"+b['name']
		print >> sys.stderr, "THE END IS NIGH !\n%s" % previousMoves
		# Remove the first element, which is a virtual one
		previousMoves.pop(0)
		return reduce(to_s, previousMoves)
class ReRouteTo(AbstractAction):
	def __init__(self, name):
		self.direction = name
	def canTraverse(self, playground, x, y, possibleActions, context):
		return (True, x, y)
	def run_to_suicide(self, playground, previousMoves, possibleActions, context):
		lastMove = previousMoves[-1]
		x = lastMove['x']
		y = lastMove['y']
		return self.run_to_suicide_in_direction(playground, previousMoves, possibleActions, context, x, y, self.direction)
class Breaker(AbstractAction):
	def run_to_suicide(self, playground, previousMoves, possibleActions, context):
		lastMove = previousMoves[-1]
		x = lastMove['x']
		y = lastMove['y']
		context['beer'] = not context['beer']
		return super(Breaker, self).run_to_suicide(playground, previousMoves, possibleActions, context)
class OnInverter(AbstractAction):
	def canTraverse(self, playground, x, y, possibleActions, context):
		return (True, x, y)
	def run_to_suicide(self, playground, previousMoves, possibleActions, context):
		lastMove = previousMoves[-1]
		x = lastMove['x']
		y = lastMove['y']
		context['ORDER']=context['ORDER'][::-1]
		context['inverted'] = not context['inverted']
#		print >> sys.stderr, "Bender is at (%s, %s) with inverted directions %s" %(x, y, context['ORDER'])
		return super(OnInverter, self).run_to_suicide(playground, previousMoves, possibleActions, context)
class Teleporter(AbstractAction):
	def __init__(self, playground):
		self.teleporters = []
		# Find teleporter locations
		for y, row in enumerate(playground):
			for x, cell in enumerate(row):
				if cell=='T':
					self.teleporters.append((x, y))
		if self.teleporters:
			print >> sys.stderr, "Found teleporters : %s" %(self.teleporters)
				
	def canTraverse(self, playground, x, y, possibleActions, context):
		position = self.teleporters.index((x, y))
		teleported = self.teleporters[(position+1)%len(self.teleporters)]
		return (True, teleported[0], teleported[1])
	def run_to_suicide(self, playground, previousMoves, possibleActions, context):
		lastMove = previousMoves[-1]
		x = lastMove['x']
		y = lastMove['y']
		return super(Teleporter, self).run_to_suicide(playground, previousMoves, possibleActions, context)

#######################################################################################################
######################################### main code loop ##############################################
#######################################################################################################
height,width = [int(i) for i in raw_input().split()]
print >> sys.stderr, "playground is of height %d and width %d" %(height,width)
playground = []
# current position of bender
x = -1
y = -1
for i in xrange(height):
	row = raw_input()
	line = list(row)
	try:
		x = line.index('@')
		y = i
	except:
		pass
	playground.append(line)

print >> sys.stderr, show_playground(playground)
# now navigate the graph
try:
	moves = OnOpenPath().run_to_suicide(playground, 
		[{'x':x, 'y':y, 'name':'SOUTH'}],
		{ # ACTIONS
		'#':OnWall(),
		'X':OnDestructible(),
		' ':OnOpenPath(),
		'@':OnOpenPath(),
		'S':ReRouteTo('SOUTH'),
		'E':ReRouteTo('EAST'),
		'N':ReRouteTo('NORTH'),
		'W':ReRouteTo('WEST'),
		'B':Breaker(),
		'I':OnInverter(),
		'T':Teleporter(playground),
		'$':OnSuicide()
		},
		{ # CONTEXT
		'ORDER':DIRECTIONS.order(),
		# When having a beer, bender should destroy X like butter (yeah, X are transformed on the way into ' ')
		'beer':False,
		'inverted':False
		}
		)
	print >> sys.stderr, "After navigation\n%s"%show_playground(playground)
	print moves
except:
	print "LOOP"
