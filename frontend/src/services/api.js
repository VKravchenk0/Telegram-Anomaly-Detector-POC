import axios from 'axios';

const API_BASE = process.env.REACT_APP_API_BASE || 'http://localhost:8081';
const NOTIFICATION_BASE = process.env.REACT_APP_NOTIFICATION_BASE || 'http://localhost:8082';

const api = axios.create({
  baseURL: API_BASE,
});

// Group Management API
export const groupAPI = {
  getAll: () => api.get('/api/groups'),
  getById: (id) => api.get(`/api/groups/${id}`),
  create: (data) => api.post('/api/groups', data),
  update: (id, data) => api.put(`/api/groups/${id}`, data),
  delete: (id) => api.delete(`/api/groups/${id}`),
};

// Zone Management API
export const zoneAPI = {
  getActive: () => api.get('/api/zone'),
  set: (data) => api.post('/api/zone', data),
};

// Notification API
export const notificationAPI = {
  getHistory: () => axios.get(`${NOTIFICATION_BASE}/api/notifications/history`),
  subscribe: () => `${NOTIFICATION_BASE}/api/notifications`,
};

export default api;
