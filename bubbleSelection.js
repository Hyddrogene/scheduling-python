


function addKeyword() {
    const selectedKeyword = keywordSelect.value;
    if (!selectedKeyword) return;
    const keywordDiv = document.createElement('div');
    keywordDiv.className = 'keyword';
    keywordDiv.textContent = selectedKeyword;
    const removeSpan = document.createElement('span');
    removeSpan.textContent = '×';
    removeSpan.onclick = function() { keywordDiv.remove();};
    keywordDiv.appendChild(removeSpan);
    document.getElementById('selectedKeywords').appendChild(keywordDiv);
}


document.addEventListener('DOMContentLoaded', function () {
  var keywordsContainer = Array.from(document.querySelectorAll('.constraint-keyword'));
  var bubblesContainer = Array.from(document.querySelectorAll('.constraint-keyword-bubbles'));
  for(let i= 0;i < keywordsContainer.length ;i++){

    var keywords = keywordsContainer[i].textContent.replace(/\s+/g, ",").split(/,/).filter(Boolean).map(function(keyword) {
      return keyword.trim();
    });

  bubblesContainer[i].innerHTML = keywords.map(function(keyword) {
      return '<span class="keyword-bubble">' + keyword + '</span>';
  }).join(' ');

  // Optionally hide the original keywords container
  keywordsContainer[i].style.display = 'none';


  }

});


function getSelectedKeywords() {
    const keywordElements = document.querySelectorAll('#selectedKeywords .keyword');
    const keywords = Array.from(keywordElements).map(element => {
                return element.textContent.slice(0, -1);
    });
    console.log(keywords);
    return keywords;
}




function filterConstraints() {
    let tags = getSelectedKeywords();//document.getElementById('keywordFilter').value.toLowerCase();
    var file = fileInput.files[0];
    var reader = new FileReader();

    reader.onload = function(e) {
        var data = JSON.parse(e.target.result);
        var nodes = [];
        
        var edges = [];        
        sizeMatrix = data.DATA.nr_sessions;
        squareMatrix =  createSquareMatrix(sizeMatrix)
        console.log(squareMatrix)


        data.CONSTRAINTS.forEach(constraint => {
            let sessions = constraint.sessions[0].set;
            for (let i = 0; i < sessions.length; i++) {
                for (let j = i + 1; j < sessions.length; j++) {
                    console.log(constraint.constraint)
                    if(tags.includes(constraint.constraint)){
                        squareMatrix[(sessions[i]-1)][(sessions[j]-1)] = 1;
                    }
                    
                }
            }

        });

        for (let i = 0; i < sizeMatrix; i++) {
                for (let j = i + 1; j < sizeMatrix; j++) {
                    if(squareMatrix[i][j] == 1){
                        edges.push({
                        from: (i+1),
                        to: (j+1)
                    });
                    }

                }
            }

        console.log("squareMatrix",squareMatrix)
        console.log("edges",edges)

        for (let i = 1; i <= data.DATA.nr_sessions; i++) {
            nodes.push({ id: i, label: `Session ${i}` });
        }

        
        console.log(allConstraintType);
        data2 = {nodes: nodes,edges: edges}
        visualizeGraph(data2);
    };

    reader.readAsText(file);

}



function addElementLi(name, value){
    const keywordSelect = document.getElementById('instancesvalues');
    const option = document.createElement('li');
    option.value = name;
    option.textContent = name+" : "+value;
    keywordSelect.appendChild(option);
}