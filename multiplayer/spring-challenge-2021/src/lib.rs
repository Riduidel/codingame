use std::io;
use std::vec::Vec;
use std::collections::*;
use std::fmt;
use std::time::SystemTime;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

#[derive(Debug)]
struct Tree {
    index:usize,
    size:usize,
    mine:bool,
    dormant:bool
}

impl fmt::Display for Tree {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "i:{} s:{} m:{}", self.index, self.size, self.mine)
    }
}


/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fn main() {
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    // number of cells of playground
    let number_of_cells = parse_input!(input_line, i32); // 37
    // content of each cell
    for i in 0..number_of_cells as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let index = parse_input!(inputs[0], i32); // 0 is the center cell, the next cells spiral outwards
        let richness = parse_input!(inputs[1], i32); // 0 if the cell is unusable, 1-3 for usable cells
        let neigh_0 = parse_input!(inputs[2], i32); // the index of the neighbouring cell for each direction
        let neigh_1 = parse_input!(inputs[3], i32);
        let neigh_2 = parse_input!(inputs[4], i32);
        let neigh_3 = parse_input!(inputs[5], i32);
        let neigh_4 = parse_input!(inputs[6], i32);
        let neigh_5 = parse_input!(inputs[7], i32);
    }

    // game loop
    loop {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let day = parse_input!(input_line, i32); // the game lasts 24 days: 0-23
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let nutrients = parse_input!(input_line, i32); // the base score you gain from the next COMPLETE action
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let sun = parse_input!(inputs[0], i32); // your sun points
        let score = parse_input!(inputs[1], i32); // your current score
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let opp_sun = parse_input!(inputs[0], i32); // opponent's sun points
        let opp_score = parse_input!(inputs[1], i32); // opponent's score
        let opp_is_waiting = parse_input!(inputs[2], i32); // whether your opponent is asleep until the next day
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let mut trees:Vec<Tree> = vec![];
        let number_of_trees = parse_input!(input_line, i32); // the current amount of trees
        for i in 0..number_of_trees as usize {
            let mut input_line = String::new();
            io::stdin().read_line(&mut input_line).unwrap();
            let inputs = input_line.split(" ").collect::<Vec<_>>();
            let cell_index = parse_input!(inputs[0], i32); // location of this tree
            let size = parse_input!(inputs[1], i32); // size of this tree: 0-3
            let is_mine = parse_input!(inputs[2], i32); // 1 if this is your tree
            let is_dormant = parse_input!(inputs[3], i32); // 1 if this tree is dormant
            trees.push(Tree {index: cell_index as usize, size: size as usize, mine: is_mine==1, dormant: false});
        }
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let number_of_possible_moves = parse_input!(input_line, i32);
        for i in 0..number_of_possible_moves as usize {
            let mut input_line = String::new();
            io::stdin().read_line(&mut input_line).unwrap();
            let possible_move = input_line.trim_matches('\n').to_string();
        }

        trees.sort_by_key(|t| -1*(t.size as i32));
        eprintln!("{:#?}", &trees);

        let mut my_trees = trees
            .iter()
            .filter(|t| t.mine);
        // Write an action using println!("message...");
        // To debug: eprintln!("Debug message...");
        match my_trees
            .next() {
                Some(t) => {
                    if t.size>=3 {
                        println!("COMPLETE {}", t.index)
                    } else {
                        println!("GROW {}", t.index)
                    }
                },
                None => println!("WAIT")
            }

        // GROW cellIdx | SEED sourceIdx targetIdx | COMPLETE cellIdx | WAIT <message>
        ;
    }
}
