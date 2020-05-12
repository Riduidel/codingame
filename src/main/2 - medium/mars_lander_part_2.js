/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

// A value greater than this angle will make the lander fall too fast
var NO_LOSS_ANGLE = 17
var TERRAIN_SECURITY = 100
var MAX_ANGLE = function(y, height) {
	var ABSOLUTE_MAX_ANGLE = 90
	return Math.floor(Math.min(ABSOLUTE_MAX_ANGLE, Math.max(NO_LOSS_ANGLE, ABSOLUTE_MAX_ANGLE*(y-height)/1000)))
}
var Surface = function() {
	this.height = -1;
    this.from = -1;
    this.to = -1
}

function projectAt(vector, point) {
	second = {'x':vector.x+vector.hSpeed, 'y':vector.y+vector.vSpeed}
	// Those points both match a line of equation a*x+b*y+c=0 (not valable for horizontal and vertical lines) 
	var a = second.y-vector.y;
	var b = second.x-vector.x; 
	var c = (vector.y-second.y)*vector.x + (second.x-vector.x)*vector.y;
	var returned = {}
	if(a==0)  {
		returned['sameX'] = {'x':point.x, 'y':c/b}
	} else {
		returned['sameX'] = {'x':point.x, 'y':(a*point.x+c)/b}
	}
	if(b==0) {
		returned['sameY'] = {'y':point.y, 'x':c/a} 
	} else {
		returned['sameY'] = {'y':point.y, 'x':(b*point.y+c)/a} 
	}
	return returned
}

function duplicate(element) {
	return JSON.parse(JSON.stringify(element))
}
var points = []
// Now we can parse the whole stuff
var surfaceN = parseInt(readline()); // the number of points used to draw the surface of Mars.
for (var i = 0; i < surfaceN; i++) {
    var inputs = readline().split(' ');
    var landX = parseInt(inputs[0]); // X coordinate of a surface point. (0 to 6999)
    var landY = parseInt(inputs[1]); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
    points.push({'x':landX, 'y':landY})
}

printErr("points are "+JSON.stringify(points))
var indexOfCenter = -1
for (var index = 1; index < points.length; index++) {
	if(points[index-1].y==points[index].y) {
		indexOfCenter = index
	}
}
// Now introduce a center point, which is the optimal aim
var newCenter =  {'x':((points[indexOfCenter].x+points[indexOfCenter-1].x)/2), 'y':points[indexOfCenter]['y'], 'center':true}
printErr(JSON.stringify(newCenter))
points.splice(indexOfCenter, 0, newCenter)
points[indexOfCenter]['height']=points[indexOfCenter]['y']
printErr("center is at index "+indexOfCenter
		+" going from "+JSON.stringify(points[indexOfCenter-1])
		+" to "+JSON.stringify(points[indexOfCenter]))
// Now we have our points, locate center points, then build left and right surfaces

function buildStairway(points, centerIndex, direction) {
	for(var index=centerIndex+direction; direction>0 ? index<points.length : index>=0; index+=direction) {
		points[index]['height']=Math.max(points[index]['y'],points[index-direction]['height'])
		// We're not even approaching, so control descend and direction
		points[index]['computeSpeed'] = function(arguments) {
			push = {'angle':0, 'throttle':0, 'direction':direction}
			if(arguments.hSpeed*direction>-20) {
				printErr("ship is not going at the right speed")
			    push.angle = push.angle===0 ? MAX_ANGLE(arguments.y, this.height) : push.angle
				push.throttle = Math.max(4, push.throttle)
			} else if(arguments.hSpeed*direction<-25) {
				printErr("ship is going too fast !")
			    push.angle = push.angle===0 ? MAX_ANGLE(arguments.y, this.height) : push.angle
				push.throttle = Math.max(4, push.throttle)
				push.direction = -1*direction
			}
			projected = projectAt(arguments, this)
			printErr("projected on height "+JSON.stringify(projected))
			if(projected.sameX==undefined || projected.sameX.y<this.height+TERRAIN_SECURITY) {
				printErr("ship is drifting below min height")
				// Maintain ship upwards, please
				push.angle = Math.min(push.angle, NO_LOSS_ANGLE)
				push.throttle = Math.max(4, push.throttle)
			}
			if(Math.abs(arguments.vSpeed)>35) {
				printErr("ship is falling too fast")
				push.angle = Math.min(push.angle, NO_LOSS_ANGLE)
				push.throttle = Math.max(4, push.throttle)
			}
			return push.direction*push.angle+" "+push.throttle
		}
	}
}



buildStairway(points, indexOfCenter, 1)
buildStairway(points, indexOfCenter, -1)
points[indexOfCenter]['computeSpeed'] = function(arguments) {
    printErr("ON CENTER STAGE ! ")
	push = {'angle':0, 'throttle':0, 'direction':0}
	// Using a small hack to find a good converging vSpeed
	projected = projectAt(arguments, this)
	printErr("projected on height "+JSON.stringify(projected))
	if(projected.sameX==undefined || projected.sameX.y<this.height+TERRAIN_SECURITY) {
		printErr("ship is drifting below min height")
		// Maintain ship upwards, please
		push.angle = Math.min(push.angle, 0)
	}
	if(Math.abs(arguments.vSpeed)>35) {
		printErr("ship is falling too fast")
		push.angle = Math.min(push.angle, NO_LOSS_ANGLE)
		push.throttle = Math.max(4, push.throttle)
	}
	if(Math.abs(arguments.hSpeed)>15) {
	    push.angle = NO_LOSS_ANGLE
		push.direction = Math.sign(arguments.hSpeed)
		push.throttle = Math.max(4, push.throttle)
		printErr("ship is going horizontally too fast "+JSON.stringify(push))
	}
	return push.direction*push.angle+" "+push.throttle
}
printErr("points are "+JSON.stringify(points))

function findSurface(points, indexOfCenter, X) {
	var center = points[indexOfCenter]
	var direction = Math.sign(X-center.x)
	if(X==center.x) {
		return center
	}
	for(var index=indexOfCenter; (direction>0 ? index<points.length-1 : index>0); index+=direction) {
		var candidate = points[index]
		var successor = points[index+direction]
		var candidateDiff = Math.sign(X-candidate.x)
		var successorDiff = Math.sign(X-successor.x)
		if((candidateDiff==direction) && (successorDiff!=direction)) {
			return candidate
		}
	}
	printErr("Returning end point")
	return direction>0 ? points[points.length-1] : points[0] 
}

// game loop
while (true) {
    var inputs = readline().split(' ');
    var X = parseInt(inputs[0]);
    var Y = parseInt(inputs[1]);
    var hSpeed = parseInt(inputs[2]); // the horizontal speed (in m/s), can be negative.
    var vSpeed = parseInt(inputs[3]); // the vertical speed (in m/s), can be negative.
    var fuel = parseInt(inputs[4]); // the quantity of remaining fuel in liters.
    var rotate = parseInt(inputs[5]); // the rotation angle in degrees (-90 to 90).
    var power = parseInt(inputs[6]); // the thrust power (0 to 4).

    // Write an action using print()
    // To debug: printErr('Debug messages...');
	
	var arguments = {'x':X, 'y':Y, 'hSpeed':hSpeed, 'vSpeed':vSpeed}
    printErr("arguments are "+JSON.stringify(arguments))
	var surface = findSurface(points, indexOfCenter, X)
	printErr("Identified next checkpoint to be "+JSON.stringify(surface))
	print(surface.computeSpeed(arguments))
}