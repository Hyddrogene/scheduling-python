<?php
function generate_date_latex($d, $m,$nbJour,$y){
    $y = strval($y);
    $out = "\begin{center}\n".'\section*{'.$m." ".$y.'}'."\n".'\end{center}'."\n";
    
    $out .= "\\newcolumntype{B}[1]{>{\columncolor{red!30}\\raggedright}m{#1}}
\\newcolumntype{G}[1]{>{\columncolor{green!30}\\raggedright}m{#1}}
            {
            \setlength\LTleft{-1.2cm}
            \begin{longtable}{|c|B{7cm}|G{7cm}|}
            \hline
            Jour & \centering Corentin  & \centering Bryan  
            \\tabularnewline\n\hline\n".'\endhead'."\n";
            
    $endLine = "\\tabularnewline\n\hline\n";
    $listItemName= '&';
    $listItem = 
         '\begin{itemize}'."\n".
             '\item exemple'."\n".
         '\end{itemize}'."\n";
         
    $tabDay = [1=>"Lundi",2=>"Mardi",3=>"Mercredi",4=>"Jeudi",5=>"Vendredi",6=>"Samedi",7=>"Dimanche"];
    $dayBegin = array_search($d, $tabDay);
    $j = $dayBegin; 
    $nbDayWeek = count($tabDay);
    for($i = 1;$i<=$nbJour;$i++){
        $dMod = $tabDay[1+(($j-1) % $nbDayWeek )];
        $out .= '\\textit{'.$dMod." ". strval($i) ." $m}";
        $out .= $listItemName.'%%%%%%%%%%%% Corentin %%%%%%%%%%'."\n";
        $out .= $listItem;
        $out .= $listItemName.'%%%%%%%%%%% Bryan %%%%%%%%%'."\n";
        $out .= $listItem; 
        $out.= $endLine;
        $j++;
    }

$out .= "    \caption{Agenda $m}\n
    \label{tab:$m$y}\n".
'\end{longtable}'."\n".
'}'."\n";
return $out;
}//FinFunction

$fd = 30;
//$fd = 30;
//$fd = 29;
$fd = 30;
$fd = 31; 
$day = "Lundi";
$day = "Dimanche";
$day = "Mercredi";
$day = "Jeudi";
$day = "Samedi";
$day = "Vendredi";
$day = "Lundi";
$mois = "Décembre";
$mois = "Janvier";
//$mois = "Février";
//$mois = "Février";
//$mois = "Mai";
//$mois = "Juin";
//$mois = "Juillet";
//$mois = "Décembre";
//$mois = "Mars";
$y = 2022;
$y =2023;
$y = 2024;
print_r(generate_date_latex($day,$mois,$fd,$y));
?>
