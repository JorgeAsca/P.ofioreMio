import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1/categorias'; 



export interface CategoriaDTO {
    categoryId: number;
    categoryName: string;
    
}

export interface SubcategoriaDTO {
    subcategoryId: number;
    subcategoryName: string; 
    
}


export interface CreateCategoriaDTO {
    categoryName: string;
}


export interface CreateSubcategoriaDTO {
    subcategoryName: string;
}


export const getAllCategorias = async (): Promise<CategoriaDTO[]> => {
    try {
        const response = await axios.get<CategoriaDTO[]>(API_BASE_URL);
        return response.data;
    } catch (error) {
        console.error("Error al obtener todas las categorías:", error);
        throw error;
    }
};

export const getCategoriaById = async (id: number): Promise<CategoriaDTO> => {
    try {
        const response = await axios.get<CategoriaDTO>(`<span class="math-inline">\{API\_BASE\_URL\}/</span>{id}`);
        return response.data;
    } catch (error) {
        console.error(`Error al obtener la categoría con ID ${id}:`, error);
        throw error;
    }
};

export const createCategoria = async (categoriaData: CreateCategoriaDTO): Promise<CategoriaDTO> => {
    try {
        const response = await axios.post<CategoriaDTO>(API_BASE_URL, categoriaData);
        return response.data;
    } catch (error) {
        console.error("Error al crear la categoría:", error);
        throw error;
    }
};



export const getSubcategoriasByCategoriaId = async (categoriaId: number): Promise<SubcategoriaDTO[]> => {
    try {
        const response = await axios.get<SubcategoriaDTO[]>(`${API_BASE_URL}/${categoriaId}/subcategorias`);
        console.log("Respuesta de subcategorías API:", response.data); 
        if (Array.isArray(response.data)) {
            return response.data;
        } else {
            console.warn(`API para subcategorías de ID ${categoriaId} no devolvió un array. Recibido:`, response.data);
            return []; 
        }
    } catch (error: any) {
        console.error(`Error al obtener subcategorías para la categoría ID ${categoriaId}:`, error);
        if (error.response) {
            
            console.error("Datos del error:", error.response.data);
            console.error("Estado del error:", error.response.status);
            console.error("Cabeceras del error:", error.response.headers);
        } else if (error.request) {
            
            console.error("Error en la solicitud (no hubo respuesta):", error.request);
        } else {
            
            console.error('Error', error.message);
        }
        return []; 
    }
};
export const createSubcategoriaForCategoria = async (categoriaId: number, subcategoriaData: CreateSubcategoriaDTO): Promise<SubcategoriaDTO> => {
    try {
        const response = await axios.post<SubcategoriaDTO>(`<span class="math-inline">\{API\_BASE\_URL\}/</span>{categoriaId}/subcategorias`, subcategoriaData);
        return response.data;
    } catch (error) {
        console.error(`Error al crear subcategoría para la categoría ID ${categoriaId}:`, error);
        throw error;
    }
};