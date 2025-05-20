import React, { useState, useEffect, type FormEvent } from 'react';
import type { MenuResponse } from '../interfaces/Menu'; 
import type { CartItem, SalesRecordPayload, SalesRecordResponse, SoldItemPayload } from '../interfaces/Venta'; 
 

const RegistrarVentaForm: React.FC = () => {
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
                if (data.length > 0 && data.filter(item => item.isAvailable).length > 0) {
                    // Seleccionar el primer item disponible por defecto
                    const firstAvailable = data.find(item => item.isAvailable);
                    if (firstAvailable) {
                         setSelectedMenuItemId(firstAvailable.menuItemId.toString());
                    }
                }
            } catch (error) {
                console.error("Error fetching menu items:", error);
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
            setQuantityToSell(1); // Resetear cantidad
        } else {
            alert("El ítem seleccionado no está disponible o no se encontró.")
        }
    };

    const handleRemoveFromCart = (menuItemIdToRemove: number) => {
        setCart(prevCart => prevCart.filter(item => item.menuItemId !== menuItemIdToRemove));
    };

    const calculateTotal = (): string => {
        return cart.reduce((total, item) => total + (item.priceAtSale * item.quantitySold), 0).toFixed(2);
    };

    const handleSubmitSale = async (event: FormEvent<HTMLFormElement> | React.MouseEvent<HTMLButtonElement>) => {
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
                const errorText = await response.text();
                
                throw new Error(`Error al registrar venta: ${response.status} - ${errorText}`);
            }
            const result: SalesRecordResponse = await response.json();
            alert(`Venta registrada con ID: ${result.salesRecordId}! Stock actualizado.`);
            setCart([]);
            setNotes('');
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
        <div>
            <h2>Registrar Venta</h2>
            <form onSubmit={handleSubmitSale}> 
                <fieldset>
                    <legend>Añadir Ítem a la Venta</legend>
                    <div>
                        <label htmlFor="menuItemSelect">Pizza:</label>
                        <select id="menuItemSelect" value={selectedMenuItemId} onChange={e => setSelectedMenuItemId(e.target.value)}>
                            <option value="">-- Selecciona una Pizza --</option>
                            {allMenuItems.filter(item => item.isAvailable).map(item => (
                                <option key={item.menuItemId} value={item.menuItemId.toString()}>
                                    {item.itemName} - ${item.salePrice.toFixed(2)}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <label htmlFor="quantityToSell">Cantidad:</label>
                        <input
                            id="quantityToSell"
                            type="number"
                            min="1"
                            value={quantityToSell}
                            onChange={e => setQuantityToSell(parseInt(e.target.value, 10))}
                        />
                    </div>
                    <button type="button" onClick={handleAddToCart}>Añadir al Carrito</button>
                </fieldset>

                <h4>Carrito de Venta:</h4>
                {cart.length === 0 ? <p>El carrito está vacío.</p> : (
                    <>
                        <ul>
                            {cart.map((item) => (
                                <li key={item.menuItemId}>
                                    {item.itemName} (x{item.quantitySold}) @ ${item.priceAtSale.toFixed(2)} c/u = ${(item.quantitySold * item.priceAtSale).toFixed(2)}
                                    <button type="button" onClick={() => handleRemoveFromCart(item.menuItemId)} style={{ marginLeft: '10px' }}>
                                        Eliminar
                                    </button>
                                </li>
                            ))}
                        </ul>
                        <h3>Total: ${calculateTotal()}</h3>
                    </>
                )}
                <div>
                    <label htmlFor="saleNotes">Notas Adicionales:</label>
                    <textarea id="saleNotes" value={notes} onChange={e => setNotes(e.target.value)} />
                </div>
                <button type="submit" disabled={cart.length === 0}> 
                    Finalizar y Registrar Venta
                </button>
            </form>
        </div>
    );
};

export default RegistrarVentaForm;