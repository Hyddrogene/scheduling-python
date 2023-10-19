import json

class constraintUSP:
    def __init__(self, sessions, constraint, parameters, elements, hardness, type):
        self.sessions = sessions
        self.constraint = constraint
        self.parameters = parameters
        self.elements = elements
        self.hardness = hardness
        self.type = type
    def __str__(self):
        return str(self.constraint)+" "+self.hardness+" "+str(self.sessions)


class instanceUSP :

    def __init__(self,filename):
        self.filename = filename
        with open(filename, 'r') as f:
            self.jsonRoot = json.load(f)
        #print(self.jsonRoot)
        self.data = self.jsonRoot["DATA"]
        self.nr_weeks = self.data["nr_weeks"]
        self.nr_days_per_week = self.data["nr_days_per_week"]
        self.nr_slots_per_day = self.data["nr_slots_per_day"]
        self.interval = self.data["interval"]
        self.abstract_grid = self.data["abstract_grid"]
        self.grid = self.data["grid"]
        self.grids = self.data["grids"]
        self.nr_courses = self.data["nr_courses"]
        self.nr_parts = self.data["nr_parts"]
        self.course_parts = [j["set"] for j in self.data["course_parts"]]
        self.nr_classes = self.data["nr_classes"]
        self.part_classes = [j["set"] for j in self.data["part_classes"]]
        self.nr_sessions = self.data["nr_sessions"]
        self.max_part_sessions = self.data["max_part_sessions"]
        self.part_nr_sessions = self.data["part_nr_sessions"]
        self.nr_equipments = self.data["nr_equipments"]
        self.nr_rooms = self.data["nr_rooms"]
        self.nr_teachers = self.data["nr_teachers"]
        self.nr_students = self.data["nr_students"]
        self.nr_part_rooms = self.data["nr_part_rooms"]
        self.part_rooms = [j["set"] for j in self.data["part_rooms"]]
        self.nr_part_teachers = self.data["nr_part_teachers"]
        self.part_teachers = [j["set"] for j in self.data["part_teachers"]]
        self.part_dailyslots = [j["set"] for j in self.data["part_dailyslots"]]
        self.part_days = [j["set"] for j in self.data["part_days"]]
        self.part_weeks = [j["set"] for j in self.data["part_weeks"]]
        self.part_bool_grid = self.data["part_bool_grid"]
        self.part_abstract_grid = self.data["part_abstract_grid"]
        self.part_session_length = self.data["part_session_length"]
        self.max_equipment_count = self.data["max_equipment_count"]
        self.max_class_maxheadcount = self.data["max_class_maxheadcount"]
        self.max_teacher_session = self.data["max_teacher_session"]
        self.max_teacher_sessions = self.data["max_teacher_sessions"]
        self.equipment_count = self.data["equipment_count"]
        self.max_room_capacity = self.data["max_room_capacity"]
        self.room_capacity = self.data["room_capacity"]
        self.part_room_use = self.data["part_room_use"]
        self.nr_part_room_mandatory = self.data["nr_part_room_mandatory"]
        self.part_room_mandatory = self.data["part_room_mandatory"]
        self.part_teacher_sessions_count = self.data["part_teacher_sessions_count"]
        self.part_session_teacher_count = self.data["part_session_teacher_count"]
        self.class_maxheadcount = self.data["class_maxheadcount"]
        self.class_parent = self.data["class_parent"]
        self.student_courses = [j["set"] for j in self.data["student_courses"]]
        self.equipment_name = self.data["equipment_name"]
        self.room_name = self.data["room_name"]
        self.teacher_name = self.data["teacher_name"]

        self.student_name = self.data["student_name"]
        self.course_name = self.data["course_name"]
        self.part_name = self.data["part_name"]
        self.class_name = self.data["class_name"]
        self.nr_labels = self.data["nr_labels"]
        self.label_name = self.data["label_name"]
        self.room_label = [j["set"] for j in self.data["room_label"]]
        self.teacher_label = [j["set"] for j in self.data["teacher_label"]]
        self.student_label = [j["set"] for j in self.data["student_label"]]
        self.course_label = [j["set"] for j in self.data["course_label"]]
        self.part_label = [j["set"] for j in self.data["part_label"]]

        solution = self.jsonRoot["SOLUTION"]
        
        self.groups = solution["GROUPS"]
        self.nr_groups = self.groups["nr_groups"]
        self.max_group_headcount = self.groups["max_group_headcount"]
        self.group_headcount = self.groups["group_headcount"]
        self.group_name = self.groups["group_name"]
        self.group_students = [j["set"] for j in self.groups["group_students"]]
        self.group_classes = [j["set"] for j in self.groups["group_classes"]]
        self.group_sessions = [j["set"] for j in self.groups["group_sessions"]]

        self.class_groups = [[k["set"] for k in j ] for j in solution["CLASS"]["class_groups"]]

        self.sessions = solution["SESSIONS"]

        self.session_rank = self.sessions["session_rank"]
        self.session_class = self.sessions["session_class"]
        self.session_dailyslot = self.sessions["session_dailyslot"]
        self.session_day = self.sessions["session_day"]
        self.session_week = self.sessions["session_week"]
        self.session_rooms = [j["set"] for j in self.sessions["session_rooms"]]
        self.session_teachers = [j["set"] for j in self.sessions["session_teachers"]]

        constraintsJson = self.jsonRoot["CONSTRAINTS"]
        self.constraints = []
        for constraintJson in constraintsJson:
            cons = constraintUSP([j["set"] for j in  constraintJson["sessions"]], constraintJson["constraint"], constraintJson["parameters"], constraintJson["elements"], constraintJson["hardness"], constraintJson["type"])
            self.constraints.append(cons)
            #print(cons)
        #print(constraintsJson)
        self.nr_slot = self.nr_weeks * self.nr_days_per_week * self.nr_slots_per_day
        #self. __createPartSlots()
        #self.__createClassPart()
        self.__createTab()
        
        #print(self.nr_weeks+1)
        #print(self.grids)
        #print(self.course_parts)
        #print(self.class_groups)
        #print(self.group_sessions[0])

    def __createTab(self):
        self.__createClassPart()
        self.__createPartCourse()
        self.__createPartCourse()
        
        self.__createPartSlots()
        self.__createClassSessions()
        self.__createSessionRank()
        self.__createSessionClass()
        self.__createPartSessions()
        self.__createTeacherParts()
        self.__createRoomParts()
        self.__createStudentGroup()
        self.__createSessionPart()

    def __createSessionPart(self):
        self.session_part = []
        for s in range(0,self.nr_sessions):
            p = self.class_part[self.session_class[s]-1]
            self.session_part.append(p)


    def __createStudentGroup(self):
        self.student_group = []
        for s in range(0,self.nr_students):
            for g in range(0,self.nr_groups):
                for sg in range(0,len(self.group_students[g])):
                    if self.group_students[g][sg] == (s+1) :
                        self.student_group.append(g+1)
            

    def __createRoomParts(self):
        self.room_parts = []
        self.room_sessions = []

        for r in range(0,self.nr_rooms):
            tmp = []
            tmp_sessions = []
            for p in range(0,self.nr_parts):
                for rp in range(0,len(self.part_rooms[p])):
                    if self.part_rooms[p][rp] == (r+1):
                        tmp.append(r+1)
                        tmp_sessions += self.part_sessions[p]
            self.room_parts.append(tmp)
            self.room_sessions.append(tmp_sessions)


    def __createPartSessions(self):
        self.part_sessions = []
        for p in range(0,self.nr_parts):
            tmp = []
            for c in range(0,len(self.part_classes[p])):
                tmp += self.class_sessions[self.part_classes[p][c]-1]
            self.part_sessions.append(tmp)
        #print(self.part_sessions)

    def __createTeacherParts(self):
        self.teacher_parts = []
        self.teacher_sessions = []

        for t in range(0,self.nr_teachers):
            tmp = []
            tmp_sessions = []
            for p in range(0,self.nr_parts):
                for tp in range(0,len(self.part_teachers[p])):
                    if self.part_teachers[p][tp] == (t+1):
                        tmp.append(p+1)
                        tmp_sessions += self.part_sessions[p]

            self.teacher_parts.append(tmp)
            self.teacher_sessions.append(tmp_sessions)
                


    def __createClassSessions(self):
        self.class_sessions = []
        s = 1
        for c in range(0,self.nr_classes):
            tmp = []
            for i in range(0,self.part_nr_sessions[self.class_part[c]-1]):
                tmp.append(s)
                s += 1
            self.class_sessions.append(tmp)
        #print(self.class_sessions)

    def __createSessionRank(self):
        self.session_rank = []
        sess = 0
        for c in range(0,self.nr_classes):
            for r in range(0,len(self.class_sessions[c])):
                self.session_rank.append(r+1)
                sess += 1

    def __createClassPart(self):
        self.class_part = [0 for i in range(0,self.nr_classes)]
        cl = 0
        for p in range(0,self.nr_parts):
            for c in range(0,len(self.part_classes[p])):
                #self.class_part[cl] = p+1
                #self.class_part[self.part_classes[p][c]-1].append(0)
                self.class_part[self.part_classes[p][c]-1] = p+1
                cl += 1

    def __createPartCourse(self):
        #print(self.course_parts)
        self.part_course = [0 for p in range(0,self.nr_parts)]
        for i in range(0, self.nr_courses):
            for j in range(0,len(self.course_parts[i])):
                self.part_course[(self.course_parts[i][j]-1)] = (i+1)

    def __createSessionClass (self):
        self.session_class = [0 for s in range(0,self.nr_sessions)]
        for c in range(0,self.nr_classes):
            for s in range(0,len(self.class_sessions[c])):
                #self.session_class[self.class_sessions[c][s]-1].append(0)
                self.session_class[self.class_sessions[c][s]-1] = c+1
    

    def __createPartSlots(self):
        self.part_slots = []
        for part in range(0,self.nr_parts):
            tabTmp = []
            for w in range(0,len(self.part_weeks[part])):
                for d in range(0,len(self.part_days[part])):
                    for s in range(0,len(self.part_dailyslots[part])):
                        val = self.part_dailyslots[part][s] + ((self.part_days[part][d]-1) * self.nr_slots_per_day ) + ((self.part_weeks[part][w]-1)*(self.nr_slots_per_day * self.nr_days_per_week))
                        tabTmp.append(val)
            self.part_slots.append(tabTmp)
        #print(self.part_slots)


    def getPositionTeacher(self,session,teacher):
        p = self.session_part[session] -1
        for i in range(0,len(self.part_teachers[p])):
            if self.part_teachers[p][i] == teacher:
                return i
                
        print("error")
        print("self.part_teachers[p]",self.part_teachers[p], " t ",teacher," p ",p," session ",session)
        return -1
    def getPositionRoom(self,session,room):
        p = self.session_part[session] -1
        for i in range(0,len(self.part_rooms[p])):
            if self.part_rooms[p][i] == room:
                return i
        return -1
#filename = "/home/etud/timetabling/src/ExperimentGenerator/tmp/experiment_18-09-23/instanceUSP_generated_180923153321_realistic_extension_v2.json"
#i = instanceUSP(filename)
#print(i.nr_weeks)
#print(i.session_rank)
#print(i.part_abstract_grid)