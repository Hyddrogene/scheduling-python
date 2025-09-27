// URL ou chemin d'accès local au fichier JSON

const jsonUrl = 'http://0.0.0.0:8000/example_extension_v2.json';
//const jsonUrl = './example_extension_v2.json';
//const jsonUrl = 'example_extension_v2.json';


// Fonction pour charger le JSON et initialiser le DataSet
async function loadJsonAndCreateNodes() {
    var nodes = [];

    const response = await fetch(jsonUrl);
    const data = await response.json();
    const nrSessions = data.DATA.nr_sessions;
    console.log(data.DATA.nr_sessions)

    for (let i = 1; i <= nrSessions; i++) {
        nodes.push({ id: i, label: `Session ${i}` });
    }
    
    console.log(nodes)
    return nodes;
    }    




// Fonction pour charger le JSON et initialiser les arêtes
function loadJsonAndCreateEdges() {
    fetch(jsonUrl)
        .then(response => response.json())
        .then(data => {
            // Initialisation du DataSet pour les arêtes
            var edges = [];


            // Traitement de chaque contrainte
            data.CONSTRAINTS.forEach(constraint => {
                // Pour chaque contrainte, créer des arêtes entre toutes les paires de sessions
                let sessions = constraint.sessions[0].set; // Prendre le tableau de sessions de la première entrée

                // Générer des arêtes pour chaque paire de sessions dans cette contrainte
                for (let i = 0; i < sessions.length; i++) {
                    for (let j = i + 1; j < sessions.length; j++) {
                        edges.push({
                            from: sessions[i],
                            to: sessions[j]//,
                            //label: constraint.constraint,
                            //font: { align: 'top' }
                        });
                    }
                }
            });

            // Vous pouvez utiliser 'edges' ici pour initialiser un graphique ou pour d'autres opérations
            console.log(Array.from(edges)); // Affiche les arêtes dans la console
        })
        .catch(error => {
            console.error('Error loading or parsing the JSON file:', error);
        });

        return edges;
}


async function ssync () {
    var nodes = [];
    var nrSessions
    fetch(jsonUrl).then(response=>response.json()).then(datas=>{
        nrSessions = datas.DATA.nr_sessions;
        console.log(datas.DATA.nr_sessions)
    

    });
    return nrSessions
}

var value 
async function displayValue() {
    try {
        var nrSessions = await ssync();
        nodes = []
        for (let i = 1; i <= nrSessions; i++) {
            nodes.push({ id: i, label: `Session ${i}` });
        }
        //        value = await nodes; // 'await' va attendre que la promesse soit résolue


        console.log("Valeur récupérée de la promesse:", nrSessions);
        console.log("Nodes:", nodes);
        

        vvalue = [
            {id: 1, label: 'Node 1'},
            {id: 2, label: 'Node 2'},
            {id: 3, label: 'Node 3'},
            {id: 4, label: 'Node 4'},
            {id: 5, label: 'Node 5'}
        ];

        console.log("PUTAIN",nodes[0])
        console.log("VPUTAIN",vvalue[0])
        var nodesObject = new vis.DataSet(nodes);
      
        //var edges = loadJsonAndCreateEdges();

        var edges = new vis.DataSet([
            {from: 1, to: 3, label: '5', font: {align: 'top'}},
            {from: 1, to: 2, label: '3', font: {align: 'top'}},
            {from: 2, to: 4, label: '6', font: {align: 'top'}},
            {from: 2, to: 5, label: '2', font: {align: 'top'}}
        ]);
      
      
        var container = document.getElementById('mynetwork');
        var data = {
            nodes: nodesObject,
            edges: edges
        };
        var options = {
          edges: {
            arrows: 'to',
            smooth: {
              type: 'continuous'
            }
          },
          physics: {
            enabled: true // Vous pouvez désactiver la physique si nécessaire
          }
        };
        var network = new vis.Network(container, data, options);



        // Vous pouvez continuer à utiliser 'value' ici
    } catch (error) {
        console.error("Erreur lors de la récupération de la promesse:", error);
    }
    
}
displayValue()





       


// Appel de la fonction au chargement du script
//var nodes = handleNodes();

// Appel de la fonction au chargement du script
//loadJsonAndCreateEdges();


