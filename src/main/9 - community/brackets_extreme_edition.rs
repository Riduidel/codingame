use std::io;
use std::process;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

fn destack(stacked:&mut Vec<char>, expected:char, current:char) {
    let last:Option<char> = stacked.pop();
    match last {
        Some(c) => {
            if c!=expected { 
                println!("false"); 
                process::exit(1);
            }
        }
        None => {
            println!(false);
            process::exit(2);
        },
    }
}
/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fn main() {
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let expression = input_line.trim().to_string();
    let mut stacked = vec![];

    eprintln!("Processing '{}'", expression);
    for c in expression.chars() {
        match c {
        '{'|'('|'[' => stacked.push(c),
        '}' => destack(&mut stacked, '{', '}'),
        ')' => destack(&mut stacked, '(', ')'),
        ']' => destack(&mut stacked, '[', ']'),
        _ => {}
        }
    }

    println!("{}", stacked.is_empty());
}