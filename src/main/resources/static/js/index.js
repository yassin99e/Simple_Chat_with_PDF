const fileInput = document.getElementById("fileInput");
const userInput = document.getElementById("userInput");
const sendMessageButton = document.getElementById("sendMessageButton");
const fileUploadSection = document.getElementById("file-upload-section");
const chatSection = document.getElementById("chat-section");
const chatMessages = document.getElementById("chat-messages");

let sessionId = null; // Store the session ID

// File Upload - Post to /upload on file selection
fileInput.addEventListener('change', () => {
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        // Send POST request to /upload
        fetch('/upload', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Hide the file input and show the message input + send button
                    fileUploadSection.style.display = "none";
                    chatSection.style.display = "flex";
                    chatMessages.style.display = "block";

                    // Store session ID if provided by the backend
                    sessionId = data.sessionId || null;
                }
            })
            .catch(error => {
                console.error('Error uploading file:', error);
            });
    }
});

// Sending message after the file is uploaded
sendMessageButton.addEventListener('click', () => {
    const message = userInput.value.trim();

    if (message !== "") {
        // Add user message to the chat box
        const userMessageDiv = document.createElement("div");
        userMessageDiv.className = "message user";
        userMessageDiv.textContent = message;
        chatMessages.appendChild(userMessageDiv);

        // Add "Processing your request..." placeholder message
        const botMessageDiv = document.createElement("div");
        botMessageDiv.className = "message bot";
        botMessageDiv.textContent = "Processing your request...";
        chatMessages.appendChild(botMessageDiv);

        // Scroll to the latest message
        chatMessages.scrollTop = chatMessages.scrollHeight;

        // Clear the input field
        userInput.value = "";

        // Prepare data for the backend
        const requestBody = {
            message: message,
        };

        if (sessionId) {
            requestBody.sessionId = sessionId; // Include session ID if available
        }

        // Send POST request to /sendMessage
        fetch('/sendMessage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody),
        })
            .then(response => response.json())
            .then(data => {
                // Update the bot's placeholder message with the actual response
                botMessageDiv.textContent = data.response;

                // Scroll to the latest message
                chatMessages.scrollTop = chatMessages.scrollHeight;
            })
            .catch(error => {
                console.error('Error sending message:', error);
                botMessageDiv.textContent = "An error occurred. Please try again.";
            });
    }
});
