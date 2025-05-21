interface LoginResponse {
    token: string;
    rol?: string; 
    nombreUsuario?: string;
}



const API_AUTH_URL = 'http://localhost:8080/api/v1/auth'; 

export const authService = {
    login: async (nombreUsuario: string, contrasena: string): Promise<LoginResponse> => {
        console.log(`Intentando login a: ${API_AUTH_URL}/login`);
        const response = await fetch(`${API_AUTH_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ nombreUsuario, contrasena }),
        });

        if (!response.ok) {
            let errorMsg = `Error al iniciar sesión: ${response.status}`;
            try {
                const errorData = await response.json();
                errorMsg = errorData.message || errorData.error || errorMsg; 
            } catch (e) {
                try {
                    const textError = await response.text();
                    if (textError) errorMsg = textError;
                } catch (textErr) { }
            }
            console.error("Error en authService.login:", errorMsg);
            throw new Error(errorMsg);
        }
        return response.json() as Promise<LoginResponse>;
    },

    
    
    getToken: (): string | null => {
        return localStorage.getItem('userToken');
    }
};
