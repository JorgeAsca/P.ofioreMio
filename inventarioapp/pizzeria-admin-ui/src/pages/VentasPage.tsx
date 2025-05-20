// src/pages/VentasPage.tsx
import React, { useState, useEffect, useCallback } from 'react';
import RegistrarVentaForm from '../components/RegistrarVentaForm'; 
import type { SalesRecordResponse } from '../interfaces/Venta'; 
import '../styles/VentasPage.css'; 


async function getAllSalesRecordsAPI(): Promise<SalesRecordResponse[]> {
    const response = await fetch('http://localhost:8080/api/v1/sales');
    if (!response.ok) {
        throw new Error('Error al cargar el historial de ventas');
    }
    return response.json();
}

const VentasPage: React.FC = () => {
    const [ventas, setVentas] = useState<SalesRecordResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(false); 
    const [error, setError] = useState<string | null>(null);
    const [showRegistrarVentaForm, setShowRegistrarVentaForm] = useState<boolean>(false);


    const fetchVentas = useCallback(async () => {
        try {
            setLoading(true);
            const data = await getAllSalesRecordsAPI(); 
            setVentas(data);
            setError(null);
        } catch (err: any) {
            setError(err.message || "Error al cargar las ventas.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    
    const handleVentaRegistrada = () => {
        
        setShowRegistrarVentaForm(false); 
        alert("Venta registrada con éxito!"); 
    };

    return (
        <div className="ventas-page">
            <div className="page-header">
                <h2>Ventas</h2>
                {!showRegistrarVentaForm && (
                    <button onClick={() => setShowRegistrarVentaForm(true)} className="button-add">
                        <span className="icon-plus">+</span> Nueva venta
                    </button>
                )}
            </div>
            <p className="page-subtitle">Registro de ventas de pizzas</p>


            {showRegistrarVentaForm && (
                <div className="registrar-venta-form-container">
                    <RegistrarVentaForm />
                   
                    <button onClick={() => setShowRegistrarVentaForm(false)} style={{ marginTop: '20px' }}>
                        Cerrar Registro de Venta
                    </button>
                </div>
            )}

            {error && <p className="error-message" style={{color: 'red'}}>{error}</p>}
            {loading && <p>Cargando historial de ventas...</p>}

            {!loading && !showRegistrarVentaForm && ventas.length === 0 && (
                <div className="no-ventas-placeholder">
                    <span className="placeholder-icon">🛒</span>
                    <h3>Todavía no hay ventas registradas</h3>
                    <p>Registra tu primera venta usando el botón superior.</p>
                </div>
            )}

            {/* Aquí iría el listado del historial de ventas si ventas.length > 0 */}
            {!loading && !showRegistrarVentaForm && ventas.length > 0 && (
                <div className="sales-history-list">
                    {ventas.map(venta => (
                        // Aquí renderizarías cada venta, similar a una tarjeta
                        <div key={venta.salesRecordId} className="sale-record-card">
                            <p>ID Venta: {venta.salesRecordId}</p>
                            <p>Fecha: {new Date(venta.saleDate).toLocaleDateString()}</p>
                            <p>Items: {venta.soldItems.length}</p>
                            <p>Notas: {venta.notes || 'N/A'}</p>
                            { // Calcular total si lo necesitas mostrar aquí
                                (() => {
                                    const total = venta.soldItems.reduce((sum, item) => sum + (item.priceAtSale * item.quantitySold), 0);
                                    return <p>Total: ${total.toFixed(2)}</p>;
                                })()
                            }
                        </div>
                    ))}
                </div>
            )} *
        </div>
    );
};

export default VentasPage;