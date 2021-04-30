use spring_challenge_2021::*;
extern crate spectral;
use spectral::prelude::*;

#[test]
pub fn four_is_four() {
    assert_that(&do_it()).is_equal_to(4);
}