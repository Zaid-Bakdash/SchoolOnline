import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

export default function Header() {
  return (
    <AppBar position="static" sx={{ mb: 4 }}>
      <Toolbar>
        <Typography
          variant="h6"
          component="div"
          sx={{ flexGrow: 1, fontWeight: 'bold' }}
        >
          Online School
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
          <Button
            color="inherit"
            component={RouterLink}
            to="/"
            sx={{ '&:hover': { backgroundColor: 'rgba(255,255,255,0.1)' } }}
          >
            Home
          </Button>
          <Button
            color="inherit"
            component={RouterLink}
            to="/students"
            sx={{ '&:hover': { backgroundColor: 'rgba(255,255,255,0.1)' } }}
          >
            Students
          </Button>
          <Button
            color="inherit"
            component={RouterLink}
            to="/courses"
            sx={{ '&:hover': { backgroundColor: 'rgba(255,255,255,0.1)' } }}
          >
            Courses
          </Button>
          <Button
            color="inherit"
            component={RouterLink}
            to="/teachers"
            sx={{ '&:hover': { backgroundColor: 'rgba(255,255,255,0.1)' } }}
          >
            Teachers
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
}
