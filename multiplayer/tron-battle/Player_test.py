from Player import *
import unittest

class TestDirection(unittest.TestCase):
    def test_that_two_points_can_be_equal(self):
        assert Point(0, 0)==Point(0, 0)
    def test_that_direction_moves_point(self):
        assert Direction("UP", 0, -1).move(Point(0, 0))==Point(0, -1)
    def test_that_directions_constants_are_usable(self):
        assert len(DIRECTIONS)>0

class TestPlaygroundAllowPoint(unittest.TestCase):
    def test_that_playground_is_loaded(self):
        # Given
        playground = Playground(30, 20)
        # Then
        assert playground.width==30
    def test_that_playground_can_validate_point(self):
        # Given
        playground = Playground(30, 20)
        # When
        tested = Point(-1, 0)
        # Then
        assert playground.allow(tested)==False

    def test_that_playground_loads_from_string(self):
        # Given
        playground = Playground.load_from("""00
  """, 
        [Player(0, 0, 0)],
        width=2, height=2)
        print("%s"%(playground.toDebug()))
        # Then
        assert isinstance(playground.memory[0][0], PlayerTrace)
        assert isinstance(playground.memory[1][0], PlayerTrace)
        assert isinstance(playground.memory[0][1], Available)
        assert isinstance(playground.memory[1][1], Available)

    def test_that_valid_move_is_recognized(self):
        # Given
        playground = Playground.load_from("""00
  """, 
        [Player(0, 0, 0)],
        width=2, height=2)
        assert playground.doComputeMove(0)=="DOWN"
