use std::time::Instant;
use spring_challenge_2023::*;
extern crate spectral;
use spectral::prelude::*;

#[test]
pub fn can_compute_topology_of_a_simple_playground() {
    let playground = Playground::init(
        vec![
            (0, CellContent::EMPTY, 0, 0, 0, 0, [1, 2, 3, 4, 5, 6]), 
            (1, CellContent::EMPTY, 0, 0, 0, 0, [-1, -1, 2, 0, 6, -1]), 
            (2, CellContent::EMPTY, 0, 0, 0, 0, [-1, -1, -1, 3, 0, 1]), 
            (3, CellContent::EMPTY, 0, 0, 0, 0, [2, -1, -1, -1, 4, 0]), 
            (4, CellContent::EMPTY, 0, 0, 0, 0, [0, 3, -1, -1, -1, 5]), 
            (5, CellContent::EMPTY, 0, 0, 0, 0, [6, 0, 4, -1, -1, -5]), 
            (6, CellContent::EMPTY, 0, 0, 0, 0, [-1, 1, 0, 5, -1, -5]), 
        ],
        Player { bases: vec![1] },
        Player { bases: vec![2] }
    );
    let distances_to_0 = playground.topology.get(0).unwrap();
    assert_that(distances_to_0.get(1).unwrap()).is_equal_to(vec![0]);
    let distances_to_1 = playground.topology.get(1).unwrap();
    assert_that(distances_to_1.get(2).unwrap()).is_equal_to(vec![1]);
    assert_that(distances_to_1.get(3).unwrap()).is_equal_to(vec![1, 2]);
    assert_that(distances_to_1.get(4).unwrap()).is_equal_to(vec![1, 0]);
    assert_that(distances_to_1.get(5).unwrap()).is_equal_to(vec![1, 0]);
    assert_that(distances_to_1.get(6).unwrap()).is_equal_to(vec![1]);
}