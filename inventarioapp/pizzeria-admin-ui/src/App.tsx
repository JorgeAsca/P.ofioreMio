import React, { useEffect, useState, useCallback } from 'react';
import { getAllProductos, deleteProducto } from './services/productoService';
import type { Producto } from './services/productoService';
import AddProductForm from './components/AddProductForm';
import './App.css';

function App() {
  const [productos, setProductos] = useState<Producto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  
  const [showForm, setShowForm] = useState(false);
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
    fetchProductos(); // Recargar productos después de crear o editar
    setShowForm(false); // Ocultar formulario
    setEditingProductId(null); // Resetear ID de edición
  };

  const handleEdit = (productId: number) => {
    setEditingProductId(productId);
    setShowForm(true);
  };

  const handleAddNew = () => {
    setEditingProductId(null); // Asegurarse que es modo creación
    setShowForm(true);
  };

  const handleDelete = async (productId: number) => {
  if (window.confirm('¿Estás seguro de que quieres eliminar este producto?')) {
    try {
      await deleteProducto(productId); // Llama al servicio
      fetchProductos(); // Recargar productos (esto está bien)
      setError(null); // Limpiar errores anteriores
    } catch (err: any) { // Captura el error del servicio
      console.error("Error al eliminar producto (desde App.tsx):", err);
      setError(err.response?.data?.message || err.message || "Error al eliminar el producto.");
    }
  }
};

  if (loading && productos.length === 0) { // Mostrar carga solo si no hay productos aún
    return <div>Cargando productos...</div>;
  }

  return (
    <div className="App">
      <h1>Panel de Administración de Pizzería</h1>
      
      {!showForm && <button onClick={handleAddNew}>Agregar Nuevo Producto</button>}
      
      {showForm && (
        <AddProductForm 
          onProductChange={handleProductChange} 
          productoAEditarId={editingProductId}
          onCancel={() => { setShowForm(false); setEditingProductId(null); }}
        />
      )}

      <h2>Inventario de Productos</h2>
      {error && <p style={{color: 'red'}}>{error}</p>}
      {productos.length === 0 && !loading ? (
        <p>No hay productos para mostrar.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Stock Actual</th>
              <th>Unidad</th>
              <th>Imagen</th>
              <th>Acciones</th> 
            </tr>
          </thead>
          <tbody>
            {productos.map((producto) => (
              <tr key={producto.productoId}>
                <td>{producto.productoId}</td>
                <td>{producto.nombreProducto}</td>
                <td>{producto.currentStock}</td>
                <td>{producto.unitOfMeasure}</td>
                <td>
                  {producto.imageUrl ? (
                    <img src={producto.imageUrl} alt={producto.nombreProducto} width="50" onError={(e) => (e.currentTarget.style.display = 'none')} />
                  ) : (
                    'N/A'
                  )}
                </td>
                <td>
                  <button onClick={() => handleEdit(producto.productoId)}>Editar</button>
                  <button onClick={() => handleDelete(producto.productoId)} style={{ marginLeft: '5px', backgroundColor: 'salmon' }}>Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default App;