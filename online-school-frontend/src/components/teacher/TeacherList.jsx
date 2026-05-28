import React, { useState } from 'react';
import { Alert, Box, Button, Snackbar, Stack } from '@mui/material';
import TeacherCard from './TeacherCard';
import TeacherForm from './TeacherForm';
import LoadingSpinner from '../common/LoadingSpinner';
import useTeachers from '../../hooks/useTeachers';

export default function TeacherList() {
  const { teachers, loading, error, addTeacher, updateTeacherData, removeTeacher, fetchTeachers } = useTeachers();
  const [showForm, setShowForm] = useState(false);
  const [editingTeacher, setEditingTeacher] = useState(null);
  const [notification, setNotification] = useState({ open: false, message: '', severity: 'success' });

  const handleCloseNotification = () => {
    setNotification({ ...notification, open: false });
  };

  const handleSubmit = async (formData) => {
    try {
      if (editingTeacher) {
        await updateTeacherData(editingTeacher.id, formData);
        setNotification({ open: true, message: 'Teacher updated successfully', severity: 'success' });
      } else {
        await addTeacher(formData);
        setNotification({ open: true, message: 'Teacher added successfully', severity: 'success' });
      }
      setShowForm(false);
      setEditingTeacher(null);
    } catch (err) {
      setNotification({ open: true, message: err.message || 'Failed to save teacher', severity: 'error' });
      throw err;
    }
  };

  const handleEdit = (teacher) => {
    setEditingTeacher(teacher);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this teacher?')) {
      try {
        await removeTeacher(id);
        setNotification({ open: true, message: 'Teacher deleted successfully', severity: 'success' });
      } catch (err) {
        setNotification({ open: true, message: err.message || 'Failed to delete teacher', severity: 'error' });
      }
    }
  };

  if (loading) return <LoadingSpinner message="Loading teachers..." />;

  return (
    <Box>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }} action={
          <Button color="inherit" size="small" onClick={fetchTeachers}>
            Retry
          </Button>
        }>
          {error}
        </Alert>
      )}

      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mb: 3 }}>
        <Button
          variant="contained"
          onClick={() => {
            setEditingTeacher(null);
            setShowForm(!showForm);
          }}
        >
          {showForm ? 'Cancel' : editingTeacher ? 'Cancel edit' : 'Add New Teacher'}
        </Button>
      </Stack>

      {showForm && (
        <Box sx={{ mb: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
          <TeacherForm
            onSubmit={handleSubmit}
            initialData={editingTeacher}
            onCancel={() => {
              setShowForm(false);
              setEditingTeacher(null);
            }}
          />
        </Box>
      )}

      {teachers.length === 0 ? (
        <Alert severity="info">No teachers found</Alert>
      ) : (
        teachers.map((teacher) => (
          <TeacherCard
            key={teacher.id}
            teacher={teacher}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        ))
      )}

      <Snackbar
        open={notification.open}
        autoHideDuration={4000}
        onClose={handleCloseNotification}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={handleCloseNotification} severity={notification.severity} sx={{ width: '100%' }}>
          {notification.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
