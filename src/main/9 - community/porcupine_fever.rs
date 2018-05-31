use std::io;
use std::cmp;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

#[derive(Debug, Copy, Clone)]
struct Cage {
    pub sick:i32,
    pub healthy:i32,
    pub alive:i32
}

impl Cage {
    pub fn derive(self)->Cage {
        let next_alive = self.alive - self.sick;
        let next_sick = cmp::min(self.sick*2, next_alive);
        let next_healthy = self.alive - next_sick;
        return Cage {
            sick: next_sick,
            healthy: next_healthy,
            alive: next_alive
        }
    }
}

fn main() {
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    // Number of porcupine cages
    let n = parse_input!(input_line, i32);
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    // Number of years to wait
    let y = parse_input!(input_line, i32);
    // All the cages are belong to this vector
    let mut cages:Vec<Cage> = vec![];
    for i in 0..n as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let s = parse_input!(inputs[0], i32);
        let h = parse_input!(inputs[1], i32);
        let a = parse_input!(inputs[2], i32);
        cages.push(Cage{sick:s, healthy:h, alive:a});
    }
    eprintln!("{:?}", cages);
    for year in 0..y {
        // The iter pattern is explained at https://stackoverflow.com/a/30026986/15619
        // In quite an un-intuitive way, to have map working, the struct must derive Copy and Clone
        // To have references copiable (see https://stackoverflow.com/a/28527789/15619)
        let mut next_cages:Vec<Cage> = cages.iter().map(|c| c.derive()).collect();
//        eprintln!("At year {},\ncages are {:?}\nnext are {:?}", year, cages, next_cages);
        cages = next_cages;
        let count = cages.iter()
            .map(|c| c.alive)
            .fold(0, |acc, count| acc + count);
        println!("{}", count);
        if count==0 {
            return;
        }
    }
}