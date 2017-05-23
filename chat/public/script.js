$(function() {
  const FADE_TIME = 150; 
  const TYPING_TIMER_LENGTH = 400;
  const COLORS = [
    '#BADA55', 'teal', 'crimson', 'orange'
  ];

  const $window = $(window);
  const $usernameInput = $('.usernameInput');
  const $messages = $('.messages');
  const $inputMessage = $('.inputMessage');
  const $loginPage = $('.login.page');
  const $chatPage = $('.chat.page');


  let username;
  let connected = false;
  let typing = false;
  let lastTypingTime;
  let $currentInput = $usernameInput.focus();

  const socket = io();

  function addParticipantsMessage (data) {
    let message = '';
    if (data.numUsers === 1) {
      message += "Du är ensam i chatten";
    } else {
      message += "Det finns " + data.numUsers + " personer i chatten";
    }
    log(message);
  }

  // Sätter användarens användarnamn
  function setUsername () {
    username = cleanInput($usernameInput.val().trim());

    if (username) {
      $loginPage.fadeOut();
      $chatPage.show();
      $loginPage.off('click');
      $currentInput = $inputMessage.focus();

      // Skickar användarnamnet till servern
      socket.emit('add user', username);
    }
  }

  // skickar meddelande
  function sendMessage () {
    let message = $inputMessage.val();
    // Fixar så man inte kan lägga in html taggar i meddelandet
    message = cleanInput(message);

    if (message && connected) {
      $inputMessage.val('');
      addChatMessage({
        username: username,
        message: message
      });
      // säger åt servern att utföra 'new message' funktionen. Vilket skickar med meddelandet
      socket.emit('new message', message);
    }
  }

  // Lägger in meddelandet på sidan
  function log (message, options) {
    const $el = $('<li>').addClass('log').text(message);
    addMessageElement($el, options);
  }


  function addChatMessage (data, options) {
    const $typingMessages = getTypingMessages(data);
    options = options || {};
    if ($typingMessages.length !== 0) {
      options.fade = false;
      $typingMessages.remove();
    }

    const $usernameDiv = $('<span class="username"/>')
      .text(data.username)
      .css('color', getUsernameColor(data.username));
    const $messageBodyDiv = $('<span class="messageBody">')
      .text(data.message);

    const typingClass = data.typing ? 'skriver' : '';
    const $messageDiv = $('<li class="message"/>')
      .data('username', data.username)
      .addClass(typingClass)
      .append($usernameDiv, $messageBodyDiv);

    addMessageElement($messageDiv, options);
  }

  function addChatTyping (data) {
    data.typing = true;
    data.message = 'skriver...';
    addChatMessage(data);
  }

  function removeChatTyping (data) {
    getTypingMessages(data).fadeOut(function () {
      $(this).remove();
    });
  }

  function addMessageElement (el, options) {
    const $el = $(el);

    if (!options) {
      options = {};
    }
    if (typeof options.fade === 'undefined') {
      options.fade = true;
    }
    if (typeof options.prepend === 'undefined') {
      options.prepend = false;
    }

    if (options.fade) {
      $el.hide().fadeIn(FADE_TIME);
    }
    if (options.prepend) {
      $messages.prepend($el);
    } else {
      $messages.append($el);
    }
    $messages[0].scrollTop = $messages[0].scrollHeight;
  }

  function cleanInput (input) {
    return $('<div/>').text(input).text();
  }

  function updateTyping () {
    if (connected) {
      if (!typing) {
        typing = true;
        socket.emit('typing');
      }
      lastTypingTime = (new Date()).getTime();

      setTimeout(function () {
        const typingTimer = (new Date()).getTime();
        const timeDiff = typingTimer - lastTypingTime;
        if (timeDiff >= TYPING_TIMER_LENGTH && typing) {
          socket.emit('stop typing');
          typing = false;
        }
      }, TYPING_TIMER_LENGTH);
    }
  }

  
  function getTypingMessages (data) {
    return $('.typing.message').filter(function (i) {
      return $(this).data('username') === data.username;
    });
  }

  // Får färgen för användarnamnet
  function getUsernameColor (username) {
    let hash = 7;
    for (let i = 0; i < username.length; i++) {
       hash = username.charCodeAt(i) + (hash << 5) - hash;
    }
    const index = Math.abs(hash % COLORS.length);
    return COLORS[index];
  }

  // Keyboard events

  $window.keydown(function (event) {
    if (!(event.ctrlKey || event.metaKey || event.altKey)) {
      $currentInput.focus();
    }
    // När användaren klickar ENTER
    if (event.which === 13) {
      if (username) {
        sendMessage();
        socket.emit('slutat skriva');
        typing = false;
      } else {
        setUsername();
      }
    }
  });

  $inputMessage.on('input', function() {
    updateTyping();
  });

  // Click events

  // Fokusera på inputen vart man än klickar på sidan
  $loginPage.click(function () {
    $currentInput.focus();
  });

  $inputMessage.click(function () {
    $inputMessage.focus();
  });

  // Socket events

  // När servern skickar ut 'login', logga meddelandet
  socket.on('login', function (data) {
    connected = true;
    // Visa välkommen meddelandet
    const message = "Välkommen till min chat";
    log(message, {
      prepend: true
    });
    addParticipantsMessage(data);
  });

  socket.on('new message', function (data) {
    addChatMessage(data);
  });

  socket.on('user joined', function (data) {
    log(data.username + ' har gått med');
    addParticipantsMessage(data);
  });

  socket.on('user left', function (data) {
    log(data.username + ' left');
    addParticipantsMessage(data);
    removeChatTyping(data);
  });

  socket.on('typing', function (data) {
    addChatTyping(data);
  });

  socket.on('stop typing', function (data) {
    removeChatTyping(data);
  });

  socket.on('disconnect', function () {
    log('Du blev frånkopplad');
  });

  socket.on('reconnect', function () {
    log('Du har gått med i chatten igen');
    if (username) {
      socket.emit('add user', username);
    }
  });

  socket.on('reconnect_error', function () {
    log('försöket att reconeccta misslyckades');
  });

});