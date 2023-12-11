import tkinter as tk
from tkinter import messagebox
from tkinter import simpledialog

class Application:
    def __init__(self, root):
        self.root = root
        self.root.title("Gestion des Cours")

        self.courses = []

        self.label = tk.Label(root, text="Liste des Cours")
        self.label.pack()

        self.listbox = tk.Listbox(root)
        self.listbox.pack()

        self.add_button = tk.Button(root, text="Ajouter Cours", command=self.add_course)
        self.add_button.pack()

        self.show_button = tk.Button(root, text="Afficher Détails", command=self.show_details)
        self.show_button.pack()

    def add_course(self):
        course_name = tk.simpledialog.askstring("Ajouter Cours", "Nom du Cours:")
        if course_name:
            self.courses.append(course_name)
            self.listbox.insert(tk.END, course_name)

    def show_details(self):
        selected_index = self.listbox.curselection()
        if selected_index:
            selected_course = self.courses[selected_index[0]]
            messagebox.showinfo("Détails du Cours", f"Nom du Cours: {selected_course}")
        else:
            messagebox.showwarning("Avertissement", "Veuillez sélectionner un cours.")

if __name__ == "__main__":
    root = tk.Tk()
    app = Application(root)
    root.mainloop()
