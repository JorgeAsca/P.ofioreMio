import React, { createContext, useState, useContext, useEffect, type ReactNode } from 'react';

interface AuthContextType {
    isAuthenticated: boolean;
    userToken: string | null;
    
    login: (token: string) => void; 
    logout: () => void;
    isLoading: boolean; 
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [userToken, setUserToken] = useState<string | null>(null);
    
    const [isLoading, setIsLoading] = useState<boolean>(true); 

    useEffect(() => {
        const storedToken = localStorage.getItem('userToken');
        
        console.log("AuthContext useEffect: Leyendo de localStorage -> storedToken:", storedToken);

        if (storedToken) { 
            setUserToken(storedToken);
            setIsAuthenticated(true);
            console.log("AuthContext useEffect: Sesión restaurada de localStorage.");
        } else {
            console.log("AuthContext useEffect: No se encontró token válido en localStorage.");
            localStorage.removeItem('userToken'); 
        }
        setIsLoading(false); 
    }, []);

    const login = (token: string) => { 
        console.log("AuthContext: Función login llamada con token:", token);
        localStorage.setItem('userToken', token);
        
        setUserToken(token);
        
        setIsAuthenticated(true);
        console.log("AuthContext: Estado después de login -> isAuthenticated:", true);
    };

    const logout = () => {
        console.log("AuthContext: Función logout llamada");
        localStorage.removeItem('userToken');
        
        setUserToken(null);
        
        setIsAuthenticated(false);
    };

   
    return (
        <AuthContext.Provider value={{ isAuthenticated, userToken, login, logout, isLoading }}>
            {!isLoading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
