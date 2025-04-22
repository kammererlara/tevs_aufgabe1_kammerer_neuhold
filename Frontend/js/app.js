const apiGatewayUrl = 'http://localhost:8080/status-service';

function showStatusMessage(message, type = 'success') {
  const statusMessageElement = document.getElementById('status-message');
  statusMessageElement.textContent = message;
  statusMessageElement.className = `mt-4 p-4 rounded-md ${type === 'success' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`;
  statusMessageElement.classList.remove('hidden');
}

function hideStatusMessage() {
  document.getElementById('status-message').classList.add('hidden');
}

document.getElementById('setStatus').addEventListener('click', () => {
  hideStatusMessage();
  const username = document.getElementById('username').value;
  const statusText = document.getElementById('statusText').value;

  if (!username || !statusText) {
    showStatusMessage('Bitte Benutzername und Status Text eingeben.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ statusText: statusText })
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => { throw new Error(err.message || 'Fehler beim Setzen des Status') });
      }
      return response.json();
    })
    .then(data => {
      showStatusMessage(`Status für Benutzer "${username}" gesetzt: ${data.statusText}`);
      document.getElementById('username').value = '';
      document.getElementById('statusText').value = '';
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});

document.getElementById('getStatus').addEventListener('click', () => {
  console.log('Fetching status for username:', username); // Debugging line
  hideStatusMessage();
  const username = document.getElementById('username').value;

  if (!username) {
    showStatusMessage('Bitte Benutzername eingeben.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'GET'
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => { throw new Error(err.message || 'Fehler beim Abrufen des Status') });
      }
      return response.json();
    })
    .then(data => {
      showStatusMessage(`Status für Benutzer "${username}": ${data.statusText}`);
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});

document.getElementById('deleteStatus').addEventListener('click', () => {
  hideStatusMessage();
  const username = document.getElementById('username').value;

  if (!username) {
    showStatusMessage('Bitte Benutzername eingeben.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'DELETE'
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => { throw new Error(err.message || 'Fehler beim Löschen des Status') });
      }
      return response.json();
    })
    .then(data => {
      showStatusMessage(`Status für Benutzer "${username}" gelöscht.`);
      document.getElementById('username').value = '';
      document.getElementById('statusText').value = '';
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});

document.getElementById('updateStatus').addEventListener('click', () => {
  hideStatusMessage();
  const username = document.getElementById('username').value;
  const statusText = document.getElementById('statusText').value;

  if (!username || !statusText) {
    showStatusMessage('Bitte Benutzername und Status Text eingeben.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ statusText: statusText })
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => { throw new Error(err.message || 'Fehler beim Aktualisieren des Status') });
      }
      return response.json();
    })
    .then(data => {
      showStatusMessage(`Status für Benutzer "${username}" aktualisiert: ${data.statusText}`);
      document.getElementById('username').value = '';
      document.getElementById('statusText').value = '';
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});
