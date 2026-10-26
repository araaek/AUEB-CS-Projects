// File: shared/dto/RequestType.java
package shared.dto;

import java.io.Serializable;

public enum RequestType implements Serializable {
    ADD_STORE,     
    ADD_PRODUCT,        
    UPDATE_STOCK,       
     REMOVE_PRODUCT, 
    RATE_STORE,      
    SEARCH_STORES,      
    PURCHASE_PRODUCTS,  
    GET_STORE_PRODUCT_SALES 
}