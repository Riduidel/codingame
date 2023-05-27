use std::io;
use std::fmt;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

/** Possible actions */
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
pub enum CellContent {
    EMPTY = 0,
    EGG,
    CRYSTAL
}

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
}

pub struct Player {
    /**
     * Indices of cells where player has bases
     */
    bases:Vec<usize>
}

pub struct Playground {
    cells:Vec<Cell>
}

impl Playground {
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
            cells.push(Cell {
                cell_index:i,
                cell_content: unsafe { ::std::mem::transmute(cell_type) },
                initial_resources: initial_resources,
                resources: 0,
                my_ants: 0,
                opponent_ants: 0,
                neighbours: vec!(neigh_0, neigh_1, neigh_2, neigh_3, neigh_4, neigh_5)
            });
        }
        Playground {
            cells: cells
        }
    
    }
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fn main() {
    let mut playground = Playground::parse_initial_playground();
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let number_of_bases = parse_input!(input_line, i32);
    let mut my_bases:Vec<usize> = Vec::with_capacity(number_of_bases as usize);
    let mut inputs = String::new();
    io::stdin().read_line(&mut inputs).unwrap();
    for i in inputs.split_whitespace() {
        let my_base_index = parse_input!(i, usize);
        my_bases.push(my_base_index);
    }
    let mut enemy_bases:Vec<usize> = Vec::with_capacity(number_of_bases as usize);
    let mut inputs = String::new();
    io::stdin().read_line(&mut inputs).unwrap();
    for i in inputs.split_whitespace() {
        let opp_base_index = parse_input!(i, usize);
        enemy_bases.push(opp_base_index);
    }
    let my_player = Player {
        bases: my_bases
    };
    let enemy = Player {
        bases: enemy_bases
    };

    // game loop
    loop {
        for cell_index in 0..playground.cells.len() as usize {
            let mut input_line = String::new();
            io::stdin().read_line(&mut input_line).unwrap();
            let inputs = input_line.split(" ").collect::<Vec<_>>();
            let resources = parse_input!(inputs[0], i32); // the current amount of eggs/crystals on this cell
            let my_ants = parse_input!(inputs[1], i32); // the amount of your ants on this cell
            let opp_ants = parse_input!(inputs[2], i32); // the amount of opponent ants on this cell
            playground.cells.get_mut(cell_index).unwrap()
                .update(resources, my_ants, opp_ants);

        }

        // Find all cells having resources
        let targets:Vec<&Cell> = playground.cells
            .iter()
            .filter(|cell| cell.resources>0)
            .collect();
        // Count the number of targets
        let targets_count = targets.len();
        // Write an action using println!("message...");
        // To debug: eprintln!("Debug message...");
        let actions:Vec<Action> = my_player.bases
            .iter()
            .flat_map(|base_index| {
                let base_cell = &playground.cells[*base_index];
                let actions:Vec<Action> = targets.iter()
                    .map(|cell| {
                        Action::LINE { start_index: base_index.clone(), end_index: cell.cell_index.clone(), strenght: 1}
                    })
                    .collect();
                actions
            })
            .collect();
        let text = actions
            .iter()
            .map(|a| format!("{}", a))
            .collect::<Vec<String>>()
            .join("; ");
        println!("{}", text);
        
    }
}
