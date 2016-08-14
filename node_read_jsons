var http = require("http");
var express = require("express");
var bodyParser = require("body-parser");

var app = express();
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.text());
app.post('/', function (request, response) {

    response.writeHead(200, { "Content-Type": "text/html" });

    response.write(generate_response(request.body.name) + "</body><html>");
    response.end();    
    
});
app.get('/', function (request, response) {
    
    //http.createServer(function (request, response) {
    response.writeHead(200, { "Content-Type": "text/html" });
    
    response.write(generate_response() + "</body><html>");
    response.end();
});

app.listen(8888, "localhost",  function () {
    
}
);

function generate_response(name_json) {

    var txtR = "";
    var fs = require('fs');

    if (name_json == undefined) {

        name_json = "united1.json";

        txtR += "<!doctype html><html><head>\r\n" +
            "<script type='text/javascript' src='http://www.kryogenix.org/code/browser/sorttable/sorttable.js'>" +
            "</script> <link rel='stylesheet' href='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'>" +
            "<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>" +
            "<script src='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js'></script>";

        txtR += "\r\n<script>function post_ajax(s) {\r\n" +
            "$.ajax({\r\ntype:'POST', url:'http://localhost:8888', " +
            "data: {'name': s} , \r\nsuccess:function(response) {" +
            "\r\$('.table').html(response);}\r\n" +
            ", error:function(xhr, status,error) {" +
            "\r\nalert('error:' + error.message); }\r\n " +
            "})};</script > ";
        

        txtR += "</head>\r\n<body>";
        var arr = fs.readdirSync("c:\\test\\");
        arr = arr.filter(function (arr) {
            return arr.substr(-5) == ".json";
        });

        
        txtR += "\r\n<select>";

        for (var i = 0; i < arr.length; i++) {
            txtR += "\r\n<option onclick='post_ajax(\"" + arr[i] + "\");'>" + arr[i] + "</option>";
        }

        
        txtR += "</select>\r\n<br>";
    }
    txtR += "<table class='sortable table table-striped'>"
    var obj;
    try {
        obj = JSON.parse(fs.readFileSync('c:\\test\\' + name_json, 'utf8'));
    }
    catch (err) {
        txtR += "<tr><td>Error of JSON parse</td></tr>";
    }
    for (var exKey in obj) {

        var ob = obj[exKey];
        if (i == 0) {
            txtR += "<tr>";
            for (var trKey in ob) {
                txtR += "<th>" + trKey + "</th>"
            }
            txtR += "</tr>";
        }

        txtR += "<tr>";
        for (var trKey in ob) {
            txtR += "<td>" + ob[trKey] + "</td>";
        }
        txtR += "</tr>";
        i++;

    }
    txtR += "</table>";

    return txtR;
}
