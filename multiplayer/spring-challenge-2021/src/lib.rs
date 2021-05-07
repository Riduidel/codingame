use std::io;
use std::vec::Vec;
use std::collections::*;
use std::fmt;
use std::time::SystemTime;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Tree  /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
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

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Point  ////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash)]
pub struct Point {
    pub row:i32,
    pub col:i32
}

impl Point {
    pub fn distance_to(&self, other:&Point)->i32 {
        (self.row-other.row).abs() + (self.col-other.col).abs()
    }

    pub fn directions()->Vec<Point> {
        return vec![
            Point { row: 0, col: -1},
            Point { row: -1, col: 0},
            Point { row: -1, col: 1},
            Point { row: 0, col: 1},
            Point { row: 1, col: 0},
            Point { row: 1, col: -1},
        ]
    }

    pub fn move_of(&self, other:&Point)->Point {
        return Point {
            row: self.row+other.row,
            col: self.col+other.col
        }
    }
}


/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Playground  ///////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/// we use as basis the axis storage defined in https://www.redblobgames.com/grids/hexagons/#map-storage
pub struct AxialHexGround<Content:Clone> {
    radius:usize,
    /// In content, all elements are none, and will be replaced by Some when needed
    /// This has the unfortunate consequence of forcing me to use matches all over the code
    /// Please note that content is stored row first, then column
    content:Vec<Vec<Option<Content>>>
}

impl<Content:Clone> AxialHexGround<Content> {
    pub fn of_radius<NewContent:Clone>(radius:usize)->AxialHexGround<NewContent> {
        let mut returned = AxialHexGround {
            radius:radius,
            content:vec![]
        };
        // This will be used to have the total number of vecs as radius is, well, a radius
        let size=radius*2+1;
        // Create and fill the vec
        for i in 0..size {
            returned.content.push(vec![None; size]);
        }
        // Return the generated hex ground
        return returned;
    }
    pub fn set_at(&mut self, position:Point, content:Content) {
        self.set(position.row, position.col, content);
    }

    pub fn get_at(&self, position:Point)->Option<&Content> {
        self.get(position.row, position.col)
    }

    /// Low level setter, which won't provide a particularly useful way to emulate the 3d part
    /// values for r and q are
    pub fn set(&mut self, r:i32, q:i32, content:Content) {
        let row=(self.radius as i32+r) as usize;
        let col=(self.radius as i32+q) as usize;
        self.content[row][col]=Option::Some(content);
    }

    /// Low level getter, which won't provide a particularly useful way to emulate the 3d part
    pub fn get(&self, r:i32, q:i32)->Option<&Content> {
        let row=(self.radius as i32+r) as usize;
        let col=(self.radius as i32+q) as usize;
        self.content[row][col].as_ref()
    }
}

/// Numbered hex ground allows both access per coordinate, but also access by cell index
/// For that, obviously, we use delegation pattern
pub struct IndexedHexGround<Content:Clone> {
    pub index:Vec<Point>,
    pub by_coordinates:AxialHexGround<Content>
}

fn index_up_to_radius(radius:usize)->Vec<Point> {
    let mut returned = vec![];
    // Distance is 0
    returned.push(Point {   col: 0,   row: 0});
    // Distance is 1
    returned.push(Point {   col: 1,   row: 0});
    returned.push(Point {   col: 1,   row: -1});
    returned.push(Point {   col: 0,   row: -1});
    returned.push(Point {   col: -1,   row: 0});
    returned.push(Point {   col: -1,   row: 1});
    returned.push(Point {   col: 0,   row: 1});
    // Distance is 2
    returned.push(Point {   col: 2,   row: 0});
    returned.push(Point {   col: 2,   row: -1});
    returned.push(Point {   col: 2,   row: -2});
    returned.push(Point {   col: 1,   row: -2});
    returned.push(Point {   col: 0,   row: -2});
    returned.push(Point {   col: -1,   row: -1});
    returned.push(Point {   col: -2,   row: 0});
    returned.push(Point {   col: -2,   row: 1});
    returned.push(Point {   col: -2,   row: 2});
    returned.push(Point {   col: -1,   row: 2});
    returned.push(Point {   col: 0,   row: 2});
    returned.push(Point {   col: 1,   row: 1});
    // Distance is 3
    returned.push(Point {   col: 3,   row: 0});
    returned.push(Point {   col: 3,   row: -1});
    returned.push(Point {   col: 3,   row: -2});
    returned.push(Point {   col: 3,   row: -3});
    returned.push(Point {   col: 2,   row: -3});
    returned.push(Point {   col: 1,   row: -3});
    returned.push(Point {   col: -0,   row: -3});
    returned.push(Point {   col: -1,   row: -2});
    returned.push(Point {   col: -2,   row: -1});
    returned.push(Point {   col: -3,   row: 0});
    returned.push(Point {   col: -3,   row: 1});
    returned.push(Point {   col: -3,   row: 2});
    returned.push(Point {   col: -3,   row: 3});
    returned.push(Point {   col: -2,   row: 3});
    returned.push(Point {   col: -1,   row: 3});
    returned.push(Point {   col: 0,   row: 3});
    returned.push(Point {   col: 1,   row: 2});
    returned.push(Point {   col: 2,   row: 1});
    return returned;
}
impl<Content:Clone> IndexedHexGround<Content> {
    pub fn of_radius<NewContent:Clone>(radius:usize)->IndexedHexGround<NewContent> {
        let returned = IndexedHexGround {
            index: index_up_to_radius(radius),
            by_coordinates:AxialHexGround::<NewContent>::of_radius(radius)
        };
        // Now fill the index
        // And then return the generated playground
        return returned;
    }

    pub fn set(&mut self, index:usize, value:Content) {
        let location = self.index[index];
        self.by_coordinates.set_at(location, value);
    }

    pub fn get(&mut self, index:usize)->Option<&Content> {
        let location = self.index[index];
        self.by_coordinates.get_at(location)
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
