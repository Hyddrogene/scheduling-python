import xml.etree.ElementTree as ET
import re
from graphviz import Digraph
import sys


kcount = 0
activateCount = 0

def parse_feature_model(xml_file):
    tree = ET.parse(xml_file)
    root = tree.getroot()
    
    feature_model = {}
    
    def parse_feature(element, parent=None,sub_type=None):
        global kcount
        global activateCount
        feature_name = element.get('name')
        mandatory = element.get('mandatory') == 'true'
        actv = element.get('activate') != None
        actvVal = False
        if actv :
            actvVal = element.get('activate')
            activateCount+=1
        feature_model[feature_name] = {'mandatory': mandatory, 'subfeatures': [], 'type': sub_type, 'parent': parent, 'activate':element.get('activate')}
        #print({'mandatory': mandatory, 'subfeatures': [], 'type': sub_type, 'parent': parent, 'activate':actv})
        subfeatures = element.findall('./subFeature/*')
        for sub in subfeatures:
            #print(sub.tag)
            #print(sub.get("name"))
            sub_type = sub.get('type')
            
            if sub.tag == "Feature" :
                parse_feature(sub, feature_name)
            else : 
                if sub_type:
                    sub_elements = sub.findall('./Feature')
                    sub_elements2 = sub.findall('./subFeature')
                    #print("sub_elements2",sub_elements2 )
                    name_sub = sub_type+str(kcount)
                    actv = element.get('activate')!= None
                    feature_model[name_sub] = {'mandatory': mandatory, 'subfeatures': [], 'type':None , 'parent': feature_name, 'activate':actv}
                    
                    kcount+=1;
                   
                    for sub_element in sub_elements:
                         parse_feature(sub_element, name_sub,sub_type)
                        #sub_feature_name = sub_element.get('name')
                        #feature_model[sub_feature_name] = {'mandatory': False, 'subfeatures': [], 'type': sub_type, 'parent': name_sub}
                    if len(sub_elements2)>0 :
                        for i in range(0,len(sub_elements2)):
                            
                            
                            sub_type = sub_elements2[i].get('type')
                            actv = element.get('activate')!= None
                            name_sub2 = sub_type+str(kcount)
                            feature_model[name_sub2] = {'mandatory': mandatory, 'subfeatures': [], 'type':sub_type , 'parent': name_sub, 'activate':actv}
                            kcount+=1
                            for sub_element2 in sub_elements2[i] :
                                parse_feature(sub_element2, name_sub2,sub_type)
                else:
                    parse_feature(sub,feature_name)
            
           
    
    features = root.findall('./Features/Feature')
    for feature in features:
        parse_feature(feature)
    #print(feature_model)
    return feature_model




def generate_feature_model_graph(feature_model):
    dot = Digraph(comment='Feature Model')
    wf = '0.3'
    
    for feature, attributes in feature_model.items():
        shape = 'ellipse'
        color = 'black'
        penwidth = '1'
        if attributes['mandatory']:
            color = 'red'
            penwidth='4'
        
        pattern = r'^(XOR|OR)\d+$'
        #print("feature",feature)
        if re.match(pattern, feature) :
            match = re.match(pattern, feature)
            
            if match.group(1) == "XOR":
                #dot.node(feature, 'XOR', shape='point', width='0.1', style='filled', color='white')
                dot.node(feature , '', shape='diamond', width= wf,height=wf, style='filled', fillcolor='white', color='black')

            else :
                #dot.node(feature, 'OR', shape='point', width='0.1', style='filled', color='black')
                dot.node(feature, '', shape='ellipse', width=wf,height=wf ,style='filled', fillcolor='black', color='black')
                #dot.node(feature, '', shape=shape, color=color)
        else : 
            
            
            activate_value = attributes['activate']
            print("feature",feature," = ",activate_value)
            if isinstance(activate_value, str):
                is_active = activate_value.lower() == 'true'
            elif isinstance(activate_value, bool):
                is_active = activate_value
            else:
                raise ValueError(f"Unexpected type for 'activate': {type(activate_value)}")

            if not is_active:
                if activateCount == 0:
                    dot.node(feature, feature, shape=shape, color=color, penwidth=penwidth)
                else:
                    dot.node(feature, feature, shape=shape, style='filled', color=color, fillcolor="gray", penwidth=penwidth)
            else:
                dot.node(feature, feature, shape=shape, style='filled', color=color, fillcolor="green", penwidth=penwidth)

            
            #if 
            """
            print(feature,attributes["activate"])
            if attributes['activate'].lower() == 'false':
                if activateCount == 0:
                    dot.node(feature, feature, shape=shape, color=color,penwidth=penwidth)
                else:
                    dot.node(feature, feature, shape=shape,style='filled', color=color,fillcolor="gray",penwidth=penwidth)
            else : 
                dot.node(feature, feature, shape=shape,style='filled', color=color,fillcolor="green",penwidth=penwidth)
        """
        
        
        
        if attributes['parent']:
            parent = attributes['parent']
            #print("parent",parent )
            #print("attributes['type']",attributes['type'] )
            #print("feature", feature)
            if attributes['type'] == 'OR':
                or_node = f"or_{parent}_{feature}"
                #dot.node(or_node, '', shape='point', width='0.1', style='filled', color='black')
                #dot.edge(parent, or_node, arrowhead='none')
                dot.edge(parent, feature, arrowhead='none', style='dashed')
            elif attributes['type'] == 'XOR':
                xor_node = f"xor_{parent}_{feature}"
                #dot.node(xor_node, '', shape='point', width='0.1', style='filled', color='white')
                #dot.edge(parent, xor_node, arrowhead='none')
                dot.edge(parent, feature, arrowhead='none', style='dashed')
            elif re.match(pattern, feature) :
                 dot.edge(parent, feature, arrowhead='none')
            else:
                dot.edge(parent, feature)
        
        # actual
        if  1 == 0 :
            principalenode = "Timetabling problem"
            
            
            se = "some-exclusive"
            ch = "course-hierarchy"
            nr = "no-room"
            
            cr = "crosscutting"
            sc = "scheduling"
            tc = "teaching"
            ad = "attending"
            
            dot.edge( "single-teacher",cr, style="invis")
            #dot.edge( nr,sc, style="invis")
            dot.edge( "event",tc, style="invis")
            dot.edge( ch,ad, style="invis")
            dot.edge( "modular","hosting", style="invis")
            
            
        if  1 == 0 :
            principalenode = "Timetabling problem"
            
            
            se = "some-exclusive"
            #ch = "course-hierarchy"
            nr = "no-room"
            
            cr = "crosscutting"
            tc = "teaching"
            ad = "attending"
            
            dot.edge( "single-teacher",cr, style="invis")
            #dot.edge( nr,sc, style="invis")
            #dot.edge( "event",tc, style="invis")
            #dot.edge( ch,ad, style="invis")
            #dot.edge( "modular","hosting", style="invis")
            
    return dot

xml_file = 'formatModelFeature.xml'
xml_file = 'formatModelFeature-opt.xml'

"""
xml_file = 'FM_general_entity.xml'

xml_file = 'FM_mandatory_timing.xml'




xml_file = 'FM_constraint.xml'

xml_file = 'formatModelFeature.xml'
#xml_file = 'FM_gc2.xml'

xml_file = 'FM_optionnal.xml'
"""


#xml_file = 'formatModelFeature-attending.xml'
#xml_file = 'formatModelFeature-courses.xml'
#xml_file = 'formatModelFeature-crosscutting.xml'
#xml_file = 'formatModelFeature-hosting.xml'
#xml_file = 'formatModelFeature-manda.xml'
#xml_file = 'formatModelFeature-opt.xml'
#xml_file = 'formatModelFeature-scheduling.xml'
#xml_file = 'formatModelFeature-teaching.xml'
#xml_file = 'formatModelFeature-timing.xml'
#xml_file = 'formatModelFeature.xml'


# Vérifie que l'utilisateur a passé un argument
if len(sys.argv) < 2:
    print("Usage: python script.py <xml_file>")
    sys.exit(1)

# Récupère l'argument passé en ligne de commande
xml_file = sys.argv[1]

feature_model = parse_feature_model(xml_file)
dot = generate_feature_model_graph(feature_model)
dot.render('feature_model', format='png', view=True)
