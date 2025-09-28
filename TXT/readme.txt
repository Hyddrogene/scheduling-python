
java -jar ExprimentGenerator_new2024Bis.jar /home/c.behuet/timetabling/tools/tools_php/config_maquette/config_20231117_181901.xml ./configurationVolume.xml ./configuration_rule.xml ./teacherCalculationByEtape.xml ./effectifFormationsData.xml experimentGeneratorName "1" <1111 optionnel>



let localTimeoutPeriod = 3_000_000
let trigCoefficient = 2.0 * Math.PI / float localTimeoutPeriod
let mutable gammaBase = 0.95
let mutable gammaAmplitude = 0.025
let gamma = gammaBase + gammaAmplitude * (1.0 + Math.Cos(trigCoefficient * float localTimeout))




        let f0 = best.SearchPenalty
        let nextSearch = fstun candidatePenalty f0 gamma
        let currentSearch = fstun currentPenalty f0 gamma
        if nextSearch < 0.3 && Math.Exp((currentSearch - nextSearch) / t) > next random then
          current <- candidate
          currentPenalty <- candidatePenalty
          assignmentPenalty <- assignmentPenalty'





        maxSlot = usp.grid[2]
        week_dur = self.usp.nr_days_per_week * maxSlot

        for p in range(0,usp.nr_parts):
            max_part_slot = 0
            for w in self.usp.part_weeks[p]:
                for d in self.usp.part_days[p]:
                    k = 0
                    for s in range(0,min(usp.part_abstract_grid[p][2],maxSlot)):
                        if (usp.part_abstract_grid[p][0]+k*usp.part_abstract_grid[p][1]) <= maxSlot :
                            f.writelines("part_slot("+self.changeStr(usp.part_name[p])+","+str((usp.part_abstract_grid[p][0]+(k*usp.part_abstract_grid[p][1])+((d-1)*maxSlot)+((w-1)*week_dur)))+fin)
                            max_part_slot = (usp.part_abstract_grid[p][0]+(k*usp.part_abstract_grid[p][1])+((d-1)*maxSlot)+((w-1)*week_dur))
                            k+=1
            f.writelines("part_maxslot("+self.changeStr(usp.part_name[p])+","+str(max_part_slot)+fin)

 
 pntx
 sbx
 ux
 hux


Les solveurs SAT basés sur CNF et MaxSAT sont centraux dans les systèmes de synthèse logique et de vérification. La popularité croissante de ces problèmes de contraintes dans l'automatisation de la conception électronique encourage les études sur différents problèmes SAT et leurs propriétés pour améliorer l'efficacité computationnelle. Il y a eu des succès tant théoriques que pratiques avec les solveurs SAT modernes à apprentissage de clauses guidé par les conflits, qui permettent de résoudre de très grandes instances industrielles en un temps relativement court. Récemment, les approches d'apprentissage machine offrent une nouvelle dimension à la résolution de ce problème difficile. Les modèles symboliques neuronaux pourraient servir de solveurs génériques qui peuvent être spécialisés pour des domaines spécifiques basés sur des données sans aucun changement dans la structure du modèle. Dans ce travail, nous proposons un modèle unique dérivé de l'architecture Transformer pour résoudre le problème MaxSAT, qui est la version d'optimisation de SAT où le but est de satisfaire le nombre maximum de clauses. Notre modèle a une structure sans échelle qui pourrait traiter des instances de taille variable. Nous utilisons un méta-chemin et un mécanisme d'auto-attention pour capturer les interactions entre les nœuds homogènes. Nous adoptons des mécanismes de cross-attention sur le graphe bipartite pour capturer les interactions entre les nœuds hétérogènes. Nous appliquons en outre un algorithme itératif à notre modèle pour satisfaire des clauses supplémentaires, permettant une solution se rapprochant de celle d'un problème SAT exact. Les mécanismes d'attention exploitent le parallélisme pour accélérer le traitement. Notre évaluation indique une accélération améliorée par rapport aux approches heuristiques et un taux d'achèvement amélioré par rapport aux approches d'apprentissage machine.
Termes indexés—logique




a = list(range(97,145))
>>> b = list(range(0,48))
>>> c = list(range(48,96))
>>> d = b+c+a
>>> d[47+2*48-1]
143
>>> d[47+(2*48)-1]



