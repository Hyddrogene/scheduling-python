package Constraint_model.RuleParsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import Constraint_model.GraphUTP;
import Constraint_model.InstanceUTPGraph.Session;


public class RuleParsing {
	private GraphUTP gutp;
	private HashMap<String,ArrayList<Session>> cache;

	private ArrayList<Rule> rulesFinale;
	private ArrayList<Parameter> parameters;
	private ArrayList<Generator> generators;
	private ArrayList<Filter> filters;
	//private ArrayList<ConstraintRule> constraintRules;
	private ArrayList<ConstraintRule> constraintRuless;
	
	public RuleParsing(Element rules,GraphUTP gutp){
		this.gutp = gutp;
		int parameterCount = 1;
		parameters = new ArrayList<>();
		NodeList ruleList = rules.getElementsByTagName("rule");
		rulesFinale = new ArrayList<>();
		constraintRuless = new ArrayList<>();
		for(int i = 0; i < ruleList.getLength() ;i++) {
			Element rule = (Element)ruleList.item(i);
			NodeList selectors = rule.getElementsByTagName("selector");
			ArrayList<String> generators = new ArrayList<>();
			ArrayList<String> filters = new ArrayList<>();
			
			for(int j = 0; j < selectors.getLength()  ;j++) {
				generators.add(selectors.item(j).getAttributes().getNamedItem("generator").getNodeValue().toString());
				filters.add(selectors.item(j).getAttributes().getNamedItem("filters").getNodeValue().toString());
			}
			NodeList constraintXml = rule.getElementsByTagName("constraint");
			ArrayList<ConstraintRule> constraintRules = new ArrayList<>();
			
			for(int j=0; j < constraintXml.getLength() ;j++) {
				Element constraintElt = (Element)constraintXml.item(j);
				String name = constraintXml.item(j).getAttributes().getNamedItem("name").getNodeValue();
				String type = constraintXml.item(j).getAttributes().getNamedItem("type").getNodeValue();
				NodeList parametersElt = constraintElt.getElementsByTagName("parameters");
				ArrayList<ArrayList<Parameter>> parametersParameters = new ArrayList<ArrayList<Parameter>>();
				if(parametersElt.getLength() > 0) {
					for(int pi = 0; pi < parametersElt.getLength() ;pi++) {
						Element paramertsXMLParser = (Element)parametersElt.item(pi);
						NodeList parameterElt = paramertsXMLParser.getElementsByTagName("parameter");
						ArrayList<Parameter> parametersCode = new ArrayList<>();
						for(int pielt = 0; pielt < parameterElt.getLength() ;pielt++) {
							Parameter parametersTmp = new Parameter(parameterElt.item(pielt).getAttributes().getNamedItem("name").getNodeValue(),parameterCount,parameterElt.item(pielt).getTextContent());
							parametersCode.add(parametersTmp);
							parameterCount++;
							parameters.add(parametersTmp);
						}
						parametersParameters.add(parametersCode);
					}
					
				}
				ConstraintRule consRule = new ConstraintRule(name, type,parametersParameters); 
				constraintRules.add(consRule);
				this.constraintRuless.add(consRule);
			}
			rulesFinale.add(new Rule((i+1),generators,filters,constraintRules));
			
			
			
						
		}
		//System.out.println(rulesFinale);
		this.calcul();
	}
	
	   public int[] convertStringToArray(String input) {
	        String[] parts = input.split(",");
	        ArrayList<Integer> list = new ArrayList<>();

	        for (String part : parts) {
	            if (part.contains("-")) {
	                String[] range = part.split("-");
	                int start = Integer.parseInt(range[0].trim());
	                int end = Integer.parseInt(range[1].trim());
	                for (int i = start; i <= end; i++) {
	                    list.add(i);
	                }
	            } else {
	                list.add(Integer.parseInt(part.trim()));
	            }
	        }

	        int[] result = list.stream().mapToInt(u->u).toArray();
	        return result;
	    }//FinMethod
	
	public void calcul() {
		cache = new HashMap<>();
		generators = new ArrayList<>();
		filters = new ArrayList<>();
		int cptfilter = 0;
		int cptGenerator = 0;
		for(int r = 0; r < rulesFinale.size() ;r++) {
						
			for(int s = 0; s < rulesFinale.get(r).generators.size() ;s++) {
				
				String generator = rulesFinale.get(r).generators.get(s);
				String filter = rulesFinale.get(r).filters.get(s);


				ArrayList<Session> filterSession = new ArrayList<>();
				if(cache.containsKey(filter)) {
					filterSession = cache.get(filter);
				}
				else {
					if(!filter.equals("")) {
						try {
							TreeCalcul tr = new TreeCalcul(gutp);
							filterSession =  tr.calcul(filter);
						}catch(RuleParsingException e){
							System.out.println("ERROR IN :"+e.getMessage()+" rule "+r+" generator = "+generator+" filter = "+filter);
							//continue;
							System.exit(1);
						}

						//System.out.println("filterSession "+filterSession);
					}
					else {
						filterSession = Arrays.stream(gutp.getSessions()).collect(Collectors.toCollection(ArrayList<Session>::new));
					}

				}

				String[] gen = generator.replace(" ","").replace("(", "").replace(")", "").split(",");
				//System.out.println("gen[0] "+gen[0]+" gen[1] "+gen[1] );
				//ArrayList<Session> generatorSession = 
				
		        String regex = "(\\w+)(\\[(\\w+)=\\'(.*)\\'\\])?";
		        Pattern pattern = Pattern.compile(regex);
		        Matcher matcher = pattern.matcher(generator);
		        //ArrayList<Integer> resultGroup = new ArrayList<>();
		        String[] testGen = IntStream.range(1, gen.length).mapToObj(u->gen[u].replace("{", "").replace("}", "")).toArray(String[]::new);
		        ArrayList<Session> filteredSet = filterSession;
		        int[] tyt;
		        if(!(gen[1].equals("{*}") || gen[1].equals("*"))) {
		        	
		        	//tyt = Arrays.stream(gen[1].replace("{", "").replace("}", "").split(",")).mapToInt(Integer::parseInt).toArray();
		        	tyt = convertStringToArray(String.join(",", testGen));//Arrays.stream(testGen).map(u->u).mapToInt(Integer::parseInt).toArray();
		        	//System.out.println("gen1 "+gen[1]+" tyt "+Arrays.toString(tyt)+" gen[2] "+Arrays.toString(testGen));

		        	ArrayList<Integer>resultGroup = new ArrayList<>(Arrays.asList(Arrays.stream(tyt).boxed().toArray(Integer[]::new)));
			        //ArrayList<Session> filteredSet = filterSession.stream().filter(u -> resultGroup.contains(u.getCpt())).collect(Collectors.toCollection(ArrayList<Session>::new));
	            	filteredSet = filterSession.stream()
	            	        .filter(u -> resultGroup.contains(u.getRank()))
	            	        .collect(Collectors.toCollection(ArrayList::new));
	            	if(tyt.length == 1) {
	            		//System.out.println("TMP-PARSER"+filterSession);
	            		int maxRank =  filterSession.stream()
		            	        .mapToInt(u -> u.getRank()).max().getAsInt();
		            	if (tyt[0] > maxRank) {
		            		filteredSet = filterSession.stream()
			            	        .filter(u -> u.getRank() == maxRank)
			            	        .collect(Collectors.toCollection(ArrayList::new));
		            	}else {
		            		filteredSet = filterSession.stream()
			            	        .filter(u -> u.getRank() == tyt[0])
			            	        .collect(Collectors.toCollection(ArrayList::new));
		            	}   

	            	}
	            	/*System.out.println("Filter complementaire "+filteredSet+" tyt "+Arrays.toString(tyt)+" rules "+rulesFinale.get(r).cpt+
	            			" c "+rulesFinale.get(r).constraints.get(0).constraint+" filterSession"+filterSession);*/
		        }
		        else {
		        	tyt = new int[0];
		        }
		        
		        
		        
		        boolean t = true;
		        ArrayList<Integer> courseid = new ArrayList<>();
		        String ressource = "";
		        while (matcher.find() && t) {
		            //System.out.println("matcher2.group(1) "+matcher.group(1));
		            //System.out.println("matcher2.group(2) "+matcher.group(2));
		            //System.out.println("matcher2.group(3) "+matcher.group(3));
		            //System.out.println("matcher2.group(4) "+matcher.group(4));
		            t = false;
		            ressource = matcher.group(1);
		            if(matcher.group(3) != null) {
		            	courseid = gutp.getSessionsRequest(matcher.group(1),matcher.group(3),matcher.group(4));
		            }
		            else {
		            	courseid = gutp.getSessionsRequest(matcher.group(1),"","");
		            }
		            
		        }
		        //System.out.println("courseid "+courseid);
        		ArrayList<String> tabuGroups = new ArrayList<>();

		        ArrayList<ArrayList<Session>> courseSessions = new ArrayList<ArrayList<Session>>();
		        ArrayList<Integer> tmpFilter = filteredSet.stream().mapToInt(uw->uw.getCpt()).boxed().collect(Collectors.toCollection(ArrayList<Integer>::new));
		        ArrayList<Integer> entityChoose = new ArrayList<>();
		        ArrayList<Session> tmpCourses =  new ArrayList<Session>();
		        
		        for(int cl = 0 ; cl < courseid.size() ;cl++) {
		        	if(ressource.equals("courses")) {
		        		int old = tmpCourses.size();
		        		for(int cli = 0; cli < gutp.getCoursesList().get(courseid.get(cl)-1).getSessions().length  ;cli++) {
		        			if(tmpFilter.size() == 0) {
		        				tmpCourses.add(gutp.getCoursesList().get(courseid.get(cl)-1).getSessions()[cli]);
		        			}
		        			else {
			        			if(tmpFilter.contains(gutp.getCoursesList().get(courseid.get(cl)-1).getSessions()[cli].getCpt()) ) {
			        				tmpCourses.add(gutp.getCoursesList().get(courseid.get(cl)-1).getSessions()[cli]);
			        			}
		        			}

		        		}
	
		        		if( tmpCourses.size() > old) {
		        			entityChoose.add(courseid.get(cl));
		        		}
		        		
		        	}
		        	else if(ressource.equals("course")) {
		        		ArrayList<Session> tmp = new ArrayList<Session>();
		        		for(int cli = 0; cli < gutp.getCoursesList().get(courseid.get(cl)-1).getSessions().length  ;cli++) {
		        			if(tmpFilter.size() == 0) {
		        				tmp.add(gutp.getCoursesList().get(courseid.get(cl)-1).getSessions()[cli]);
		        			}
		        			else {
			        			if(tmpFilter.contains(gutp.getCoursesList().get(courseid.get(cl)-1).getSessions()[cli].getCpt()) ) {
			        				tmp.add(gutp.getCoursesList().get(courseid.get(cl)-1).getSessions()[cli]);
			        			}
		        			}

		        		}
		        		if(tmp.size() > 0) {
		        			entityChoose.add(courseid.get(cl));
		        			courseSessions.add(tmp);
		        		}
		        			
		        		
		        	}
		        	else if(ressource.equals("part")) {
		        		ArrayList<Session> tmp = new ArrayList<Session>();
		        		for(int cli = 0; cli < gutp.getPartsList().get(courseid.get(cl)-1).getSessions().length  ;cli++) {
		        			if(tmpFilter.size() == 0) {
		        				tmp.add(gutp.getPartsList().get(courseid.get(cl)-1).getSessions()[cli]);
		        			}
		        			else {
			        			if(tmpFilter.contains(gutp.getPartsList().get(courseid.get(cl)-1).getSessions()[cli].getCpt()) ) {
			        				tmp.add(gutp.getPartsList().get(courseid.get(cl)-1).getSessions()[cli]);
			        			}
		        			}
		        		}
		        		if(tmp.size() > 0) {
		        			entityChoose.add(courseid.get(cl));
		        			courseSessions.add(tmp);
		        		}
		        			
		        	}
		        	else if(ressource.equals("class")) {
		        		ArrayList<Session> tmp = new ArrayList<Session>();
		        		/*if(rulesFinale.get(r).generators.size() >1) {
		        			System.out.println("rulesFinale.get(r).generators "+rulesFinale.get(r).generators);
		        			System.out.println("tmpfilter "+tmpFilter);
		        			System.out.println("filteredSet "+filteredSet);
		        			System.out.println("filterSession "+filterSession);
		        			
		        			
		        		}*/
		        		for(int cli = 0; cli < gutp.getClassesList().get(courseid.get(cl)-1).getSession().length  ;cli++) {
		        			if(tmpFilter.size() == 0) {
		        				tmp.add(gutp.getClassesList().get(courseid.get(cl)-1).getSession()[cli]);
		        			}
		        			else {
			        			if(tmpFilter.contains(gutp.getClassesList().get(courseid.get(cl)-1).getSession()[cli].getCpt()) ) {
			        				tmp.add(gutp.getClassesList().get(courseid.get(cl)-1).getSession()[cli]);
			        			}
		        			}
		        		}
		        		if(tmp.size() > 0) {
		        			entityChoose.add(courseid.get(cl));
		        			courseSessions.add(tmp);
		        		}
		        			
		        	}
		        	else if(ressource.equals("room")) {
		        		ArrayList<Session> tmp = new ArrayList<Session>();
		        		
		        		for(int cli = 0; cli < gutp.getRoomsAL().get(courseid.get(cl)-1).getSessions().size()  ;cli++) {
		        			if(tmpFilter.size() == 0) {
		        				tmp.add(gutp.getRoomsAL().get(courseid.get(cl)-1).getSessions().get(cli));
		        			}
		        			else {
			        			if(tmpFilter.contains(gutp.getRoomsAL().get(courseid.get(cl)-1).getSessions().get(cli).getCpt()) ) {
			        				tmp.add(gutp.getRoomsAL().get(courseid.get(cl)-1).getSessions().get(cli));
			        			}
		        			}
		        		}
		        		if(tmp.size() > 0) {
		        			entityChoose.add(courseid.get(cl));
		        			courseSessions.add(tmp);
		        		}
		        			
		        	}
		        	else if(ressource.equals("lecturer") || ressource.equals("teacher")) {
		        		ArrayList<Session> tmp = new ArrayList<Session>();
		        		for(int cli = 0; cli < gutp.getTeachersAL().get(courseid.get(cl)-1).getSessions().size()  ;cli++) {
		        			if(tmpFilter.size() == 0) {
		        				tmp.add(gutp.getTeachersAL().get(courseid.get(cl)-1).getSessions().get(cli));
		        			}
		        			else {
			        			if(tmpFilter.contains(gutp.getTeachersAL().get(courseid.get(cl)-1).getSessions().get(cli).getCpt()) ) {
			        				tmp.add(gutp.getTeachersAL().get(courseid.get(cl)-1).getSessions().get(cli));
			        			}
		        			}
		        		}
		        		if(tmp.size() > 0) {
		        			entityChoose.add(courseid.get(cl));
		        			courseSessions.add(tmp);
		        		}
		        			
		        	}
		        	else if(ressource.equals("student")) {
		        		ArrayList<Session> tmp = new ArrayList<Session>();
		        		
		        		if(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup()== null) {
		        			for(int cli = 0; cli < gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions().length  ;cli++) {
		        			if(tmpFilter.size() == 0) {
		        				//if(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup()== null) {
		        					//tmp.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli]);
		        				//}
		        				//else if(!tabuGroups.contains(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup().getId())) {
		        					tmp.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli]);
		        					//tabuGroups.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup().getId());
		        				
		        			}
		        			else {
			        			if(tmpFilter.contains(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli].getCpt()) ) {
			        				tmp.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli]);
			        			}
		        			}
		        			}
		        		}
	        			else if(!tabuGroups.contains(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup().getId())){
	        				tabuGroups.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup().getId());
	        				for(int cli = 0; cli < gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions().length  ;cli++) {
			        			if(tmpFilter.size() == 0) {
			        				//if(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup()== null) {
			        					//tmp.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli]);
			        				//}
			        				//else if(!tabuGroups.contains(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup().getId())) {
			        					tmp.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli]);
			        					//tabuGroups.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getGroup().getId());
			        				
			        			}
			        			else {
				        			if(tmpFilter.contains(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli].getCpt()) ) {
				        				tmp.add(gutp.getStudentsAL().get(courseid.get(cl)-1).getSessions()[cli]);
				        			}
			        			}
			        			}
	        			}
	
		        		if(tmp.size() > 0) {
		        			entityChoose.add(courseid.get(cl));
		        			courseSessions.add(tmp);
		        		}
		        			
		        	}
		        }
		        
        		if( tmpCourses.size() > 0) {
        			courseSessions.add(tmpCourses);
        		}
        		
		        if(!cache.containsKey(filter)) {
		        	cache.put(filter, filterSession);
		        }
		       
				//System.out.println("courseSessions "+courseSessions);
				//System.out.println("Filter complementaire "+filteredSet+" tyt "+Arrays.toString(tyt));
				//System.out.println("filtersession "+filterSession);
				//System.out.println("rule r "+rulesFinale.get(r).cpt);
				//System.out.println("rule r "+rulesFinale.get(r).generators);
				//System.out.println("rule r "+rulesFinale.get(r).filters);
				//System.out.println("entityChoose "+entityChoose);
				rulesFinale.get(r).addScopeSession(courseSessions);
				rulesFinale.get(r).addFilterSession(filterSession);
				rulesFinale.get(r).addEntityGenerator(entityChoose);
				rulesFinale.get(r).addTypeEntity(ressource);
				//System.out.println("====================");
				Filter f = new Filter((cptfilter+1),filter);
				f.setSessions(filterSession);
				cptfilter++;
				Generator g = new Generator((cptGenerator+1),generator,ressource,tyt);
				cptGenerator++;
				g.setSessions(courseSessions);
				rulesFinale.get(r).addFiltersObj(f);
				rulesFinale.get(r).addGeneratorsObj(g);
				
				generators.add(g);
				filters.add(f);
				
	            //if(matcher.group(3)!=null && matcher.group(3).equals("parent") ) {
	            //if(rulesFinale.get(r).cpt==14 ) {
	            	//System.out.println("generators "+generator);
		           // System.out.println("filters"+filter);
		            //System.out.println(Arrays.toString(gutp.getClassesList().stream().map(u->u.getId()).toArray()));
		            //System.out.println("matcher2.group(3) "+matcher.group(3));
		            //System.out.println("matcher2.group(4) "+matcher.group(4));
		            //System.out.println("rulesFinale.get(r).cpt "+rulesFinale.get(r).cpt);
		           // System.exit(0);
	            //}
				/*for(int t = 0; t <  ;t++) {
					
				}*/
				//if(rulesFinale.get(r).cpt  ==165) {
					//rulesFinale.get(r)
				//	System.exit(0);
				//}
			}
			rulesFinale.get(r).flatRule();

			/*if(rulesFinale.get(r).constraintUTPs.size()>200) {
            	System.out.println("filters "+rulesFinale.get(r).filters);
            	System.out.println("filters "+rulesFinale.get(r).sessionFilter);
	            System.out.println("generators"+rulesFinale.get(r).generators);
	            System.out.println("generators"+rulesFinale.get(r).generators);
				System.exit(0);
			}*/
		}
		//System.exit(0);
		//System.out.println("cache"+cache);
	}//FinMethod
	
	public ArrayList<Rule> getRules() {
		return this.rulesFinale;
	}//FinMethod

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}//FinMethod

	public ArrayList<Generator> getGenerators() {
		return generators;
	}//FinMethod

	public ArrayList<Filter> getFilters() {
		return filters;
	}//FinMethod

	public ArrayList<ConstraintRule> getConstraintRules() {
		return constraintRuless;
	}//FinMethod
	
	

	
	
}//FinClass
