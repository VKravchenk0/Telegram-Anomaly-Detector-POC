import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Navigation.css';

const Navigation = () => {
  const location = useLocation();
  
  const isActive = (path) => location.pathname === path;
  
  return (
    <nav className="navigation">
      <div className="nav-brand">
        <h2>Telegram Monitoring</h2>
      </div>
      <div className="nav-links">
        <Link 
          to="/" 
          className={isActive('/') ? 'active' : ''}
        >
          Dashboard
        </Link>
        <Link 
          to="/groups" 
          className={isActive('/groups') ? 'active' : ''}
        >
          Group Management
        </Link>
        <Link 
          to="/pipeline" 
          className={isActive('/pipeline') ? 'active' : ''}
        >
          Pipeline Management
        </Link>
      </div>
    </nav>
  );
};

export default Navigation;
