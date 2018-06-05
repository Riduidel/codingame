use std::time::Instant;
// This is some kind of magic : we're in a module, which is included in the real file

fn simplify(input:String, output:String) {
    let start = Instant::now();

    assert_eq!(super::simplify(input.to_owned()), output);

    let elapsed = start.elapsed();
    // or format as milliseconds:
    println!("Conversion of {} into {} dured: {} ms",
            input, output,
             (elapsed.as_secs() * 1_000) + (elapsed.subsec_nanos() / 1_000_000) as u64);
}

#[test]
fn simple_fraction_works() {
    simplify("4/6".to_string(), "2/3".to_string());
}

#[test]
fn zero_numerator_works() {
    simplify("0/6".to_string(), "0".to_string());
}

#[test]
fn zero_denominator_works() {
    simplify("3/0".to_string(), "DIVISION BY ZERO".to_string());
}

#[test]
fn negative_denominator_works() {
    simplify("20/-5".to_string(), "-4".to_string());
}

#[test]
fn negative_numerator_works() {
    simplify("-5/20".to_string(), "-1/4".to_string());
}

#[test]
fn big_numerator_works() {
    simplify("9784128/9994708".to_string(), "2446032/2498677".to_string());
}
