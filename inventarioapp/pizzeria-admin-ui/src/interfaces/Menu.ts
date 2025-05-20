export interface IngredienteRecetaForm { // Para el estado local del formulario de ingredientes
    productoId: number;
    quantityRequired: number;
    unitRequired: string;
    nombreProducto?: string; 
}

export interface MenuItemIngredientePayload { // Para enviar al backend
    productoId: number;
    quantityRequired: number;
    unitRequired: string;
}

export interface MenuPayload { // DTO para crear un nuevo ítem de menú
    itemName: string;
    description?: string;
    salePrice: number;
    isAvailable: boolean;
    ingredients: MenuItemIngredientePayload[];
}

export interface MenuResponse extends MenuPayload { // Respuesta esperada del backend
    menuItemId: number;
    createdAt: string; 
}