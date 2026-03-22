// Simple static server for frontend testing
const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = process.env.PORT || 3000;
const PUBLIC_DIR = path.join(__dirname, 'frontend');

const MIME_TYPES = {
    '.html': 'text/html',
    '.css': 'text/css',
    '.js': 'text/javascript',
    '.json': 'application/json',
    '.png': 'image/png',
    '.jpg': 'image/jpeg',
    '.svg': 'image/svg+xml',
    '.woff2': 'font/woff2',
    '.woff': 'font/woff',
    '.ttf': 'font/ttf'
};

const server = http.createServer((req, res) => {
    console.log(`[REQ] ${req.method} ${req.url}`);
    
    // ignore query string
    let requestUrl = req.url.split('?')[0];

    // default to index.html for root requests
    if (requestUrl === '/') {
        requestUrl = '/index.html';
    } else if (requestUrl.endsWith('/')) {
        requestUrl += 'index.html';
    }

    let filePath = path.join(PUBLIC_DIR, requestUrl);

    fs.stat(filePath, (err, stats) => {
        // If it's a directory without trailing slash, redirect or append index.html
        if (!err && stats.isDirectory()) {
            filePath = path.join(filePath, 'index.html');
        }

        const extname = String(path.extname(filePath)).toLowerCase();
        const contentType = MIME_TYPES[extname] || 'application/octet-stream';

        fs.readFile(filePath, (error, content) => {
            if (error) {
                if (error.code === 'ENOENT') {
                    // Provide a small 404 response
                    fs.readFile(path.join(PUBLIC_DIR, 'index.html'), (err404, defaultContent) => {
                        console.error(`404 Not Found: ${filePath}`);
                        res.writeHead(404, { 'Content-Type': 'text/html' });
                        res.end('<h1>404 Not Found</h1><p>The requested page does not exist or has been moved.</p>', 'utf-8');
                    });
                } else {
                    console.error(`500 Server Error: ${error.code}`);
                    res.writeHead(500, { 'Content-Type': 'text/plain' });
                    res.end(`Server Error: ${error.code}`, 'utf-8');
                }
            } else {
                res.writeHead(200, { 'Content-Type': contentType });
                res.end(content, 'utf-8');
            }
        });
    });
});

server.listen(PORT, () => {
    console.log(`\n===========================================`);
    console.log(`InsurAI Frontend Server is running!`);
    console.log(`Open in Browser: http://localhost:${PORT}/`);
    console.log(`===========================================\n`);
    console.log(`Press Ctrl+C to stop the server\n`);
});
