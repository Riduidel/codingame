use std::io;
use std::collections::BTreeMap;
use std::time::{SystemTime, UNIX_EPOCH};

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

/******************************************************************************************/
/********************************** UNIT TEST GENERATOR ***********************************/
/******************************************************************************************/
const FUNCTION_PREFIX:&'static str = "";
const CODE_PREFIX:&'static str = "\t";

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
    fn can_compute_at(&self, assert:&String) -> String {
        let mut function = "".to_owned();
        function.push_str(FUNCTION_PREFIX);
        function.push_str("#[test]\n");
        function.push_str(FUNCTION_PREFIX);
        function.push_str(&format!("fn can_compute_at_{:?}() {{\n", current_time_millis()));
        function.push_str(&*self.to_unit_test(CODE_PREFIX));
        function.push_str(assert);
        function.push_str(FUNCTION_PREFIX);
        function.push_str("}\n");
        return function;
    }

    fn to_unit_test(&self, prefix:&str) -> String {
        panic!(format!("ToUnitTest::to_unit_test SHOULD be overwritten by implementors of ToUnitTest"));
    }
}

/******************************************************************************************/
/************************************** A POINT *******************************************/
/******************************************************************************************/
#[derive(Debug, Copy, Clone)]
struct Point {
    x:i32,
    y:i32
}


impl Point {
    fn directions()->Vec<Point> {
        return vec![
            Point { x:-1, y:0 },
            Point { x:1, y:0 },
            Point { x:0, y:1 },
            Point { x:0, y:-1 },
        ];
    }
    fn move_of(&self, other:&Point) -> Point {
        return self.move_of_offset(other.x, other.y);
    }
    fn move_of_offset(&self, x:i32, y:i32) -> Point {
        return Point {
            x:self.x + x,
            y:self.y + y,
        }
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
    fn new(width:usize, height:usize, default_value:Content)-> Playground<Content> {
        // Build the vector
        let mut content:Vec<Vec<Content>> = vec![];
        for _ in 0..height {
            let row:Vec<Content> = vec![default_value.clone(); width as usize];
            content.push(row);
        }
        // Then create the struct
        return Playground { width:width, height:height, content:Box::new(content) };
    }

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

    fn set_at(&mut self, point:Point, value:Content) {
        self.set(point.x as usize, point.y as usize, value);
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

    fn contains(&self, point:&Point)->bool {
        return
            point.x>=0 && (point.x as usize)<self.width &&
            point.y>=0 && (point.y as usize)<self.height;
    }

    fn get_at<'get_at_lifetime>(&'get_at_lifetime self, point:&Point)-> &'get_at_lifetime Content {
        return self.get(point.x as usize, point.y as usize);
    }
}

/******************************************************************************************/
/******************************** STATIC PLAYGROUND ELEMENTS ******************************/
/******************************************************************************************/
#[derive(PartialEq, Debug, Clone, Copy)]
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
    fn fill(&mut self, lines:Vec<String>) {
        for (line_index, line) in lines.iter().enumerate() {
            if let Some(line_vec) = self.content.get_mut(line_index) {
                for (column_index, c) in line.chars().enumerate() {
                    line_vec[column_index] = Static::from(c);
                }
            }
        }
    }
    fn allow(&self, point:&Point) -> bool {
        if self.contains(point) {
            return *self.get_at(point)==Static::Empty;
        }
        return false;
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
        self.text.push_str(&format!("let mut playground = Playground::new({}, {}, Static::Empty);\n", visited.width, visited.height).to_owned());
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
        self.text.push_str("\".to_string()");
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

    fn allow(&self, point:&Point) -> bool {
        return self.playground.allow(point);
    }
    fn find_moves_at(&self, point:Point) -> Vec<Point> {
        Point::directions().iter()
            .map(|d| point.move_of(d))
            .filter(|p| self.allow(p))
            .collect()
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

#[derive(Clone, Copy, Debug, PartialEq)]
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

#[derive(Debug, Clone, Copy, PartialEq)]
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

impl From<Entity> for Point {
    fn from(item: Entity) -> Self {
        Point {x: item.x, y: item.y }
    }
}

impl <'from> From<&'from Entity> for Point {
    fn from(item: &'from Entity) -> Self {
        Point {x: item.x, y: item.y }
    }
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

#[derive(Clone)]
struct Round {
    game: Game,
    entities:Box<Vec<Entity>>,
    entities_playground:Playground<Option<Entity>>
}

const GROUP_RANGE:i32=2;

impl Round {
    fn new(game:Game, entities:Vec<Entity>) -> Round {
        let mut entities_playground:Playground<Option<Entity>> = Playground::new(game.playground.width, game.playground.height, None);
        for entity in entities.clone() {
            entities_playground.set_at(Point::from(entity), Some(entity));
        }
        return Round { game: game, 
            entities: Box::new(entities),
            entities_playground: entities_playground
        };
    }

    fn find_my_explorers(&self) -> &[Entity] {
        return &self.entities[0..1];
    }

    fn change_score_around(returned:&mut Playground<i32>, point:Point, distance:i32, score:i32) {
        for x in -distance..distance {
            for y in -distance..distance {
                let moved = point.move_of_offset(x, y);
                if returned.contains(&moved) {
                    let new_score = returned.get_at(&moved)+score;
                    returned.set_at(moved, new_score);
                }
            }
        }
    }

    fn compute_scores(&self) -> Playground<i32> {
        let mut returned:Playground<i32> = Playground::new(self.game.playground.width, self.game.playground.height, 0);
        let my = self.find_my_explorers();
        for entity in self.entities.iter() {
            if !my.contains(entity) {
                let current_position:Point = entity.into();
                match entity.entity_type {
                    EntityType::WANDERER => {
                        Round::change_score_around(&mut returned, current_position, GROUP_RANGE, -100);
                    },
                    EntityType::EXPLORER => {
                        Round::change_score_around(&mut returned, current_position, GROUP_RANGE, 5);
                    }
                }
            }
        }
        return returned;
    }

    fn compute(&self) -> String {
        let my = self.find_my_explorers();
        let mut returned = "".to_string();
        let scores = self.compute_scores();
        for explorer in my {
            let positions = self.game.find_moves_at(Point::from(explorer));
            let mut scored:BTreeMap<i32, Point> = BTreeMap::new();
            for p in positions.iter() {
                let local_score = scores.get_at(p);
                scored.insert(*local_score, *p);
            }
            match scored.iter().next() {
                Some(entry) => returned.push_str(&format!("MOVE {} {} Score is {}", entry.1.x, entry.1.y, entry.0)),
                None => returned.push_str("WAIT I don't know what to do")
            }
        }
    	eprintln!("{}", self.can_compute_at(&format!("\tassert!(round.compute()!=\"{}\");\n", returned)));
        return returned;
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
        returned.push_str("let round = Round::new(game, entities);\n");
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
    let mut playground:Playground<Static> = Playground::new(width as usize, height as usize, Static::Empty);
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

        println!("{}", round.compute()); // MOVE <x> <y> | WAIT
    }
}

#[cfg(test)]
mod test;