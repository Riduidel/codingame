from Player import *
import unittest

class InGameTests(unittest.TestCase):
    def load_playground_from(self, playground, players):
        return Playground.load_from(playground, players)
    def test_playground_at_turn_144_time_1591694966226(self):
        playground = self.load_playground_from("""11111111111111111111          
10000000000000000001          
10            111101          
10 00000000000111101          
10 0         0111101          
10 0 0000000 0111101          
10 0 0     0 0111101          
10 0 0 000 0 0111101          
10 0 0 0 0 0 0111101          
10 0 0 0 0 0 0111101          
10 0 0 0 0 0 0111111          
10 0 0 0 0 0 0111 11          
10 0 0 0 0 0 0111111          
10 0 0 0 0 0 0111 11          
10 0 00000 0 0111 11          
10 0       0 0111 11          
10 000000000 0111 11          
10           0111 11          
10000000000000111 11          
11111111111111111 11          
""",
        [Player(0, 7, 13), Player(1, 17, 12)])
        assert playground.doComputeMove(1)=="DOWN"


    def test_playground_at_turn_6_time_1591987044330(self):
        playground = self.load_playground_from("""                              
                              
                              
                              
   1                          
   1                          
   1                          
   1                          
   11                         
  0 1                         
  0                           
  0                           
  0                           
  0                           
  0                           
  0                           
                              
                              
                              
                              
""",
        [Player(0, 2, 9), Player(1, 4, 9)])
        assert playground.doComputeMove(0)!="RIGHT"
