var deci = 121;
var nega = deci_to_nega(deci);
alert(nega);

function deci_to_nega(deci) {
	var res = "";
	var remainder;
	while (deci != 0) {
		remainder = deci%(-2);
		deci = Math.trunc(deci/(-2));
		if (remainder < 0) {
			//+base
			//a = (-r)c + d = (-r)c + d - r + r = (-r)(c + 1) + (d + r)
			//see https://en.wikipedia.org/wiki/Negative_base
			remainder += 2;
			deci += 1;
		}
		res = remainder + res;
	}
	return res;
}