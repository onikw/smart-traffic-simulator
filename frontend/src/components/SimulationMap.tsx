import React, { useEffect, useState } from 'react';

interface StepData {
    leftVehicles: string[];
}

const SimulationMap: React.FC = () => {
    const [step, setStep] = useState<number>(1);
    const [data, setData] = useState<StepData | null>(null);

    useEffect(() => {
        const interval = setInterval(async () => {
            try {
                const res = await fetch(`/status/step${step}.json`);
                if (res.ok) {
                    const json = await res.json();
                    setData(json);
                    setStep((prev) => prev + 1);
                } else {
                    clearInterval(interval);
                }
            } catch (err) {
                console.error('Błąd pobierania kroku:', err);
                clearInterval(interval);
            }
        }, 1000);

        return () => clearInterval(interval);
    }, [step]);

    return (
        <div className="bg-gray-900 text-white p-4 rounded shadow min-h-[150px]">
            <h2 className="text-xl font-bold mb-2">Mapka - krok {step - 1}</h2>
            {data ? (
                <>
                    <p>Pojazdy, które opuściły skrzyżowanie:</p>
                    <ul className="list-disc list-inside">
                        {data.leftVehicles.length > 0 ? (
                            data.leftVehicles.map((v) => <li key={v}>{v}</li>)
                        ) : (
                            <li>Brak</li>
                        )}
                    </ul>
                </>
            ) : (
                <p className="italic text-gray-400">Oczekiwanie na pierwszy krok...</p>
            )}
        </div>
    );
};

export default SimulationMap;
