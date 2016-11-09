import sys
import math

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
			return self.id==other.id and self.y==other.y
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
		print("connected %s to %s"%(first, second), file=sys.stderr)
	
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
def paths_ending_with(node_matching, nodes_paths, deepness = ""):
	next_paths = []
	returned = []
	for path in nodes_paths:
#		print("Testing path %s"%(path), file=sys.stderr)
		path_ending = path[-1]
		if node_matching(path_ending):
			returned.append(path)
		else:
			for node in path_ending.edges:
				if not node in path:
					new_path = []
					new_path.extend(path)
					new_path.append(node)
					next_paths.append(new_path)
	if returned:
		return returned
	else:
		return paths_ending_with(node_matching, next_paths, deepness+"\t")
def measure(path):
	returned = 0
	for n in path:
		returned = returned + len(n.edges)
	return returned

	
def unplug(graph, to_unplug):
	"""To unplug that node, we have first to find all paths to exit nodes with their length, 
	then break the first link in the shorter path"""
	def can_exit(node):
		return "exit" in node.properties and node.properties["exit"]
	paths = paths_ending_with(can_exit, [[to_unplug]])
	shortest = sys.maxsize
	to_remove = None
	for p in paths:
		value = measure(p)
		if value<shortest:
			shortest = value
			to_remove = p
	print("Shortest path to exit from %s is\n%s"%(to_unplug, to_remove), file=sys.stderr)
	if to_remove:
		first, second = (to_remove[0], to_remove[1])
		first.disconnect(second)
		return "%d %d"%(first.id, second.id)
	return "oo fuck"
		
# n: the total number of nodes in the level, including the gateways
# l: the number of links
# e: the number of exit gateways
g = Graph()
n, l, e = [int(i) for i in input().split()]
for i in range(l):
	# n1: N1 and N2 defines a link between these nodes
	n1, n2 = [int(j) for j in input().split()]
	g.connect(n1, n2)
for i in range(e):
	ei = int(input())  # the index of a gateway node
	g.find(ei).properties["exit"]=True
	print("%s is an exit node"%ei, file=sys.stderr)

# game loop
while True:
	si = int(input())  # The index of the node on which the Skynet agent is positioned this turn

	to_unplug = g.find(si)
	
	print(unplug(g, to_unplug))