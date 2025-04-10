import { FaTrafficLight } from "react-icons/fa";

export default function HeaderComponent() {
    return (
        <header className="bg-gray-800 text-white p-6 shadow-md">
            <div className="container mx-auto flex justify-between items-center">
                <div className="flex items-center space-x-4">
                    <FaTrafficLight className="text-red-400 text-4xl" />
                    <h1 className="text-3xl font-bold">Symulacja Skrzyżowania</h1>
                </div>

            </div>
        </header>
    );
}
