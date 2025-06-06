import React, { useState } from 'react';
import { FaFileAlt } from 'react-icons/fa';

const SimulationViewer: React.FC = () => {
    const [file, setFile] = useState<File | null>(null);
    const [message, setMessage] = useState<string>('');

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setFile(e.target.files[0]);
            setMessage('');
        }
    };

    const handleUpload = async () => {
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            const res = await fetch('/api/simulate', {
                method: 'POST',
                body: formData,
            });

            if (!res.ok) throw new Error('Błąd podczas wysyłania pliku');

            setMessage('✅ Symulacja zakończona pomyślnie!');
        } catch (error) {
            console.error(error);
            setMessage('❌ Wystąpił błąd podczas symulacji.');
        }
    };

    return (
        <div className="space-y-6">

            {/* Input pliku */}
            <div className="flex flex-col space-y-2">
                <label className="block text-sm font-medium text-gray-200">Plik wejściowy JSON:</label>
                <div className="flex items-center gap-4">
                    <label className="cursor-pointer bg-gray-200 hover:bg-gray-300 text-sm text-gray-800 font-medium py-2 px-4 rounded-lg transition">
                        Wybierz plik
                        <input
                            type="file"
                            accept=".json"
                            onChange={handleFileChange}
                            className="hidden"
                        />
                    </label>

                    {file && (
                        <span className="flex items-center text-sm text-white">
                            <FaFileAlt className="mr-2 text-green-400" />
                            {file.name}
                        </span>
                    )}
                </div>
            </div>


            {/* Przycisk startu */}
            <button
                onClick={handleUpload}
                disabled={!file}
                className="bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded w-full transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
                Rozpocznij Symulację
            </button>

            {/* Komunikat */}
            {message && <p className="text-sm font-semibold">{message}</p>}
        </div>
    );
};

export default SimulationViewer;
