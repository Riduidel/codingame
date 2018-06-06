use std::io;


macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

#[derive(Debug, Copy, Clone)]
struct Fraction {
	pub signum:i32,
    pub integer_part:i32,
    pub numerator:i32,
    pub denominator:i32
}

// I don't even understand that shit !
// In fact, it's the software implementation of Euclid's algorithm.
// it requires a to be greater than b
mod euclid {
    use std::cmp::Ordering;
    pub fn gcd(m:i32, n:i32)->i32 {
        assert!(m > 0 && n > 0);
    
        match m.cmp(&n) {
            Ordering::Equal => m,
            Ordering::Less => gcd(m, n-m),
            Ordering::Greater => gcd(m-n, n),
            }
    }
}
impl Fraction {
    fn find_signum(self)->i32 {
        let mut signum = 1;
        if self.integer_part!=0 {
            signum = self.integer_part.signum();
        } else {
            signum = signum*self.numerator.signum()*self.denominator.signum();
        }
        return signum;
    }
    
    fn reduce(self)->Fraction {
        if self.denominator==0 || self.numerator==0 {
            return self;
        }
        // Now reduce fraction
        let best_reducer:i32 = euclid::gcd(self.numerator, self.denominator);
        if best_reducer>0 {
            return Fraction {
                signum:self.signum,
                integer_part:self.integer_part,
                numerator:self.numerator/best_reducer,
                denominator:self.denominator/best_reducer
            }
        } else {
            return self;
        }     
    }
    /// From a "classical" fraction, generates a fraction with the integer part isolated
    fn separate(self)->Fraction {
        if self.integer_part!=0 {
            return self;
        }
        let signum = self.find_signum();
        let next_numerator = self.numerator.abs();
        let next_denominator = self.denominator.abs();
        if self.denominator==0 {
            return self;
        }
        return Fraction {
        	signum:signum,
            integer_part:next_numerator/next_denominator,
            numerator:next_numerator%next_denominator,
            denominator:next_denominator
        }
    }
    fn simplify(self)->Fraction {
        let separated = self.separate();
//        eprintln!("separated {} into {:?}", self.to_string(), separated);
        return separated.reduce();
    }
}

impl ToString for Fraction {
    fn to_string(&self) -> String {
    	if self.denominator==0 {
    		return String::from("DIVISION BY ZERO");
    	} else {
	    	let mut returned:String = String::from("");
	    	if self.signum<0 {
	    		returned.push_str(&"-");
	    	}
	    	if self.integer_part>0 {
		    	returned.push_str(&format!("{}", self.integer_part));
		    	if self.numerator>0 {
		    		returned.push_str(&" ");
		    	}
	    	}
	    	if self.numerator>0 {
			   	returned.push_str(&format!("{}/{}", self.numerator, self.denominator));
	    	} else {
		    	if self.integer_part==0 {
		    		returned.push_str(&"0");
		    	}
	    	}
		   	return returned;
    	}
    }
}

impl From<String> for Fraction {
    /// This is an implementation of the from trait, which allow easy conversion
    /// See https://doc.rust-lang.org/rust-by-example/conversion/from_into.html
    fn from(item: String) -> Self {
        // Splitting a string is as easy as invoking the split method on string
        // But beware, it gives an iterator, NOT an array
        // See https://stackoverflow.com/a/26643821/15619
        let split:Vec<&str> = item.split("/").collect();
        Fraction {
        	signum:1,
            integer_part:0,
            // https://stackoverflow.com/a/27683271/15619
            numerator:split[0].parse().unwrap(),
            denominator:split[1].parse().unwrap()
        }
    }
}

fn simplify(number:String)->String {
    let fraction = Fraction::from(number.to_string());
    let reduced = fraction.simplify();
    eprintln!("{} reduced to {:?}", number, reduced);
	return reduced.to_string();	
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fn main() {
    #![allow(non_snake_case)]
    let mut input_line = String::new();
    io::stdin().read_line(&mut input_line).unwrap();
    let n = parse_input!(input_line, i32);
    let mut tested = vec![];
    for _i in 0..n as usize {
        let mut input_line = String::new();
        io::stdin().read_line(&mut input_line).unwrap();
        let x_y = input_line.trim().to_string();
        tested.push(x_y);
    }
    // Now reduce fractions
    for number in tested.iter() {
        println!("{}", simplify(number.to_string()));
    }
}

#[cfg(test)]
mod test;