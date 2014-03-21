// First, checks if it isn't implemented yet.
if (!String.prototype.format) {
  String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) { 
      return typeof args[number] != 'undefined'
        ? args[number]
        : match
      ;
    });
  };
}

$(document).ready(function() {

  var inputField = $("#chat-input > input");
  var chatBox = $("#chat-area ul");
  var scrollArea = $(".nano-content");
  var users = [];
  var socket;
  var holdOn = false;

  var self = {
    id: -1,
    name: "You",
    color: "#54B9B9"
  }

  var root = {
    id: 0,
    name: "System",
    color: "#C94343"
  }

  var noUsr = {
    id: -1,
    name: "",
    color: "#54B9B9"
  }

  var pushMessage = function(user, message) {
    var nameSpan = $('<span>', {class: 'name', text: user.name + ':'});
    nameSpan.css("color", user.color);
    var listItem = $('<li>', {text: message});
    listItem
      .prepend(nameSpan)
      .attr("userid", user.id);
    chatBox.append(listItem);
    $(".nano").nanoScroller();
  }

  var pushMessages = function(messages) {
    for(i = 0; i < messages.length; i++) {
      pushMessage(noUsr, messages[i]);
    }
    attemptScroll();
  }

  var systemPush = function(message) {
    console.log(message);
    pushMessage(root, message);
    attemptScroll();  
  }

  $("#chat-input").submit(function(event) {
    event.preventDefault();
    input = inputField.val();
    inputField.val('');
    if(socket!=null) { handleInput(input); }
  })

  var init = function() {
    pushMessage(root, 'Attempting to connect to server, please wait ...')
    socket = io.connect('http://10.0.0.2:8889', {
     'reconnection delay' : 2000,
     'force new connection' : true
    });

    socket.on('message', function(data) {
          data = JSON.parse(data);
          console.log(data);
          handleResponse(data);
    });

    socket.on('connect', function() {
      systemPush("Successfully connected to the server!")
    });

    socket.on('disconnect', function() {
      pushMessage(root, "Now disconected from the server!");
    });
  }

  var handleInput = function(input) {
    if(input.length < 1) return;
    if(input.charAt(0) == '/') {
      var command = input.substr(1);
      var arguments = '';
      if(input.indexOf(' ') > -1) {
        command = input.substr(1, input.indexOf(' ')-1);
        arguments = input.substr(input.indexOf(' ')+1);
      }
      handleCommand(command, arguments);
    } else {
      attemptSendMessage(input);
    }
  }

  var handleCommand = function(command, arguments) {
    if(command == null) { badCommand(command); }
    else {
      switch(command) {
        case 'login':
          attemptLogin(arguments);
          break;

        case 'logout':
          attemptLogout();
          break;

        case 'holdOn':
          holdOn = true;
          break;

        case 'holdOff':
          holdOn = false;
          break;

        default:
          badCommand(command);
          break;
      }
    }
  }

  var sendRequest = function(request) {
    console.log(request);
    request = JSON.stringify(request);
    socket.send(request);
  }

  var attemptSendMessage = function(message) {
    var request = {request: "message", context: message}
    sendRequest(request);
  }

  var attemptLogin = function(username) {
    var request = {request: "login", context: username}
    sendRequest(request);
  }

  var attemptLogout = function() {
    var request = {request: "logout"};
    sendRequest(request);
  }

  var badCommand = function(command) {
    if(command == null) { console.log('/ must be followed by a command') }
    else { console.log('Command: '+command+' is not legal.') }
  }
  
  var handleResponse = function(data) {
    var type = data.response;
    switch(type) {
      case 'login':
        resolveLogin(data);
        break;

      case 'logout':
        resolveLogout(data);
        break;

      case 'message':
        resolveMessage(data);
        break;

      case 'new message':
        resolveNewMessage(data);
        break;
    }
  }

  var resolveLogin = function(data) {
    var status = data.status;
    if(status == 'error') {
      systemPush("Error: "+data.context);
    } else if(status == "OK") {
      systemPush("Login granted!")
      pushMessages(data.context);
    }
  }

  var resolveLogout = function(data) {
    var status = data.status;
    if(status == 'error') {
      systemPush("Error: "+data.context);
    } else if(status == "OK") {
      systemPush("Successfully logged out.");
    }
  }

  var resolveMessage = function(data) {
    var status = data.status;
    if(status == 'error') {
      systemPush("Error: "+data.context);
    }
  }

  var resolveNewMessage = function(data) {
    pushMessage(noUsr, data.context);
    attemptScroll();
  }

  init();

  var attemptScroll = function() {
    if(holdOn) return;
    scrollArea.stop().animate({scrollTop: chatBox.height()}, 'slow');
  }

  $(".nano").nanoScroller();

});