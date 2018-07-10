var express = require('express');
var path = require('path');
var app = express();

app.use(require('node-sass-middleware')({
    src: path.join(__dirname, '../src/main/resources/stylesheets'),
    dest: path.join(__dirname, "website/stylesheets"),
    prefix: "/public/stylesheets",
    indentedSyntax: false,
    sourceMap: false
}));

app.use('/public', express.static(path.join(__dirname, 'website')));

app.use(function (req, res) {
    res.status(200);
    res.sendFile(path.join(__dirname, 'website/index.html'));
});

app.listen(3000, function () {
    console.log('Example app listening on port 3000!');
});