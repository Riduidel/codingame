from Player import *
import unittest

class TestDirection(unittest.TestCase):
    def test_that_two_points_can_be_equal(self):
        assert Point(0, 0)==Point(0, 0)
    def test_that_direction_moves_point(self):
        assert Direction("UP", 0, -1).move(Point(0, 0))==Point(0, -1)
    def test_that_directions_constants_are_usable(self):
        assert "UP" in DIRECTIONS

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