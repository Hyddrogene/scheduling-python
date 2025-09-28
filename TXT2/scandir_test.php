<?php

function directory_lien_creator($fdirectory,$c,&$display){
    $output = scandir($fdirectory);
    for($i= 2;$i<count($output);$i++){
        if(preg_match("/\w+\.(html|php)/",$output[$i]) && $c==0){
            $name = explode(".",$output[$i])[0];
            $display .= "<li><a href=\"".$fdirectory."/".$output[$i]."\" />".$name."</a></li>\n";
        }
        elseif(!preg_match("/\w*\.\w*/",$output[$i]) && $c!=0){
            directory_lien_creator($fdirectory."/".$output[$i],$c-1,$display);
        }
    }
}//FinFunction

$fdirectoryex = "public_html_starwars";
//s$fdirectoryex = "Public/";
$display = "";
directory_lien_creator($fdirectoryex,1,$display);
print_r($display);
?>
