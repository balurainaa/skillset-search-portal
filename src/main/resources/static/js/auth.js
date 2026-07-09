const BASE_URL = "http://localhost:8080";

function getStoredToken() { return localStorage.getItem("jwt_token"); }
function getStoredEmail() { return localStorage.getItem("jwt_email"); }
function getStoredRole() { return localStorage.getItem("jwt_role"); }
function getStoredEmployeeId() { return localStorage.getItem("jwt_employeeId"); }

function getAuthHeader() {
    const token = getStoredToken();
    return token ? "Bearer " + token : null;
}

function setSession(token, email, role, employeeId) {
    localStorage.setItem("jwt_token", token);
    localStorage.setItem("jwt_email", email);
    localStorage.setItem("jwt_role", role || "");
    localStorage.setItem("jwt_employeeId", employeeId != null ? employeeId : "");
}

function clearSession() {
    localStorage.removeItem("jwt_token");
    localStorage.removeItem("jwt_email");
    localStorage.removeItem("jwt_role");
    localStorage.removeItem("jwt_employeeId");
    window.location.href = "login.html";
}

// Call at the top of dashboard.html / admin.html to block access if not logged in
function requireAuth() {
    if (!getStoredToken()) {
        window.location.href = "login.html";
    }
}

// Call at the top of admin.html to block non-admins from that page
function requireAdmin() {
    requireAuth();
    if (getStoredRole() !== "ADMIN") {
        window.location.href = "dashboard.html";
    }
}

// Shows "Logged in as: email (ROLE)" and wires up the Logout button.
// Expects #authStatus and #logoutBtn elements on the page.
function initNavbar() {
    const statusEl = document.getElementById("authStatus");
    const logoutBtn = document.getElementById("logoutBtn");
    const token = getStoredToken();

    if (statusEl) {
        if (token) {
            const roleLabel = getStoredRole() ? " (" + getStoredRole() + ")" : "";
            statusEl.textContent = "Logged in as: " + getStoredEmail() + roleLabel;
        } else {
            statusEl.textContent = "Not logged in";
        }
    }

    if (logoutBtn) {
        logoutBtn.addEventListener("click", clearSession);
    }
}

function showMsg(el, text, isError) {
    el.textContent = text;
    el.className = "msg mt-3 " + (isError ? "text-danger" : "text-success");
}

function csvToList(value) {
    return value.split(",").map(s => s.trim()).filter(s => s.length > 0);
}

async function postJson(url, body, msgEl, onSuccess, auth = true) {
    try {
        const headers = { "Content-Type": "application/json" };
        if (auth && getAuthHeader()) headers["Authorization"] = getAuthHeader();
        const response = await fetch(url, { method: "POST", headers, body: JSON.stringify(body) });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                showMsg(msgEl, "Not authorized. Please log in first.", true);
                return;
            }
            const firstError = typeof data === "object" ? Object.values(data)[0] : null;
            showMsg(msgEl, firstError || "Something went wrong", true);
            return;
        }
        onSuccess(data);
    } catch (err) {
        showMsg(msgEl, "Could not reach the server. Is it running on port 8080?", true);
    }
}

async function getJson(url, msgEl, onSuccess, auth = true) {
    try {
        const headers = {};
        if (auth && getAuthHeader()) headers["Authorization"] = getAuthHeader();
        const response = await fetch(url, { headers });
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                showMsg(msgEl, "Not authorized. Please log in first.", true);
                return;
            }
            showMsg(msgEl, "Request failed.", true);
            return;
        }
        onSuccess(data);
    } catch (err) {
        showMsg(msgEl, "Could not reach the server. Is it running on port 8080?", true);
    }
}
