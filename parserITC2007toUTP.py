from parserITC2007 import *
import os
from statistics import *

class groups :
    def __init__(self,id, val,tab):
        self.size = val
        self.id =id
        self.courses = tab
        self.classes = [tab[i]+"-cl" for i in range(0,len(tab))]
    def setStudents(self, students):
        self.students = students


class parseitc2007 : 
    def __init__(self,filename):
        self.filename = filename
        self.itc = itc2007(filename)


    def getRoomsForCourse(self,course_name,course_size=9999):
        tab = []
        tabu_room = [] 
        for i in self.itc.room_constraints :
            if i[0] == course_name:
                tabu_room.append(i[1])

        tabu = []
        for i in self.itc.rooms:
            for j in self.itc.room_constraints :
                if not(i[0] in tabu_room) :
                    if not(i[0] in tabu) :
                        if course_size <= i[1]:
                            tabu.append(i[0])
                            tab.append(i[0])
        return tab
        #self.itc.room_constraints

    def getNrCurricula(self,course):
        u = 0
        for c in self.itc.curriculas:
            if course in c[2]:
                u += 1
        return u

    def getCourseSize(self,course):
        for i in self.itc.courses :
            if i[0] == course :
                return i[4]


    def convert(self):
        Filename_ictpUSp = self.filename[0:-5]+"_utp.xml"
        if (os.path.exists(Filename_ictpUSp) == False):
            f = open(Filename_ictpUSp,mode= "a+")
        else:
            f = open(Filename_ictpUSp, "w")
            print("File Exists")   
        fin = ").\n"

        f.writelines("<?xml version=\"1.0\"?>\n")
        f.writelines("<timetabling xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"usp_timetabling_v1_2.xsd\" name=\"ua_timtetabling_itcconversion\" nrWeeks=\"1\" nrDaysPerWeek=\""+str(self.itc.nr_days)+"\" nrSlotsPerDay=\""+str(self.itc.nr_periods_per_day)+"\">\n")
        #print(itc)
        print(self.itc.curriculas)
        #print(self.itc.courses)
        #print(self.itc.rooms)
        #print(self.itc.room_constraints)
        #print(self.itc.unavailability_constraints)

        #rooms
        f.writelines("<rooms>\n")
        for r in self.itc.rooms:
            f.writelines("<room id=\""+r[0]+"\" capacity=\""+str(r[1])+"\" label=\""+"room,bat-"+str(r[2])+"\"/>\n")
        f.writelines("</rooms>\n")
        #teachers
        tabu = []
        f.writelines("<teachers>\n")
        for c in self.itc.courses:
            if not(c[1] in tabu):
                f.writelines("<teacher id=\""+c[1]+"\" label=\"teacher\" />\n")
                tabu.append(c[1])
        f.writelines("</teachers>\n")

        #courses

        tabu = []
        f.writelines("<courses>\n")
        for c in self.itc.courses:
            
            f.writelines("<course id=\""+c[0]+"\" >\n")
            f.writelines("<part id=\""+c[0]+"-p"+"\" nrSessions=\""+str(c[2])+"\" label=\"part\" >\n")
            f.writelines("<classes maxHeadCount=\""+str(c[4])+"\" >\n")
            f.writelines("<class id=\""+c[0]+"-cl"+"\"/>\n")
            f.writelines("</classes>\n")
            f.writelines("<allowedSlots sessionLength=\"1\">\n")
            f.writelines("<dailySlots>1-"+str(self.itc.nr_periods_per_day)+"</dailySlots>\n")
            f.writelines("<days>1-"+str(self.itc.nr_days)+"</days>\n")
            f.writelines("<weeks>1</weeks>\n")
            f.writelines("</allowedSlots>\n")

            f.writelines("<allowedRooms sessionRooms=\"single\">\n")
            for i  in self.getRoomsForCourse(c[0],c[4]) :
                f.writelines("<room refId=\""+i+"\"/>\n")
        	#<room refId="H006"/>
            f.writelines("</allowedRooms>\n")

            f.writelines("<allowedTeachers sessionTeachers=\"1\">\n")
            f.writelines("<teacher refId=\""+c[1]+"\" nrSessions=\""+str(c[2])+"\" />\n")
        	#<room refId="H006"/>
            f.writelines("</allowedTeachers>\n")
            f.writelines("</part>\n")
            f.writelines("</course>\n")
        f.writelines("</courses>\n")

        #students
        curriculaSize = []
        for i in self.itc.curriculas:
            res = [self.getCourseSize(j) / self.getNrCurricula(j)  for j in i[2]]
            print(res )
            print(mean(res))
            curriculaSize.append(round(min(res)))
            #for j in i[2]:
            #    self.getNrCurricula(j)
        groupes = []
        for i in self.itc.courses:
            k = 0 
            t = 0
            for j in self.itc.curriculas: 
                
                if i[0] in j[2] :
                   t +=  curriculaSize[k]
                k += 1
        k = 0
        for i in self.itc.curriculas:
            g = groups(i[0],curriculaSize[k],i[2])
            groupes.append(g)
            k += 1


        stud = 1
       
        f.writelines("<students>\n")
        for i in groupes:
            students = []
            for k in range(0,i.size):
                f.writelines("<student id=\"student-"+str(stud)+"\" label=\""+"student"+"\">\n")
                students.append("student-"+str(stud))
                f.writelines("<courses>\n")
                for j in i.courses :
                    f.writelines("<course refId=\""+j+"\" />\n")
                f.writelines("</courses>\n")
                f.writelines("</student>\n")
                stud += 1
            i.setStudents(students)
            
        f.writelines("</students>\n")

        #rules

        f.writelines("<rules>\n")

        f.writelines("<rule>\n")
        f.writelines("    <selector generator=\"(student, *)\" filters=\"\" />\n")
        f.writelines("    <constraint name=\"disjunctiveStudents\" type=\"hard\">\n")
        f.writelines("    </constraint>\n")
        f.writelines("</rule>\n")

        f.writelines("<rule>\n")
        f.writelines("    <selector generator=\"(lecturer, *)\" filters=\"\" />\n")
        f.writelines("    <constraint name=\"disjunctiveTeachers\" type=\"hard\">\n")
        f.writelines("    </constraint>\n")
        f.writelines("</rule>\n")

        f.writelines("<rule>\n")
        f.writelines("    <selector generator=\"(room, *)\" filters=\"\" />\n")
        f.writelines("    <constraint name=\"disjunctiveRooms\" type=\"hard\">\n")
        f.writelines("    </constraint>\n")
        f.writelines("</rule>\n")

        f.writelines("</rules>\n")

        #groupes
        f.writelines("<solution>\n")
        f.writelines("<groups>\n")
        for i in groupes:
            f.writelines("<group id=\""+i.id+"\">\n")
            f.writelines("<students>\n")
            for j in i.students:
                f.writelines("<student refId=\""+j+"\"/>\n")
            f.writelines("</students>\n")
            f.writelines("<classes>\n")
            for j in i.classes:
                f.writelines("<class refId=\""+j+"\"/>\n")
            f.writelines("</classes>\n")
            f.writelines("</group>\n")    
        f.writelines("</groups>\n")
        f.writelines("</solution>\n")
        f.writelines("</timetabling>\n")


filename = "/home/etud/Bureau/ITC-2007/ITC-2007-CBCTT/comp05.ectt"

pitc = parseitc2007(filename)
pitc.convert()