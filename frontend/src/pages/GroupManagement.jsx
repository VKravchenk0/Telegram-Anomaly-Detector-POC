import React, { useState, useEffect } from 'react';
import { groupAPI } from '../services/api';
import './GroupManagement.css';

const GroupManagement = () => {
  const [groups, setGroups] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    link: '',
    latitude: 50.4501,
    longitude: 30.5234,
  });
  const [editingId, setEditingId] = useState(null);

  useEffect(() => {
    loadGroups();
  }, []);

  const loadGroups = async () => {
    try {
      const response = await groupAPI.getAll();
      setGroups(response.data);
    } catch (error) {
      console.error('Error loading groups:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await groupAPI.update(editingId, formData);
      } else {
        await groupAPI.create(formData);
      }
      loadGroups();
      resetForm();
    } catch (error) {
      console.error('Error saving group:', error);
    }
  };

  const handleEdit = (group) => {
    setFormData({
      name: group.name,
      link: group.link,
      latitude: group.latitude,
      longitude: group.longitude,
    });
    setEditingId(group.id);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this group?')) {
      try {
        await groupAPI.delete(id);
        loadGroups();
      } catch (error) {
        console.error('Error deleting group:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({ name: '', link: '', latitude: 50.4501, longitude: 30.5234 });
    setEditingId(null);
  };

  return (
    <div className="group-management">
      <h1>Group Management</h1>
      
      <form onSubmit={handleSubmit} className="group-form">
        <input
          type="text"
          placeholder="Group Name"
          value={formData.name}
          onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          required
        />
        <input
          type="text"
          placeholder="Group Link"
          value={formData.link}
          onChange={(e) => setFormData({ ...formData, link: e.target.value })}
          required
        />
        <input
          type="number"
          step="0.0001"
          placeholder="Latitude"
          value={formData.latitude}
          onChange={(e) => setFormData({ ...formData, latitude: parseFloat(e.target.value) })}
          required
        />
        <input
          type="number"
          step="0.0001"
          placeholder="Longitude"
          value={formData.longitude}
          onChange={(e) => setFormData({ ...formData, longitude: parseFloat(e.target.value) })}
          required
        />
        <div className="form-actions">
          <button type="submit">{editingId ? 'Update' : 'Create'}</button>
          {editingId && <button type="button" onClick={resetForm}>Cancel</button>}
        </div>
      </form>

      <table className="groups-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Link</th>
            <th>Latitude</th>
            <th>Longitude</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {groups.map((group) => (
            <tr key={group.id}>
              <td>{group.name}</td>
              <td>
                <a href={group.link} target="_blank" rel="noopener noreferrer">
                  {group.link}
                </a>
              </td>
              <td>{group.latitude.toFixed(4)}</td>
              <td>{group.longitude.toFixed(4)}</td>
              <td>
                <button onClick={() => handleEdit(group)}>Edit</button>
                <button onClick={() => handleDelete(group.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default GroupManagement;
