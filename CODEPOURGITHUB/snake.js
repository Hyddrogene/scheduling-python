const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");

const snakeSize = 10;
const foodSize = 10;

let snake = [    {x: 100, y: 100},    {x: 90, y: 100},    {x: 80, y: 100},    {x: 70, y: 100},    {x: 60, y: 100},];

let food = {x: 200, y: 200};

let dx = snakeSize;
let dy = 0;

function drawSnakePart(snakePart) {
    ctx.fillStyle = "lightgreen";
    ctx.fillRect(snakePart.x, snakePart.y, snakeSize, snakeSize);
    ctx.strokeStyle = "darkgreen";
    ctx.strokeRect(snakePart.x, snakePart.y, snakeSize, snakeSize);
}

function drawSnake() {
    snake.forEach(drawSnakePart);
}

function advanceSnake() {
    const head = {x: snake[0].x + dx, y: snake[0].y + dy};
    snake.unshift(head);
    if (snake[0].x === food.x && snake[0].y === food.y) {
        food = {x: Math.floor(Math.random() * canvas.width/10) * snakeSize, y: Math.floor(Math.random() * canvas.height/10) * snakeSize};
    } else {
        snake.pop();
    }
}

function drawFood() {
    ctx.fillStyle = "red";
    ctx.fillRect(food.x, food.y, foodSize, foodSize);
    ctx.strokeStyle = "darkred";
    ctx.strokeRect(food.x, food.y, foodSize, foodSize);
}

function changeDirection(event) {
    const LEFT_KEY = 37;
    const RIGHT_KEY = 39;
    const UP_KEY = 38;
    const DOWN_KEY = 40;

    const keyPressed = event.keyCode;
    const goingUp = dy === -snakeSize;
    const goingDown = dy === snakeSize;
    const goingRight = dx === snakeSize;
    const goingLeft = dx === -snakeSize;

    if (keyPressed === LEFT_KEY && !goingRight) {
        dx = -snakeSize;
        dy = 0;
    }
    if (keyPressed === UP_KEY && !goingDown) {
        dx = 0;
        dy = -snakeSize;
    }
    if (keyPressed === RIGHT_KEY && !goingLeft) {
        dx = snakeSize;
        dy = 0;
    }
    if (keyPressed === DOWN_KEY && !goingUp) {
        dx = 0;
        dy = snakeSize;
    }
}

function main() {
    if (hasGameEnded()) {
        alert("Game over");
        return;
    }

    setTimeout(function onTick() {
        clearCanvas();
        drawFood();
        advanceSnake();
        drawSnake();

        main();
    }, 100);
}

function clearCanvas() {
    ctx.fillStyle = "white";
    ctx.strokeStyle = "black";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    ctx.strokeRect(0, 0, canvas.width, canvas.height);
}

// La fonction pour mettre à jour la direction du serpent
function changeDirection(event) {
  const LEFT_KEY = 37;
  const RIGHT_KEY = 39;
  const UP_KEY = 38;
  const DOWN_KEY = 40;

  const keyPressed = event.keyCode;

  const goingUp = dy === -10;
  const goingDown = dy === 10;
  const goingRight = dx === 10;
  const goingLeft = dx === -10;

  if (keyPressed === LEFT_KEY && !goingRight) {
    dx = -10;
    dy = 0;
  }

  if (keyPressed === UP_KEY && !goingDown) {
    dx = 0;
    dy = -10;
  }

  if (keyPressed === RIGHT_KEY && !goingLeft) {
    dx = 10;
    dy = 0;
  }

  if (keyPressed === DOWN_KEY && !goingUp) {
    dx = 0;
    dy = 10;
  }
}

// La fonction pour dessiner le serpent
function drawSnake() {
  snake.forEach((segment) => {
    ctx.fillStyle = "green";
    ctx.fillRect(segment.x, segment.y, 10, 10);
  });
}

// La fonction pour déplacer le serpent
function moveSnake() {
  const head = { x: snake[0].x + dx, y: snake[0].y + dy };
  snake.unshift(head);
  const hasEatenFood = snake[0].x === foodX && snake[0].y === foodY;
  if (hasEatenFood) {
    score += 10;
    document.getElementById("score").innerHTML = score;
    generateFood();
  } else {
    snake.pop();
  }
}

// La fonction pour générer de la nourriture pour le serpent
function generateFood() {
  foodX = Math.floor(Math.random() * 30) * 10;
  foodY = Math.floor(Math.random() * 30) * 10;
  snake.forEach((segment) => {
    const hasEatenFood = segment.x === foodX && segment.y === foodY;
    if (hasEatenFood) {
      generateFood();
    }
  });
}

// La fonction principale pour animer le jeu
function main() {
  if (didGameEnd()) {
    return;
  }

  setTimeout(function onTick() {
    clearCanvas();
    drawFood();
    moveSnake();
    drawSnake();
    main();
  }, 100);
}

// La fonction pour initialiser le jeu
function init() {
  document.addEventListener("keydown", changeDirection);
  generateFood();
  main();
}

init();
 
