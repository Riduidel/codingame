use std::time::{SystemTime, UNIX_EPOCH};
use std::cmp::Ordering;
use std::io;
use std::fmt;
use std::ops::Add;
use std::fs::File;
use std::io::prelude::*;
use std::path::Path;
use std::collections::BTreeMap;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}
/** Possible actions */
#[derive(Debug, PartialEq)]
pub enum Action {
    /**
     * Creates a beacon on the given cell index with the given strenght
     */
    BEACON {index:usize, strenght:i32},
    /**
     * Create a beacon line of the given strenght between the two cell indices
     */
    LINE {start_index:usize, end_index:usize, strenght:i32},
    WAIT,
    MESSAGE {text:String}
}

impl Action {
    fn to_test(&self)->String {
        format!("Action::{:?}", self)
    }
}

impl fmt::Display for Action {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::BEACON { index, strenght } => write!(f, "BEACON {} {}", index, strenght),
            Self::LINE { start_index, end_index, strenght } => write!(f, "LINE {} {} {}", start_index, end_index, strenght),
            Self::WAIT => write!(f, "WAIT"),
            Self::MESSAGE { text } => write!(f, "MESSAGE {}", text)
        }
    }
}

// See https://stackoverflow.com/a/42382144/15619
#[repr(i32)]
#[derive(Copy, Clone, Debug)]
pub enum CellContent {
    EMPTY = 0,
    EGG,
    CRYSTAL
}

#[derive(Copy, Clone, Debug)]
pub struct Point {
    row:i32,
    col:i32
}

impl Point {
    const DIRECTIONS: [Self; 6] = [
        // EAST
        Point { row: 0, col: 1},
        // NORTHEAST
        Point { row: -1, col: 1},
        // NORTHWEST
        Point { row: -1, col: -1},
        // WEST
        Point { row: -1, col: -1},
        // SOUTHWEST
        Point { row: 1, col: -1},
        // SOUTHWEST
        Point { row: 1, col: 1},
    ];

    fn to_test(&self)->String {
        format!("({}, {})", self.row, self.col)
    }
}

impl Add for Point {
    type Output = Self;

    fn add(self, other: Self) -> Self {
        Self {
            row: self.row + other.row,
            col: self.col + other.col,
        }
    }
}

#[derive(Clone, Debug)]
pub struct Cell {
    cell_index:usize,
    cell_content:CellContent,
    initial_resources:i32,
    resources:i32,
    my_ants:i32,
    opponent_ants:i32,
    neighbours: Vec<i32>
}

impl Cell {
    pub fn update(&mut self, resources:i32, my_ants:i32, opponent_ants:i32) {
        self.resources = resources;
        self.my_ants = my_ants;
        self.opponent_ants = opponent_ants;
    }

    fn to_test(&self)->String {
        format!(
            "({}, CellContent::{:?}, {}, {}, {}, {}, {:?})",
            self.cell_index,
            self.cell_content,
            self.initial_resources,
            self.resources,
            self.my_ants,
            self.opponent_ants,
            self.neighbours
        )
    }

    fn compute_topology(&self, cells:&Vec<Cell>) -> Vec<Vec<usize>>{
        let mut paths:BTreeMap<usize, Vec<usize>> = BTreeMap::new();
        // Now navigate through connected elements
        let mut evaluated = vec![self.cell_index];
        paths.insert(self.cell_index, vec![]);
        while !evaluated.is_empty() {
            let current_turn = evaluated.clone();
            evaluated.clear();
            for index in current_turn {
                let cell = cells.get(index).unwrap();
                let current_path = paths.get(&index).unwrap();
                let mut next_path = current_path.clone();
                next_path.push(index);
                for neigh in cell.neighbours.clone() {
                    if neigh>=0 {
                        if !paths.contains_key(&(neigh as usize)) {
                            evaluated.push(neigh as usize);
                            paths.insert(neigh as usize, next_path.clone());
                        }
                    }
                }
            }
        }
        // Now convert that BTreeMap to a Vec!
        let mut returned:Vec<Vec<usize>> = vec![];
        for index in 0..cells.len() {
            returned.push(paths.get(&index).unwrap().clone());
        }
        return returned;
    }
}

#[derive(Debug)]
pub struct Player {
    /**
     * Indices of cells where player has bases
     */
    pub bases:Vec<usize>
}

impl Player {
    pub fn parse()->Player {
        let mut inputs = String::new();
        io::stdin().read_line(&mut inputs).unwrap();
        let my_bases:Vec<usize> = inputs.split_whitespace()
            .into_iter()
            .map(|i| parse_input!(i, usize))
            .collect();
        
        Player {
            bases: my_bases
        }
    }

    fn to_test(&self)->String {
        format!("Player {{ bases: vec!{:?} }}", self.bases)
    }
}

#[derive(Debug)]
pub struct Playground {
    pub cells:Vec<Cell>,
    pub my_player: Player,
    pub opponent: Player,
    pub topology: Vec<Vec<Vec<usize>>>
}

impl Playground {

    /** 
     * Compute topological informations (like distances and positions)
    */
    fn compute_topology(cells:&Vec<Cell>) -> Vec<Vec<Vec<usize>>> {
        let mut topology:Vec<Vec<Vec<usize>>> = vec![];
        for index in 0..cells.len() {
            let cell = cells.get(index).unwrap();
            topology.push(cell.compute_topology(&cells));
        }
        return topology;
    }

    fn parse_initial_playground()->Playground {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let number_of_cells = parse_input!(input_line, usize); // amount of hexagonal cells in this map
        let mut cells:Vec<Cell> = Vec::with_capacity(number_of_cells as usize);
        for i in 0..number_of_cells {
            let mut input_line = String::new();
            io::stdin().read_line(&mut input_line).unwrap();
            let inputs = input_line.split(" ").collect::<Vec<_>>();
            let cell_type = parse_input!(inputs[0], i32); // 0 for empty, 1 for eggs, 2 for crystal
            let initial_resources = parse_input!(inputs[1], i32); // the initial amount of eggs/crystals on this cell
            let neigh_0 = parse_input!(inputs[2], i32); // the index of the neighbouring cell for each direction
            let neigh_1 = parse_input!(inputs[3], i32);
            let neigh_2 = parse_input!(inputs[4], i32);
            let neigh_3 = parse_input!(inputs[5], i32);
            let neigh_4 = parse_input!(inputs[6], i32);
            let neigh_5 = parse_input!(inputs[7], i32);
            let neighbours = vec!(neigh_0, neigh_1, neigh_2, neigh_3, neigh_4, neigh_5);
            cells.push(Cell {
                cell_index:i,
                cell_content: unsafe { ::std::mem::transmute(cell_type) },
                initial_resources: initial_resources,
                resources: 0,
                my_ants: 0,
                opponent_ants: 0,
                neighbours: neighbours
            });
        }
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let _number_of_bases = parse_input!(input_line, i32);
        let my = Player::parse();
        let opponent = Player::parse();
        let mut returned = Playground {
            topology: Playground::compute_topology(&cells),
            cells: cells,
            my_player: my,
            opponent: opponent,
        };
        return returned;
    
    }

    pub fn parse_turn(&mut self) {
//        let turn_start = chrono::offset::Utc::now();
        for cell_index in 0..self.cells.len() as usize {
            let mut input_line = String::new();
            match io::stdin().read_line(&mut input_line) {
                Ok(_) => {
                    let inputs = input_line.split(" ").collect::<Vec<_>>();
                    let mut cell = self.cells.get_mut(cell_index);
                    match cell {
                        Some(c) => {
                            c.update(
                                parse_input!(inputs[0], i32), 
                                parse_input!(inputs[1], i32), 
                                parse_input!(inputs[2], i32));
                        },
                        None => {
                            eprintln!("No cell found at {}", cell_index);
                        }
                    }
                },
                Err(error) => {
                    eprintln!("Unable to read line {} due to {}", cell_index, error);
                }
            }
        }
    }

    pub fn compute_actions(&self)->Vec<Action> {
        // Find all cells having resources
        let targets:Vec<&Cell> = self.cells
            .iter()
            .filter(|cell| cell.resources>0)
            .collect();
        // Count the number of targets
        let targets_count = targets.len();
        // Write an action using println!("message...");
        // To debug: eprintln!("Debug message...");
        let actions:Vec<Action> = self.my_player.bases
            .iter()
            .flat_map(|base_index| {
                let base_cell = &self.cells[*base_index];
                let actions:Vec<Action> = targets.iter()
                    .map(|cell| {
                        Action::LINE { start_index: base_index.clone(), end_index: cell.cell_index.clone(), strenght: 1}
                    })
                    .collect();
                actions
            })
            .collect();
        return actions;
    }

    fn to_test(&self, current:Vec<Action>)->String {
        let start = SystemTime::now();
        let since_the_epoch = start
            .duration_since(UNIX_EPOCH)
            .expect("Time went backwards");
        let timestamp = since_the_epoch.as_millis();
        return format!("
        #[test]
        pub fn turn_at_{}() {{
            let playground = Playground::init(
                vec![{}],
                {},
                {}
            );
            playground.draw(\"in_game_at_{}.pkchr\");
            let computed = playground.compute_actions();
            let current = vec![{}];
            assert_that(&computed).is_not_equal_to(&current);

        }}", timestamp,
        self.cells.iter()
            .map(|c| c.to_test())
            .collect::<Vec<String>>()
            .join(", "),
        self.my_player.to_test(),
        self.opponent.to_test(),
        timestamp,
        current.iter()
            .map(|action| action.to_test())
            .collect::<Vec<String>>()
            .join(", "))
    }

    pub fn init(cells:Vec<(usize, CellContent, i32, i32, i32, i32, [i32; 6])>,
        my_player:Player,
        opponent:Player)->Playground {
        let parsed_cells:Vec<Cell> = cells.iter()
        .map(|cell| Cell {
            cell_index: cell.0,
            cell_content: cell.1,
            initial_resources: cell.2,
            resources: cell.3,
            my_ants: cell.4,
            opponent_ants: cell.5,
            neighbours: cell.6.to_vec(),
        })
        .collect();
        let mut returned = Playground {
            topology: Playground::compute_topology(&parsed_cells),
            cells: parsed_cells,
            my_player: my_player,
            opponent: opponent,
        };
        return returned;
    }

    fn to_pikchr(&self)->String {
        format!("TODO")
    }

    /**
     * Draw current state to timestamp
     */
    pub fn draw(&self, filename:&str) {
        let path = Path::new(&filename);
        let display = path.display();
    
        // Open a file in write-only mode, returns `io::Result<File>`
        let mut file = match File::create(&path) {
            Err(why) => panic!("couldn't create {}: {}", display, why),
            Ok(file) => file,
        };
    
        match file.write_all(self.to_pikchr().as_bytes()) {
            Err(why) => panic!("couldn't write to {}: {}", display, why),
            Ok(_) => println!("successfully wrote to {}", display),
        }
    }
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fn main() {
//    let TURN_DURATION:chrono::Duration = chrono::Duration::milliseconds(100);
    let mut playground = Playground::parse_initial_playground();
    // game loop
    loop {
        playground.parse_turn();

        let actions = playground.compute_actions();
        let text = actions
            .iter()
            .map(|a| format!("{}", a))
            .collect::<Vec<String>>()
            .join("; ");

        println!("{}", text);
        eprintln!("{}", playground.to_test(actions));
    }
}
