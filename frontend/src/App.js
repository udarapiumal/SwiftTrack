import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Signup from "./Components/Sign_up";
import Login from "./Components/Login"; // create this component
import "./App.css";
import ClientDashboard from "./Components/ClientDashboard";

function App() {
  return (
    <Router>
      <Routes>
      <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<ClientDashboard/>} />
        <Route path="/" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;
