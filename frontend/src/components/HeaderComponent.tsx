import { FaTrafficLight, FaCarSide, FaMapMarkedAlt } from "react-icons/fa";

export default function HeaderComponent() {
    return (
        <header className="bg-gray-800 text-white p-6 shadow-md">
            <div className="container mx-auto flex justify-between items-center">
                {/* LEWA STRONA */}
                <div className="flex items-center space-x-4">
                    <FaTrafficLight className="text-red-400 text-4xl" />
                    <h1 className="text-3xl font-bold">Symulacja Skrzyżowania</h1>
                </div>
                {/*
                PRAWA STRONA
                <nav>
                    <ul className="flex space-x-6">
                        <li className="flex items-center">
                            <FaCarSide className="text-green-400 mr-2 text-2xl" />
                            <span className="text-lg">Pojazdy</span>
                        </li>
                        <li className="flex items-center">
                            <FaMapMarkedAlt className="text-blue-400 mr-2 text-2xl" />
                            <span className="text-lg">Mapa</span>
                        </li>
                    </ul>
                </nav> */}
            </div>
        </header>
    );
}
