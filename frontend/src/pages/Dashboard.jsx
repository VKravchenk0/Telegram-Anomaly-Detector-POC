import React, { useState, useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import { notificationAPI } from '../services/api';
import './Dashboard.css';

// Custom animated marker icon
const createBlinkingIcon = (isNew) => {
  return L.divIcon({
    className: isNew ? 'blink-marker new' : 'blink-marker',
    html: '<div class="marker-dot"></div>',
    iconSize: [20, 20],
    iconAnchor: [10, 10],
  });
};

const Dashboard = () => {
  const [notifications, setNotifications] = useState([]);
  const [newNotificationIds, setNewNotificationIds] = useState(new Set());
  const eventSourceRef = useRef(null);

  useEffect(() => {
    loadHistory();
    connectSSE();

    return () => {
      if (eventSourceRef.current) {
        eventSourceRef.current.close();
      }
    };
  }, []);

  const loadHistory = async () => {
    try {
      const response = await notificationAPI.getHistory();
      setNotifications(response.data);
    } catch (error) {
      console.error('Error loading history:', error);
    }
  };

  const connectSSE = () => {
    const eventSource = new EventSource(notificationAPI.subscribe());
    eventSourceRef.current = eventSource;

    eventSource.onmessage = (event) => {
      const notification = JSON.parse(event.data);
      console.log('Received notification:', notification);
      
      setNotifications((prev) => [notification, ...prev]);
      setNewNotificationIds((prev) => new Set(prev).add(notification.id));

      // Remove blink effect after 2 seconds
      setTimeout(() => {
        setNewNotificationIds((prev) => {
          const newSet = new Set(prev);
          newSet.delete(notification.id);
          return newSet;
        });
      }, 2000);
    };

    eventSource.onerror = (error) => {
      console.error('SSE error:', error);
      eventSource.close();
      
      // Reconnect after 5 seconds
      setTimeout(() => {
        console.log('Reconnecting to SSE...');
        connectSSE();
      }, 5000);
    };
  };

  return (
    <div className="dashboard">
      <h1>Dashboard</h1>
      
      <div className="dashboard-content">
        <div className="map-container">
          <MapContainer
            center={[49.0, 32.0]}
            zoom={6}
            className="notification-map"
          >
            <TileLayer
              url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            />
            {notifications.map((notif) => (
              <Marker
                key={notif.id}
                position={[notif.latitude, notif.longitude]}
                icon={createBlinkingIcon(newNotificationIds.has(notif.id))}
              >
                <Popup>
                  <div className="notification-popup">
                    <h3>{notif.groupName}</h3>
                    <p><strong>Keyword:</strong> {notif.keyword}</p>
                    <p><strong>Content:</strong> {notif.content}</p>
                    <p><strong>Time:</strong> {new Date(notif.timestamp).toLocaleString()}</p>
                    <a href={notif.groupLink} target="_blank" rel="noopener noreferrer">
                      Open Group
                    </a>
                  </div>
                </Popup>
              </Marker>
            ))}
          </MapContainer>
        </div>

        <div className="notifications-table-container">
          <h2>Recent Notifications</h2>
          <table className="notifications-table">
            <thead>
              <tr>
                <th>Time</th>
                <th>Group</th>
                <th>Keyword</th>
                <th>Content</th>
                <th>Location</th>
              </tr>
            </thead>
            <tbody>
              {notifications.map((notif) => (
                <tr
                  key={notif.id}
                  className={newNotificationIds.has(notif.id) ? 'new-notification' : ''}
                >
                  <td>{new Date(notif.timestamp).toLocaleTimeString()}</td>
                  <td>
                    <a href={notif.groupLink} target="_blank" rel="noopener noreferrer">
                      {notif.groupName}
                    </a>
                  </td>
                  <td><span className="keyword-badge">{notif.keyword}</span></td>
                  <td>{notif.content}</td>
                  <td>{notif.latitude.toFixed(2)}, {notif.longitude.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
