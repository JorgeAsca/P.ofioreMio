import React, { useState, useEffect, useCallback } from 'react';
import RegistrarVentaForm from '../components/RegistrarVentaForm'; 
import Modal from '../components/Modal';
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
    const [historialVentas, setHistorialVentas] = useState<SalesRecordResponse[]>([]);
    const [loadingVentas, setLoadingVentas] = useState<boolean>(false); 
    const [errorVentas, setErrorVentas] = useState<string | null>(null);
    const [showRegistrarVentaModal, setShowRegistrarVentaModal] = useState<boolean>(false);

    const fetchHistorialVentas = useCallback(async () => {
        try {
            setLoadingVentas(true);
            const data = await getAllSalesRecordsAPI();
            setHistorialVentas(data);
            setErrorVentas(null);
        } catch (err: any) {
            setErrorVentas(err.message || "Error al cargar el historial de ventas.");
            console.error(err);
        } finally {
            setLoadingVentas(false);
        }
    }, []);

 
    useEffect(() => {
        fetchHistorialVentas();
    }, [fetchHistorialVentas]);

    const handleVentaRegistradaConExito = () => {
        fetchHistorialVentas(); 
        setShowRegistrarVentaModal(false); 
        alert("Venta registrada con éxito y stock actualizado!");
    };

    const openRegistrarVentaModal = () => {
        setShowRegistrarVentaModal(true);
    };

    return (
        <div className="ventas-page">
            <div className="page-header">
                <h2>Ventas</h2>
                <button onClick={openRegistrarVentaModal} className="button-add">
                    <span className="icon-plus">+</span> Nueva venta
                </button>
            </div>
            <p className="page-subtitle">Registro diario de ventas de pizzas y otros productos.</p>

            <Modal
                isOpen={showRegistrarVentaModal}
                onClose={() => setShowRegistrarVentaModal(false)}
                title="Registrar Nueva Venta"
            >
                <RegistrarVentaForm
                    
                    onSaleSuccess={handleVentaRegistradaConExito} 
                    onCancel={() => setShowRegistrarVentaModal(false)} 
                />
            </Modal>

            {errorVentas && <p className="error-message" style={{ color: 'red' }}>{errorVentas}</p>}

            {/* Sección para mostrar el historial de ventas */}
            <div className="sales-history-section">
                <h3>Historial de Ventas</h3>
                {loadingVentas && <p>Cargando historial...</p>}
                {!loadingVentas && historialVentas.length === 0 && (
                    <div className="no-ventas-placeholder">
                        <span className="placeholder-icon">🛒</span>
                        <h4>Todavía no hay ventas registradas</h4>
                        <p>Registra tu primera venta usando el botón superior.</p>
                    </div>
                )}
                {!loadingVentas && historialVentas.length > 0 && (
                    <div className="sales-history-list">
                        {historialVentas.map(venta => (
                            <div key={venta.salesRecordId} className="sale-record-card">
                                <div className="sale-record-header">
                                    <strong>Venta ID: {venta.salesRecordId}</strong>
                                    <span>Fecha: {new Date(venta.saleDate).toLocaleDateString()}</span>
                                </div>
                                <ul className="sold-items-list">
                                    {venta.soldItems.map((item, index) => (
                                        <li key={index}>
                                            
                                            Item ID: {item.menuItemId} - Cantidad: {item.quantitySold} - Precio: ${item.priceAtSale.toFixed(2)}
                                        </li>
                                    ))}
                                </ul>
                                <div className="sale-record-footer">
                                    <span>Notas: {venta.notes || 'N/A'}</span>
                                    <span>Total: ${venta.soldItems.reduce((sum, item) => sum + (item.priceAtSale * item.quantitySold), 0).toFixed(2)}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default VentasPage;