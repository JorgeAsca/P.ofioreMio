// src/pages/LoginPage.tsx
import React, { useState, type FormEvent, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext'; 
import { authService } from '../../services/authService'; 
import '../styles/AuthPage.css'; 

const LoginPage: React.FC = () => {
    const [nombreUsuario, setNombreUsuario] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    
    // Ya no necesitamos userRole aquí para la lógica de redirección inmediata
    const { login, isAuthenticated, isLoading: authContextIsLoading } = useAuth(); 
    const navigate = useNavigate();

    // Efecto para redirigir después de que isAuthenticated cambie a true
    useEffect(() => {
        if (!authContextIsLoading && isAuthenticated) {
            console.log("LoginPage useEffect: Autenticado. Redirigiendo a /productos...");
            navigate('/productos', { replace: true }); // Redirige a una página por defecto
        }
    }, [isAuthenticated, navigate, authContextIsLoading]);

    // Efecto para redirigir si ya está autenticado al cargar la página
    useEffect(() => {
        if (!authContextIsLoading && isAuthenticated) {
            console.log("LoginPage: Usuario ya autenticado al cargar, redirigiendo a /productos...");
            navigate('/productos', { replace: true });
        }
    }, [authContextIsLoading, isAuthenticated, navigate]);


    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const responseData = await authService.login(nombreUsuario, password);
            
            // El backend podría seguir enviando 'rol', pero aquí solo nos importa el token
            if (responseData && responseData.token) {
                // Llama a la función login del AuthContext (versión simplificada que solo toma token)
                login(responseData.token); 
                console.log("LoginPage handleSubmit: Login API exitoso. Context login() llamado.");
                // La navegación ahora es manejada por el useEffect que observa 'isAuthenticated'
            } else {
                let errorMessage = "Respuesta de login inválida del servidor.";
                if (!responseData?.token) errorMessage += " Falta token.";
                console.error(errorMessage, responseData);
                throw new Error(errorMessage);
            }
        } catch (err: any) {
            setError(err.message || "Error al iniciar sesión. Verifique sus credenciales.");
            console.error("Error de login capturado:", err);
        } finally {
            setLoading(false);
        }
    };

    if (authContextIsLoading) {
        return <div>Verificando sesión...</div>;
    }

    return (
        <div className="auth-page-container">
            <div className="auth-form-card">
                <div className="auth-logo-container">
                    <div className="auth-logo-placeholder">LOGO</div>
                </div>
                <h2>Iniciar Sesión</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="login-username">Nombre de Usuario:</label>
                        <input
                            type="text"
                            id="login-username"
                            value={nombreUsuario}
                            onChange={(e) => setNombreUsuario(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="login-password">Contraseña:</label>
                        <input
                            type="password"
                            id="login-password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>
                    {error && <p className="error-message auth-error">{error}</p>}
                    <button type="submit" className="auth-button" disabled={loading}>
                        {loading ? 'Ingresando...' : 'Ingresar'}
                    </button>
                </form>
                <div className="auth-links">
                    <p>¿No tienes cuenta? <Link to="/register">Regístrate </Link></p>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;

