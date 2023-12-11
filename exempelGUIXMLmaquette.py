import tkinter as tk
from tkinter import ttk
from xml.etree.ElementTree import Element, SubElement, tostring
from xml.dom import minidom

class XMLGeneratorApp:
    def __init__(self, root):
        self.root = root
        self.root.title("XML Generator")

        # Variables pour stocker les valeurs saisies
        self.departement_entries = []
        self.step_entries = []

        # Créer les composants de l'interface
        ttk.Label(root, text="Configuration XML").pack(pady=10)

        self.add_step_button = ttk.Button(root, text="Ajouter Étape", command=self.add_step)
        self.add_step_button.pack(pady=5)

        self.add_departement_button = ttk.Button(root, text="Ajouter Département", command=self.add_departement)
        self.add_departement_button.pack(pady=5)

        self.generate_button = ttk.Button(root, text="Générer XML", command=self.generate_xml)
        self.generate_button.pack(pady=20)

        self.result_label = ttk.Label(root, text="")
        self.result_label.pack()

    def add_step(self):
        # Ajouter dynamiquement une nouvelle étape
        frame = ttk.Frame(self.root)
        frame.pack(pady=5)

        etape_label = ttk.Label(frame, text=f"Étape {len(self.step_entries) + 1}")
        etape_label.grid(row=0, column=0, padx=5)

        entry_var = tk.StringVar()
        entry = ttk.Entry(frame, textvariable=entry_var)
        entry.grid(row=0, column=1, padx=5)

        self.step_entries.append({'label': etape_label, 'entry': entry})

    def add_departement(self):
        # Ajouter dynamiquement un nouveau département
        frame = ttk.Frame(self.root)
        frame.pack(pady=5)

        departement_label = ttk.Label(frame, text=f"Département {len(self.departement_entries) + 1}")
        departement_label.grid(row=0, column=0, padx=5)

        entry_var = tk.StringVar()
        entry = ttk.Entry(frame, textvariable=entry_var)
        entry.grid(row=0, column=1, padx=5)

        probability_var = tk.DoubleVar()
        probability_entry = ttk.Entry(frame, textvariable=probability_var)
        probability_entry.grid(row=0, column=2, padx=5)

        self.departement_entries.append({'label': departement_label, 'entry': entry, 'probability_entry': probability_entry})

    def generate_xml(self):
        # Logique pour récupérer les valeurs saisies et générer le fichier XML
        root_element = Element("configuration")

        for entry_data in self.departement_entries:
            values = entry_data['entry'].get()
            probability = entry_data['probability_entry'].get()
            nr_departement_element = SubElement(root_element, "departement")
            nr_departement_element.set("name", entry_data['label'].cget("text"))
            nr_departement_element.set("probability", str(probability))
            nr_departement_element.text = values

        for entry_data in self.step_entries:
            values = entry_data['entry'].get()
            nr_departement_element = SubElement(root_element, "etape")
            nr_departement_element.set("name", entry_data['label'].cget("text"))
            nr_departement_element.text = values

        xml_content = self.prettify(root_element)

        # Afficher le résultat
        self.result_label.config(text="Le fichier XML a été généré avec succès!")
        print(xml_content)  # Vous pouvez enregistrer xml_content dans un fichier si nécessaire

    def prettify(self, elem):
        """Return a pretty-printed XML string."""
        rough_string = tostring(elem, 'utf-8')
        reparsed = minidom.parseString(rough_string)
        return reparsed.toprettyxml(indent="    ")

if __name__ == "__main__":
    root = tk.Tk()
    app = XMLGeneratorApp(root)
    root.mainloop()
