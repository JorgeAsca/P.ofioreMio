import React, { useState, useEffect, useCallback } from 'react';
import { getAllProductos, deleteProducto, type Producto } from '../services/productoService'; 
import AddProductForm from '../components/AddProductForm';
import Modal from '../components/Modal';
import '../styles/ProductosPage.css'; 


const ProductosPage: React.FC = () => {
    const [productos, setProductos] = useState<Producto[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [isAddProductModalOpen, setIsAddProductModalOpen] = useState<boolean>(false);
    const [editingProductId, setEditingProductId] = useState<number | null>(null);

    const fetchProductos = useCallback(async () => {
        try {
            setLoading(true);
            const data = await getAllProductos();
            setProductos(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || "Error al cargar los productos.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchProductos();
    }, [fetchProductos]);

    const handleProductChange = () => {
        fetchProductos();
        setIsAddProductModalOpen(false);
        setEditingProductId(null);
    };

    const handleEdit = (productId: number) => {
        setEditingProductId(productId);
        setIsAddProductModalOpen(true);
    };

    const handleAddNew = () => {
        setEditingProductId(null);
        setIsAddProductModalOpen(true);
    };

    const closeModal = () => {
        setIsAddProductModalOpen(false);
        setEditingProductId(null);
    };

    const handleDelete = async (productId: number) => {
        if (window.confirm('¿Estás seguro de que quieres eliminar este producto?')) {
            try {
                await deleteProducto(productId);
                fetchProductos();
                setError(null);
            } catch (err: any) {
                console.error("Error al eliminar producto:", err);
                setError(err.response?.data?.message || err.message || "Error al eliminar el producto.");
            }
        }
    };

    
    return (
        <div className="productos-page">
            <div className="page-header">
                <h2>Productos</h2>
                
                <button onClick={handleAddNew} className="button-add">
                    <span className="icon-plus">+</span> Añadir
                </button>
            </div>

            
            <Modal
                isOpen={isAddProductModalOpen}
                onClose={closeModal}
                title={editingProductId ? "Editar Producto" : "Añadir Nuevo Producto"}
            >
                <AddProductForm
                    onProductChange={handleProductChange} 
                    productoAEditarId={editingProductId}
                    onCancel={closeModal} 
                />
            </Modal>

            {error && <p style={{ color: 'red' }}>{error}</p>}
            
            {!loading && productos.length > 0 && (
                 <div className="product-list">
                    {productos.map((producto) => (
                        <div key={producto.productoId} className="product-card">
                            <div className="product-card-main-info">
                                <h3>{producto.nombreProducto}</h3>
                                <p className="product-category-price">
                                    <span>{producto.categoria?.categoryName || 'Ingredientes'}</span>
                                </p>
                                <span className="stock-badge">
                                    Stock: {producto.currentStock} {producto.unitOfMeasure}
                                </span>
                            </div>
                            <div className="product-actions">
                                <button className="action-button edit-button" onClick={() => handleEdit(producto.productoId)}>
                                    ✏️
                                </button>
                                <button className="action-button delete-button" onClick={() => handleDelete(producto.productoId)}>
                                    🗑️
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
            {loading && productos.length === 0 && <p>Cargando productos...</p>}
            {!loading && productos.length === 0 && !error && (<p>No hay productos para mostrar.</p>)}
        </div>
    );
};

export default ProductosPage;