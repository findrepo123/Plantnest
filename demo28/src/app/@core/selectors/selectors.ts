// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';

import { CartItemState } from '../reducers/cart.reducer';
import { WishlistState } from '../reducers/wishlist.reducer';

export const getCartItemsState = createFeatureSelector<CartItemState>('cart');
export const getWishlistState = createFeatureSelector<WishlistState>('wishlist');

/************************    CartItem Selectors   ***********************/
export const cartItemsSelector = createSelector(
    getCartItemsState, cartItemState => {
        return cartItemState.data;
    }
);

/************************    Wishlist Selectors   ***********************/
export const wishlistSelector = createSelector(
    getWishlistState, wishlistState => {
        return wishlistState.data;
    }
);
