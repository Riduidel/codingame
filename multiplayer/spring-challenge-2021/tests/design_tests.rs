use spring_challenge_2021::*;
extern crate spectral;
use spectral::prelude::*;

#[test]
pub fn indices_are_good() {
    // Given
    let playground:IndexedHexGround<i32> = IndexedHexGround::<i32>::of_radius(3);
    // When
    // Then
    assert_that(&playground.index[0]).is_equal_to(Point { row:0, col:0});
    assert_that(&playground.index[1]).is_equal_to(Point { row:0, col:1});
    assert_that(&playground.index[7]).is_equal_to(Point { row:0, col:2});
    assert_that(&playground.index[19]).is_equal_to(Point { row:0, col:3});
    assert_that(&playground.index[36]).is_equal_to(Point { row:1, col:2});
}

#[test]
pub fn can_get_by_index_a_value_set_by_coordinates() {
    // Given
    let mut playground:IndexedHexGround<i32> = IndexedHexGround::<i32>::of_radius(3);
    // When
    playground.by_coordinates.set(-1, 1, 13);
    // Then
    assert_that(&playground.get(2).unwrap()).is_equal_to(&13);
}