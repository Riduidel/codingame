import sys
import math
import operator

class Node(object):
	def __init__(self, id):
		self.id = id
		self.edges = set()
		self.properties = {}
	
	def connect(self, node):
		self.edges.add(node)
		node.edges.add(self)
	def disconnect(self, node):
		self.edges.remove(node)
		node.edges.remove(self)
	
	def __eq__(self, other):
		if isinstance(other, self.__class__):
			return self.id==other.id
		else:
			return False
	def __repr__(self):
		return "Node %d %s"%(self.id, self.properties)
	def __hash__ (self):
		return self.id

class Graph(object):
	def __init__(self):
		self.nodes = set()
	def connect(self, first_id, second_id):
		first = self.find_or_create(first_id)
		second = self.find_or_create(second_id)
		first.connect(second)
#		print("connected %s to %s"%(first, second), file=sys.stderr)
	
	def find_or_create(self, id):
		found = self.find(id)
		if not found:
			found = self.create(id)
		return found
	
	def find(self, id):
		for n in self.nodes:
			if n.id==id:
				return n
		return None
	
	def create(self, id):
		node = Node(id)
		self.nodes.add(node)
		return node

def find_short_paths_between(start, end, paths = None):
	"""Find all the paths of the shortest distance between start and end"""
	if not paths:
		paths = [[start]]
	returned = []
	if start==end:
		return paths
	if not start.edges or not end.edges:
		return returned
	new_paths = []
#	print("Current path length %d"%len(paths[0]), file=sys.stderr)
	for p in paths:
		ending = p[-1]
#		print("Testing path %s"%(p), file=sys.stderr)
		if ending==end:
#			print("Found an exit path", file=sys.stderr)
			returned.append(p)
		else:
			for n in ending.edges:
				if not n in p and not "exit" in n.properties:
					next = []
					next.extend(p)
					next.append(n)
					new_paths.append(next)
	if returned:
#		print("We have exit paths %s"%returned, file=sys.stderr)
		return returned
	else:
		if new_paths:
			return find_short_paths_between(start, end, new_paths)
		else:
			return returned

def compute_safety_duration_for(path):
	duration = 0
	for node in path:
		increment = 1
		for output in node.edges:
			if "exit" in output.properties:
				increment = increment-1
		duration = duration + increment
#	print("Safety of %s is %s"%(path, duration), file=sys.stderr)
	return duration

def find_best_pair(paths):
	nodes_to_gateways = {}
	best_node = None
	for p in paths:
		# p[-1] is the exit node, so we don't use it at all
		# p[-2] is the last non exit node we must use as key
		last_node = p[-2]
		if not last_node in nodes_to_gateways:
			nodes_to_gateways[last_node]={'length':sys.maxsize, 'exits':set()}
		nodes_to_gateways[last_node]['exits'].add(p[-1])
		nodes_to_gateways[last_node]['count']=len(nodes_to_gateways[last_node]['exits'])
		nodes_to_gateways[last_node]['safety']=compute_safety_duration_for(p[0:len(p)-1])
		nodes_to_gateways[last_node]['length']=min(len(p), nodes_to_gateways[last_node]['length'])
	# Now we have a full hash, sort it by measure
	if not nodes_to_gateways:
		raise ValueError("Hell, no exit found")
	nodes = sorted(nodes_to_gateways, key=lambda k:nodes_to_gateways[k]['length'])
	for n in nodes:
		print("node %s is linked to %s"%(n, nodes_to_gateways[n]), file=sys.stderr)
	# best node should be the last node before reaching exit node
	best_node = nodes[0]
	print("Suppsed best node %s"%best_node, file=sys.stderr)
	# replacing best_node by an even better one if necessary
	if nodes_to_gateways[best_node]['length']>2:
		nodes = sorted(nodes_to_gateways, key=lambda k:nodes_to_gateways[k]['safety'])
		# we have some time left, so choose **wisely** which node to disconnect
		for n in nodes:
			if nodes_to_gateways[n]['count']>1:
				best_node = n
				break
	return best_node, next(iter(nodes_to_gateways[best_node]['exits']))
def unplug(graph, to_unplug, exits):
	"""To unplug that node, we have first to find all paths to exit nodes with their length, 
	then break the first link in the shorter path"""
	paths = []
	for e in exits:
		for n in e.edges:
			paths_to_ending = find_short_paths_between(to_unplug, n)
			if paths_to_ending:
				for p in paths_to_ending:
					used = list(p)
					used.append(e)
					paths.append(used)
			else:
				pass
	print("There are %d paths in which select the best"%(len(paths)), file=sys.stderr)
	first, second = find_best_pair(paths)
	print("Removing connection %s to %s"%(first, second), file=sys.stderr)
	first.disconnect(second)
	return "%d %d"%(first.id, second.id)
		
# n: the total number of nodes in the level, including the gateways
# l: the number of links
# e: the number of exit gateways
g = Graph()
n, l, e = [int(i) for i in input().split()]
exits = []
for i in range(l):
	# n1: N1 and N2 defines a link between these nodes
	n1, n2 = [int(j) for j in input().split()]
	g.connect(n1, n2)
for i in range(e):
	ei = int(input())  # the index of a gateway node
	exit_node = g.find(ei)
	exit_node.properties["exit"]=True
	exits.append(exit_node)
#	print("%s is an exit node"%ei, file=sys.stderr)

# game loop
while True:
	si = int(input())  # The index of the node on which the Skynet agent is positioned this turn

	to_unplug = g.find(si)
	
	print(unplug(g, to_unplug, exits))