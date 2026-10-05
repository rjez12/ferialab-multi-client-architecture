"""Catálogo y exportación CSV de proyectos publicados en FeriaLab."""

import csv
import json
import os
import threading
import urllib.error
import urllib.request
import tkinter as tk
from tkinter import filedialog, messagebox, ttk


API_BASE = os.environ.get("FERIALAB_API", "http://localhost:8080/ferialab/api/v1")


class FeriaLabDataTools(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("FeriaLab · Herramientas de datos")
        self.geometry("980x600")
        self.minsize(760, 420)
        self.projects = []

        header = ttk.Frame(self, padding=16)
        header.pack(fill="x")
        ttk.Label(header, text="Catálogo de proyectos", font=("Segoe UI", 18, "bold")).pack(anchor="w")
        ttk.Label(header, text="Consulta datos publicados y guárdalos como CSV.").pack(anchor="w", pady=(4, 12))

        controls = ttk.Frame(header)
        controls.pack(fill="x")
        self.query = tk.StringVar()
        self.query.trace_add("write", lambda *_: self.render())
        ttk.Entry(controls, textvariable=self.query).pack(side="left", fill="x", expand=True)
        ttk.Button(controls, text="Exportar CSV", command=self.export_csv).pack(side="left", padx=(8, 0))
        ttk.Button(controls, text="Actualizar", command=self.load_projects).pack(side="left", padx=(8, 0))

        columns = ("title", "category", "team", "stage", "summary")
        self.table = ttk.Treeview(self, columns=columns, show="headings")
        for column, heading, width in [
            ("title", "Proyecto", 190), ("category", "Categoría", 130),
            ("team", "Equipo", 150), ("stage", "Estado", 110), ("summary", "Resumen", 360),
        ]:
            self.table.heading(column, text=heading)
            self.table.column(column, width=width, anchor="w")
        self.table.pack(fill="both", expand=True, padx=16)
        self.status = tk.StringVar(value="Conectando con FeriaLab…")
        ttk.Label(self, textvariable=self.status, padding=(16, 10)).pack(fill="x")
        self.load_projects()

    def load_projects(self):
        self.status.set("Conectando con FeriaLab…")
        threading.Thread(target=self._fetch_projects, daemon=True).start()

    def _fetch_projects(self):
        request = urllib.request.Request(
            f"{API_BASE}/proyectos?publicados=true", headers={"Accept": "application/json"}
        )
        try:
            with urllib.request.urlopen(request, timeout=10) as response:
                projects = json.loads(response.read().decode("utf-8"))
            if not isinstance(projects, list):
                raise ValueError("La API no devolvió una lista de proyectos")
            self.after(0, lambda: self._set_projects(projects))
        except (urllib.error.URLError, TimeoutError, ValueError, json.JSONDecodeError):
            self.after(0, lambda: self.status.set("No se pudo conectar. Revisa que la API esté activa."))

    def _set_projects(self, projects):
        self.projects = projects
        self.status.set(f"{len(projects)} proyectos publicados")
        self.render()

    def render(self):
        query = self.query.get().casefold().strip()
        self.table.delete(*self.table.get_children())
        for project in self.projects:
            values = (
                project.get("title", ""), project.get("category", ""), project.get("team", ""),
                project.get("stage", ""), project.get("summary", ""),
            )
            if query in " ".join(values).casefold():
                self.table.insert("", "end", values=values)

    def export_csv(self):
        if not self.projects:
            messagebox.showinfo("Sin datos", "Conecta con la API antes de exportar.")
            return
        path = filedialog.asksaveasfilename(
            title="Guardar catálogo", defaultextension=".csv",
            filetypes=(("CSV", "*.csv"),), initialfile="ferialab-proyectos.csv",
        )
        if not path:
            return
        fields = ("id", "title", "category", "summary", "team", "stage", "urlRepositorio")
        with open(path, "w", newline="", encoding="utf-8-sig") as output:
            writer = csv.DictWriter(output, fieldnames=fields, extrasaction="ignore")
            writer.writeheader()
            writer.writerows(self.projects)
        self.status.set(f"CSV guardado: {path}")


if __name__ == "__main__":
    FeriaLabDataTools().mainloop()
