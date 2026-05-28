import { useState, useEffect } from 'react';
import {
  getTeachers,
  createTeacher,
  updateTeacher,
  deleteTeacher
} from '../services/teacherService';

export default function useTeachers() {
  const [teachers, setTeachers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchTeachers();
  }, []);

  const fetchTeachers = async () => {
    try {
      setLoading(true);
      const data = await getTeachers();
      setTeachers(data);
      setError(null);
    } catch (err) {
      setError(err.message || 'Unable to load teachers');
    } finally {
      setLoading(false);
    }
  };

  const addTeacher = async (teacher) => {
    try {
      const newTeacher = await createTeacher(teacher);
      setTeachers((prev) => [...prev, newTeacher]);
      return newTeacher;
    } catch (err) {
      setError(err.message || 'Unable to add teacher');
      throw err;
    }
  };

  const updateTeacherData = async (id, teacher) => {
    try {
      const updated = await updateTeacher(id, teacher);
      setTeachers((prev) => prev.map((t) => (t.id === id ? updated : t)));
      return updated;
    } catch (err) {
      setError(err.message || 'Unable to update teacher');
      throw err;
    }
  };

  const removeTeacher = async (id) => {
    const previous = teachers;
    setTeachers((prev) => prev.filter((t) => t.id !== id));
    try {
      await deleteTeacher(id);
    } catch (err) {
      setTeachers(previous);
      setError(err.message || 'Unable to delete teacher');
      throw err;
    }
  };

  return {
    teachers,
    loading,
    error,
    fetchTeachers,
    addTeacher,
    updateTeacherData,
    removeTeacher
  };
}
