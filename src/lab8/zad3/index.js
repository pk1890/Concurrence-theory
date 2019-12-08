const http = require('http');
const app = require('express')();

const server = http.createServer(app);

app.get('/', (req, res) =>{
    console.log("Handling request");
    setTimeout(() => {
        res.statusCode = 200;
        res.setHeader("XProductPrice", Math.floor(Math.random()*1000));
        res.send('<h1><font color="pink">THIS REST SERVER IS BROUGHT TO YOU BY SOKET GANG</text></h1>');
    }, 100);
});

server.listen(8080);