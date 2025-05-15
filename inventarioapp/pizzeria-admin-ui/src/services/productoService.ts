import axios from "axios";



const API_URL = "http://localhost:8080/api/v1/productos";

//Definimos una interfaz para la estructura de los productos(DTO)
export interface Producto {
    productoId: number;
    nombreProducto: string;
    descripcionProducto?: string; 
    currentStock: number;
    unitOfMeasure: string;
    imageUrl?: string;
    minStockAlert?: number;
    createdAt?: string; // Las fechas pueden ser string en formato ISO
    updatedAt?: string;
    categoria?: any;
    subcategoria?: any;
}
// Interfaz para el DTO de creación de producto (sin IDs generados por el backend)
export interface CreateProductoDTO {
    nombreProducto: string;
    descripcionProducto?: string;
    currentStock: number;
    unitOfMeasure: string;
    imageUrl?: string;
    minStockAlert?: number;
    categoria?: { categoryId: number };
    subcategoria?: { subcategoriaId: number }; 
}

export interface UpdateProductoDTO {
    nombreProducto?: string;
    descripcionProducto?: string;
    currentStock?: number;
    unitOfMeasure?: string;
    imageUrl?: string;
    minStockAlert?: number;
    categoria?: { categoryId: number };
    subcategoria?: { subcategoriaId: number };
}




// Función para obtener todos los productos
export const getAllProductos = async (): Promise<Producto[]> => {
    try {
        const response = await axios.get<Producto[]>(API_URL);
        return response.data;
    } catch (error) {
        console.error("Error al obtener todos los productos:", error);
        
        throw error;
    }
};

// Función para obtener un producto por ID
export const getProductoById = async (id: number): Promise<Producto> => {
    try {
        const response = await axios.get<Producto>(`<span class="math-inline">\{API\_URL\}/</span>{id}`);
        
        return response.data;
    } catch (error) {
        console.error(`Error al obtener el producto con ID ${id}:`, error);
        throw error;
    }
};

// Función para crear un nuevo producto
export const createProducto = async (productoData: CreateProductoDTO): Promise<Producto> => {
    try {
        const response = await axios.post<Producto>(API_URL, productoData);
        return response.data;
    } catch (error) {
        console.error("Error al crear el producto:", error);
        throw error;
    }
};

// Función para actualizar un producto
export const updateProducto = async (id: number, productoData: UpdateProductoDTO): Promise<Producto> => {
    try {
        
        const urlToUpdate = `${API_URL}/${id}`; 
      
        
        console.log('URL para PUT (desde productoService.ts):', urlToUpdate); // Para depurar
        
        const response = await axios.put<Producto>(urlToUpdate, productoData);
        return response.data;
    } catch (error) {
        console.error(`Error al actualizar el producto con ID ${id}:`, error);
        throw error;
    }
};

// Función para eliminar un producto
export const deleteProducto = async (id: number): Promise<void> => {
    try {
        // Asegúrate de que la URL se construye así:
        const urlToDelete = `${API_URL}/${id}`; 
        
        
        console.log('URL para DELETE:', urlToDelete); 
        
        await axios.delete(urlToDelete);
    } catch (error) {
        console.error(`Error al eliminar el producto con ID ${id}:`, error);
        throw error;
    }
};
