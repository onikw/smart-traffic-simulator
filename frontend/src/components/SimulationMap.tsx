import React, { useEffect, useState } from 'react';
import { SimulationVisStep } from '../types/simulation';

const SimulationMap: React.FC = () => {
    const [speed, setSpeed] = useState(1.0);
    const [stepData, setStepData] = useState<SimulationVisStep | null>(null);
    const [isRunning, setIsRunning] = useState(false);
    const [log, setLog] = useState<string[]>([]);

    useEffect(() => {
        if (!isRunning) return;

        let isCancelled = false;

        const fetchSteps = async (stepNumber: number) => {
            try {
                const res = await fetch(`/api/steps/${stepNumber}`);
                if (!res.ok) {
                    console.log(`✅ Koniec symulacji (brak step${stepNumber}.json)`);
                    setIsRunning(false);
                    return;
                }

                const contentType = res.headers.get("content-type");
                if (!contentType || !contentType.includes("application/json")) {
                    console.warn(`❌ Niepoprawny typ odpowiedzi: ${contentType}`);
                    setIsRunning(false);
                    return;
                }

                const data = await res.json() as SimulationVisStep;
                if (isCancelled) return;

                setStepData(data);


                if (data.action === 'addVehicle' && data.addedVehicle) {
                    setLog((prev) => [...prev, `➕ Dodano: ${data.addedVehicle}`]);
                }

                if (data.action === 'step') {
                    if (data.leftVehicles.length > 0) {
                        setLog((prev) => [...prev, `🚦 Przejechali: ${data.vehicleDirections.join(', ')}`]);
                    } else {
                        setLog((prev) => [...prev, '🚦 Nikt nie przejechał']);
                    }
                }

                if (!isCancelled && isRunning) {
                    setTimeout(() => fetchSteps(stepNumber + 1), 1000 / speed);
                }

            } catch (err) {
                console.error('❌ Błąd pobierania lub parsowania JSON-a:', err);
                setIsRunning(false);
            }
        };

        fetchSteps(1);

        return () => {
            isCancelled = true;
        };
    }, [isRunning, speed]);

    const handleStart = () => {
        setStepData(null);
        setLog([]);
        setIsRunning(true);
    };

    return (
        <div className="bg-gray-900 text-white p-6 rounded shadow space-y-6">

            {/* Suwak */}
            <div className="flex flex-col">
                <label className="text-sm mb-1">
                    Prędkość wizualizacji: <span className="text-green-400">{speed.toFixed(1)}</span> kroków/s
                </label>
                <input
                    type="range"
                    min={0.1}
                    max={3}
                    step={0.1}
                    value={speed}
                    onChange={(e) => setSpeed(parseFloat(e.target.value))}
                    className="w-full accent-green-500"
                />
            </div>

            {/* Przycisk Start */}
            <button
                onClick={handleStart}
                className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition w-full"
                disabled={isRunning}
            >
                {isRunning ? '⏳ Trwa wizualizacja...' : '▶️ Start wizualizacji'}
            </button>

            {/* Skrzyżowanie */}
            <div className="min-h-[150px] bg-gray-800 rounded p-4 space-y-4 border border-gray-600">
                <h2 className="text-xl font-semibold">🛣️ Skrzyżowanie</h2>



                {/* Aktualny krok */}
                {stepData && (
                    <div className="text-sm mt-4 text-gray-300 space-y-2">
                        <p><strong>🔄 Typ kroku:</strong> {stepData.action === 'addVehicle' ? 'Dodanie pojazdu' : 'Symulacja przejazdu'}</p>

                        {stepData.addedVehicle && (
                            <p><strong>➕ Dodano pojazd:</strong> {stepData.addedVehicle}</p>
                        )}

                        {stepData.leftVehicles.length > 0 && (
                            <p><strong>🚗 Przejechały:</strong> {stepData.vehicleDirections.join(', ')}</p>
                        )}

                        <p><strong>🚦 Oczekujących pojazdów:</strong> {stepData.vehiclesWaiting}</p>


                    </div>
                )}
            </div>

            {/* Historia */}
            <div className="bg-gray-800 p-4 rounded border border-gray-600 max-h-[200px] overflow-y-auto text-sm">
                <h3 className="font-semibold mb-2">📜 Log:</h3>
                {log.length === 0 ? (
                    <p className="text-gray-500">Brak danych do wyświetlenia.</p>
                ) : (
                    <ul className="list-disc pl-4 space-y-1">
                        {log.map((entry, idx) => (
                            <li key={idx}>{entry}</li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};

export default SimulationMap;
