import sys
import math
import re
from functools import reduce

def path(text):
	return text.split("/")
class Flags(object):
	def __init__(self, text):
		self.all  = "a" in text
		self.directories = "d" in text
		if "L" in text:
			pattern = re.compile("L ([0-9]+)")
			result = pattern.search(text)
			if result:
				self.limit = int(result.group(1))
			else:
				self.limit = sys.maxsize
		else:
			self.limit = sys.maxsize
		print("Showing all files ? %s. Showing only directories ? %s. Limit ? %d"%(self.all, self.directories, self.limit), file=sys.stderr)

class Counter(object):
	def __init__(self):
		self.dir = 0
		self.file = 0
	def add(self, entry):
		if entry.is_file():
			self.file = self.file+1
		else:
			self.dir = self.dir+1
	def report_for(self, flags):
		if self.dir==1:
			text = "%d directory"%(self.dir)
		else:
			text = "%d directories"%(self.dir)
		if not flags.directories:
			if self.file==1:
				text += ", %d file"%self.file
			else:
				text += ", %d files"%self.file
		return text

class Entry(object):
	def __init__(self, name = '.'):
		self.name = name
		self.children = {}

	def build_hierarchy_using(self, nodes):
		if nodes:
			node = nodes.pop(0)
			if not node in self.children:
				self.children[node]=Entry(node)
			self.children[node].build_hierarchy_using(nodes)

	def build_hierarchy_from(self, line):
		nodes = path(line)
		if nodes[0]==self.name:
			nodes.pop(0)
		self.build_hierarchy_using(nodes)

	def __repr__(self):
		return "%s"%self.name

	def __eq__(self, other):
		if isinstance(other, self.__class__):
			return self.name==other.name
		else:
			return False
	def __hash__ (self):
		return self.name.__hash__()
	def tree_elements(self, flags):
		def valid(element):
			if flags.directories:
				if element.is_file():
					return False
			return not element.is_hidden()
		if flags.all and not flags.directories:
#			print("Returning all elements %s"%self.children.values(), file=sys.stderr)
			return self.children.values()
		return list(filter(valid, self.children.values()))

	def tree_in(self, counter, flags, prefix = []):
		def sorting_order(element):
			text = element.name.lower()
#			print("Getting %s name"%element.name, file=sys.stderr)
			if element.is_hidden():
#				print("HIDDEN name %s"%element.name[1:-1], file=sys.stderr)
				return text[1:]
			return text
		index = 0
		returned = ""
		if len(prefix)<flags.limit:
			elements = self.tree_elements(flags)
			for value in sorted(elements, key=sorting_order):
				counter.add(value)
				try:
					last = index==len(elements)-1
					if last:
						prefix.append("`-- ")
					else:
						prefix.append("|-- ")
					returned += "\n"+''.join(prefix)+value.name
					prefix.pop()
					if last:
						prefix.append("    ")
					else:
						prefix.append("|   ")
					returned+=value.tree_in(counter, flags, prefix)
				finally:
					prefix.pop()
				index = index+1
		return returned

	def tree_from(self, nodes, path, flags):
		counter = Counter()
		if nodes:
			name = nodes.pop(0)
			if name in self.children and not self.children[name].is_file():
				return self.children[name].tree_from(nodes, path, flags)
			else:
				returned = "%s [error opening dir]"%path
				returned +="\n\n"+counter.report_for(flags)
				return returned
		else:
			returned = path+self.tree_in(counter, flags)
			returned +="\n\n"+counter.report_for(flags)
			return returned

	def tree(self, pathname, flags):
		nodes = path(pathname)
		if nodes[0]==self.name:
			nodes.pop(0)
		return self.tree_from(nodes, pathname, flags)
	def is_file(self):
		return not self.children
	def is_hidden(self):
		return self.name.startswith(".")

target = input()
flags = input()
files = int(input())

print("testing tree in \"%s\" with flags \"%s\""%(target, flags), file=sys.stderr)
root = Entry()
for i in range(files):
	line = input()
#	print("Adding %s"%(line), file=sys.stderr)
	root.build_hierarchy_from(line)

# Write an action using print
# To debug: print("Debug messages...", file=sys.stderr)

print(root.tree(target, Flags(flags)))
