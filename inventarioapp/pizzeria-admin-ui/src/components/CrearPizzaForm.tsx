
import React, { useState, useEffect, type FormEvent, type ChangeEvent } from 'react';
import { type IngredienteRecetaForm, type MenuPayload, type MenuResponse, type MenuItemIngredientePayload } from '../interfaces/Menu';
import { type ProductoInventario } from '../interfaces/Producto';

interface CrearPizzaFormProps {
    onPizzaSaved: () => void;
    onCancel: () => void;
    pizzaToEdit?: MenuResponse; 
}


async function getAllProductsAPI(): Promise<ProductoInventario[]> {
    const response = await fetch('http://localhost:8080/api/v1/productos');
    if (!response.ok) {
        throw new Error('Error al cargar productos del inventario para la receta');
    }
    return response.json();
}

const CrearPizzaForm: React.FC<CrearPizzaFormProps> = ({ onPizzaSaved, onCancel, pizzaToEdit }) => {
    const [itemName, setItemName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [salePrice, setSalePrice] = useState<string>('');
    const [isAvailable, setIsAvailable] = useState<boolean>(true);
    const [currentIngredients, setCurrentIngredients] = useState<IngredienteRecetaForm[]>([]); 

    // ... (estados para allInventoryProducts, selectedProductId, quantityRequired, unitRequired) ...
    const [allInventoryProducts, setAllInventoryProducts] = useState<ProductoInventario[]>([]);
    const [selectedProductId, setSelectedProductId] = useState<string>('');
    const [quantityRequired, setQuantityRequired] = useState<string>('');
    const [unitRequired, setUnitRequired] = useState<string>('');


    
    useEffect(() => {
        if (pizzaToEdit) {
            setItemName(pizzaToEdit.itemName || '');
            setDescription(pizzaToEdit.description || '');
            setSalePrice(pizzaToEdit.salePrice?.toString() || '');
            setIsAvailable(pizzaToEdit.isAvailable !== undefined ? pizzaToEdit.isAvailable : true);
            
            const ingredientsForForm: IngredienteRecetaForm[] = pizzaToEdit.ingredients?.map(ing => {
                const productDetails = allInventoryProducts.find(p => p.productoId === ing.productoId);
                return {
                    ...ing, 
                    nombreProducto: productDetails?.nombreProducto || `Producto ID: ${ing.productoId}`
                };
            }) || [];
            setCurrentIngredients(ingredientsForForm);
        } else {
            
            setItemName('');
            setDescription('');
            setSalePrice('');
            setIsAvailable(true);
            setCurrentIngredients([]);
        }
    }, [pizzaToEdit, allInventoryProducts]); 

    
     useEffect(() => {
        const fetchInventoryProducts = async () => {
            try {
                const data = await getAllProductsAPI();
                setAllInventoryProducts(data);
                // No preseleccionar para forzar una elección, o preseleccionar si es necesario
            } catch (error) {
                console.error("Error fetching inventory products for recipe:", error);
            }
        };
        fetchInventoryProducts();
    }, []);

    const handleAddIngredient = () => {
        if (!selectedProductId || !quantityRequired || !unitRequired) {
            alert("Por favor, completa todos los campos del ingrediente.");
            return;
        }
        const product = allInventoryProducts.find(p => p.productoId.toString() === selectedProductId);
        if (product) {
            const newIngredient: IngredienteRecetaForm = {
                productoId: parseInt(selectedProductId, 10),
                quantityRequired: parseFloat(quantityRequired),
                unitRequired: unitRequired,
                nombreProducto: product.nombreProducto
            };
            setCurrentIngredients(prevIngredients => [...prevIngredients, newIngredient]);
            setSelectedProductId('');
            setQuantityRequired('');
            setUnitRequired('');
        } else {
            alert("Producto seleccionado no encontrado.");
        }
    };

    const handleRemoveIngredient = (indexToRemove: number) => {
        setCurrentIngredients(prevIngredients => prevIngredients.filter((_, index) => index !== indexToRemove));
    };


    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const ingredientsPayload: MenuItemIngredientePayload[] = currentIngredients.map(ing => ({
            productoId: ing.productoId,
            quantityRequired: ing.quantityRequired,
            unitRequired: ing.unitRequired
        }));

        const pizzaData: MenuPayload = { 
            itemName,
            description,
            salePrice: parseFloat(salePrice),
            isAvailable,
            ingredients: ingredientsPayload,
        };

        try {
            let response;
            if (pizzaToEdit && pizzaToEdit.menuItemId) {
                
                console.log("Enviando datos para ACTUALIZAR pizza:", JSON.stringify({ ...pizzaData, menuItemId: pizzaToEdit.menuItemId }, null, 2));
                response = await fetch(`http://localhost:8080/api/v1/menu/${pizzaToEdit.menuItemId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    
                    body: JSON.stringify({ ...pizzaData, menuItemId: pizzaToEdit.menuItemId }) 
                });
            } else {
                // MODO CREACIÓN: Petición POST
                console.log("Enviando datos para CREAR pizza:", JSON.stringify(pizzaData, null, 2));
                response = await fetch('http://localhost:8080/api/v1/menu', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(pizzaData) // MenuPayload es compatible con lo que espera el backend para crear (MenuDTO sin IDs generados)
                });
            }

            if (!response.ok) {
                let errorMsg = pizzaToEdit ? 'Error al actualizar la pizza' : 'Error al guardar la pizza';
                try {
                    const errorData = await response.json();
                    errorMsg = errorData.message || errorData.error || errorMsg; 
                } catch (e) {
                    const textError = await response.text();
                    errorMsg = textError || errorMsg;
                }
                throw new Error(errorMsg);
            }

            alert(`Pizza "${pizzaData.itemName}" ${pizzaToEdit ? 'actualizada' : 'guardada'} con éxito!`);
            onPizzaSaved(); 
        } catch (error: any) {
            console.error("Error en submit de pizza:", error);
            alert(`Error: ${error.message || 'No se pudo procesar la solicitud.'}`);
        }
    };

    
    return (
        <form onSubmit={handleSubmit} className="crear-pizza-form">
           
            <div className="form-group">
                <label htmlFor="itemName-pizza">Nombre de la Pizza:</label>
                <input type="text" id="itemName-pizza" value={itemName} onChange={e => setItemName(e.target.value)} required />
            </div>

            <div className="form-group">
                <label htmlFor="description-pizza">Descripción:</label>
                <textarea id="description-pizza" value={description} onChange={e => setDescription(e.target.value)} />
            </div>

            <div className="form-group">
                <label htmlFor="salePrice-pizza">Precio de Venta:</label>
                <input type="number" step="0.01" id="salePrice-pizza" value={salePrice} onChange={e => setSalePrice(e.target.value)} required />
            </div>

            <fieldset className="form-fieldset ingredients-fieldset">
                <legend>Ingredientes de la Receta</legend>
                <div className="ingredient-adder">
                    <div className="form-group">
                        <label htmlFor="productSelectRecipe-pizza">Ingrediente:</label>
                        <select id="productSelectRecipe-pizza" value={selectedProductId} onChange={e => setSelectedProductId(e.target.value)}>
                            <option value="">-- Selecciona un producto --</option>
                            {allInventoryProducts.map(product => (
                                <option key={product.productoId} value={product.productoId.toString()}>
                                    {product.nombreProducto} (Stock: {product.currentStock} {product.unitOfMeasure})
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="form-group inline-group">
                        <div>
                            <label htmlFor="quantityRequiredRecipe-pizza">Cantidad:</label>
                            <input
                                type="number"
                                step="0.001"
                                id="quantityRequiredRecipe-pizza"
                                placeholder="Ej: 0.250"
                                value={quantityRequired}
                                onChange={e => setQuantityRequired(e.target.value)}
                            />
                        </div>
                        <div>
                            <label htmlFor="unitRequiredRecipe-pizza">Unidad:</label>
                            <input
                                type="text"
                                id="unitRequiredRecipe-pizza"
                                placeholder="Ej: kg, gr"
                                value={unitRequired}
                                onChange={e => setUnitRequired(e.target.value)}
                            />
                        </div>
                    </div>
                    <button type="button" onClick={handleAddIngredient} className="button-add-ingredient">
                        + Añadir Ingrediente
                    </button>
                </div>

                {currentIngredients.length > 0 && (
                    <>
                        <h4>Ingredientes Añadidos:</h4>
                        <ul className="current-ingredients-list">
                            {currentIngredients.map((ing, index) => (
                                <li key={index}>
                                    <span>{ing.nombreProducto}: {ing.quantityRequired} {ing.unitRequired}</span>
                                    <button type="button" onClick={() => handleRemoveIngredient(index)} className="button-remove-ingredient">&times;</button>
                                </li>
                            ))}
                        </ul>
                    </>
                )}
            </fieldset>

            <div className="form-group checkbox-group">
                <input type="checkbox" id="isAvailableRecipe-pizza" checked={isAvailable} onChange={e => setIsAvailable(e.target.checked)} />
                <label htmlFor="isAvailableRecipe-pizza">Disponible en menú</label>
            </div>

            <div className="form-actions">
                <button type="button" className="button-secondary" onClick={onCancel}>
                    Cancelar
                </button>
                <button type="submit" className="button-primary">
                    {pizzaToEdit ? "Actualizar Pizza" : "Guardar Pizza"}
                </button>
            </div>
        </form>
    );
};

export default CrearPizzaForm;