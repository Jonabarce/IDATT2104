const net = require('net'); // Importer net-modulen som tillater opprettelse av TCP servere og klienter

// Opprett en HTTP-server som svarer med en enkel WebSocket-testside
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        // HTML-koden som sendes som respons til klienten
        let content = `<!DOCTYPE html>
      <html>
        <head>
          <meta charset="UTF-8" />
        </head>
        <body>
          <div class="wrapper" style=" background-color #e2e8f0; display: flex; place-items: center; flex-direction: column; justify-content: center; align-items: center;">
            <div>
            <p style="font-size: 30px; color: #0f172a; text-decoration: underline"> WebSocket test page</p>
            </div>
            <div>
            <textarea placeholder="Type in a message to send" id="message" rows="10" cols="50" style=" border-radius: 15px; border: solid 3px white; font-size: 25px; width: 600px; height: 290px; background-color: #c4b5fd; color: white; resize: none"></textarea>
            </div>
            <div>
            <button id="send" onClick="sendMessage()" style=" border-radius: 15px; border: solid 3px white; width: 250px; height: 50px; color: white; background-color: deeppink; font-size: 15px" >Send message</button>
            </div>
            <div>
            <p style=" font-size: 23px; color: #0f172a; text-decoration: underline;">Message received</p>
            </div>
            <div>
            <textarea id="reply" rows="10" cols="50" style=" border-radius: 15px; border: solid 3px white; font-size: 25px;width: 600px; height: 290px; background-color: #c4b5fd; color: white; resize: none"></textarea>
            </div>
            
            </div>
          <script>
            // Definer en funksjon for å sende meldinger via WebSocket
            function sendMessage() {
              let msg = document.getElementById("message").value;
              ws.send(msg)
              console.log(msg)
            }

            // Finn textarea-elementet som vil bli brukt til å vise svar
            let reply = document.getElementById("reply");

            // Opprett en WebSocket-tilkobling til ws://localhost:3001
            let ws = new WebSocket('ws://localhost:3001');

            // Lytt etter meldinger fra WebSocket-serveren
            ws.onmessage = event => {
              reply.innerHTML=event.data;
            }

            // Send en melding når WebSocket-tilkoblingen er åpen
            ws.onopen = () => ws.send('hello');
          </script>
        </body>
      </html>
    `;

        // Send HTTP-responsen til klienten
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});

// Start HTTP-serveren og lytt på port 3000
httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});
