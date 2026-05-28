import api from './api';

export interface AuthResponse {
  token: string;
  name: string;
  username: string;
  email: string;
  role: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterData {
  name: string;
  username: string;
  email: string;
  password: string;
  role: 'ADMIN' | 'TEACHER' | 'STUDENT' | 'USER';
}

export const authService = {
  login: (credentials: LoginCredentials) => api.post<AuthResponse>('/auth/login', credentials),
  register: (data: RegisterData) => api.post<AuthResponse>('/auth/register', data),
  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('authUser');
  },
  getCurrentUser: () => api.get<AuthResponse>('/auth/me'),
};
