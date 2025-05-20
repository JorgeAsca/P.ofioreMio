// src/App.tsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import ProductosPage from './pages/ProductosPage';
import PizzasPage from './pages/PizzasPage';
import VentasPage from './pages/VentasPage';
import './App.css'; 
const Navbar: React.FC = () => {
    return (
        <nav className="bottom-navbar"> 
            <NavLink to="/productos">
                
                <span className="icon-productos">📦</span> 
                Productos
            </NavLink>
            <NavLink to="/pizzas">
                <span className="icon-pizzas">🍕</span> 
                Pizzas
            </NavLink>
            <NavLink to="/ventas">
                <span className="icon-ventas">🛒</span> 
                Ventas
            </NavLink>
        </nav>
    );
};


function App() {
  return (
    <Router>
      <div className="app-container"> 
        <header className="app-header"> 
          <h1>Inventario y control </h1>
        </header>

        <main className="app-content"> 
          <Routes>
            <Route path="/" element={<ProductosPage />} /> 
            <Route path="/productos" element={<ProductosPage />} />
            <Route path="/pizzas" element={<PizzasPage />} />
            <Route path="/ventas" element={<VentasPage />} />
            
          </Routes>
        </main>

        <Navbar /> 
      </div>
    </Router>
  );
}

export default App;