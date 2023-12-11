

class itc2007 : 
    def __init__(self,filename):
        self.filename = filename
        # Open a file in read mode
        with open(self.filename, 'r') as self.file:
            # Read the file line by line
            i = 0
            self.courses = []
            self.rooms = []
            self.room_constraints = []
            self.unavailability_constraints = []
            self.curriculas = []

            for line in self.file:
                if line != "\n" and line != "END.\n": 
                    if line == "ROOM_CONSTRAINTS:\n" or i==5 :
                        if line == "ROOM_CONSTRAINTS:\n":
                            i+=1
                        else:
                            h = line.replace("\n","").split(" ")
                            #print(h)
                            self.room_constraints.append([h[0],h[1]])
                    elif line == "UNAVAILABILITY_CONSTRAINTS:\n" or i==4 :
                        if line == "UNAVAILABILITY_CONSTRAINTS:\n":
                            i+=1
                        else:
                            h = line.replace("\n","").split(" ")
                            #print(h)
                            self.unavailability_constraints.append([h[0],int(h[1]),int(h[2])])
                    elif line == "CURRICULA:\n" or i==3 :
                        if line == "CURRICULA:\n":
                            i+=1
                        else:
                            h = line.replace("\n","").split(" ")
                            #print(h)
                            g = int(h[1])
                            self.curriculas.append([h[0],g,h[2:g+2]])
                    elif line == "ROOMS:\n" or i==2 :
                        if line == "ROOMS:\n":
                            i+=1
                        else:
                            #name teacher 1 1 10 0
                            h = line.replace("\n","").split(" ")
                            #print(h)
                            self.rooms.append([h[0],int(h[1]),int(h[2])])
                    elif line == "COURSES:\n" or i == 1:
                        if line == "COURSES:\n":
                            i+=1
                        else:
                            #name teacher 1 1 10 0
                            h = line.replace("\n","").split(" ")
                            #print(h)
                            self.courses.append([h[0],h[1],int(h[2]),int(h[3]),int(h[4]),int(h[5])])
                    else :
                        #print(line,i, end="")
                        val = line.split(":")[0].replace(" ", "")
                        val1 = line.split(":")[1].replace(" ", "").replace("\n", "")
                        if val == "Name":
                            self.name = val1
                        if val == "Courses":
                            self.nr_courses = int(val1)
                        if val == "Rooms":
                            self.nr_rooms = int(val1)
                        if val == "Days":
                            self.nr_days = int(val1)
                        if "Periods_per_day" == val:
                            self.nr_periods_per_day = int(val1)
                        if "Curricula" == val:
                            self.curricula = int(val1)
                        if val == "Min_Max_Daily_Lectures":
                            #print(line, end="")
                            val1 = line.split(":")[1].split(" ")
                            #print(val1,len(val1), end="")
                            if len(val1) == 3 :
                                self.min_daily_lectures = int(val1[1])
                                self.max_daily_lectures = int(val1[2])
                            else:
                                self.min_daily_lectures = int(val1[0])
                                self.max_daily_lectures = int(val1[1])
                        if val == "UnavailabilityConstraints":
                            self.nr_unavailability_constraints= int(val1)
            print("Finish")
                        
    def __str__(self):
        return self.name+" nr_courses "+str(self.nr_courses)+" nr_days "+str(self.nr_days)+" min_daily_lectures "+str(self.min_daily_lectures)+" max_daily_lectures "+str(self.max_daily_lectures)

#itc = itc2007("/home/etud/Bureau/ITC-2007/ITC-2007-CBCTT/comp05.ectt")
#print(itc)
#print(itc.curriculas)
#print(itc.courses)
#print(itc.rooms)
#print(itc.room_constraints)
#print(itc.unavailability_constraints)