/*linefeed($dzn);
comment($dzn,"DECOMPTE DES ELEMENTS PRINCIPAUX");
linefeed($dzn);
//
linefeed($dzn);
comment($dzn,"DECOMPTE DES SOUS-ELEMENTS");
linefeed($dzn);
$x = "nrPartEquipments";
$qry = $qPartEquipments;
jsonVariableEncoding(writeCount($x,$qry),$x,$activited,$jsonFile);

//writeSimpleInt($x, array_reduce( iterator_to_array($xpath->query($qParameters)), function($carry, $item){$a = sizeof(explode(",",$item->textContent));return $carry+$a; } ));

$x = "nrPartSessionsTeacher";
$qry = $qPartSessionsTeacher;
jsonVariableEncoding(writeCount($x,$qry),$x,$activited,$jsonFile);

//
linefeed($dzn);
comment($dzn,"DIMENSIONS");
linefeed($dzn);
$x = "maxSessionRank";
$qry = $qParts;
$a = "nrSessions";
$maxSessionRank=writeMaxBound($x,$qry,$a);
jsonVariableEncoding($maxSessionRank,$x,$activited,$jsonFile);
*/
/*$x = "maxTeacherCapacity";
$qry = $qPartTeachersSet;
$a = "studentsPerTeacher";
jsonVariableEncoding(writeMaxBound($x,$qry,$a),$x,$activited,$jsonFile);*/
/*
$x = "maxPartEquipmentCount";
$qry = $qPartEquipments;
$a = "count";
jsonVariableEncoding(writeMaxBound($x,$qry,$a),$x,$activited,$jsonFile);
$x = "maxPartRoomClassBound";
$qry = $qPartRooms;
$a = "max";
jsonVariableEncoding(writeMaxBound($x,$qry,$a),$x,$activited,$jsonFile);
$x = "maxPartTeacherClassBound";
$qry = $qPartTeachers;
$a = "max";
jsonVariableEncoding(writeMaxBound($x,$qry,$a),$x,$activited,$jsonFile);
*/
//
/*
writeSubElementsSets(nameVariable,taille,Query,sous-element,numbering element,option)1=id 2=refId 3=txt 4=autre 
*/
/*
linefeed($dzn);
comment($dzn,"RELATIONS ENTRE CLASSES D'ELEMENTS (STRUCTURELLES/ASSOCIATIVES)");
linefeed($dzn);
$x = "partEquipments";
$r = "nrParts";
$qry = $qParts;
$subtag = "equipment";
jsonArraySetEncoding(writeSubElementsSets($x,$r,$qry,$subtag,$equipments,2),$x,$activited,$jsonFile);
$x = "classGroups";
$r = "nrClasses";
$qry = $qClasses;
$subtag = "group";
jsonArraySetEncoding(writeSubElementsSets($x,$r,$qry,$subtag,$groups,2),$x,$activited,$jsonFile);
*/
//
/*
* writeAttributes(nomVariable,taille,query,element,type)1=nombre,2=type def,3=string
 
$x = "partEquipmentCount";
$r = "nrPartEquipments";
$qry = $qPartEquipments;
$a = "count";
jsonArrayEncoding(writeAttributes($x,$r,$qry,$a,1),$x,$activited,$jsonFile,"int");
*/
/*
$x = "partRoomClassBound";
$r1 = "nrPartRooms";
$r2="UTT_RESOURCE_BOUND";
$qry = $qPartRooms;
$a = ["min","max"];
writeAttributePairs($x,$r1,$r2,$qry,$a,1);
*/

/*$x = "parameterValue";
$qry = $qParameters;
$attribute = "textContent";
writePropertiesParameter($x,$attribute,$qry);

$x = "partSessionsTeacher";
$r = "nrParts";
$qry = $qPartTeachersSet;
$a = "sessionTeachers";
jsonArrayEncoding(writeAttributes($x,$r,$qry,$a,1),$x,$activited,$jsonFile,"int");
*/
/* useless
$x = "partTeacherCapacity";
$r = "nrParts";
$qry = $qPartTeachersSet;
$a = "studentsPerTeacher";
writeAttributes($x,$r,$qry,$a,1);
*/
/*
$x = "scopeClustering";
$r = "nrScopes";
$qry = $qSessions;
$a = "groupBy";
jsonArrayEncoding(writeAttributes($x,$r,$qry,$a,2,"utt_"),$x,$activited,$jsonFile,"enum");
*/
/*$x = "parameterValue";
$r = "nrParameters";
$qry = $qParameters;
writeValue($x,$r,$qry,1);*/
