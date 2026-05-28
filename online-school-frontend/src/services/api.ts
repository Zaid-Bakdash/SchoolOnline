import axios from 'axios';

// Base API configuration
const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('authUser');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Types
export interface Student {
  id?: number;
  name: string;
  email: string;
  studentId: string;
  enrollmentDate: string;
  classIds?: number[];
  registrationIds?: number[];
  createdAt?: string;
  updatedAt?: string;
}

export interface Teacher {
  id?: number;
  name: string;
  email: string;
  employeeId: string;
  department: string;
  hireDate: string;
  classIds?: number[];
  createdAt?: string;
  updatedAt?: string;
}

export interface Course {
  id?: number;
  name: string;
  description: string;
  credits: number;
  duration: number;
  classIds?: number[];
  registrationIds?: number[];
  createdAt?: string;
  updatedAt?: string;
}

export interface Class {
  id?: number;
  name: string;
  semester: string;
  year: number;
  maxCapacity: number;
  teacherId: number;
  teacherName?: string;
  studentIds?: number[];
  courseIds?: number[];
  createdAt?: string;
  updatedAt?: string;
}

export interface Registration {
  id?: number;
  registrationDate: string;
  status: 'ACTIVE' | 'COMPLETED' | 'DROPPED' | 'PENDING';
  grade?: string;
  studentId: number;
  courseId: number;
  studentName?: string;
  courseName?: string;
  createdAt?: string;
  updatedAt?: string;
}

// API Services
export const studentService = {
  getAll: () => api.get<Student[]>('/students'),
  getById: (id: number) => api.get<Student>(`/students/${id}`),
  getByStudentId: (studentId: string) => api.get<Student>(`/students/studentId/${studentId}`),
  getByClass: (classId: number) => api.get<Student[]>(`/students/class/${classId}`),
  create: (student: Omit<Student, 'id'>) => api.post<Student>('/students', student),
  update: (id: number, student: Omit<Student, 'id'>) => api.put<Student>(`/students/${id}`, student),
  delete: (id: number) => api.delete(`/students/${id}`),
  enrollInClass: (studentId: number, classId: number) => api.post<Student>(`/students/${studentId}/enroll/${classId}`),
  removeFromClass: (studentId: number, classId: number) => api.delete<Student>(`/students/${studentId}/enroll/${classId}`),
  existsByStudentId: (studentId: string) => api.get<boolean>(`/students/exists/studentId/${studentId}`),
};

export const teacherService = {
  getAll: () => api.get<Teacher[]>('/teachers'),
  getById: (id: number) => api.get<Teacher>(`/teachers/${id}`),
  getByEmployeeId: (employeeId: string) => api.get<Teacher>(`/teachers/employeeId/${employeeId}`),
  create: (teacher: Omit<Teacher, 'id'>) => api.post<Teacher>('/teachers', teacher),
  update: (id: number, teacher: Omit<Teacher, 'id'>) => api.put<Teacher>(`/teachers/${id}`, teacher),
  delete: (id: number) => api.delete(`/teachers/${id}`),
  existsByEmployeeId: (employeeId: string) => api.get<boolean>(`/teachers/exists/employeeId/${employeeId}`),
};

export const courseService = {
  getAll: () => api.get<Course[]>('/courses'),
  getById: (id: number) => api.get<Course>(`/courses/${id}`),
  create: (course: Omit<Course, 'id'>) => api.post<Course>('/courses', course),
  update: (id: number, course: Omit<Course, 'id'>) => api.put<Course>(`/courses/${id}`, course),
  delete: (id: number) => api.delete(`/courses/${id}`),
  existsByName: (name: string) => api.get<boolean>(`/courses/exists/name/${name}`),
};

export const classService = {
  getAll: () => api.get<Class[]>('/classes'),
  getById: (id: number) => api.get<Class>(`/classes/${id}`),
  getByTeacher: (teacherId: number) => api.get<Class[]>(`/classes/teacher/${teacherId}`),
  create: (clazz: Omit<Class, 'id'>) => api.post<Class>('/classes', clazz),
  update: (id: number, clazz: Omit<Class, 'id'>) => api.put<Class>(`/classes/${id}`, clazz),
  delete: (id: number) => api.delete(`/classes/${id}`),
  addCourse: (classId: number, courseId: number) => api.post<Class>(`/classes/${classId}/courses/${courseId}`),
  removeCourse: (classId: number, courseId: number) => api.delete<Class>(`/classes/${classId}/courses/${courseId}`),
};

export const registrationService = {
  getAll: () => api.get<Registration[]>('/registrations'),
  getById: (id: number) => api.get<Registration>(`/registrations/${id}`),
  getByStudent: (studentId: number) => api.get<Registration[]>(`/registrations/student/${studentId}`),
  getByCourse: (courseId: number) => api.get<Registration[]>(`/registrations/course/${courseId}`),
  getByStatus: (status: string) => api.get<Registration[]>(`/registrations/status/${status}`),
  create: (registration: Omit<Registration, 'id'>) => api.post<Registration>('/registrations', registration),
  update: (id: number, registration: Omit<Registration, 'id'>) => api.put<Registration>(`/registrations/${id}`, registration),
  delete: (id: number) => api.delete(`/registrations/${id}`),
};

export default api;