import React, { useMemo, useState } from 'react';
import { Alert, Box, Button, Stack, TextField, Snackbar } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import StudentCard from './StudentCard';
import StudentForm from './StudentForm';
import LoadingSpinner from '../common/LoadingSpinner';
import useStudents from '../../hooks/useStudents';

export default function StudentList() {
  const { students, loading, error, fetchStudents, addStudent, updateStudentData, removeStudent } = useStudents();
  const [showForm, setShowForm] = useState(false);
  const [editingStudent, setEditingStudent] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [notification, setNotification] = useState({ open: false, message: '', severity: 'success' });
  const navigate = useNavigate();

  const handleCloseNotification = () => {
    setNotification((prev) => ({ ...prev, open: false }));
  };

  const filteredStudents = useMemo(() => {
    const lowerSearch = searchTerm.trim().toLowerCase();
    if (!lowerSearch) return students;
    return students.filter((student) =>
      student.name.toLowerCase().includes(lowerSearch) ||
      student.email.toLowerCase().includes(lowerSearch)
    );
  }, [students, searchTerm]);

  const handleAddStudent = async (formData) => {
    try {
      await addStudent(formData);
      setNotification({ open: true, message: 'Student added successfully', severity: 'success' });
      setShowForm(false);
      setSearchTerm('');
    } catch (err) {
      setNotification({ open: true, message: err.message || 'Failed to add student', severity: 'error' });
      throw err;
    }
  };

  const handleEditStudent = (student) => {
    setEditingStudent(student);
    setShowForm(true);
  };

  const handleUpdateStudent = async (formData) => {
    try {
      await updateStudentData(editingStudent.id, formData);
      setNotification({ open: true, message: 'Student updated successfully', severity: 'success' });
      setEditingStudent(null);
      setShowForm(false);
    } catch (err) {
      setNotification({ open: true, message: err.message || 'Failed to update student', severity: 'error' });
      throw err;
    }
  };

  const handleDeleteStudent = async (id) => {
    if (window.confirm('Are you sure you want to delete this student?')) {
      try {
        await removeStudent(id);
        setNotification({ open: true, message: 'Student deleted successfully', severity: 'success' });
      } catch (err) {
        setNotification({ open: true, message: err.message || 'Failed to delete student', severity: 'error' });
      }
    }
  };

  const handleViewStudent = (student) => {
    navigate(`/students/${student.id}`);
  };

  if (loading) return <LoadingSpinner message="Loading students..." />;

  return (
    <Box>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }} action={
          <Button color="inherit" size="small" onClick={fetchStudents}>
            Retry
          </Button>
        }>
          {error}
        </Alert>
      )}

      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mb: 3 }}>
        <TextField
          fullWidth
          label="Search students by name or email"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <Button variant="outlined" onClick={() => setSearchTerm('')}>
          Clear
        </Button>
        <Button
          variant="contained"
          onClick={() => {
            setEditingStudent(null);
            setShowForm((prev) => !prev);
          }}
        >
          {showForm ? 'Cancel' : editingStudent ? 'Cancel edit' : 'Add New Student'}
        </Button>
      </Stack>

      {showForm && (
        <Box sx={{ mb: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
          <StudentForm
            onSubmit={editingStudent ? handleUpdateStudent : handleAddStudent}
            initialData={editingStudent}
            onCancel={() => {
              setShowForm(false);
              setEditingStudent(null);
            }}
          />
        </Box>
      )}

      {filteredStudents.length === 0 ? (
        <Alert severity="info">{searchTerm ? 'No results found.' : 'No students found'}</Alert>
      ) : (
        filteredStudents.map((student) => (
          <StudentCard
            key={student.id}
            student={student}
            onEdit={handleEditStudent}
            onDelete={handleDeleteStudent}
            onView={handleViewStudent}
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
