import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import "../css/Sign_up.css";

export default function Signup() {
  const [form, setForm] = useState({ username: "", email: "", password: "" });
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await axios.post("http://localhost:8084/auth/signup", form);
      setMessage(res.data);

      // redirect to login after 1.5 seconds
      setTimeout(() => navigate("/login"), 1500);
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
        <h2>Create account</h2>

        <input
          type="text"
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
        />

        <button type="submit">Sign Up</button>

        {message && (
          <p
            style={{
              textAlign: "center",
              marginTop: "0.5rem",
              color: "#fff",
            }}
          >
            {message}
          </p>
        )}

        <p style={{ marginTop: "1rem", textAlign: "center" }}>
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </form>
    </div>
  );
}
