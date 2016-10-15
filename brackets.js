var to_be_analyzed = "(t{t})[r)";

var pairs = [['(', ')'], ['{', '}'], ['[', ']'] ];

var do_break = false;

for (var j=0; j<pairs.length; j++) {
	to_be_analyzed = check_brackets(to_be_analyzed, j);
}

var check = true;

for (var j=0; j<pairs.length; j++) {
	for (var k=0; k<pairs[j].length;k++) {
		if (to_be_analyzed.indexOf(pairs[j][k])!=-1) {
			check = false;
			break;
		}
	}
}
alert(check);

function check_brackets (to_analyze, i) {
	var pair_one = pairs[i][0];
	var pair_two = pairs[i][1];
	var one_pos = to_analyze.indexOf(pair_one);
	var two_pos = to_analyze.lastIndexOf(pair_two);
	if ( (one_pos!=-1) && (two_pos!=-1) )
	{
		to_analyze = cut_it(to_analyze, one_pos);
		two_pos = to_analyze.lastIndexOf(pair_two);
		to_analyze = cut_it(to_analyze, two_pos);
		to_analyze = check_brackets(to_analyze, i);
		if (do_break) {return to_analyze;}
	}
	else {
		do_break = true;
		return to_analyze;
	}
}

function cut_it(to_cut, position) {
	var res = "";
	
	if (position == 0) {
		res = to_cut.substr(position + 1);
	}		
	if ( (position < to_cut.length - 1) && (position >0) ) {
		res = to_cut.substr(0, position) + to_cut.substr(position + 1);
	} 
	
	if (position == to_cut.length - 1) {
		res = to_cut.substr(0, position);
	}
	
	return res;
}