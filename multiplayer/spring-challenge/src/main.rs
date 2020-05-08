use std::io;
use std::fmt;
use std::time::SystemTime;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

#[derive(Debug, Clone, Copy)]
struct Point {
    row:i32,
    col:i32
}

impl Point {
    fn distance_to(&self, other:&Point)->i32 {
        (self.row-other.row).abs() + (self.col-other.col).abs()
    }

    fn directions()->Vec<Point> {
        return vec![
            Point { row: -1, col: 0 },
            Point { row:1, col:0 },
            Point { row: 0, col: -1},
            Point { row:0, col: 1}
        ]
    }

    fn move_of(&self, other:&Point)->Point {
        return Point {
            row: self.row+other.row,
            col: self.col+other.col
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Cell  /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
#[derive(Debug, Clone, Copy)]
enum Cell {
    Pill {
        score:i32,
    },
    Ground,
    Wall
}

impl Cell {
    fn for_char(cell:char)->Cell {
        match cell {
            'O' => Cell::Pill { score:10},
            '.' => Cell::Pill { score:1},
            ' ' => Cell::Ground,
            '#' => Cell::Wall,
            _ => panic!("Seems like there was an unrecognized character: \"{}\"", cell)
        }
    }
    fn allow(&self)->bool {
        match self {
            Cell::Pill { score } => true,
            Cell::Ground => true,
            Cell::Wall => false
        }
    }
    fn pac_on(&self)->(&Cell, i32) {
        match self {
            Cell::Pill { score } => (&Cell::Ground, *score),
            _ => (self, 0)
        }
    }
}

impl fmt::Display for Cell {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", match self {
            Cell::Pill { score } => if score>&1 { "O" } else { "." },
            Cell::Ground => " ",
            Cell::Wall => "#"
        })
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////// Playground  /////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/*
 * Full playground, with all cells defined
 */
#[derive(Debug, Clone)]
struct Playground {
    width: i32,
    height:i32,
    playground:Vec<Vec<Cell>>
}

impl Playground {
    fn new(width:i32, height:i32) -> Playground {
        Playground {
            width: width,
            height: height,
            playground: vec![]
        }
    }
    fn new_from(other:Playground) -> Playground {
        Playground::new(other.width, other.height)
    }
    fn new_from_str(playground_at_turn:&String)->Playground {
        let rows:Vec<&str> = playground_at_turn.split("\n").collect();
        let height = rows.len() as i32;
        let width = rows[0].len() as i32;
        let mut returned = Playground::new(width, height);
        rows.iter()
            .for_each(|row| {
                returned.add_row(row.to_string());
            });
        return returned;
    }
    fn add_row(&mut self, row_content:String) {
        let mut row_content = row_content;
        while row_content.len()<self.width as usize {
            row_content.push(' ');
        }
        // Don't forget to pad line to given width
        self.playground.push(row_content.chars()
            .map(|c| Cell::for_char(c))
            .collect());
    }

    fn set(&mut self, row:usize, col:usize, cell:Cell) {
        self.playground[row][col] = cell;
    }

    /*
     * Parse a playground representation and build a pair of playgrounds from that representation.
     */
    fn parse(playground_at_turn:&str)->(Playground, Playground) {
        let initial_playground = Playground::new_from_str(&playground_at_turn.replace(".", " ").replace("o", " "));
        let in_turn_playground = Playground::new_from_str(&playground_at_turn.to_owned());

        return (initial_playground, in_turn_playground)
    }

    fn can_have_pac_at(&self, pac:&Pac)->bool {
        return self.playground[pac.position.row as usize][pac.position.col as usize].allow();
    }

    fn recompute_position_according_to_bounds(&self, position:&Point)->Point {
        let moved_row= if position.row<0 {
            position.row+self.height
        } else if position.row>=self.height {
            position.row-self.height
        } else {
            position.row
        };
        let moved_col = if position.col<0 {
            position.col+self.width
        } else if position.col>=self.width {
            position.col-self.width
        } else {
            position.col
        };
        return Point { row:moved_row, col:moved_col }
    }

    fn with_pac_on(&mut self, pac:&Pac)->i32 {
        let row = pac.position.row as usize;
        let col = pac.position.col as usize;
        let cell = self.playground[row][col];
        let (next_cell, score) = cell.pac_on();
        self.set(row, col, *next_cell);
        return score;
    }

    fn move_pac_on(&self, possible_pac:Pac)->Option<(Playground, Pac, i32)> {
        let mut possible_pac = possible_pac;
        possible_pac.position = self.recompute_position_according_to_bounds(&possible_pac.position);
        let mut future = self.clone();
        if future.can_have_pac_at(&possible_pac) {
            // Don't forget to change pac cell! (and to score accordingly)
            let score = future.with_pac_on(&possible_pac);
            return Some((future, possible_pac, score));
        } else {
            return None;
        }
    }

    fn compute_pac_move(&self, pac:&Pac)->String {
        let mut sorted_tuples = Point::directions().iter()
            .map(|direction| self.move_pac_on(pac.move_of(direction)))
            .filter(|option| option.is_some())
            .map(|option| option.unwrap())
            .collect::<Vec<(Playground, Pac, i32)>>();
        sorted_tuples.sort_by(|a, b| b.2.cmp(&a.2));
        let best_tuple:&(Playground, Pac, i32) = &sorted_tuples[0];
        // then go to nearest small pill
        format!("MOVE {} {} {}", pac.pac_id, best_tuple.1.position.col, best_tuple.1.position.row)
    }

    fn compute_pacs_moves(&self, pacs:&Vec<Pac>)->Vec<String> {
        pacs.iter()
            .filter(|p| p.mine)
            .map(|p| self.compute_pac_move(p))
            .collect()
    }
}

impl fmt::Display for Playground {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        self.playground.iter()
            .for_each(|row| {
                row.iter().for_each(|cell| write!(f, "{}", cell).unwrap());
                write!(f, "\n").unwrap();
            });
        fmt::Result::Ok(())
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Pacs  /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
#[derive(Debug, Clone)]
struct Pac {
    pac_id:i32, // pac unique id
    mine: bool, // : le propriétaire du pac (1 si ce pac est à vous, 0 sinon)
    position:Point,
    type_id:String,
    speed_turns_left:i32,
    ability_cooldown:i32
}

impl Pac {
    fn move_of(&self, direction:&Point)->Pac {
        let mut moved = self.clone();
        moved.position = self.position.move_of(direction);
        moved
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// Utilities  ////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
fn to_test(playground:&Playground, pacs:&Vec<Pac>, commands:&Vec<String>)->String {
    let timestamp = match SystemTime::now().duration_since(SystemTime::UNIX_EPOCH) {
        Ok(n) => n.as_secs(),
        Err(_) => 0,
    };
    return format!("\t#[test]
    fn test_move_at_{}() {{
        let (_playground, at_turn) = Playground::parse(\"{}\");
        let pacs = vec![{}];

        let effective_moves = at_turn.compute_pacs_moves(&pacs);
        let expected_moves = vec![{}];
        for i in 0..effective_moves.len() {{
            assert_ne!(effective_moves[i], expected_moves[i])
        }}
    }}
    ", timestamp, 
        playground,
        pacs.iter()
            .map(|p| format!(
                "Pac {{ pac_id: {}, mine: {}, position: Point {{ col: {}, row: {} }}, type_id: \"{}\".to_owned(), speed_turns_left: {}, ability_cooldown: {} }}",
                p.pac_id, p.mine, p.position.col, p.position.row, p.type_id, p.speed_turns_left, p.ability_cooldown
            ) )
            .fold(String::new(), |a, b| if a.len()>0 {
                a + ",\n\t\t" + &b
            } else {
                b
            }),
        commands.iter()
            .map(|s| format!("\"{}\"", s))
            .fold(String::new(), |a, b| if a.len()>0 {
                a + ",\n\t\t" + &b
            } else {
                b.to_string()
            })
);
}
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// Main  ///////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Grab the pellets as fast as you can!
 **/
fn main() {
    /////////////////////////////////////////////////////////////////////////////////////////////////
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let inputs = input_line.split(" ").collect::<Vec<_>>();
    let width = parse_input!(inputs[0], i32); // size of the grid
    let height = parse_input!(inputs[1], i32); // top left corner is (x=0, y=0)
    let mut playground = Playground::new(width, height);
    for i in 0..height as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let row = input_line.trim_end().to_string(); // one line of the grid: space " " is floor, pound "#" is wall
        playground.add_row(row);
    }
    eprintln!("Playground is \n{}", playground);
    /////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// Initialization of playground is over //////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    // game loop
    loop {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let my_score = parse_input!(inputs[0], i32);
        let opponent_score = parse_input!(inputs[1], i32);
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let visible_pac_count = parse_input!(input_line, i32); // all your pacs and enemy pacs in sight
        let mut pacs = vec![];
        for i in 0..visible_pac_count as usize {
            let mut input_line = String::new();
            io::stdin().read_line(&mut input_line).unwrap();
            let inputs = input_line.split(" ").collect::<Vec<_>>();
            let pac_id = parse_input!(inputs[0], i32); // pac number (unique within a team)
            let mine = parse_input!(inputs[1], i32)!=0; // true if this pac is yours
            let col = parse_input!(inputs[2], i32); // position in the grid
            let row = parse_input!(inputs[3], i32); // position in the grid
            let type_id = inputs[4].trim().to_string(); // unused in wood leagues
            let speed_turns_left = parse_input!(inputs[5], i32); // unused in wood leagues
            let ability_cooldown = parse_input!(inputs[6], i32); // unused in wood leagues
            pacs.push(Pac {pac_id, mine,position: Point {col, row}, type_id, speed_turns_left, ability_cooldown});
        }
        // Get a new playground from the standard one
        let mut playground_at_turn = playground.clone();
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let visible_pellet_count = parse_input!(input_line, i32); // all pellets in sight
        for i in 0..visible_pellet_count as usize {
            let mut input_line = String::new();
            io::stdin().read_line(&mut input_line).unwrap();
            let inputs = input_line.split(" ").collect::<Vec<_>>();
            let x = parse_input!(inputs[0], usize);
            let y = parse_input!(inputs[1], usize);
            let score = parse_input!(inputs[2], i32); // amount of points this pellet is worth
            playground_at_turn.set(y, x, Cell::Pill { score });
        }

        // Write an action using println!("message...");
        // To debug: eprintln!("Debug message...");
        let moves = playground_at_turn.compute_pacs_moves(&pacs);
        eprintln!("{}", to_test(&playground_at_turn, &pacs, &moves));

        moves.iter().for_each(|m| println!("{}", m));
    }
}


#[cfg(test)]
mod tests {
    use super::*;

	#[test]
    fn test_move_at_1588879197() {
        let (_playground, at_turn) = Playground::parse("###################################
#...#....O#.#...#.#...#.#O....#...#
#.#.#.###.#.#.#.#.#.#.#.#.###.#.#.#
#.#.....#.#...#.....#...#.#.....#.#
#.#####.#.#.###.###.###.#.#.#####.#
#.......   .....#.#.......  ......#
###.###.#.#.###.#.#.###.#.#.###.###
........#.#...#.#.#.#...#.#........
###.#####.###.#.#.#.#.###.#####.###
###.......#...O.....O...#.......###
###.###.#.#.###.#.#.###.#.#.###.###
#...#...#...###.#.#.###...#...#...#
#.#.#.#.###.###.###.###.###.#.#.#.#
#.#...#.#.................#.#...#.#
###.#.#.#.#.#.###.###.#.#.#.#.#.###
###.#.#...#.#...#.#...#.#...#.#.###
###################################
");
        let pacs = vec![Pac { pac_id: 0, mine: true, position: Point { col: 10, row: 5 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 },
		Pac { pac_id: 0, mine: false, position: Point { col: 26, row: 5 }, type_id: "NEUTRAL".to_owned(), speed_turns_left: 0, ability_cooldown: 0 }];
        let effective_moves = at_turn.compute_pacs_moves(&pacs);
        let expected_moves = vec!["MOVE 0 15 10"];
        for i in 0..effective_moves.len() {
            assert_ne!(effective_moves[i], expected_moves[i])
        }
    }
}