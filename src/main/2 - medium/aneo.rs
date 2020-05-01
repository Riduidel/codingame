use std::io;
use std::fmt;
/**
OK, so now, let's try to things about that a little ...

Here we want to find the highest speed at which all lights can be passed without even slowing down.
But there is a catch : we can change the problem space from "finding the highest speed" to
finding the line with the lowest directing coefficient.

Let's draw it!

Suppose I take as x-axis the distance and as y-axis the time, the third example, which is

90
3
300 30
1500 30
3000 30

can be drawn as



   |  |           |              |
   |  |           |              |
   |  |           |              |
90s|                              
80s|                   /          
70s|                              
60s|  |          /|              |
50s|  |        /  |              |
40s|  |      /    |              |
30s|       /                      
20s|    /                         
10s|  /                           
0s x--+-----------+--------------+
     300         1500           3000

(both x and y axis are divided by 100)
First, we convert our speed in m/s (which give us 25 m/s)
So, after 10 s, our car will be at 250m.
And so on ...
But look the graphic : to see if the car speed is good, we only have to test if the light green at the distance.
*/

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

#[derive(PartialEq)]
enum Color {
    GREEN,
    RED
}
impl fmt::Display for Color {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        if self==&Color::GREEN {
            write!(f, "GREEN")
        } else {
            write!(f, "RED")
        }
    }
}

#[derive(Debug)]
struct Light {
    /* Distance of light from start */
    distance:i32,
    /* Duration of one toggle between green and red light */
    duration:i32
}
impl Light {
    fn color_at(&self, time:&i32)->Color {
        let count_cycles = (*time)/self.duration;
        eprintln!("After {} s., light {} has done {} pair of cycles.", time, self, count_cycles);
        return match count_cycles%2 {
            0 => Color::GREEN,
            _ => Color::RED
        }
    }
}
impl fmt::Display for Light {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "Light {} m. blinking in {} s.", self.distance, self.duration)
    }
}
/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
 fn main() {
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let max_speed_in_kmh = parse_input!(input_line, i32);
    eprintln!("Max speed is {} km/h", max_speed_in_kmh);
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let light_count = parse_input!(input_line, i32);
    let mut lights = vec!();
    eprintln!("There are {} lights", light_count);
    for i in 0..light_count as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let inputs = input_line.split(" ").collect::<Vec<_>>();
        let distance = parse_input!(inputs[0], i32);
        let duration = parse_input!(inputs[1], i32);
        lights.push(Light { distance, duration });
//        eprintln!("Light {} is at {} m. and change color each {} s.", i, distance, duration);
    }
//    eprintln!("lights are {:#?}", lights);

    let maximum_speed = compute_maximum_speed(max_speed_in_kmh, &lights);
    println!("{}", maximum_speed);
}

fn compute_maximum_speed(max_speed_in_kmh:i32, lights_vec:&Vec<Light>)->i32 {
    // The "+1" here is because numbere squences have exclusive end
    for speed_in_kmh in (0..(max_speed_in_kmh+1)).rev() {
        let color = last_light_at(speed_in_kmh, lights_vec);
        eprintln!("At speed {}, last light color is {}", speed_in_kmh, color);
        if(color==Color::GREEN) {
            return speed_in_kmh;
        }
    }
    return 0;
}

fn last_light_at(speed_in_kmh:i32, lights_vec:&Vec<Light>)->Color {
    // Now, compute for each light the time at which the car will reach that light
    for light in lights_vec {
        // We do not want any float number ! So manipulate things a little
        // We search for time_at_light in s., which is distance (m) * speed (m/s)
        // We know that 36 * d (m) = 10 * t (s) * v (km/h)
        let time_at_light = (36*light.distance)/(10*speed_in_kmh);
        eprintln!("It took {} s to reach light {} at speed {}", time_at_light, light, speed_in_kmh);
        let light_color = light.color_at(&time_at_light);
        eprintln!("Light is supposed to be of color {}", light_color);
        if light_color==Color::RED {
            return light_color;
        }
    }
    return Color::GREEN;
}