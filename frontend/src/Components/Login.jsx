import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../css/Sign_up.css"; // reuse same theme

export default function Login() {
  const [form, setForm] = useState({ username: "", password: "" });
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Backend should return { token, clientId }
      const res = await axios.post("http://localhost:8084/auth/login", form);

      // Destructure token and clientId from response
      const { token, clientId } = res.data;

      if (!token || !clientId) {
        setMessage("Login failed: Invalid response from server");
        return;
      }

      // Save token + clientId to localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("clientId", clientId);

      setMessage("Login successful!");

      // Redirect to dashboard
      navigate("/dashboard");
    } catch (err) {
      if (err.response) {
        setMessage(err.response.data);
      } else {
        setMessage("Something went wrong.");
      }
    }
  };

  return (
    <div className="signup-wrapper">
      <form className="signup-card" onSubmit={handleSubmit}>
        <h2>Login</h2>

        <label htmlFor="username">Username</label>
        <input
          id="username"
          name="username"
          value={form.username}
          onChange={handleChange}
          placeholder="your username"
        />

        <label htmlFor="password">Password</label>
        <input
          id="password"
          type="password"
          name="password"
          value={form.password}
          onChange={handleChange}
          placeholder="your password"
        />

        <button type="submit" className="signup-btn">Login</button>
        {message && <p className="signup-message">{message}</p>}
      </form>
    </div>
  );
}
