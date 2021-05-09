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

#[test]
pub fn can_dehydrate() {
    // Given
    let mut playground:IndexedHexGround<i32> = IndexedHexGround::<i32>::of_radius(1);
    playground.by_coordinates.set(-1, 1, 13);
    // When
    assert_that(&playground.quine("i32")).is_equal_to(&"IndexedHexGround::<i32>::parse(vec![None, None, Some(13), None, None, None, None])".to_string());
    // Then
}

#[test]
pub fn can_rehydrate() {
    // Given
    let mut playground:IndexedHexGround<i32> = IndexedHexGround::<i32>::parse(vec![None, None, Some(13), None, None, None, None]);
    // Then
    assert_that(&playground.get(1)).is_equal_to(None);
    assert_that(&playground.get(2).unwrap()).is_equal_to(&13);
}

#[test]
fn should_seed_interesting_location_at_first_turn() {
    let day = 0;
    let sun = 2;
    let nutrients = 20;
    let ground = VecGround::<Cell>::parse(&mut vec![Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 })]);
    let mut trees = vec![t(22, 1, true, false), t(27, 1, true, false), t(31, 1, false, false), t(36, 1, false, false)];
    assert_that(&compute_action(day, sun, nutrients, &ground, &mut trees).0).is_equal_to(&Action::SEED { id: 22, position: 9 });
}


#[test]
fn should_harvest_even_the_less_interesting_tree_before_game_ends() {
    let day = 23;
    let sun = 32;
    let nutrients = 15;
    let ground = VecGround::<Cell>::parse(&mut vec![Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 })]);
    let mut trees = vec![t(1, 0, true, false), t(4, 0, true, false), t(5, 1, false, false), t(6, 2, false, false), t(7, 2, false, false), t(8, 2, false, false), t(9, 1, true, false), t(10, 0, true, false), t(13, 0, false, false), t(14, 0, true, false), t(15, 1, false, false), t(16, 0, true, false), t(18, 2, false, false), t(20, 1, false, false), t(21, 0, false, false), t(22, 3, true, false), t(27, 1, true, false), t(29, 1, false, false), t(32, 0, false, false), t(33, 1, false, false), t(34, 0, false, false), t(36, 3, false, false)];
    assert_that(&compute_action(day, sun, nutrients, &ground, &mut trees).0).is_not_equal_to(&Action::WAIT);
}