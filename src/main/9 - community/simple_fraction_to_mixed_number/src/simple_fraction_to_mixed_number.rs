use std::io;
use std::cell::Cell;

macro_rules! parse_input {
    ($x:expr, $t:ident) => ($x.trim().parse::<$t>().unwrap())
}

//////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// PRIMES NUMBERS COMPUTER ////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
struct Primes {
   // Contain all primes up to a given value built from the primes_upto fonction
    PRIMES:Cell<Vec<i32>>,
    // Contain for each number upto the given value the potential for this number to be a prime
    NUMBERS:Cell<Vec<bool>>
}

impl Primes {
    pub fn new()->Primes {
        Primes {
            PRIMES:Cell::new(vec![]),
            NUMBERS:Cell::new(vec![])
        }
    }
    fn extend_numbers_up_to(&mut self, upper_bound:i32) {
        let current_numbers = self.NUMBERS.get_mut();
        while (current_numbers.len() as i32)<=upper_bound {
            current_numbers.push(true)
        }
    }

    fn cribling_numbers_up_to(&mut self, upper_bound:i32) {
        let current_numbers = self.NUMBERS.get_mut();
        let current_primes = self.PRIMES.get_mut();
        // Then go from the max prime found in PRIMES upto sqrt of that value
        let first_prime = *current_primes.as_slice().last().unwrap_or(&2i32);
        let mut to_test:Vec<i32> = vec![];
        // Do not forget to take again already known primes in order to eliminate "new" multiples
        to_test.extend(current_primes.iter());
        to_test.extend(first_prime..(upper_bound+1));
        for potential_prime in to_test {
            if current_numbers[potential_prime as usize] {
                for multiplier in 2..(upper_bound/potential_prime+1) {
                    let number = potential_prime*multiplier;
                    if (number as usize)<current_numbers.len() {
                        current_numbers[(potential_prime*multiplier) as usize] = false;
                    }
                }
            }
        }
    }

    fn store_primes(&mut self, upper_bound:i32) {
        let current_numbers = self.NUMBERS.get_mut();
        let current_primes = self.PRIMES.get_mut();
        let first_prime = *current_primes.as_slice().last().unwrap_or(&2i32);
        // Now we have eliminated all non-primes, add the primes to the primes vec
        for number in first_prime..(upper_bound+1) {
            if current_numbers[number as usize] {
            	if !current_primes.contains(&number) {
	                current_primes.push(number);
            	}
            }
        }
    }

    /// Provides the list of primes up to a given value.
    /// This function uses classical Erathostenes Sieve
    /// https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
    pub fn upto(&mut self, upper_bound:i32)->Vec<i32> {
        let numbers_len:i32 = self.NUMBERS.get_mut().len() as i32;
        if numbers_len<upper_bound {
            self.extend_numbers_up_to(upper_bound);
            self.cribling_numbers_up_to(upper_bound);
            self.store_primes(upper_bound);
        }
        return self.PRIMES.get_mut().clone();
    }
	
	fn reduce_to_prime_factors(&mut self, number:i32)->Vec<i32> {
//        eprintln!("reducing {} to prime factors", number);
		let mut returned:Vec<i32> = vec![];
		let primes = self.upto(number);
		let mut primes_iter = primes.iter();
		let mut remaining = number;
		let mut next_prime = primes_iter.next();
		while remaining>1 {
			match next_prime {
				Some(current_prime) => {
					if remaining%current_prime==0 {
						remaining = remaining/current_prime;
						returned.push(*current_prime);
					} else {
						next_prime = primes_iter.next();
					}
				},
				// We've exhausted primes, so return
				None => return returned,
			}
		}
		return returned;
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// FRACTION SIMPLIFIER //////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////
#[derive(Debug, Copy, Clone)]
struct Fraction {
	pub signum:i32,
    pub integer_part:i32,
    pub numerator:i32,
    pub denominator:i32
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
    
    fn reduce(self, PRIMES:&mut Primes)->Fraction {
        if self.denominator==0 || self.numerator==0 {
            return self;
        }
    	let numerator_primes = PRIMES.reduce_to_prime_factors(self.numerator);
    	eprintln!("{} can be separated into {:?}", self.numerator, numerator_primes);
    	let mut denominator_primes = PRIMES.reduce_to_prime_factors(self.denominator);
    	eprintln!("{} can be separated into {:?}", self.denominator, denominator_primes);
    	let mut new_numerator = vec![];
    	for number in numerator_primes {
    		if denominator_primes.contains(&number) {
    			let index = denominator_primes.iter().position(|x| *x == number).unwrap();
				denominator_primes.remove(index);
    		} else {
    			new_numerator.push(number);
    		}
    	}
        return Fraction {
        	signum:self.signum,
            integer_part:self.integer_part,
            numerator:if new_numerator.len()==0 { 1 } else { new_numerator.iter().fold(1, |acc, n| acc * n) },
            denominator:denominator_primes.iter().fold(1, |acc, n| acc * n)
        }
    }
    /// From a "classical" fraction, generates a fraction with the integer part isolated
    fn separate(self)->Fraction {
        if self.integer_part!=0 {
            return self;
        }
        let signum = self.find_signum();
        // Now come the hard part
        let mut next_numerator = self.numerator.abs();
        let next_denominator = self.denominator.abs();
        if self.denominator==0 {
            return self;
        }
        let mut next_integer_part = 0;
        while next_denominator<=next_numerator {
            next_numerator = next_numerator-next_denominator;
            next_integer_part = next_integer_part+1;
        }
        return Fraction {
        	signum:signum,
            integer_part:next_integer_part,
            numerator:next_numerator,
            denominator:next_denominator
        }
    }
    fn simplify(self, PRIMES:&mut Primes)->Fraction {
        let separated = self.separate();
//        eprintln!("separated {} into {:?}", self.to_string(), separated);
        return separated.reduce(PRIMES);
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

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fn main() {
    #![allow(non_snake_case)]
    let mut PRIMES = Primes::new();
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
        let fraction = Fraction::from(number.to_string());
        let reduced = fraction.simplify(&mut PRIMES);
        eprintln!("{} reduced to {:?}", number, reduced);
        println!("{}", reduced.to_string());
    }
}

fn simplify(number:String)->String {
    #![allow(non_snake_case)]
    let mut PRIMES = Primes::new();
    let fraction = Fraction::from(number.to_string());
    let reduced = fraction.simplify(&mut PRIMES);
	return reduced.to_string();	
}

#[cfg(test)]
mod test;