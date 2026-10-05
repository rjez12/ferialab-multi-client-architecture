using System;
using System.Windows.Forms;

namespace FeriaLab.Organizer
{
    internal static class Program
    {
        [STAThread]
        private static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new ProjectsForm());
        }
    }
}
