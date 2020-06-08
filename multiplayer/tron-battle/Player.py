import sys
import math

# Auto-generated code below aims at helping you parse
# the standard input according to the problem statement.
GRID_WIDTH=30
GRID_HEIGHT=20
# =====================================================
class Available:
    def allow(self):
        return True
    def toDebug(self):
        return " "
# =====================================================
class Point:
    def __init__(self, x, y):
        super().__init__()
        self.x = x
        self.y = y
    def __str__(self):
        return "Point(%d;%d)"%(self.x, self.y)
    def __eq__(self, other):
        """Makie sure two points at the same position are equals"""
        if isinstance(other, Point):
            return self.x==other.x and self.y==other.y
        return False

class Direction(Point):
    def __init__(self, name, x, y):
        super().__init__(x, y)
        self.name = name
    def move(self, point):
        return Point(point.x+self.x, point.y+self.y)
    def __str__(self):
        return "Direction(%s; %d; %d)"%(self.name, self.x, self.y)
    def directions():
        used = [
            Direction("UP", 0, -1),
            Direction("DOWN", 0, 1),
            Direction("LEFT", -1, 0),
            Direction("RIGHT", 1, 0)
        ]
        returned = dict()
        for d in used:
            returned[d.name]=d
        d

DIRECTIONS = Direction.directions()

class Player(Point):
    def __init__(self, playerIndex, x, y):
        super().__init__(x, y)
        self.index = playerIndex
    def __str__(self):
        return "Player(%d; %d; %d)"%(self.index, self.x, self.y)
    def allow(self):
        return False
    def toDebug(self):
        return "%d"%self.index

class Playground:
    def __init__(self, width, height):
        super().__init__()
        self.width = width
        self.height = height
        self.memory = []
        for i in range(width):
            column = []
            for j in range(height):
                column.append(Available())
            self.memory.append(column)
        print("There are %d columns (height is %d)"%(len(self.memory), self.height), file=sys.stderr)

    def clear(self):
        self.players = []
    def setPlayer(self, playerIndex, x, y):
        p = Player(playerIndex, x, y)
        self.players.append(p)
        self.memory[x][y]=p
    def allow(self, point):
        if(point.x>=0):
            if(point.y>=0):
                if(point.x>=self.width):
                    return False
                else:
                    if(point.y>=self.height):
                        return False
                    else:
                        return self.memory[point.x][point.y].allow()
            else:
                return False
        else:
            return False
    def generateDebug(self, myPlayerIndex):
        """Generates a Python test that will reproduce this exact instant of game"""
        playground = ""
        for j in range(self.height):
            for i in range(self.width):
                playground+=self.memory[i][j].toDebug()
            playground+="\n"
        return playground
    def computeMove(self, myPlayerIndex):
        debug = self.generateDebug(myPlayerIndex)
        me = self.players[myPlayerIndex]
        for direction in DIRECTIONS.items():
            if(self.allow(direction.value.move(me))):
                return (direction.value.name, debug)
        ("DIE", debug)

# =====================================================

if __name__ == '__main__':
    # At game beginning, we define a playground
    playground = Playground(GRID_WIDTH, GRID_HEIGHT)
    # game loop
    while True:
        # =====================================================
        # playersCount: total number of players (2 to 4).
        # myPlayerIndex: your player number (0 to 3).
        playersCount, myPlayerIndex = [int(i) for i in input().split()]
        playground.clear()
        for i in range(playersCount):
            # x0: starting X coordinate of lightcycle (or -1)
            # y0: starting Y coordinate of lightcycle (or -1)
            # x1: starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
            # y1: starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
            x0, y0, x1, y1 = [int(j) for j in input().split()]
            playground.setPlayer(i, x1, y1)
        # =====================================================

        command, debug = playground.computeMove(myPlayerIndex)
        # Write an action using print
        print(debug, file=sys.stderr)

        # A single line with UP, DOWN, LEFT or RIGHT
        print(command)