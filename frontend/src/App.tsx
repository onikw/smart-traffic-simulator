import SimulationViewer from './components/SimulationViewer';
import HeaderComponent from './components/HeaderComponent';
import SimulationMap from './components/SimulationMap';

function App() {
  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col">
      <HeaderComponent />

      {/* Główna zawartość */}
      <div id="container" className="flex-grow flex flex-col">
        <div className="flex justify-around p-4 gap-4">

          {/* Lewa kolumna*/}
          <aside className="w-1/4 bg-gray-800 border border-gray-600 m-5 rounded-lg p-4 shadow-lg">
            <h2 className="text-xl font-semibold mb-2">Wyślij dane</h2>
            <SimulationViewer />
          </aside>

          {/* Prawa kolumna*/}
          <section className="flex-grow bg-gray-800 border border-gray-600 m-5 rounded-lg p-4 shadow-lg">
            <h2 className="text-xl font-semibold mb-2">Wizualizacja</h2>
            <SimulationMap />
          </section>
        </div>
      </div>

      {/* Stopka */}
      <footer className="bg-gray-950 text-white p-4 text-center">
        <p>&copy; 2025 Wiktor Onik</p>
      </footer>
    </div>
  );
}

export default App;
