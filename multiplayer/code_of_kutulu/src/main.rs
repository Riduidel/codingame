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

trait PlaygroundVisitor<Content:Clone, Returned> {
    fn start_visit_playground(&mut self, visited:&Playground<Content>) {}
    fn end_visit_playground(&mut self, visited:&Playground<Content>) -> Returned {
        panic!("The end_visit_playground method SHOULD be implemented!");
    }
    fn start_visit_row(&mut self, line_index:usize, line:&Vec<Content>) {}
    fn visit_cell(&mut self, column_index:usize, line_index:usize, content:&Content) {}
    fn end_visit_row(&mut self, line_index:usize, line:&Vec<Content>) {}
}

/// This is the generic implementation of a playground, which is derived in each case
impl <'playground_lifetime, Content:Clone> Playground<Content> {

    fn accept<'visit_lifetime, Returned, Visitor:PlaygroundVisitor<Content, Returned>>
        (&'visit_lifetime self, visitor:&mut Visitor) -> Returned {
        visitor.start_visit_playground(self);
        for (line_index, line) in self.content.iter().enumerate() {
            visitor.start_visit_row(line_index, line);
            for (column_index, c) in line.iter().enumerate() {
                visitor.visit_cell(column_index, line_index, c);
            }
            visitor.end_visit_row(line_index, line);
        }
        return visitor.end_visit_playground(self);
    }

    fn set(&mut self, x:usize, y:usize, value:Content) {
        if let Some(line) = self.content.get_mut(y) {
            line[x] = value;
        }
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
#[derive(Debug, Clone, Copy)]
enum Static {
    Wall,
    Spawn,
    Empty
}

impl From<char> for Static {
    fn from(item: char) -> Self {
        match item {
            '#' => Static::Wall,
            'w' => Static::Spawn,
            _ => Static::Empty
        }
    }
}

impl ToString for Static {
    fn to_string(&self) -> String {
        match *self {
            Static::Wall => "#".to_string(),
            Static::Spawn => "w".to_string(),
            Static::Empty => " ".to_string()
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

    fn fill(&mut self, lines:Vec<String>) {
        for (line_index, line) in lines.iter().enumerate() {
            if let Some(line_vec) = self.content.get_mut(line_index) {
                for (column_index, c) in line.chars().enumerate() {
                    line_vec[column_index] = Static::from(c);
                }
            }
        }
    }
}

struct Filler {
    prefix:Box<String>,
    text:Box<String>,
    width:usize,
    height:usize
}

impl Filler {
    fn on(playground:&Playground<Static>, prefix:&str) -> String {
        let mut filler = Filler {
            width:playground.width,
            height:playground.height,
            prefix:Box::new(prefix.to_string()),
            text:Box::new("".to_owned())
        };
        return playground.accept(&mut filler);
    }
}

impl PlaygroundVisitor<Static, String> for Filler {
    fn start_visit_playground(&mut self, visited:&Playground<Static>) {
        self.text.push_str(&self.prefix);
        self.text.push_str(&format!("let playground = Playground::new({}, {});\n", visited.width, visited.height).to_owned());
        self.text.push_str(&self.prefix);
        self.text.push_str(&format!("playground.fill(vec![\n"));
    }
    fn end_visit_playground(&mut self, visited:&Playground<Static>) -> String {
        self.text.push_str(&self.prefix);
        self.text.push_str("\t]);\n");
        return *self.text.clone();
    }
    fn start_visit_row(&mut self, line_index:usize, line:&Vec<Static>) {
        self.text.push_str(&self.prefix);
        self.text.push_str("\t\"");
    }
    fn visit_cell(&mut self, column_index:usize, line_index:usize, content:&Static) {
        self.text.push_str(&content.to_string());
    }
    fn end_visit_row(&mut self, line_index:usize, line:&Vec<Static>) {
        self.text.push_str("\"");
        if line_index<self.height-1 {
        self.text.push_str(",");
        }
        self.text.push_str("\n");
    }
}

impl ToUnitTest for Playground<Static> {
    fn to_unit_test(&self, prefix:&str) -> String {
        return Filler::on(self, prefix);
    }
}

#[derive(Clone)]
struct Game {
    playground:Playground<Static>,
    sanity_loss_lonely:i32,
    sanity_loss_group:i32,
    wanderer_spawn_time:i32,
    wanderer_life_time:i32
}

impl Game {
    fn new(playground:Playground<Static>, sanity_loss_lonely:i32, sanity_loss_group:i32, wanderer_spawn_time:i32, wanderer_life_time:i32) -> Game {
        return Game {
            playground: playground,
            sanity_loss_lonely: sanity_loss_lonely,
            sanity_loss_group: sanity_loss_group,
            wanderer_spawn_time: wanderer_spawn_time,
            wanderer_life_time: wanderer_life_time
        }
    }
}

impl ToUnitTest for Game {
    fn to_unit_test(&self, prefix:&str) -> String {
        let mut returned = "".to_string();
        returned.push_str(&self.playground.to_unit_test(prefix));
        returned.push_str(&prefix);
        returned.push_str(
            &format!(
                "let game = Game::new(playground, {}, {}, {}, {});\n",
                self.sanity_loss_lonely,
                self.sanity_loss_group,
                self.wanderer_spawn_time,
                self.wanderer_life_time
            ));
        return returned;
    }
}

enum EntityType {
    EXPLORER,
    WANDERER
}
impl From<String> for EntityType {
    fn from(item:String) -> Self {
        match item.as_ref() {
            "EXPLORER" => EntityType::EXPLORER,
            _ => EntityType::WANDERER
        }
    }
}

impl ToString for EntityType {
    fn to_string(&self) -> String {
        match *self {
            EntityType::EXPLORER => "EXPLORER".to_string(),
            EntityType::WANDERER => "WANDERER".to_string()
        }
    }
}

struct Entity {
    entity_type:EntityType,
    id:i32,
    x:i32,
    y:i32,
    ///Explorer: sanity | Spawning minion: time before spawn | Wanderer: time before being recalled
    param0:i32,
    /// Explorer: ignore for this league | Minion: Current state amongst those -> SPAWNING = 0 , WANDERING = 1
    param1:i32,
    /// Explorer: ignore for this league | Minion: id  of the explorer targeted by this minion. -1 if no target (occurs only on spawn)
    param2:i32
}

impl ToUnitTest for Entity {
    fn to_unit_test(&self, prefix:&str) -> String {
        let mut returned = "".to_string();
        returned.push_str(
            &format!("Entity {{ entity_type:{}, id:{}, x:{}, y:{}, param0:{}, param1:{}, param2:{} }}",
                self.entity_type.to_string(),
                self.id,
                self.x,
                self.y,
                self.param0,
                self.param1,
                self.param2
            ));
        return returned;
    }
}

struct Round {
    game: Game,
    entities:Vec<Entity>
}

impl Round {
    fn new(game:Game, entities:Vec<Entity>) -> Round {
        return Round { game: game, entities: entities };
    }
}
impl ToUnitTest for Round {
    fn to_unit_test(&self, prefix:&str) -> String {
        let mut returned = "".to_string();
        returned.push_str(&self.game.to_unit_test(prefix));
        returned.push_str(prefix);
        returned.push_str("let entities = vec![\n");
        for (index, entity) in self.entities.iter().enumerate() {
            returned.push_str(prefix);
            returned.push_str("\t");
            returned.push_str(&entity.to_unit_test(prefix));
            if index<self.entities.len()-1 {
                returned.push_str(",");
            }
            returned.push_str("\n");
        }
        returned.push_str(prefix);
        returned.push_str("\t];\n");
        returned.push_str(prefix);
        returned.push_str("let round = Round::new(game, entities);");
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
    let mut playground:Playground<Static> = Playground::new(width as usize, height as usize);
    let mut lines = vec![];
    for _ in 0..height as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let line = input_line.trim_right().to_string();
        lines.push(line);
    }
    playground.fill(lines);
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let inputs = input_line.split(" ").collect::<Vec<_>>();
    let sanity_loss_lonely = parse_input!(inputs[0], i32); // how much sanity you lose every turn when alone, always 3 until wood 1
    let sanity_loss_group = parse_input!(inputs[1], i32); // how much sanity you lose every turn when near another player, always 1 until wood 1
    let wanderer_spawn_time = parse_input!(inputs[2], i32); // how many turns the wanderer take to spawn, always 3 until wood 1
    let wanderer_life_time = parse_input!(inputs[3], i32); // how many turns the wanderer is on map after spawning, always 40 until wood 1
    let game = Game::new(playground, sanity_loss_lonely, sanity_loss_group, wanderer_spawn_time, wanderer_life_time);

    // game loop
    loop {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let entity_count = parse_input!(input_line, i32); // the first given entity corresponds to your explorer
        let mut entities:Vec<Entity> = vec![];
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
            entities.push(Entity {
                entity_type: EntityType::from(entity_type),
                id: id,
                x: x,
                y: y,
                param0: param_0,
                param1: param_1,
                param2: param_2
            })
        }
        // One new reference of round is generated each ... well ... round, so game is no more owned
        // I guess there should be a borrow thing
        let mut round = Round::new(game.clone(), entities);
        // Write an action using println!("message...");
        // To debug: eprintln!("Debug message...");

    	eprintln!("{}", round.can_compute_at());
        println!("WAIT"); // MOVE <x> <y> | WAIT
    }
}

#[cfg(test)]
mod test;