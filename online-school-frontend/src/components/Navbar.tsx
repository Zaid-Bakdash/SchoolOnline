import React from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
} from '@mui/material';
import {
  School,
  Dashboard,
  People,
  Person,
  Book,
  Class,
  Assignment,
  Login as LoginIcon,
  AppRegistration,
  Logout,
} from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();

  const menuItems = [
    { label: 'Dashboard', path: '/dashboard', icon: <Dashboard /> },
    { label: 'Students', path: '/students', icon: <People /> },
    { label: 'Teachers', path: '/teachers', icon: <Person /> },
    { label: 'Courses', path: '/courses', icon: <Book /> },
    { label: 'Classes', path: '/classes', icon: <Class /> },
    { label: 'Registrations', path: '/registrations', icon: <Assignment /> },
  ];

  return (
    <AppBar position="static" elevation={2}>
      <Toolbar>
        <School sx={{ mr: 2 }} />
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Online School Management
        </Typography>
        <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
          {user && menuItems.map((item) => (
            <Button
              key={item.path}
              color="inherit"
              startIcon={item.icon}
              onClick={() => navigate(item.path)}
              sx={{
                backgroundColor: location.pathname === item.path ? 'rgba(255, 255, 255, 0.1)' : 'transparent',
                '&:hover': {
                  backgroundColor: 'rgba(255, 255, 255, 0.2)',
                },
              }}
            >
              {item.label}
            </Button>
          ))}
          {user ? (
            <>
              <Typography sx={{ mr: 2 }}>
                {user.username} ({user.role})
              </Typography>
              <Button color="inherit" startIcon={<Logout />} onClick={logout}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Button
                color="inherit"
                startIcon={<LoginIcon />}
                onClick={() => navigate('/login')}
              >
                Login
              </Button>
              <Button
                color="inherit"
                startIcon={<AppRegistration />}
                onClick={() => navigate('/register')}
              >
                Register
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;