package FeatureModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Les fonctionnalités (features) sont organisées sous forme d'arbre. Certaines peuvent être 
 * activées et validées selon les contraintes du modèle.
 *
 * <p>L'objet {@code FMwithUTP} permet de :
 * <ul>
 *   <li>Vérifier si certaines fonctionnalités sont valide ;</li>
 *   <li>Activées les features valide et leurs parentes.</li>
 *   <li>Générer un Feature model correspondant à l'instance fournit.</li>
 * </ul>
 */
public class FMwithUTP {
	private FeatureModel fm;
	private InstanceUTPArray utp;
	
	/**
	 * Constructeur de la classe {@code FMwithUTP}.
	 * <p>
	 * Initialise une instance à partir :
	 * <ul>
	 *   <li>d'un fichier représentant un modèle de fonctionnalités (feature model), 
	 *       chargé de manière vierge (toutes les fonctionnalités désactivées),</li>
	 *   <li>et d'une instance {@code InstanceUTPArray} représentant les données spécifiques à UTP.</li>
	 * </ul>
	 *
	 * <p>Ce constructeur appelle ensuite la méthode {@code verificationUTP()} afin de vérifier 
	 * la cohérence entre le modèle et les données UTP fournies.
	 *
	 * @param fileFM chemin vers le fichier XML du modèle de fonctionnalités
	 * @param utp    instance des données UTP à associer
	 * @throws Exception si une erreur survient lors du chargement ou de la vérification
	 */
	public FMwithUTP(String fileFM, InstanceUTPArray utp) throws Exception {
		this.fm = FeatureModelParser.parseFeatureModelFalse(fileFM);
		this.utp = utp;
		verificationUTP();
	}//FinMethod
	
	/**
	 * Vérifie les propriétés de l'instance UTP afin d'activer les caractéristiques (features)
	 * correspondantes dans le modèle de fonctionnalités.
	 *
	 * <p>La méthode parcourt les différentes propriétés de l’instance {@code InstanceUTPArray}
	 * et utilise des booléens pour déterminer si certaines conditions sont remplies.
	 * Pour chaque condition vérifiée, la feature correspondante est activée dans le
	 * modèle de fonctionnalités {@code fm}, en mémoire.
	 *
	 * <p>Par exemple :
	 * <ul>
	 *   <li>La feature {@code course-hierarchy} est activée systématiquement.</li>
	 *   <li>La feature {@code event} est activée si, pour au moins une partie de cours, la liste des salles ou
	 *       la liste des enseignants est vide.</li>
	 * </ul>
	 *
	 */
	public void verificationUTP(){
		//==== course hierarchy
		// Active la feature "course-hierarchy" dans tous les cas.
		// Cela signifie que la hiérarchie des cours (cours -> parties -> classes -> séances) est utilisée.
		
		this.fm.findAndActivate("course-hierarchy");
		
		//===== event
		// Vérifie si, pour au moins une partie du cours, aucune salle ou aucun enseignant n'est associé.
		// Si c'est le cas, cela signifie qu'on veut un cours spécial et qu'on le considère comme un événement, 
		
		boolean b_event = false;
		
		for(int p= 0; p < this.utp.nr_parts ; p++) {
			if(this.utp.part_rooms.get(p).size() <= 0 || this.utp.part_teachers.get(p).size() <= 0 ) {
				b_event = true;
				break;
			}
		}
		if(b_event) {
			this.fm.findAndActivate("event");
		}		
		//====== full-period
		
		//TODO
		
		//====== full-week
		// Active la feature si la semaine d’enseignement couvre 7 jours.

		boolean b_fullweek = false;
		
		if(this.utp.nr_days_per_week == 7) {
			b_fullweek = true;
		}

		if(b_fullweek) {
			this.fm.findAndActivate("full-week");
		}
		
		//====== no-overlap
		// Active la contrainte d’absence de chevauchement entre les séances,
		// si les créneaux journaliers permettent de planifier sans conflit. (les créneaux sont suffisafement grand)
		//  La vérification consiste à :
		// - Parcourir chaque partie de cours (p) et examiner ses créneaux journaliers (part_dailyslots).
		// - Pour chaque intervalle entre deux créneaux consécutifs,
		//   on vérifie s’il est suffisamment long pour accueillir une séance de n’importe quelle partie (p2).
		// - Si un intervalle est trop court pour contenir au moins une séance complète,
		//   la contrainte "no-overlap" ne peut pas être garantie, et elle n’est pas activée.
		//
		// Si aucun conflit n'est détecté, la feature "no-overlap" est activée.
		// Cela signifie que les séances peuvent être planifiées sans se superposer.

		boolean b_nooverlap = true;

		
		for(int p=0; p < this.utp.part_session_length.length; p++ ) {
			
			for(int dl = 0; dl < this.utp.part_dailyslots.get(p).size()-1 ;dl++) {
				
				for(int p2=0; p2 < this.utp.part_session_length.length; p2++ ) {
					int session_lenght = this.utp.part_session_length[p2];
					
					if(session_lenght > ( this.utp.part_dailyslots.get(p).get(dl+1) - this.utp.part_dailyslots.get(p).get(dl)) ) {
						
						b_nooverlap = false;
						break;
					}
				}
			}
		}
		
		if(b_nooverlap) {
			this.fm.findAndActivate("no-overlap");
		}
		
		
		//===== same-duration
		// Active la feature same-duration si toutes les parties de cours ont exactement 
		// la même durée de séance (session_length).
		
		boolean b_sameduration = true;
		
		int val = this.utp.part_session_length[0];
		
		for(int p=1; p < this.utp.part_session_length.length; p++ ) {
			if(val != this.utp.part_session_length[p]) {
				b_sameduration = false;
				break;
			}
		}
		
		if(b_sameduration) {
			this.fm.findAndActivate("same-duration");
		}
		
		
		// ===== modular (synchronous)
		// Active la feature "modular" si la grille horaire est dite "abstraite",
		// c’est-à-dire alignée selon des blocs horaires fixes (ex. : 1h, 1h30, etc.).
		//
		// Cela est déterminé par la variable `abstract_grid` :
		// - Si elle vaut 1 (ou tout autre valeur non nulle), la grille est modulaire.
		// - Dans ce cas, la planification peut être basée sur une grille commune

				
		boolean b_synchronous = this.utp.abstract_grid == 0 ?  false:true;
		
		if(b_synchronous) {
			this.fm.findAndActivate("modular");
		}
		
				
		//====== single-week
		// Active la feature "single-week" si l’instance UTP ne couvre qu’une seule semaine.

		
		boolean b_singleweek = false;
		
		if(this.utp.nr_weeks == 1) {
			b_singleweek = true;
		}

		if(b_singleweek) {
			this.fm.findAndActivate("single-week");
		}
		
		
		//====== no-room
		// Active la feature "no-room" si aucune salle n'est spécifiée pour une parties de cours.

		boolean b_noroom = true;
		
		for(int p= 0; p < this.utp.nr_parts ; p++) {
			if(this.utp.part_rooms.get(p).size() > 0 || this.utp.part_room_use[p].equals("")) {
				b_noroom = b_noroom && false;
			}
		}
		if(b_noroom) {
			this.fm.findAndActivate("no-room");
		}
		
		//====== single-room
		// Active la feature "single-room" si au moins une partie est explicitement marquée 
		// comme utilisant une seule salle via part_room_use = "single".

		
		boolean b_singleroom = true;
		
		for(int p= 0; p < this.utp.nr_parts ; p++) {
			if(this.utp.part_room_use[p].equals("single")) {
				b_singleroom = true;
				break;
			}
		}
		if(b_singleroom) {
			this.fm.findAndActivate("single-room");
		}
		
		//====== multi-room
		// Active la feature "multi-room" si au moins une partie est marquée comme pouvant utiliser
		// plusieurs salles (part_room_use = "multiple").

		
		boolean b_multiroom = true;
		
		for(int p= 0; p < this.utp.nr_parts ; p++) {
			if(this.utp.part_room_use[p].equals("multiple")) {
				b_multiroom = true;
				break;
			}
		}
		if(b_multiroom) {
			this.fm.findAndActivate("multi-room");
		}
		
		
		//====== room-capacity
		// Active la feature "room-capacity" si au moins une salle a une capacité définie.
		// Ici, on vérifie que la valeur de capacité n’est pas égale à -2 (valeur sentinelle indiquant
		// l’absence de donnée). Dès qu'une salle possède une capacité valide, la feature est activée.

		boolean b_roomcapacity = false;
		
		for(int r= 0; r < this.utp.nr_rooms ; r++) {
			if(this.utp.room_capacity[r] != -2) {
				b_roomcapacity = true;
				break;
			}
		}
		if(b_roomcapacity) {
			this.fm.findAndActivate("room-capacity");
		}
		
		
		//====== none-exclusive
		// Active la feature "none-exclusive" si toutes les salles utilisées ont une contrainte
		// explicite de non-recouvrement (noOverlap) de type "room".
		//
		// Étapes de vérification :
		// 1. On identifie les salles utilisées (celles ayant au moins une séance potentielle).
		// 2. On cherche les contraintes "noOverlap" appliquées aux salles dans la liste des contraintes.
		// 3. On vérifie que toutes les salles utilisées figurent bien dans ces contraintes.
		//
		// Si c’est le cas, cela signifie qu’aucune salle ne peut être utilisée par deux séances en parallèle,
		// donc la feature "none-exclusive" est activée.

		
		boolean b_noneexclusive = false;
		
		ArrayList<Integer> rooms = new ArrayList<Integer>();
		ArrayList<Integer> used_rooms = new ArrayList<Integer>();
		
		for(int i = 0; i < this.utp.nr_rooms ;i++) {
			if(this.utp.room_sessions.get(i).size() > 0 ) {
				used_rooms.add(i);
			}
		}
		
		
		
		for(ConstraintUTP constraint : this.utp.constraints) {
			
			if(constraint.getConstraint().equals("noOverlap")) {
				if(constraint.getType()[0].equals("room")) {
					rooms.add(constraint.getElements()[0]);
				}
			}
			
		}
		
		int count_rooms = 0;
		
		for(int i = 0 ; i < used_rooms.size() ;i++) {
			if(rooms.contains(used_rooms.get(i))) {
				count_rooms++;
			}
			
		}
		
		
		
		if(count_rooms == used_rooms.size()) {
			b_noneexclusive = true;
		}
		
		if(b_noneexclusive) {
			this.fm.findAndActivate("none-exclusive");
		}
		
		//====== all-exclusive
		// Active la feature "all-exclusive" si aucune salle utilisée ne possède de contrainte
		// explicite de non-recouvrement ("noOverlap").

		
		boolean b_allexclusive = false;

		if(count_rooms == 0) {
			b_allexclusive = true;
		}
		
		if(b_allexclusive) {
			this.fm.findAndActivate("all-exclusive");
		}
		
		//====== some-exclusive
		// Active la feature "some-exclusive" si certaines salles utilisées ont une contrainte
		// de non-recouvrement, mais pas toutes.
		//
		// Cela reflète un cas intermédiaire entre "none-exclusive" (toutes contraintes présentes)
		// et "all-exclusive" (aucune contrainte présente).

		boolean b_someexclusive = false;

		if(count_rooms > 0  && count_rooms != used_rooms.size() ) {
			b_someexclusive = true;
		}
		
		if(b_someexclusive) {
			this.fm.findAndActivate("some-exclusive");
		}
		
		//====== no-teacher
		// Active la feature "no-teacher" si au moins une partie de cours ne possède aucun enseignant associé,
		// ou si le nombre d’enseignants requis par séance est nul.

		boolean b_noteacher = false;//true;
		
		for(int p= 0; p < this.utp.nr_parts ; p++) {
			if(this.utp.part_teachers.get(p).size() <= 0 || this.utp.part_session_teacher_count[p] <= 0 ) {
				b_noteacher = true;//b_noteacher && false;
				break;
			}
		}
		if(b_noteacher) {
			this.fm.findAndActivate("no-teacher");
		}
		
		//====== single-teacher
		// Active la feature "single-teacher" si au moins une partie a exactement un enseignant associé
		// par séance (session_teacher_count = 1).
		
		boolean b_singleteacher = false;
		
		for(int p= 0; p < this.utp.nr_parts ; p++) {
			if(this.utp.part_teachers.get(p).size() > 0 && this.utp.part_session_teacher_count[p] == 1 ) {
				b_singleteacher = true;//b_singleteacher && false;
				break;
			}
		}
		if(b_singleteacher) {
			this.fm.findAndActivate("single-teacher");
		}
		
		//====== multi-teacher
		// ===== multi-teacher
		// Active la feature "multi-teacher" si au moins une partie a plusieurs enseignants associés,
		// et si le nombre d’enseignants par séance est supérieur à un.
		
		boolean b_multiteacher = false;
		
		for(int p= 0; p < this.utp.nr_parts ; p++) {
			if(this.utp.part_teachers.get(p).size() > 1 && this.utp.part_session_teacher_count[p] > 1 ) {
				b_multiteacher = true;//b_singleteacher && false;
				break;
			}
		}
		if(b_multiteacher) {
			this.fm.findAndActivate("multi-teacher");
		}
		
		//====== session-overlap-teacher
		
		boolean b_sessionoverlapteacher = false;
		
		ArrayList<Integer> teachers = new ArrayList<Integer>();
		ArrayList<Integer> used_teachers = new ArrayList<Integer>();
		
		for(int i = 0; i < this.utp.nr_teachers ;i++) {
			if(this.utp.teacher_sessions.get(i).size() > 0 ) {
				used_teachers.add(i);
			}
		}
		
		
		
		for(ConstraintUTP constraint : this.utp.constraints) {
			
			if(constraint.getConstraint().equals("noOverlap")) {
				if(constraint.getType()[0].equals("teacher")) {
					teachers.add(constraint.getElements()[0]);
				}
			}
			
		}
		
		int count_teachers = 0;
		
		for(int i = 0 ; i < used_teachers.size() ;i++) {
			if(teachers.contains(used_teachers.get(i))) {
				count_teachers++;
			}
			
		}
		
		
		
		if(count_teachers == used_teachers.size()) {
			b_sessionoverlapteacher = true;
		}
		//Uohw ZHad DgXj FWS6 sQ
		if(b_sessionoverlapteacher) {
			this.fm.findAndActivate("session-overlap");
		}
		
		
		//====== service
		
		boolean b_teacherservice = true;
		
        int somme_service = java.util.Arrays.stream(this.utp.part_teacher_sessions_count).flatMapToInt(java.util.Arrays::stream).sum(); 
        
		if(somme_service == 0) {
			b_teacherservice = false;
		}
        
		if(b_teacherservice) {
			this.fm.findAndActivate("service");
		}
		
		//====== session-overlap-student
		
		boolean b_sessionoverlapstudent = false;
		
		ArrayList<Integer> students = new ArrayList<Integer>();
		ArrayList<Integer> used_studentns = new ArrayList<Integer>();
		
		for(int i = 0; i < this.utp.nr_groups ;i++) {
			if(this.utp.group_sessions.get(i).size() > 0 ) {
				used_studentns.add(i);
			}
		}
		
		
		
		for(ConstraintUTP constraint : this.utp.constraints) {
			
			if(constraint.getConstraint().equals("noOverlap")) {
				if(constraint.getType()[0].equals("student") || constraint.getType()[0].equals("group")) {
					students.add(constraint.getElements()[0]);
				}
			}
			
		}
				
		
		
		if(  students.size() == used_studentns.size()) {
			b_sessionoverlapstudent = true;
		}
		//Uohw ZHad DgXj FWS6 sQ
		if(b_sessionoverlapstudent) {
			this.fm.findAndActivate("session-overlap ");
		}
		
		
		//====== sectionning
		
		boolean b_sectionning = false;
		
		b_sectionning =  this.utp.nr_groups == 0? false:true;
		
		if(b_sectionning) {
			this.fm.findAndActivate("sectioning");
		}
		
		
		//====== Constraint list name 
		
		List<String> ruleName = this.utp.constraints.stream().map(u->u.getConstraint()).distinct().toList();
		System.out.println(ruleName);
		
		// Ce bloc de code permet d'analyser l'ensemble des contraintes
		// déclarées dans l'objet UTP (utp.constraints), puis d'en extraire
		// le nom de chaque contrainte sous forme de chaîne de caractères.
		//
		// Chaque contrainte est ensuite comparée à un ensemble prédéfini
		// de groupes de contraintes (par "feature") :
		//
		// - "calendar"     : contraintes liées au calendrier et aux créneaux horaires
		// - "regularity"   : contraintes de régularité entre les séances
		// - "orchestration": contraintes sur l’organisation entre plusieurs séances
		// - "workload"     : contraintes sur la charge de travail
		// - "logistics"    : contraintes sur les affectations logistiques
		// - "ressourcing"  : contraintes sur les ressources (salles, enseignants)
		//
		// Si au moins une contrainte d’un groupe est utilisée dans le modèle,
		// la feature correspondante est activée via le modèle de features (fm).
		//
		//
		// Exemple :
		// Si "sameTeachers" et "gap" apparaissent parmi les contraintes,
		// alors les features "regularity" et "workload" seront activées.
		//
		// Liste des contraintes pour les features
		// - Calendar      : allowedPeriod assignSlot assigneWeek assignDay assignWeeklySlot forbiddenPeriod allowedGrids
		// - regularity    : sameRooms sameTeachers sameSlot sameDay sameWeekDay sameWeek periodic
		// - Orchestration : sequenced differentDay differentSlot differentWeekDay differentWeek gap
		// - workload      : gap session_workload time_workload compactness
		// - Logistics     : sameRooms sameTeachers differentTeachers differentRooms  
		// - ressourcing   : allowedRooms allowedTeachers requiredRooms requiredTeachers forbiddenRooms forbiddenTeachers assignRooms assignTeachers
		
		

		Map<String, List<String>> features = new HashMap<>();
		features.put("calendar", List.of("allowedPeriod", "assignSlot", "assignWeek", "assignDay", "assignWeeklySlot", "forbiddenPeriod", "allowedGrids"));
		features.put("regularity", List.of("sameRooms", "sameTeachers", "sameSlot", "sameDay", "sameWeekDay", "sameWeek", "periodic"));
		features.put("orchestration", List.of("sequenced", "differentDay", "differentSlot", "differentWeekDay", "differentWeek", "gap"));
		features.put("workload", List.of("gap", "session_workload", "time_workload", "compactness"));
		features.put("logistics", List.of("sameRooms", "sameTeachers", "differentTeachers", "differentRooms"));
		features.put("ressourcing", List.of("allowedRooms", "allowedTeachers", "requiredRooms", "requiredTeachers", "forbiddenRooms", "forbiddenTeachers", "assignRooms", "assignTeachers"));

		// Activation des features si au moins une contrainte est présente
		for (Map.Entry<String, List<String>> entry : features.entrySet()) {
		    String feature = entry.getKey();
		    List<String> constraints = entry.getValue();

		    boolean isActive = constraints.stream().anyMatch(ruleName::contains);

		    if (isActive) {
		        this.fm.findAndActivate(feature);
		    }
		}

		
	}//FinMethod
	
	public FeatureModel getFeatureModel() {
		return this.fm;
	}//FinMethod
}//FinClass
