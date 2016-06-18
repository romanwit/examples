var http = require("http");

http.createServer(function(request, response) {
    response.writeHead(200, {"Content-Type": "text/html"});
    var i = 0;
    var txtR = "<!doctype html><html><head>" +
    "<script type'text/javascript' src='http://www.kryogenix.org/code/browser/sorttable/sorttable.js'>"+
    "</script></head><body><table class='sortable'>";
    var fs = require('fs');
    var obj = JSON.parse(fs.readFileSync('c:\\test\\grid.json', 'utf8'));
    //var obj=JSON.parse("[{\"id\": \"1\",    \"firstName\": \"Peter\", \"lastName\": \"Jhons\"}]");
    for(var exKey in obj) {

        var ob = obj[exKey];
        if (i==0)
        {
            txtR+="<tr>";
            for (var trKey in ob)
            {
                txtR += "<th>" + trKey + "</th>"
            }
            txtR+="</tr>";
        }

        txtR += "<tr>";
        for(var trKey in ob) {
            //console.log("key:"+trKey+", value:"+ob[trKey]);
            txtR += "<td>"+ob[trKey]+"</td>";
        }
        txtR += "</tr>";
        i++;

    }
    response.write(txtR+"</table></body><html>");
    response.end();
}).listen(8888);