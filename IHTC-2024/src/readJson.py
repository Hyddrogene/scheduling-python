import json


class ihtcData :
    def __init__ (self,file_path):

        self.file_path = file_path

        # Ouvrir et lire le fichier JSON
        with open(self.file_path, 'r') as f:
            self.data_dict = json.load(f)

        self.nr_days = self.data_dict["days"] 
        self.skill_lvls = self.data_dict["skill_levels"] 
        self.nr_shifts_days = len(self.data_dict["shift_types"]) 
        self.shifts_days_name = self.data_dict["shift_types"] 
        self.age_groups = len(self.data_dict["age_groups"]) 
        self.age_groups_name = self.data_dict["age_groups"] 
        self.shift_days_convert = { self.data_dict["shift_types"][i]:i for i in range(len(self.data_dict["shift_types"]))}
        self.age_groups_convert = {age_group: i for i, age_group in enumerate(self.data_dict["age_groups"])}

        #### SURGEON
        self.surgeons = self.data_dict["surgeons"] 
        self.surgeon_name = [ i["id"] for i in self.surgeons]
        self.surgeon_max_workload = [ i["max_surgery_time"] for i in self.surgeons]
        self.num_surgeon = {self.surgeons[i]["id"] : i for i in range(len(self.surgeons))}
        print(self.surgeon_max_workload)
        #### ROOM
        self.rooms = self.data_dict["rooms"] 

        self.nr_room = len(self.rooms)
        self.room_name = [i["id"] for i in self.rooms]
        self.room_capacity = [i["capacity"] for i in self.rooms]
        self.num_room = {self.rooms[i]["id"]: i for i in range(0,len(self.rooms))}
        print(self.num_room)

        #### OT
        
        self.operating_theaters = self.data_dict["operating_theaters"] 
        self.ot_name = [i["id"] for i in self.operating_theaters] 
        self.ot_available = [i["availability"] for i in self.operating_theaters]
        print("operating_theaters",self.operating_theaters)

        #### OCCUPANTS
        self.occupants = self.data_dict["occupants"] 
        self.occupant_name = [ i["id"] for i in self.occupants]
        self.occupant_gender = [ 1 if i["gender"] == "A" else 0  for i in self.occupants]
        self.occupant_age = [ self.age_groups_convert[i["age_group"]] for i in self.occupants]
        self.occupant_duration = [ int(i["length_of_stay"]) for i in self.occupants]
        self.occupant_workload = [ i["workload_produced"] for i in self.occupants]
        self.occupant_workload_len = [ len(i["workload_produced"]) for i in self.occupants]
        self.occupant_workload_sum = [ sum([len(self.occupants[j]["workload_produced"]) for j in  range(0,i)]) for i in range(0,len(self.occupants))]
        self.occupant_room = [ self.num_room[i["room_id"]] for i in self.occupants]

        self.patients = self.data_dict["patients"] 
        #print(self.patients)
        self.patient_name = [i["id"] for i in self.patients]
        self.patient_mandatory = [1 if int(i["mandatory"]) else 0 for i in self.patients]
        self.patient_gender = [ 1 if i["gender"] == "A" else 0  for i in self.patients]
        self.patient_age = [ self.age_groups_convert[i["age_group"]] for i in self.patients]
        self.patient_day_release = [ int(i["surgery_release_day"]) for i in self.patients]

        self.patient_due_day = [ int(i["surgery_due_day"]) if "surgery_due_day" in i else -1 for i in self.patients]
        self.patient_duration = [ int(i["length_of_stay"]) for i in self.patients]
        self.patient_duration_surgeon = [ int(i["surgery_duration"]) for i in self.patients]
        self.patient_due_day_surgeon = [ int(i["surgery_release_day"]) for i in self.patients]
        
        self.patient_surgeon= [ self.num_surgeon[i["surgeon_id"]] for i in self.patients]

        self.patient_forbidden_room = [ [self.num_room[j] for j in i["incompatible_room_ids"] ]  for i in self.patients]
        self.patient_workload = [ i["workload_produced"] for i in self.patients]
        self.patient_skill_needs = [i["skill_level_required"] for i in self.patients] 
        self.patient_position = [sum([len(self.patients[j]["workload_produced"]) for j in  range(0,i)]) for i in range(0,len(self.patients))]
        #self.patient_position2 = [sum([len(self.patients[j]["skill_level_required"]) for j in  range(0,i)]) for i in range(0,len(self.patients))]
        
        print("patient_position",self.patient_position)
        #print("patient_position",self.patient_position2)

        ### NURSE
        
        self.nurses = self.data_dict["nurses"]
        self.nurse_name = [i["id"] for i in self.nurses]
        self.nurse_skill = [int(i["skill_level"]) for i in self.nurses]
        self.nurse_working_shift = [ [ [j["day"],self.shift_days_convert[j["shift"]],j["max_load"]] for j in i["working_shifts"]] for i in self.nurses]

        ### NURSE_ALL
        self.nurse_working_day = [len(i) for i in self.nurse_working_shift]
        self.nurse_working_position = [ sum([ self.nurse_working_day[j] for j in range(i)]) for i in range(len(self.nurse_working_day))  ]

        print(self.nurses)
        print("nurse_working_day",self.nurse_working_day)
        print("nurse_working_position",self.nurse_working_position)

        print("PATIENTS")
        print(self.patient_mandatory)
        print(self.patient_gender)
        print(self.patient_age)
        print(self.patient_duration)
        print(self.patient_mandatory)
        print(self.patient_surgeon)
        print("patient_surgeon", self.patient_surgeon)
        print("patient_due_day ",self.patient_due_day )
        print("patient_forbidden_room" ,self.patient_forbidden_room )
        print("patient_workload",self.patient_workload)
        print("patient_skill_needs",self.patient_skill_needs)
        print("nurse_working_shift",self.nurse_working_shift)

        ####
                
        print("OCCUPANTS")
        print(self.occupant_name)
        print(self.occupant_gender)
        print(self.age_groups_convert)
        print(self.occupant_age)
        print(self.occupant_workload_len)
        print(self.occupant_workload_sum)
        print(self.occupant_room)

    def toMzn(self):
        # Ouvrir le fichier en mode écriture
        with open('data.dzn', 'w') as f:

            # Ecriture des valeurs de `nr_days`, `skill_lvls` et `nr_shifts_days`
            f.write(f'nr_days = {self.nr_days};\n')
            f.write(f'skill_lvls = {self.skill_lvls};\n')
            f.write(f'nr_shifts_days = {self.nr_shifts_days};\n')
            f.write(f'shifts_days_name = [{", ".join(f"\"{s}\"" for s in self.shifts_days_name)}];\n')

            f.write(f'nr_occupants = {len(self.occupants)};\n')
            f.write(f'nr_patients = {len(self.patients)};\n')
            f.write(f'nr_nurses = {len(self.nurses)};\n')
            f.write(f'nr_surgeons = {len(self.surgeons)};\n')
            f.write(f'nr_operating_theater = {len(self.operating_theaters)};\n')
            f.write(f'nr_rooms = {self.nr_room};\n')
            f.write(f'nr_nurses_all = {sum(self.nurse_working_day)};\n')

            
            
            # Ecriture de `age_groups` et `age_groups_name`
            f.write(f'nr_age_of_group = {self.age_groups};\n')
            f.write(f'age_groups_name = [{", ".join(f"\"{s}\"" for s in self.age_groups_name)}];\n')


            # Ecriture de `shift_days_convert` et `age_groups_convert`
            f.write(f'%shift_days_convert = [{", ".join(f"{key}={value}" for key, value in self.shift_days_convert.items())}];\n')
            f.write(f'%age_groups_convert = [{", ".join(f"{key}={value}" for key, value in self.age_groups_convert.items())}];\n')

            #### SURGEON
            # Ecriture des chirurgiens
            #sg_name = ["\""+str(s)+"\"" for s in self.surgeon_name]
            #f.write(f'surgeon_name = [{ ", ".join(sg_name) }];\n')
            f.write(f'surgeon_name = [{", ".join(f"\"{s}\"" for s in self.surgeon_name)}];\n')
            f.write(f'surgeon_max_workload = array2d(1..{len(self.surgeon_max_workload)
            },1..{len(self.surgeon_max_workload[0])},[{", ".join(map(str, [item for sublist in self.surgeon_max_workload for item in sublist]))}]);\n')

            #### ROOM
            # Ecriture des salles
            
            #f.write(f'room_name = [{", ".join(map(str, self.room_name))}];\n')
            f.write(f'room_name = [{", ".join(f"\"{s}\"" for s in self.room_name)}];\n')
            f.write(f'room_capacity = [{", ".join(map(str, self.room_capacity))}];\n')

            #### OPERATING THEATERS (OT)
            # Ecriture des salles d'opération
            #f.write(f'ot_name = [{", ".join(map(str, self.ot_name))}];\n')
            f.write(f'ot_name = [{", ".join(f"\"{s}\"" for s in self.ot_name)}];\n')
            #f.write(f'ot_available = [{", ".join(map(str, self.ot_available))}];\n')
            #f.write(f'ot_available = [{", ".join(map(str, self.ot_available))}];\n')
            f.write(f'ot_available = array2d(1..{len(self.ot_available)
            },1..{len(self.ot_available[0])},[{", ".join(map(str, [item for sublist in self.ot_available for item in sublist]))}]);\n')

            #### OCCUPANTS
            # Ecriture des occupants
            #f.write(f'occupant_name = ["{", ".join(map(str, self.occupant_name))}"];\n')
            f.write(f'occupant_name = [{", ".join(f"\"{s}\"" for s in self.occupant_name)}];\n')
            f.write(f'occupant_gender = [{", ".join(map(str, self.occupant_gender))}];\n')
            f.write(f'occupant_age = [{", ".join(map(str, self.occupant_age))}];\n')
            f.write(f'occupant_duration = [{", ".join(map(str, self.occupant_duration))}];\n')
            #f.write(f'occupant_workload = [{", ".join(map(str, self.occupant_workload))}];\n')
            f.write(f'occupant_workload = [{", ".join(map(str, [item for sublist in self.occupant_workload for item in sublist]))}];\n')
            f.write(f'occupant_workload_len = [{", ".join(map(str, self.occupant_workload_len))}];\n')
            f.write(f'occupant_workload_sum = [{", ".join(map(str, self.occupant_workload_sum))}];\n')
            f.write(f'occupant_room = [{", ".join(map(str, [r+1 for r in self.occupant_room]))}];\n')

            ### PATIENT
            f.write(f'patient_name = [{", ".join(f"\"{s}\"" for s in self.patient_name)}];\n')
            f.write(f'patient_mandatory = [{", ".join(map(str, self.patient_mandatory))}];\n')
            f.write(f'patient_gender = [{", ".join(map(str, self.patient_gender))}];\n')
            f.write(f'patient_age = [{", ".join(map(str, self.patient_age))}];\n')
            f.write(f'patient_day_release = [{", ".join(map(str, self.patient_day_release))}];\n')
            f.write(f'patient_due_day = [{", ".join(map(str, self.patient_due_day))}];\n')
            f.write(f'patient_duration = [{", ".join(map(str, self.patient_duration))}];\n')
            f.write(f'patient_duration_surgeon = [{", ".join(map(str, self.patient_duration_surgeon))}];\n')
            f.write(f'patient_due_day_surgeon = [{", ".join(map(str, self.patient_due_day_surgeon))}];\n')
            f.write(f'patient_surgeon = [{", ".join(map(str, [s+1 for s in self.patient_surgeon]))}];\n')

            #f.write(f'patient_forbidden_room = [{", ".join(map(str, [room for sublist in self.patient_forbidden_room for room in sublist]))}];\n')
            #ssrm = ["{"+", ".join(r)+"}" for r in self.patient_forbidden_room]
            ssrm = ["{" + ", ".join(map(str, [rr+1 for rr in r])) + "}" for r in self.patient_forbidden_room]
            print("SSRML",ssrm)
            f.write(f'patient_forbidden_room = [{", ".join(ssrm)}];\n')


            f.write(f'patient_workload = [{", ".join(map(str, [workload for sublist in self.patient_workload for workload in sublist]))}];\n')
            f.write(f'patient_skill_needs = [{", ".join(map(str, [patient_skill_needs for sublist in self.patient_skill_needs for patient_skill_needs in sublist]))}];\n')
            #f.write(f'patient_skill_needs = [{", ".join(map(str, self.patient_skill_needs))}];\n')
            f.write(f'patient_position = [{", ".join(map(str, self.patient_position))}];\n')


            ### NURSE
            #f.write(f'nurse_name = [{", ".join(map(str, self.nurse_name))}];\n')
            f.write(f'nurse_name = [{", ".join(f"\"{s}\"" for s in self.nurse_name)}];\n')
            f.write(f'nurse_skill = [{", ".join(map(str, self.nurse_skill))}];\n')
            #f.write(f'nurse_working_shift = [{", ".join(map(str, [shift for day in self.nurse_working_shift for shift in day]))}];\n')
            dcdcd = "array2d("+"1.."+str(sum(self.nurse_working_day))+",1..3,["+", ".join(map(str, [shift for day in self.nurse_working_shift for shift in day])).replace("[","").replace("]","")+"])"
            f.write(f'nurse_working_shift = {dcdcd};\n')

            print()
            #f.write(f'nurse_working_shift = [\n| ' + ' |\n| '.join(["{ " + ", ".join(map(str, shift)) + " }" for shift in self.nurse_working_shift]) + ' |\n];\n')

            ### NURSE_ALL
            f.write(f'nurse_working_day = [{", ".join(map(str, self.nurse_working_day))}];\n')
            f.write(f'nurse_working_position = [{", ".join(map(str, self.nurse_working_position))}];\n')




        print("Fichier 'data.dzn' généré avec succès.")




data = "ihtc2024_test_dataset/ihtc2024_test_dataset/test01.json"
data = "/home/etud/Bureau/IHTC-2024/ihtc2024_test_dataset/ihtc2024_test_dataset/test01.json"
ihtcData = ihtcData(data)
ihtcData.toMzn()