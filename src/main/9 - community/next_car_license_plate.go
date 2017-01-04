package main

import "fmt"
import "os"
import "bufio"
import "strings"
import "strconv"

const ALPHABET = 26

func from_letters(v string) int {
    returned := 0
    for i := 0; i < len(v); i++ {
        letterIndex := int(v[i])-int('A')
        fmt.Fprintln(os.Stderr, "Letter", string(v[i]), "has index", letterIndex)
        returned = returned*ALPHABET + letterIndex
    }
    return returned
}

func to_numbers(values []string) []int {
    middle, _ := strconv.Atoi(values[1])
    return []int{from_letters(values[0]), middle, from_letters(values[2])}
}
func to_number(values []string) int {
    numbers := to_numbers(values)
    fmt.Fprintln(os.Stderr, "Splitted numbers are", numbers)
    return ((numbers[0]*ALPHABET*ALPHABET)+numbers[2])*1000+numbers[1]
}

func extract_base(number int) []int {
  if number<ALPHABET {
    return []int{number}
  } else {
    value := number/ALPHABET
    n := number-value*ALPHABET
    return append([]int{value}, extract_base(n)...)
  }
}

func to_letter(number int) string {
    n := number
    fmt.Fprintln(os.Stderr, "Converting", n, "to letter")
    var l []string
    digits := extract_base(number)
    for _, value := range digits {
        l = append(l, string(byte(value+int('A'))))
    }
    for len(l)<2 {
        var all []string
        all =append(all, "A")
        all = append(all, l...)
        l = all
    }
    return strings.Join(l, "")
}

func to_letters(numbers []int) string {
    fmt.Fprintln(os.Stderr, "Converting to string array from", numbers)
    text := fmt.Sprintf("%3.3d", numbers[1])
    parts := []string {to_letter(numbers[0]), text, to_letter(numbers[2])}
    return strings.Join(parts, "-")
}

func to_array(number int) []int {
    n := number
    n1 := n/(1000*ALPHABET*ALPHABET)
    n2 := (n - n1*1000*ALPHABET*ALPHABET)/1000
    n3 := n - n1*1000*ALPHABET*ALPHABET - n2*1000
    return []int {n1, n3, n2}
}

func to_plate(number int) string {
    // first, regenerate the array
    array := to_array(number)
    return to_letters(array)
}

func transform(x string, n int) string {
    fmt.Fprintln(os.Stderr, "generating plate from", x, "iterating", n, "times")
    
//    var values = strings.Split(x, "-")
    var numbers = to_numbers(strings.Split(x, "-"))
    
    fmt.Fprintln(os.Stderr, "Converted to a number array", numbers)
    // Now iterate
    for n>0 {
//        fmt.Fprintln(os.Stderr, "n is", n, "and numbers are", numbers)
        numbers[1]=numbers[1]+1
        if numbers[1]>=1000 {
            numbers[1] = 1
            numbers[2] = numbers[2]+1
            if numbers[2]>=ALPHABET*ALPHABET {
                numbers[0] = numbers[0]+1
                numbers[2] = 0
                if numbers[0]>=ALPHABET*ALPHABET {
                  numbers[0] = 0
                }
            }
        }
        n = n-1
    }

    return to_letters(numbers)
}

func main() {
    scanner := bufio.NewScanner(os.Stdin)
    scanner.Buffer(make([]byte, 1000000), 1000000)

    scanner.Scan()
    x := scanner.Text()
    var n int
    scanner.Scan()
    fmt.Sscan(scanner.Text(),&n)

   fmt.Println(transform(x, n))// Write answer to stdout
}