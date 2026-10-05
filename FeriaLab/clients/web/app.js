const API_BASE = new URLSearchParams(window.location.search).get("api") || "http://localhost:8080/ferialab/api/v1";

const demoProjects = [
  {
    id: "demo-01",
    title: "Aula eficiente",
    category: "Sostenibilidad",
    summary: "Un monitor sencillo para conocer y reducir el consumo eléctrico de los espacios de estudio.",
    team: "Colectivo Voltio",
    accent: "mint",
    mark: "01",
    stage: "Prototipo"
  },
  {
    id: "demo-02",
    title: "Mapa de agua",
    category: "Tecnología",
    summary: "Registro colaborativo de puntos de agua y reportes de mantenimiento en el campus.",
    team: "Equipo Nómada",
    accent: "blue",
    mark: "02",
    stage: "En desarrollo"
  },
  {
    id: "demo-03",
    title: "Huerto cercano",
    category: "Comunidad",
    summary: "Una red local para compartir cosechas, semillas y talleres de cultivo urbano.",
    team: "Semilla Urbana",
    accent: "coral",
    mark: "03",
    stage: "Idea validada"
  },
  {
    id: "demo-04",
    title: "Turno claro",
    category: "Tecnología",
    summary: "Una propuesta para ordenar filas de atención y mostrar tiempos estimados con transparencia.",
    team: "Punto y Línea",
    accent: "lilac",
    mark: "04",
    stage: "Prototipo"
  }
];

const projectGrid = document.querySelector("#project-grid");
const searchInput = document.querySelector("#project-search");
const countOutput = document.querySelector("#project-count");
const statusOutput = document.querySelector("#data-status");
const emptyState = document.querySelector("#empty-state");
const projectDialog = document.querySelector("#project-dialog");
let projects = [...demoProjects];
let selectedCategory = "Todas";

function escapeHtml(value = "") {
  return String(value).replace(/[&<>"']/g, (character) => ({
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
  })[character]);
}

function projectCard(project, index) {
  const title = escapeHtml(project.title);
  const category = escapeHtml(project.category);
  const summary = escapeHtml(project.summary);
  const team = escapeHtml(project.team || "Equipo participante");
  const accent = ["mint", "blue", "coral", "lilac"].includes(project.accent) ? project.accent : ["mint", "blue", "coral", "lilac"][index % 4];
  const mark = escapeHtml(project.mark || String(index + 1).padStart(2, "0"));
  const stage = escapeHtml(project.stage || "En revisión");
  return `<article class="project-card accent-${accent}">
    <div class="card-art"><span class="card-number">${mark}</span><span class="art-shape"></span><span class="card-stage">${stage}</span></div>
    <div class="card-content"><div class="card-meta"><span>${category}</span><span class="meta-spark" aria-hidden="true">✳</span></div>
      <h3>${title}</h3><p>${summary}</p>
      <div class="card-bottom"><span class="team-name"><span class="team-avatar">${team.charAt(0).toUpperCase()}</span>${team}</span><button class="view-project" type="button" data-project="${escapeHtml(project.id)}">Ver proyecto <span aria-hidden="true">↗</span></button></div>
    </div></article>`;
}

function renderProjects() {
  const query = searchInput.value.trim().toLocaleLowerCase("es");
  const filtered = projects.filter((project) => {
    const matchesCategory = selectedCategory === "Todas" || project.category === selectedCategory;
    const searchable = `${project.title} ${project.category} ${project.summary} ${project.team || ""}`.toLocaleLowerCase("es");
    return matchesCategory && searchable.includes(query);
  });
  projectGrid.innerHTML = filtered.map(projectCard).join("");
  countOutput.textContent = String(projects.length).padStart(2, "0");
  emptyState.hidden = filtered.length !== 0;
}

async function loadProjects() {
  try {
    const response = await fetch(`${API_BASE}/proyectos?publicados=true`, { headers: { Accept: "application/json" } });
    if (!response.ok) throw new Error(`API respondió ${response.status}`);
    const payload = await response.json();
    const records = Array.isArray(payload) ? payload : payload.items;
    if (!Array.isArray(records)) throw new Error("La respuesta de proyectos no es una lista");
    projects = records.map((record, index) => ({ ...record, mark: String(index + 1).padStart(2, "0") }));
    statusOutput.textContent = "Datos conectados a FeriaLab";
    statusOutput.classList.add("is-connected");
  } catch {
    projects = [...demoProjects];
    statusOutput.textContent = "Modo demostración · datos de ejemplo";
  }
  renderProjects();
}

document.querySelectorAll(".filter").forEach((button) => {
  button.addEventListener("click", () => {
    selectedCategory = button.dataset.category;
    document.querySelectorAll(".filter").forEach((item) => {
      const active = item === button;
      item.classList.toggle("active", active);
      item.setAttribute("aria-pressed", String(active));
    });
    renderProjects();
  });
});

searchInput.addEventListener("input", renderProjects);

projectGrid.addEventListener("click", (event) => {
  const button = event.target.closest("[data-project]");
  if (!button) return;
  const project = projects.find((item) => String(item.id) === button.dataset.project);
  if (!project) return;
  document.querySelector("#dialog-category").textContent = project.category || "PROYECTO";
  document.querySelector("#dialog-title").textContent = project.title;
  document.querySelector("#dialog-summary").textContent = project.summary;
  document.querySelector("#dialog-team").textContent = project.team || "Equipo participante";
  const link = document.querySelector("#dialog-link");
  if (project.urlRepositorio) {
    link.href = project.urlRepositorio;
    link.hidden = false;
  } else {
    link.hidden = true;
  }
  projectDialog.showModal();
});

loadProjects();
