from Player import *
import unittest

class InGameTests(unittest.TestCase):
    def load_playground_from(self, text):
        returned = Playground.load_from(text)