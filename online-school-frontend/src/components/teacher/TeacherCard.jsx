import React from 'react';
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  Box,
  Chip
} from '@mui/material';

export default function TeacherCard({ teacher, onEdit, onDelete }) {
  return (
    <Card sx={{ mb: 2, '&:hover': { boxShadow: 4 } }}>
      <CardContent>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
          <Box>
            <Typography variant="h6" component="div">
              {teacher.name}
            </Typography>
            <Typography sx={{ mb: 1.5 }} color="textSecondary">
              {teacher.email}
            </Typography>
          </Box>
          <Chip label={teacher.department || 'General'} color="primary" variant="outlined" />
        </Box>
        <Box sx={{ display: 'flex', gap: 2, mt: 2, flexWrap: 'wrap' }}>
          <Typography variant="body2">
            <strong>Employee ID:</strong> {teacher.employeeId}
          </Typography>
          <Typography variant="body2">
            <strong>Hire Date:</strong> {teacher.hireDate || 'N/A'}
          </Typography>
        </Box>
      </CardContent>
      <CardActions>
        <Button size="small" onClick={() => onEdit?.(teacher)}>
          Edit
        </Button>
        <Button size="small" color="error" onClick={() => onDelete?.(teacher.id)}>
          Delete
        </Button>
      </CardActions>
    </Card>
  );
}
