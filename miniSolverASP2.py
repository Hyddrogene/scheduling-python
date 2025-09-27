#Voici un exemple de code Python qui implémente un parseur pour les programmes ASP et crée une structure de données adéquate pour représenter le programme :

class Literal:
    def __init__(self, name, sign):
        self.name = name
        self.sign = sign

    def __str__(self):
        return f"{self.sign}{self.name}"

class Rule:
    def __init__(self, head, body):
        self.head = head
        self.body = body

    def __str__(self):
        return f"{self.head} :- {', '.join(str(lit) for lit in self.body)}."

class ASPProgram:
    def __init__(self):
        self.rules = []
        self.litterals = []

    def add_rule(self, rule):
        self.rules.append(rule)

    def __str__(self):
        return "\n".join(str(rule) for rule in self.rules)

    def parse_literal(self,lit_str):
        sign = "-" if lit_str[0] == "-" else ""
        name = lit_str[1:] if lit_str[0] == "-" else lit_str
        tmp = Literal(name, sign)
        self.litterals.append(tmp)
        return tmp

    def parse_rule(self,rule_str):
        head, body = rule_str.split(" :- ")
        head = self.parse_literal(head.strip())
        body = [self.parse_literal(lit.strip()) for lit in body.split(",")]
        return Rule(head, body)

    def parse_program(self,program_str):
        for line in program_str.split("\n"):
            line = line.strip()
            if line and not line.startswith("%"):
                rule = self.parse_rule(line)
                self.add_rule(rule)
        return self

#Ce code définit trois classes : `Literal`, `Rule` et `ASPProgram`. La classe `Literal` représente un littéral ASP, qui est un atome (représenté par une chaîne de caractères) avec un signe (positif ou négatif). La classe `Rule` représente une règle ASP, qui est composée d'une tête (un littéral) et d'un corps (une liste de littéraux). La classe `ASPProgram` représente un programme ASP, qui est une liste de règles.

#La fonction `parse_literal` prend une chaîne de caractères représentant un littéral ASP et renvoie un objet `Literal`. La fonction `parse_rule` prend une chaîne de caractères représentant une règle ASP et renvoie un objet `Rule`. La fonction `parse_program` prend une chaîne de caractères représentant un programme ASP et renvoie un objet `ASPProgram`.

#Voici un exemple d'utilisation de ce code :

program_str = """
a :- b, c.
-d :- e.
"""


program = ASPProgram()
program.parse_program(program_str)

print(program)
for i in program.litterals :
    print(i)
"""
Ce code crée un objet `ASPProgram` à partir de la chaîne de caractères `program_str` et affiche la représentation sous forme de chaîne de caractères du programme :

a :- b, c.
-d :- e.

Bien sûr, ceci n'est qu'un exemple simple de parseur ASP en Python, mais j'espère que cela vous donnera une idée de la façon dont vous pouvez implémenter un parseur ASP en Python et créer une structure de données adéquate pour représenter un programme ASP.
"""