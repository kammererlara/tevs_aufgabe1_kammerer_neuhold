const apiGatewayUrl = 'http://localhost:8080/status-service';

function showStatusMessage(message, type = 'success') {
  const statusMessageElement = document.getElementById('status-message');
  statusMessageElement.textContent = message;
  statusMessageElement.className = `mt-6 p-4 rounded-md ${type === 'success' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`;
  statusMessageElement.classList.remove('hidden');
}

function hideStatusMessage() {
  document.getElementById('status-message').classList.add('hidden');
}

document.getElementById('setStatus').addEventListener('click', () => {
  hideStatusMessage();
  const username = document.getElementById('username').value;
  const status = document.getElementById('status').value;

  if (!username || !status) {
    showStatusMessage('Please enter username and status text.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: status
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => {
          const errorMessage = err.message || 'Failed to set status';
          throw new Error(errorMessage);
        });
      }
      return response.text();
    })
    .then(data => {
      showStatusMessage(data);
      document.getElementById('username').value = '';
      document.getElementById('status').value = '';
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});

document.getElementById('getStatus').addEventListener('click', () => {
  hideStatusMessage();
  const username = document.getElementById('username').value;

  if (!username) {
    showStatusMessage('Please enter username.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'GET'
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => {
          const errorMessage = err.message || 'Failed to get status';
          throw new Error(errorMessage);
        });
      }
      return response.json();
    })
    .then(data => {
      showStatusMessage(`Status for user ${username}: ${data.status}`);
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});

document.getElementById('deleteStatus').addEventListener('click', () => {
  hideStatusMessage();
  const username = document.getElementById('username').value;

  if (!username) {
    showStatusMessage('Please enter username.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'DELETE'
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => {
          const errorMessage = err.message || 'Failed to delete status';
          throw new Error(errorMessage);
        });
      }
      return response.text();
    })
    .then(data => {
      showStatusMessage(data);
      document.getElementById('username').value = '';
      document.getElementById('status').value = '';
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});

document.getElementById('updateStatus').addEventListener('click', () => {
  hideStatusMessage();
  const username = document.getElementById('username').value;
  const status = document.getElementById('status').value;

  if (!username || !status) {
    showStatusMessage('Please enter username and status text.', 'error');
    return;
  }

  fetch(`${apiGatewayUrl}/status/${username}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: status
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => {
          const errorMessage = err.message || 'Failed to update status';
          throw new Error(errorMessage);
        });
      }
      return response.text();
    })
    .then(data => {
      showStatusMessage(data);
      document.getElementById('username').value = '';
      document.getElementById('status').value = '';
    })
    .catch(error => {
      showStatusMessage(error.message, 'error');
    });
});
