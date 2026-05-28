import api from './api';

export const getTeachers = async () => {
  const response = await api.get('/teachers');
  return response.data;
};

export const getTeacherById = async (id) => {
  const response = await api.get(`/teachers/${id}`);
  return response.data;
};

export const createTeacher = async (teacher) => {
  const response = await api.post('/teachers', teacher);
  return response.data;
};

export const updateTeacher = async (id, teacher) => {
  const response = await api.put(`/teachers/${id}`, teacher);
  return response.data;
};

export const deleteTeacher = async (id) => {
  await api.delete(`/teachers/${id}`);
};