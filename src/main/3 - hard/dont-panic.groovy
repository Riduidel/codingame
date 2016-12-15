computeSolution = { parameters ->
	/* Given all parameters, we should compute here a path going from start to end */
	returned = []
	0.upto(parameters.nbFloors) { returned.add([])}

}

shouldBlock = { expectedPos, clonePos, direction ->
    if(clonePos>expectedPos && direction.equals("RIGHT")) {
        return "BLOCK"
    }
    if(clonePos<expectedPos && direction.equals("LEFT")) {
        return "BLOCK"
    }
    return "WAIT"
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

input = new Scanner(System.in);

nbFloors = input.nextInt() // number of floors
width = input.nextInt() // width of the area
nbRounds = input.nextInt() // maximum number of rounds
exitFloor = input.nextInt() // floor on which the exit is found
exitPos = input.nextInt() // position of the exit on its floor
nbTotalClones = input.nextInt() // number of generated clones
nbAdditionalElevators = input.nextInt() // number of elevators we can generate
nbElevators = input.nextInt() // number of elevators
def elevators = [:]
for (i = 0; i < nbElevators; ++i) {
    elevatorFloor = input.nextInt() // floor on which this elevator is found
    elevatorPos = input.nextInt() // position of the elevator on its floor
    if(!elevators.containsKey(elevatorFloor)) {
        elevators.put(elevatorFloor, [])
    }
    elevators.get(elevatorFloor).add(elevatorPos)
}
parameters = [nbFloors:nbFloors, nbRounds:nbRounds, exitFloor:exitFloor, exitPos:exitPos, nbTotalClones:nbTotalClones, nbAdditionalElevators:nbAdditionalElevators, nbElevators:nbElevators, elevators:elevators]

System.err << "Now we have all informations\n"

parameters.each {
	System.err << "\t" << it.key << "=" << it.value << "\n"
}

System.err << "Compute a solution\n"
computeSolution(parameters)
// game loop
while (true) {
    cloneFloor = input.nextInt() // floor of the leading clone
    clonePos = input.nextInt() // position of the leading clone on its floor
    direction = input.next() // direction of the leading clone: LEFT or RIGHT
    
    instruction = "WAIT"

    System.err << "Lead clone is at floor ${cloneFloor} position ${clonePos} going to ${direction}\n"
    if(cloneFloor==exitFloor) {
        System.err <<"Reaching exit floor !\n"
        instruction = shouldBlock(exitPos, clonePos, direction)
    } else if(cloneFloor>=0) {
        // Do we have an escalator ?
        if(elevators.containsKey(cloneFloor)) {
            System.err << "There is an escalator !\n"
            sorted = elevators[cloneFloor].sort {
                Math.abs(it-clonePos)
            }
            def nearest = sorted[0]
            instruction = shouldBlock(nearest, clonePos, direction)
        } else if(nbAdditionalElevators>=0) {
            elevators.put(cloneFloor, [clonePos])
            instruction = "ELEVATOR";
            nbAdditionalElevators--;
            System.err << "There is NO escalator ! Creating an elevator. We can now create ${nbAdditionalElevators} elevators\n"
        }
    }
    // Write an action using println
    // To debug: System.err << "Debug messages...\n"

    println instruction // action: WAIT or BLOCK
}