import React, { useEffect, useState } from "react";
import axios from "axios";
import "../css/Dashboard.css";

export default function Dashboard() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [items, setItems] = useState("");
  const [cancellingOrderId, setCancellingOrderId] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  const clientId = localStorage.getItem("clientId");
  const token = localStorage.getItem("token");
  const userName = localStorage.getItem("userName") || "Customer";

  useEffect(() => {
    fetchOrders();
  }, [clientId, token]);

  const fetchOrders = async () => {
    try {
      if (!token || !clientId) {
        setError("You are not logged in. Please login first.");
        setLoading(false);
        return;
      }
      
      const res = await axios.get(
        `http://localhost:8081/cms/client/${clientId}/orders`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setOrders(res.data);
    } catch (err) {
      setError("Failed to fetch orders");
    } finally {
      setLoading(false);
    }
  };

  // SUBMIT ORDER FUNCTION
  const handleSubmitOrder = async (e) => {
    e.preventDefault();
    
    if (!items.trim()) {
      alert("Please enter at least one item");
      return;
    }

    setSubmitting(true);
    try {
      // Create SOAP request XML
      const soapRequest = `<?xml version="1.0" encoding="utf-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:cms="http://example.com/cms">
  <soapenv:Header/>
  <soapenv:Body>
    <cms:SubmitOrderRequest>
      <orderId>${Date.now()}</orderId>
      <clientId>${clientId}</clientId>
      <items>${items.trim()}</items>
    </cms:SubmitOrderRequest>
  </soapenv:Body>
</soapenv:Envelope>`;

      console.log("Sending SOAP request:", soapRequest);

      // Send SOAP request to the endpoint you provided
      const response = await axios.post(
        "http://localhost:8080/services/CmsService",
        soapRequest,
        {
          headers: {
            "Content-Type": "text/xml; charset=utf-8",
            "SOAPAction": "", // Add appropriate SOAPAction if needed by your service
            "Authorization": `Bearer ${token}`
          }
        }
      );

      console.log("Order submitted successfully:", response.data);
      
      alert("Order submitted successfully!");
      setShowPopup(false);
      setItems("");
      
      // Refresh orders list after a short delay to allow backend processing
      setTimeout(() => {
        fetchOrders();
      }, 1000);
      
    } catch (err) {
      console.error("Submit order failed:", err);
      alert("Failed to submit order: " + (err.response?.data || err.message));
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancelOrder = async (orderId) => {
    if (!window.confirm("Are you sure you want to cancel this order?")) {
      return;
    }

    setCancellingOrderId(orderId);
    try {
      const response = await axios.post(
        `http://localhost:8081/cms/order/cancel?orderId=${orderId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.data.includes("successfully") || response.data.includes("canceled")) {
        alert("Order cancelled successfully!");
        await fetchOrders();
      } else {
        alert("Failed to cancel order: " + response.data);
      }
    } catch (err) {
      console.error("Cancel order failed:", err);
      alert("Failed to cancel order: " + (err.response?.data || err.message));
    } finally {
      setCancellingOrderId(null);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("clientId");
    localStorage.removeItem("userName");
    window.location.href = "/login";
  };

  const getStatusBadgeClass = (status) => {
    switch (status.toLowerCase()) {
      case "submitted": return "status-pending";
      case "pending": return "status-pending";
      case "processing": return "status-processing";
      case "completed": return "status-completed";
      case "cancelled": return "status-cancelled";
      default: return "status-pending";
    }
  };

  const canCancelOrder = (status) => {
    const statusLower = status.toLowerCase();
    return statusLower === "submitted" || statusLower === "pending" || statusLower === "processing";
  };

  // Calculate statistics
  const totalOrders = orders.length;
  const pendingOrders = orders.filter(order => order.status === "PENDING").length;
  const completedOrders = orders.filter(order => order.status === "COMPLETED").length;

  if (loading) return <div className="loading">Loading orders...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Order Management Dashboard</h1>
        <div className="user-info">
          <div className="user-welcome">Welcome, {userName}</div>
          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>

      <div className="dashboard-content">
        <div className="sidebar">
          <div className="sidebar-section">
            <h3>Order Statistics</h3>
            <div className="sidebar-stats">
              <div className="stat-item">
                <span className="stat-label">Total Orders</span>
                <span className="stat-value">{totalOrders}</span>
              </div>
              <div className="stat-item">
                <span className="stat-label">Pending</span>
                <span className="stat-value">{pendingOrders}</span>
              </div>
              <div className="stat-item">
                <span className="stat-label">Completed</span>
                <span className="stat-value">{completedOrders}</span>
              </div>
              <div className="stat-item">
                <span className="stat-label">Cancelled</span>
                <span className="stat-value">
                  {orders.filter(order => order.status === "CANCELLED").length}
                </span>
              </div>
            </div>
          </div>
          
          <div className="sidebar-section">
            <h3>Quick Actions</h3>
            <button 
              className="submit-order-btn" 
              onClick={() => setShowPopup(true)}
              style={{width: '100%', justifyContent: 'center'}}
            >
              + New Order
            </button>
          </div>
        </div>

        <div className="main-content">
          <div className="content-header">
            <h2 className="content-title">Recent Orders</h2>
            <button className="submit-order-btn" onClick={() => setShowPopup(true)}>
              + Submit New Order
            </button>
          </div>

          <div className="orders-table-container">
            {orders.length === 0 ? (
              <div className="no-orders">No orders found. Submit your first order!</div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Order ID</th>
                    <th>Items</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map((order) => (
                    <tr key={order.orderId} className={cancellingOrderId === order.orderId ? "cancelling" : ""}>
                      <td>#{order.orderId}</td>
                      <td>{order.items}</td>
                      <td>
                        <span className={`status-badge ${getStatusBadgeClass(order.status)}`}>
                          {order.status}
                        </span>
                      </td>
                      <td>
                        <button
                          className={`action-btn cancel-btn ${!canCancelOrder(order.status) ? 'disabled' : ''}`}
                          onClick={() => handleCancelOrder(order.orderId)}
                          disabled={!canCancelOrder(order.status) || cancellingOrderId === order.orderId}
                        >
                          {cancellingOrderId === order.orderId ? 'Cancelling...' : 'Cancel'}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>

      {/* Popup Form */}
      {showPopup && (
        <div className="popup-overlay">
          <div className="popup">
            <h3>Submit New Order</h3>
            <form onSubmit={handleSubmitOrder}>
              <label>Items (comma separated)</label>
              <input
                type="text"
                value={items}
                onChange={(e) => setItems(e.target.value)}
                placeholder="e.g., Product A, Product B, Product C"
                required
                disabled={submitting}
              />
              <div className="popup-buttons">
                <button 
                  type="button" 
                  onClick={() => setShowPopup(false)}
                  disabled={submitting}
                >
                  Cancel
                </button>
                <button type="submit" disabled={submitting}>
                  {submitting ? 'Submitting...' : 'Submit Order'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}