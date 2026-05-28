import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Box,
  Typography,
  Button,
  Alert,
  Snackbar,
  Chip,
  Stack,
  Paper
} from '@mui/material';
import { getStudentById, updateStudent, deleteStudent } from '../../services/studentService';
import LoadingSpinner from '../common/LoadingSpinner';
import StudentForm from '../student/StudentForm';

export default function StudentDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [student, setStudent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [notification, setNotification] = useState({ open: false, message: '', severity: 'success' });

  const fetchStudent = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getStudentById(id);
      setStudent(data);
    } catch (err) {
      setError(err.message || 'Failed to load student details');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStudent();
  }, [id]);

  const handleDelete = async () => {
    if (window.confirm('Delete this student?')) {
      try {
        await deleteStudent(id);
        navigate('/students');
      } catch (err) {
        setNotification({ open: true, message: err.message || 'Failed to delete student', severity: 'error' });
      }
    }
  };

  const handleUpdate = async (formData) => {
    try {
      const updated = await updateStudent(id, formData);
      setStudent(updated);
      setEditMode(false);
      setNotification({ open: true, message: 'Student details updated successfully', severity: 'success' });
    } catch (err) {
      setNotification({ open: true, message: err.message || 'Failed to save changes', severity: 'error' });
      throw err;
    }
  };

  const classCount = useMemo(() => student?.classIds?.length || 0, [student]);

  if (loading) return <LoadingSpinner message="Loading student details..." />;

  if (error) {
    return (
      <Box>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button variant="contained" onClick={fetchStudent}>
          Retry
        </Button>
      </Box>
    );
  }

  if (!student) {
    return (
      <Alert severity="info">Student not found.</Alert>
    );
  }

  return (
    <Box>
      <Stack direction={{ xs: 'column', sm: 'row' }} justifyContent="space-between" alignItems="flex-start" sx={{ mb: 3, gap: 2 }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold' }}>
            {student.name}
          </Typography>
          <Typography color="textSecondary">Student ID: {student.studentId}</Typography>
        </Box>
        <Stack direction="row" spacing={2}>
          <Button variant="outlined" onClick={() => navigate('/students')}>
            Back to Students
          </Button>
          <Button variant="contained" onClick={() => setEditMode((prev) => !prev)}>
            {editMode ? 'Cancel' : 'Edit'}
          </Button>
          <Button color="error" variant="outlined" onClick={handleDelete}>
            Delete
          </Button>
        </Stack>
      </Stack>

      {editMode ? (
        <Paper sx={{ p: 3, mb: 3 }}>
          <StudentForm onSubmit={handleUpdate} initialData={student} />
        </Paper>
      ) : (
        <Paper sx={{ p: 3, mb: 3 }}>
          <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
            <Box>
              <Typography variant="subtitle1" sx={{ mb: 1 }}><strong>Email</strong></Typography>
              <Typography>{student.email}</Typography>
            </Box>
            <Box>
              <Typography variant="subtitle1" sx={{ mb: 1 }}><strong>Enrollment Date</strong></Typography>
              <Typography>{student.enrollmentDate}</Typography>
            </Box>
            <Box>
              <Typography variant="subtitle1" sx={{ mb: 1 }}><strong>Created At</strong></Typography>
              <Typography>{student.createdAt ? new Date(student.createdAt).toLocaleString() : 'N/A'}</Typography>
            </Box>
            <Box>
              <Typography variant="subtitle1" sx={{ mb: 1 }}><strong>Updated At</strong></Typography>
              <Typography>{student.updatedAt ? new Date(student.updatedAt).toLocaleString() : 'N/A'}</Typography>
            </Box>
          </Box>
        </Paper>
      )}

      <Box>
        <Typography variant="h6" sx={{ mb: 2 }}>Enrolled Classes ({classCount})</Typography>
        {classCount === 0 ? (
          <Alert severity="info">This student is not enrolled in any classes yet.</Alert>
        ) : (
          <Stack direction="row" spacing={1} flexWrap="wrap">
            {student.classIds.map((classId) => (
              <Chip key={classId} label={`Class ${classId}`} sx={{ mb: 1 }} />
            ))}
          </Stack>
        )}
      </Box>

      <Snackbar
        open={notification.open}
        autoHideDuration={4000}
        onClose={() => setNotification((prev) => ({ ...prev, open: false }))}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert severity={notification.severity} sx={{ width: '100%' }}>
          {notification.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}
