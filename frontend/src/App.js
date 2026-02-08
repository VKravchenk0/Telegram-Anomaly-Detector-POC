import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navigation from './components/Navigation';
import Dashboard from './pages/Dashboard';
import GroupManagement from './pages/GroupManagement';
import PipelineManagement from './pages/PipelineManagement';
import './styles/index.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navigation />
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/groups" element={<GroupManagement />} />
          <Route path="/pipeline" element={<PipelineManagement />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
