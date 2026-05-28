import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService, AuthResponse, LoginCredentials, RegisterData } from '../services/authService';
import api from '../services/api';

interface AuthContextValue {
  user: AuthResponse | null;
  loading: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  register: (data: RegisterData) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider: React.FC<React.PropsWithChildren<{}>> = ({ children }) => {
  const [user, setUser] = useState<AuthResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = localStorage.getItem('authUser');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  const persistUser = (authResponse: AuthResponse) => {
    localStorage.setItem('authToken', authResponse.token);
    localStorage.setItem('authUser', JSON.stringify(authResponse));
    setUser(authResponse);
  };

  const login = async (credentials: LoginCredentials) => {
    const response = await authService.login(credentials);
    persistUser(response.data);
    navigate('/dashboard', { replace: true });
  };

  const register = async (data: RegisterData) => {
    const response = await authService.register(data);
    persistUser(response.data);
    navigate('/dashboard', { replace: true });
  };

  const logout = () => {
    authService.logout();
    setUser(null);
    navigate('/login', { replace: true });
  };

  const value = useMemo(
    () => ({ user, loading, login, register, logout }),
    [user, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
