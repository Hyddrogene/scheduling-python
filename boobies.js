 
const distanceRange = document.getElementById('distanceRange');
const circleContainer = document.querySelector('.circle-container');

// Fonction pour mettre à jour la variable CSS --distance
function updateDistance() {
  const distance = distanceRange.value + 'px';
  circleContainer.style.setProperty('--distance', distance);
}

// Écouter les changements sur le slider et mettre à jour la distance
distanceRange.addEventListener('input', updateDistance);

// Mettre à jour la distance initiale
//updateDistance();
