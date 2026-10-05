using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace FeriaLab.Organizer
{
    public sealed class ProjectsForm : Form
    {
        private const string DefaultApi = "http://localhost:8080/ferialab/api/v1";
        private readonly DataGridView grid = new DataGridView();
        private readonly Label status = new Label();
        private readonly TextBox search = new TextBox();
        private readonly List<Project> projects = new List<Project>();

        public ProjectsForm()
        {
            Text = "FeriaLab · Organización";
            Width = 1000;
            Height = 640;
            StartPosition = FormStartPosition.CenterScreen;
            search.Dock = DockStyle.Top;
            search.TextChanged += (sender, args) => RenderRows();
            status.Dock = DockStyle.Bottom;
            status.Height = 28;
            status.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            grid.Dock = DockStyle.Fill;
            grid.ReadOnly = true;
            grid.AllowUserToAddRows = false;
            grid.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            grid.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            grid.Columns.Add("title", "Proyecto");
            grid.Columns.Add("category", "Categoría");
            grid.Columns.Add("team", "Equipo");
            grid.Columns.Add("stage", "Estado");
            grid.Columns.Add("summary", "Resumen");
            Controls.Add(grid);
            Controls.Add(search);
            Controls.Add(status);
            Shown += async (sender, args) => await LoadProjects();
        }

        private async Task LoadProjects()
        {
            status.Text = "Conectando con FeriaLab…";
            try
            {
                var baseUrl = Environment.GetEnvironmentVariable("FERIALAB_API") ?? DefaultApi;
                using (var client = new HttpClient())
                using (var response = await client.GetAsync(baseUrl + "/proyectos?publicados=true"))
                {
                    response.EnsureSuccessStatusCode();
                    var serializer = new DataContractJsonSerializer(typeof(List<Project>));
                    using (var stream = await response.Content.ReadAsStreamAsync())
                        projects.AddRange((List<Project>)serializer.ReadObject(stream));
                }
                status.Text = projects.Count + " proyectos publicados";
                RenderRows();
            }
            catch (Exception)
            {
                status.Text = "No se pudo conectar. Revisa que la API esté activa.";
            }
        }

        private void RenderRows()
        {
            var query = search.Text.Trim();
            grid.Rows.Clear();
            foreach (var project in projects)
            {
                var searchable = project.Title + " " + project.Category + " " + project.Team + " " + project.Summary;
                if (searchable.IndexOf(query, StringComparison.CurrentCultureIgnoreCase) >= 0)
                    grid.Rows.Add(project.Title, project.Category, project.Team, project.Stage, project.Summary);
            }
        }

        [DataContract]
        private sealed class Project
        {
            [DataMember(Name = "title")] public string Title { get; set; }
            [DataMember(Name = "category")] public string Category { get; set; }
            [DataMember(Name = "team")] public string Team { get; set; }
            [DataMember(Name = "stage")] public string Stage { get; set; }
            [DataMember(Name = "summary")] public string Summary { get; set; }
        }
    }
}
