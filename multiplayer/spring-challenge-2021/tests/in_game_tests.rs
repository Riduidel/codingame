use std::time::Instant;
use spring_challenge_2021::*;
extern crate spectral;
use spectral::prelude::*;
/*
    #[test]
    fn test_actions_at_1620540281() {
        let day = 0;
        let sun = 2;
        let ground = VecGround::<Cell>::parse(&mut vec![Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 })]);
        let mut trees = vec![t(22, 1, true, false), t(27, 1, true, false), t(31, 1, false, false), t(36, 1, false, false)];
        let now = Instant::now();
        assert_that(&compute_action(day, sun, &ground, &mut trees)).is_not_equal_to(&Action::SEED { id: 22, position: 23 });
    }

    #[test]
    fn test_actions_at_1620565153() {
        let day = 15;
        let sun = 5;
        let ground = VecGround::<Cell>::parse(&mut vec![Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 })]);
        let mut trees = vec![t(0, 0, false, false), t(1, 3, true, false), t(2, 2, true, false), t(3, 1, true, false), t(4, 0, true, false), t(5, 0, false, false), t(6, 1, true, false), t(7, 2, false, false), t(8, 0, true, false), t(9, 0, true, false), t(10, 0, true, false), t(12, 0, true, false), t(14, 0, false, false), t(15, 2, false, false), t(18, 2, false, false), t(20, 1, false, false), t(21, 0, false, false), t(22, 2, true, false), t(27, 1, true, false), t(29, 0, false, false), t(30, 2, false, false), t(31, 2, false, false), t(32, 0, false, false), t(33, 1, false, false), t(36, 1, false, false)];
        let now = Instant::now();
        assert_that(&compute_action(day, sun, &ground, &mut trees)).is_not_equal_to(&Action::COMPLETE { id: 1 });
    }

#[test]
fn test_actions_at_1620566619() {
    let day = 6;
    let sun = 8;
    let ground = VecGround::<Cell>::parse(&mut vec![Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 })]);
    let mut trees = vec![t(1, 0, false, false), t(2, 0, true, false), t(5, 0, false, false), t(7, 1, false, false), t(9, 1, true, false), t(12, 0, true, false), t(13, 0, true, false), t(15, 0, false, false), t(18, 1, false, false), t(22, 1, true, false), t(27, 1, true, false), t(31, 2, false, false), t(36, 1, false, false)];
    let now = Instant::now();
    assert_that(&compute_action(day, sun, &ground, &mut trees)).is_not_equal_to(&Action::SEED { id: 9, position: 10 });
}

#[test]
fn test_actions_at_1620571180() {
    let day = 6;
    let sun = 15;
    let ground = VecGround::<Cell>::parse(&mut vec![Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 3 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 2 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 1 }), Some(Cell { richness: 0 }), Some(Cell { richness: 1 })]);
    let mut trees = vec![t(7, 1, false, false), t(8, 0, false, false), t(9, 0, true, false), t(15, 0, false, false), t(18, 1, false, false), t(20, 0, false, false), t(22, 2, true, false), t(27, 2, true, false), t(30, 0, false, false), t(31, 1, false, false), t(36, 2, false, false)];
    let now = Instant::now();
    assert_that(&compute_action(day, sun, &ground, &mut trees)).is_not_equal_to(&Action::WAIT);
}
*/
