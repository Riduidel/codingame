use std::io;
use std::time::{SystemTime, UNIX_EPOCH};

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

/******************************************************************************************/
/********************************** UNIT TEST GENERATOR ***********************************/
/******************************************************************************************/
const FUNCTION_PREFIX:&'static str = "\t";
const CODE_PREFIX:&'static str = "\t\t";

fn current_time_millis() -> i64 {
    let current_time = SystemTime::now();
    let since_the_epoch = current_time.duration_since(UNIX_EPOCH)
        .expect("Time went backwards");
    let milliseconds = (since_the_epoch.as_secs() as i64 * 1000) + 
                (since_the_epoch.subsec_nanos() as i64 / 1000 / 1000);
    return milliseconds;
}
trait ToUnitTest {
    /// This function should not be overwriten by trait implementors
    fn can_compute_at(&self) -> String {
        let mut function = "".to_owned();
        function.push_str(FUNCTION_PREFIX);
        function.push_str("#[test]\n");
        function.push_str(FUNCTION_PREFIX);
        function.push_str(&format!("fn can_compute_at_{:?}() {{\n", current_time_millis()));
        function.push_str(&*self.to_unit_test(CODE_PREFIX));
        function.push_str(FUNCTION_PREFIX);
        function.push_str("}}\n");
        return function;
    }

    fn to_unit_test(&self, prefix:&str) -> String {
        panic!(format!("ToUnitTest::to_unit_test SHOULD be overwritten by implementors of ToUnitTest"));
    }
}

/******************************************************************************************/
/************************************* PLAYGROUND *****************************************/
/******************************************************************************************/
#[derive(Clone)]
struct Playground<Content : Clone> {
    width:usize,
    height:usize,
    /// This vector stores elements by row then colum (so y is before x)
    content:Box<Vec<Vec<Content>>>
}

/// This is the generic implementation of a playground, which is derived in each case
impl <'playground_lifetime, Content:Clone> Playground<Content> {

    fn set(&mut self, x:usize, y:usize, value:Content) {
        let line = self.content
            .get_mut(y as usize)
            .expect(&format!("I tried to read line {} in a playground of height {}", y, self.height));
        line[x as usize] = value;
    }

    fn get<'get_lifetime>(&'get_lifetime self, x:usize, y:usize)-> &'get_lifetime Content {
        let line = self.content
            .get(y as usize)
            .expect(&format!("I tried to read line {} in a playground of height {}", y, self.height));
        // Notice how the ";" implies I need to add a return statement, which I'm not against at all
        return line.into_iter()
            .nth(x)
            .expect(&format!("Cannot read content from ({};{})", x, y));
    }
}

/******************************************************************************************/
/******************************** STATIC PLAYGROUND ELEMENTS ******************************/
/******************************************************************************************/
#[derive(Clone, Copy)]
enum Static {
    Wall,
    Spawn,
    Empty
}

impl Static {
    fn parse(c:char)->Static {
        match c {
            '#' => Static::Wall,
            'w' => Static::Spawn,
            _ => Static::Empty
        }
    }
}

/******************************************************************************************/
/********************************* DEDICATED PLAYGROUND IMPL ******************************/
/******************************************************************************************/
impl <'static_playground_lifetime> Playground<Static> {
    fn new(width:usize, height:usize)-> Playground<Static> {
        // Build the vector
        let mut content:Vec<Vec<Static>> = vec![];
        for _ in 0..height {
            let row:Vec<Static> = vec![Static::Empty; width as usize];
            content.push(row);
        }
        // Then create the struct
        return Playground { width:width, height:height, content:Box::new(content) };
    }

    fn set_char(mut self, x:usize, y:usize, c:char) {
        self.set(x, y, Static::parse(c));
    }

    fn fill(mut self, lines:Vec<String>) {
        for (line_index, line) in lines.iter().enumerate() {
            for (column_index, c) in line.chars().enumerate() {
                self.clone().set_char(column_index, line_index, c);
            }
        }
    }
}

impl ToUnitTest for Playground<Static> {
    fn to_unit_test(&self, prefix:&str) -> String {
        let mut returned = CODE_PREFIX.to_owned();
        returned.push_str(&format!("let tested = Playground::new({}, {});\n", self.width, self.height).to_owned());
        returned.push_str(prefix);
        returned.push_str("tested.fill(vec![\n");
        returned.push_str(prefix);
        returned.push_str("\t");
        returned.push_str("]);\n");
        return returned;
    }
}

/**
 * Survive the wrath of Kutulu
 * Coded fearlessly by JohnnyYuge & nmahoude (ok we might have been a bit scared by the old god...but don't say anything)
 **/
fn main() {
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let width = parse_input!(input_line, i32);
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let height = parse_input!(input_line, i32);
    let playground:Playground<Static> = Playground::new(width as usize, height as usize);
    let mut lines = vec![];
    for _ in 0..height as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let line = input_line.trim_right().to_string();
        lines.push(line);
    }
    playground.clone().fill(lines);
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let inputs = input_line.split(" ").collect::<Vec<_>>();
    let sanity_loss_lonely = parse_input!(inputs[0], i32); // how much sanity you lose every turn when alone, always 3 until wood 1
    let sanity_loss_group = parse_input!(inputs[1], i32); // how much sanity you lose every turn when near another player, always 1 until wood 1
    let wanderer_spawn_time = parse_input!(inputs[2], i32); // how many turns the wanderer take to spawn, always 3 until wood 1
    let wanderer_life_time = parse_input!(inputs[3], i32); // how many turns the wanderer is on map after spawning, always 40 until wood 1

    // game loop
    loop {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let entity_count = parse_input!(input_line, i32); // the first given entity corresponds to your explorer
        for i in 0..entity_count as usize {
            let mut input_line = String::new();
            io::stdin().read_line(&mut input_line).unwrap();
            let inputs = input_line.split(" ").collect::<Vec<_>>();
            let entity_type = inputs[0].trim().to_string();
            let id = parse_input!(inputs[1], i32);
            let x = parse_input!(inputs[2], i32);
            let y = parse_input!(inputs[3], i32);
            let param_0 = parse_input!(inputs[4], i32);
            let param_1 = parse_input!(inputs[5], i32);
            let param_2 = parse_input!(inputs[6], i32);
        }

        // Write an action using println!("message...");
        // To debug: eprintln!("Debug message...");

    	eprintln!("{}", playground.can_compute_at());
        println!("WAIT"); // MOVE <x> <y> | WAIT
    }
}