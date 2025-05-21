// src/pages/RegisterPage.tsx
import React, { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../../services/authService'; // Asegúrate que la ruta es correcta
import '../styles/AuthPage.css';

const RegisterPage: React.FC = () => {
    const [nombreUsuario, setNombreUsuario] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    // El rol se sigue enviando al backend si tu API de registro lo necesita
    const rolParaBackend = 'EMPLEADO'; 
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const navigate = useNavigate();

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (password !== confirmPassword) {
            setError("Las contraseñas no coinciden.");
            return;
        }
        if (password.length < 6) {
            setError("La contraseña debe tener al menos 6 caracteres.");
            return;
        }
        setLoading(true);
        setError(null);

        try {
            await authService.register({ 
                nombreUsuario, 
                contrasena: password, 
                rol: rolParaBackend 
            });
            
            alert("¡Registro exitoso! Ahora puedes iniciar sesión.");
            navigate('/login'); 

        } catch (err: any) {
            setError(err.message || "Error al registrar el usuario. Inténtalo de nuevo.");
            console.error("Error de registro:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-page-container">
            <div className="auth-form-card">
                <div className="auth-logo-container">
                    <div className="auth-logo-placeholder">LOGO</div>
                </div>
                <h2>Registrar Nuevo Usuario</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="reg-username">Nombre de Usuario:</label>
                        <input
                            type="text"
                            id="reg-username"
                            value={nombreUsuario}
                            onChange={(e) => setNombreUsuario(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="reg-password">Contraseña:</label>
                        <input
                            type="password"
                            id="reg-password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="reg-confirm-password">Confirmar Contraseña:</label>
                        <input
                            type="password"
                            id="reg-confirm-password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>
                    {error && <p className="error-message auth-error">{error}</p>}
                    <button type="submit" className="auth-button" disabled={loading}>
                        {loading ? 'Registrando...' : 'Registrar'}
                    </button>
                </form>
                 <div className="auth-links">
                    <p>¿Ya tienes cuenta? <Link to="/login">Inicia sesión aquí</Link></p>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;
