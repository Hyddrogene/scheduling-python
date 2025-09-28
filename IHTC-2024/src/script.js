const wheel = document.getElementById("wheel");
const spinButton = document.getElementById("spinButton");
const indicator = document.getElementById("indicator"); // Le div où le résultat sera affiché

// Segments de la roue (chaque segment est lié à un div avec un nom)
const segments = document.querySelectorAll('.segment');  // Récupère tous les segments

spinButton.addEventListener("click", () => {
  // Génère un angle de rotation aléatoire entre 360 et 3960 degrés
  const randomDegree = Math.floor(Math.random() * 3600) + 360;

  // Applique la rotation à la roue avec transition
  wheel.style.transition = "transform 4s ease-out";
  wheel.style.transform = `rotate(${randomDegree}deg)`;

  // Désactiver le bouton pendant que la roue tourne
  spinButton.disabled = true;

  // Réactiver le bouton après la fin de l'animation (4s)
  setTimeout(() => {
    // Position de l'indicateur (on utilise sa position réelle dans le DOM)
    const indicatorRect = indicator.getBoundingClientRect();
    const indicatorCenterX = indicatorRect.left + indicatorRect.width / 2;
    const indicatorCenterY = indicatorRect.top + indicatorRect.height / 2;

    // Fonction pour calculer la distance euclidienne entre deux points
    const calculateDistance = (x1, y1, x2, y2) => {
      return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    };

    // Initialiser le segment le plus proche et la distance minimale
    let closestSegment = null;
    let minDistance = Infinity;

    // Boucle pour vérifier chaque segment
    segments.forEach(segment => {
      // Récupérer le texte à l'intérieur du segment
      const textDiv = segment.querySelector('div');
      if (textDiv) {
        const textRect = textDiv.getBoundingClientRect();
        const textCenterX = textRect.left + textRect.width / 2;
        const textCenterY = textRect.top + textRect.height / 2;

        // Calculer la distance entre l'indicateur et le centre du texte
        const distance = calculateDistance(indicatorCenterX, indicatorCenterY, textCenterX, textCenterY);

        // Mettre à jour le segment le plus proche
        if (distance < minDistance) {
          minDistance = distance;
          closestSegment = segment;
        }
      }
    });

    // Afficher le résultat dans le div "indicator"
    if (closestSegment) {
      const result = closestSegment.querySelector('div').textContent;  // Récupérer le texte du segment
      alert( `Vous avez gagné: ${result}`);
    }

    // Réactiver le bouton après la rotation
    spinButton.disabled = false;
  }, 4000);
});



const updateSegmentOptions = (optionIndex, newContent) => {
  const option = segmentSelector.querySelector(`option[value="${optionIndex}"]`);
  if (option) {
    option.textContent = `(${newContent})`;
  }
};


// Segments de la roue (chaque segment est lié à un div avec un nom)
const segments2 = document.querySelectorAll('.segment div');  // Récupère tous les div contenant les titres dans les segments

// Sélection des éléments de contrôle
const segmentSelector = document.getElementById("segmentSelector");
const newTitleInput = document.getElementById("newTitle");
const changeTitleButton = document.getElementById("changeTitleButton");

// Changer le titre d'un segment
changeTitleButton.addEventListener("click", () => {
  const selectedSegmentIndex = parseInt(segmentSelector.value);
  const newTitle = newTitleInput.value;

  if (newTitle.trim()) {
    if(selectedSegmentIndex == 0){
      segments2[0].textContent = newTitle;
      segments2[1].textContent = newTitle;
    }
    if(selectedSegmentIndex == 1){

      segments2[2].textContent = newTitle;
      segments2[3].textContent = newTitle;
    }
    if(selectedSegmentIndex == 2){
      segments2[4].textContent = newTitle;
      segments2[5].textContent = newTitle;
    }
    if(selectedSegmentIndex == 3){
      segments2[6].textContent = newTitle;
      segments2[7].textContent = newTitle;
    }
    updateSegmentOptions(selectedSegmentIndex,newTitle);
    newTitleInput.value = ''; // Réinitialiser le champ après le changement
  } else {
    alert('Veuillez entrer un nouveau titre.');
  }
});
