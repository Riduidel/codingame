import sys
import math
import time

# Only prepare actions ten turns in advance
PREDICTION_HORIZON=10
# =====================================================
def current_time_millis():
    return int(round(time.time() * 1000))
class Delay:
    def __init__(self):
        super().__init__()
        self.start = current_time_millis()
    def isElapsed(self, delay):
        return current_time_millis()-self.start>delay
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
    def distance1To(self, p):
        return abs(self.x-p.x)+abs(self.y+p.y)

class Direction(Point):
    def __init__(self, name, x, y):
        super().__init__(x, y)
        self.name = name
    def move(self, point):
        return Point(point.x+self.x, point.y+self.y)
    def __str__(self):
        return "Direction(%s; %d; %d)"%(self.name, self.x, self.y)

DIRECTIONS = [
            Direction("UP", 0, -1),
            Direction("DOWN", 0, 1),
            Direction("LEFT", -1, 0),
            Direction("RIGHT", 1, 0)
        ]

# =====================================================
class Available:
    def allow(self):
        return True
    def toDebug(self):
        return " "
    def __eq__(self, other):
        return isinstance(other, Available)


# =====================================================
class Player(Point):
    """Effective, real, immediate representation of a player"""
    def __init__(self, playerIndex, x, y):
        super().__init__(x, y)
        self.index = playerIndex
    def __str__(self):
        return "Player(%d; %d; %d)"%(self.index, self.x, self.y)
    def allow(self):
        return False
    def toDebug(self):
        return "%d"%self.index
    def move(self, direction):
        return Player(self.index, direction.x+self.x, direction.y+self.y)
    def trace(self):
        """Convert this player to a trace"""
        return PlayerTrace(self.index, self.x, self.y)
class PlayerTrace(Point):
    """Memory of a player positions"""
    def __init__(self, playerIndex, x, y):
        super().__init__(x, y)
        self.index = playerIndex
    def __str__(self):
        return "PlayerTrace(%d; %d; %d)"%(self.index, self.x, self.y)
    def allow(self):
        return False
    def toDebug(self):
        return "%d"%self.index
    def move(self, direction):
        return PlayerTrace(self.index, direction.x+self.x, direction.y+self.y)

# =====================================================
class Playground:
    GRID_WIDTH=30
    GRID_HEIGHT=20
    TOTAL_CELLS=GRID_WIDTH*GRID_HEIGHT
    # =====================================================
    def load_from(playground, players, width=GRID_WIDTH, height=GRID_HEIGHT):
        """Load this playground object from infos provided in arguments"""
        returned = Playground(width=width, height=height)
        x = 0
        y = 0
        for c in playground:
            if c=='\n':
                y+=1
                x=0
            else:
                if c!=' ':
                    returned.memory[x][y] = PlayerTrace(int(c), x, y)
                x+=1
        returned.players = players
        return returned
    # =====================================================
    def __init__(self, width=GRID_WIDTH, height=GRID_HEIGHT, simulation = False):
        super().__init__()
        self.width = width
        self.height = height
        self.simulation = simulation
        self.players = []
        self.memory = []
        for i in range(width):
            column = []
            for j in range(height):
                column.append(Available())
            self.memory.append(column)

    def clear(self):
        self.players = []
    def setPlayer(self, playerIndex, x0, y0, x, y):
        """Store the given player and all associated informations.
        Notice we distinguish between the active player and its trace:
        Player object is set in the players list, and player trace is dropped on memory
        """
        p = PlayerTrace(playerIndex, x, y)
        self.players.append(Player(playerIndex, x, y))
        self.memory[x0][y0]=p
        self.setPlayerTrace(p)
    def setPlayerTrace(self, trace):
        self.memory[trace.x][trace.y]=trace
    # =====================================================
    def copy(self, player):
        """Creates a full copy of this playground: both memory and players are copied, excepted the player given as argument which replaces the player of same index"""
        returned = Playground(self.width, self.height)
        for i in range(self.width):
            for j in range(self.height):
                returned.memory[i][j]=self.memory[i][j]
        for p in self.players:
            if player and p.index==player.index:
                returned.players.append(player)
            else:
                returned.players.append(p)
        return returned
    # =====================================================
    def toDebug(self):
        playground = ""
        for j in range(self.height):
            for i in range(self.width):
                playground+=self.memory[i][j].toDebug()
            playground+="\n"
        return playground
    def playersToDebug(self):
        returned = ""
        for index,text in enumerate(["Player(%d, %d, %d)"%(p.index, p.x, p.y) for p in self.players]):
            if index>0:
                returned += ", "
            returned += text
        return "["+returned+"]"
    def generateDebug(self, turn, myPlayerIndex, effectiveMove):
        """Generates a Python test that will reproduce this exact instant of game"""
        return """
    def test_playground_at_turn_%d_time_%d(self):
        playground = self.load_playground_from(\"\"\"%s\"\"\",
        %s)
        assert playground.doComputeMove(%d)!=\"%s\"
        """%(turn, current_time_millis(), self.toDebug(), self.playersToDebug(), myPlayerIndex, effectiveMove)
    # =====================================================
    def allow(self, point):
        """Generic allowance function.
        This simply returns true if the position can be used by our player, and false otherwise
        """
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
    def computeMove(self, turn, myPlayerIndex):
        move = self.doComputeMove(myPlayerIndex)
        debug = self.generateDebug(turn, myPlayerIndex, move)
        return (move, debug)
    def getValidDirectionsAt(self, me):
        return [direction for direction in DIRECTIONS if self.allow(me.move(direction))]
    def getNextPointsAt(self, me):
        returned = []
        for d in DIRECTIONS:
            moved = me.move(d)
            if self.allow(moved):
                returned.append(moved)
        return returned
    def doComputeMove(self, myPlayerIndex):
        """Essential method computing all moves of our tron cycle"""
        me = self.players[myPlayerIndex]
        # First, filter ibvoously bad decisions away
        valid = self.getValidDirectionsAt(me)
        if valid:
            # Then, bind an evaluator to each direction
            evaluated = dict((direction.name,self.evaluator(me, direction)) for direction in valid)
            # Now, score each direction according to the number of accessible spots
            evaluated = sorted(evaluated.items(), key=lambda entry: entry[1].evaluate(), reverse=True)
            # And get first result
            return next(iter(evaluated))[0]
        return "DIE"
    def evaluator(self, me, direction):
        """Create an evaluator for the given direction.
        First version simply evaluates the number of cells reachable from the given direction
        """
        moved = me.move(direction)
        return CombiningEvaluator([
            CountCellsEvaluator(self.copy(moved), moved)
            ])

# =====================================================
class CombiningEvaluator:
    """Combine multiple evaluators into a uique metric"""
    def __init__(self, evaluators):
        self.evaluators = evaluators
        self.score = -1
    def evaluate(self):
        if self.score<0:
            self.score = 0
            size = len(self.evaluators)
            for index, e in enumerate(self.evaluators):
                # THis little hack allows first evaluator to have numeric priority over second
                self.score += (size-index)*e.evaluate()
        return self.score
class CountCellsEvaluator:
    """Gives the direction with the most accessible cells a hgher score"""
    def __init__(self, playground, me):
        self.playground = playground
        self.me = me
        self.count=-1
    def evaluate(self):
        """Count number of reachable cells by simply trying to navigate them recursively from initial position given by self.me"""
        if self.count<0:
            self.count = self.evaluate_at(self.me.trace())
        return self.count/Playground.TOTAL_CELLS
    def evaluate_at(self, me):
        """Evaluate this position in the following way
        1. list each navigable direction
        2. For each one, recursively call this method
        3. Add the score obtained and returns it
        """
        self.playground.setPlayerTrace(me)
        valid = self.playground.getValidDirectionsAt(me)
        # Count is set at 1 because we can go on this spot!
        count = 1
        for direction in valid:
            # We call the move on the PlayerTrace object to get a new PlayerTrace object
            futureMe = me.move(direction)
            count += self.evaluate_at(futureMe)
        return count

# =====================================================
if __name__ == '__main__':
    # At game beginning, we define a playground
    playground = Playground()
    turn = 0
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
            playground.setPlayer(i, x0, y0, x1, y1)
        # =====================================================

        command, debug = playground.computeMove(turn, myPlayerIndex)
        # Write an action using print
        print(debug, file=sys.stderr)

        # A single line with UP, DOWN, LEFT or RIGHT
        print(command)

        turn = turn + 1
