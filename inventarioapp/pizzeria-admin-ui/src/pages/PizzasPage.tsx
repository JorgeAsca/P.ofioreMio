import React, { useState, useEffect, useCallback } from 'react';
import CrearPizzaForm from '../components/CrearPizzaForm';
import Modal from '../components/Modal'; 
import type { MenuResponse } from '../interfaces/Menu'; 
import '../styles/PizzasPage.css'; 



async function getAllMenuItems(): Promise<MenuResponse[]> {
    const response = await fetch('http://localhost:8080/api/v1/menu');
    if (!response.ok) {
        throw new Error('Error al cargar el menú de pizzas');
    }
    return response.json();
}

async function deleteMenuItemAPI(menuItemId: number): Promise<void> {
    const response = await fetch(`http://localhost:8080/api/v1/menu/${menuItemId}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Error al eliminar el ítem del menú ${menuItemId}`);
    }
}

const PizzasPage: React.FC = () => {
    const [pizzas, setPizzas] = useState<MenuResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [showCreateFormModal, setShowCreateFormModal] = useState<boolean>(false);
    const [editingPizza, setEditingPizza] = useState<MenuResponse | null | undefined>(null);

    const fetchPizzas = useCallback(async () => {
        try {
            setLoading(true);
            const data = await getAllMenuItems();
            setPizzas(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || "Error al cargar las pizzas.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchPizzas();
    }, [fetchPizzas]);

    const handlePizzaFormSuccess = () => {
        fetchPizzas();
        setShowCreateFormModal(false);
        setEditingPizza(null);
    };

    const openCreatePizzaModal = () => {
        setEditingPizza(null);
        setShowCreateFormModal(true);
    };

    const openEditPizzaModal = (pizzaToEdit: MenuResponse) => {
        setEditingPizza(pizzaToEdit);
        setShowCreateFormModal(true);
    };

    const handleDeletePizza = async (pizzaId: number) => {
        if (window.confirm('¿Estás seguro de que quieres eliminar esta pizza del menú?')) {
            try {
                await deleteMenuItemAPI(pizzaId);
                fetchPizzas();
            } catch (err: any) {
                setError(err.message || "Error al eliminar la pizza.");
                console.error(err);
            }
        }
    };

    const handleConsumirPizza = (pizza: MenuResponse) => {
        alert(`Simulando consumo de: ${pizza.itemName}`);
    };

    return (
        <div className="pizzas-page">
            <div className="page-header">
                <h2>Pizzas</h2>
                <button onClick={openCreatePizzaModal} className="button-add">
                    <span className="icon-plus">+</span> Añadir Pizza
                </button>
            </div>

            <Modal
                isOpen={showCreateFormModal}
                onClose={() => {
                    setShowCreateFormModal(false);
                    setEditingPizza(null);
                }}
                title={editingPizza ? "Editar Pizza" : "Añadir Nueva Pizza"}
            >
                <CrearPizzaForm
                    onPizzaSaved={handlePizzaFormSuccess}
                    onCancel={() => {
                        setShowCreateFormModal(false);
                        setEditingPizza(null);
                    }}
                    pizzaToEdit={editingPizza || undefined}
                />
            </Modal>

            {error && <p className="error-message" style={{ color: 'red' }}>{error}</p>}
            {loading && <p>Cargando pizzas...</p>}
            {!loading && pizzas.length === 0 && !showCreateFormModal && (
                <p>No hay pizzas en el menú. ¡Añade la primera!</p>
            )}

            {!showCreateFormModal && pizzas.length > 0 && (
                <div className="pizza-list">
                    {pizzas.map((pizza) => (
                        <div key={pizza.menuItemId} className="pizza-card">
                            

                            <div className="pizza-card-main-info">
                                <h3>{pizza.itemName}</h3>
                                <p className="pizza-description">{pizza.description || "Pizza deliciosa y única."}</p>
                                <div className="pizza-tags">
                                    <span className="pizza-ingredients-count">
                                        {pizza.ingredients?.length || 0} ingredientes
                                    </span>
                                </div>
                            </div>

                            <div className="pizza-card-price-actions">
                                <span className="pizza-price">${pizza.salePrice.toFixed(2)}</span>
                                <div className="pizza-action-buttons">
                                    <button
                                        className="action-button edit-button"
                                        title="Editar Pizza"
                                        onClick={() => openEditPizzaModal(pizza)}
                                    >
                                        ✏️
                                    </button>
                                    <button
                                        className="action-button delete-button"
                                        title="Eliminar Pizza"
                                        onClick={() => handleDeletePizza(pizza.menuItemId)}
                                    >
                                        🗑️
                                    </button>
                                </div>
                                <button
                                    onClick={() => handleConsumirPizza(pizza)}
                                    className="action-button consumir-button"
                                    disabled={!pizza.isAvailable}
                                    style={{ marginTop: '10px' }}
                                >
                                    Consumir
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default PizzasPage;