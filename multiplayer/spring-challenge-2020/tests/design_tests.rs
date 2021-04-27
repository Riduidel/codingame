use std::time::Instant;
use spring_challenge_2020::*;
#[macro_use]
extern crate spectral;

use spectral::prelude::*;

#[test]
fn can_choose_big_pill_below() {
    let (_playground, at_turn) = Playground::parse("####
# .#
#O #
####
");
    let pacs = vec![Pac { pac_id: 0, mine: true, position: Point { col: 1, row: 1 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 }];
    let now = Instant::now();
    let effective_moves = at_turn.compute_pacs_moves(&pacs);
    let elapsed = now.elapsed();
    asserting(&"the number of moves is the right one").that(&effective_moves).has_length(1);
    assert_that(&effective_moves[0].to_string()).is_equal_to(&"MOVE 0 1 2".to_string());
}

#[test]
fn can_choose_big_pill_on_right() {
    let (_playground, at_turn) = Playground::parse("####
# O#
#. #
####
");
    let pacs = vec![Pac { pac_id: 0, mine: true, position: Point { col: 1, row: 1 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 }];
    let now = Instant::now();
    let effective_moves = at_turn.compute_pacs_moves(&pacs);
    let elapsed = now.elapsed();
    asserting(&"the number of moves is the right one").that(&effective_moves).has_length(1);
    assert_that(&effective_moves[0].to_string()).is_equal_to(&"MOVE 0 2 1".to_string());
}

#[test]
fn can_choose_nearer_small_pill() {
    let (_playground, at_turn) = Playground::parse("####
# .#
# .#
####
");
    let pacs = vec![Pac { pac_id: 0, mine: true, position: Point { col: 1, row: 1 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 }];
    let now = Instant::now();
    let effective_moves = at_turn.compute_pacs_moves(&pacs);
    let elapsed = now.elapsed();
    asserting(&"the number of moves is the right one").that(&effective_moves).has_length(1);
    assert_that(&effective_moves[0].to_string()).is_equal_to(&"MOVE 0 2 1".to_string());
}

#[test]
fn two_pacs_shouldnt_choose_the_same_target() {
    let (_playground, at_turn) = Playground::parse("####
# .#
#  #
####
");
    let pacs = vec![
        Pac { pac_id: 0, mine: true, position: Point { col: 1, row: 1 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 },
        Pac { pac_id: 1, mine: true, position: Point { col: 2, row: 2 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 },
        ];
    let now = Instant::now();
    let effective_moves = at_turn.compute_pacs_moves(&pacs);
    asserting(&"the number of moves is the right one").that(&effective_moves).has_length(2);
    assert_that(&effective_moves)
        .contains(&"MOVE 0 1 2".to_string());
    assert_that(&effective_moves)
        .contains(&"MOVE 1 2 1".to_string());
}
