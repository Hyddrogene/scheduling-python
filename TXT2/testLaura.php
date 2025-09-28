<?php


function changeDate($date = 0,$guill = ""){
    //test date is not empty
    if($date == 0){
        exit("ERROR DATE IS EMPTY IN FUNCTION CHANGEDATE");
    }
    //Convert date in integer
    $convertDate = intval($date);
    if($convertDate<10){
        $date = "0".strval($convertDate+1);
    }
    else{
        $date = strval($convertDate+1);
    }
    return "CAST(".$guill.$date.$guill."AS TIME)";
}//FinFunction

function requestGenerator($attribute1,$date,$time1,$time2){
    $time1 = changeDate($time1,"'");
    $time2 = changeDate($time2,"'");
    $requestString = "SELECT * from reservation where desk = '$attribute1' AND scheduled_date=$date AND 
    ($time1 > start_time OR $time1 < end_time) AND ($time2 >start_time OR $time2 <end_time)";
    //time au format 9:4:6 -> HH:MM:SS  CAST($time1 AS TIME)
}//FinFunction



?>
