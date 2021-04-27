use std::time::Instant;
use spring_challenge_2020::*;

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
    let effective_moves = at_turn.compute_pacs_moves(&pacs);
    let elapsed = now.elapsed();
    println!("Elapsed: {:#?}", elapsed);
    let expected_moves = vec!["MOVE 0 21 10"];
    for i in 0..effective_moves.len() {
        assert_ne!(effective_moves[i], expected_moves[i])
    }
}
