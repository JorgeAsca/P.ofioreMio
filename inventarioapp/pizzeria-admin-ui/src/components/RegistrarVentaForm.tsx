import React, { useState, useEffect, type FormEvent } from 'react';
import type { MenuResponse } from '../interfaces/Menu'; 
import type { CartItem, SalesRecordPayload, SalesRecordResponse, SoldItemPayload } from '../interfaces/Venta'; 
 
interface RegistrarVentaFormProps {
    onSaleSuccess: () => void; 
    onCancel: () => void;      
}


const RegistrarVentaForm: React.FC<RegistrarVentaFormProps> = ({ onSaleSuccess, onCancel }) => {
    const [allMenuItems, setAllMenuItems] = useState<MenuResponse[]>([]);
    const [cart, setCart] = useState<CartItem[]>([]);
    const [notes, setNotes] = useState<string>('');

    const [selectedMenuItemId, setSelectedMenuItemId] = useState<string>('');
    const [quantityToSell, setQuantityToSell] = useState<number>(1);

    useEffect(() => {
        const fetchMenuItems = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/v1/menu');
                if (!response.ok) throw new Error('Error al cargar el menú de pizzas');
                const data: MenuResponse[] = await response.json();
                setAllMenuItems(data);
                if (data.length > 0) { 
                    const firstAvailable = data.find(item => item.isAvailable);
                    if (firstAvailable) {
                        setSelectedMenuItemId(firstAvailable.menuItemId.toString());
                    }
                }
            } catch (error) {
                console.error("Error fetching menu items for sale:", error);
                
            }
        };
        fetchMenuItems();
    }, []);

    const handleAddToCart = () => {
        if (!selectedMenuItemId || quantityToSell <= 0) {
            alert("Selecciona un ítem y una cantidad válida.");
            return;
        }
        const menuItem = allMenuItems.find(item => item.menuItemId.toString() === selectedMenuItemId);
        if (menuItem && menuItem.isAvailable) {
            const existingCartItemIndex = cart.findIndex(item => item.menuItemId.toString() === selectedMenuItemId);

            if (existingCartItemIndex > -1) {
                const updatedCart = cart.map((item, index) =>
                    index === existingCartItemIndex
                        ? { ...item, quantitySold: item.quantitySold + quantityToSell }
                        : item
                );
                setCart(updatedCart);
            } else {
                setCart(prevCart => [
                    ...prevCart,
                    {
                        menuItemId: menuItem.menuItemId,
                        itemName: menuItem.itemName, 
                        quantitySold: quantityToSell,
                        priceAtSale: menuItem.salePrice 
                    }
                ]);
            }
            setQuantityToSell(1); 
        } else {
            alert("El ítem seleccionado no está disponible o no se encontró.");
        }
    };

    const handleRemoveFromCart = (menuItemIdToRemove: number) => {
        setCart(prevCart => prevCart.filter(item => item.menuItemId !== menuItemIdToRemove));
    };

    const calculateTotal = (): string => {
        return cart.reduce((total, item) => total + (item.priceAtSale * item.quantitySold), 0).toFixed(2);
    };

    const handleSubmitSale = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (cart.length === 0) {
            alert("El carrito está vacío.");
            return;
        }

        const soldItemsPayload: SoldItemPayload[] = cart.map(item => ({
            menuItemId: item.menuItemId,
            quantitySold: item.quantitySold,
            priceAtSale: item.priceAtSale
        }));

        const salesData: SalesRecordPayload = {
            saleDate: new Date().toISOString().split('T')[0], 
            notes: notes,
            soldItems: soldItemsPayload
            
        };
        
        console.log("Enviando SalesRecordPayload:", JSON.stringify(salesData, null, 2));

        try {
            const response = await fetch('http://localhost:8080/api/v1/sales', { 
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(salesData),
            });

            if (!response.ok) {
                let errorMsg = `Error al registrar venta: ${response.status}`;
                try {
                    
                    const errorText = await response.text();
                    
                    const errorJson = JSON.parse(errorText); 
                    errorMsg = errorJson.message || errorJson.error || errorText || errorMsg;
                } catch (e) {
                    
                    errorMsg = `Error al registrar venta: ${response.status}. Intenta de nuevo.`;
                }
                throw new Error(errorMsg);
            }
            
            
            setCart([]);
            setNotes('');
            
            const firstAvailable = allMenuItems.find(item => item.isAvailable);
            setSelectedMenuItemId(firstAvailable ? firstAvailable.menuItemId.toString() : '');
            setQuantityToSell(1);

            onSaleSuccess(); 

        } catch (error) {
            console.error("Error al registrar venta:", error);
             if (error instanceof Error) {
                alert(`Error al registrar venta: ${error.message}`);
            } else {
                alert('Ocurrió un error desconocido al registrar la venta.');
            }
        }
    };

    return (
        
        <form onSubmit={handleSubmitSale}>
            <fieldset className="form-fieldset"> 
                <legend>Añadir Ítem a la Venta</legend>
                <div className="form-group"> 
                    <label htmlFor="menuItemSelectSale">Pizza/Ítem:</label>
                    <select 
                        id="menuItemSelectSale" 
                        value={selectedMenuItemId} 
                        onChange={e => setSelectedMenuItemId(e.target.value)}
                    >
                        <option value="">-- Selecciona un Ítem --</option>
                        {allMenuItems.filter(item => item.isAvailable).map(item => (
                            <option key={item.menuItemId} value={item.menuItemId.toString()}>
                                {item.itemName} - ${item.salePrice.toFixed(2)}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="quantityToSellSale">Cantidad:</label>
                    <input
                        id="quantityToSellSale"
                        type="number"
                        min="1"
                        value={quantityToSell}
                        onChange={e => setQuantityToSell(parseInt(e.target.value, 10) || 1)} 
                    />
                </div>
                <button 
                    type="button" 
                    onClick={handleAddToCart} 
                    className="button-add-ingredient" // Reutiliza estilo de CrearPizzaForm o crea uno nuevo
                    style={{alignSelf: 'center', marginTop: '10px'}}
                >
                    Añadir al Carrito
                </button>
            </fieldset>

            <h4>Carrito de Venta:</h4>
            {cart.length === 0 ? <p>El carrito está vacío.</p> : (
                <>
                    
                    <ul className="current-cart-list"> 
                        {cart.map((item) => (
                            <li key={item.menuItemId}>
                                <span>{item.itemName} (x{item.quantitySold}) @ ${item.priceAtSale.toFixed(2)} c/u = ${(item.quantitySold * item.priceAtSale).toFixed(2)}</span>
                                <button 
                                    type="button" 
                                    onClick={() => handleRemoveFromCart(item.menuItemId)} 
                                    className="button-remove-ingredient" 
                                >
                                    &times; 
                                </button>
                            </li>
                        ))}
                    </ul>
                    <h3>Total: ${calculateTotal()}</h3>
                </>
            )}
            <div className="form-group">
                <label htmlFor="saleNotes">Notas Adicionales:</label>
                <textarea id="saleNotes" value={notes} onChange={e => setNotes(e.target.value)} />
            </div>
            <div className="form-actions"> 
                <button type="button" className="button-secondary" onClick={onCancel}>
                    Cancelar
                </button>
                <button type="submit" className="button-primary" disabled={cart.length === 0}>
                    Finalizar y Registrar Venta
                </button>
            </div>
        </form>
    );
};


export default RegistrarVentaForm;