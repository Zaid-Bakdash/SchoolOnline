import React from 'react';
import { Box, Typography } from '@mui/material';
import TeacherList from '../teacher/TeacherList';

export default function TeachersPage() {
  return (
    <Box>
      <Typography variant="h4" component="h1" sx={{ mb: 3, fontWeight: 'bold' }}>
        Teachers
      </Typography>
      <TeacherList />
    </Box>
  );
}
