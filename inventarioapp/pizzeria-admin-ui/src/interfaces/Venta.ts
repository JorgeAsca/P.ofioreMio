
import type { MenuResponse } from './Menu'; 

export interface CartItem {
    menuItemId: number;
    itemName: string;
    quantitySold: number;
    priceAtSale: number;
}

export interface SoldItemPayload { 
    menuItemId: number;
    quantitySold: number;
    priceAtSale: number;
}

export interface SalesRecordPayload { 
    saleDate: string; 
    userId?: number; 
    notes?: string;
    soldItems: SoldItemPayload[];
}

export interface SalesRecordResponse extends SalesRecordPayload { 
    salesRecordId: number;
    recordedAt: string; 
}