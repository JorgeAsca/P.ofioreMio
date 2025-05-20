
import React, { useState, useEffect, type FormEvent } from 'react';
import { type IngredienteRecetaForm, type MenuPayload, type MenuResponse, type MenuItemIngredientePayload } from '../interfaces/Menu';
import { type ProductoInventario } from '../interfaces/Producto';
const CrearPizzaForm: React.FC = () => {
    const [itemName, setItemName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [salePrice, setSalePrice] = useState<string>(''); 
    const [isAvailable, setIsAvailable] = useState<boolean>(true);
    const [ingredients, setIngredients] = useState<IngredienteRecetaForm[]>([]);

    const [allProducts, setAllProducts] = useState<ProductoInventario[]>([]);
    const [selectedProductId, setSelectedProductId] = useState<string>(''); 
    const [quantityRequired, setQuantityRequired] = useState<string>('');
    const [unitRequired, setUnitRequired] = useState<string>('');

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/v1/productos');
                if (!response.ok) {
                    throw new Error('Error al cargar productos del inventario');
                }
                const data: ProductoInventario[] = await response.json();
                setAllProducts(data);
                if (data.length > 0) {
                    setSelectedProductId(data[0].productoId.toString());
                }
            } catch (error) {
                console.error("Error fetching products:", error);
                
            }
        };
        fetchProducts();
    }, []);

    const handleAddIngredient = () => {
        if (!selectedProductId || !quantityRequired || !unitRequired) {
            alert("Por favor, completa todos los campos del ingrediente.");
            return;
        }
        const product = allProducts.find(p => p.productoId.toString() === selectedProductId);
        if (product) {
            setIngredients(prevIngredients => [
                ...prevIngredients,
                {
                    productoId: parseInt(selectedProductId, 10),
                    quantityRequired: parseFloat(quantityRequired),
                    unitRequired: unitRequired,
                    nombreProducto: product.nombreProducto
                }
            ]);
            setQuantityRequired('');
            setUnitRequired('');
            
        }
    };

    const handleRemoveIngredient = (indexToRemove: number) => {
        setIngredients(prevIngredients => prevIngredients.filter((_, index) => index !== indexToRemove));
    };

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const ingredientsPayload: MenuItemIngredientePayload[] = ingredients.map(ing => ({
            productoId: ing.productoId,
            quantityRequired: ing.quantityRequired,
            unitRequired: ing.unitRequired
        }));

        const menuData: MenuPayload = {
            itemName,
            description,
            salePrice: parseFloat(salePrice),
            isAvailable,
            ingredients: ingredientsPayload
        };
        
        console.log("Enviando MenuPayload:", JSON.stringify(menuData, null, 2));

        try {
            const response = await fetch('http://localhost:8080/api/v1/menu', { //
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(menuData),
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Error al crear pizza: ${response.status} - ${errorText}`);
            }

            const result: MenuResponse = await response.json();
            alert(`Pizza "${result.itemName}" creada con éxito! ID: ${result.menuItemId}`);
            // Limpiar formulario
            setItemName('');
            setDescription('');
            setSalePrice('');
            setIsAvailable(true);
            setIngredients([]);
        } catch (error) {
            console.error("Error al crear pizza:", error);
            if (error instanceof Error) {
                alert(`Error al crear pizza: ${error.message}`);
            } else {
                alert('Ocurrió un error desconocido al crear la pizza.');
            }
        }
    };

    return (
        <div>
            <h2>Crear Nueva Pizza (Ítem de Menú)</h2>
            <form onSubmit={handleSubmit}>
                
                <div>
                    <label htmlFor="itemName">Nombre de la Pizza:</label>
                    <input id="itemName" type="text" value={itemName} onChange={e => setItemName(e.target.value)} required />
                </div>
                
                <div>
                    <label htmlFor="description">Descripción:</label>
                    <textarea id="description" value={description} onChange={e => setDescription(e.target.value)} />
                </div>
                
                <div>
                    <label htmlFor="salePrice">Precio de Venta:</label>
                    <input id="salePrice" type="number" step="0.01" value={salePrice} onChange={e => setSalePrice(e.target.value)} required />
                </div>
                
                <div>
                    <label htmlFor="isAvailable">
                        <input id="isAvailable" type="checkbox" checked={isAvailable} onChange={e => setIsAvailable(e.target.checked)} />
                        Disponible
                    </label>
                </div>

                <hr />
                <h3>Receta de la Pizza</h3>
                <fieldset>
                    <legend>Añadir Ingrediente</legend>
                    <div>
                        <label htmlFor="productSelect">Producto:</label>
                        <select id="productSelect" value={selectedProductId} onChange={e => setSelectedProductId(e.target.value)}>
                            <option value="">-- Selecciona un producto --</option>
                            {allProducts.map(product => (
                                <option key={product.productoId} value={product.productoId.toString()}>
                                    {product.nombreProducto} ({product.unitOfMeasure})
                                </option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <label htmlFor="quantityRequired">Cantidad:</label>
                        <input
                            id="quantityRequired"
                            type="number"
                            step="0.001" 
                            placeholder="Cantidad"
                            value={quantityRequired}
                            onChange={e => setQuantityRequired(e.target.value)}
                        />
                    </div>
                    <div>
                        <label htmlFor="unitRequired">Unidad:</label>
                        <input
                            id="unitRequired"
                            type="text"
                            placeholder="Unidad (ej. kg, gr, unidad)"
                            value={unitRequired}
                            onChange={e => setUnitRequired(e.target.value)}
                        />
                    </div>
                    <button type="button" onClick={handleAddIngredient}>Añadir Ingrediente</button>
                </fieldset>

                <h4>Ingredientes de la Receta Actual:</h4>
                {ingredients.length === 0 ? <p>No hay ingredientes añadidos.</p> : (
                    <ul>
                        {ingredients.map((ing, index) => (
                            <li key={index}>
                                {ing.nombreProducto || `ID Prod: ${ing.productoId}`}: {ing.quantityRequired} {ing.unitRequired}
                                <button type="button" onClick={() => handleRemoveIngredient(index)} style={{ marginLeft: '10px' }}>
                                    Eliminar
                                </button>
                            </li>
                        ))}
                    </ul>
                )}
                <hr />
                <button type="submit">Guardar Pizza</button>
            </form>
        </div>
    );
};

export default CrearPizzaForm;