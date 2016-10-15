var x = ["4", "13", "5", "/", "+"];
var y = evalRPN(x);
alert(y);

function evalRPN(tokens) {
    var t;
    var num;
	var i = 0;
	while (i<tokens.length) {
		
        t = tokens[i];
        num = 0;
		switch (t) {
			case "+": 
				num = parseInt(tokens[i-2]) + parseInt(tokens[i-1]);
				break;			
			case "-":
				num = parseInt(tokens[i-2]) - parseInt(tokens[i-1]);
				break;
			
			case "*":
				num = parseInt(tokens[i-2]) * parseInt(tokens[i-1]);
				break;
			
			case "/":
				num = Math.trunc(parseInt(tokens[i-2]) / parseInt(tokens[i-1]));
				break;
		}
		
		if ((t=="*")||(t=="/")||(t=="-")||(t=="+")) {
			tokens.splice(i + 1, 0, num);
			tokens.splice(i-2, 3);
			i = i - 1;
		}
		else {
			i++;
		}
        
    }
	return(tokens[0]);
};
