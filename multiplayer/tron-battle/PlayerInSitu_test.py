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

    def test_playground_at_turn_80_time_1592161981732_support_dead_player_deletion(self):
        playground = self.load_playground_from("""222                           
222                           
222                           
222                           
222                           
22222222222222222 222222111   
2200000000000000222    21     
22011         00       21     
22011         0        21     
22011         00       21     
22011         0022222222111111
22011         00             1
2201111       00             1
22011111      00             1
22011111      00             1
220011111     00             1
22 001111     00             1
220001111     00             1
22000 111111111111111111111111
220000000000000000000000000000
""",
        [Player(0, 29, 19), Player(1, 5, 12), Player(2, 2, 0)])
        assert playground.memory[29][19]!=Available()
        playground.setPlayer(0, -1, -1, -1, -1)
        assert playground.memory[29][19]==Available()

    def test_playground_at_turn_80_time_1592162644834_only_available_direction_is_up(self):
        playground = self.load_playground_from("""222                           
222                           
222                           
222                           
222                           
22222222222222222 222222111   
22              222    21     
22 11                  21     
22 11                  21     
22 11                  21     
22 11           22222222111111
22 11                        1
22 1111                      1
22 11111                     1
22 11111                     1
22  11111                    1
22   1111                    1
22   1111                    1
22    111111111111111111111111
22                            
""",
        [Player(1, 5, 12), Player(2, 2, 0)])
        assert playground.doComputeMove(1)!="RIGHT"


    def test_playground_at_turn_1_time_1592242331799(self):
        """This one is particularly hard: I'm quite sure my bot shouldn't 
        go up at this turn, but should wait one more turn.
        """
        playground = self.load_playground_from("""                              
                              
                              
                              
                              
                              
                              
                              
                              
                              
                              
                              
                              
                              
                              
                              
                      11 00   
                              
                              
                              
""",
        [Player(0, 25, 16), Player(1, 23, 16)])
        assert playground.doComputeMove(0)!="UP"

    def test_playground_at_turn_16_time_1592337492702(self):
        """
        Here the player should go down
            {'direction': PlayerSim(1; 17; 14; LEFT), 'mine': 376, 'max': 31, 'sum': 407};
            {'direction': PlayerSim(1; 18; 15; DOWN), 'mine': 374, 'max': 33, 'sum': 407};
        """
        playground = self.load_playground_from("""                              
                              
                              
             111111           
                  1           
                  1           
                  1000000000  
                  10          
                  10          
                  10          
                  10          
                  10          
                  10          
                  10          
                  10          
                   0          
                              
                              
                              
                              
""",
        [Player(0, 19, 15), Player(1, 18, 14)])
        assert playground.doComputeMove(1)!="LEFT"