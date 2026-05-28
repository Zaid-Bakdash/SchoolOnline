import { useState, useEffect } from 'react';
import {
  getStudents,
  createStudent,
  updateStudent,
  deleteStudent
} from '../services/studentService';

export default function useStudents() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    try {
      setLoading(true);
      const data = await getStudents();
      setStudents(data);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const addStudent = async (student) => {
    try {
      const newStudent = await createStudent(student);
      setStudents([...students, newStudent]);
      return newStudent;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const updateStudentData = async (id, student) => {
    try {
      const updated = await updateStudent(id, student);
      setStudents(students.map(s => s.id === id ? updated : s));
      return updated;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const removeStudent = async (id) => {
    const previous = students;
    setStudents((prev) => prev.filter((s) => s.id !== id));
    try {
      await deleteStudent(id);
    } catch (err) {
      setStudents(previous);
      setError(err.message || 'Unable to delete student');
      throw err;
    }
  };

  return {
    students,
    loading,
    error,
    fetchStudents,
    addStudent,
    updateStudentData,
    removeStudent
  };
}
