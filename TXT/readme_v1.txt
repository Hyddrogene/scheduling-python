for execute the code you can use the ./timetabling/tools/tools_php/Execute.php
with the command line :

    time php Execute.php /chemin/du/fichier/nomdufichier.xml

ou simplement en executant 
 
    time php Execute.php 
    
en paramétrant la variable $st = "/chemin/du/fichier/nomdufichier.xml";

cela va crée un objet Execute qui à plusieurs méthode :

Une première qui s'appelle parse() elle va permettre de construire le graphe qui représente le fichier xml 

(1) $exec = new Execute($argv,$st);//crée l'objet éxecute
(2) $exec->parse();//execute s'occupe de faire le parsing du fichier
(3 option) $exec->getStatistique("write");//On peut récupérer les statsiques du fichier dans /chemin/du/fichier/nomdufichier_statistics.txt
(3 option) $exec->generateParsedFile("dzn");//xml, dzn, json

Il existe 2 facon pour résoudre le problème :

/!\ à l'heure actuel pour une résolution minizinc l'ensemble des contraintes n'est pas accepté -> les contraintes de types forbidden_period sont explosives en effet elle ne tienne pas en mémoire /!\
/!\ Le temps de compilation du modèle minzinc est très long 30-45mn, et la résolution est de l'ordre de 20 à 86 secondes /!\
/!\ Il faut utiliser ortools ou chuffed, gecode ne fonctionne pas. La contrainte global argsort n'aide pas à la propagation dans le problème /!\
/!\ Pour une simplifications de la commande un fichier .mpc est utilisé en le modifiant on peut alors avoir accès à d'autres solver /!\
 
la commande 4 va éxecuter via minzinc et tout faire d'un coup et écrire le résultat dans /timetabling/tools/tmp/experiment_date-du-jour/nomdufichier_extension_datedujour_h_mn_s.txt
(4) $exec->solve("mzn",false);//mzn -> solve with minzinc, chr -> solve with chr//false -> minizinc error msg, true -> no msg minzinc error

Dans cette version on execute une première fois un script minzinc qui va compilé au format fzn un modèle minizinc.
(4 bis) $exec->compile("mzn");//Une seule option pour le moment le format mzn// lance la compilation du fichier parser par Execute 
// le fichier crée se nomme : /timetabling/tools/tmp/experiment_date-du-jour/nomdufichier_extension_datedujour_h_mn_s.fzn

(4 bis) $exec->solution();//Lance la résolution via un solveur en donnant le format fzn à un solveur
//le fichier resultats se nomme: /timetabling/tools/tmp/experiment_date-du-jour/nomdufichier_extension_datedujour_h_mn_s_v2.txt

la sortie du solveur n'étant au format attendu pour être écrit en xml il faut alors le convertir au bon format
(4 bis) $exec->solutionConverter();//récupére dans /timetabling/tools/tmp/experiment_date-du-jour/nomdufichier_extension_datedujour_h_mn_s_v2.txt
//cependant si un nomdefichierautre.txt est fournis en entrée il transformera au bon format le fichier en entrée.
//le fichier resultats se nomme: /timetabling/tools/tmp/experiment_date-du-jour/nomdufichier_extension_datedujour_h_mn_s_v3.txt

//transforme le fichier de l'étape précédente en XML accepter par le schéma USP.
(4 bis) $exec->convert2xml();
//crée 2 fichiers xml : /timetabling/tools/tmp/experiment_date-du-jour/nomdufichier_extension_datedujour_h_mn_s_session_v3.xml
// /timetabling/tools/tmp/experiment_date-du-jour/nomdufichier_extension_datedujour_h_mn_s_class_v3.xml
