document.addEventListener("DOMContentLoaded", function () {
  const socket = new WebSocket("ws://localhost:8080/status");

  const messageInput = document.getElementById("messageInput");
  const sendButton = document.getElementById("sendButton");
  const messagesDiv = document.getElementById("messages");

  socket.onopen = function () {
    console.log("Verbindung zum WebSocket-Server hergestellt.");
  };

  socket.onmessage = function (event) {
    console.log("Nachricht erhalten:", event.data);
    const messageElement = document.createElement("p");
    messageElement.textContent = event.data;
    messagesDiv.appendChild(messageElement);
  };

  socket.onerror = function (error) {
    console.error("WebSocket-Fehler:", error);
  };

  socket.onclose = function () {
    console.log("WebSocket-Verbindung geschlossen.");
  };

  sendButton.addEventListener("click", function () {
    const message = messageInput.value.trim();
    if (message) {
      socket.send(message);
      messageInput.value = "";
    }
  });

  // Senden mit Enter-Taste
  messageInput.addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
      sendButton.click();
    }
  });
});
