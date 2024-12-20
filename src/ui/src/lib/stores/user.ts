import type { LoginRequest, User } from '$lib/api/api';
import { writable } from 'svelte/store';

// Store to hold user data
export const user = writable<User | null>(null);

export function loginUser(userData: User)
{
    user.set(userData);
    localStorage.setItem('user', JSON.stringify(userData));
}

// Function to log the user out
export function logoutUser() {
    user.set(null);
    localStorage.removeItem('user'); // Clear storage
}

// Initialize store with data from localStorage
const savedUser = localStorage.getItem('user');
if (savedUser) {
  user.set(JSON.parse(savedUser));
}