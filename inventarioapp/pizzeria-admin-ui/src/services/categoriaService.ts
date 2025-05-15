// src/services/categoriaService.ts
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1/categorias'; // URL base para categorías

// Define estas interfaces según los DTOs que devuelve tu backend
// Podrías tenerlas en un archivo separado src/interfaces/dtos.ts y exportarlas/importarlas
export interface CategoriaDTO {
    categoryId: number;
    categoryName: string;
    // subcategorias?: SubcategoriaDTO[]; // Si quieres incluir subcategorías directamente aquí
}

export interface SubcategoriaDTO {
    subcategoryId: number;
    subcategoryName: string; // Asegúrate que el nombre del campo coincida con tu DTO del backend
    // categoryId?: number; // Podrías añadir esto si es útil
}

// Crear Categoria DTO (para enviar en el cuerpo de un POST/PUT a categorías)
export interface CreateCategoriaDTO {
    categoryName: string;
}

// Crear Subcategoria DTO (para enviar en el cuerpo de un POST a /categorias/{id}/subcategorias)
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

// ... (puedes añadir updateCategoria y deleteCategoria después) ...

export const getSubcategoriasByCategoriaId = async (categoriaId: number): Promise<SubcategoriaDTO[]> => {
    try {
        const response = await axios.get<SubcategoriaDTO[]>(`${API_BASE_URL}/${categoriaId}/subcategorias`);
        console.log("Respuesta de subcategorías API:", response.data); // AÑADE ESTE LOG
        if (Array.isArray(response.data)) {
            return response.data;
        } else {
            console.warn(`API para subcategorías de ID ${categoriaId} no devolvió un array. Recibido:`, response.data);
            return []; // Devuelve array vacío si la respuesta no es un array
        }
    } catch (error: any) {
        console.error(`Error al obtener subcategorías para la categoría ID ${categoriaId}:`, error);
        if (error.response) {
            // El servidor respondió con un código de estado fuera del rango 2xx
            console.error("Datos del error:", error.response.data);
            console.error("Estado del error:", error.response.status);
            console.error("Cabeceras del error:", error.response.headers);
        } else if (error.request) {
            // La solicitud se hizo pero no se recibió respuesta
            console.error("Error en la solicitud (no hubo respuesta):", error.request);
        } else {
            // Algo más causó el error
            console.error('Error', error.message);
        }
        return []; // Siempre devuelve un array vacío en caso de error en la llamada
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