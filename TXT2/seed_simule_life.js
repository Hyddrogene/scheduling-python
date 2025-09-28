 /*create function for life simulation*/
    function lifeSimulation() {
        /*create a loop to iterate through the rows*/
        for (var i = 0; i < rows; i++) {
            /*create a loop to iterate through the columns*/
            for (var j = 0; j < columns; j++) {
                /*create a variable to store the number of neighbors*/
                var neighbors = 0;
                /*create a loop to iterate through the rows*/
                for (var x = -1; x <= 1; x++) {
                    /*create a loop to iterate through the columns*/
                    for (var y = -1; y <= 1; y++) {
                        /*create a variable to store the row index*/
                        var rowIndex = i + x;
                        /*create a variable to store the column index*/
                        var columnIndex = j + y;
                        /*check if the row index is within the range*/
                        if (rowIndex >= 0 && rowIndex < rows) {
                            /*check if the column index is within the range*/
                            if (columnIndex >= 0 && columnIndex < columns) {
                                /*check if the cell is alive*/
                                if (grid[rowIndex][columnIndex].alive) {
                                    /*increment the number of neighbors*/
                                    neighbors++;
                                }
                            }
                        }
                    }
                }
                /*check if the cell is alive*/
                if (grid[i][j].alive) {
                    /*decrement the number of neighbors*/
                    neighbors--;
                }
                /*check if the number of neighbors is less than 2*/
                if (neighbors < 2) {
                    /*set the cell to dead*/
                    grid[i][j].alive = false;
                }
                /*check if the number of neighbors is greater than 3*/
                if (neighbors > 3) {
                    /*set the cell to dead*/
                    grid[i][j].alive = false;
                }
                /*check if the number of neighbors is equal to 3*/
                if (neighbors == 3) {
                    /*set the cell to alive*/
                    grid[i][j].alive = true;
                }
            }
        }
    }
    /*create function for drawing the grid*/
    function drawGrid() {
        /*create a loop to iterate through the rows*/
        for (var i = 0; i < rows; i++) {
            /*create a loop to iterate through the columns*/
            for (var j = 0; j < columns; j++) {
                /*check if the cell is alive*/
                if (grid[i][j].alive) {
                    /*set the fill color to black*/
                    context.fillStyle = "black";
                } else {
                    /*set the fill color to white*/
                    context.fillStyle = "white";
                }
                /*draw the cell*/
                context.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
    /*create function for drawing the grid*/
    function drawGrid() {
        /*create a loop to iterate through the rows*/
        for (var i = 0; i < rows; i++) {
            /*create a loop to iterate through the columns*/
            for (var j = 0; j < columns; j++) {
                /*check if the cell is alive*/
                if (grid[i][j].alive) {
                    /*set the fill color to black*/
                    context.fillStyle = "black";
                } else {
                    /*set the fill color to white*/
                    context.fillStyle = "white";
                }
                /*draw the cell*/
                context.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
    /*create function for drawing the grid*/
    function drawGrid() {
        /*create a loop to iterate through the rows*/
        for (var i = 0; i < rows; i++) {
            /*create a loop to iterate through the columns*/
            for (var j = 0; j < columns; j++) {
                /*check if the cell is alive*/
                if (grid[i][j].alive) {
                    /*set the fill color to black*/
                    context.fillStyle = "black";
                } else {
                    /*set the fill color to white*/
                    context.fillStyle = "white";
                }
                /*draw the cell*/
                context.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
    /*create function for drawing the grid*/
    function drawGrid() {
        /*create a loop to iterate through the rows*/
        for (var i = 0; i < rows; i++) {
            /*create a loop to iterate through the columns*/
            for (var j = 0; j < columns; j++) {
                /*check if the cell is alive*/
                if (grid[i][j].alive) {
                    /*set the fill color to black*/
                    context.fillStyle = "black";
                } else {
                    /*set the fill color to white*/
                    context.fillStyle = "white";
                }
                /*draw the cell*/
                context.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
    /*create function for drawing the grid*/
    function drawGrid() {
        /*
