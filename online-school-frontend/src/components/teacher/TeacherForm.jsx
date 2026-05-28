import React, { useMemo, useState, useEffect } from 'react';
import { TextField, Button, Box, Alert, Stack } from '@mui/material';

const initialTeacher = {
  name: '',
  email: '',
  employeeId: '',
  department: '',
  hireDate: ''
};

const validateTeacher = (values) => {
  const errors = {};
  if (!values.name.trim()) {
    errors.name = 'Name is required';
  } else if (values.name.trim().length < 2) {
    errors.name = 'Name must be at least 2 characters';
  }

  if (!values.email.trim()) {
    errors.email = 'Email is required';
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email)) {
    errors.email = 'Enter a valid email address';
  }

  if (!values.employeeId.trim()) {
    errors.employeeId = 'Employee ID is required';
  }

  if (!values.department.trim()) {
    errors.department = 'Department is required';
  }

  if (!values.hireDate) {
    errors.hireDate = 'Hire date is required';
  } else if (new Date(values.hireDate) > new Date()) {
    errors.hireDate = 'Hire date cannot be in the future';
  }

  return errors;
};

export default function TeacherForm({ onSubmit, initialData = null, onCancel }) {
  const [formData, setFormData] = useState(initialData || initialTeacher);
  const [touched, setTouched] = useState({});
  const [submitError, setSubmitError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    setFormData(initialData || initialTeacher);
    setTouched({});
    setSubmitError(null);
    setSuccessMessage('');
  }, [initialData]);

  const errors = useMemo(() => validateTeacher(formData), [formData]);
  const isValid = Object.keys(errors).length === 0;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (touched[name]) {
      setSubmitError(null);
    }
  };

  const handleBlur = (e) => {
    const { name } = e.target;
    setTouched((prev) => ({ ...prev, [name]: true }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setTouched({ name: true, email: true, employeeId: true, department: true, hireDate: true });
    setSubmitError(null);

    if (!isValid) {
      return;
    }

    try {
      await onSubmit(formData);
      setSuccessMessage('Teacher saved successfully');
    } catch (err) {
      setSubmitError(err.message || 'Unable to save teacher');
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} noValidate>
      {submitError && <Alert severity="error" sx={{ mb: 2 }}>{submitError}</Alert>}
      {successMessage && <Alert severity="success" sx={{ mb: 2 }}>{successMessage}</Alert>}

      <Stack spacing={2}>
        <TextField
          fullWidth
          label="Name"
          name="name"
          value={formData.name}
          onChange={handleChange}
          onBlur={handleBlur}
          error={Boolean(touched.name && errors.name)}
          helperText={touched.name && errors.name}
          required
        />

        <TextField
          fullWidth
          label="Email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          onBlur={handleBlur}
          error={Boolean(touched.email && errors.email)}
          helperText={touched.email && errors.email}
          required
        />

        <TextField
          fullWidth
          label="Employee ID"
          name="employeeId"
          value={formData.employeeId}
          onChange={handleChange}
          onBlur={handleBlur}
          error={Boolean(touched.employeeId && errors.employeeId)}
          helperText={touched.employeeId && errors.employeeId}
          required
        />

        <TextField
          fullWidth
          label="Department"
          name="department"
          value={formData.department}
          onChange={handleChange}
          onBlur={handleBlur}
          error={Boolean(touched.department && errors.department)}
          helperText={touched.department && errors.department}
          required
        />

        <TextField
          fullWidth
          label="Hire Date"
          name="hireDate"
          type="date"
          value={formData.hireDate}
          onChange={handleChange}
          onBlur={handleBlur}
          error={Boolean(touched.hireDate && errors.hireDate)}
          helperText={touched.hireDate && errors.hireDate}
          InputLabelProps={{ shrink: true }}
          required
        />

        <Stack direction="row" spacing={2} sx={{ pt: 1 }}>
          <Button type="submit" variant="contained" disabled={!isValid}>
            Save Teacher
          </Button>
          {onCancel && (
            <Button type="button" variant="outlined" onClick={onCancel}>
              Cancel
            </Button>
          )}
        </Stack>
      </Stack>
    </Box>
  );
}
