<?php
function createDzn($argv,$st = ""){
    $out = "";
    $filename = $st;
    if(sizeof($argv) < 1 and $st == "") {
        exit("Argv not complete\n");
    }
    if(sizeof($argv) >= 2) {
        $filename = $argv["1"];
    }
    $fhandler = fopen($filename, 'r');
	if($fhandler === false) {
		return $out;
	}
	$i = 0;
	while($line = fgets($fhandler)) {
		if($i == 0) {
            $i++;
			continue;
		}
		$splitLine = preg_split('/\s/', $line);
		if($i == 1){
            $out .= "nbVertex = ".strval($splitLine[0]).";\n";
            $out .= "nbEdge = ".strval($splitLine[2]).";\n";
		}
		if($i==2){
            $out .= "Edge = array2d(1..nbEdge,1..2,[ ".strval($splitLine[0]).",".strval($splitLine[1]).",\n";
		}
		if($i >2){
            $out .= strval($splitLine[0]).",".strval($splitLine[1]).",\n";
		}
		$i++;
    }
    fclose($fhandler);
    $out = substr($out,0,-2);
    $out .= "]);\n";
    return $out;
    
}//FinFunction

$st = "/home/etud/Téléchargements/arkLIsyEf/dwt__221.mtx.rnd";
print_r(createDzn($argv,$st));

?>
