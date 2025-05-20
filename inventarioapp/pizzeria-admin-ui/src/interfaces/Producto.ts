export interface ProductoInventario {
    productoId: number;
    nombreProducto: string;
    descripcionProducto?: string;
    currentStock: number;
    unitOfMeasure: string;
    
}