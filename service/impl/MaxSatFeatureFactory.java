//package cb_ctt.features.service.impl;
//
//import cb_ctt.Formulation;
//import cb_ctt.dto.*;
//import cb_ctt.features.service.FeatureFactory;
//import cb_ctt.features.service.FeatureGroup;
//import org.logicng.cardinalityconstraints.CCConfig;
//import org.logicng.cardinalityconstraints.CCEncoder;
//import org.logicng.cardinalityconstraints.CCIncrementalData;
//import org.logicng.collections.ImmutableFormulaList;
//import org.logicng.collections.LNGVector;
//import org.logicng.configurations.Configuration;
//import org.logicng.datastructures.Assignment;
//import org.logicng.formulas.*;
//import org.logicng.solvers.MaxSATSolver;
//import org.logicng.solvers.maxsat.algorithms.MaxSAT;
//import org.logicng.util.Pair;
//
//
//import java.io.*;
//import java.lang.Math;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.util.*;
//
//public class MaxSatFeatureFactory implements FeatureFactory {
//   final FormulaFactory f = new FormulaFactory();
//   final CType equals = CType.EQ;
//   final CType atLeast = CType.GE;
//   final CType atMost = CType.LE;
//   int variableId = 0;
//   // should be larger than sum of all weights of soft clauses! to do
//   int top = 1000000;
//   private Hashtable<Variable, Integer> literalToInt = new Hashtable<>();
//   private Hashtable<Integer, Variable> intToLiteral = new Hashtable<>();
//
//   @Override
//   public String name() {
//      throw new UnsupportedOperationException("Not yet implemented!"); // TODO: implement me
//   }
//
//   @Override
//   public FeatureGroup calcFeatures(CbCttInstance instance, Formulation formulation) {
//      CCConfig.Builder builder = new CCConfig.Builder();
//      builder = builder.alkEncoding(CCConfig.ALK_ENCODER.CARDINALITY_NETWORK);
//      builder = builder.amkEncoding(CCConfig.AMK_ENCODER.CARDINALITY_NETWORK);
//      builder = builder.exkEncoding(CCConfig.EXK_ENCODER.CARDINALITY_NETWORK);
//      Configuration ccConfig = builder.build();
//      f.putConfiguration(ccConfig);
////      try {
////         MaxSAT.MaxSATResult result = solveSatWithLogicNGFromFile("SAT_formulation_" + "Ing0607-3" + ".txt");
////      } catch (IOException e) {
////         e.printStackTrace();
////      }
//      List<Map<String, Literal>> variables = createVariables(instance);
//      Set<SATClause> slotDayConstraints = generateSlotDayConstraints(instance, variables);
//      Set<SATClause> workingDaysConstraints = generateWorkingDaysConstraints(instance, variables);
//      Set<SATClause> nLecturesConstraints = generateNLecturesConstraints(instance, variables);
//      Set<SATClause> isolatedLecturesConstraints = generateIsolatedLecturesConstraints(instance, variables);
//      Set<SATClause> roomCapacityConstraints = generateRoomCapacityConstraints(instance, variables);
//      Set<SATClause> timeSlotAvailabilityConstraints = generateTimeSlotAvailabilityConstraints(instance, variables);
//      Set<SATClause> roomClashConstraints = generateRoomClashConstraints(instance, variables);
//      Set<SATClause> teacherClashConstraints = generateTeacherClashConstraints(instance, variables);
//      Set<SATClause> curriculumClashConstraints = generateCurriculumClashConstraints(instance, variables);
//      Set<SATClause> courseCurriculumConstraints = generateCourseCurriculumConstraints(instance, variables);
//      Set<SATClause> courseRoomHourAgreement = generateCourseRoomHourAgreement(instance, variables);
//      Set<SATClause> CourseHourRoomAgreement = generateCourseHourRoomAgreement(instance, variables);
//      Set<SATClause> SoftExactlyOneRoomConstraints = generateSoftExactlyOneRoomConstraints(instance, variables);
//      Set<SATClause> allClauses = new HashSet();
//      allClauses.addAll(slotDayConstraints);
//      allClauses.addAll(workingDaysConstraints);
//      allClauses.addAll(nLecturesConstraints);
//      allClauses.addAll(isolatedLecturesConstraints);
//      allClauses.addAll(roomCapacityConstraints);
//      allClauses.addAll(timeSlotAvailabilityConstraints);
//      allClauses.addAll(roomClashConstraints);
//      allClauses.addAll(teacherClashConstraints);
//      allClauses.addAll(curriculumClashConstraints);
//      allClauses.addAll(courseCurriculumConstraints);
//      allClauses.addAll(courseRoomHourAgreement);
//      allClauses.addAll(SoftExactlyOneRoomConstraints);
//      allClauses.addAll(CourseHourRoomAgreement);
////      MaxSAT.MaxSATResult result = solveSatWithLogicNG(allClauses);
//      String outputLocation = "";
//      try {
//         outputLocation = writeWDImacsFile(instance, allClauses);
//      } catch (IOException e) {
//         e.printStackTrace();
//      }
//      return null;
//   }
//
//   private MaxSAT.MaxSATResult solveSatWithLogicNG(Set<SATClause> clauses) {
//      for (SATClause clause : clauses) {
//         for (Literal literal : clause.getLiterals()) {
//            if (literal == null) {
//               boolean stop = true;
//            }
//            if (!literalToInt.containsKey(literal.variable())) {
//               int id = getVariableId();
//               literalToInt.put(literal.variable(), id);
//               intToLiteral.put(id, literal.variable());
//            }
//         }
//      }
//      final int MAXVAR = literalToInt.size() * 2;
//      final int NBCLAUSES = clauses.size();
//      MaxSATSolver solver = MaxSATSolver.wbo();
//      long start = System.currentTimeMillis();
//      int softTotal = 0;
//      for (SATClause clause : clauses) {
////			solver.addHardFormula(clauseToFormula(clause));
//         if (clause.isHard()) {
//            solver.addHardFormula(clauseToFormula(clause));
//         } else {
//            solver.addSoftFormula(clauseToFormula(clause), clause.getWeight());
//            softTotal =  softTotal + clause.getWeight();
//         }
//      }
//      System.out.println("total: " + Integer.toString(softTotal));
//      long end = System.currentTimeMillis();
//      System.out.println(end - start);
//      MaxSAT.MaxSATResult solution = solver.solve();
//      Assignment model = solver.model();
//      int result = solver.result();
//      System.out.println("result");
//      System.out.println(result);
//      System.out.println(solution);
//      return solution;
//   }
//
//
//   private Formula clauseToFormula(SATClause clause) {
//      return f.clause(clause.getLiterals());
//   }
//
//   private void checkForNull(Set<SATClause> clauses) {
//      for (SATClause cl : clauses) {
//         for (Literal lit : cl.getLiterals()) {
//            if (lit == null) {
//               boolean stop = true;
//            }
//         }
//      }
//   }
//
//   private String writeWDImacsFile(CbCttInstance instance, Set<SATClause> clauses) throws IOException {
//      for (SATClause clause : clauses) {
//         for (Literal literal : clause.getLiterals()) {
//            if (!literalToInt.containsKey(literal.variable())) {
//               int id = getVariableId();
//               literalToInt.put(literal.variable(), id);
//               intToLiteral.put(id, literal.variable());
//            }
//         }
//      }
//      final int MAXVAR = literalToInt.size();
//      final int NBCLAUSES = clauses.size();
//
//      FileWriter f0 = new FileWriter("SAT_formulation_" + instance.getId() + ".wcnf");
//      String newLine = System.getProperty("line.separator");
//      f0.write("p " + "wcnf " + Integer.toString(MAXVAR) + " " + Integer.toString(NBCLAUSES) + " " + Integer.toString(top));
//      for (SATClause clause : clauses) {
//         f0.write("\n");
//         int weight = clause.getWeight();
//         f0.write(Integer.toString(weight) + " ");
//         for (Literal lit : clause.getLiterals()) {
//            if (lit.phase() == false) {
//               f0.write(Integer.toString(-literalToInt.get(lit.variable())) + " ");
//            } else {
//               f0.write(Integer.toString(literalToInt.get(lit.variable())) + " ");
//            }
//         }
//         f0.write(Integer.toString(0) + " ");
//      }
//      f0.close();
//      return "SAT_formulation_" + instance.getId() + ".wcnf";
//   }
//
//   private int getVariableId() {
//      this.variableId = this.variableId + 1;
//      return variableId;
//   }
//
//   //relation between cr and chr
//   private Set<SATClause> generateCourseRoomHourAgreement(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      Map<String, Literal> courseRoomSlotVariables = variables.get(4);
//
//      Set<SATClause> courseHourRoomConstraints = new HashSet<>();
//
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      for (Course course : instance.getCourses()) {
//         for (Room room : instance.getRooms()) {
//            Set<Literal> clauseVars = new HashSet<>();
//            Set<Literal> clauseVars2 = new HashSet<>();
//            String var2_string = course.getId() + room.getId() + Boolean.toString(false);
//            Literal var2 = courseRoomVariables.get(var2_string);
//            clauseVars2.add(var2);
//            String var5_string = course.getId() + room.getId() + Boolean.toString(true);
//            Literal var5 = courseRoomVariables.get(var5_string);
//            clauseVars.add(var5);
//            for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//               String var1_string = course.getId() + room.getId() + Integer.toString(slot) + Boolean.toString(true);
//               Literal var1 = courseRoomSlotVariables.get(var1_string);
//               String var4_string = course.getId() + room.getId() + Integer.toString(slot) + Boolean.toString(false);
//               Literal var4 = courseRoomSlotVariables.get(var4_string);
//               clauseVars.add(var4);
//               clauseVars2.add(var1);
//               SATClause cl = new SATClause(clauseVars2, top, true);
//               courseHourRoomConstraints.add(cl);
//            }
//            SATClause cl = new SATClause(clauseVars, top, true);
//            courseHourRoomConstraints.add(cl);
//         }
//      }
//      return courseHourRoomConstraints;
//   }
//
//   //Relation between ch and chr:
//   private Set<SATClause> generateCourseHourRoomAgreement(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      Map<String, Literal> courseRoomSlotVariables = variables.get(4);
//
//      Set<SATClause> courseHourRoomConstraints = new HashSet<>();
//
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      for (Course course : instance.getCourses()) {
//         for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//            Set<Literal> clauseVars = new HashSet<>();
//            String var3_string = course.getId() + Integer.toString(slot) + Boolean.toString(true);
//            Literal var3 = courseSlotVariables.get(var3_string);
//            clauseVars.add(var3);
//            for (Room room : instance.getRooms()) {
//               Set<Literal> clauseVars2 = new HashSet<>();
//               String var1_string = course.getId() + room.getId() + Integer.toString(slot) + Boolean.toString(true);
//               Literal var1 = courseRoomSlotVariables.get(var1_string);
//               String var2_string = course.getId() + Integer.toString(slot) + Boolean.toString(false);
//               Literal var2 = courseSlotVariables.get(var2_string);
//               String var4_string = course.getId() + room.getId() + Integer.toString(slot) + Boolean.toString(false);
//               Literal var4 = courseRoomSlotVariables.get(var4_string);
//               clauseVars2.add(var1);
//               clauseVars2.add(var2);
//               clauseVars.add(var4);
//               SATClause cl = new SATClause(clauseVars2, top, true);
//               courseHourRoomConstraints.add(cl);
//            }
//            SATClause cl = new SATClause(clauseVars, top, true);
//            courseHourRoomConstraints.add(cl);
//         }
//      }
//      return courseHourRoomConstraints;
//   }
//
//
//   private Set<SATClause> generateSoftExactlyOneRoomConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//
//      Set<SATClause> exactlyOneRoomConstraints = new HashSet<>();
//      for (Course course : instance.getCourses()) {
//         Set<Variable> constraintVariables = new HashSet<>();
//         for (Room room : instance.getRooms()) {
//            String var1_string = course.getId() + room.getId() + Boolean.toString(false);
//            Literal var1 = courseRoomVariables.get(var1_string);
//            constraintVariables.add(var1.variable());
//         }
//         //If a course has only one lecture, we don't have to worry about the constraint of at most one room
//         //If there is only one room we don't have to worry either
//         if (course.getLectures() == 1 || constraintVariables.size() == 1) {
//            continue;
//         }
//         final CCEncoder encoder = new CCEncoder(f);
//         final PBConstraint constraint = f.cc(atMost, Math.min(course.getLectures(), constraintVariables.size() - 1), constraintVariables);
//         //incremental encoding not possible
//         if (constraintVariables.size() == 2) {
//            ImmutableFormulaList cnf = encoder.encode(constraint);
//            for (int i = 0; i < (cnf.size() - 1); i++) {
//               Formula clause = cnf.get(i);
//               Set<Literal> literals = clause.literals();
//               SATClause cl = new SATClause(literals, 1, false);
//               exactlyOneRoomConstraints.add(cl);
//            }
//         } else {
//            final Pair<ImmutableFormulaList, CCIncrementalData> cnf = encoder.encodeIncremental(constraint);
//            List<Formula> result = cnf.second().newUpperBound(1);
//            Literal literal = new ArrayList<>(result.get(0).literals()).get(0);
//            //Last one we add later as a soft constraint
//            for (int i = 0; i < (cnf.first().size() - 1); i++) {
//               Formula clause = cnf.first().get(i);
//               Set<Literal> literals = clause.literals();
//               SATClause cl = new SATClause(literals, top, true);
//               exactlyOneRoomConstraints.add(cl);
//            }
//            try {
//               LNGVector<Literal> output = getFieldValue(cnf.second(), "vector1");
//               boolean to_right = false;
//               for (Literal lit : output) {
//                  if (lit.variable().equals(literal.variable())) {
//                     to_right = true;
//                  }
//                  if (to_right) {
//                     Set<Literal> out_lit = new HashSet<>();
//                     out_lit.add(lit.negate());
//                     SATClause cl_out = new SATClause(out_lit, 1, false);
//                     exactlyOneRoomConstraints.add(cl_out);
//                  }
//               }
//            } catch (NoSuchFieldException e) {
//               e.printStackTrace();
//            } catch (IllegalAccessException e) {
//               e.printStackTrace();
//            }
//         }
//      }
//      return exactlyOneRoomConstraints;
//   }
////SUPERSEDED by soft constraint
////	private Set<SATClause> generateExactlyOneRoomConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
////		Map<String, Literal> courseDayVariables = variables.get(0);
////		Map<String, Literal> courseSlotVariables = variables.get(1);
////		Map<String, Literal> courseRoomVariables = variables.get(2);
////		Map<String, Literal> curriculumSlotVariables = variables.get(3);
////
////		Set<SATClause> exactlyOneRoomConstraints = new HashSet<>();
////		for (Course course : instance.getCourses()) {
////			Set<Variable> constraintVariables = new HashSet<>();
////			for (Room room : instance.getRooms()) {
////				String var1_string = course.getId() + room.getId() + Boolean.toString(false);
////				Literal var1 = courseRoomVariables.get(var1_string);
////				constraintVariables.add(var1.variable());
////			}
////			final Formula formula = f.cc(equals, 1, constraintVariables);
////			final Formula cnf = formula.cnf();
////			for (Formula clause : cnf) {
////				Set<Literal> literals = clause.literals();
////				SATClause cl = new SATClause(literals, top, true);
////				exactlyOneRoomConstraints.add(cl);
////			}
////		}
////		return exactlyOneRoomConstraints;
////	}
//
//   public LNGVector<Literal> getFieldValue(CCIncrementalData data, String fieldName) throws NoSuchFieldException,
//           SecurityException, IllegalArgumentException, IllegalAccessException {
//
//      Field field = data.getClass().getDeclaredField(fieldName);
//      if (Modifier.isPrivate(field.getModifiers())) {
//         field.setAccessible(true);
//         return (LNGVector<Literal>) field.get(data);
//      }
//      return null;
//   }
//
//   //at_least(workingDays(c),{cdc,d1 , cdc,d2 ,..., cdc,d5 }) SUPERSEDED by soft constraint
////	private Set<SATClause> generateExactlyOneRoomConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
////		Map<String, Literal> courseDayVariables = variables.get(0);
////		Map<String, Literal> courseSlotVariables = variables.get(1);
////		Map<String, Literal> courseRoomVariables = variables.get(2);
////		Map<String, Literal> curriculumSlotVariables = variables.get(3);
////
////		Set<SATClause> exactlyOneRoomConstraints = new HashSet<>();
////		for (Course course : instance.getCourses()) {
////			Set<Variable> constraintVariables = new HashSet<>();
////			for (Room room : instance.getRooms()) {
////				String var1_string = course.getId() + room.getId() + Boolean.toString(false);
////				Literal var1 = courseRoomVariables.get(var1_string);
////				constraintVariables.add(var1.variable());
////			}
////			final Formula formula = f.cc(equals, 1, constraintVariables);
////			final Formula cnf = formula.cnf();
////			for (Formula clause : cnf) {
////				Set<Literal> literals = clause.literals();
////				SATClause cl = new SATClause(literals, top, true);
////				exactlyOneRoomConstraints.add(cl);
////			}
////		}
////		return exactlyOneRoomConstraints;
////	}
////
////	public LNGVector<Literal> getFieldValue(CCIncrementalData data, String fieldName) throws NoSuchFieldException,
////			SecurityException, IllegalArgumentException, IllegalAccessException {
////
////		Field field = data.getClass().getDeclaredField(fieldName);
////		if (Modifier.isPrivate(field.getModifiers())) {
////			field.setAccessible(true);
////			return (LNGVector<Literal>) field.get(data);
////		}
////		return null;
////	}
//
//   //at_least(workingDays(c),{cdc,d1 , cdc,d2 ,..., cdc,d5 })
//   private Set<SATClause> generateWorkingDaysConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//
//      Set<SATClause> WorkingDaysConstraints = new HashSet<>();
//      for (Course course : instance.getCourses()) {
//         int minDays = course.getMinDays();
//         int weekDays = instance.getDays();
//         Set<Variable> constraintVariables = new HashSet<>();
//         for (int day = 0; day < weekDays; day++) {
//            String var1_string = course.getId() + Integer.toString(day) + Boolean.toString(true);
//            Literal var1 = courseDayVariables.get(var1_string);
//            //Some tricks because cc only accepts positive literals...
//            Literal tempVar = f.literal("negated_" + var1_string, true);
//            //if var1 is true, tempvar is true
//            Set<Literal> trickClause = new HashSet<>();
//            trickClause.add(tempVar);
//            trickClause.add(var1.negate());
//            SATClause cl = new SATClause(trickClause, top, true);
//            WorkingDaysConstraints.add(cl);
//            //if tempvar is true, var 1 is true
//            trickClause = new HashSet<>();
//            trickClause.add(tempVar.negate());
//            trickClause.add(var1);
//            cl = new SATClause(trickClause, top, true);
//            WorkingDaysConstraints.add(cl);
//            constraintVariables.add(tempVar.variable());
//         }
//         final CCEncoder encoder = new CCEncoder(f);
//         final PBConstraint constraint = f.cc(atMost, instance.getDays() - 1, constraintVariables);
//         final Pair<ImmutableFormulaList, CCIncrementalData> cnf = encoder.encodeIncremental(constraint);
//         //We get the actual cut-off literal, above was only to get all literals of the output of the cardinality
//         //network
//         Literal literal = null;
//         List<Formula> result = null;
//         if (minDays == 1) {
//            LNGVector<Literal> output = null;
//            try {
//               output = getFieldValue(cnf.second(), "vector1");
//            } catch (NoSuchFieldException e) {
//               e.printStackTrace();
//            } catch (IllegalAccessException e) {
//               e.printStackTrace();
//            }
//            literal = output.get(output.size() - 1);
//         } else {
//            result = cnf.second().newUpperBound(instance.getDays() - minDays);
//            literal = new ArrayList<>(result.get(0).literals()).get(0);
//         }
//         //Last one we add later as a soft constraint
//         for (int i = 0; i < (cnf.first().size() - 1); i++) {
//            Formula clause = cnf.first().get(i);
//            Set<Literal> literals = clause.literals();
//            SATClause cl = new SATClause(literals, top, true);
//            WorkingDaysConstraints.add(cl);
//         }
//         try {
//            LNGVector<Literal> output = getFieldValue(cnf.second(), "vector1");
//            boolean to_right = false;
//            for (Literal lit : output) {
//               if (lit.variable().equals(literal.variable())) {
//                  to_right = true;
//               }
//               if (to_right) {
//                  Set<Literal> out_lit = new HashSet<>();
//                  out_lit.add(lit.negate());
//                  SATClause cl_out = new SATClause(out_lit, 5, false);
//                  WorkingDaysConstraints.add(cl_out);
//               }
//            }
//         } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//         } catch (IllegalAccessException e) {
//            e.printStackTrace();
//         }
////           for(Literal outputLit : cnf.second().)
//      }
//      return WorkingDaysConstraints;
//   }
//
//   //exactly(numLectures(c),{chc,h1 , chc,h2 ,..., chc,hn })
//   private Set<SATClause> generateNLecturesConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//
//      Set<SATClause> nLecturesConstraints = new HashSet<>();
//      for (Course course : instance.getCourses()) {
//         int nLectures = course.getLectures();
//         Set<Variable> constraintVariables = new HashSet<>();
//         for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//            String var1_string = course.getId() + Integer.toString(slot) + Boolean.toString(false);
//            Literal var1 = courseSlotVariables.get(var1_string);
//            constraintVariables.add(var1.variable());
//         }
//         final Formula formula = f.cc(equals, nLectures, constraintVariables);
//         final Formula cnf = formula.cnf();
//         for (Formula clause : cnf) {
//            Set<Literal> literals = clause.literals();
//            SATClause cl = new SATClause(literals, top, true);
//            nLecturesConstraints.add(cl);
//         }
//      }
//      return nLecturesConstraints;
//   }
//
//   private Set<SATClause> generateSlotDayConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      int slotCount = 0;
//      HashMap<Integer, Integer> slotToDay = new HashMap<>();
//      HashMap<Integer, Set<Integer>> dayToSlots = new HashMap<>();
//      for (int i = 0; i < nrOfDays; i++) {
//         Set<Integer> slotsOfDay = new HashSet<>();
//         for (int j = 0; j < nrOfPeriods; j++) {
//            slotToDay.put(slotCount, i);
//            slotsOfDay.add(slotCount);
//            slotCount++;
//         }
//         dayToSlots.put(i, slotsOfDay);
//      }
//      Set<SATClause> slotDayConstraints = new HashSet<>();
//      for (Course course : instance.getCourses()) {
//         for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//            Set<Literal> clauseVars = new HashSet<>();
//            int day = slotToDay.get(slot);
//            String var1_string = course.getId() + Integer.toString(slot) + Boolean.toString(true);
//            Literal var1 = courseSlotVariables.get(var1_string);
//            String var2_string = course.getId() + Integer.toString(day) + Boolean.toString(false);
//            Literal var2 = courseDayVariables.get(var2_string);
//            clauseVars.add(var1);
//            clauseVars.add(var2);
////            Formula clause = f.clause(clauseVars);
//            SATClause cl = new SATClause(clauseVars, top, true);
//            slotDayConstraints.add(cl);
//         }
//         for (int day = 0; day < nrOfDays; day++) {
//            Set<Literal> clauseVars = new HashSet<>();
//            String var1_string = course.getId() + Integer.toString(day) + Boolean.toString(true);
//            Literal var1 = courseDayVariables.get(var1_string);
//            clauseVars.add(var1);
//            for (Integer slot : dayToSlots.get(day)) {
//               String var2_string = course.getId() + Integer.toString(slot) + Boolean.toString(false);
//               Literal var2 = courseSlotVariables.get(var2_string);
//               clauseVars.add(var2);
//            }
////            Formula clause = f.clause(clauseVars);
//            SATClause cl = new SATClause(clauseVars, top, true);
//            slotDayConstraints.add(cl);
//         }
//      }
//      return slotDayConstraints;
//   }
//
//   private Set<SATClause> generateIsolatedLecturesConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//
//      Set<SATClause> IsolatedLectureConstraints = new HashSet<>();
//
//      Set<Integer> lastSlotsOfDay = new HashSet<>();
//      Set<Integer> firstSlotsOfDay = new HashSet<>();
//      Set<Integer> betweenSlotsOfDay = new HashSet<>();
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      int slotCount = 0;
//      for (int i = 0; i < nrOfDays; i++) {
//         for (int j = 0; j < nrOfPeriods; j++) {
//            if (j == 0) {
//               firstSlotsOfDay.add(slotCount);
//            } else if (j == (nrOfPeriods - 1)) {
//               lastSlotsOfDay.add(slotCount);
//            } else {
//               betweenSlotsOfDay.add(slotCount);
//            }
//            slotCount++;
//         }
//      }
//      for (Curriculum cur : instance.getCurricula()) {
//         for (Integer firstSlot : firstSlotsOfDay) {
//            Set<Literal> clauseVars = new HashSet<>();
//            String var1_string = cur.getId() + Integer.toString(firstSlot) + Boolean.toString(true);
//            Literal var1 = curriculumSlotVariables.get(var1_string);
//
//            String var2_string = cur.getId() + Integer.toString(firstSlot + 1) + Boolean.toString(false);
//            Literal var2 = curriculumSlotVariables.get(var2_string);
//
//            clauseVars.add(var1);
//            clauseVars.add(var2);
//
////            Formula clause = f.clause(clauseVars);
//            SATClause cl = new SATClause(clauseVars, 2, false);
//            IsolatedLectureConstraints.add(cl);
//         }
//         for (Integer lastSlot : lastSlotsOfDay) {
//            Set<Literal> clauseVars = new HashSet<>();
//            String var1_string = cur.getId() + Integer.toString(lastSlot) + Boolean.toString(true);
//            Literal var1 = curriculumSlotVariables.get(var1_string);
//
//            String var2_string = cur.getId() + Integer.toString(lastSlot - 1) + Boolean.toString(false);
//            Literal var2 = curriculumSlotVariables.get(var2_string);
//
//            clauseVars.add(var1);
//            clauseVars.add(var2);
//
////            Formula clause = f.clause(clauseVars);
//            SATClause cl = new SATClause(clauseVars, 2, false);
//            IsolatedLectureConstraints.add(cl);
//         }
//
//         for (Integer firstSlot : betweenSlotsOfDay) {
//            Set<Literal> clauseVars = new HashSet<>();
//            String var1_string = cur.getId() + Integer.toString(firstSlot) + Boolean.toString(true);
//            Literal var1 = curriculumSlotVariables.get(var1_string);
//
//            String var2_string = cur.getId() + Integer.toString(firstSlot + 1) + Boolean.toString(false);
//            Literal var2 = curriculumSlotVariables.get(var2_string);
//
//            String var3_string = cur.getId() + Integer.toString(firstSlot - 1) + Boolean.toString(false);
//            Literal var3 = curriculumSlotVariables.get(var3_string);
//
//            clauseVars.add(var1);
//            clauseVars.add(var2);
//            clauseVars.add(var3);
//
//            //Formula clause = f.clause(clauseVars);
//            SATClause cl = new SATClause(clauseVars, 2, false);
//            IsolatedLectureConstraints.add(cl);
//         }
//      }
//      return IsolatedLectureConstraints;
//   }
//
//   //	Room Capacity: The encoding of the "Room Capacity" constraint is straightforward: for
////	each course c and room r, if numStudents(c) > capacity(r), for each hour h, we add one
////	soft one-literal clause chrc,h,r with cost numStudents(c) - capacity(r).
//   private Set<SATClause> generateRoomCapacityConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      Map<String, Literal> courseRoomSlotVariables = variables.get(4);
//
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      Set<SATClause> roomCapacityConstraints = new HashSet<>();
//      for (Course course : instance.getCourses()) {
//         for (Room room : instance.getRooms()) {
//            for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//               if (room.getCapacity() < course.getStudents()) {
//                  Set<Literal> clauseVars = new HashSet<>();
//                  String var1_string = course.getId() + room.getId() + Integer.toString(slot) + Boolean.toString(true);
//                  Literal var1 = courseRoomSlotVariables.get(var1_string);
//                  clauseVars.add(var1);
//
//                  SATClause cl = new SATClause(clauseVars, course.getStudents() - room.getCapacity(), false);
//                  roomCapacityConstraints.add(cl);
//               }
//            }
//         }
//      }
//      return roomCapacityConstraints;
//   }
//// SUPERSEDED by soft constraint
////	private Set<SATClause> generateRoomCapacityConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
////		Map<String, Literal> courseDayVariables = variables.get(0);
////		Map<String, Literal> courseSlotVariables = variables.get(1);
////		Map<String, Literal> courseRoomVariables = variables.get(2);
////		Map<String, Literal> curriculumSlotVariables = variables.get(3);
////
////		Set<SATClause> roomCapacityConstraints = new HashSet<>();
////		for (Course course : instance.getCourses()) {
////			for (Room room : instance.getRooms()) {
////				if (room.getCapacity() < course.getStudents()) {
////					Set<Literal> clauseVars = new HashSet<>();
////					String var1_string = course.getId() + room.getId() + Boolean.toString(true);
////					Literal var1 = courseRoomVariables.get(var1_string);
////
////					clauseVars.add(var1);
////
//////               Formula clause = f.clause(clauseVars);
////					SATClause cl = new SATClause(clauseVars, top, true);
////					roomCapacityConstraints.add(cl);
////				}
////			}
////		}
////		return roomCapacityConstraints;
////	}
//
//   private Set<SATClause> generateTimeSlotAvailabilityConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      int slotCount = 0;
//
//      HashMap<Integer, Integer> slotToDay = new HashMap<>();
//      HashMap<Integer, Set<Integer>> dayToSlots = new HashMap<>();
//      for (int i = 0; i < nrOfDays; i++) {
//         Set<Integer> slotsOfDay = new HashSet<>();
//         for (int j = 0; j < nrOfPeriods; j++) {
//            slotToDay.put(slotCount, i);
//            slotsOfDay.add(slotCount);
//            slotCount++;
//         }
//         dayToSlots.put(i, slotsOfDay);
//      }
//
//      Set<SATClause> timeSlotAvailabilityConstraints = new HashSet<>();
//      for (Course course : instance.getCourses()) {
//         for (Period forbiddenPeriod : course.getUnavailable()) {
//            int day_slot = forbiddenPeriod.getSlot();
//            int correct_slot = forbiddenPeriod.getDay() * nrOfPeriods + day_slot;
//            Set<Literal> clauseVars = new HashSet<>();
//            String var1_string = course.getId() + Integer.toString(correct_slot) + Boolean.toString(true);
//            Literal var1 = courseSlotVariables.get(var1_string);
//            clauseVars.add(var1);
//
////            Formula clause = f.clause(clauseVars);
//            SATClause cl = new SATClause(clauseVars, top, true);
//            timeSlotAvailabilityConstraints.add(cl);
//         }
//      }
//      return timeSlotAvailabilityConstraints;
//   }
//
//   void GeneratePermutations(List<List<String>> Lists, List<String> result, int depth, String current) {
//      if (depth == Lists.size()) {
//         result.add(current);
//         return;
//      }
//
//      for (int i = 0; i < Lists.get(depth).size(); ++i) {
//         GeneratePermutations(Lists, result, depth + 1, current + Lists.get(depth).get(i) + ",");
//      }
//   }
//
//   private Set<SATClause> generateRoomClashConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      Map<String, Literal> courseRoomSlotVariables = variables.get(4);
//
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      Set<SATClause> roomClashConstraints = new HashSet<>();
//      List<String> result = new ArrayList<>();
//      List<Course> courses = new ArrayList(instance.getCourses());
//      for (int i = 0; i < courses.size(); i++) {
//         for (int j = i + 1; j < courses.size(); j++) {
//            Course course1 = courses.get(i);
//            Course course2 = courses.get(j);
//            for (Room room : instance.getRooms()) {
//               for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//                  Set<Literal> clauseVars = new HashSet<>();
//                  String var1_string = course1.getId() + room.getId() + Integer.toString(slot) + Boolean.toString(true);
//                  Literal var1 = courseRoomSlotVariables.get(var1_string);
//                  String var2_string = course2.getId() + room.getId() + Integer.toString(slot) + Boolean.toString(true);
//                  Literal var2 = courseRoomSlotVariables.get(var2_string);
//
//                  clauseVars.add(var1);
//                  clauseVars.add(var2);
//                  SATClause cl = new SATClause(clauseVars, top, true);
//                  roomClashConstraints.add(cl);
//               }
//            }
//         }
//      }
//      return roomClashConstraints;
//   }
//
////	Superseded by new soft constraints + new variables
////	private Set<SATClause> generateRoomClashConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
////		Map<String, Literal> courseDayVariables = variables.get(0);
////		Map<String, Literal> courseSlotVariables = variables.get(1);
////		Map<String, Literal> courseRoomVariables = variables.get(2);
////		Map<String, Literal> curriculumSlotVariables = variables.get(3);
////		int nrOfDays = instance.getDays();
////		int nrOfPeriods = instance.getPeriodsPerDay();
////		Set<SATClause> roomClashConstraints = new HashSet<>();
////		List<String> result = new ArrayList<>();
////		List<Course> courses = new ArrayList(instance.getCourses());
////		for (int i = 0; i < courses.size(); i++) {
////			for (int j = i + 1; j < courses.size(); j++) {
////				Course course1 = courses.get(i);
////				Course course2 = courses.get(j);
////				for (Room room : instance.getRooms()) {
////					String var3_string = course1.getId() + room.getId() + Boolean.toString(true);
////					Literal var3 = courseRoomVariables.get(var3_string);
////					String var4_string = course2.getId() + room.getId() + Boolean.toString(true);
////					Literal var4 = courseRoomVariables.get(var4_string);
////					for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
////						Set<Literal> clauseVars = new HashSet<>();
////						String var1_string = course1.getId() + Integer.toString(slot) + Boolean.toString(true);
////						Literal var1 = courseSlotVariables.get(var1_string);
////						String var2_string = course2.getId() + Integer.toString(slot) + Boolean.toString(true);
////						Literal var2 = courseSlotVariables.get(var2_string);
////
////						clauseVars.add(var1);
////						clauseVars.add(var2);
////						clauseVars.add(var3);
////						clauseVars.add(var4);
////						Formula clause = f.clause(clauseVars);
////						SATClause cl = new SATClause(clauseVars, top, true);
////						roomClashConstraints.add(cl);
////					}
////				}
////			}
////		}
////		return roomClashConstraints;
////	}
//
//   private Set<SATClause> generateTeacherClashConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      Set<SATClause> teacherClashConstraints = new HashSet<>();
//      for (Course course1 : instance.getCourses()) {
//         for (Course course2 : instance.getCourses()) {
//            if (course1 != course2) {
//               if (course1.getTeacher().equals(course2.getTeacher())) {
//                  for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//                     Set<Literal> clauseVars = new HashSet<>();
//                     String var1_string = course1.getId() + Integer.toString(slot) + Boolean.toString(true);
//                     Literal var1 = courseSlotVariables.get(var1_string);
//                     String var2_string = course2.getId() + Integer.toString(slot) + Boolean.toString(true);
//                     Literal var2 = courseSlotVariables.get(var2_string);
//                     clauseVars.add(var1);
//                     clauseVars.add(var2);
//                     Formula clause = f.clause(clauseVars);
//                     SATClause cl = new SATClause(clauseVars, top, true);
//                     teacherClashConstraints.add(cl);
//                  }
//               }
//            }
//         }
//      }
//      return teacherClashConstraints;
//   }
//
//   private Set<SATClause> generateCurriculumClashConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      Set<SATClause> curriculumClashConstraints = new HashSet<>();
//      for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//         for (Curriculum cur : instance.getCurricula()) {
//            Set<Course> courses = cur.getCourses();
//            for (Course course1 : courses) {
//               for (Course course2 : courses) {
//                  if (course1 != course2) {
//                     Set<Literal> clauseVars = new HashSet<>();
//                     String var1_string = course1.getId() + Integer.toString(slot) + Boolean.toString(true);
//                     Literal var1 = courseSlotVariables.get(var1_string);
//                     String var2_string = course2.getId() + Integer.toString(slot) + Boolean.toString(true);
//                     Literal var2 = courseSlotVariables.get(var2_string);
//                     clauseVars.add(var1);
//                     clauseVars.add(var2);
//                     Formula clause = f.clause(clauseVars);
//                     SATClause cl = new SATClause(clauseVars, top, true);
//                     curriculumClashConstraints.add(cl);
//                  }
//               }
//            }
//         }
//      }
//      return curriculumClashConstraints;
//   }
//
//   //Relation between ch and kh
//   private Set<SATClause> generateCourseCurriculumConstraints(CbCttInstance instance, List<Map<String, Literal>> variables) {
//      Map<String, Literal> courseDayVariables = variables.get(0);
//      Map<String, Literal> courseSlotVariables = variables.get(1);
//      Map<String, Literal> courseRoomVariables = variables.get(2);
//      Map<String, Literal> curriculumSlotVariables = variables.get(3);
//      int nrOfDays = instance.getDays();
//      int nrOfPeriods = instance.getPeriodsPerDay();
//      HashMap<Course, Set<Curriculum>> courseToCurricula = new HashMap<>();
//      for (Curriculum cur : instance.getCurricula()) {
//         Set<Course> courses = cur.getCourses();
//         for (Course course : courses) {
//            if (courseToCurricula.containsKey(course)) {
//               courseToCurricula.get(course).add(cur);
//            } else {
//               courseToCurricula.put(course, new HashSet<>());
//               courseToCurricula.get(course).add(cur);
//            }
//         }
//      }
//      Set<SATClause> courseCurriculumConstraints = new HashSet<>();
//      for (Course course : instance.getCourses()) {
//         for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//            //Course that is not part of curriculum
//            if (courseToCurricula.get(course) == null) {
//               continue;
//            }
//            for (Curriculum cur : courseToCurricula.get(course)) {
//               Set<Literal> clauseVars = new HashSet<>();
//               String var1_string = course.getId() + Integer.toString(slot) + Boolean.toString(true);
//               Literal var1 = courseSlotVariables.get(var1_string);
//               String var2_string = cur.getId() + Integer.toString(slot) + Boolean.toString(false);
//               Literal var2 = curriculumSlotVariables.get(var2_string);
//               clauseVars.add(var1);
//               clauseVars.add(var2);
////               Formula clause = f.clause(clauseVars);
//               SATClause cl = new SATClause(clauseVars, top, true);
//               courseCurriculumConstraints.add(cl);
//            }
//         }
//      }
//      for (int slot = 0; slot < nrOfDays * nrOfPeriods; slot++) {
//         for (Curriculum cur : instance.getCurricula()) {
//            Set<Literal> clauseVars = new HashSet<>();
//            String var1_string = cur.getId() + Integer.toString(slot) + Boolean.toString(true);
//            Literal var1 = curriculumSlotVariables.get(var1_string);
//            clauseVars.add(var1);
//            for (Course course : cur.getCourses()) {
//               String var2_string = course.getId() + Integer.toString(slot) + Boolean.toString(false);
//               Literal var2 = courseSlotVariables.get(var2_string);
//               clauseVars.add(var2);
//            }
////            Formula clause = f.clause(clauseVars);
//            SATClause cl = new SATClause(clauseVars, top, true);
//            courseCurriculumConstraints.add(cl);
//         }
//      }
//      return courseCurriculumConstraints;
//   }
//
//   private List<Map<String, Literal>> createVariables(CbCttInstance instance) {
//      Set<Curriculum> allCurricula = instance.getCurricula();
//      List<Map<String, Literal>> varMapList = new ArrayList<Map<String, Literal>>();
//      Map<String, Literal> courseDayVariables = new HashMap<>();
//      Map<String, Literal> courseSlotVariables = new HashMap<>();
//      Map<String, Literal> courseRoomVariables = new HashMap<>();
//      Map<String, Literal> curriculumSlotVariables = new HashMap<>();
//      Map<String, Literal> courseRoomSlotVariables = new HashMap<>();
//
//      int nPeriodsPerDay = instance.getPeriodsPerDay();
//      int nDays = instance.getDays();
//      Set<Room> rooms = instance.getRooms();
//      String courseDayType = "course_day";
//      String courseSlotType = "course_slot";
//      String courseRoomType = "course_room";
//      String curriculumSlotType = "curriculum_slot";
//      String courseRoomSlotType = "course_room_slot";
//
//      for (Course course : instance.getCourses()) {
//         String courseId = course.getId();
//         //Creating day variables
//         for (int i = 0; i < nDays; i++) {
//            int day = i;
//            final Literal dayVariable = f.literal(courseDayType + course.getId() + Integer.toString(day) + Boolean.toString(false), true);
//            final Literal dayNegatedVariable = dayVariable.negate();
//            courseDayVariables.put(course.getId() + Integer.toString(day) + Boolean.toString(false), dayVariable);
//            courseDayVariables.put(course.getId() + Integer.toString(day) + Boolean.toString(true), dayNegatedVariable);
//         }
//         //Creating slot variables
//         for (int i = 0; i < (nPeriodsPerDay * nDays); i++) {
//            int slot = i;
//            final Literal slotVariable = f.literal(courseSlotType + course.getId() + Integer.toString(slot) + Boolean.toString(false), true);
//            final Literal slotNegatedVariable = slotVariable.negate();
//            courseSlotVariables.put(course.getId() + Integer.toString(slot) + Boolean.toString(false), slotVariable);
//            courseSlotVariables.put(course.getId() + Integer.toString(slot) + Boolean.toString(true), slotNegatedVariable);
//            //Create CourseSlotRoomVariables
//            for (Room room : rooms) {
//               final Literal courseRoomSlotVariable = f.literal(courseRoomSlotType + course.getId() + room.getId()
//                       + Integer.toString(slot) + Boolean.toString(false), true);
//               final Literal courseRoomSlotNegatedVariable = courseRoomSlotVariable.negate();
//               courseRoomSlotVariables.put(course.getId() + room.getId()
//                       + Integer.toString(slot) + Boolean.toString(false), courseRoomSlotVariable);
//               courseRoomSlotVariables.put(course.getId() + room.getId()
//                       + Integer.toString(slot) + Boolean.toString(true), courseRoomSlotNegatedVariable);
//            }
//         }
//         //Creating Room variables
//         for (Room room : rooms) {
//            final Literal roomVariable = f.literal(courseRoomType + course.getId() + room.getId() + Boolean.toString(false), true);
//            final Literal roomNegatedVariable = roomVariable.negate();
//            courseRoomVariables.put(course.getId() + room.getId() + Boolean.toString(false), roomVariable);
//            courseRoomVariables.put(course.getId() + room.getId() + Boolean.toString(true), roomNegatedVariable);
//         }
//      }
//      for (Curriculum curriculum : allCurricula) {
//         //Creating curriculum slot variables
//         for (int i = 0; i < (nPeriodsPerDay * nDays); i++) {
//            int slot = i;
//            final Literal curriculumVariable = f.literal(curriculumSlotType + curriculum.getId() + Integer.toString(slot) + Boolean.toString(false), true);
//            final Literal curriculumNegatedVariable = curriculumVariable.negate();
//            curriculumSlotVariables.put(curriculum.getId() + Integer.toString(slot) + Boolean.toString(false), curriculumVariable);
//            curriculumSlotVariables.put(curriculum.getId() + Integer.toString(slot) + Boolean.toString(true), curriculumNegatedVariable);
//         }
//      }
//      varMapList.add(courseDayVariables);
//      varMapList.add(courseSlotVariables);
//      varMapList.add(courseRoomVariables);
//      varMapList.add(curriculumSlotVariables);
//      varMapList.add(courseRoomSlotVariables);
//      return varMapList;
//   }
//
//   private MaxSAT.MaxSATResult solveSatWithLogicNGFromFile(String path) throws IOException {
//      Set<SATClause> clauses = new HashSet<>();
//      FileReader file = new FileReader(new File(path));
//      BufferedReader br = new BufferedReader(file);
//      String temp = br.readLine();
//      boolean firstLine = true;
//      while (temp != null) {
//         if (firstLine){
//            firstLine = false;
//            temp = br.readLine();
//            continue;
//         }
//         String[] spaceSeparatedArray = temp.split(" ");
//         int weight = Integer.valueOf(spaceSeparatedArray[0]);
//         boolean firstString = true;
//         Set<Literal> lits = new HashSet<>();
//         for (String s : spaceSeparatedArray){
//            if (firstString){
//               firstString = false;
//               continue;
//            }
//            int id = Integer.valueOf(s);
//            if (id == 0){
//               continue;
//            }
//            Literal lit;
//            if (id < 0){
//               lit = f.literal(Integer.toString(-id),false);
//            }
//            else{
//               lit = f.literal(Integer.toString(id),true);
//            }
//            lits.add(lit);
//         }
//         SATClause clause = new SATClause(lits,weight,weight==1000000);
//         clauses.add(clause);
//         temp = br.readLine();
//      }
//      MaxSATSolver solver = MaxSATSolver.wbo();
//      long start = System.currentTimeMillis();
//      for (SATClause clause : clauses) {
////         //create new clause based on literal ids
////         SATClause idClause;
////         Set<Literal> idLits= new HashSet<>()
////         for(Literal lit : clause.getLiterals()){
////            if (lit.phase() == false){
////               idLits.add(f.literal(literalToInt.get(lit.variable()),false);)
////            }
////         }
////			solver.addHardFormula(clauseToFormula(clause));
//         if (clause.isHard()) {
//            solver.addHardFormula(clauseToFormula(clause));
//         } else {
//            solver.addSoftFormula(clauseToFormula(clause), clause.getWeight());
//         }
//      }
//      long end = System.currentTimeMillis();
//      System.out.println(end - start);
//      MaxSAT.MaxSATResult solution = solver.solve();
//      Assignment model = solver.model();
//      int result = solver.result();
//      System.out.println("result");
//      System.out.println(result);
//      System.out.println(solution);
//      return solution;
//   }
//}
////}
////
