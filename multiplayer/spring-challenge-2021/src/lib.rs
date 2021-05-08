use std::io;
use std::vec::Vec;
use std::collections::*;
use std::fmt;
use std::time::SystemTime;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Cell  /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
#[derive(Debug, Clone, Copy)]
pub struct Cell {
    // richness goes from 0 for unusable cell to 3 for the cells to force get
    pub richness:usize
}
/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Action  /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
#[derive(Debug,Clone,PartialEq)]
pub enum Action {
    COMPLETE {
        id:usize
    },
    GROW {
        id:usize
    },
    SEED {
        id:usize
    },
    WAIT
}

impl fmt::Display for Action {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        match self {
            Action::COMPLETE {id} => write!(f, "{} {}", "COMPLETE", id),
            Action::GROW {id} => write!(f, "{} {}", "GROW", id),
            Action::SEED {id} => write!(f, "{} {}", "SEED", id),
            Action::WAIT => write!(f, "WAIT"),
        }
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Tree  /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
#[derive(Debug,Clone)]
pub struct Tree {
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

pub fn t(index:usize, size:usize, mine:bool, dormant:bool)->Tree {
    Tree {
        index:index, size:size, mine:mine, dormant:dormant
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

    /// directions here are sorted according to days
    pub fn directions()->Vec<Point> {
        return vec![
            Point { row: 0, col: 1},
            Point { row: -1, col: 1},
            Point { row: -1, col: 0},
            Point { row: 0, col: -1},
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
    pub fn set_at(&mut self, position:&Point, content:Content) {
        self.set(position.row, position.col, content);
    }

    pub fn get_at(&self, position:&Point)->Option<&Content> {
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
    if radius>=1 {
        returned.push(Point {   col: 1,   row: 0});
        returned.push(Point {   col: 1,   row: -1});
        returned.push(Point {   col: 0,   row: -1});
        returned.push(Point {   col: -1,   row: 0});
        returned.push(Point {   col: -1,   row: 1});
        returned.push(Point {   col: 0,   row: 1});
    }
    if radius>=2 {
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
    }
    if radius>=3 {
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
    }
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

    pub fn parse<NewContent:Clone+Copy>(values:Vec<Option<NewContent>>)->IndexedHexGround<NewContent> {
        let radius = match values.len() {
            1 => 0,
            7 => 1,
            19 => 2,
            37 => 3,
            _ => panic!("values array is of size {}, which is not supported", values.len())
        };
        let mut returned:IndexedHexGround<NewContent> = IndexedHexGround::<NewContent>::of_radius(radius);
        for (position, element) in values.iter().enumerate() {
            element.as_ref().map(|e| returned.set(position, *e));
        }
        return returned;
    }

    pub fn set(&mut self, index:usize, value:Content) {
        let location = self.index[index];
        self.by_coordinates.set_at(&location, value);
    }

    pub fn get(&mut self, index:usize)->Option<&Content> {
        let location = self.index[index];
        self.by_coordinates.get_at(&location)
    }
}

impl<Content:Clone+fmt::Debug> QuinableGround<Content> for IndexedHexGround<Content> {
    fn quine(&self, contained_type:&str)->String {
        let hydrater:Vec<Option<&Content>> = self.index.iter()
            .map(|p| self.by_coordinates.get_at(p))
            .collect();
        return format!("IndexedHexGround::<{}>::parse(vec!{:?})", contained_type, hydrater);
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// VecGround  ///////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
struct Geometry<Content> {
    // -1 is used for elements that are on ground's edge
    neighbours:[i32;6],
    content:Option<Content>
}
pub struct VecGround<Content> {
    storage:Vec<Geometry<Content>>
}
impl<Content> VecGround<Content> {
    pub fn parse<NewContent:Clone>(cells:&mut Vec<Option<NewContent>>)->VecGround<NewContent> {
        let mut returned:VecGround<NewContent> = VecGround {
            storage: Vec::with_capacity(cells.len())
        };
        let len = cells.len();
        if len!=37 {
            panic!("We only know how to handle points for an hex ground of radius 3");
        }
        // Reconstitute geometry
        let points:Vec<Point> = index_up_to_radius(3);
        for (index, point) in points.iter().enumerate() {
            // As a default, we always go outside
            let mut neighbours:[i32;6] = [-1;6];
            for (neighour_index, move_point) in Point::directions().iter().enumerate() {
                let moved = point.move_of(move_point);
                match points.iter().position(|p| p==&moved) {
                    Some(found_at) => neighbours[neighour_index] = found_at as i32,
                    _ => {}
                }
            }
            // Now we have all of our neighbours, set geometry!
            returned.set_geometry(index, neighbours);
        }
        // set content
        for i in 0..len {
            match cells.remove(0) {
                Some(content) => returned.set_content(i, content),
                _ => {}
            }
        }
        return returned;
    }
    pub fn of_cells<NewContent:Clone>(cells:usize)->VecGround<NewContent> {
        VecGround {
            storage: Vec::with_capacity(cells)
        }
    }

    pub fn set_geometry(&mut self, index:usize, geometry:[i32;6]) {
        if index==self.storage.len() {
            self.storage.push(Geometry {
                neighbours:geometry,
                content:None
            });
        } else {
            panic!("can't set geometry at any other place than ground len()")
        }
    }

    pub fn set_content(&mut self, index:usize, content:Content) {
        self.storage[index].content = Some(content);
    }
}

impl<Content:Clone+fmt::Debug> QuinableGround<Content> for VecGround<Content> {
    fn quine(&self, contained_type:&str)->String {
        let hydrater:Vec<Option<&Content>> = self.storage.iter()
            .map(|geom| geom.content.as_ref())
            .collect();
        return format!("VecGround::<{}>::parse(vec!{:?})", contained_type, hydrater);
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// QuinableGround  ///////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
pub trait QuinableGround<Content> {
    fn quine(&self, content_type:&str)->String;
}

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////// Computer  ///////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
struct Computer<'lifetime> {
    day:usize,
    sun:usize,
    ground:&'lifetime VecGround<Cell>,
    trees:&'lifetime Vec<Tree>
}

impl<'lifetime> Computer<'lifetime> {
    fn compute (&self)->Action {
        
        let mut my_trees:Vec<&Tree> = self.trees
            .iter()
            .filter(|t| t.mine)
            .collect();
        my_trees.sort_by_key(|t| -1*(t.size as i32));
        // Write an action using println!("message...");
        // To debug: eprintln!("Debug message...");
        return match my_trees.iter()
            .next() {
                Some(t) => {
                    if t.size>=3 {
                        Action::COMPLETE {id: t.index }
                    } else {
                        Action::GROW {id: t.index }
                    }
                },
                None => Action::WAIT
            };
        }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Main decorations  /////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

fn to_test(day:i32, sun:i32, ground:&dyn QuinableGround<Cell>, trees:&Vec<Tree>, action:&Action)->String {
    let timestamp = match SystemTime::now().duration_since(SystemTime::UNIX_EPOCH) {
        Ok(n) => n.as_secs(),
        Err(_) => 0,
    };
    return format!("\t#[test]
    fn test_actions_at_{}() {{
        let day = {};
        let sun = {};

        let ground = {};

        let mut trees = vec![{}];
        let now = Instant::now();
        assert_that(&compute_action(day, sun, &ground, &mut trees)).is_not_equal_to(&Action::{:?});
    }}
    ", timestamp, 
        day,
        sun,
        ground.quine("Cell"),
        trees.iter().map(|t| format!("t({}, {}, {}, {})", t.index, t.size, t.mine, t.dormant)).collect::<Vec<String>>().join(", "), 
        action
    );
}

/// day indicates the shadow direction
/// sun gives the number of action points that can be used in the day
/// ground is, well, the ground
/// contains all trees, both mines and opponent ones
pub fn compute_action(day:i32, sun:i32, ground:&VecGround<Cell>, trees:&mut Vec<Tree>)->Action {
//    let wood_action = wood_compute_action(&mut trees.clone());
    let computer = Computer {
        day: day as usize,
        sun: sun as usize,
        ground: ground,
        trees:trees
    };
    let action = computer.compute();
    eprintln!("{}", to_test(day, sun, ground, &trees, &action));
    return action;
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
    let mut ground:VecGround<Cell> = VecGround::<Cell>::of_cells(37);
    // content of each cell
    for i in 0..number_of_cells as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let index = parse_input!(inputs[0], i32); // 0 is the center cell, the next cells spiral outwards
        let richness = parse_input!(inputs[1], usize); // 0 if the cell is unusable, 1-3 for usable cells
        let neigh_0 = parse_input!(inputs[2], i32); // the index of the neighbouring cell for each direction
        let neigh_1 = parse_input!(inputs[3], i32);
        let neigh_2 = parse_input!(inputs[4], i32);
        let neigh_3 = parse_input!(inputs[5], i32);
        let neigh_4 = parse_input!(inputs[6], i32);
        let neigh_5 = parse_input!(inputs[7], i32);
        ground.set_geometry(index as usize, [neigh_0, neigh_1, neigh_2, neigh_3, neigh_4, neigh_5]);
        ground.set_content(index as usize, Cell {richness: richness});
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


        let action = compute_action(day, sun, &ground, &mut trees);
        // GROW cellIdx | SEED sourceIdx targetIdx | COMPLETE cellIdx | WAIT <message>
        println!("{}", action);
    }
}
