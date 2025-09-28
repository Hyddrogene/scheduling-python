package XHSTT14;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Parser.ParserITC;

public class ParserXHSTT extends ParserITC {
	public File file;
	public String filename;
	
	public void readConfig() {
		this.file = new File(this.filename);
		try {
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		      DocumentBuilder builder = factory.newDocumentBuilder();
		      
		      Document document = builder.parse(file);
		      document.getDocumentElement().normalize();

		      Element root = document.getDocumentElement();
		      Element instance = (Element)root.getElementsByTagName("Instance").item(0);
		      
		      Element timeGroups = (Element)instance.getElementsByTagName("TimeGroups").item(0);
		      
		      NodeList days = timeGroups.getElementsByTagName("Day");
		      NodeList weeks = timeGroups.getElementsByTagName("Week");
		      NodeList timeGroup = timeGroups.getElementsByTagName("TimeGroup");
		      
		      
		      Element times = (Element)instance.getElementsByTagName("Times").item(0);
		      
		      NodeList timeList = times.getElementsByTagName("Time");
		      
		      boolean oneWeek;
		      ArrayList<Week14> myWeeks = new ArrayList<Week14>(); 
		      HashMap<String, Week14> myStringToWeek = new HashMap<String, Week14>();
		      
		      //Time Element
		      if(weeks.getLength() == 0) {
		    	  System.out.println("One week");
		    	  oneWeek = true;
		    	  Week14 w = new Week14("weekType","weekType",1);
		    	  myStringToWeek.put("weekType", w);
		    	  myWeeks.add(w);		      
		      }
		      else {
		    	  oneWeek = false;
		    	  for(int i = 0; i < weeks.getLength() ;i++) {
		    		  Element elt = (Element)weeks.item(i);
			    	  String id = elt.getAttributes().getNamedItem("Id").getNodeValue().toString();
			    	  String name = elt.getElementsByTagName("Name").item(0).getTextContent();
			    	  Week14 w = new Week14(name,id,(i+1));
			    	  myStringToWeek.put(id, w);
			    	  myWeeks.add(w);
		    	  }
		    	  
		    	  

		      }
		      
		      ArrayList<Day14> myDays = new ArrayList<Day14>();
		      HashMap<String, Day14> myStringToDay = new HashMap<String,Day14>();
		      
		      for(int i = 0; i < days.getLength() ;i++) {
		    	  Element elt = (Element)days.item(i);
		    	  String id = days.item(i).getAttributes().getNamedItem("Id").getNodeValue().toString();
		    	  String name = elt.getElementsByTagName("Name").item(0).getTextContent();
		    	  Day14 tmpDay = new Day14(id,name,(i+1));
		    	  myDays.add(tmpDay);
		    	  myStringToDay.put(id, tmpDay);
		      }
		      
		      
		      for(int i = 0; i < timeList.getLength() ;i++) {
		    	  Element elt = (Element)timeList.item(i);
		    	  String id = elt.getAttributes().getNamedItem("Id").getNodeValue().toString();
		    	  String name = elt.getElementsByTagName("Name").item(0).getTextContent();
		    	  
		    	  String refDay = elt.getElementsByTagName("Day").item(0).getAttributes().getNamedItem("Reference").getNodeValue().toString();
		    	  
		    	  Time14 tmpTime = new Time14(id, name, (i+1));
		    	  tmpTime.setRefDay(myStringToDay.get(refDay));
		    	  myStringToDay.get(refDay).addTimes(tmpTime);
		    	  
		    	  if(oneWeek) {
		    		  myWeeks.get(0).addTime(tmpTime);
		    		  tmpTime.setRefWeek(myWeeks.get(0));
		    	  }
		    	  else {
			    	  String refWeek = elt.getElementsByTagName("Week").item(0).getAttributes().getNamedItem("Reference").getNodeValue().toString();
		    		  myStringToWeek.get(refWeek).addTime(tmpTime);
		    		  tmpTime.setRefWeek(myWeeks.get(0));
		    	  }
	
		      }
		      
		      System.out.println(myStringToWeek);
		      
		      Element ressourceTypes = (Element) instance.getElementsByTagName("ResourceTypes").item(0);
		      HashMap<String, String> ressourceTypeGive = new HashMap<String, String>(); 
		      
		      HashMap<String, Boolean> innerMap = new HashMap<>();
		      innerMap.put("Teacher", false);
		      innerMap.put("Room", false);
		      innerMap.put("Student", false);
		      innerMap.put("Class", false);
		      
		      ArrayList<Room> rooms = new ArrayList<Room>();
		      ArrayList<Class14> classes = new ArrayList<Class14>();
		      ArrayList<Teacher>  teachers = new ArrayList<Teacher>();
		      ArrayList<Course>  courses = new ArrayList<Course>();
		      
		      HashMap<String,Room> roomsRef = new HashMap<String,Room>();
		      HashMap<String,Class14> classesRef = new HashMap<String,Class14>();
		      HashMap<String,Teacher>  teachersRef = new HashMap<String,Teacher>();
		      HashMap<String,Course>  coursesRef = new HashMap<String,Course>();
		      
		      
		      NodeList ressourceTypes2 =  ressourceTypes.getElementsByTagName("ResourceType");
		      
		      
		      for(int i = 0; i < ressourceTypes2.getLength() ;i++) {
		    	  Element elt = (Element)ressourceTypes2.item(i);
		    	  String id = elt.getAttributes().getNamedItem("Id").getNodeValue().toString();
		    	  String name = elt.getElementsByTagName("Name").item(0).getTextContent();
		    	  if( id.equals("Teacher") || name.equals("Teacher")) {
		    		  innerMap.replace("Teacher", true);
		    	  }
		    	  else if( id.equals("Room") || name.equals("Room")) {
		    		  innerMap.replace("Room", true);
		    	  }
		    	  else if( id.equals("Student") || name.equals("Student")) {
		    		  innerMap.replace("Student", true);
		    	  }
		    	  else if( id.equals("Class") || name.equals("Class")) {
		    		  innerMap.replace("Class", true);
		    	  }
		    	  else {
		    		  System.out.println("Ressource can't be read "+id);
		    	  }  
		      }
		      System.out.println("Ressource  "+innerMap);
		      Element ressourcesElement = (Element)instance.getElementsByTagName("Resources").item(0);
		      //System.out.println("Elt "+instance.getElementsByTagName("Resources").item(0).getTextContent().toString());

		      NodeList ressourceList = ressourcesElement.getElementsByTagName("Resource");
		      for(int i = 0; i < ressourceList.getLength() ;i++) {
		    	  Element elt = (Element)ressourceList.item(i);
		    	  String id = elt.getAttributes().getNamedItem("Id").getNodeValue().toString();
		    	  String name = elt.getElementsByTagName("Name").item(0).getTextContent();
		    	  String resourceType = elt.getElementsByTagName("ResourceType").item(0).getAttributes().getNamedItem("Reference").getNodeValue().toString();
		    	  NodeList resourceGroups = elt.getElementsByTagName("ResourceGroup");//.item(0).getAttributes().getNamedItem("Reference").getNodeValue().toString()
		    	  ArrayList<String> labels = new ArrayList<String>();
		    	  
		    	  for(int j = 0; j < resourceGroups.getLength() ;j++) {
		    		  Element elt2 = (Element)resourceGroups.item(j);
			    	  String refElt = elt2.getAttributes().getNamedItem("Reference").getNodeValue().toString(); 
			    	  labels.add(refElt);
			    	  
		    	  }
		    	  
		    	  ressourceTypeGive.put(id, resourceType);
		    	  
		    	  if(resourceType.equals("Room")) {
		    		  Room room = new Room(id,name,resourceType,labels);
		    		  rooms.add(room);
		    		  roomsRef.put(id,room);
		    	  }
		    	  else if(resourceType.equals("Teacher")) {
		    		  Teacher teacher = new Teacher(id,name,resourceType,labels);
		    		  teachers.add(teacher);
		    		  teachersRef.put(id,teacher);
		    	  }
		    	  else if(resourceType.equals("Class")) {
		    		  Class14 class14 = new Class14(id,name,resourceType,labels);
		    		  classes.add(class14);
		    		  classesRef.put(id,class14);
		    	  }
		    	  else if(resourceType.equals("Student")) {
		    		  Class14 class14 = new Class14(id,name,resourceType,labels);
		    		  classes.add(class14);
		    		  classesRef.put(id,class14);
		    	  }
		    	  else {
		    		  System.out.println("Ressource can't be find "+resourceType);
		    	  }
		    	  
		      }
		      
		      
		      Element events = (Element)instance.getElementsByTagName("Events").item(0);
		      
		      Element eventGroups = (Element)events.getElementsByTagName("EventGroups").item(0);
		      NodeList eventGroupList = eventGroups.getElementsByTagName("Course");
		      HashMap<String, Course> stringToCourses = new HashMap<String, Course>();
		      
		      for(int i = 0; i < eventGroupList.getLength() ;i++) {
		    	  Element elt = (Element)eventGroupList.item(i);
		    	  String id = elt.getAttributes().getNamedItem("Id").getNodeValue().toString();
		    	  String name = elt.getElementsByTagName("Name").item(0).getTextContent();
		    	  Course course = new Course(id,name);
		    	  courses.add(course);
		    	  stringToCourses.put(id, course);
		      }
		      
		      
		      
		      NodeList eventList = events.getElementsByTagName("Event");
		      ArrayList<Event> myEvents = new ArrayList<Event>();
		      
		      for(int i = 0; i < eventList.getLength() ;i++) {
		    	  Element elt = (Element)eventList.item(i);
		    	  String id = elt.getAttributes().getNamedItem("Id").getNodeValue().toString();
		    	  String name = elt.getElementsByTagName("Name").item(0).getTextContent();
		    	  int duration = Integer.parseInt(elt.getElementsByTagName("Duration").item(0).getTextContent());
		    	  
		    	  ArrayList<Room> roomsEvnt = new ArrayList<Room>();
		    	  ArrayList<Teacher> teachersEvnt = new ArrayList<Teacher>();
		    	  ArrayList<Class14> classesEvnt = new ArrayList<Class14>();
		    	  
		    	  String courseRef = elt.getElementsByTagName("Course").item(0).getAttributes().getNamedItem("Reference").getNodeValue().toString();

		    	  
		    	  Course courseTmp = stringToCourses.get(courseRef);
		    	  
		    	  NodeList ResourceList = elt.getElementsByTagName("Resource");
		    	  for(int j = 0; j < ResourceList.getLength() ;j++) {
		    		  
		    		  if(ResourceList.item(j).getAttributes().getNamedItem("Reference") != null) {
			    		  String ressourceRef = ResourceList.item(j).getAttributes().getNamedItem("Reference").getNodeValue().toString();
			    		  String u = ressourceTypeGive.get(ressourceRef);
			    		  if (u == null) {
			    			  //System.out.println("Undefined element ref:"+ressourceRef+" >TODO");
			    			  if(innerMap.get(ressourceRef)) {
				    				  if(ressourceRef.equals("Room")) {
				    					  roomsEvnt = rooms ;
					    			  }
					    			  else if(ressourceRef.equals("Teacher")) {
					    				  teachersEvnt = teachers ;
					    			  }
					    			  else if(ressourceRef.equals("Student") || ressourceRef.equals("Class")) {
					    				  classesEvnt = classes ;
					    			  }
					    			  else {
					    				  System.out.println("Impossible");
					    			  }
				    			  

			    			  }
			    			  else {
			    				  System.out.println("Undefined ressource type for "+"ref:"+ressourceRef);
			    			  }
			    		  }
			    		  else {
			    			  if(u.equals("Room")) {
			    				  roomsEvnt.add(roomsRef.get(ressourceRef));
			    			  }
			    			  else if (u.equals("Teacher")) {
			    				  teachersEvnt.add(teachersRef.get(ressourceRef));
			    			  }
			    			  else if(u.equals("Class") || u.equals("Student")) {
			    				  classesEvnt.add(classesRef.get(ressourceRef));
			    			  } 
			    			  else {
			    				  System.out.println("Undefined ressource type:"+u+" ref:"+ressourceRef);
			    			  }
			    		  }
		    		  }
		    		  else {
		    			  Element elt3 = (Element)ResourceList.item(j);
		    			  String ressourceRef = elt3.getElementsByTagName("ResourceType").item(0).getAttributes().getNamedItem("Reference").getNodeValue().toString();
		    			  
		    			  if(innerMap.get(ressourceRef)) {
		    				  if(ressourceRef.equals("Room")) {
		    					  roomsEvnt = rooms ;
			    			  }
			    			  else if(ressourceRef.equals("Teacher")) {
			    				  teachersEvnt = teachers ;
			    			  }
			    			  else if(ressourceRef.equals("Student") || ressourceRef.equals("Class")) {
			    				  classesEvnt = classes ;
			    			  }
			    			  else {
			    				  System.out.println("Impossible");
			    			  }
		    			  

	    			  }
	    			  else {
	    				  System.out.println("Undefined ressource type for "+"ref:"+ressourceRef);
	    			  }
		    		  }

		    	  }
		    	  NodeList ndl = elt.getElementsByTagName("EventGroup"); 
		    	  //EventGroup Reference=
		    	  ArrayList<String> labels = IntStream.range(0, ndl.getLength() )
		    			  .mapToObj(u-> ndl.item(u).getAttributes().getNamedItem("Reference").getNodeValue().toString())
		    			  .collect(Collectors.toCollection(ArrayList<String>::new));
		    	  //.getAttributes().getNamedItem("Reference").getNodeValue().toString();
		    	  Event event = new Event(id, name, duration,courseTmp);
		    	  event.setClasses(classesEvnt);
		    	  event.setRooms(roomsEvnt);
		    	  event.setTeachers(teachersEvnt);
		    	  event.setLabels(labels);
		    	  myEvents.add(event);
		      }
		      
		      Element eltConstraint = (Element) instance.getElementsByTagName("Constraints").item(0);
	    	  String[] constraintName = new String[] {
	    	  "AvoidUnavailableTimesConstraint",
	    	  "PreferTimesConstraint",
	    	  "LimitWorkloadConstraint",
	    	  "LinkEventsConstraint",
	    	  "ClusterBusyTimesConstraint",
	    	  "DistributeSplitEventsConstraint",
	    	  "PreferResourcesConstraint",
	    	  "AvoidClashesConstraint",
	    	  "AvoidSplitAssignmentsConstraint",
	    	  "AssignResourceConstraint",
	    	  "SplitEventsConstraint",
	    	  "AssignTimeConstraint",
	    	  "LimitIdleTimesConstraint",
	    	  "OrderEventsConstraint",
	    	  "SpreadEventsConstraint",
	    	  "LimitBusyTimesConstraint"};	      
	    	  HashMap<String, ArrayList<String>> possibleParameters = new HashMap<String, ArrayList<String>>();
	    	  
	    	  //AvoidUnavailableTimesConstraint
	    	  possibleParameters.put( "AvoidUnavailableTimesConstraint",Arrays.stream(new String[] {"Times","TimeGroups"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //PreferTimesConstraint
	    	  possibleParameters.put( "PreferTimesConstraint",Arrays.stream(new String[] {"Times","Duration","TimeGroups"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //LimitWorkloadConstraint
	    	  possibleParameters.put( "LimitWorkloadConstraint",Arrays.stream(new String[] {"Minimum","Maximum"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //LinkEventsConstraint
	    	  possibleParameters.put( "LinkEventsConstraint",Arrays.stream(new String[] {}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //ClusterBusyTimesConstraint 
	    	  possibleParameters.put( "ClusterBusyTimesConstraint",Arrays.stream(new String[] {"TimeGroups","Minimum","Maximum"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //DistributeSplitEventsConstraint
	    	  possibleParameters.put( "DistributeSplitEventsConstraint",Arrays.stream(new String[] {"Duration","Minimum","Maximum"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //PreferResourcesConstraint
	    	  possibleParameters.put( "PreferResourcesConstraint",Arrays.stream(new String[] {"Role","ResourceGroups","Resources"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //AvoidClashesConstraint
	    	  possibleParameters.put( "AvoidClashesConstraint",Arrays.stream(new String[] {}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //AvoidSplitAssignmentsConstraint
	    	  possibleParameters.put( "AvoidSplitAssignmentsConstraint",Arrays.stream(new String[] {"Role"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //AssignResourceConstraint
	    	  possibleParameters.put( "AssignResourceConstraint",Arrays.stream(new String[] {"Role"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //SplitEventsConstraint
	    	  possibleParameters.put( "SplitEventsConstraint",Arrays.stream(new String[] {"MinimumDuration","MaximumDuration","MinimumAmount","MaximumAmount"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //AssignTimeConstraint
	    	  possibleParameters.put( "AssignTimeConstraint",Arrays.stream(new String[] {}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //LimitIdleTimesConstraint 
	    	  possibleParameters.put( "LimitIdleTimesConstraint",Arrays.stream(new String[] {"TimeGroups","Minimum","Maximum"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //OrderEventsConstraint
	    	  possibleParameters.put( "OrderEventsConstraint",Arrays.stream(new String[] {}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //SpreadEventsConstraint
	    	  possibleParameters.put( "SpreadEventsConstraint",Arrays.stream(new String[] {"TimeGroups"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  //LimitBusyTimesConstraint
	    	  possibleParameters.put( "LimitBusyTimesConstraint",Arrays.stream(new String[] {"TimeGroups","Minimum","Maximum"}).map(ui->ui)
	    			  .collect(Collectors.toCollection(ArrayList<String>::new)));
	    	  /*
	    	   * <Name>Op maximaal 4 dagen les (gewicht 1000)</Name>
					<Required>false</Required>
					<Weight>1000</Weight>
					<CostFunction>Quadratic</CostFunction>
					<AppliesTo>
						<Resources>
							<Resource Reference="BRD"/>
							<Resource Reference="CUA"/>
							<Resource Reference="GEJ"/>
							<Resource Reference="GRA"/>
							<Resource Reference="KAE"/>
							<Resource Reference="KUE"/>
							<Resource Reference="LAI"/>
							<Resource Reference="LMA"/>
							<Resource Reference="VEO"/>
							<Resource Reference="WOE"/>
						</Resources>
					</AppliesTo>
					<TimeGroups>
						<TimeGroup Reference="gr_ma"/>
						<TimeGroup Reference="gr_di"/>
						<TimeGroup Reference="gr_wo"/>
						<TimeGroup Reference="gr_do"/>
						<TimeGroup Reference="gr_vr"/>
					</TimeGroups>
					<Minimum>0</Minimum>
					<Maximum>4</Maximum>
	    	   * */
	    	  
	    	  HashMap<String, String> valueFromValue = new HashMap<String,String>();
	    	  valueFromValue.put("Resources", "Resource");
	    	  valueFromValue.put("EventGroups", "EventGroup");
	    	  valueFromValue.put("Events", "Event");
	    	  valueFromValue.put("ResourceGroups", "ResourceGroup");
	    	  
		      for(int i = 0; i < constraintName.length ;i++) {
		    	  
		    	  String name = eltConstraint.getElementsByTagName("Name").item(0).getTextContent();
		    	  String hardness = eltConstraint.getElementsByTagName("Required").item(0).getTextContent();
		    	  int weitght = Integer.parseInt(eltConstraint.getElementsByTagName("Weight").item(0).getTextContent());
		    	  String costFunction = eltConstraint.getElementsByTagName("CostFunction").item(0).getTextContent();
		    	  Element ndlElt = (Element)eltConstraint.getElementsByTagName("AppliesTo").item(0);
		    	  //AppliesTo  =  Counter({'Resources': 1020, 'EventGroups': 960, 'Events': 267, 'ResourceGroups': 231, 'EventPairs': 1})
		    	  
		    	  NodeList nodeResourceList = ndlElt.getElementsByTagName("Resource");
		    	  NodeList nodeEventGroupList =ndlElt.getElementsByTagName("EventGroup");
		    	  NodeList nodeEventList =ndlElt.getElementsByTagName("Event");
		    	  NodeList nodeResourceGroupList =ndlElt.getElementsByTagName("ResourceGroup");
		    	  
		    	  String typeRef = "X";
		    	  if(nodeResourceList.getLength() > 0) {
		    		  typeRef = "Resource";
		    	  }
		    	  else if(nodeEventGroupList.getLength() > 0) {
		    		  typeRef = "EventGroup";
		    	  }
	    	      else if(nodeEventList.getLength() > 0) {
	    	    	  typeRef = "Event";
		    	  }
	    	  	  else if(nodeResourceGroupList.getLength() > 0) {
	    	  		typeRef = "ResourceGroup";
	    	      }
		    	  
		    	  
		    	  //TODO
		    	  
		    	  for(String param : possibleParameters.get(constraintName[i] )) {
		    		  NodeList ndl = eltConstraint.getElementsByTagName(param);
		    	  }

		      }
		      //System.out.println(myEvents);
		      
				/*
				 * 
LinkEventsConstraint  =  Name: 173, Required: 173, Weight: 173, CostFunction: 173, AppliesTo: 173
  
    
        
        
        
        				<Event Id="E0">
					<Name>E0</Name>
					<Duration>1</Duration>
					<Resources>
						<Resource>
							<Role>RoleRoom</Role>
							<ResourceType Reference="Room" />
						</Resource>
						<Resource Reference="Teacher6" />
						<Resource Reference="Student184" />
						<Resource Reference="Student222" />
						<Resource Reference="Student223" />
						<Resource Reference="Student224" />
						<Resource Reference="Student225" />
						<Resource Reference="Student226" />
						<Resource Reference="Student227" />
						<Resource Reference="Student228" />
						<Resource Reference="Student229" />
						<Resource Reference="Student230" />
						<Resource Reference="Student231" />
						<Resource Reference="Student232" />
						<Resource Reference="Student233" />
					</Resources>
					<EventGroups>
						<EventGroup Reference="EGAll" />
						<EventGroup Reference="Course0" />
					</EventGroups>
				</Event>
            
            
            <Event Id="C0T3R0">
          <Name>C0T3R0</Name>
          <Duration>2</Duration>
          <Resources>
            <Resource Reference="C0">
              <Role>Class</Role>
              <ResourceType Reference="Class"/>
            </Resource>
            <Resource Reference="T3">
              <Role>Teacher</Role>
              <ResourceType Reference="Teacher"/>
            </Resource>
            <Resource Reference="R0">
              <Role>Room</Role>
              <ResourceType Reference="Room"/>
            </Resource>
          </Resources>
          <EventGroups>
            <EventGroup Reference="gr_AllEvents"/>
          </EventGroups>
        </Event>
            
            
            
				*/
		      
		      
		      //NodeList weeks = root.getElementsByTagName("weeks");
		      //this.nrWeeks = Integer.parseInt(weeks.item(0).getTextContent());		      
		      //Resources
		      //ResourceTypes
		      //ResourceType
		      
		}
		  catch (Exception e) {
			   System.out.println("Error while parsing XHSTT file");
			   e.printStackTrace();
		  }
	}//FinMethod
	
	public ParserXHSTT(String filename) {
		this.filename = filename;
		readConfig();
	}//FinMethod
}//FinClass
