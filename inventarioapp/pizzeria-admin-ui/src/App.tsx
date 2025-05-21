import React, { type JSX } from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink, Navigate, useNavigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext'; 

import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage'; 
import ProductosPage from './pages/ProductosPage';
import PizzasPage from './pages/PizzasPage';
import VentasPage from './pages/VentasPage';

import './App.css'; 

const Navbar: React.FC = () => {
    const { isAuthenticated, logout } = useAuth();
    const navigate = useNavigate(); // Para redirigir en logout

    const handleLogout = () => {
        logout();
        navigate('/login'); // Redirigir a login después de cerrar sesión
    };

    if (!isAuthenticated) { // No mostrar Navbar si no está autenticado
        return null;
    }

    return (
        <nav className="bottom-navbar"> 
            <NavLink to="/productos" className={({ isActive }) => isActive ? "active" : ""}>
                <span className="icon-productos">📦</span> 
                Productos
            </NavLink>
            <NavLink to="/pizzas" className={({ isActive }) => isActive ? "active" : ""}>
                <span className="icon-pizzas">🍕</span> 
                Pizzas
            </NavLink>
            <NavLink to="/ventas" className={({ isActive }) => isActive ? "active" : ""}>
                <span className="icon-ventas">🛒</span> 
                Ventas
            </NavLink>
            {isAuthenticated && ( 
                <button 
                    onClick={handleLogout} 
                    style={{ 
                        background: 'none', border: 'none', color: '#757575', 
                        cursor: 'pointer', display: 'flex', flexDirection: 'column', 
                        alignItems: 'center', fontFamily: 'inherit', fontSize: 'inherit' 
                    }}
                    title="Salir"
                >
                    <span className="icon-logout" style={{ fontSize: '24px', marginBottom: '4px' }}>🚪</span> 
                    Salir
                </button>
            )}
        </nav>
    );
};

const AppLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { isAuthenticated } = useAuth();
    if (!isAuthenticated) return null; // No renderizar layout si no está autenticado

    return (
        <div className="app-layout-container"> 
            <header className="app-header">
                <h1>Inventario y Control</h1>
            </header>
            <main className="app-content">
                {children}
            </main>
            <Navbar /> 
        </div>
    );
};

const ProtectedRoute: React.FC<{ children: JSX.Element }> = ({ children }) => { // Ya no necesita allowedRoles
    const { isAuthenticated, isLoading } = useAuth(); 

    if (isLoading) {
        return <div>Cargando sesión...</div>; 
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    return children; 
};

const RootRedirector: React.FC = () => {
    const { isAuthenticated, isLoading } = useAuth();

    if (isLoading) {
        return <div>Cargando sesión...</div>;
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    // Si está autenticado, redirige a una página por defecto, ej. /productos
    return <Navigate to="/productos" replace />; 
};

function App() {
 return (
    <AuthProvider>
        <Router>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/" element={<RootRedirector />} />

                <Route 
                    path="/productos" 
                    element={
                        <ProtectedRoute>
                            <AppLayout>
                                <ProductosPage />
                            </AppLayout>
                        </ProtectedRoute>
                    } 
                />
                <Route 
                    path="/pizzas" 
                    element={
                        <ProtectedRoute>
                            <AppLayout>
                                <PizzasPage />
                            </AppLayout>
                        </ProtectedRoute>
                    } 
                />
                <Route 
                    path="/ventas" 
                    element={
                        <ProtectedRoute>
                            <AppLayout>
                                <VentasPage />
                            </AppLayout>
                        </ProtectedRoute>
                    } 
                />
                
                <Route path="*" element={ <Navigate to="/" replace /> } />
            </Routes>
        </Router>
    </AuthProvider>
 );
}

export default App;
