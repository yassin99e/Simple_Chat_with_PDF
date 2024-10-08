const chatMessages = document.getElementById("chat-messages");

function sendMessage() {
    const userInput = document.getElementById("userInput").value;

    if (userInput.trim() === "") {
        return;
    }

    // Display user message
    const userMessage = document.createElement("div");
    userMessage.className = "message user";
    userMessage.innerHTML = userInput;
    chatMessages.appendChild(userMessage);

    // Show bot is processing
    const botMessage = document.createElement("div");
    botMessage.className = "message bot";
    botMessage.innerHTML = "Processing your request...";
    chatMessages.appendChild(botMessage);

    // Scroll to the bottom
    chatMessages.scrollTop = chatMessages.scrollHeight;

    // Clear input field
    document.getElementById("userInput").value = "";

    // Send the user input to the backend
    fetch('/chat/sendMessage', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ message: userInput })
    })
        .then(response => response.json())
        .then(data => {
            // Update bot's response with the actual message from the AI
            botMessage.innerHTML = data.response;
            chatMessages.scrollTop = chatMessages.scrollHeight;
        })
        .catch(error => {
            console.error('Error:', error);
            botMessage.innerHTML = "An error occurred. Please try again.";
        });
}