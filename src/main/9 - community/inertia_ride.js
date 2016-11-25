var Playground = function(playground, width, height) {
	this.playground = playground
	this.width = width
	this.height = height
	this.content = []
	for(var i=0;i<width; i++) {
		this.content[i]=-1
	}
}
Playground.prototype.find_content_at = function(p) {
	if(this.content[p]<0) {
		for(var j=0;j<this.height;j++) {
			if(this.playground[j][p]!='.') {
				this.content[p] = this.playground[j][p]
				break
			}
		}
	}
	return this.content[p]
}
Playground.prototype.get_change_at = function(inertia, position) {
	if(position<0 || position>=this.width) 
		return {'inertia':0, 'position':0}
	var c = this.find_content_at(position)
	var direction = Math.sign(inertia)
	if(c=='_') {
		returned = {'inertiaChange':-1*direction}
	} else if(c=='/') {
		returned = {'inertiaChange': direction>=0 ? -10 : -9}
	} else if(c=='\\') {
		returned = {'inertiaChange': direction<=0 ? 10 : 9}
	}
	returned['inertia'] = inertia+returned['inertiaChange']
	returned['positionChange'] = Math.sign(returned['inertia'])
	returned['position'] = position+returned['positionChange']
	printErr("At position "+position+" (content is \""+c+"\") running at speed "+inertia+". Exiting with "+JSON.stringify(returned))
	return returned
}
var Turn = function(i, p) {
    if (typeof p === 'undefined') { 
        p = 0;
    }
    this.position = p;
    this.inertia = i;
}
Turn.prototype.has_finished = function(playground, width, height) {
    if(this.position<0) {
		printErr("exited on left")
        return true
    } else if(this.position>=width) {
		printErr("exited on right")
        return true
    }
	var content = playground.find_content_at(this.position)
    if(content=="_") {
        return this.inertia==0
    }
    return false
}
Turn.prototype.run_on = function(playground) {
	var change = playground.get_change_at(this.inertia, this.position)
	return new Turn(change['inertia'], change['position'])
}
function navigate_on(playground, width, height, inertia) {
	var count=0
	var t = new Turn(inertia)
	var p = new Playground(playground, width, height)
	var position = 0
	do {
		count++
		position = t.position
		t = t.run_on(p)
/*		if(count>20) {
			throw "ShtapIt"
		}
*/	} while(!t.has_finished(p, width, height));
	return position
}

function to_s(playground) {
    var returned = ""
    playground.forEach(function(row) {
        returned += row.join('')+"\n"
    })
    return returned
}

var inertia = parseInt(readline());
var inputs = readline().split(' ');
var W = parseInt(inputs[0]);
var H = parseInt(inputs[1]);
var playground = []
for (var i = 0; i < H; i++) {
    playground.push(readline().split(''));
}

// Write an action using print()
// To debug: printErr('Debug messages...');
printErr(to_s(playground))

print(navigate_on(playground, W, H, inertia));