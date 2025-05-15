
import React, { useState, useEffect } from 'react';

import { type CreateProductoDTO, type UpdateProductoDTO, createProducto, getProductoById, updateProducto } from '../services/productoService';
import {
  getAllCategorias,
  getSubcategoriasByCategoriaId,
  type CategoriaDTO, 
  type SubcategoriaDTO 

} from '../services/categoriaService';
// Asumiendo que tienes este servicio y DTOs

interface AddProductFormProps {
  onProductChange: () => void; 
  productoAEditarId?: number | null; // ID del producto a editar, si es null o undefined, es modo creación
  onCancel: () => void; // Para cerrar el formulario
}

const AddProductForm: React.FC<AddProductFormProps> = ({ onProductChange, productoAEditarId, onCancel }) => {
  const [isEditMode, setIsEditMode] = useState(false);

  // Estados para los campos del formulario
  const [nombreProducto, setNombreProducto] = useState('');
  const [descripcionProducto, setDescripcionProducto] = useState('');
  const [currentStock, setCurrentStock] = useState('0');
  const [unitOfMeasure, setUnitOfMeasure] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [minStockAlert, setMinStockAlert] = useState('0');
  

  const [categorias, setCategorias] = useState<CategoriaDTO[]>([]);
  const [selectedCategoriaId, setSelectedCategoriaId] = useState<string>('');

  const [subcategorias, setSubcategorias] = useState<SubcategoriaDTO[]>([]);
  const [selectedSubcategoriaId, setSelectedSubcategoriaId] = useState<string>('');
  const [loadingSubcategorias, setLoadingSubcategorias] = useState<boolean>(false);

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  // Cargar datos del producto si estamos en modo edición
  useEffect(() => {
    if (productoAEditarId) {
      setIsEditMode(true);
      const fetchProductoData = async () => {
        try {
          const producto = await getProductoById(productoAEditarId);
          setNombreProducto(producto.nombreProducto || ''); // Valor por defecto si es null/undefined
          setDescripcionProducto(producto.descripcionProducto || '');
          setCurrentStock(producto.currentStock !== null && producto.currentStock !== undefined ? producto.currentStock.toString() : '0');
          setUnitOfMeasure(producto.unitOfMeasure || '');
          setImageUrl(producto.imageUrl || '');
          setMinStockAlert(producto.minStockAlert !== null && producto.minStockAlert !== undefined ? producto.minStockAlert.toString() : '0');

          if (producto.categoria && producto.categoria.categoryId !== null && producto.categoria.categoryId !== undefined) {
            setSelectedCategoriaId(producto.categoria.categoryId.toString());
          } else {
            setSelectedCategoriaId('');

            if (producto.subcategoria && producto.subcategoria.subcategoryId) {

              if (producto.categoria.categoryId) {
                const subcats = await getSubcategoriasByCategoriaId(producto.categoria.categoryId);
                setSubcategorias(subcats); // Carga las subcategorías de la categoría del producto
                if (producto.subcategoria && producto.subcategoria.subcategoryId) {
                  setSelectedSubcategoriaId(producto.subcategoria.subcategoryId.toString());
                }
              }
            }
          }

        } catch (error) {
          console.error("Error cargando datos del producto para editar:", error);
          setFormError("No se pudieron cargar los datos del producto.");
        }
      };
      fetchProductoData();
    } else {
      setIsEditMode(false);
      // Resetear formulario si no es modo edición (o al cambiar de editar a crear)
      setNombreProducto(''); setDescripcionProducto(''); setCurrentStock('0');
      setUnitOfMeasure(''); setImageUrl(''); setMinStockAlert('0');
      setSelectedCategoriaId(''); setSelectedSubcategoriaId('');
    }
  }, [productoAEditarId]); // Se ejecuta cuando productoAEditarId cambia


  // Cargar categorías al montar el componente (solo una vez)
  useEffect(() => {
    const fetchCategorias = async () => {
      try {
        const data = await getAllCategorias();
        setCategorias(data);
      } catch (error) {
        console.error("Error cargando categorías:", error);
        setFormError("No se pudieron cargar las categorías.");
      }
    };
    fetchCategorias();
  }, []);

  // Cargar subcategorías cuando cambie la categoría seleccionada
  useEffect(() => {
    // No limpiar selectedSubcategoriaId si estamos en modo edición y ya tiene un valor
    // hasta que las nuevas subcategorías se carguen.
    const currentSubcatSelection = selectedSubcategoriaId;

    if (selectedCategoriaId && selectedCategoriaId !== "") {
      const fetchSubcategorias = async () => {
        setLoadingSubcategorias(true);
        setSubcategorias([]); // Limpiar antes de cargar nuevas
        
        setSelectedSubcategoriaId('');


        try {
          const data = await getSubcategoriasByCategoriaId(parseInt(selectedCategoriaId));
          setSubcategorias(data);
          // Si estábamos editando y la subcategoría original pertenece a esta nueva lista de subcategorías,
          // podríamos intentar preseleccionarla. Esto requiere más lógica.
          // Por ahora, el usuario tendrá que volver a seleccionar.
        } catch (error) {
          console.error("Error cargando subcategorías:", error);
        } finally {
          setLoadingSubcategorias(false);
        }
      };
      fetchSubcategorias();
    } else {
      setSubcategorias([]);
      setSelectedSubcategoriaId('');
    }
  }, [selectedCategoriaId, isEditMode, productoAEditarId]); // Añadir isEditMode y productoAEditarId a las dependencias puede ser necesario para la lógica de preselección

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);
    setFormError(null);
    setSuccessMessage(null);

    const categoriaParaEnviar = selectedCategoriaId ? { categoryId: parseInt(selectedCategoriaId) } : undefined;
    const subcategoriaParaEnviar = selectedSubcategoriaId && subcategorias.length > 0 ? { subcategoriaId: parseInt(selectedSubcategoriaId) } : undefined;

    const payload: CreateProductoDTO | UpdateProductoDTO = { // UpdateProductoDTO es similar a CreateProductoDTO
      nombreProducto,
      descripcionProducto: descripcionProducto || undefined,
      currentStock: parseFloat(currentStock),
      unitOfMeasure,
      imageUrl: imageUrl || undefined,
      minStockAlert: minStockAlert && parseFloat(minStockAlert) > 0 ? parseFloat(minStockAlert) : undefined,
      categoria: categoriaParaEnviar,
      subcategoria: subcategoriaParaEnviar,
    };

    try {
      if (isEditMode && productoAEditarId) {
        await updateProducto(productoAEditarId, payload);
        setSuccessMessage('¡Producto actualizado con éxito!');
      } else {
        await createProducto(payload as CreateProductoDTO); // payload es compatible
        setSuccessMessage('¡Producto agregado con éxito!');
      }
      onProductChange(); // Notificar al padre para que refresque la lista
      if (!isEditMode) { // Solo limpiar si no estamos editando (o el usuario podría querer seguir editando)
        setNombreProducto(''); setDescripcionProducto(''); setCurrentStock('0');
        setUnitOfMeasure(''); setImageUrl(''); setMinStockAlert('0');
        setSelectedCategoriaId(''); // setSelectedSubcategoriaId se limpia por el efecto
      } else {
        onCancel(); // Cerrar formulario después de editar
      }
    } catch (error: any) {
      console.error("Error al guardar producto:", error);
      setFormError(error.response?.data?.message || error.message || 'Ocurrió un error al guardar el producto.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>{isEditMode ? 'Editar Producto' : 'Agregar Nuevo Producto'}</h3>
      {/* ... (mensajes de error y éxito) ... */}
      {/* ... (todos los campos del formulario como los tenías) ... */}
      {/* Ejemplo de un campo: */}
      <div>
        <label htmlFor="nombreProducto">Nombre del Producto:</label>
        <input type="text" value={nombreProducto || ''} onChange={(e) => setNombreProducto(e.target.value)} required />
      </div>
      {/* ... (campos: descripcionProducto, currentStock, unitOfMeasure, imageUrl, minStockAlert) ... */}
      <div>
        <label htmlFor="currentStock">Stock Actual:</label>
        <input type="number" value={currentStock || '0'} onChange={(e) => setCurrentStock(e.target.value)} required step="0.01" min="0" />
      </div>
      <div>
        <label htmlFor="unitOfMeasure">Unidad de Medida:</label>
        <input type="text" id="unitOfMeasure" value={unitOfMeasure} onChange={(e) => setUnitOfMeasure(e.target.value)} required />
      </div>
      <div>
        <label htmlFor="imageUrl">URL de Imagen:</label>
        <input type="url" id="imageUrl" value={imageUrl} onChange={(e) => setImageUrl(e.target.value)} />
      </div>
      <div>
        <label htmlFor="minStockAlert">Alerta Mínima de Stock:</label>
        <input type="number" id="minStockAlert" value={minStockAlert} onChange={(e) => setMinStockAlert(e.target.value)} step="0.01" min="0" />
      </div>
      <div>
        <label htmlFor="categoria">Categoría:</label>
        <select value={selectedCategoriaId || ''} onChange={(e) => setSelectedCategoriaId(e.target.value)}>
          <option value="">-- Selecciona una Categoría --</option>
          {categorias.map(cat => (
            <option key={cat.categoryId} value={cat.categoryId}>{cat.categoryName}</option>
          ))}
        </select>
      </div>

      {loadingSubcategorias && <p>Cargando subcategorías...</p>}
      {!loadingSubcategorias && subcategorias.length > 0 && (
        <div>
          <label htmlFor="subcategoria">Subcategoría:</label>
          <select id="subcategoria" value={selectedSubcategoriaId} onChange={(e) => setSelectedSubcategoriaId(e.target.value)}>
            <option value="">-- Selecciona una Subcategoría --</option>
            {subcategorias.map(subcat => (
              <option key={subcat.subcategoryId} value={subcat.subcategoryId}>{subcat.subcategoryName}</option>
            ))}
          </select>
        </div>
      )}
      <br />
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Guardando...' : (isEditMode ? 'Actualizar Producto' : 'Agregar Producto')}
      </button>
      <button type="button" onClick={onCancel} style={{ marginLeft: '10px' }}>
        Cancelar
      </button>
    </form>
  );
};

export default AddProductForm;