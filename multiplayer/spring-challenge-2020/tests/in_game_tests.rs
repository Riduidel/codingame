use std::time::Instant;
use spring_challenge_2020::*;
#[macro_use]
extern crate spectral;

use spectral::prelude::*;

#[test]
fn test_move_at_1588946109() {
    let (_playground, at_turn) = Playground::parse("#################################
###...#.#...#...#...#...#.#...###
#####.#.#.#####.#.#####.#.#.#####
#.#.....#.O.#.......#.O.#.....#.#
#.#.#.#.###.#.#.#.#.#.###.#.#.#.#
#...#.#.......#...#.......#.#...#
###.#.#.###.#.#.#.#.#.###.#.#.###
#O....#.#.....#...#.....#.#....O#
#.#.###.#.###.#####.###.#.###.#.#
#.#.....#.#   ....... #.#.....#.#
###.###.#.# ###.#.### #.#.###.###
#################################
");
    let pacs = vec![Pac { pac_id: 0, mine: true, position: Point { col: 21, row: 9 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 },
    Pac { pac_id: 0, mine: false, position: Point { col: 13, row: 9 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 }];
    let now = Instant::now();
    let effective_moves = at_turn.compute_pacs_moves(&pacs.iter().map(|p| p).collect());
    let elapsed = now.elapsed();
    println!("Elapsed: {:#?}", elapsed);
    assert_that(&effective_moves).contains("MOVE 0 20 9".to_string());
}
